package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Nodeops;

public class PeepholeOptimizationsPassTest {
  private PeepholeOptimizationsPass pass;
  private AbstractPeepholeOptimization optimization1;
  private AbstractPeepholeOptimization optimization2;

  @Before
  public void setup() {
    pass = new PeepholeOptimizationsPass(null, optimization1, optimization2);
  }

  @Test
  public void testTraverseEmptyNode() {
    Node node = Nodeops.SCOPE;
    pass.traverse(node);
  }

  @Test
  public void testTraverseFunctionNodeWithNoChildren() {
    Node node = Nodeops.FUNCTION_DECLARATION("func", Nodeops.BLOCK(null));
    pass.traverse(node);
  }

  @Test
  public void testTraverseFunctionNodeWithChildren() {
    Node functionNode = Nodeops.FUNCTION_DECLARATION("func", Nodeops.BLOCK(null));
    Node childNode = Nodeops.NUMBER_LITERAL(1);
    Node child2Node = Nodeops.BLOCK(Nodeops.ASSIGNMENT("x", Nodeops.NUMBER_LITERAL(2)));
    Node bodyNode = Nodeops.BLOCK(childNode, child2Node);
    Node childNodeInFunction = Nodeops.MEMBER(functionNode, "body", Nodeops.BLOCK(null));
    functionNode.setFirstChild(childNodeInFunction);
    pass.traverse(functionNode);
  }

  @Test
  public void testShouldVisitFunctionNode() {
    Node functionNode = Nodeops.FUNCTION_DECLARATION("func", Nodeops.BLOCK(null));
    assertTrue(pass.shouldVisit(functionNode));
  }

  @Test
  public void testShouldVisitScriptNode() {
    Node scriptNode = Nodeops.SCRIPT(null);
    assertTrue(pass.shouldVisit(scriptNode));
  }

  @Test
  public void testShouldNotVisitNonFunctionNonScriptNode() {
    Node node = Nodeops.IDENTIFIER("identifier");
    assertFalse(pass.shouldVisit(node));
  }

  @Test
  public void testShouldNotVisitNonFunctionNonScriptNodeWithChildren() {
    Node node = Nodeops.BLOCK(Nodeops.ASSIGNMENT("x", Nodeops.NUMBER_LITERAL(1)));
    assertFalse(pass.shouldVisit(node));
  }

  @Test
  public void testVisitNode() {
    Node nodeToVisit = Nodeops.NUMBER_LITERAL(1);
    pass.visit(nodeToVisit);
  }

  @Test
  public void testVisitFunctionNodeWithNoChildren() {
    Node functionNode = Nodeops.FUNCTION_DECLARATION("func", Nodeops.BLOCK(null));
    pass.visit(functionNode);
  }

  @Test
  public void testVisitFunctionNodeWithChildren() {
    Node functionNode = Nodeops.FUNCTION_DECLARATION("func", Nodeops.BLOCK(null));
    Node childNode = Nodeops.NUMBER_LITERAL(1);
    Node child2Node = Nodeops.BLOCK(Nodeops.ASSIGNMENT("x", Nodeops.NUMBER_LITERAL(2)));
    Node bodyNode = Nodeops.BLOCK(childNode, child2Node);
    Node childNodeInFunction = Nodeops.MEMBER(functionNode, "body", Nodeops.BLOCK(null));
    functionNode.setFirstChild(childNodeInFunction);
    pass.visit(functionNode);
  }

  @Test
  public void testShouldRetraverseFunctionNodeWithChangedParent() {
    Node parentNode = Nodeops.BLOCK(Nodeops.ASSIGNMENT("x", Nodeops.NUMBER_LITERAL(1)));
    Node functionNode = Nodeops.FUNCTION_DECLARATION("func", Nodeops.BLOCK(null));
    Node childNode = Nodeops.NUMBER_LITERAL(1);
    Node child2Node = Nodeops.BLOCK(Nodeops.ASSIGNMENT("x", Nodeops.NUMBER_LITERAL(2)));
    Node bodyNode = Nodeops.BLOCK(childNode, child2Node);
    Node childNodeInFunction = Nodeops.MEMBER(functionNode, "body", Nodeops.BLOCK(null));
    Node functionNodeWithParent = Nodeops.MEMBER(parentNode, "body", functionNode);
    functionNode.setFirstChild(childNodeInFunction);
    pass.traverse(parentNode);
    assertTrue(pass.shouldRetraverse(functionNodeWithParent));
  }

  @Test
  public void testShouldNotRetraverseFunctionNodeWithUnchangedParent() {
    Node parentNode = Nodeops.BLOCK(Nodeops.ASSIGNMENT("x", Nodeops.NUMBER_LITERAL(1)));
    Node functionNode = Nodeops.FUNCTION_DECLARATION("func", Nodeops.BLOCK(null));
    Node childNode = Nodeops.NUMBER_LITERAL(1);
    Node child2Node = Nodeops.BLOCK(Nodeops.ASSIGNMENT("x", Nodeops.NUMBER_LITERAL(2)));
    Node bodyNode = Nodeops.BLOCK(childNode, child2Node);
    Node childNodeInFunction = Nodeops.MEMBER(functionNode, "body", Nodeops.BLOCK(null));
    Node functionNodeWithParent = Nodeops.MEMBER(parentNode, "body", functionNode);
    functionNode.setFirstChild(childNodeInFunction);
    pass.traverse(parentNode);
    pass.traverse(functionNode);
    pass.traverse(functionNode);
    assertFalse(pass.shouldRetraverse(functionNodeWithParent));
  }
}