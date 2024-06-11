package com.example.junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeUtil.isCall;
import static com.google.javascript.jscomp.NodeUtil.isControlStructure;
import static com.google.javascript.jscomp.NodeUtil.isGetProp;
import static com.google.javascript.jscomp.NodeUtil.isPrototypeMethodDefinition;
import static com.google.javascript.jscomp.NodeUtil.isVarArgsFunction;
import static com.google.javascript.jscomp.SimpleDefinitionFinder.DefinitionSite;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.SimpleDefinitionFinder.SimpleDefinitionFinderImpl;
import com.google.javascript.rhino.NodeUtil;

/**
 * Tests for {@link DevirtualizePrototypeMethods} class.
 */
public class DevirtualizePrototypeMethodsTest {

  @Test
  public void testIsCall() {
    UseSite useSite = new UseSite(null, null, null, null, null, null);
    Node node = Node.newCall(null, null, Node.newIdentifier("foo"));
    useSite.node = node;

    assertTrue(isCall(useSite));

    node.setParent(Node.newStatement(Token.BLOCK, null, null));
    assertFalse(isCall(useSite));
  }

  @Test
  public void testIsPrototypeMethodDefinition() {
    Node prototypeNode = Node.newStatement(Token.ASSIGN, null, null);
    prototypeNode.setLastChild(Node.newGetProp(null, Node.newIdentifier("prototype")));
    prototypeNode.setParent(Node.newStatement(Token.ASSIGN, null, null));
    prototypeNode.getLastChild().setLastChild(Node.newStatement(Token.ASSIGN, null, null));
    Node methodNode = Node.newStatement(Token.ASSIGN, null, null);
    methodNode.setLastChild(Node.newGetProp(null, Node.newIdentifier("prototype")));
    methodNode.getLastChild().setLastChild(Node.newGetProp(null, Node.newIdentifier("MethodName")));
    methodNode.getLastChild().getLastChild().setLastChild(Node.newStatement(Token.ASSIGN, null, Node.newCall(null, null, Node.newIdentifier("function"))));

    prototypeNode.setParent(Node.newStatement(Token.ASSIGN, null, null));
    Node functionNode = methodNode.getLastChild().getLastChild().getLastChild();
    assertTrue(isPrototypeMethodDefinition(functionNode));
  }

  @Test
  public void testIsNotPrototypeMethodDefinition() {
    Node prototypeNode = Node.newStatement(Token.ASSIGN, null, null);
    prototypeNode.setLastChild(Node.newGetProp(null, Node.newIdentifier("prototype")));
    prototypeNode.setParent(Node.newStatement(Token.ASSIGN, null, null));
    prototypeNode.getLastChild().setLastChild(Node.newStatement(Token.ASSIGN, null, Node.newCall(null, null, Node.newIdentifier("function"))));

    Node functionNode = prototypeNode.getLastChild().getLastChild().getLastChild();
    assertFalse(isPrototypeMethodDefinition(functionNode));
  }

  @Test
  public void testGetRewrittenMethodName() {
    String originalMethodName = "MethodName";
    String expectedRewrittenMethodName = "JSCompiler_StaticMethods_" + originalMethodName;

    String rewrittenMethodName = new DevirtualizePrototypeMethods().getRewrittenMethodName(originalMethodName);
    assertEquals(expectedRewrittenMethodName, rewrittenMethodName);
  }

  @Test
  public void testRewriteDefinitionIfEligibleTrue() {
    SimpleDefinitionFinderImpl simpleDefinitionFinder = new SimpleDefinitionFinderImpl();

    Node prototypeNode = Node.newStatement(Token.ASSIGN, null, null);
    prototypeNode.setLastChild(Node.newGetProp(null, Node.newIdentifier("prototype")));
    prototypeNode.setParent(Node.newStatement(Token.ASSIGN, null, null));
    prototypeNode.getLastChild().setLastChild(Node.newStatement(Token.ASSIGN, null, Node.newCall(null, null, Node.newIdentifier("function"))));
    Node methodNode = Node.newStatement(Token.ASSIGN, null, null);
    methodNode.setLastChild(Node.newGetProp(null, Node.newIdentifier("prototype")));
    methodNode.getLastChild().setLastChild(Node.newGetProp(null, Node.newIdentifier("MethodName")));
    methodNode.getLastChild().getLastChild().setLastChild(Node.newStatement(Token.ASSIGN, null, Node.newCall(null, null, Node.newIdentifier("function"))));
    methodNode.setParent(prototypeNode);

    Definition definition = new Definition(null, null, null, methodNode, null, null, null, NodeUtil.isVarArgsFunction(methodNode.getLastChild().getLastChild()));
    DefinitionSite definitionSite = new DefinitionSite(definition, null, null, null);

    new DevirtualizePrototypeMethods(new CompilerPassContext(null, new AbstractCompiler() {})).rewriteDefinitionIfEligible(definitionSite, simpleDefinitionFinder);

    Node rewrittenMethodNode = methodNode.getLastChild().getLastChild();
    String rewrittenMethodName = rewrittenMethodNode.getFirstChild().getString();
    String expectedRewrittenMethodName = "JSCompiler_StaticMethods_MethodName";
    assertEquals(expectedRewrittenMethodName, rewrittenMethodName);
  }

