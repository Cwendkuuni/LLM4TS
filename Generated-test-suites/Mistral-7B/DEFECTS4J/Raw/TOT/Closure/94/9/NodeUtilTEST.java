import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.jscomp.testing.TopLevelCodeTestingTreeBuilder;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = TopLevelCodeTestingTreeBuilder.getTree("for (var i in [1, 2, 3]) {}", "script_src").getRootNode();
    assertTrue(isForIn(node.getFirstChild()));

    node = TopLevelCodeTestingTreeBuilder.getTree("for (var i = 0; i < 5; i++) {}", "script_src").getRootNode();
    assertFalse(isForIn(node.getFirstChild()));

    node = TopLevelCodeTestingTreeBuilder.getTree("for (var i in obj) {}", "script_src").getRootNode();
    assertTrue(isForIn(node.getFirstChild()));

    node = TopLevelCodeTestingTreeBuilder.getTree("for (var i = 0; i < obj.length; i++) {}", "script_src").getRootNode();
    assertFalse(isForIn(node.getFirstChild()));
  }

  @Test
  public void testIsStatement() {
    Node node = TopLevelCodeTestingTreeBuilder.getTree("1 + 2", "expression_src").getRootNode();
    assertFalse(isStatement(node));

    node = TopLevelCodeTestingTreeBuilder.getTree("if (1 > 0) {}", "script_src").getRootNode();
    assertTrue(isStatement(node));

    node = TopLevelCodeTestingTreeBuilder.getTree("function foo() {}", "script_src").getRootNode();
    assertTrue(isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = TopLevelCodeTestingTreeBuilder.getTree("function foo() {}", "script_src").getRootNode();
    assertTrue(isFunctionDeclaration(node));

    node = TopLevelCodeTestingTreeBuilder.getTree("var foo = function() {}", "script_src").getRootNode();
    assertFalse(isFunctionDeclaration(node));

    node = TopLevelCodeTestingTreeBuilder.getTree("function* foo() {}", "script_src").getRootNode();
    assertFalse(isFunctionDeclaration(node));

    node = TopLevelCodeTestingTreeBuilder.getTree("class Foo {}", "script_src").getRootNode();
    assertFalse(isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = TopLevelCodeTestingTreeBuilder.getTree("function foo() {}", "script_src").getRootNode();
    assertFalse(isHoistedFunctionDeclaration(node));

    node = TopLevelCodeTestingTreeBuilder.getTree("(function foo() {})();", "script_src").getRootNode();
    assertTrue(isHoistedFunctionDeclaration(node));

    node = TopLevelCodeTestingTreeBuilder.getTree("function foo() {}", "function Bar() { foo(); }").getRootNode();
    assertTrue(isHoistedFunctionDeclaration(node));

    node = TopLevelCodeTestingTreeBuilder.getTree("function foo() {}", "function Bar() { var x = 1; function foo() {} }").getRootNode();
    assertFalse(isHoistedFunctionDeclaration(node));
  }
}