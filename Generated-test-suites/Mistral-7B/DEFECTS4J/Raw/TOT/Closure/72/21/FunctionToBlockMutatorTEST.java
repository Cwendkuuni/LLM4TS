package com.google.javascript.jscomp;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeOperations;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

public class FunctionToBlockMutatorTest {

  private FunctionToBlockMutator functionToBlockMutator;
  private Node testFunctionNode;

  @Before
  public void setUp() {
    functionToBlockMutator = new FunctionToBlockMutator(
        new DummyCompiler(), new DummyNameIdSupplier());

    String testFunctionSource =
        "function testFunction(a, b) {" +
            "var result = a + b;" +
            "return result;" +
            "}";

    testFunctionNode = NodeOperations.parse(testFunctionSource);
  }

  @Test
  public void testMutateFunctionWithNoArguments() {
    String fnName = "testFunction";
    Node callNode = NodeUtil.newCallExpressionNode("testFunction");
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedBlock = functionToBlockMutator.mutate(fnName, testFunctionNode,
        callNode, resultName, needsDefaultResult, isCallInLoop);

    // The testFunctionNode has been mutated in place, so make sure it's the
    // same node as the one returned by mutateFunction.
    Node expectedMutatedBlock = NodeUtil.getFunctionBody(testFunctionNode);
    assertThat(expectedMutatedBlock).isEqualTo(mutatedBlock);

    // Verify that the local names are unique.
    NodeTraversal.traverse(functionToBlockMutator, testFunctionNode,
        new MakeDeclaredNamesUnique(new InlineRenamer(
            new DummyNameIdSupplier(),
            "inline_",
            isCallInLoop)));
    Map<String, Integer> nameOccurrences = Maps.newHashMap();
    NodeTraversal.traverse(functionToBlockMutator, testFunctionNode,
        new NameCounter(nameOccurrences));
    for (Map.Entry<String, Integer> entry : nameOccurrences.entrySet()) {
      String name = entry.getKey();
      int expectedOccurrences = 1;
      if (name.equals("result")) {
        expectedOccurrences = 2; // One for declaration and one for assignment.
      }
      assertThat(entry.getValue()).isEqualTo(expectedOccurrences);
    }
  }

  @Test
  public void testMutateFunctionWithArguments() {
    String fnName = "testFunction";
    Node callNode = NodeUtil.newCallExpressionNode("testFunction",
        NodeUtil.newStringNode("a"), NodeUtil.newNumberNode(1),
        NodeUtil.newStringNode("b"), NodeUtil.newNumberNode(2));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedBlock = functionToBlockMutator.mutate(fnName, testFunctionNode,
        callNode, resultName, needsDefaultResult, isCallInLoop);

    // The testFunctionNode has been mutated in place, so make sure it's the
    // same node as the one returned by mutateFunction.
    Node expectedMutatedBlock = NodeUtil.getFunctionBody(testFunctionNode);
    assertThat(expectedMutatedBlock).isEqualTo(mutatedBlock);

    // Verify that arguments have been inlined.
    Node argumentMap = testFunctionNode.getFirstChild().getFirstChild();
    Map<String, Node> actualArguments = NodeUtil.getChildrenMap(argumentMap);
    Map<String, Node> expectedArguments = Maps.newHashMap();
    expectedArguments.put("a", NodeUtil.newNumberNode(1));
    expectedArguments.put("b", NodeUtil.newNumberNode(2));
    assertThat(actualArguments).isEqualTo(expectedArguments);
  }

  @Test
  public void testMutateFunctionWithLabel() {
    String fnName = "testFunction";
    Node callNode = NodeUtil.newLabeledStatementNode(
        new Node(Token.LABEL, "testLabel"), NodeUtil.newCallExpressionNode("testFunction"));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedBlock = functionToBlockMutator.mutate(fnName, testFunctionNode,
        callNode, resultName, needsDefaultResult, isCallInLoop);

    // The testFunctionNode has been mutated in place, so make sure it's the
    // same node as the one returned by mutateFunction.
    Node expectedMutatedBlock = NodeUtil.getFunctionBody(testFunctionNode);
    assertThat(expectedMutatedBlock).isEqualTo(mutatedBlock);

    // Verify that the label is still present.
    Node labelNode = testFunctionNode.getFirstChild();
    Node expectedLabelNode = NodeUtil.newLabelNode("testLabel");
    assertThat(labelNode).isInstanceOf(expectedLabelNode.getClass());
  }

