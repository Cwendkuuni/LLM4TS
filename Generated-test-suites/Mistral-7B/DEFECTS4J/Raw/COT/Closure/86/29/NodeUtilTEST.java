import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

/**
 * Test cases for NodeUtil class.
 */
public class NodeUtilTest {

  /**
   * Test isForIn method.
   */
  @Test
  public void testIsForIn() {
    Node node = new Node("for (var i in array) {}");
    assertTrue(isForIn(node));

    node = new Node("for (var i = 0; i < 10; i++) {}");
    assertFalse(isForIn(node));

    node = new Node("function foo() { for (var i in array) {} }");
    assertFalse(isForIn(node));
  }

  /**
   * Test isStatement method.
   */
  @Test
  public void testIsStatement() {
    Node node = new Node("1 + 1");
    assertFalse(isStatement(node));

    node = new Node("if (1) {}");
    assertTrue(isStatement(node));

    node = new Node("function foo() {}");
    assertTrue(isStatement(node));
  }

  /**
   * Test isFunctionDeclaration method.
   */
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node("function foo() {}");
    assertTrue(isFunctionDeclaration(node));

    node = new Node("function bar(x) {}");
    assertTrue(isFunctionDeclaration(node));

    node = new Node("1 + function foo() {}");
    assertFalse(isFunctionDeclaration(node));

    node = new Node("if (true) function foo() {}");
    assertFalse(isFunctionDeclaration(node));
  }

  /**
   * Test isHoistedFunctionDeclaration method.
   */
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node("function foo() {}");
    assertFalse(isHoistedFunctionDeclaration(node));

    node = new Node("(function foo() {})()");
    assertFalse(isHoistedFunctionDeclaration(node));

    node = new Node("(function foo() {})");
    assertTrue(isHoistedFunctionDeclaration(node));

    node = new Node("function foo() {} function bar() {}");
    assertTrue(isHoistedFunctionDeclaration(node.getChild(0)));
    assertFalse(isHoistedFunctionDeclaration(node.getChild(1)));

    node = new Node("function foo() {} script (function bar() {})");
    assertFalse(isHoistedFunctionDeclaration(node.getChild(0)));
    assertTrue(isHoistedFunctionDeclaration(node.getChild(1)));
  }

  /**
   * Test exception handling when parent is null in isStatement method.
   */
  @Test(expected = IllegalStateException.class)
  public void testIsStatementParentNull() {
    Node node = new Node("1 + 1");
    Preconditions.checkState(node.getParent() != null);
    isStatement(node);
  }
}