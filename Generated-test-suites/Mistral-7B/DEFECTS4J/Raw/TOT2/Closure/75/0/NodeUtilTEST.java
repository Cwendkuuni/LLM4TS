import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createNode;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = createNode("for (x in y) { }");
    assertTrue(NodeUtil.isForIn(node));

    Node node2 = createNode("if (true) { for (x in y) { } }");
    assertFalse(NodeUtil.isForIn(node2));

    Node node3 = createNode("for (x in []);"); // edge case: empty array
    assertFalse(NodeUtil.isForIn(node3));

    Node node4 = createNode("for (x in null) { }"); // edge case: null
    assertFalse(NodeUtil.isForIn(node4));
  }

  @Test
  public void testIsStatement() {
    Node node = createNode("1 + 1;");
    assertTrue(NodeUtil.isStatement(node));

    Node node2 = createNode("1 + 1"); ;
    assertFalse(NodeUtil.isStatement(node2));

    Node node3 = createNode("if (true) {}");
    assertTrue(NodeUtil.isStatement(node3));

    Node node4 = createNode("1 + 1 * 2"); // complex expression
    assertTrue(NodeUtil.isStatement(node4));

    Node node5 = createNode("1 + 1");
    assertFalse(NodeUtil.isStatement(node5));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = createNode("function foo() {}");
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    Node node2 = createNode("(function() {})()");
    assertFalse(NodeUtil.isFunctionDeclaration(node2));

    Node node3 = createNode("function foo() {} function bar() {}"); // complex function declaration
    assertFalse(NodeUtil.isFunctionDeclaration(node3));

    Node node4 = createNode("1 + 1; function foo() {}"); // function declaration not at the beginning
    assertFalse(NodeUtil.isFunctionDeclaration(node4));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = createNode("function foo() {}");
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

    Node node2 = createNode("function foo() {} function bar() {}");
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));

    Node node3 = createNode("function foo() {} function bar() {} function baz() {}"); // multiple function declarations
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));

    Node node4 = createNode("function foo() {} function foo() {}"); // duplicate function name
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node4));
  }
}