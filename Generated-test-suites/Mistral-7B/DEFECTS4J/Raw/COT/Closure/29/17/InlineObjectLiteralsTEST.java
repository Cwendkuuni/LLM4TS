import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerTestCase;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @org.junit.Test
  public void testInlineObjectLiterals_BasicFunctionality() {
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(
        new StubCompiler(), new StubNameIdSupplier());
    Node root = NodeUtil.parse("var obj = {a: 1, b: 2};");
    inlineObjectLiterals.process(null, root);

    // Check that the object literal has been split into individual variables
    Node objectNode = root.getFirstChild();
    Node expectedFirstChild = NodeUtil.newVarNode("JSCompiler_object_inline_a_", null);
    Node expectedSecondChild = NodeUtil.newVarNode("JSCompiler_object_inline_b_", null);
    Node actualFirstChild = objectNode.getFirstChild();
    Node actualSecondChild = objectNode.getFirstChild().getNext();

    assertEquals(expectedFirstChild, actualFirstChild);
    assertEquals(expectedSecondChild, actualSecondChild);
  }

  @org.junit.Test
  public void testInlineObjectLiterals_EmptyObjectLiteral() {
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(
        new StubCompiler(), new StubNameIdSupplier());
    Node root = NodeUtil.parse("var obj = {};");
    inlineObjectLiterals.process(null, root);

    // Check that the object literal has not been split into individual variables
    // since it is empty
    Node objectNode = root.getFirstChild();
    assertTrue(objectNode.isObjectLit());
  }

  @org.junit.Test
  public void testInlineObjectLiterals_NestedObjectLiterals() {
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(
        new StubCompiler(), new StubNameIdSupplier());
    Node root = NodeUtil.parse("var obj1 = {a: {b: 1, c: 2}};");
    inlineObjectLiterals.process(null, root);

    // Check that the nested object literal has been split into individual variables
    Node objectNode = root.getFirstChild();
    Node expectedFirstChild = NodeUtil.newVarNode("JSCompiler_object_inline_a_", null);
    Node expectedSecondChild = NodeUtil.newVarNode("JSCompiler_object_inline_b_", null);
    Node expectedThirdChild = NodeUtil.newVarNode("JSCompiler_object_inline_c_", null);
    Node actualFirstChild = objectNode.getFirstChild();
    Node actualSecondChild = objectNode.getFirstChild().getNext();
    Node actualThirdChild = actualSecondChild.getNext();

    assertEquals(expectedFirstChild, actualFirstChild);
    assertEquals(expectedSecondChild, actualSecondChild);
    assertEquals(expectedThirdChild, actualThirdChild);
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testInlineObjectLiterals_SelfReferentialObject() {
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(
        new StubCompiler(), new StubNameIdSupplier());
    Node root = NodeUtil.parse("var obj = {a: obj};");
    inlineObjectLiterals.process(null, root);

    fail("Expected AssertionError but no exception was thrown.");
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testInlineObjectLiterals_ObjectWithGetter() {
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(
        new StubCompiler(), new StubNameIdSupplier());
    Node root = NodeUtil.parse("var obj = {" +
        "  get getA() { return 1; }, " +
        "  a: 2" +
        "};");
    inlineObjectLiterals.process(null, root);

    fail("Expected AssertionError but no exception was thrown.");
  }

  @org.junit.Test
  public void testInlineObjectLiterals_GlobalObject() {
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(
        new StubCompiler(), new StubNameIdSupplier());
    Node root = NodeUtil.parse("window.obj = {a: 1, b: 2};");
    inlineObjectLiterals.process(null, root);

    // Check that the object literal has not been split into individual variables
    // since it is a global object
    Node objectNode = root.getFirstChild();
    assertTrue(objectNode.isObjectLit());
  }

  @org.junit.Test
  public void testInlineObjectLiterals_ObjectWithFunction() {
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(
        new StubCompiler(), new StubNameIdSupplier());
    Node root = NodeUtil.parse("var obj = {fn: function() {}};");
    inlineObjectLiterals.process(null, root);

    // Check that the object literal has not been split into individual variables
    // since it contains a function
    Node objectNode = root.getFirstChild();
    assertTrue(objectNode.isObjectLit());
  }
}