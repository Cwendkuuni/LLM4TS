import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import org.junit.Test;
import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.jscomp.jsonml.Token;

/**
 * tests for NodeUtil class
 */
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{-1, -1, -1});
    Node nodeNotForIn = new Node(Token.IF, new int[]{-1, -1, -1});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{-1, -1, -1});
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{-1, -1, -1});
    Node nodeIf = new Node(Token.IF, new int[]{-1, -1, -1});

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertTrue(NodeUtil.isStatement(nodeIf));

    Node nodeExpression = new Node(Token.IDENT, new int[]{-1, -1, -1});

    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{-1, 0, -1});
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{-1, 1, -1});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeRootFunction = new Node(
        Token.FUNCTION,
        new int[]{-1, 0, -1},
        new Node[]{
          new Node(Token.BLOCK, new int[]{-1, -1, -1}),
          new Node(Token.VAR, new int[]{-1, 1, -1}, new Node[]{
            new Node(Token.IDENT, new int[]{-1, -1, -1}),
            new Node(Token.FUNCTION, new int[]{-1, 1, -1}, new Node[]{
              new Node(Token.BLOCK, new int[]{-1, -1, -1}),
              new Node(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration).getNodeType(),
                      new int[]{-1, 0, -1}, null)
            })
          })
        });

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeRootFunction));

    Node nodeNestedFunction = new Node(
        Token.FUNCTION,
        new int[]{-1, 0, -1},
        new Node[]{
          new Node(Token.BLOCK, new int[]{-1, -1, -1}),
          new Node(Token.VAR, new int[]{-1, 1, -1}, new Node[]{
            new Node(Token.IDENT, new int[]{-1, -1, -1}),
            new Node(Token.FUNCTION, new int[]{-1, 1, -1}, new Node[]{
              new Node(Token.BLOCK, new int[]{-1, -1, -1}),
              new Node(Token.IF, new int[]{-1, 0, -1}, new Node[]{
                new Node(Token.IDENT, new int[]{-1, -1, -1})
              })
            })
          })
        });

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNestedFunction));
  }
}