package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class FunctionToBlockMutatorTest {

  @Test
  public void testMutate_BasicFunction() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, "testFunction", null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
    assertNotNull(mutatedNode);
  }

  @Test
  public void testMutate_FunctionWithArguments() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, "testFunction", null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, Node.newArray());
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    LinkedHashMap<String, Node> argMap = new LinkedHashMap<>();
    argMap.put("arg1", Node.newNumber(Token.NUMBER, 1));
    argMap.put("arg2", Node.newNumber(Token.NUMBER, 2));

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
    assertNotNull(mutatedNode);
  }

  @Test
  public void testMutate_FunctionWithNoReturn() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, "testFunction", null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
    assertNull(mutatedNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutate_NullFnName() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    String fnName = null;
    Node fnNode = Node.newFunction(Token.FUNCTION, "testFunction", null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutate_NullFnNode() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    String fnName = "testFunction";
    Node fnNode = null;
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutate_NullCallNode() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, "testFunction", null, null);
    Node callNode = null;
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
  }

  @Test
  public void testReplaceReturns_BasicFunction() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, "testFunction", null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = Node.newFunction(Token.FUNCTION, fnName, null, null);
    mutatedNode.getLastChild().setType(Token.RETURN);
    mutatedNode.getLastChild().addChild(Node.newNumber(Token.NUMBER, 5));

    Node replacementNode = functionToBlockMutator.replaceReturns(
        mutatedNode, resultName, null, needsDefaultResult);

    assertNotNull(replacementNode);
  }

  @Test
  public void testReplaceReturns_FunctionWithResult() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, "testFunction", null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = Node.newFunction(Token.FUNCTION, fnName, null, null);
    mutatedNode.getLastChild().setType(Token.RETURN);
    mutatedNode.getLastChild().addChild(Node.newName(Token.NAME, "resultName"));

    Node replacementNode = functionToBlockMutator.replaceReturns(
        mutatedNode, resultName, null, needsDefaultResult);

    assertNotNull(replacementNode);
  }

  @Test
  public void testReplaceReturns_FunctionWithoutResult() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, "testFunction", null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = Node.newFunction(Token.FUNCTION, fnName, null, null);

    Node replacementNode = functionToBlockMutator.replaceReturns(
        mutatedNode, resultName, null, needsDefaultResult);

    assertNull(replacementNode);
  }
}

class NoOpCompiler implements AbstractCompiler {
  @Override
  public void error(String message) {
    // Do nothing
  }

  @Override
  public void warning(String message) {
    // Do nothing
  }

  @Override
  public void warning(int line, String message) {
    // Do nothing
  }

  @Override
  public void warning(int line, String message, Object... args) {
    // Do nothing
  }

  @Override
  public void warning(int line, String message, Throwable t) {
    // Do nothing
  }

  @Override
  public void note(String message) {
    // Do nothing
  }

  @Override
  public void note(int line, String message) {
    // Do nothing
  }

  @Override
  public void note(int line, String message, Object... args) {
    // Do nothing
  }

  @Override
  public void note(int line, String message, Throwable t) {
    // Do nothing
  }

  @Override
  public void info(String message) {
    // Do nothing
  }

  @Override
  public void info(int line, String message) {
    // Do nothing
  }

  @Override
  public void info(int line, String message, Object... args) {
    // Do nothing
  }

  @Override
  public void info(int line, String message, Throwable t) {
    // Do nothing
  }

  @Override
  public Node visit(Node node, Node parent) {
    return node;
  }

  @Override
  public Node visit(Node node, Node parent, boolean validate) {
    return node;
  }

  @Override
  public Node visit(Node node, Node parent, boolean validate, boolean optimize) {
    return node;
  }
}