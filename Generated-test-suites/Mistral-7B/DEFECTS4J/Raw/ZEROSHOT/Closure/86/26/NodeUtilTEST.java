import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(TOKEN, FOR, new int[0]);
    Node nodeFor = new Node(TOKEN, FOR, new int[0]);
    Node nodeIf = new Node(TOKEN, IF, new int[0]);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(TOKEN, SCRIPT, new int[0]);
    Node nodeFunction = new Node(TOKEN, FUNCTION, new int[0]);
    Node nodeIf = new Node(TOKEN, IF, new int[0]);
    Node nodeExpression = new Node(TOKEN, IDENT, "test");

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeIf));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(TOKEN, FUNCTION, new int[] {
      IDENT, "testFunction", LPAREN, RPAREN, LBRACE, RBRACE
    });
    Node nodeFunctionExpression = new Node(TOKEN, FUNCTION, new int[] {
      LPAREN, RPAREN, LBRACE, RBRACE
    });
    Node nodeVariableDeclaration = new Node(TOKEN, VAR, new int[] { IDENT, "testVariable" });

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeVariableDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = new Node(TOKEN, FUNCTION, new int[] {
      IDENT, "testFunction", LPAREN, RPAREN, LBRACE, RBRACE
    });
    Node nodeFunctionDeclarationInFunction = new Node(TOKEN, FUNCTION, new int[] {
      FUNCTION, IDENT, "testFunctionParent", LPAREN, RPAREN, LBRACE, RBRACE, LBRACE,
      nodeFunctionDeclarationInScript, RBRACE
    });

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateParentIsNull() {
    Node node = new Node(TOKEN, IDENT, "test");
    NodeUtil.isStatement(node);
  }

  @Test
  public void testGetParentNull() {
    Node node = new Node(TOKEN, IDENT, "test");
    assertNull(node.getParent());
  }
}