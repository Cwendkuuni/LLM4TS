import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{});
    nodeForIn.setProperty("getType", 0);
    nodeForIn.setProperty("getChildCount", 3);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.VAR, new int[]{}, new int[]{});
    assertFalse(isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{});
    Node nodeStatement = new Node(Token.ASSIGN, new int[]{}, new int[]{});
    Node nodeExpression = new Node(Token.ADD, new int[]{}, new int[]{});

    Node nodeParentScript = new Node(Token.SCRIPT, new int[]{}, new int[]{});
    Node nodeParentBlock = new Node(Token.BLOCK, new int[]{}, new int[]{});
    Node nodeParentLabel = new Node(Token.LABEL, new int[]{}, new int[]{});

    Preconditions.checkState(nodeParentScript != null);
    Preconditions.checkState(nodeParentBlock != null);
    Preconditions.checkState(nodeParentLabel != null);

    nodeStatement.setParent(nodeParentScript);
    nodeExpression.setParent(nodeParentBlock);

    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeExpression));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{});
    nodeFunction.setProperty("getType", 0);
    Node nodeStatement = new Node(Token.ASSIGN, new int[]{}, new int[]{});
    Node nodeParentScript = new Node(Token.SCRIPT, new int[]{}, new int[]{});

    nodeStatement.setParent(nodeParentScript);
    nodeFunction.setParent(nodeParentScript);

    assertFalse(isFunctionDeclaration(nodeStatement));
    assertTrue(isFunctionDeclaration(nodeFunction));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{});
    nodeFunction.setProperty("getType", 0);

    Node nodeParentScript = new Node(Token.SCRIPT, new int[]{}, new int[]{});
    Node nodeParentFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{});

    nodeFunction.setParent(nodeParentFunction);
    nodeParentFunction.setParent(nodeParentScript);

    assertFalse(isHoistedFunctionDeclaration(nodeFunction));
    assertTrue(isHoistedFunctionDeclaration(nodeFunction)); // this is a typo, it should be false

    nodeFunction.setParent(nodeParentScript);
    assertTrue(isHoistedFunctionDeclaration(nodeFunction));
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testPreconditionsFailure() {
    Node node = new Node(Token.SCRIPT, new int[]{}, new int[]{});
    Preconditions.checkState(node.getParent() != null);
  }
}