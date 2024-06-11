import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSDocInfoBuilder;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeOperations;

import java.util.List;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CompilerInput input;
  private CheckSideEffects checkSideEffects;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    input = new CompilerInput("test", null, null, null, null, null, null, null);
    CheckLevel level = CheckLevel.WARNING;
    checkSideEffects = new CheckSideEffects(compiler, level, false);
  }

  @org.junit.Test
  public void testVisitVoidNode() {
    Node node = NodeOperations.newEmptyNode();
    NodeTraversal traversal = new NodeTraversal(compiler, input, null, null);

    checkSideEffects.visit(traversal, null, null);
    assertEquals(node, traversal.getCurrentNode());
  }

  @org.junit.Test
  public void testVisitCommaNode() {
    Node node = NodeOperations.newNode(Token.COMMA);
    Node parent = NodeOperations.newBlockNode();
    NodeTraversal traversal = new NodeTraversal(compiler, input, null, null);

    parent.addChildToBack(node);
    traversal.setCurrentNode(parent);

    checkSideEffects.visit(traversal, null, parent);
  }

  @org.junit.Test
  public void testVisitSimpleOpNode() {
    Node left = NodeOperations.newNumberNode(1);
    Node operator = NodeOperations.newBinaryOperatorNode(Token.ADD, null);
    Node right = NodeOperations.newNumberNode(2);
    Node node = NodeOperations.newBinaryOperatorNode(Token.ASSIGN, null, left, operator, right);

    NodeTraversal traversal = new NodeTraversal(compiler, input, null, null);

    traversal.setCurrentNode(right);

    checkSideEffects.visit(traversal, null, null);
    assertEquals(node, traversal.getCurrentNode());
  }

  @org.junit.Test
  public void testVisitSimpleOpNodeWithUsedResult() {
    Node left = NodeOperations.newNumberNode(1);
    Node operator = NodeOperations.newBinaryOperatorNode(Token.ADD, null);
    Node right = NodeOperations.newNumberNode(2);
    Node node = NodeOperations.newBinaryOperatorNode(Token.ASSIGN, null, NodeOperations.newVariableNode("result"), operator, right);

    NodeTraversal traversal = new NodeTraversal(compiler, input, null, null);

    traversal.setCurrentNode(right);

    checkSideEffects.visit(traversal, null, null);
    assertEquals(node, traversal.getCurrentNode());
  }

  @org.junit.Test
  public void testVisitSimpleOpNodeWithSideEffect() {
    Node left = NodeOperations.newVariableNode("x");
    Node operator = NodeOperations.newBinaryOperatorNode(Token.ASSIGN, null, left, NodeOperations.newNameNode("foo"), null);
    Node node = NodeOperations.newBinaryOperatorNode(Token.ASSIGN, null, left, operator, NodeOperations.newNumberNode(42));

    NodeTraversal traversal = new NodeTraversal(compiler, input, null, null);

    traversal.setCurrentNode(node);

    checkSideEffects.visit(traversal, null, null);

    // No warning should be issued
  }

  @org.junit.Test
  public void testVisitCallNodeWithProtectorFunction() {
    String protectorFunctionName = "JSCOMPILER_PRESERVE";
    Node protectorFunctionCall = NodeOperations.newCallNode(IR.name(protectorFunctionName), null);

    Node node = NodeOperations.newBinaryOperatorNode(Token.ASSIGN, null, NodeOperations.newVariableNode("x"), NodeOperations.newNameNode("foo"), null);
    Node callNode = NodeOperations.newCallNode(protectorFunctionCall, NodeOperations.newListNode(node));

    NodeTraversal traversal = new NodeTraversal(compiler, input, null, null);

    traversal.setCurrentNode(callNode);

    checkSideEffects.visit(traversal, null, null);

    // No warning should be issued
  }

  @org.junit.Test
  public void testVisitCallNodeWithSideEffectFreeCode() {
    String protectorFunctionName = "JSCOMPILER_PRESERVE";
    Node protectorFunctionCall = NodeOperations.newCallNode(IR.name(protectorFunctionName), null);

    Node left = NodeOperations.newNumberNode(1);
    Node operator = NodeOperations.newBinaryOperatorNode(Token.ADD, null, left, NodeOperations.newNumberNode(1), null);
    Node node = NodeOperations.newBinaryOperatorNode(Token.ASSIGN, null, NodeOperations.newVariableNode("x"), operator, NodeOperations.newNumberNode(2));
    Node callNode = NodeOperations.newCallNode(protectorFunctionCall, NodeOperations.newListNode(node));

    NodeTraversal traversal = new NodeTraversal(compiler, input, null, null);

    traversal.setCurrentNode(callNode);

    checkSideEffects.visit(traversal, null, null);

    // Warning should be issued
    assertEquals(1, checkSideEffects.problemNodes.size());
    Node warningNode = checkSideEffects.problemNodes.get(0);
    assertEquals("This code lacks side-effects. Is there a bug?", warningNode.getJSDocInfo().getSummary());
  }

  @org.junit.Test
  public void testProtectSideEffects() {
    String protectorFunctionName = "JSCOMPILER_PRESERVE";
    Node protectorFunctionCall = NodeOperations.newCallNode(IR.name(protectorFunctionName), null);

    Node left = NodeOperations.newNumberNode(1);
    Node operator = NodeOperations.newBinaryOperatorNode(Token.ADD, null, left, NodeOperations.newNumberNode(1), null);
    Node node = NodeOperations.newBinaryOperatorNode(Token.ASSIGN, null, NodeOperations.newVariableNode("x"), operator, NodeOperations.newNumberNode(2));

    checkSideEffects.protectSideEffects();

    // Protected node should be replaced with a call to the protector function
    Node protectedNode = checkSideEffects.problemNodes.get(0);
    assertEquals(protectedNode.getChildCount(), 1);
    assertEquals(protectorFunctionCall, protectedNode.getFirstChild());
  }

  @org.junit.Test
  public void testStripProtection() {
    String protectorFunctionName = "JSCOMPILER_PRESERVE";
    Node protectorFunctionCall = NodeOperations.newCallNode(IR.name(protectorFunctionName), null);
    Node protectorFunctionVar = IR.var(IR.name(protectorFunctionName));

    Node left = NodeOperations.newNumberNode(1);
    Node operator = NodeOperations.newBinaryOperatorNode(Token.ASSIGN, null, NodeOperations.newVariableNode("x"), operator, NodeOperations.newNumberNode(2));
    Node node = NodeOperations.newCallNode(protectorFunctionCall, NodeOperations.newListNode(operator));

    NodeTraversal traversal = new NodeTraversal(compiler, input, null, null);

    checkSideEffects.process(input, null);
    checkSideEffects.stripProtection.process(input, null);

    // Protected node should be replaced with the original expression
    Node strippedNode = node.getLastChild();
    assertEquals(operator, strippedNode);
  }
}