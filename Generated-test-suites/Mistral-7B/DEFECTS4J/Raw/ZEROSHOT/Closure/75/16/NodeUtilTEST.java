import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR, new int[]{}, new Object[3]);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn1 = new Node(SCRIPT, new int[]{}, new Object[0]);
    Node nodeNotForIn2 = new Node(IF, new int[]{}, new Object[3]);
    Node nodeNotForIn3 = new Node(FOR, new int[]{1, 2}, new Object[3]);
    Node nodeNotForIn4 = new Node(FOR, new int[]{TOKEN_INVALID}, new Object[3]);

    assertFalse(NodeUtil.isForIn(nodeNotForIn1));
    assertFalse(NodeUtil.isForIn(nodeNotForIn2));
    assertFalse(NodeUtil.isForIn(nodeNotForIn3));
    assertFalse(NodeUtil.isForIn(nodeNotForIn4));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(PRINT, new int[]{}, new Object[1]);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(ADD, new int[]{OP_LEFT_PAREN, NUM_INT, OP_RIGHT_PAREN}, new Object[3]);
    assertFalse(NodeUtil.isStatement(nodeExpression));

    Node nodeNull = new Node(NULL, new int[0], new Object[0]);
    assertFalse(NodeUtil.isStatement(nodeNull));

    Node nodeFunction = new Node(FUNCTION, new int[]{ID}, new Object[1]);
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeScript = new Node(SCRIPT, new int[0], new Object[0]);
    assertTrue(NodeUtil.isStatement(nodeScript));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration1 = new Node(FUNCTION, new int[]{ID, OP_LEFT_PAREN, OP_RIGHT_PAREN, BLOCK}, new Object[4]);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration1));

    Node nodeFunctionDeclaration2 = new Node(FUNCTION, new int[]{ID}, new Object[1]);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration2));

    Node nodeFunctionExpression = new Node(FUNCTION, new int[]{ID, OP_LEFT_PAREN, OP_RIGHT_PAREN}, new Object[3]);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    Node nodeScriptFunction = new Node(SCRIPT, new int[]{FUNCTION}, new Object[1]);
    Node functionNode = nodeScriptFunction.getFirstChild();
    assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration1 = new Node(
        SCRIPT,
        new int[]{FUNCTION},
        new Object[]{
            new Node(FUNCTION, new int[]{ID}, new Object[1])
        }
    );
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration1.getFirstChild()));

    Node nodeHoistedFunctionDeclaration2 = new Node(
        FUNCTION,
        new int[]{FUNCTION},
        new Object[]{
            new Node(FUNCTION, new int[]{ID}, new Object[1])
        }
    );
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration2.getFirstChild()));

    Node nodeNonHoistedFunctionDeclaration = new Node(
        SCRIPT,
        new int[]{BLOCK},
        new Object[]{
            new Node(BLOCK, new int[]{FUNCTION}, new Object[]{
                new Node(FUNCTION, new int[]{ID}, new Object[1])
            })
        }
    );
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNonHoistedFunctionDeclaration.getFirstChild().getFirstChild()));

    Node nodeNonHoistedFunctionDeclaration2 = new Node(
        FUNCTION,
        new int[]{FUNCTION},
        new Object[]{
            new Node(BLOCK, new int[]{FUNCTION}, new Object[]{
                new Node(FUNCTION, new int[]{ID}, new Object[1])
            })
        }
    );
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNonHoistedFunctionDeclaration2.getFirstChild()));
  }
}