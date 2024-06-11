package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.LinkedHashMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.FunctionToBlockMutator.LabelNameSupplier;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class FunctionToBlockMutatorTest {
  @Test
  public void testMutateFunction() {
    AbstractCompiler compiler = new TestAbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test_id";

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newFunctionNode(fnName, null, null, Node.EMPTY_NODE_ARRAY, Node.EMPTY_NODE_ARRAY);
    Node callNode = Node.newExprNode(Token.CALL, Node.newNameNode(fnName), Node.EMPTY_NODE_ARRAY);

    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // Assert that the mutated node is a block statement
    assertThat(mutatedNode.getType()).isEqualTo(Token.BLOCK);

    // TODO: Add more assertions based on the specific behavior of the mutator methods
  }

  @Test
  public void testReplaceReturns() {
    AbstractCompiler compiler = new TestAbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test_id";

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String labelName = "test_label";

    // Test with no resultName and no labelName
    Node block = Node.newBlock(Node.EMPTY_NODE_ARRAY);
    Node result = functionToBlockMutator.replaceReturns(block, null, null, false);
    assertNull(result);

    // Test with no resultName but a labelName
    block = Node.newBlock(Node.newReturnNode(Node.newNumberLiteral(1)));
    result = functionToBlockMutator.replaceReturns(block, labelName, null, false);
    assertNull(result);

    // Test with a resultName but no labelName
    resultName = "test_result";
    block = Node.newBlock(Node.newReturnNode(Node.newNumberLiteral(1)));
    result = functionToBlockMutator.replaceReturns(block, resultName, null, true);
    Node expectedResult = createAssignStatementNode(resultName, Node.newNumberLiteral(1));
    Node expectedBreak = new Node(Token.BREAK, Node.newString(Token.LABEL_NAME, labelName));
    Node expectedBlock = new Node(Token.BLOCK, Node.newList(expectedResult, expectedBreak));
    assertThat(result).isEqualTo(expectedBlock);

    // Test with a resultName and a labelName
    block = Node.newBlock(Node.newReturnNode(Node.newNumberLiteral(1)));
    result = functionToBlockMutator.replaceReturns(block, resultName, labelName, true);
    expectedResult = createAssignStatementNode(resultName, Node.newNumberLiteral(1));
    Node expectedLabel = new Node(Token.LABEL, Node.newString(Token.LABEL_NAME, labelName));
    Node expectedLabelName = new Node(Token.LABEL_NAME, Node.newString(Token.LABEL_NAME, labelName));
    Node expectedBreak = new Node(Token.BREAK, expectedLabelName);
    Node expectedBlock = new Node(Token.BLOCK, Node.newList(expectedResult, expectedLabel, expectedBreak));
    assertThat(result).isEqualTo(expectedBlock);
  }

  @Test
  public void testMutateFunctionWithArguments() {
    AbstractCompiler compiler = new TestAbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test_id";

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newFunctionNode(fnName, null, null, Node.EMPTY_NODE_ARRAY, Node.EMPTY_NODE_ARRAY);
    Node callNode = Node.newExprNode(Token.CALL, Node.newNameNode(fnName), Node.newArrayNode(Node.newString("arg1"), Node.newString("arg2")));

    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Map<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", Node.newNumberLiteral(1));
    argMap.put("arg2", Node.newNumberLiteral(2));

    Set<String> namesToAlias = Sets.newHashSet("arg1");

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // Assert that the mutated node is a block statement
    assertThat(mutatedNode.getType()).isEqualTo(Token.BLOCK);

    // TODO: Add more assertions based on the specific behavior of the mutator methods
  }

  private Node createAssignStatementNode(String name, Node expression) {
    Node nameNode = Node.newString(Token.NAME, name);
    Node assign = new Node(Token.ASSIGN, nameNode, expression);
    return NodeUtil.newExpr(assign);
  }

  private static class TestAbstractCompiler implements AbstractCompiler {
    @Override
    public void reportError(String message, int line, int column) {
      throw new RuntimeException("Compiler Error: " + message);
    }

    @Override
    public void reportWarning(String message, int line, int column) {
      // Do nothing
    }

    @Override
    public void reportFatalError(String message) {
      throw new RuntimeException("Compiler Fatal Error: " + message);
    }

    @Override
    public String getUniqueNameId() {
      return safeNameIdSupplier.get();
    }

    @Override
    public void addCompilationUnit(CompilationUnit unit) {
      // Do nothing
    }

    @Override
    public void addSourceFile(SourceFile sourceFile) {
      // Do nothing
    }

    @Override
    public void addSourceFileWithCharset(SourceFile sourceFile, String charset) {
      // Do nothing
    }

    @Override
    public void addTopLevelDeclaration(Declaration declaration) {
      // Do nothing
    }

    @Override
    public void addFunctionDeclaration(Function function) {
      // Do nothing
    }

    @Override
    public void addFunctionExpression(Function function) {
      // Do nothing
    }

    @Override
    public void addStatement(Statement statement) {
      // Do nothing
    }

    @Override
    public void addTopLevelStatement(Statement statement) {
      // Do nothing
    }

    @Override
    public void addVariable(Variable variable) {
      // Do nothing
    }

    @Override
    public void addVariableDeclaration(VariableDeclaration variableDeclaration) {
      // Do nothing
    }

    @Override
    public void addVariableDeclarationList(VariableDeclarationList variableDeclarationList) {
      // Do nothing
    }

    @Override
    public void addExpressionStatement(ExpressionStatement expressionStatement) {
      // Do nothing
    }

    @Override
    public void addDeclarationStatement(DeclarationStatement declarationStatement) {
      // Do nothing
    }

    @Override
    public void addLabel(Label label) {
      // Do nothing
    }

    @Override
    public void addTryStatement(TryStatement tryStatement) {
      // Do nothing
    }

    @Override
    public void addCatchClause(CatchClause catchClause) {
      // Do nothing
    }

    @Override
    public void addFinallyClause(FinallyClause finallyClause) {
      // Do nothing
    }

    @Override
    public void addStatementList(StatementList statementList) {
      // Do nothing
    }

    @Override
    public void addStatementListWithReturn(StatementList statementList, Node returnNode) {
      // Do nothing
    }
  }
}