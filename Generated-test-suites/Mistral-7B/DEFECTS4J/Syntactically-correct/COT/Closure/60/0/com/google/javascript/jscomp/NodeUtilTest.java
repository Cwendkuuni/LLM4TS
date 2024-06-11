import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node("for (var i in array) { }");
    assertTrue(isForIn(node));

    node = new Node("for (var i = 0; i < 10; i++) { }");
    assertFalse(isForIn(node));

    node = new Node("for (var i = 0; i < 10; ) { }"); // missing condition
    assertFalse(isForIn(node));

    node = new Node("for (var i = 0; ; i++) { }"); // infinite loop
    assertFalse(isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node("return 1;");
    assertTrue(isStatement(node));

    node = new Node("1 + 1;");
    assertFalse(isStatement(node));

    node = new Node("if (true) { return 1; }");
    assertTrue(isStatement(node));

    node = new Node("{ return 1; }"); // empty statement
    assertTrue(isStatement(node));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullParent() {
    Node node = new Node("return 1;");
    Preconditions.checkState(node.getParent() != null);
    node.setParent(null);
    isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node("function foo() { }");
    assertTrue(isFunctionDeclaration(node));

    node = new Node("return function foo() { }");
    assertFalse(isFunctionDeclaration(node));

    node = new Node("{ function foo() { } }"); // anonymous function in block
    assertFalse(isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node("function foo() { }");
    assertFalse(isHoistedFunctionDeclaration(node));

    Node scriptNode = new Node("function foo() { }");
    assertTrue(isHoistedFunctionDeclaration(scriptNode));

    Node functionNode = new Node("function bar() { function foo() { } }");
    assertTrue(isHoistedFunctionDeclaration(functionNode.getChild(0)));
  }
}