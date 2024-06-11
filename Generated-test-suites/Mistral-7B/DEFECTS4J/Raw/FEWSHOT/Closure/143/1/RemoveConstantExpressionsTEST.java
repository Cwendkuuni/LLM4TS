package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.Token;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class RemoveConstantExpressionsTest {
  private Compiler compiler;
  private RemoveConstantExpressions removeConstantExpressions;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = mock(Compiler.class);
    removeConstantExpressions = new RemoveConstantExpressions(compiler);
    rootNode = new Node(Token.PROGRAM, null);
  }

  @Test
  public void testRemoveConstantExpressions() {
    Node expressionNode1 = new Node(Token.EXPR_RESULT, null);
    Node constantExpression1 = new Node(Token.NUMBER, 1);
    Node additionNode1 = new Node(Token.ADD, null, expressionNode1, constantExpression1);

    Node expressionNode2 = new Node(Token.EXPR_RESULT, null);
    Node constantExpression2 = new Node(Token.NUMBER, 2);
    Node additionNode2 = new Node(Token.ADD, null, expressionNode2, constantExpression2);

    Node expressionNode3 = new Node(Token.EXPR_RESULT, null);
    Node functionCallNode = new Node(Token.CALL, null, expressionNode3, new Node(Token.IDENTIFIER, "foo"));

    Node expressionStatementNode1 = new Node(Token.EXPR_STATEMENT, null, additionNode1);
    Node expressionStatementNode2 = new Node(Token.EXPR_STATEMENT, null, additionNode2);
    Node expressionStatementNode3 = new Node(Token.EXPR_STATEMENT, null, functionCallNode);

    rootNode.setFirstChild(expressionStatementNode1);
    rootNode.setNextSibling(expressionStatementNode2);
    rootNode.setNextSibling(expressionStatementNode3);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<List<Node>> listCaptor = ArgumentCaptor.forClass(List.class);

    removeConstantExpressions.process(null, rootNode);

    Node actualParentNode1 = null;
    Node actualParentNode2 = null;

    when(compiler.getErrorReporter()).thenReturn(node -> {});
    NodeTraversal traversal = new NodeTraversal(rootNode);
    traversal.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal traversal, Node node, Node parent) {
        if (parent == expressionStatementNode1) {
          actualParentNode1 = parent;
        } else if (parent == expressionStatementNode2) {
          actualParentNode2 = parent;
        }
      }
    });

    assertThat(actualParentNode1).isNotNull();
    assertThat(actualParentNode2).isNotNull();

    List<Node> expectedChildrenNode1 = Lists.newArrayList(functionCallNode);
    Node newExpressionStatementNode1 = new Node(Token.EXPR_STATEMENT, null, functionCallNode);
    changeNodeRecursively(actualParentNode1, newExpressionStatementNode1, new Node(Token.EXPR_STATEMENT, null, expressionNode3));

    List<Node> expectedChildrenNode2 = Lists.newArrayList();
    Node newExpressionStatementNode2 = new Node(Token.EXPR_STATEMENT, null);
    changeNodeRecursively(actualParentNode2, newExpressionStatementNode2, null);

    List<Node> expectedChildren = Lists.newArrayList(expectedChildrenNode1, expectedChildrenNode2);

    Node actualParent = actualParentNode1.getParent();
    Node actual = actualParent.getFirstChild();

    assertThat(actual).isNotNull();
    assertThat(actual.getFirstChild()).isNull();
    assertThat(actual.getNextSibling()).isNotNull();
    assertThat(actual.getNextSibling().getFirstChild()).isNull();
    assertThat(actual.getNextSibling().getNextSibling()).isNull();

    Node expected = newNode(Token.EXPR_STATEMENT, expectedChildren.toArray(new Node[0]));
    assertThat(actual).isEqualTo(expected);
  }

  private Node newNode(int type, Node... children) {
    return new Node(type, null, children);
  }

  private void changeNodeRecursively(Node oldNode, Node newNode, Node replacementNode) {
    oldNode.getParent().getFirstChild().setFirstChild(newNode);
    for (Node child = oldNode.getFirstChild(); child != null; child = child.getNextSibling()) {
      changeNodeRecursively(child, newNode, replacementNode);
    }
    oldNode.setParent(null);
    if (replacementNode != null) {
      replacementNode.setParent(oldNode.getParent());
    }
  }
}

class NodeUtil {
  static boolean nodeTypeMayHaveSideEffects(Node node) {
    return nodeTypeMayHaveSideEffects(node.getType());
  }

  static boolean nodeTypeMayHaveSideEffects(int type) {
    switch (type) {
      case Token.ASSIGN:
      case Token.ASSIGN_ADD:
      case Token.ASSIGN_SUB:
      case Token.ASSIGN_DIV:
      case Token.ASSIGN_MOD:
      case Token.ASSIGN_BIT_OR:
      case Token.ASSIGN_BIT_XOR:
      case Token.ASSIGN_BIT_AND:
      case Token.ASSIGN_LEFT_SHIFT:
      case Token.ASSIGN_RIGHT_SHIFT:
      case Token.CALL:
      case Token.DECLARE_VAR:
      case Token.DELETE:
      case Token.EXPR_STATEMENT:
      case Token.FOR_STATEMENT:
      case Token.IF:
      case Token.INDEX:
      case Token.IN:
      case Token.INSTANCE_OF:
      case Token.LOGICAL_AND:
      case Token.LOGICAL_OR:
      case Token.RETURN:
      case Token.SWITCH:
      case Token.THROW:
      case Token.TRY:
      case Token.VAR_DECLARE_LIST:
      case Token.WHILE:
        return true;
      default:
        return false;
    }
  }
}