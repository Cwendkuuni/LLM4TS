package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.DefinitionsRemover.DefinitionSite;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.FunctionTypeNative;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;

import java.util.Collection;
import java.util.List;

/**
 * Tests for the DevirtualizePrototypeMethods class.
 */
@RunWith(JUnit4.class)
public class DevirtualizePrototypeMethodsTest {

  @Test
  public void testDevirtualizePrototypeMethods() {
    Node root = new Node(Token.PROGRAM, null);

    JSTypeRegistry typeRegistry = new JSTypeRegistry();

    // Create test definition.
    Node definitionNode = new Node(Token.VAR, new Node(Token.IDENT, "test"));
    definitionNode.setStart(0);
    definitionNode.setEnd(10);
    definitionNode.setJSType(new FunctionTypeNative(typeRegistry));
    definitionNode.getFirstChild().setEnd(10);

    Node functionNode = new Node(Token.FUNCTION, definitionNode);
    functionNode.setStart(10);
    functionNode.setEnd(20);

    Node bodyNode = new Node(Token.BLOCK, null);
    Node addNode = new Node(Token.CALL, new Node(Token.IDENT, "add"));
    addNode.setStart(21);
    addNode.setEnd(30);
    Node thisNode = new Node(Token.THIS);
    addNode.addChildToBack(thisNode);
    Node arg1Node = new Node(Token.INT_LITERAL, 1);
    addNode.addChildToBack(arg1Node);
    Node arg2Node = new Node(Token.INT_LITERAL, 2);
    addNode.addChildToBack(arg2Node);

    bodyNode.addChildToFront(addNode);
    functionNode.setLastChild(bodyNode);
    definitionNode.setLastChild(functionNode);

    root.addChildToFront(definitionNode);

    // Create test call site.
    Node callSiteNode = new Node(Token.CALL, new Node(Token.IDENT, "test"));
    callSiteNode.setStart(41);
    callSiteNode.setEnd(50);
    Node testNode = callSiteNode.getFirstChild();
    Node testObjectNode = new Node(Token.THIS);
    testNode.addChildToBack(testObjectNode);
    Node arg1Node2 = new Node(Token.INT_LITERAL, 3);
    testNode.addChildToBack(arg1Node2);
    Node arg2Node2 = new Node(Token.INT_LITERAL, 4);
    testNode.addChildToBack(arg2Node2);

    root.addChildToFront(testSiteNode);

    // Create test compiler pass manager.
    AbstractCompiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new DevirtualizePrototypeMethods(compiler));

    // Process test code.
    passManager.process(root, null);

    // Check that definition was rewritten as a global function.
    DefinitionSite definitionSite = new DefinitionSite(definitionNode, null, null);
    Definition definition = new Definition(definitionNode, null, null, null, null);
    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
    devitationalizer.rewriteDefinitionIfEligible(definitionSite, null);
    Node rewrittenDefinitionNode = definitionSite.node;

    assertNotNull(rewrittenDefinitionNode);
    assertEquals(
        "function test(self, a, b) { return add(self, a, b); }",
        rewrittenDefinitionNode.toString());

    // Check that call site was rewritten to call the global function.
    Node callSite = testSiteNode;
    Node rewrittenCallSiteNode = callSite.getFirstChild();
    Node rewrittenCallSiteNameNode = rewrittenCallSiteNode.getLastChild();
    String rewrittenCallSiteName = rewrittenCallSiteNameNode.getString();