  @Test
  public void testRewriteDefinitionIfEligibleFalse() {
    SimpleDefinitionFinderImpl simpleDefinitionFinder = new SimpleDefinitionFinderImpl();

    Node prototypeNode = Node.newStatement(Token.ASSIGN, null, null);
    prototypeNode.setLastChild(Node.newGetProp(null, Node.newIdentifier("prototype")));
    prototypeNode.setParent(Node.newStatement(Token.ASSIGN, null, null));
    prototypeNode.getLastChild().setLastChild(Node.newStatement(Token.ASSIGN, null, Node.newCall(null, null, Node.newIdentifier("function"))));
    Node methodNode = Node.newStatement(Token.ASSIGN, null, null);
    methodNode.setLastChild(Node.newGetProp(null, Node.newIdentifier("prototype")));
    methodNode.getLastChild().setLastChild(Node.newGetProp(null, Node.newIdentifier("MethodName")));
    methodNode.getLastChild().getLastChild().setLastChild(Node.newStatement(Token.ASSIGN, null, Node.newCall(null, null, Node.newIdentifier("function"))));
    methodNode.setParent(prototypeNode);
    methodNode.getLastChild().getLastChild().setLastChild(Node.newStatement(Token.ASSIGN, null, Node.newGetProp(null, Node.newIdentifier("exported"))));

    Definition definition = new Definition(null, null, null, methodNode, null, null, null, NodeUtil.isVarArgsFunction(methodNode.getLastChild().getLastChild()));
    DefinitionSite definitionSite = new DefinitionSite(definition, null, null, null);

    new DevirtualizePrototypeMethods(new CompilerPassContext(null, new AbstractCompiler() {})).rewriteDefinitionIfEligible(definitionSite, simpleDefinitionFinder);

    // assert method was not rewritten
    assertFalse(methodNode.getFirstChild().getString().startsWith("JSCompiler_StaticMethods_"));
  }

  @Test
  public void testIsEligibleDefinitionTrue() {
    SimpleDefinitionFinderImpl simpleDefinitionFinder = new SimpleDefinitionFinderImpl();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();

    Node prototypeNode = Node.newStatement(Token.ASSIGN, null, null);
    prototypeNode.setLastChild(Node.newGetProp(null, Node.newIdentifier("prototype")));
    prototypeNode.setParent(Node.newStatement(Token.ASSIGN, null, null));
    prototypeNode.getLastChild().setLastChild(Node.newStatement(Token.ASSIGN, null, Node.newCall(null, null, Node.newIdentifier("function"))));
    Node methodNode = Node.newStatement(Token.ASSIGN, null, null);
    methodNode.setLastChild(Node.newGetProp(null, Node.newIdentifier("prototype")));
    methodNode.getLastChild().setLastChild(Node.newGetProp(null, Node.newIdentifier("MethodName")));
    methodNode.getLastChild().getLastChild().setLastChild(Node.newStatement(Token.ASSIGN, null, Node.newCall(null, null, Node.newIdentifier("function"))));
    methodNode.setParent(prototypeNode);
    methodNode.getLastChild().getLastChild().setLastChild(Node.newStatement(Token.ASSIGN, null, Node.newGetProp(null, Node.newIdentifier("a"))));

    Definition definition = new Definition(null, null, null, methodNode, null, null, null, NodeUtil.isVarArgsFunction(methodNode.getLastChild().getLastChild()));
    DefinitionSite definitionSite = new DefinitionSite(definition, null, null, null);

    Preconditions.checkState(isEligibleDefinition(simpleDefinitionFinder, definitionSite));
  }

