import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FORIN, new int[]{}, new Node[3]);
    assertTrue("Should return true for FOR-IN loop node.", isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{}, new Node[0]);
    assertFalse("Should return false for non-FOR-IN node.", isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[]{}, new Node[2]);
    assertTrue("Should return true for node that is a statement.", isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.PLUS, new int[]{}, new Node[] { Node.newNumber(1) });
    assertFalse("Should return false for node that is an expression.", isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new Node[2]);
    nodeFunctionDeclaration.setName(Node.newIdentifier("myFunction"));
    assertTrue("Should return true for function declaration node.", isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[]{}, new Node[1]);
    nodeFunctionExpression.setName(Node.newIdentifier("myFunction"));
    assertFalse("Should return false for function expression node.", isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test(expected = AssertionError.class)
  public void testCheckParentStateFailure() {
    Node nodeParentNull = Node.newNode(Token.SCRIPT, null, new Node[0]);
    Node nodeChild = Node.newNode(Token.IF, new int[]{}, new Node[0]);
    nodeChild.setParent(nodeParentNull);
    isStatement(nodeChild);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{}, new Node[0]);
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new Node[2]);
    nodeFunctionDeclaration.setName(Node.newIdentifier("myFunction"));
    nodeFunctionDeclaration.setParent(nodeScript);
    assertTrue("Should return true for hoisted function declaration.", isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[]{}, new Node[2]);
    nodeFunctionDeclarationInFunction.setName(Node.newIdentifier("myFunction"));
    Node nodeInnerFunction = Node.newNode(Token.FUNCTION, new int[]{}, new Node[2]);
    nodeFunctionDeclarationInFunction.addChild(nodeInnerFunction);
    nodeFunctionDeclarationInFunction.setParent(Node.newNode(Token.FUNCTION, new int[]{}, new Node[0]));
    assertFalse("Should return false for function declaration in a function.", isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}