import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.testing.TestSourceFile;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new TestSourceFile("test.js", "for (var i in [1, 2, 3]) {}")
        .getAst();
    assertTrue(isForIn(node.getFirstChild()));
  }

  @Test
  public void testIsForIn_false() {
    Node node = new TestSourceFile("test.js", "for (var i = 0; i < 5; i++) {}")
        .getAst();
    assertFalse(isForIn(node.getFirstChild()));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new TestSourceFile("test.js", "var x = 5;")
        .getAst().getLastChild();
    assertTrue(isStatement(statementNode));
  }

  @Test
  public void testIsStatement_false() {
    Node expressionNode = new TestSourceFile("test.js", "5 + 3")
        .getAst().getLastChild();
    assertFalse(isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new TestSourceFile("test.js", "function foo() {}")
        .getAst().getLastChild();
    assertTrue(isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsFunctionDeclaration_false() {
    Node expressionNode = new TestSourceFile("test.js", "function foo() { return 5; }")
        .getAst().getLastChild();
    assertFalse(isFunctionDeclaration(expressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new TestSourceFile("test.js", "function foo() {}")
        .getAst();
    assertTrue(isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_false() {
    Node functionNode = new TestSourceFile("test.js", "function foo() {}")
        .getAst().getLastChild();
    assertFalse(isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsFunctionDeclaration_inScript() {
    Node functionNode = new TestSourceFile("test.js", "function foo() {}")
        .getAst();
    assertTrue(isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsFunctionDeclaration_inFunction() {
    Node functionNode = new TestSourceFile("test.js", "function bar() {\n function foo() {} \n}")
        .getAst();
    assertFalse(isFunctionDeclaration(functionNode));
  }
}