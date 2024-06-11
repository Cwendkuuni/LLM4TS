import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    // Given: A simple object literal assignment
    String sourceCode = "var x = {a: 1, b: 2};";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "");

    // When: The InlineObjectLiterals pass is applied
    Node externs = Node.newTextNode(sourceCode);
    Node root = Node.newTextNode("");
    inlineObjectLiterals.process(externs, root);

    // Then: The object literal should be replaced with individual variable assignments
    Node x = NodeUtil.findSingleChildOfType(root, Token.VAR);
    List<Node> assignments = NodeUtil.findChildrenOfType(x, Token.ASSIGN);
    assertEquals(2, assignments.size());

    Node aAssignment = assignments.get(0);
    assertEquals("a", aAssignment.getFirstChild().getString());
    Node a = aAssignment.getLastChild();
    assertEquals(Token.NUMBER, a.getType());
    assertEquals(1.0, a.getNumberValue().doubleValueExact());

    Node bAssignment = assignments.get(1);
    assertEquals("b", bAssignment.getFirstChild().getString());
    Node b = bAssignment.getLastChild();
    assertEquals(Token.NUMBER, b.getType());
    assertEquals(2.0, b.getNumberValue().doubleValueExact());
  }

  @Test
  public void testInlineObjectLiteralsWithInitialValues() {
    // Given: An object literal assignment with initial values
    String sourceCode = "var x = {a: 1, b: 2};";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "");

    // When: The InlineObjectLiterals pass is applied
    Node externs = Node.newTextNode(sourceCode);
    Node root = Node.newTextNode("");
    inlineObjectLiterals.process(externs, root);

    // Then: The object literal should be replaced with individual variable assignments and initial values
    Node x = NodeUtil.findSingleChildOfType(root, Token.VAR);
    List<Node> assignments = NodeUtil.findChildrenOfType(x, Token.ASSIGN);
    assertEquals(2, assignments.size());

    Node aAssignment = assignments.get(0);
    assertEquals("a", aAssignment.getFirstChild().getString());
    Node a = aAssignment.getLastChild();
    assertEquals(Token.NUMBER, a.getType());
    assertEquals(1.0, a.getNumberValue().doubleValueExact());

    Node bAssignment = assignments.get(1);
    assertEquals("b", bAssignment.getFirstChild().getString());
    Node b = bAssignment.getLastChild();
    assertEquals(Token.NUMBER, b.getType());
    assertEquals(2.0, b.getNumberValue().doubleValueExact());
  }

  @Test
  public void testInlineObjectLiteralsWithNestedObjects() {
    // Given: An object literal assignment with nested objects
    String sourceCode = "var x = {a: {b: 1}, c: 2};";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "");

    // When: The InlineObjectLiterals pass is applied
    Node externs = Node.newTextNode(sourceCode);
    Node root = Node.newTextNode("");
    inlineObjectLiterals.process(externs, root);

    // Then: The object literal should be replaced with individual variable assignments
    Node x = NodeUtil.findSingleChildOfType(root, Token.VAR);
    List<Node> assignments = NodeUtil.findChildrenOfType(x, Token.ASSIGN);
    assertEquals(2, assignments.size());

    Node aAssignment = assignments.get(0);
    Node a = NodeUtil.findSingleChildOfType(aAssignment.getLastChild(), Token.OBJECTLIT);
    List<Node> aChildren = NodeUtil.findChildrenOfType(a, Token.ASSIGN);
    assertEquals(1, aChildren.size());

    Node bAssignment = aChildren.get(0);
    assertEquals("b", bAssignment.getFirstChild().getString());
    Node b = bAssignment.getLastChild();
    assertEquals(Token.NUMBER, b.getType());
    assertEquals(1.0, b.getNumberValue().doubleValueExact());

    Node cAssignment = assignments.get(1);
    assertEquals("c", cAssignment.getFirstChild().getString());
    Node c = cAssignment.getLastChild();
    assertEquals(Token.NUMBER, c.getType());
    assertEquals(2.0, c.getNumberValue().doubleValueExact());
  }

  @Test
  public void testInlineObjectLiteralsWithComplexObject() {
    // Given: An object literal assignment with complex values
    String sourceCode = "var x = {" +
        "  a: 1," +
        "  b: {c: {d: 'foo'}}, " +
        "  e: function() {return 42;}" +
        "};";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "");

    // When: The InlineObjectLiterals pass is applied
    Node externs = Node.newTextNode(sourceCode);
    Node root = Node.newTextNode("");
    inlineObjectLiterals.process(externs, root);

    // Then: The object literal should be replaced with individual variable assignments
    Node x = NodeUtil.findSingleChildOfType(root, Token.VAR);
    List<Node> assignments = NodeUtil.findChildrenOfType(x, Token.ASSIGN);
    assertEquals(3, assignments.size());

    Node aAssignment = assignments.get(0);
    assertEquals("a", aAssignment.getFirstChild().getString());
    Node a = aAssignment.getLastChild();
    assertEquals(Token.NUMBER, a.getType());
    assertEquals(1.0, a.getNumberValue().doubleValueExact());

    Node bAssignment = assignments.get(1);
    Node bObject = NodeUtil.findSingleChildOfType(bAssignment.getLastChild(), Token.OBJECTLIT);
    List<Node> bChildren = NodeUtil.findChildrenOfType(bObject, Token.ASSIGN);
    assertEquals(1, bChildren.size());

    Node cAssignment = bChildren.get(0);
    assertEquals("c", cAssignment.getFirstChild().getString());
    Node c = cAssignment.getLastChild();
    assertEquals(Token.OBJECTLIT, c.getType());
    Node dAssignment = NodeUtil.findSingleChildOfType(c, Token.ASSIGN);
    assertEquals("d", dAssignment.getFirstChild().getString());
    Node d = dAssignment.getLastChild();
    assertEquals(Token.STRING, d.getType());
    assertEquals("foo", d.getStringValue());

    Node eAssignment = assignments.get(2);
    assertEquals("e", eAssignment.getFirstChild().getString());
    Node e = eAssignment.getLastChild();
    assertEquals(Token.FUNCTION, e.getType());
    assertEquals(0, e.getChildCount());
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiteralsWithInvalidObject() {
    // Given: An object literal with an error in it
    String sourceCode = "var x = {a: 1, b = 2};";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "");

    // When: The InlineObjectLiterals pass is applied
    Node externs = Node.newTextNode(sourceCode);
    Node root = Node.newTextNode("");
    inlineObjectLiterals.process(externs, root);

    // Then: An AssertionError should be thrown because of the syntax error
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReference() {
    // Given: An object literal with a self-reference
    String sourceCode = "var x = {a: {b: x}};";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "");

    // When: The InlineObjectLiterals pass is applied
    Node externs = Node.newTextNode(sourceCode);
    Node root = Node.newTextNode("");
    inlineObjectLiterals.process(externs, root);

    // Then: The object literal should not be replaced with individual variable assignments due to the self-reference
    Node x = NodeUtil.findSingleChildOfType(root, Token.VAR);
    List<Node> assignments = NodeUtil.findChildrenOfType(x, Token.ASSIGN);
    assertEquals(1, assignments.size());

    Node assignment = assignments.get(0);
    assertEquals("x", assignment.getFirstChild().getString());
    Node object = assignment.getLastChild();
    assertEquals(Token.OBJECTLIT, object.getType());
    List<Node> properties = NodeUtil.findChildrenOfType(object, Token.ASSIGN);
    assertEquals(1, properties.size());

    Node property = properties.get(0);
    assertEquals("a", property.getFirstChild().getString());
    Node objectProperty = property.getLastChild();
    Node objectPropertyObject = NodeUtil.findSingleChildOfType(objectProperty, Token.OBJECTLIT);
    List<Node> selfReferences = NodeUtil.findChildrenOfType(objectPropertyObject, Token.GETPROP);
    assertEquals(1, selfReferences.size());

    Node selfReference = selfReferences.get(0);
    assertEquals("x", selfReference.getStringValue());
  }
}