    assertEquals("test", rewrittenCallSiteName);
    assertEquals(
        "test(test, 3, 4)",
        rewrittenCallSiteNode.toString());
  }

  /**
   * Test definition site that is not in the global scope.
   */
  @Test
  public void testNotInGlobalScope() {
    Node root = new Node(Token.PROGRAM, null);

    // Create test definition.
    Node definitionNode = new Node(Token.VAR, new Node(Token.IDENT, "test"));
    definitionNode.setStart(0);
    definitionNode.setEnd(10);
    definitionNode.setJSType(new FunctionTypeNative(null));
    definitionNode.getFirstChild().setEnd(10);

    Node functionNode = new Node(Token.FUNCTION, definitionNode);
    functionNode.setStart(10);
    functionNode.setEnd(20);

    Node bodyNode = new Node(Token.BLOCK, null);
    Node addNode = new Node(Token.CALL, new Node(Token.IDENT, "add"));
    addNode.setStart(21);
    addNode.setEnd(30);
    Node thisNode = new Node(Token.THIS);
    addNode.addChildToBack(thisNode);
    Node arg1Node = new Node(Token.IDENT, "a");
    addNode.addChildToBack(arg1Node);
    Node arg2Node = new Node(Token.IDENT, "b");
    addNode.addChildToBack(arg2Node);

    bodyNode.addChildToFront(addNode);
    functionNode.setLastChild(bodyNode);
    definitionNode.setLastChild(functionNode);

    root.addChildToFront(definitionNode);

    // Create test call site.
    Node callSiteNode = new Node(Token.CALL, new Node(Token.IDENT, "test"));
    callSiteNode.setStart(41);
    callSiteNode.setEnd(50);
    Node testNode = callSiteNode.getFirstChild();
    Node testObjectNode = new Node(Token.THIS);
    testNode.addChildToBack(testObjectNode);
    Node arg1Node2 = new Node(Token.IDENT, "a");
    testNode.addChildToBack(arg1Node2);
    Node arg2Node2 = new Node(Token.IDENT, "b");
    testNode.addChildToBack(arg2Node2);

    root.addChildToFront(callSiteNode);

    // Create test compiler pass manager.
    AbstractCompiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new DevirtualizePrototypeMethods(compiler));

    // Process test code.
    passManager.process(root, null);

    // Check that definition was not rewritten.
    DefinitionSite definitionSite = new DefinitionSite(definitionNode, null, null);
    assertNull(definitionSite.node);
  }

  /**
   * Test definition site that is not eligible because it is part of
   * an object literal.
   */
  @Test
  public void testObjectLiteral() {
    Node root = new Node(Token.PROGRAM, null);

    // Create test definition.
    Node definitionNode = new Node(Token.OBJECT_LITERAL, null);
    definitionNode.setStart(0);
    definitionNode.setEnd(30);

    Node testPropertyNode = new Node(Token.IDENT, "test");
    testPropertyNode.setStart(2);
    testPropertyNode.setEnd(12);
    Node testValueNode = new Node(Token.FUNCTION, null);
    testValueNode.setStart(13);
    testValueNode.setEnd(23);
    Node testValueBodyNode = new Node(Token.BLOCK, null);
    testValueBodyNode.setStart(15);
    testValueBodyNode.setEnd(22);
    testValueNode.setLastChild(testValueBodyNode);
    definitionNode.addChildToFront(testPropertyNode);
    definitionNode.addChildToBack(testValueNode);

    root.addChildToFront(definitionNode);

    // Create test call site.
    Node callSiteNode = new Node(Token.CALL, new Node(Token.IDENT, "test"));
    callSiteNode.setStart(41);
    callSiteNode.setEnd(50);
    Node testNode = callSiteNode.getFirstChild();
    Node testObjectNode = new Node(Token.THIS);
    testNode.addChildToBack(testObjectNode);
    Node arg1Node = new Node(Token.INT_LITERAL, 1);
    testNode.addChildToBack(arg1Node);
    Node arg2Node = new Node(Token.INT_LITERAL, 2);
    testNode.addChildToBack(arg2Node);

    root.addChildToFront(callSiteNode);

    // Create test compiler pass manager.
    AbstractCompiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new DevirtualizePrototypeMethods(compiler));

    // Process test code.
    passManager.process(root, null);

    // Check that definition was not rewritten.
    DefinitionSite definitionSite = new DefinitionSite(definitionNode, null, null);
    assertNull(definitionSite.node);
  }

  /**
   * Test definition site that is not eligible because it is defined
   * in a module loaded after the first call site.
   */
  @Test
  public void testModuleLoadedAfterCallSite() {
    Node root = new Node(Token.PROGRAM, null);

    // Create first call site module.
    Node callSiteModuleNode = new Node(Token.MODULE, null);
    callSiteModuleNode.setStart(0);
    callSiteModuleNode.setEnd(10);
    Node testCallSiteNode = new Node(Token.CALL, new Node(Token.IDENT, "test"));
    testCallSiteNode.setStart(3);
    testCallSiteNode.setEnd(13);
    callSiteModuleNode.addChildToBack(testCallSiteNode);

    root.addChildToFront(callSiteModuleNode);

    // Create test definition module.
    Node definitionModuleNode = new Node(Token.MODULE, null);
    definitionModuleNode.setStart(11);
    definitionModuleNode.setEnd(30);

    // Create test definition.
    Node definitionNode = new Node(Token.VAR, new Node(Token.IDENT, "test"));
    definitionNode.setStart(13);
    definitionNode.setEnd(23);
    definitionNode.setJSType(new FunctionTypeNative(null));
    definitionNode.getFirstChild().setEnd(23);

    Node functionNode = new Node(Token.FUNCTION, definitionNode);
    functionNode.setStart(24);
    functionNode.setEnd(33);

    Node bodyNode = new Node(Token.BLOCK, null);
    Node addNode = new Node(Token.CALL, new Node(Token.IDENT, "add"));
    addNode.setStart(26);
    addNode.setEnd(35);
    Node thisNode = new Node(Token.THIS);
    addNode.addChildToBack(thisNode);
    Node arg1Node = new Node(Token.IDENT, "a");
    addNode.addChildToBack(arg1Node);
    Node arg2Node = new Node(Token.IDENT, "b");
    addNode.addChildToBack(arg2Node);

    bodyNode.addChildToFront(addNode);
    functionNode.setLastChild(bodyNode);
    definitionNode.setLastChild(functionNode);

    definitionModuleNode.addChildToBack(definitionNode);

    root.addChildToFront(definitionModuleNode);

    // Create test compiler pass manager.
    AbstractCompiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new DevirtualizePrototypeMethods(compiler));

    // Process test code.
    passManager.process(root, null);

    // Check that definition was not rewritten.
    DefinitionSite definitionSite = new DefinitionSite(definitionNode, callSiteModuleNode, null);
    assertNull(definitionSite.node);
  }

  /**
   * Test definition site that is not eligible because it is used
   * in multiple modules.
   */
  @Test
  public void testMultipleUseSites() {
    Node root = new Node(Token.PROGRAM, null);

    // Create first call site module.
    Node callSiteModuleNode1 = new Node(Token.MODULE, null);
    callSiteModuleNode1.setStart(0);
    callSiteModuleNode1.setEnd(10);
    Node testCallSiteNode1 = new Node(Token.CALL, new Node(Token.IDENT, "test"));
    testCallSiteNode1.setStart(3);
    testCallSiteNode1.setEnd(13);
    callSiteModuleNode1.addChildToBack(testCallSiteNode1);

    root.addChildToFront(callSiteModuleNode1);

    // Create second call site module.
    Node callSiteModuleNode2 = new Node(Token.MODULE, null);
    callSiteModuleNode2.setStart(11);
    callSiteModuleNode2.setEnd(20);
    Node testCallSiteNode2 = new Node(Token.CALL, new Node(Token.IDENT, "test"));
    testCallSiteNode2.setStart(3);
    testCallSiteNode2.setEnd(13);
    callSiteModuleNode2.addChildToBack(testCallSiteNode2);

    root.addChildToFront(callSiteModuleNode2);

    // Create test definition.
    Node definitionNode = new Node(Token.VAR, new Node(Token.IDENT, "test"));
    definitionNode.setStart(22);
    definitionNode.setEnd(32);
    definitionNode.setJSType(new FunctionTypeNative(null));
    definitionNode.getFirstChild().setEnd(32);

    Node functionNode = new Node(Token.FUNCTION, definitionNode);
    functionNode.setStart(33);
    functionNode.setEnd(42);

    Node bodyNode = new Node(Token.BLOCK, null);
    Node addNode = new Node(Token.CALL, new Node(Token.IDENT, "add"));
    addNode.setStart(35);
    addNode.setEnd(44);
    Node thisNode = new Node(Token.THIS);
    addNode.addChildToBack(thisNode);
    Node arg1Node = new Node(Token.IDENT, "a");
    addNode.addChildToBack(arg1Node);
    Node arg2Node = new Node(Token.IDENT, "b");
    addNode.addChildToBack(arg2Node);

    bodyNode.addChildToFront(addNode);
    functionNode.setLastChild(bodyNode);
    definitionNode.setLastChild(functionNode);

    root.addChildToFront(definitionNode);

    // Create test compiler pass manager.
    AbstractCompiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new DevirtualizePrototypeMethods(compiler));

    // Process test code.
    passManager.process(root, null);

    // Check that definition was not rewritten.
    DefinitionSite definitionSite = new DefinitionSite(definitionNode, null, null);
    assertNull(definitionSite.node);
  }
}