package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.javascript.jscomp.NodeUtil.isBlock;
import static com.google.javascript.jscomp.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.NodeUtil.isIf;
import static com.google.javascript.jscomp.NodeUtil.isLabel;
import static com.google.javascript.jscomp.NodeUtil.isTry;
import static com.google.javascript.jscomp.MinimizeExitPoints.tryMinimizeExits;

import com.google.common.collect.ImmutableList;
import com.google.common.truth.Truth;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MinimizeExitPointsTest {

  private static final String SOURCE_CODE =
      "function foo() {"
          + "if (true) {"
          + "  return;"
          + "} "
          + "if (false) {"
          + "  return;"
          + "} "
          + "if (false) {"
          + "  return;"
          + "} "
          + "}";

  private AbstractCompiler compiler;
  private Node rootNode;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    options.setAssumeFunctionCallsAreValid(true);
    options.setKeepSemicolons(true);
    this.compiler = new Compiler(options);
    this.rootNode = NodeFactory.parse(SOURCE_CODE);
  }

  @Test
  public void testMinimizeExitPoints_empty() {
    NodeTraversal traversal = new NodeTraversal(compiler);
    new MinimizeExitPoints(compiler).process(null, rootNode);
    NodeTraversal.traverse(compiler, rootNode, traversal);
    Truth.assertThat(isBlock(rootNode)).isTrue();
    Truth.assertThat(rootNode.getLastChild()).isNull();
  }

  @Test
  public void testMinimizeExitPoints_singleReturn() {
    Node n = NodeUtil.makeLabeledStatement(
        NodeFactory.createLabelNode("label1"), NodeFactory.createReturnNode());
    Node parent = NodeFactory.createBlockNode();
    parent.addChildToBack(n);

    tryMinimizeExits(parent, Token.RETURN, "label1");
    Truth.assertThat(parent.getLastChild()).isNull();
  }

  @Test
  public void testMinimizeExitPoints_singleBreak() {
    Node n = NodeFactory.createLabelNode("label1");
    Node loop = NodeFactory.createBreakStatement(n);
    Node parent = NodeFactory.createBlockNode();
    parent.addChildToBack(NodeFactory.createStatementList(loop));

    tryMinimizeExits(parent, Token.BREAK, "label1");
    Truth.assertThat(parent.getLastChild()).isNull();
  }

  @Test
  public void testMinimizeExitPoints_singleContinue() {
    Node n = NodeFactory.createLabelNode("label1");
    Node loop = NodeFactory.createContinueStatement(n);
    Node parent = NodeFactory.createBlockNode();
    parent.addChildToBack(NodeFactory.createStatementList(loop));

    tryMinimizeExits(parent, Token.CONTINUE, "label1");
    Truth.assertThat(parent.getLastChild()).isNull();
  }

  @Test
  public void testMinimizeExitPoints_multipleReturns() {
    Node n = NodeFactory.createBlockNode();
    Node ifBlock = NodeFactory.createIfStatement(
        NodeFactory.createBooleanLiteral(true), NodeFactory.createBlockNode());
    Node elseBlock = NodeFactory.createBlockNode();
    n.addChildToBack(ifBlock);
    n.addChildToBack(elseBlock);

    Node firstReturn = NodeFactory.createReturnNode();
    Node secondReturn = NodeFactory.createReturnNode();
    ifBlock.addChildToBack(firstReturn);
    elseBlock.addChildToBack(secondReturn);

    tryMinimizeExits(n, Token.RETURN, null);
    Truth.assertThat(n.getLastChild()).isNull();
  }

  @Test
  public void testMinimizeExitPoints_labeledBreak() {
    Node n = NodeFactory.createBlockNode();
    Node ifBlock = NodeFactory.createIfStatement(
        NodeFactory.createBooleanLiteral(true), NodeFactory.createBlockNode());
    Node elseBlock = NodeFactory.createBlockNode();
    Node label = NodeFactory.createLabelNode("label1");
    Node breakStatement = NodeFactory.createLabeledStatement(label, NodeFactory.createBreakStatement(label));
    Node firstStatement = NodeFactory.createStatementList(ifBlock);
    Node secondStatement = NodeFactory.createStatementList(elseBlock, breakStatement);
    n.addChildToBack(firstStatement);
    n.addChildToBack(secondStatement);

    tryMinimizeExits(n, Token.BREAK, "label1");
    Truth.assertThat(n.getLastChild().getFirstChild()).isInstanceOf(NodeFactory.class);
    Truth.assertThat(n.getLastChild().getFirstChild().getType()).isEqualTo(Token.BLOCK);
  }

  @Test
  public void testMinimizeExitPoints_tryCatch() {
    Node tryBlock = NodeFactory.createTryStatement(
        NodeFactory.createBlockNode(), NodeFactory.createBlockNode(), NodeFactory.createBlockNode());
    Node catchBlock = NodeFactory.createBlockNode();
    Node finallyBlock = NodeFactory.createBlockNode();

    tryBlock.setFirstChild(NodeFactory.createBlockNode());
    catchBlock.addChildToBack(NodeFactory.createReturnNode());
    finallyBlock.addChildToBack(NodeFactory.createReturnNode());

    tryMinimizeExits(tryBlock, Token.RETURN, null);
    Truth.assertThat(tryBlock.getLastChild()).isNull();

    Truth.assertThat(catchBlock.getFirstChild()).isInstanceOf(NodeFactory.class);
    Truth.assertThat(catchBlock.getFirstChild().getType()).isEqualTo(Token.BLOCK);

    Truth.assertThat(finallyBlock.getFirstChild()).isInstanceOf(NodeFactory.class);
    Truth.assertThat(finallyBlock.getFirstChild().getType()).isEqualTo(Token.BLOCK);
  }

  @Test
  public void testMinimizeExitPoints_tryCatchFinally_multipleReturns() {
    Node tryBlock = NodeFactory.createTryStatement(
        NodeFactory.createBlockNode(), NodeFactory.createBlockNode(), NodeFactory.createBlockNode());
    Node catchBlock = NodeFactory.createBlockNode();
    Node finallyBlock = NodeFactory.createBlockNode();

    Node tryBlockChild = NodeFactory.createBlockNode();
    Node firstReturn = NodeFactory.createReturnNode();
    Node secondReturn = NodeFactory.createReturnNode();
    Node ifStatement = NodeFactory.createIfStatement(
        NodeFactory.createBooleanLiteral(true), NodeFactory.createBlockNode());
    Node elseStatement = NodeFactory.createBlockNode();

    tryBlockChild.addChildToBack(ifStatement);
    tryBlockChild.addChildToBack(elseStatement);

    tryBlock.setFirstChild(tryBlockChild);
    catchBlock.addChildToBack(firstReturn);
    finallyBlock.addChildToBack(secondReturn);

    tryMinimizeExits(tryBlock, Token.RETURN, null);
    Truth.assertThat(tryBlock.getLastChild()).isNull();

    Truth.assertThat(catchBlock.getFirstChild()).isInstanceOf(NodeFactory.class);
    Truth.assertThat(catchBlock.getFirstChild().getType()).isEqualTo(Token.BLOCK);

    Truth.assertThat(finallyBlock.getFirstChild()).isInstanceOf(NodeFactory.class);
    Truth.assertThat(finallyBlock.getFirstChild().getType()).isEqualTo(Token.BLOCK);

    Truth.assertThat(tryBlockChild.getFirstChild()).isNull();
  }

  @Test
  public void testMinimizeExitPoints_forLoop() {
    Node forLoop = NodeFactory.createForStatement(
        NodeFactory.createVariableDeclarationList(ImmutableList.of(NodeFactory.createVariable("i", NodeFactory.createNullLiteral()))),
        NodeFactory.createExpressionStatement(NodeFactory.createAssignment(
            NodeFactory.createName("i"), NodeFactory.createNumberLiteral(0))),
        NodeFactory.createExpressionStatement(NodeFactory.createAssignment(
            NodeFactory.createName("i"), NodeFactory.createAddition(
                NodeFactory.createName("i"), NodeFactory.createNumberLiteral(1)))),
        NodeFactory.createBlockNode(
            NodeFactory.createExpressionStatement(NodeFactory.createPrintStatement(NodeFactory.createStringLiteral("i = "), NodeFactory.createName("i"))),
            NodeFactory.createIfStatement(
                NodeFactory.createExpressionStatement(NodeFactory.createAssign(
                    NodeFactory.createName("i"), NodeFactory.createSubtraction(NodeFactory.createName("i"), NodeFactory.createNumberLiteral(10)))),
                NodeFactory.createBlockNode(), NodeFactory.createBlockNode())));

    Node parent = NodeFactory.createBlockNode();
    parent.addChildToBack(forLoop);

    tryMinimizeExits(parent, Token.CONTINUE, null);
    Truth.assertThat(parent.getLastChild()).isNull();
  }

  @Test
  public void testMinimizeExitPoints_whileLoop() {
    Node whileLoop = NodeFactory.createWhileStatement(
        NodeFactory.createExpressionStatement(NodeFactory.createAssign(
            NodeFactory.createName("i"), NodeFactory.createSubtraction(
                NodeFactory.createName("i"), NodeFactory.createNumberLiteral(1)))),
        NodeFactory.createBlockNode());

    Node parent = NodeFactory.createBlockNode();
    parent.addChildToBack(whileLoop);

    tryMinimizeExits(parent, Token.CONTINUE, null);
    Truth.assertThat(parent.getLastChild()).isNull();
  }

  @Test
  public void testMinimizeExitPoints_doWhileLoop() {
    Node doWhileLoop = NodeFactory.createDoWhileStatement(
        NodeFactory.createBlockNode(),
        NodeFactory.createExpressionStatement(NodeFactory.createAssign(
            NodeFactory.createName("i"), NodeFactory.createSubtraction(
                NodeFactory.createName("i"), NodeFactory.createNumberLiteral(1)))));

    Node parent = NodeFactory.createBlockNode();
    parent.addChildToBack(doWhileLoop);

    tryMinimizeExits(parent, Token.CONTINUE, null);
    Truth.assertThat(parent.getLastChild()).isNull();
  }

  @Test
  public void testMinimizeExitPoints_ifStatement() {
    Node ifStatement = NodeFactory.createIfStatement(
        NodeFactory.createExpressionStatement(NodeFactory.createAssign(
            NodeFactory.createName("i"), NodeFactory.createNumberLiteral(10))),
        NodeFactory.createBlockNode(), NodeFactory.createBlockNode());

    Node parent = NodeFactory.createBlockNode();
    parent.addChildToBack(ifStatement);

    tryMinimizeExits(parent, Token.RETURN, null);
    Truth.assertThat(parent.getLastChild()).isNull();
  }

  @Test
  public void testMinimizeExitPoints_ifElseStatement() {
    Node ifStatement = NodeFactory.createIfStatement(
        NodeFactory.createExpressionStatement(NodeFactory.createAssign(
            NodeFactory.createName("i"), NodeFactory.createNumberLiteral(10))),
        NodeFactory.createBlockNode(), NodeFactory.createBlockNode());

    Node elseStatement = NodeFactory.createBlockNode();
    Node returnStatement = NodeFactory.createReturnNode();
    elseStatement.addChildToBack(returnStatement);

    Node parent = NodeFactory.createBlockNode();
    parent.addChildToBack(ifStatement);
    parent.addChildToBack(elseStatement);

    tryMinimizeExits(parent, Token.RETURN, null);
    Truth.assertThat(parent.getLastChild()).isNull();
  }

  @Test
  public void testMinimizeExitPoints_ifElseIfStatement() {
    Node ifStatement = NodeFactory.createIfStatement(
        NodeFactory.createExpressionStatement(NodeFactory.createAssign(
            NodeFactory.createName("i"), NodeFactory.createNumberLiteral(10))),
        NodeFactory.createBlockNode(), NodeFactory.createBlockNode());

    Node elseIfStatement = NodeFactory.createIfStatement(
        NodeFactory.createExpressionStatement(NodeFactory.createAssign(
            NodeFactory.createName("i"), NodeFactory.createNumberLiteral(5))),
        NodeFactory.createBlockNode(), NodeFactory.createBlockNode());

    Node elseStatement = NodeFactory.createBlockNode();
    Node returnStatement = NodeFactory.createReturnNode();
    elseStatement.addChildToBack(returnStatement);

    Node parent = NodeFactory.createBlockNode();
    parent.addChildToBack(ifStatement);
    parent.addChildToBack(elseIfStatement);
    parent.addChildToBack(elseStatement);

    tryMinimizeExits(parent, Token.RETURN, null);
    Truth.assertThat(parent.getLastChild()).isNull();
  }

  @Test
  public void testMinimizeExitPoints_multipleExits() {
    Node ifStatement = NodeFactory.createIfStatement(
        NodeFactory.createExpressionStatement(NodeFactory.createAssign(
            NodeFactory.createName("i"), NodeFactory.createNumberLiteral(10))),
        NodeFactory.createBlockNode(), NodeFactory.createBlockNode());

    Node elseIfStatement = NodeFactory.createIfStatement(
        NodeFactory.createExpressionStatement(NodeFactory.createAssign(
            NodeFactory.createName("i"), NodeFactory.createNumberLiteral(5))),
        NodeFactory.createBlockNode(), NodeFactory.createBlockNode());

    Node elseStatement = NodeFactory.createBlockNode();
    Node firstReturnStatement = NodeFactory.createReturnNode();
    Node secondReturnStatement = NodeFactory.createReturnNode();
    elseStatement.addChildToBack(firstReturnStatement);
    elseStatement.addChildToBack(secondReturnStatement);

    Node parent = NodeFactory.createBlockNode();
    parent.addChildToBack(ifStatement);
    parent.addChildToBack(elseIfStatement);
    parent.addChildToBack(elseStatement);

    tryMinimizeExits(parent, Token.RETURN, null);
    Truth.assertThat(parent.getLastChild()).isNull();
  }

  @Test
  public void testMinimizeExitPoints_functionDeclaration() {
    Node functionDeclaration = NodeFactory.createFunctionDeclaration(
        "foo", ImmutableList.of(), NodeFactory.createBlockNode(
            NodeFactory.createReturnStatement(NodeFactory.createNumberLiteral(42))));

    Node parent = NodeFactory.createBlockNode();
    parent.addChildToBack(functionDeclaration);

    tryMinimizeExits(parent, Token.RETURN, null);
    Truth.assertThat(parent.getLastChild()).isNull();
  }

  @Test
  public void testMinimizeExitPoints_multipleFunctionDeclarations() {
    Node functionDeclaration1 = NodeFactory.createFunctionDeclaration(
        "foo", ImmutableList.of(), NodeFactory.createBlockNode(
            NodeFactory.createReturnStatement(NodeFactory.createNumberLiteral(42))));

    Node functionDeclaration2 = NodeFactory.createFunctionDeclaration(
        "bar", ImmutableList.of(), NodeFactory.createBlockNode(
            NodeFactory.createReturnStatement(NodeFactory.createNumberLiteral(43))));

    Node parent = NodeFactory.createBlockNode();
    parent.addChildToBack(functionDeclaration1);
    parent.addChildToBack(functionDeclaration2);

    tryMinimizeExits(parent, Token.RETURN, null);
    Truth.assertThat(parent.getLastChild()).isNull();
  }
}