  @Test
  public void testMutateFunctionWithLabelAndArguments() {
    String fnName = "testFunction";
    Node callNode = NodeUtil.newLabeledStatementNode(
        new Node(Token.LABEL, "testLabel"),
        NodeUtil.newCallExpressionNode(
            "testFunction",
            NodeUtil.newStringNode("a"), NodeUtil.newNumberNode(1),
            NodeUtil.newStringNode("b"), NodeUtil.newNumberNode(2)));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedBlock = functionToBlockMutator.mutate(fnName, testFunctionNode,
        callNode, resultName, needsDefaultResult, isCallInLoop);

    // The testFunctionNode has been mutated in place, so make sure it's the
    // same node as the one returned by mutateFunction.
    Node expectedMutatedBlock = NodeUtil.getFunctionBody(testFunctionNode);
    assertThat(expectedMutatedBlock).isEqualTo(mutatedBlock);

    // Verify that arguments have been inlined and the label is still present.
    Node argumentMap = testFunctionNode.getFirstChild().getFirstChild();
    Map<String, Node> actualArguments = NodeUtil.getChildrenMap(argumentMap);
    Map<String, Node> expectedArguments = Maps.newHashMap();
    expectedArguments.put("a", NodeUtil.newNumberNode(1));
    expectedArguments.put("b", NodeUtil.newNumberNode(2));
    assertThat(actualArguments).isEqualTo(expectedArguments);

    Node labelNode = testFunctionNode.getFirstChild();
    Node expectedLabelNode = NodeUtil.newLabelNode("testLabel");
    assertThat(labelNode).isInstanceOf(expectedLabelNode.getClass());
  }

  @Test
  public void testMutateFunctionWithLabelAndArgumentsInLoop() {
    String fnName = "testFunction";
    Node callNode = NodeUtil.newLabeledStatementNode(
        new Node(Token.LABEL, "testLabel"),
        NodeUtil.newCallExpressionNode(
            "testFunction",
            NodeUtil.newStringNode("a"), NodeUtil.newNumberNode(1),
            NodeUtil.newStringNode("b"), NodeUtil.newNumberNode(2)));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = true;

    Node mutatedBlock = functionToBlockMutator.mutate(fnName, testFunctionNode,
        callNode, resultName, needsDefaultResult, isCallInLoop);

    // The testFunctionNode has been mutated in place, so make sure it's the
    // same node as the one returned by mutateFunction.
    Node expectedMutatedBlock = NodeUtil.getFunctionBody(testFunctionNode);
    assertThat(expectedMutatedBlock).isEqualTo(mutatedBlock);

    // Verify that variables have been initialized.
    Node variableInitializations = testFunctionNode.getLastChild();
    Node expectedVariableInitializations = NodeUtil.newBlockNode(
        NodeUtil.newVarDeclarationListNode(
            "result", NodeUtil.newUndefinedNode(testFunctionNode.getLastChild())));
    assertThat(variableInitializations).isEqualTo(expectedVariableInitializations);

    // Verify that arguments have been inlined and the label is still present.
    Node argumentMap = testFunctionNode.getFirstChild().getFirstChild();
    Map<String, Node> actualArguments = NodeUtil.getChildrenMap(argumentMap);
    Map<String, Node> expectedArguments = Maps.newHashMap();
    expectedArguments.put("a", NodeUtil.newNumberNode(1));
    expectedArguments.put("b", NodeUtil.newNumberNode(2));
    assertThat(actualArguments).isEqualTo(expectedArguments);

    Node labelNode = testFunctionNode.getFirstChild();
    Node expectedLabelNode = NodeUtil.newLabelNode("testLabel");
    assertThat(labelNode).isInstanceOf(expectedLabelNode.getClass());
  }

  @Test(expected = AssertionError.class)
  public void testMutateFunctionWithMissingReturnStatement() {
    String fnName = "testFunction";
    Node callNode = NodeUtil.newCallExpressionNode("testFunction");
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedBlock = functionToBlockMutator.mutate(fnName, testFunctionNode,
        callNode, resultName, needsDefaultResult, isCallInLoop);

    throw new AssertionError("Expected testFunctionNode to have a return statement.");
  }
}

class DummyCompiler implements AbstractCompiler {
  @Override
  public MakeDeclaredNamesUnique getNameMangler() {
    return new MakeDeclaredNamesUnique(new DummyNameIdSupplier());
  }
}

class DummyNameIdSupplier implements Supplier<String> {
  private int idCounter = 0;

  @Override
  public String get() {
    return "id_" + idCounter++;
  }
}

class MakeDeclaredNamesUnique extends MakeDeclaredNamesUniqueBase {
  public MakeDeclaredNamesUnique(Supplier<String> nameIdSupplier) {
    super(nameIdSupplier);
  }
}

class NameCounter implements NodeTraversal.Visitor<Void> {
  private Map<String, Integer> nameOccurrences;

  public NameCounter(Map<String, Integer> nameOccurrences) {
    this.nameOccurrences = nameOccurrences;
  }

  @Override
  public Void visit(Node node) {
    if (node.getType() == Token.NAME) {
      String name = node.getString();
      nameOccurrences.merge(name, 1, Integer::sum);
    }
    return null;
  }
}