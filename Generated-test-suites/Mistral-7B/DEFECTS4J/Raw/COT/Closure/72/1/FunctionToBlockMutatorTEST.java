package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.base.Supplier;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Test class for FunctionToBlockMutator.
 */
public class FunctionToBlockMutatorTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;
  private FunctionToBlockMutator functionToBlockMutator;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    safeNameIdSupplier = () -> "test_id";
    functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
  }

  @Test
  public void testMutate_NullFnNode() {
    assertNull(functionToBlockMutator.mutate(null, null, null, null, false, false));
  }

  @Test
  public void testMutate_NullCallNode() {
    Node fnNode = NodeUtil.newFunctionNode(null, null);
    assertNull(functionToBlockMutator.mutate(null, fnNode, null, null, false, false));
  }

  @Test
  public void testMutate_NullResultName() {
    Node fnNode = NodeUtil.newFunctionNode(null, null);
    Node callNode = NodeUtil.newCallNode("testFunc", NodeUtil.newFunctionExpressionNode(null, null));
    assertNull(functionToBlockMutator.mutate(null, fnNode, callNode, null, false, false));
  }

  @Test
  public void testMutate_BasicCase() {
    String fnName = "testFunc";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null);
    Node callNode = NodeUtil.newCallNode("testFunc", NodeUtil.newFunctionExpressionNode(null, null));
    Node resultNode = NodeUtil.newIdentifierNode("result");
    Node injectableBlock = functionToBlockMutator.mutate(fnName, fnNode, callNode, "result", false, false);

    assertNotNull(injectableBlock);
    assertEquals(Node.BLOCK, injectableBlock.getType());
    assertTrue(injectableBlock.getFirstChild().getType() == Token.VAR);
    assertTrue(injectableBlock.getLastChild().getType() == Token.RETURN);
  }

  @Test
  public void testMutate_CallInLoop() {
    String fnName = "testFunc";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null);
    Node callNode = NodeUtil.newCallNode("testFunc", NodeUtil.newFunctionExpressionNode(null, null));
    Node resultNode = NodeUtil.newIdentifierNode("result");
    Node injectableBlock = functionToBlockMutator.mutate(fnName, fnNode, callNode, "result", true, false);

    assertNotNull(injectableBlock);
    assertEquals(Node.BLOCK, injectableBlock.getType());
    assertTrue(injectableBlock.getFirstChild().getType() == Token.VAR);
    assertTrue(injectableBlock.getLastChild().getType() == Token.RETURN);

    // Fix-up all local names to be unique for this subtree.
    functionToBlockMutator.makeLocalNamesUnique(injectableBlock, true);

    Node clonedInjectableBlock = injectableBlock.cloneTree();
    assertEquals(injectableBlock, clonedInjectableBlock);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutate_InvalidIsCallInLoop() {
    String fnName = "testFunc";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null);
    Node callNode = NodeUtil.newCallNode("testFunc", NodeUtil.newFunctionExpressionNode(null, null));
    Node resultNode = NodeUtil.newIdentifierNode("result");
    functionToBlockMutator.mutate(fnName, fnNode, callNode, "result", true, "invalid");
  }

  @Test
  public void testFixUnitializedVarDeclarations() {
    Node uninitializedNode = NodeUtil.newVarNode("a");
    Node uninitializedBlock = NodeUtil.newBlockNode(uninitializedNode);

    functionToBlockMutator.fixUnitializedVarDeclarations(uninitializedBlock);

    Node clonedUninitializedBlock = uninitializedBlock.cloneTree();
    assertEquals(uninitializedBlock, clonedUninitializedBlock);

    Node initializedBlock = functionToBlockMutator.fixUnitializedVarDeclarations(uninitializedBlock);
    assertNotSame(uninitializedBlock, initializedBlock);

    Node initializedNode = initializedBlock.getFirstChild();
    assertTrue(initializedNode.getType() == Token.VAR);
    assertTrue(initializedNode.getFirstChild().getType() == Token.NAME);
    assertTrue(initializedNode.getFirstChild().getFirstChild().getType() == Token.STRING);
    assertEquals("a", initializedNode.getFirstChild().getFirstChild().getString());
    Node valueNode = initializedNode.getFirstChild().getLastChild();
    assertTrue(valueNode.getType() == Token.UNDEFINED);
  }

  @Test
  public void testMakeLocalNamesUnique() {
    String fnName = "testFunc";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null);
    Node fnTemplateRoot = NodeUtil.newBlockNode(fnNode);

    // Make variable names unique to this instance.
    functionToBlockMutator.makeLocalNamesUnique(fnTemplateRoot, false);

    Node clonedFnTemplateRoot = fnTemplateRoot.cloneTree();
    assertEquals(fnTemplateRoot, clonedFnTemplateRoot);

    Node updatedFnTemplateRoot = functionToBlockMutator.makeLocalNamesUnique(fnTemplateRoot, false);
    assertNotSame(fnTemplateRoot, updatedFnTemplateRoot);

    for (int i = 0; i < fnTemplateRoot.getNumChildren(); i++) {
      Node child = fnTemplateRoot.getChild(i);
      Node clonedChild = clonedFnTemplateRoot.getChild(i);
      Node updatedChild = updatedFnTemplateRoot.getChild(i);
      assertEquals(child, clonedChild);
      assertEquals(clonedChild, updatedChild);
    }
  }

  @Test
  public void testReplaceReturns() {
    String fnName = "testFunc";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null);
    Node resultNode = NodeUtil.newIdentifierNode("result");
    Node labelName = "test_label";

    Node block = NodeUtil.newBlockNode(NodeUtil.newReturnNode(null));

    Node injectableBlock = functionToBlockMutator.replaceReturns(block, resultNode, labelName, true);

    assertNotNull(injectableBlock);
    assertEquals(Node.BLOCK, injectableBlock.getType());
    assertTrue(injectableBlock.getFirstChild().getType() == Token.VAR);
    assertTrue(injectableBlock.getLastChild().getType() == Token.BREAK);

    Node clonedInjectableBlock = injectableBlock.cloneTree();
    assertEquals(injectableBlock, clonedInjectableBlock);

    Node updatedInjectableBlock = functionToBlockMutator.replaceReturns(injectableBlock, resultNode, labelName, true);
    assertNotSame(injectableBlock, updatedInjectableBlock);
  }

  @Test
  public void testReplaceReturns_NoResultName() {
    String fnName = "testFunc";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null);
    Node labelName = "test_label";

    Node block = NodeUtil.newBlockNode(NodeUtil.newReturnNode(null));

    Node injectableBlock = functionToBlockMutator.replaceReturns(block, null, labelName, false);

    assertNotNull(injectableBlock);
    assertEquals(Node.BLOCK, injectableBlock.getType());
    assertTrue(injectableBlock.getLastChild().getType() == Token.BREAK);

    Node clonedInjectableBlock = injectableBlock.cloneTree();
    assertEquals(injectableBlock, clonedInjectableBlock);

    Node updatedInjectableBlock = functionToBlockMutator.replaceReturns(injectableBlock, null, labelName, false);
    assertNotSame(injectableBlock, updatedInjectableBlock);
  }

  @Test
  public void testReplaceReturns_NoLabelName() {
    String fnName = "testFunc";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null);
    Node resultNode = NodeUtil.newIdentifierNode("result");

    Node block = NodeUtil.newBlockNode(NodeUtil.newReturnNode(null));

    try {
      functionToBlockMutator.replaceReturns(block, resultNode, null, true);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Pass
    }
  }
}