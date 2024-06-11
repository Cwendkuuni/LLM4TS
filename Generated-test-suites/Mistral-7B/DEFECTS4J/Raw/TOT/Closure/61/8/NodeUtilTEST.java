import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.jscomp.testing.TestCode;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = TestCode.getNode(
        "for (var i in [1, 2, 3]) { print(i); }");
    assertTrue(isForIn(n));

    n = TestCode.getNode("for (var i = 0; i < 10; i++) { print(i); }");
    assertFalse(isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = TestCode.getNode("print(1);");
    assertTrue(isStatement(n));

    n = TestCode.getNode("1 + 1;");
    assertFalse(isStatement(n)); // this is an expression
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = TestCode.getNode("function foo() { print(1); }");
    assertTrue(isFunctionDeclaration(n));

    n = TestCode.getNode("return function() { print(1); }");
    assertFalse(isFunctionDeclaration(n)); // this is a function expression
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = TestCode.getNode("function foo() { print(1); }");
    assertFalse(isHoistedFunctionDeclaration(n));

    n = TestCode.getNode("function foo() { print(1); }\nfunction bar() { print(2); }");
    assertFalse(isHoistedFunctionDeclaration(n));

    n = TestCode.getNode("function foo() { print(1); }function bar() { }");
    assertTrue(isHoistedFunctionDeclaration(n));

    n = TestCode.getNode("function foo() { print(1); }function bar() { }\nfunction baz() { }");
    assertFalse(isHoistedFunctionDeclaration(n)); // hoisted functions are not hoisted across function boundaries
  }

  @Test
  public void testIsForInWithEmptyArray() {
    Node n = TestCode.getNode("for (var i in []) { print(i); }");
    assertFalse(isForIn(n));
  }

  @Test
  public void testIsForInWithNull() {
    Node n = TestCode.getNode("for (var i in null) { print(i); }");
    assertFalse(isForIn(n));
  }

  @Test
  public void testIsStatementWithEmptyStatement() {
    Node n = TestCode.getNode(";");
    assertTrue(isStatement(n));
  }
}