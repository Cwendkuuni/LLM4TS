import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.SimpleDefinitionFinder.DefinitionSite;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeNative;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DevirtualizePrototypeMethodsTest {

  @Mock
  private Compiler compiler;

  @Mock
  private SimpleDefinitionFinder simpleDefinitionFinder;

  @Mock
  private Definition definition;

  @Mock
  private JSModule definitionModule;

  @Mock
  private UseSite useSite;

  @Mock
  private JSModule callModule;

  @Mock
  private Node node;

  @Mock
  private Node parent;

  @Mock
  private Node functionNode;

  @Mock
  private Node expr;

  @Mock
  private Node block;

  @Mock
  private Node newNameNode;

  @Mock
  private Node rValue;

  @Mock
  private Node lValue;

  @Mock
  private Node argList;

  @Mock
  private Node body;

  @Mock
  private Node objectNode;

  @Mock
  private Node getPropNode;

  private DevirtualizePrototypeMethods devirtualizePrototypeMethods;

  @Before
  public void setup() {
    devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
  }

  @Test
  public void testIsCall() {
    assert(DevirtualizePrototypeMethods.isCall(useSite));
    assertFalse(DevirtualizePrototypeMethods.isCall(new Node()));
  }

  @Test
  public void testIsPrototypeMethodDefinition() {
    assertFalse(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(new Node()));
    assertFalse(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(node));
    assertFalse(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(
        NodeUtil.getFunctionNode(new Node())));
    assertFalse(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(
        NodeUtil.getFunctionNode(node)));
    assertFalse(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(
        NodeUtil.getFunctionNode(NodeUtil.getFunctionNode(node))));
    assertTrue(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(
        NodeUtil.getFunctionNode(NodeUtil.getSetPropNode(node, new Node()))));
  }

  @Test
  public void testGetRewrittenMethodName() {
    assertSame("JSCompiler_StaticMethods_accumulate",
        devirtualizePrototypeMethods.getRewrittenMethodName("accumulate"));
  }

  @Test
  public void testIsEligibleDefinition() {
    when(simpleDefinitionFinder.getDefinitionSites(node)).thenReturn(
        Lists.newArrayList(definitionSite(definition, definitionModule, node)));
    when(definitionSite(definition, definitionModule, node).node).thenReturn(node);

    assertTrue(devirtualizePrototypeMethods.isEligibleDefinition(simpleDefinitionFinder, definitionSite(definition, definitionModule, node)));
  }

  @Test
  public void testIsEligibleDefinition_inExterns() {
    when(definitionSite(definition, definitionModule, node).node).thenReturn(node);
    when(definition.getInExterns()).thenReturn(true);

    assertFalse(devirtualizePrototypeMethods.isEligibleDefinition(simpleDefinitionFinder, definitionSite(definition, definitionModule, node)));
  }

  @Test
  public void testIsEligibleDefinition_inGlobalScope() {
    when(definitionSite(definition, definitionModule, node).node).thenReturn(node);
    when(definition.getInGlobalScope()).thenReturn(false);

    assertFalse(devirtualizePrototypeMethods.isEligibleDefinition(simpleDefinitionFinder, definitionSite(definition, definitionModule, node)));
  }

  @Test
  public void testIsEligibleDefinition_notPrototypeMethod() {
    when(definitionSite(definition, definitionModule, node).node).thenReturn(node);
    when(node.getParent()).thenReturn(parent);
    when(parent.getParent()).thenReturn(expr);
    when(expr.getParent()).thenReturn(block);

    assertFalse(devirtualizePrototypeMethods.isEligibleDefinition(simpleDefinitionFinder, definitionSite(definition, definitionModule, node)));
  }

  @Test
  public void testIsEligibleDefinition_exported() {
    when(definitionSite(definition, definitionModule, node).node).thenReturn(node);
    when(definition.getRValue()).thenReturn(rValue);
    when(definition.getLValue()).thenReturn(lValue);
    when(lValue.getString()).thenReturn("foo");
    when(compiler.getCodingConvention().isExported(lValue.getLastChild())).thenReturn(true);

    assertFalse(devirtualizePrototypeMethods.isEligibleDefinition(simpleDefinitionFinder, definitionSite(definition, definitionModule, node)));
  }

  @Test
  public void testIsEligibleDefinition_usedOnce() {
    when(definitionSite(definition, definitionModule, node).node).thenReturn(node);
    when(definition.getRValue()).thenReturn(rValue);
    when(definition.getLValue()).thenReturn(lValue);
    when(lValue.getString()).thenReturn("foo");
    when(simpleDefinitionFinder.getUseSites(definition)).thenReturn(Lists.newArrayList());

    assertFalse(devitationalizePrototypeMethods.isEligibleDefinition(simpleDefinitionFinder, definitionSite(definition, definitionModule, node)));
  }

  @Test
  public void testIsEligibleDefinition_usedOutsideFunctionContext() {
    when(definitionSite(definition, definitionModule, node).node).thenReturn(node);
    when(definition.getRValue()).thenReturn(rValue);
    when(definition.getLValue()).thenReturn(lValue);
    when(lValue.getString()).thenReturn("foo");
    when(simpleDefinitionFinder.getUseSites(definition)).thenReturn(Lists.newArrayList(useSite));
    when(isCall(useSite)).thenReturn(false);

    assertFalse(devitationalizePrototypeMethods.isEligibleDefinition(simpleDefinitionFinder, definitionSite(definition, definitionModule, node)));
  }

  @Test
  public void testIsEligibleDefinition_multipleDefinitions() {
    when(definitionSite(definition, definitionModule, node).node).thenReturn(node);
    when(definitionSite(definition, definitionModule, node).definition).thenReturn(definition);
    when(simpleDefinitionFinder.getDefinitionsReferencedAt(node)).thenReturn(Lists.newArrayList(definition, new Definition()));

    assertFalse(devitationalizePrototypeMethods.isEligibleDefinition(simpleDefinitionFinder, definitionSite(definition, definitionModule, node)));
  }

  @Test
  public void testIsEligibleDefinition_dependsOnCallModule() {
    when(definitionSite(definition, definitionModule, node).node).thenReturn(node);
    when(definitionSite(definition, definitionModule, node).definition).thenReturn(definition);
    when(simpleDefinitionFinder.getDefinitionsReferencedAt(node)).thenReturn(Lists.newArrayList(definition));
    when(definition.getModule()).thenReturn(definitionModule);
    when(definitionModule.getName()).thenReturn("module1");
    when(callModule.getName()).thenReturn("module2");
    when(simpleDefinitionFinder.getUseSites(definition)).thenReturn(Lists.newArrayList(useSite));
    when(useSite.getModule()).thenReturn(callModule);

    assertFalse(devitationalizePrototypeMethods.isEligibleDefinition(simpleDefinitionFinder, definitionSite(definition, definitionModule, node)));
  }

  @Test
  public void testIsEligibleDefinition_usedBeforeDefinition() {
    when(definitionSite(definition, definitionModule, node).node).thenReturn(node);
    when(definitionSite(definition, definitionModule, node).definition).thenReturn(definition);
    when(simpleDefinitionFinder.getDefinitionsReferencedAt(node)).thenReturn(Lists.newArrayList(definition));
    when(definition.getModule()).thenReturn(definitionModule);
    when(definitionModule.getName()).thenReturn("module1");
    when(simpleDefinitionFinder.getUseSites(definition)).thenReturn(Lists.newArrayList(useSite));
    when(useSite.getModule()).thenReturn(callModule);
    when(callModule.getName()).thenReturn("module0");

    assertFalse(devitationalizePrototypeMethods.isEligibleDefinition(simpleDefinitionFinder, definitionSite(definition, definitionModule, node)));
  }

  @Test
  public void testRewriteDefinitionIfEligible() {
    when(simpleDefinitionFinder.getDefinitionSites(node)).thenReturn(
        Lists.newArrayList(definitionSite(definition, definitionModule, node)));
    when(definitionSite(definition, definitionModule, node).node).thenReturn(node);
    when(definition.getModule()).thenReturn(definitionModule);
    when(definitionModule.getName()).thenReturn("module1");

    devirtualizePrototypeMethods.rewriteDefinitionIfEligible(simpleDefinitionFinder, definitionSite(definition, definitionModule, node));

    verify(node).replaceChild(functionNode, newNameNode);
    verify(parent).addChildToFront(newNameNode);
    verify(functionNode).addChildToFront(argList);
    verify(functionNode).getLastChild().setJSType(JSTypeRegistry.createFunctionType(
        JSTypeRegistry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE),
        JSType.UNKNOWN_TYPE, Lists.newArrayList(JSTypeRegistry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), JSTypeRegistry.getTypeOf(String.class), JSTypeRegistry.getTypeOf(String.class), JSTypeRegistry.getTypeOf(String.class))));
    verify(compiler).reportCodeChange();
  }

  @Test
  public void testRewriteCallSites() {
    when(simpleDefinitionFinder.getDefinitionSites(node)).thenReturn(
        Lists.newArrayList(definitionSite(definition, definitionModule, node)));
    when(definitionSite(definition, definitionModule, node).node).thenReturn(node);
    when(definitionSite(definition, definitionModule, node).definition).thenReturn(definition);
    when(simpleDefinitionFinder.getUseSites(definition)).thenReturn(Lists.newArrayList(useSite));
    when(useSite.node).thenReturn(node);
    when(node.getParent()).thenReturn(parent);
    when(parent.getParent()).thenReturn(expr);
    when(expr.getParent()).thenReturn(block);
    when(definition.getModule()).thenReturn(definitionModule);
    when(definitionModule.getName()).thenReturn("module1");
    when(callModule.getName()).thenReturn("module0");

    devirtualizePrototypeMethods.rewriteDefinitionIfEligible(simpleDefinitionFinder, definitionSite(definition, definitionModule, node));

    verify(parent).replaceChild(node, newNameNode);
    verify(parent).addChildToFront(Node.newString(Token.NAME, "JSCompiler_StaticMethods_accumulate"));
  }

  @Test
  public void testFixFunctionType() {
    when(functionNode.getJSType()).thenReturn(FunctionType.create(
        null, null, null, null, JSTypeRegistry.createFunctionType(
            JSTypeRegistry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE),
            JSTypeRegistry.getTypeOf(String.class),
            Lists.newArrayList(JSTypeRegistry.getTypeOf(String.class), JSTypeRegistry.getTypeOf(String.class), JSTypeRegistry.getTypeOf(String.class)))));

    devirtualizePrototypeMethods.fixFunctionType(functionNode);

    verify(functionNode).setJSType(JSTypeRegistry.createFunctionType(
        JSTypeRegistry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE),
        null,
        Lists.newArrayList(JSTypeRegistry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE),
            JSTypeRegistry.getTypeOf(String.class),
            JSTypeRegistry.getTypeOf(String.class),
            JSTypeRegistry.getTypeOf(String.class))));
  }

  private DefinitionSite definitionSite(Definition definition, JSModule definitionModule, Node node) {
    return new DefinitionSite() {
      @Override
      public Definition getDefinition() {
        return definition;
      }

      @Override
      public Node getNode() {
        return node;
      }

      @Override
      public JSModule getModule() {
        return definitionModule;
      }
    };
  }

  // TODO(user): Add more test cases for edge cases and exceptions.
}