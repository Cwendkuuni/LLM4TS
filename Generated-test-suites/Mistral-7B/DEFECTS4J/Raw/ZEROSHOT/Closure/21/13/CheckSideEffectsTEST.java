import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Compiler.SourceFile;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeOperations;

import java.util.List;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckLevel level;
  private CheckSideEffects checkSideEffects;
  private NodeTraversal nodeTraversal;
  private CompilerPassManager passManager;

  @org.junit.Before
  public void setUp() {
    compiler = mock(Compiler.class);
    level = CheckLevel.WARNING;
    nodeTraversal = mock(NodeTraversal.class);
    passManager = mock(CompilerPassManager.class);
    when(compiler.getSourceFile(0)).thenReturn(SourceFile.create("test", "test.js"));
    checkSideEffects = new CheckSideEffects(compiler, level, false);
    Checker checker = mock(Checker.class);
    when(compiler.getChecker()).thenReturn(checker);
  }

  @org.junit.Test
  public void testVisitVoidNode() {
    Node voidNode = NodeOperations.createVoidNode();
    Node parent = NodeOperations.createBlockNode();
    parent.addChildToBack(voidNode);

    when(nodeTraversal.getCompiler()).thenReturn(compiler);

    checkSideEffects.visit(nodeTraversal, null, parent);

    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testVisitIfStatementWithUselessCode() {
    Node ifNode = NodeOperations.createIfStatementNode(false);
    Node uselessCode = NodeOperations.createAssignment(NodeOperations.createNameNode("x"), NodeOperations.createNumberNode(42));
    ifNode.addChildToBack(uselessCode);

    Node parent = NodeOperations.createBlockNode();
    parent.addChildToBack(ifNode);

    when(nodeTraversal.getCompiler()).thenReturn(compiler);

    checkSideEffects.visit(nodeTraversal, null, parent);

    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(ifNode, checkSideEffects.problemNodes.get(0));
  }

  @org.junit.Test
  public void testVisitSimpleOperator() {
    Node opNode = NodeOperations.createAdditionNode(NodeOperations.createNameNode("x"), NodeOperations.createNumberNode(42));

    Node parent = NodeOperations.createBlockNode();
    parent.addChildToBack(opNode);

    when(nodeTraversal.getCompiler()).thenReturn(compiler);

    checkSideEffects.visit(nodeTraversal, null, parent);

    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testVisitStringConcatenation() {
    Node strNode1 = NodeOperations.createNameNode("s");
    Node strNode2 = NodeOperations.createStringLiteralNode(" this string is ");
    Node strNode3 = NodeOperations.createStringLiteralNode(" continued on the next line but you forgot the +");
    Node concatNode = NodeOperations.createBinaryOperatorNode(Token.OP_PLUS, strNode1, strNode2);
    concatNode.addChildToBack(strNode3);

    Node parent = NodeOperations.createBlockNode();
    parent.addChildToBack(concatNode);

    when(nodeTraversal.getCompiler()).thenReturn(compiler);

    checkSideEffects.visit(nodeTraversal, null, parent);

    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(concatNode, checkSideEffects.problemNodes.get(0));
  }

  @org.junit.Test
  public void testVisitFunctionCallWithNoResult() {
    Node functionCall = NodeOperations.createCallNode(NodeOperations.createNameNode("foo"), NodeOperations.createArrayNode());

    Node parent = NodeOperations.createBlockNode();
    parent.addChildToBack(functionCall);

    when(nodeTraversal.getCompiler()).thenReturn(compiler);

    checkSideEffects.visit(nodeTraversal, null, parent);

    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(functionCall, checkSideEffects.problemNodes.get(0));
  }

  @org.junit.Test
  public void testVisitFunctionCallWithResultNoSideEffect() {
    Node functionCall = NodeOperations.createCallNode(NodeOperations.createNameNode("foo"), NodeOperations.checkSideEffectsCreateArrayNode(1));

    Node parent = NodeOperations.createBlockNode();
    parent.addChildToBack(functionCall);

    when(nodeTraversal.getCompiler()).thenReturn(compiler);

    checkSideEffects.visit(nodeTraversal, null, parent);

    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testVisitFunctionCallWithResultSideEffect() {
    Node functionCall = NodeOperations.createCallNode(NodeOperations.createNameNode("foo"), NodeOperations.createArrayNode());
    Node sideEffect = NodeOperations.createAssignment(NodeOperations.createNameNode("x"), NodeOperations.createNumberNode(42));
    functionCall.addChildToBack(sideEffect);

    Node parent = NodeOperations.createBlockNode();
    parent.addChildToBack(functionCall);

    when(nodeTraversal.getCompiler()).thenReturn(compiler);

    checkSideEffects.visit(nodeTraversal, null, parent);

    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(functionCall, checkSideEffects.problemNodes.get(0));
  }

  @org.junit.Test
  public void testVisitCallWithMissingSemicolon() {
    Node functionCall = NodeOperations.createCallNode(NodeOperations.createNameNode("x"), NodeOperations.createArrayNode());
    Node parent = NodeOperations.createBlockNode();
    parent.addChildToBack(functionCall);

    when(nodeTraversal.getCompiler()).thenReturn(compiler);

    checkSideEffects.visit(nodeTraversal, null, parent);

    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testVisitCallWithSemicolon() {
    Node functionCall = NodeOperations.createCallNode(NodeOperations.createNameNode("x"), NodeOperations.createArrayNode());
    Node semicolon = NodeOperations.createTokenNode(Token.SEMICOLON);
    Node parent = NodeOperations.createBlockNode();
    parent.addChildToBack(functionCall);
    parent.addChildToBack(semicolon);

    when(nodeTraversal.getCompiler()).thenReturn(compiler);

    checkSideEffects.visit(nodeTraversal, null, parent);

    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(semicolon, checkSideEffects.problemNodes.get(0));
  }

  @org.junit.Test
  public void testVisitCallWithResultAndSideEffect() {
    Node functionCall = NodeOperations.createCallNode(NodeOperations.createNameNode("foo"), NodeOperations.checkSideEffectsCreateArrayNode(1));
    Node sideEffect = NodeOperations.createAssignment(NodeOperations.createNameNode("x"), NodeOperations.createNumberNode(42));
    functionCall.addChildToBack(sideEffect);

    Node parent = NodeOperations.createBlockNode();
    parent.addChildToBack(functionCall);

    when(nodeTraversal.getCompiler()).thenReturn(compiler);

    checkSideEffects.visit(nodeTraversal, null, parent);

    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(functionCall, checkSideEffects.problemNodes.get(0));
  }

  @org.junit.Test
  public void testVisitCallWithResultAndNoSideEffect() {
    Node functionCall = NodeOperations.createCallNode(NodeOperations.createNameNode("foo"), NodeOperations.checkSideEffectsCreateArrayNode(1));

    Node parent = NodeOperations.createBlockNode();
    parent.addChildToBack(functionCall);

    when(nodeTraversal.getCompiler()).thenReturn(compiler);

    checkSideEffects.visit(nodeTraversal, null, parent);

    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testVisitAssignStatement() {
    Node assignStatement = NodeOperations.createAssignmentNode(NodeOperations.createNameNode("x"), NodeOperations.createNumberNode(42));

    Node parent = NodeOperations.createBlockNode();
    parent.addChildToBack(assignStatement);

    when(nodeTraversal.getCompiler()).thenReturn(compiler);

    checkSideEffects.visit(nodeTraversal, null, parent);

    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(assignStatement, checkSideEffects.problemNodes.get(0));
  }

  @org.junit.Test
  public void testVisitExpressionStatement() {
    Node expressionStatement = NodeOperations.createExpressionStatementNode(NodeOperations.createNameNode("x"));

    Node parent = NodeOperations.createBlockNode();
    parent.addChildToBack(expressionStatement);

    when(nodeTraversal.getCompiler()).thenReturn(compiler);

    checkSideEffects.visit(nodeTraversal, null, parent);

    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(expressionStatement, checkSideEffects.problemNodes.get(0));
  }

  @org.junit.Test
  public void testVisitExpressionStatementWithSideEffect() {
    Node expressionStatement = NodeOperations.createExpressionStatementNode(NodeOperations.createAssignmentNode(NodeOperations.createNameNode("x"), NodeOperations.createNumberNode(42)));

    Node parent = NodeOperations.createBlockNode();
    parent.addChildToBack(expressionStatement);

    when(nodeTraversal.getCompiler()).thenReturn(compiler);

    checkSideEffects.visit(nodeTraversal, null, parent);

    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(expressionStatement, checkSideEffects.problemNodes.get(0));
  }

  @org.junit.Test
  public void testVisitExpressionStatementWithResultNoSideEffect() {
    Node expressionStatement = NodeOperations.createExpressionStatementNode(NodeOperations.createCallNode(NodeOperations.createNameNode("foo"), NodeOperations.createArrayNode()));

    Node parent = NodeOperations.createBlockNode();
    parent.addChildToBack(expressionStatement);

    when(nodeTraversal.getCompiler()).thenReturn(compiler);

    checkSideEffects.visit(nodeTraversal, null, parent);

    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testVisitForStatement() {
    Node forStatement = NodeOperations.createForStatementNode(NodeOperations.createNameNode("i"), NodeOperations.createNumberNode(1), NodeOperations.createNumberNode(1), NodeOperations.createBlockNode());

    Node parent = NodeOperations.createBlockNode();
    parent.addChildToBack(forStatement);

    when(nodeTraversal.getCompiler()).thenReturn(compiler);

    checkSideEffects.visit(nodeTraversal, null, parent);

    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testVisitBlockStatement() {
    Node blockStatement = NodeOperations.createBlockNode();

    Node parent = NodeOperations.createBlockNode();
    parent.addChildToBack(blockStatement);

    when(nodeTraversal.getCompiler()).thenReturn(compiler);

    checkSideEffects.visit(nodeTraversal, null, parent);

    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testProcess() {
    Node root = NodeOperations.createBlockNode();
    Node functionDefinition = NodeOperations.createFunctionDefinitionNode("testFunction", NodeOperations.createArrayNode(), NodeOperations.createBlockNode());
    Node statement = NodeOperations.createAssignmentNode(NodeOperations.createNameNode("x"), NodeOperations.createNumberNode(42));

    root.addChildToBack(functionDefinition);
    functionDefinition.addChildToBack(statement);

    when(nodeTraversal.getRoot()).thenReturn(root);

    checkSideEffects.process(null, root);

    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(statement, checkSideEffects.problemNodes.get(0));
  }

  @org.junit.Test
  public void testProtectSideEffects() {
    Node root = NodeOperations.createBlockNode();
    Node functionDefinition = NodeOperations.createFunctionDefinitionNode("testFunction", NodeOperations.createArrayNode(), NodeOperations.createBlockNode());
    Node statement = NodeOperations.createAssignmentNode(NodeOperations.createNameNode("x"), NodeOperations.createNumberNode(42));

    root.addChildToBack(functionDefinition);
    functionDefinition.addChildToBack(statement);

    when(nodeTraversal.getRoot()).thenReturn(root);

    checkSideEffects.protectSideEffects();

    // The protected nodes should be added as the last child of the parent node.
    Node parent = functionDefinition;
    List<Node> parentNodes = Lists.newArrayList();
    while (parent != null) {
      parentNodes.add(parent);
      parent = parent.getParent();
    }
    parentNodes.reverse();

    for (Node parentNode : parentNodes) {
      assertEquals(0, parentNode.getChildCount());
    }

    Node protectedNode = parentNodes.get(0).getLastChild();
    assertEquals(Node.CALL, protectedNode.getType());
    assertEquals(NodeOperations.createNameNode(CheckSideEffects.PROTECTOR_FN), protectedNode.getFirstChild());
    Node protectedBlock = protectedNode.getLastChild();
    assertEquals(Node.BLOCK, protectedBlock.getType());
    assertEquals(statement, protectedBlock.getFirstChild());
  }

  @org.junit.Test
  public void testStripProtection() {
    Node root = NodeOperations.createBlockNode();
    Node functionDefinition = NodeOperations.createFunctionDefinitionNode("testFunction", NodeOperations.createArrayNode(), NodeOperations.createBlockNode());
    Node statement = NodeOperations.createAssignmentNode(NodeOperations.createNameNode("x"), NodeOperations.createNumberNode(42));
    Node callNode = NodeOperations.createCallNode(NodeOperations.createNameNode(CheckSideEffects.PROTECTOR_FN), NodeOperations.createArrayNode());
    Node protectedBlock = NodeOperations.createBlockNode();
    protectedBlock.addChildToBack(statement);
    callNode.addChildToBack(protectedBlock);
    statement.addChildToBack(NodeOperations.createNumberNode(43));

    functionDefinition.addChildToBack(callNode);

    when(nodeTraversal.getRoot()).thenReturn(root);

    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    stripProtection.process(null, root);

    assertEquals(1, functionDefinition.getChildCount());
    assertEquals(statement, functionDefinition.getChild(0));
  }
}