import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isForIn;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isHoistedFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isStatement;
import static com.google.common.base.Preconditions.checkState;

import org.junit.Test;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] { 0, 0, 0 }, new Node[3]);
    assertTrue("Should be FOR-IN loop", isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[0], new Node[0]);
    assertFalse("Should not be FOR-IN loop", isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.SCRIPT, new int[0], new Node[0]);
    Node nodeExpression = Node.newNode(Token.FUNCTION, new int[0], new Node[0]);
    Node nodeLabel = Node.newNode(Token.LABEL, new int[0], new Node[0]);

    assertTrue("Should be statement", isStatement(nodeStatement));
    assertTrue("Should be statement", isStatement(nodeLabel));

    assertFalse("Should not be statement", isStatement(nodeExpression));
  }

  @Test(expected = AssertionError.class)
  public void testIsNullParent() {
    Node node = Node.newNode(Token.FUNCTION, new int[0], new Node[0]);
    Node nullParent = null;
    node.setParent(nullParent);
    NodeUtil.isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] { 1 }, new Node[] {
        Node.newIdent("foo"),
        Node.newBlock(new Node[0])
    });
    assertTrue("Should be function declaration", isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[0], new Node[0]);
    nodeFunctionExpression.setProperty("args", Node.newArray(new Node[0]));
    nodeFunctionExpression.setProperty("body", Node.newBlock(new Node[0]));
    assertFalse("Should not be function declaration", isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[0], new Node[0]);
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] { 1 }, new Node[] {
        Node.newIdent("foo"),
        Node.newBlock(new Node[0])
    });
    nodeScript.setChild(0, nodeFunctionDeclaration);

    assertTrue("Should be hoisted function declaration", isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[] { 1, 1 }, new Node[] {
        Node.newIdent("foo"),
        Node.newBlock(new Node[0])
    });
    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[0], new Node[0]);
    nodeFunction.setProperty("body", Node.newBlock(new Node[0]));
    nodeFunction.setChild(0, nodeFunctionDeclarationInFunction);

    assertFalse("Should not be hoisted function declaration", isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}