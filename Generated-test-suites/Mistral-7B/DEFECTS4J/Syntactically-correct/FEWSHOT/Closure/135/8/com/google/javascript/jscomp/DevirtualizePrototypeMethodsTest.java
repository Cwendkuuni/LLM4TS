package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.NodeUtil.isCall;
import static com.google.javascript.jscomp.NodeUtil.isGetProp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.DefinitionsRemover.DefinitionSite;
import com.google.javascript.jscomp. JaneNameReferenceGraph;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeNative;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;

public class DevirtualizePrototypeMethodsTest {

  @Test
  public void testIsCall() {
    Node node = NodeFactory.createCall(NodeFactory.createName("foo"), Lists.newArrayList());
    assertTrue(isCall(new UseSite(node, null)));

    node = NodeFactory.createName("foo");
    assertFalse(isCall(new UseSite(node, null)));
  }

  @Test
  public void testIsPrototypeMethodDefinition() {
    Node prototypeNode = NodeFactory.createCall(NodeFactory.createName("Object"), Lists.newArrayList(), Lists.newArrayList(
        NodeFactory.createName("prototype")
    ));

    Node prototypeMethodDefinition = NodeFactory.createGetProp(PrototypeNode, NodeFactory.createName("accumulate"));
    assertTrue(isPrototypeMethodDefinition(prototypeMethodDefinition));

    Node nonPrototypeMethodDefinition = NodeFactory.createGetProp(PrototypeNode, NodeFactory.createName("toString"));
    assertFalse(isPrototypeMethodDefinition(nonPrototypeMethodDefinition));
  }

  @Test
  public void testRewrittenMethodName() {
    String originalMethodName = "accumulate";
    String newMethodName = "JSCompiler_StaticMethods_" + originalMethodName;
    assertEquals(newMethodName, new DevirtualizePrototypeMethods().getRewrittenMethodName(originalMethodName));
  }

  @Test
  public void testSimpleDefinitionFinder() {
    Node externs = NodeFactory.createRootNode();
    Node root = NodeFactory.createRootNode();

    AbstractCompiler compiler = new Compiler();
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    DefinitionSite definitionSite = defFinder.getDefinitionSiteForName(root, "accumulate");
    assertNotNull(definitionSite);
  }

  @Test
  public void testIsEligibleDefinition() {
    Node externs = NodeFactory.createRootNode();
    Node root = NodeFactory.createRootNode();

    AbstractCompiler compiler = new Compiler();
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    DefinitionSite definitionSite = defFinder.getDefinitionSiteForName(root, "accumulate");
    assertTrue(new DevirtualizePrototypeMethods().isEligibleDefinition(defFinder, definitionSite));
  }

  @Test
  public void testRewriteDefinitionIfEligible() {
    Node externs = NodeFactory.createRootNode();
    Node root = NodeFactory.createRootNode();

    AbstractCompiler compiler = new Compiler();
    DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(externs, root);

    Node prototypeNode = NodeFactory.createCall(NodeFactory.createName("Object"), Lists.newArrayList(), Lists.newArrayList(
        NodeFactory.createName("prototype")
    ));
    Node accumulateMethodDefinition = NodeFactory.createGetProp(PrototypeNode, NodeFactory.createName("accumulate"));

    String newMethodName = "JSCompiler_StaticMethods_accumulate";
    String expectedRewrittenDefinition = "var " + newMethodName + " = function(self, value) {" +
        "  self.total += value; return self.total" +
        "}";

    assertEquals(expectedRewrittenDefinition, accumulateMethodDefinition.toString(Printer.plainTextPrinter()));
  }

  @Test
  public void testRewriteCallSites() {
    Node externs = NodeFactory.createRootNode();
    Node root = NodeFactory.createRootNode();

    AbstractCompiler compiler = new Compiler();
    DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(externs, root);

    DefinitionSite definitionSite = new SimpleDefinitionFinder(compiler).getDefinitionSiteForName(root, "accumulate");

    String newMethodName = "JSCompiler_StaticMethods_accumulate";
    String expectedRewrittenCallSite = newMethodName + "(" +
        "  this," +
        "  a," +
        "  b," +
        "  c" +
        ")";

    List<UseSite> useSites = Lists.newArrayList();
    UseSite callSite1 = new UseSite(NodeFactory.createCall(NodeFactory.createGetProp(NodeFactory.createName("a"), NodeFactory.createName("accumulate")), Lists.newArrayList(NodeFactory.createName("a"), NodeFactory.createNumber(1), NodeFactory.createNumber(2))), null);
    UseSite callSite2 = new UseSite(NodeFactory.createCall(NodeFactory.createGetProp(NodeFactory.createName("b"), NodeFactory.createName("accumulate")), Lists.newArrayList(NodeFactory.createName("b"), NodeFactory.createNumber(1), NodeFactory.createNumber(2))), null);

    useSites.add(callSite1);
    useSites.add(callSite2);

    for (UseSite useSite : useSites) {
      assertEquals(expectedRewrittenCallSite, useSite.node.toString(Printer.plainTextPrinter()));
    }
  }

  @Test
  public void testFixFunctionType() {
    Node functionNode = NodeFactory.createFunctionDeclaration("accumulate", NodeFactory.createList(NodeFactory.createName("self"), NodeFactory.createName("a"), NodeFactory.createName("b"), NodeFactory.createName("c")), NodeFactory.createBlock(NodeFactory.createReturn(NodeFactory.createName("self.total"))), NodeFactory.createEmptyStatement(), NodeFactory.createEmptyStatement());

    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    FunctionType originalFunctionType = NodeUtil.getFunctionType(functionNode);

    DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(new Compiler());
    devirtualizePrototypeMethods.fixFunctionType(functionNode);

    JSType rewrittenFunctionType = functionNode.getJSType();
    ObjectType thisType = typeRegistry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE);
    List<JSType> parameterTypes = Lists.newArrayList();
    parameterTypes.add(thisType);
    parameterTypes.addAll(originalFunctionType.getParameters().stream().map(n -> n.getJSType()).collect(Collectors.toList()));

    JSType expectedFunctionType = typeRegistry.createFunctionType(thisType, originalFunctionType.getReturnType(), parameterTypes);

    assertEquals(expectedFunctionType, rewrittenFunctionType);
  }
}

class UseSite {
  Node node;
  JSModule module;

  UseSite(Node node, JSModule module) {
    this.node = node;
    this.module = module;
  }
}

class Compiler extends AbstractCompiler {
  @Override
  public void reportCodeChange() {
    // empty implementation
  }
}

class SimpleDefinitionFinder extends AbstractCompilerPass {
  // ...
}