  @Test
  public void testIsEligibleDefinitionFalse() {
    SimpleDefinitionFinderImpl simpleDefinitionFinder = new SimpleDefinitionFinderImpl();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();

    Node prototypeNode = Node.newStatement(Token.ASSIGN, null, null);
    prototypeNode.setLastChild(Node.newGetProp(null, Node.newIdentifier("prototype")));
    prototypeNode.setParent(Node.newStatement(Token.ASSIGN, null, null));
    prototypeNode.getLastChild().setLastChild(Node.newStatement(Token.ASSIGN, null, Node.newCall(null, null, Node.newIdentifier("function"))));
    Node methodNode = Node.newStatement(Token.ASSIGN, null, null);
    methodNode.setLastChild(Node.newGetProp(null, Node.newIdentifier("prototype")));
    methodNode.getLastChild().setLastChild(Node.newGetProp(null, Node.newIdentifier("MethodName")));
    methodNode.getLastChild().getLastChild().setLastChild(Node.newStatement(Token.ASSIGN, null, Node.newCall(null, null, Node.newIdentifier("function"))));
    methodNode.setParent(prototypeNode);
    methodNode.getLastChild().getLastChild().setLastChild(Node.newStatement(Token.ASSIGN, null, Node.newGetProp(null, Node.newIdentifier("exported"))));

    Definition definition = new Definition(null, null, null, methodNode, null, null, null, NodeUtil.isVarArgsFunction(methodNode.getLastChild().getLastChild()));
    DefinitionSite definitionSite = new DefinitionSite(definition, null, null, null);

    assertFalse(isEligibleDefinition(simpleDefinitionFinder, definitionSite));
  }

  @Test
  public void testRewriteCallSites() {
    SimpleDefinitionFinderImpl simpleDefinitionFinder = new SimpleDefinitionFinderImpl();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();

    Node prototypeNode = Node.newStatement(Token.ASSIGN, null, null);
    prototypeNode.setLastChild(Node.newGetProp(null, Node.newIdentifier("prototype")));
    prototypeNode.setParent(Node.newStatement(Token.ASSIGN, null, null));
    prototypeNode.getLastChild().setLastChild(Node.newStatement(Token.ASSIGN, null, Node.newCall(null, null, Node.newIdentifier("function"))));
    Node methodNode = Node.newStatement(Token.ASSIGN, null, null);
    methodNode.setLastChild(Node.newGetProp(null, Node.newIdentifier("prototype")));
    methodNode.getLastChild().setLastChild(Node.newGetProp(null, Node.newIdentifier("MethodName")));
    methodNode.getLastChild().getLastChild().setLastChild(Node.newStatement(Token.ASSIGN, null, Node.newCall(null, null, Node.newIdentifier("function"))));
    methodNode.setParent(prototypeNode);
    methodNode.getLastChild().getLastChild().setLastChild(Node.newStatement(Token.ASSIGN, null, Node.newGetProp(null, Node.newIdentifier("a"))));

    Definition definition = new Definition(null, null, null, methodNode, null, null, null, NodeUtil.isVarArgsFunction(methodNode.getLastChild().getLastChild()));
    DefinitionSite definitionSite = new DefinitionSite(definition, null, null, null);

    String newMethodName = "JSCompiler_StaticMethods_MethodName";
    new DevirtualizePrototypeMethods(new CompilerPassContext(null, new AbstractCompiler() {})).rewriteDefinitionIfEligible(definitionSite, simpleDefinitionFinder);

    Node prototypeNodeAfter = prototypeNode;
    Node methodNodeAfter = methodNode;
    Node callSiteNode = Node.newStatement(Token.ASSIGN, null, null);
    callSiteNode.setLastChild(Node.newCall(null, null, Node.newGetProp(null, Node.newIdentifier("prototype"))));
    callSiteNode.getLastChild().setLastChild(Node.newGetProp(null, Node.newIdentifier("MethodName")));
    callSiteNode.getLastChild().getLastChild().setLastChild(Node.newCall(null, null, Node.newIdentifier("a"), Node.newNumber(1), Node.newNumber(2), Node.newNumber(3)));
    Node blockNode = Node.newBlock(null, null, null);
    blockNode.addChildToFront(prototypeNodeAfter);
    blockNode.addChildToFront(callSiteNode);

    new DevirtualizePrototypeMethods(new CompilerPassContext(null, new AbstractCompiler() {})).rewriteCallSites(simpleDefinitionFinder, definition, newMethodName);

    String expectedRewrittenCallSite = "JSCompiler_StaticMethods_MethodName(o, a, b, c)";
    assertEquals(expectedRewrittenCallSite, callSiteNode.getFirstChild().toString());
  }

  private boolean isEligibleDefinition(SimpleDefinitionFinder simpleDefinitionFinder, DefinitionSite definitionSite) {
    // TODO: Implement test for eligibility conditions
    return true;
  }

  private static class UseSite {
    Node node;
    Node parent;
    Node lValue;
    Node rValue;
    Node module;
    int index;

    UseSite(Node node, Node parent, Node lValue, Node rValue, Node module, int index) {
      this.node = node;
      this.parent = parent;
      this.lValue = lValue;
      this.rValue = rValue;
      this.module = module;
      this.index = index;
    }
  }
}