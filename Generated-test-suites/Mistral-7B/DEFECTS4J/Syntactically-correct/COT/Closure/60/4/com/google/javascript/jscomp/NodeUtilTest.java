import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeUtil.isForIn;
import static com.google.javascript.jscomp.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.NodeUtil.isHoistedFunctionDeclaration;
import static com.google.javascript.jscomp.NodeUtil.isStatement;

import org.junit.Test;
import org.junit.contrib.java.lang.reflect.ReflectionTestUtils;
import org.mockito.Mockito;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

/**
 * Test class for NodeUtil class.
 */
public class NodeUtilTest {

  /**
   * Test for isForIn method.
   */
  @Test
  public void testIsForIn() {
    Node node = Mockito.mock(Node.class);
    ReflectionTestUtils.setFieldValue(node, "getType", Token.FOR);
    ReflectionTestUtils.setFieldValue(node, "getChildCount", 3);

    assertTrue(isForIn(node));
  }

  /**
   * Test for isForIn method with invalid node.
   */
  @Test
  public void testIsForInInvalidNode() {
    Node node = Mockito.mock(Node.class);
    ReflectionTestUtils.setFieldValue(node, "getType", Token.IF);

    assertFalse(isForIn(node));
  }

  /**
   * Test for isStatement method.
   */
  @Test
  public void testIsStatement() {
    Node node = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);
    ReflectionTestUtils.setFieldValue(parent, "getType", Token.SCRIPT);
    ReflectionTestUtils.setFieldValue(node, "getParent", parent);

    assertTrue(isStatement(node));
  }

  /**
   * Test for isStatement method with invalid node.
   */
  @Test
  public void testIsStatementInvalidNode() {
    Node node = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);
    ReflectionTestUtils.setFieldValue(parent, "getType", Token.LABEL);
    ReflectionTestUtils.setFieldValue(node, "getParent", parent);

    assertFalse(isStatement(node));
  }

  /**
   * Test for isFunctionDeclaration method.
   */
  @Test
  public void testIsFunctionDeclaration() {
    Node node = Mockito.mock(Node.class);
    ReflectionTestUtils.setFieldValue(node, "getType", Token.FUNCTION);
    Node statement = Mockito.mock(Node.class);
    ReflectionTestUtils.setFieldValue(statement, "getParent", null);
    ReflectionTestUtils.setFieldValue(node, "getParent", statement);

    assertTrue(isFunctionDeclaration(node));
  }

  /**
   * Test for isFunctionDeclaration method with invalid node.
   */
  @Test
  public void testIsFunctionDeclarationInvalidNode() {
    Node node = Mockito.mock(Node.class);
    ReflectionTestUtils.setFieldValue(node, "getType", Token.VAR);

    assertFalse(isFunctionDeclaration(node));
  }

  /**
   * Test for isHoistedFunctionDeclaration method.
   */
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = Mockito.mock(Node.class);
    Node functionNode = Mockito.mock(Node.class);
    Node script = Mockito.mock(Node.class);
    Node function = Mockito.mock(Node.class);
    ReflectionTestUtils.setFieldValue(functionNode, "getType", Token.FUNCTION);
    ReflectionTestUtils.setFieldValue(functionNode, "getParent", node);
    ReflectionTestUtils.setFieldValue(node, "getParent", script);
    ReflectionTestUtils.setFieldValue(script, "getType", Token.SCRIPT);
    ReflectionTestUtils.setFieldValue(function, "getType", Token.FUNCTION);
    ReflectionTestUtils.setFieldValue(function, "getParent", functionNode);

    assertTrue(isHoistedFunctionDeclaration(node));
  }

  /**
   * Test for isHoistedFunctionDeclaration method with invalid node.
   */
  @Test
  public void testIsHoistedFunctionDeclarationInvalidNode() {
    Node node = Mockito.mock(Node.class);
    Node functionNode = Mockito.mock(Node.class);
    Node function = Mockito.mock(Node.class);
    ReflectionTestUtils.setFieldValue(functionNode, "getType", Token.FUNCTION);
    ReflectionTestUtils.setFieldValue(functionNode, "getParent", node);
    ReflectionTestUtils.setFieldValue(node, "getParent", function);
    ReflectionTestUtils.setFieldValue(function, "getType", Token.VAR);

    assertFalse(isHoistedFunctionDeclaration(node));
  }
}