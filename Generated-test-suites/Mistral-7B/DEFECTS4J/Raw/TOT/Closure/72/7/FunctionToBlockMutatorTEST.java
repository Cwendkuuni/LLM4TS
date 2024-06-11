import static com.google.javascript.jscomp.FunctionToBlockMutator.*;
import static com.google.javascript.rhino.NodeType.*;
import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateFunctionWithNoArguments() {
    AbstractCompiler compiler = new TestCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, new Node(Token.BLOCK), null);

    Node callNode = NodeUtil.newCallNode(fnName, new Node(Token.NUM, 5));

    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    // The mutated node should have the same structure as the original node.
    assertEquals(fnNode.toStringTree(PrinterLevel.SYNTAX),
        mutatedNode.toStringTree(PrinterLevel.SYNTAX));

    // The function body should be cloned and unique local names should be assigned.
    Node mutatedFunctionBody = mutatedNode.getFirstChild();
    Node originalFunctionBody = fnNode.getFirstChild();
    assertEquals(originalFunctionBody.getNumChildren(),
        mutatedFunctionBody.getNumChildren());
    for (int i = 0; i < originalFunctionBody.getNumChildren(); i++) {
      Node originalNode = originalFunctionBody.getChild(i);
      Node mutatedNode = mutatedFunctionBody.getChild(i);
      // The names of the variables should be different.
      assertNotSame(originalNode.getFirstChild().getString(),
          mutatedNode.getFirstChild().getString());
    }
  }

  @Test
  public void testMutateFunctionWithArguments() {
    AbstractCompiler compiler = new TestCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, new Node(Token.BLOCK), null);

    Node argumentNode1 = NodeUtil.newNameNode("arg1");
    Node argumentNode2 = NodeUtil.newNameNode("arg2");
    Node callNode = NodeUtil.newCallNode(fnName, argumentNode1, argumentNode2);

    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    // The mutated node should have the same structure as the original node.
    assertEquals(fnNode.toStringTree(PrinterLevel.SYNTAX),
        mutatedNode.toStringTree(PrinterLevel.SYNTAX));

    // The function body should be cloned and unique local names should be assigned.
    Node mutatedFunctionBody = mutatedNode.getFirstChild();
    Node originalFunctionBody = fnNode.getFirstChild();
    assertEquals(originalFunctionBody.getNumChildren(),
        mutatedFunctionBody.getNumChildren());
    for (int i = 0; i < originalFunctionBody.getNumChildren(); i++) {
      Node originalNode = originalFunctionBody.getChild(i);
      Node mutatedNode = mutatedFunctionBody.getChild(i);
      // The names of the variables should be different.
      assertNotSame(originalNode.getFirstChild().getString(),
          mutatedNode.getFirstChild().getString());
    }

    // The arguments should be injected into the function body.
    Map<String, Node> argMap = FunctionArgumentInjector.getFunctionCallParameterMap(
        fnNode, callNode, safeNameIdSupplier);
    List<Node> arguments = Lists.newLinkedList();
    for (Entry<String, Node> entry : argMap.entrySet()) {
      arguments.add(entry.getValue());
    }
    Node injectedArguments = NodeUtil.getFunctionBody(mutatedNode);
    assertEquals(arguments.size(), injectedArguments.getNumChildren());
    for (int i = 0; i < arguments.size(); i++) {
      Node argument = arguments.get(i);
      Node injectedArgument = injectedArguments.getChild(i);
      // The argument nodes should be equal.
      assertEquals(argument.toStringTree(PrinterLevel.SYNTAX),
          injectedArgument.toStringTree(PrinterLevel.SYNTAX));
    }
  }

  @Test
  public void testReplaceReturns() {
    AbstractCompiler compiler = new TestCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, new Node(Token.BLOCK), null);
    fnNode.addChild(NodeUtil.newReturnNode(new Node(Token.NUM, 5)));

    String resultName = "result";
    String labelName = "testLabel";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, null,
        resultName, needsDefaultResult, isCallInLoop);

    Node expectedNode = NodeUtil.newBlockNode(
        new Node(Token.LABEL, new Node(Token.LABEL_NAME, labelName)),
        new Node(Token.BREAK, new Node(Token.LABEL_NAME, labelName)),
        new Node(Token.ASSIGN, resultName, NodeUtil.newUndefinedNode(null)));

    // The mutated node should have the same structure as the expected node.
    assertEquals(expectedNode.toStringTree(PrinterLevel.SYNTAX),
        mutatedNode.toStringTree(PrinterLevel.SYNTAX));
  }

  @Test
  public void testReplaceReturnsWithResult() {
    AbstractCompiler compiler = new TestCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, new Node(Token.BLOCK), null);
    fnNode.addChild(NodeUtil.newReturnNode(new Node(Token.NUM, 5)));

    String resultName = "result";
    String labelName = "testLabel";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, null,
        resultName, needsDefaultResult, isCallInLoop);

    Node expectedNode = NodeUtil.newBlockNode(
        new Node(Token.LABEL, new Node(Token.LABEL_NAME, labelName)),
        new Node(Token.ASSIGN, resultName, new Node(Token.NUM, 5)),
        new Node(Token.BREAK, new Node(Token.LABEL_NAME, labelName)));

    // The mutated node should have the same structure as the expected node.
    assertEquals(expectedNode.toStringTree(PrinterLevel.SYNTAX),
        mutatedNode.toStringTree(PrinterLevel.SYNTAX));
  }

  @Test
  public void testReplaceReturnsInLoop() {
    AbstractCompiler compiler = new TestCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, new Node(Token.BLOCK), null);
    fnNode.addChild(NodeUtil.newReturnNode(new Node(Token.NUM, 5)));

    String resultName = "result";
    String labelName = "testLabel";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = true;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, null,
        resultName, needsDefaultResult, isCallInLoop);

    Node expectedNode = NodeUtil.newBlockNode(
        new Node(Token.LABEL, new Node(Token.LABEL_NAME, labelName)),
        new Node(Token.ASSIGN, resultName, NodeUtil.newUndefinedNode(null)),
        new Node(Token.BREAK, new Node(Token.LABEL_NAME, labelName)));

    // The mutated node should have the same structure as the expected node.
    assertEquals(expectedNode.toStringTree(PrinterLevel.SYNTAX),
        mutatedNode.toStringTree(PrinterLevel.SYNTAX));
  }

  @Test(expected = AssertionError.class)
  public void testReplaceReturnsNoFunction() {
    AbstractCompiler compiler = new TestCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newBlockNode(new Node(Token.NUM, 5));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    functionToBlockMutator.mutate(fnName, fnNode, null, resultName, needsDefaultResult, isCallInLoop);

    fail("Expected AssertionError");
  }
}