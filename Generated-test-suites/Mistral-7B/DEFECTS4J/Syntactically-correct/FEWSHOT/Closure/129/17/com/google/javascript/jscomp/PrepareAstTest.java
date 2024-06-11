package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    Node root = NodeFactory.objectLiteral();
    Node block = IR.block();
    Node call = NodeFactory.call(null, "testFunction", null);
    Node exprVoid = NodeFactory.expressionStatement(call);
    Node exprResult = IR.expressionStatement(NodeFactory.assign(block, IR.literal(1)));
    root.addChildrenToFront(exprVoid, exprResult);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    assertTrue(root.getType() == Token.OBJECT_LITERAL);
    assertTrue(exprVoid.getType() == Token.EXPR_VOID);
    assertTrue(exprResult.getType() == Token.EXPR_RESULT);

    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root);

    checkState(false, "normalizeNodeType constraints violated");
  }

  @Test
  public void testNormalizeBlocks() {
    Node root = NodeFactory.ifStatement(
        NodeFactory.booleanLiteral(true),
        NodeFactory.block(),
        NodeFactory.block());

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    Node ifBlock = root.getFirstChild();
    Node ifCondition = ifBlock.getFirstChild();

    assertTrue(ifBlock.isBlock());
    assertTrue(ifCondition.isExpressionStatement());

    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root);

    checkState(false, "normalizeNodeType constraints violated");
  }

  @Test
  public void testPrepareAnnotations() {
    Node root = NodeFactory.function(
        NodeFactory.name("testFunction"),
        NodeFactory.block(),
        NodeFactory.emptyStatementList());

    Node objlit = NodeFactory.objectLiteral();
    Node key = NodeFactory.name("a");
    Node value = NodeFactory.function(
        NodeFactory.name("testFunctionInner"),
        NodeFactory.block(),
        NodeFactory.emptyStatementList());
    objlit.addChildrenToFront(key, value);

    Node assign = NodeFactory.assign(
        NodeFactory.name("x"),
        NodeFactory.call(NodeFactory.name("eval"), null, objlit));

    Node parent = NodeFactory.assign(
        NodeFactory.name("parent"),
        NodeFactory.objectLiteral());
    Node dispatcher = NodeFactory.function(
        NodeFactory.name("dispatcher"),
        NodeFactory.block(),
        NodeFactory.emptyStatementList());
    parent.addChildrenToFront(dispatcher);

    Node call = NodeFactory.call(
        parent,
        "testFunction",
        NodeFactory.emptyArgumentList());

    Node function = NodeFactory.function(
        NodeFactory.name("testFunctionWithDispatcher"),
        NodeFactory.block(),
        NodeFactory.emptyStatementList());
    Node statements = NodeFactory.statementList();
    statements.addChild(assign);
    statements.addChild(call);
    function.addChildrenToFront(statements, root);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, function);

    assertTrue(dispatcher.isFunction());
    assertTrue(dispatcher.getBooleanProp(Node.IS_DISPATCHER));
    assertTrue(call.getBooleanProp(Node.IS_DISPATCHER_CALL));

    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, function);

    checkState(false, "normalizeNodeType constraints violated");
  }

  @Test
  public void testTraverse() {
    Node root = NodeFactory.block();
    Node call1 = NodeFactory.call(null, "testFunction1", null);
    Node call2 = NodeFactory.call(null, "testFunction2", null);
    root.addChildrenToFront(call1, call2);

    PrepareAst prepareAst = new PrepareAst(null, false);
    NodeTraversal.traverse(
        prepareAst.compiler, root, new PrepareAnnotations());

    assertTrue(call1.getBooleanProp(Node.FREE_CALL));
    assertTrue(call2.getBooleanProp(Node.FREE_CALL));
  }

  @Test
  public void testTraverseWithDirectEval() {
    Node root = NodeFactory.block();
    Node call1 = NodeFactory.assign(
        NodeFactory.name("x"),
        NodeFactory.call(null, "testFunction1", null));
    Node call2 = NodeFactory.call(null, "eval", NodeFactory.list(NodeFactory.literal("1")), null);
    root.addChildrenToFront(call1, call2);

    PrepareAst prepareAst = new PrepareAst(null, false);
    NodeTraversal.traverse(
        prepareAst.compiler, root, new PrepareAnnotations());

    assertTrue(call1.getBooleanProp(Node.FREE_CALL));
    assertTrue(call2.getBooleanProp(Node.DIRECT_EVAL));
  }
}