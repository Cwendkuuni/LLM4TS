package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = new Node(" Program ");
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostThreshold() {
    Node root = new Node("Program");
    int cost = InlineCostEstimator.getCost(root, 10);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithNode() {
    Node root = new Node("Program");
    Node identifierNode = new Node("identifier");
    root.addChild(identifierNode);

    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);

    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, estimator.getCost());
  }

  @Test
  public void testGetCostWithLongerIdentifiers() {
    Node root = new Node("Program");
    Node longerIdentifierNode = new Node("longIdentifier");
    root.addChild(longerIdentifierNode);

    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);

    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * 3, estimator.getCost());
  }

  @Test
  public void testGetCostWithStringConcatenation() {
    Node root = new Node("Program");
    Node string1Node = new Node("s1");
    Node string2Node = new Node("s2");
    Node string3Node = new Node("s3");
    root.addChild(string1Node);
    root.addChild(new Node("+"));
    root.addChild(string2Node);
    root.addChild(new Node("+"));
    root.addChild(string3Node);

    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);

    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * 3 + 2, estimator.getCost());
  }

  @Test
  public void testGetCostWithNestedIdentifiers() {
    Node root = new Node("Program");
    Node identifier1Node = new Node("identifier1");
    Node identifier2Node = new Node("identifier2");
    Node identifier3Node = new Node("identifier3");
    root.addChild(identifier1Node);
    root.addChild(new Node("."));
    root.addChild(identifier2Node);
    root.addChild(new Node("."));
    root.addChild(identifier3Node);

    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);

    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * 3, estimator.getCost());
  }

  @Test
  public void testGetCostWithEmptyString() {
    Node root = new Node("Program");
    Node emptyStringNode = new Node("\"\"");
    root.addChild(emptyStringNode);

    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);

    assertEquals(0, estimator.getCost());
  }

  @Test
  public void testGetCostWithString() {
    Node root = new Node("Program");
    Node stringNode = new Node("\"string\"");
    root.addChild(stringNode);

    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);

    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * 2 + 4, estimator.getCost());
  }

  @Test
  public void testGetCostWithComment() {
    Node root = new Node("Program");
    Node commentNode = new Node("// comment");
    root.addChild(commentNode);

    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);

    assertEquals(0, estimator.getCost());
  }

  @Test
  public void testGetCostWithBooleanLiteral() {
    Node root = new Node("Program");
    Node booleanLiteralNode = new Node("true");
    root.addChild(booleanLiteralNode);

    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);

    assertNull(estimator);
  }

  @Test
  public void testGetCostWithNullLiteral() {
    Node root = new Node("Program");
    Node nullLiteralNode = new Node(null);
    root.addChild(nullLiteralNode);

    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);

    assertNull(estimator);
  }

  @Test
  public void testGetCostWithNestedNodes() {
    Node root = new Node("Program");
    Node programIdentifierNode = new Node("program");
    Node expressionNode = new Node("expression");
    Node identifier1Node = new Node("identifier1");
    Node operatorNode = new Node("+");
    Node identifier2Node = new Node("identifier2");
    Node operator2Node = new Node("-");
    Node numberNode = new Node("1");

    root.addChild(programIdentifierNode);
    programIdentifierNode.addChild(expressionNode);
    expressionNode.addChild(identifier1Node);
    identifier1Node.addChild(operatorNode);
    operatorNode.addChild(identifier2Node);
    identifier2Node.addChild(operator2Node);
    operator2Node.addChild(numberNode);

    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);

    assertEquals(
        InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * 3
            + InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * 2
            + InlineCostEstimator.ESTIMATED_IDENTIFIER_COST
            + 3, estimator.getCost());
  }
}