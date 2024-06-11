import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node("for (var i in array) { }");
    assertTrue(isForIn(node));

    node = new Node("for (var i = 0; i < 10; i++) { }");
    assertFalse(isForIn(node));

    node = new Node("for (var i = 0; ; i++) { }");
    assertFalse(isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node("{}");
    assertTrue(isStatement(node));

    node = new Node("function foo() {}");
    assertFalse(isStatement(node));

    node = new Node("return 1;");
    assertTrue(isStatement(node));

    Node parent = new Node("function foo() { return 1; }");
    Node child = new Node("{}");
    child.setParent(parent);
    assertFalse(isStatement(child));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node("function foo() {}");
    assertTrue(isFunctionDeclaration(node));

    node = new Node("function foo() { return 1; }");
    assertTrue(isFunctionDeclaration(node));

    node = new Node("var foo = function() {};");
    assertFalse(isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node("function foo() {}");
    assertFalse(isHoistedFunctionDeclaration(node));

    Node parentScript = new Node("(function () {\n" + "  function foo() {};\n" + "})();");
    node = new Node("function foo() {}");
    node.setParent(parentScript);
    assertTrue(isHoistedFunctionDeclaration(node));

    Node parentFunction = new Node("function bar() {\n" + "  function foo() {};\n" + "}");
    node = new Node("function foo() {}");
    node.setParent(parentFunction);
    assertTrue(isHoistedFunctionDeclaration(node));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullParent() {
    Node node = new Node("{}");
    Preconditions.checkState(node.getParent() != null);
    node.setParent(null);
    isStatement(node);
  }
}