import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node("for (var i = 0; i < 10; i++) {}");
    assertTrue(isForIn(node));

    node = new Node("for (var i in arr) {}");
    assertTrue(isForIn(node));

    node = new Node("if (true) for (var i = 0; i < 10; i++) {}");
    assertFalse(isForIn(node));

    node = new Node("function foo() { for (var i = 0; i < 10; i++) {} }");
    assertFalse(isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node("return 1;");
    assertTrue(isStatement(node));

    node = new Node("if (true) {}", "if (true)");
    assertTrue(isStatement(node));

    node = new Node("for (var i = 0; i < 10; i++) {}", "for");
    assertFalse(isStatement(node));

    node = new Node("1 + 1");
    assertFalse(isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node("function foo() {}");
    assertTrue(isFunctionDeclaration(node));

    node = new Node("function bar() {} function foo() {}");
    assertFalse(isFunctionDeclaration(node));

    node = new Node("function foo() {} return;");
    assertFalse(isFunctionDeclaration(node));

    node = new Node("function foo(x) { return x * 2; }");
    assertTrue(isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node("function foo() {}");
    assertFalse(isHoistedFunctionDeclaration(node));

    Node scriptNode = new Node("function foo() {}");
    Node functionNode = new Node("function bar() {}", scriptNode);
    assertTrue(isHoistedFunctionDeclaration(scriptNode));
    assertFalse(isHoistedFunctionDeclaration(functionNode));

    Node functionInFunction = new Node("function bar() { function foo() {} }");
    assertTrue(isHoistedFunctionDeclaration(functionInFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsForInNullNode() {
    NodeUtil.isForIn(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullNode() {
    NodeUtil.isStatement(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationNullNode() {
    NodeUtil.isFunctionDeclaration(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }
}