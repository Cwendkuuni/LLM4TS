import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new Node[]{});
    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new Node[]{});
    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new Node[]{});
    Node nodeExpression = new Node(Token.FUNCTION, new int[]{}, new Node[]{});
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new Node[]{});
    Node nodeBlock = new Node(Token.BLOCK, new int[]{}, new Node[]{});
    Node nodeLabel = new Node(Token.LABEL, new int[]{}, new Node[]{});

    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeExpression));
    assertTrue(isStatement(nodeScript));
    assertTrue(isStatement(nodeBlock));
    assertFalse(isStatement(nodeLabel));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(
        Token.FUNCTION, new int[]{}, new Node[]{new Node(Token.ID, new int[]{}, new String[]{"myFunction"})});
    Node nodeFunctionExpression = new Node(
        Token.FUNCTION, new int[]{}, new Node[]{new Node(Token.ID, new int[]{}, new String[]{"myFunction"})}, new Node[]{});

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new Node[]{
      new Node(Token.FUNCTION, new int[]{}, new Node[]{
        new Node(Token.ID, new int[]{}, new String[]{"myFunction"})
      })
    });
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new Node[]{
      new Node(Token.ID, new int[]{}, new String[]{"myFunction"})
    });
    Node nodeBlock = new Node(Token.BLOCK, new int[]{}, new Node[]{
      new Node(Token.FUNCTION, new int[]{}, new Node[]{
        new Node(Token.ID, new int[]{}, new String[]{"myFunction"})
      })
    });

    assertTrue(isHoistedFunctionDeclaration(nodeScript.getFirstChild()));
    assertFalse(isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(isHoistedFunctionDeclaration(nodeBlock.getFirstChild()));
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateException() {
    Node node = new Node(Token.SCRIPT, new int[]{}, new Node[]{});
    Preconditions.checkState(node.getParent() == null);
  }
}