import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeWithForIn = new Node(FOR, 0, new Node[3]);
    assertTrue(NodeUtil.isForIn(nodeWithForIn));

    Node nodeWithoutForIn = new Node(SCRIPT, 0, new Node[0]);
    assertFalse(NodeUtil.isForIn(nodeWithoutForIn));

    Node nodeWithThreeChildren = new Node(SCRIPT, 0, new Node[3]);
    assertFalse(NodeUtil.isForIn(nodeWithThreeChildren));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(SCRIPT, 0, new Node[0]);
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = new Node(IDENT, 0, new String[] {"x"});
    assertFalse(NodeUtil.isStatement(expressionNode));

    Node functionNode = new Node(FUNCTION, 0, new Node[] {});
    assertTrue(NodeUtil.isStatement(functionNode));
  }

  @Test(expected = AssertionError.class)
  public void testIsStatementWithNullParent() {
    Node node = new Node(SCRIPT, 0, new Node[0]);
    Preconditions.checkState(node.getParent() != null);
    NodeUtil.isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(FUNCTION, 0, new Node[] {
        new Node(IDENT, 0, new String[] {"foo"}),
        new Node(BLOCK, 0, new Node[0])
    });
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));

    Node functionExpression = new Node(FUNCTION, 0, new Node[] {
        new Node(IDENT, 0, new String[] {"foo"}),
        new Node(IDENT, 0, new String[] {"bar"})
    });
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test(expected = AssertionError.class)
  public void testIsFunctionDeclarationWithNullParent() {
    Node functionDeclaration = new Node(FUNCTION, 0, new Node[] {
        new Node(IDENT, 0, new String[] {"foo"}),
        new Node(BLOCK, 0, new Node[0])
    });
    Preconditions.checkState(functionDeclaration.getParent() != null);
    NodeUtil.isFunctionDeclaration(functionDeclaration);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = new Node(FUNCTION, 0, new Node[] {
        new Node(IDENT, 0, new String[] {"foo"}),
        new Node(BLOCK, 0, new Node[0])
    });
    Node scriptNode = new Node(SCRIPT, 0, new Node[] { hoistedFunctionDeclaration });
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));

    Node functionNodeInsideFunction = new Node(FUNCTION, 0, new Node[] {
        new Node(IDENT, 0, new String[] {"foo"}),
        new Node(BLOCK, 0, new Node[0])
    });
    Node functionNodeInsideScript = new Node(SCRIPT, 0, new Node[] { functionNodeInsideFunction });
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInsideFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithFunctionGrandparent() {
    Node hoistedFunctionDeclaration = new Node(FUNCTION, 0, new Node[] {
        new Node(IDENT, 0, new String[] {"foo"}),
        new Node(BLOCK, 0, new Node[0])
    });
    Node functionNode = new Node(FUNCTION, 0, new Node[] {
        new Node(IDENT, 0, new String[] {"foo"}),
        new Node(BLOCK, 0, new Node[] { hoistedFunctionDeclaration })
    });
    Node scriptNode = new Node(SCRIPT, 0, new Node[] { functionNode });
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
  }
}