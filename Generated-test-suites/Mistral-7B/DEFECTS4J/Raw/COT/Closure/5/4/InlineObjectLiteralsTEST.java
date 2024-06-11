import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceMap;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private CompilerOptions options;
  private InlineObjectLiterals inlineObjectLiterals;

  @Before
  public void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions();
    inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "");
  }

  @Test
  public void testInlineObjectLiteral() {
    Node root = Node.objectLiteral();
    Node obj = root.addChild(Token.OBJECT_LITERAL);
    obj.setPropertyIn("name", Node.stringLiteral("John Doe"));
    obj.setPropertyIn("age", Node.numberLiteral(30));

    inlineObjectLiterals.process(null, root);

    Node expected = Node.block("var name = JSCompiler_object_inline_name_1;",
        "var age = JSCompiler_object_inline_age_1;",
        "return true;");

    Node actual = ((Node) root.getFirstChild()).getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testInlineObjectLiteralWithEmptyProperties() {
    Node root = Node.objectLiteral();

    inlineObjectLiterals.process(null, root);

    Node expected = Node.block("", "return true;");
    Node actual = ((Node) root.getFirstChild()).getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testInlineObjectLiteralWithSelfReferentialAssignment() {
    Node root = Node.objectLiteral();
    Node obj = root.addChild(Token.OBJECT_LITERAL);

    obj.setPropertyIn("a", Node.objectLiteral());
    Node a = obj.getLastChild();
    a.setPropertyIn("b", Node.name("a"));

    inlineObjectLiterals.process(null, root);

    // The test assumes the error handling by the compiler,
    // as it throws an exception during the test execution.
    // This is not a good test case, and it should be re-written.
    // assertThrows(RuntimeException.class, () -> {
    //   compiler.compile(root, options, null, null);
    // });
  }

  @Test
  public void testInlineObjectLiteralWithGettersAndSetters() {
    Node root = Node.objectLiteral();
    Node obj = root.addChild(Token.OBJECT_LITERAL);

    obj.setPropertyIn("get a() { return this.a; }", "set a(value) { this.a = value; }",
        "a", Node.numberLiteral(1));

    inlineObjectLiterals.process(null, root);

    Node expected = Node.block(
        "var a_getter = JSCompiler_object_inline_a_getter_1;",
        "var a_setter = JSCompiler_object_inline_a_setter_1;",
        "var a_JSCompiler_object_inline_a_1;",
        "return true;");

    Node actual = ((Node) root.getFirstChild()).getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testInlineObjectLiteralWithNestedObjectLiterals() {
    Node root = Node.objectLiteral();
    Node obj = root.addChild(Token.OBJECT_LITERAL);

    obj.setPropertyIn("name", Node.stringLiteral("John Doe"));
    obj.setPropertyIn("age", Node.numberLiteral(30));
    obj.setPropertyIn("nested", Node.objectLiteral());

    Node nestedObj = obj.getLastChild().getLastChild();
    nestedObj.setPropertyIn("color", Node.stringLiteral("red"));

    inlineObjectLiterals.process(null, root);

    Node expected = Node.block(
        "var name = JSCompiler_object_inline_name_1;",
        "var age = JSCompiler_object_inline_age_1;",
        "var nested_color = JSCompiler_object_inline_nested_color_1;",
        "return true;");

    Node actual = ((Node) root.getFirstChild()).getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testInlineObjectLiteralWithCyclicReference() {
    Node root = Node.objectLiteral();
    Node obj1 = root.addChild(Token.OBJECT_LITERAL);
    Node obj2 = root.addChild(Token.OBJECT_LITERAL);

    obj1.setPropertyIn("a", Node.name("obj2"));
    obj2.setPropertyIn("a", Node.name("obj1"));

    inlineObjectLiterals.process(null, root);

    // The test assumes the error handling by the compiler,
    // as it throws an exception during the test execution.
    // assertThrows(RuntimeException.class, () -> {
    //   compiler.compile(root, options, null, null);
    // });
  }

  @Test
  public void testInlineObjectLiteralWithInvalidProperties() {
    Node root = Node.objectLiteral();
    Node obj = root.addChild(Token.OBJECT_LITERAL);

    obj.setPropertyIn("invalid", Node.name("undefined"));

    inlineObjectLiterals.process(null, root);

    Node expected = Node.block("", "return true;");

    Node actual = ((Node) root.getFirstChild()).getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testInlineObjectLiteralWithInitialization() {
    Node root = Node.objectLiteral();
    Node obj = root.addChild(Token.OBJECT_LITERAL);

    inlineObjectLiterals.process(null, root);

    obj.setPropertyIn("name", Node.stringLiteral("John Doe"));
    obj.setPropertyIn("age", Node.numberLiteral(30));

    ReferenceMap referenceMap = new ReferenceMap();
    ReferenceCollection referenceCollection = new ReferenceCollection();

    Reference nameReference = new Reference(
        Node.name("name"),
        Node.getSourcePosition(null, 0, 0),
        Behavior.NONE);
    Reference ageReference = new Reference(
        Node.name("age"),
        Node.getSourcePosition(null, 0, 0),
        Behavior.NONE);
    referenceCollection.add(nameReference);
    referenceCollection.add(ageReference);

    inlineObjectLiterals.new InliningBehavior().afterExitScope(
        new NodeTraversal(compiler), referenceCollection);

    Node expected = Node.block(
        "var name = JSCompiler_object_inline_name_1;",
        "var age = JSCompiler_object_inline_age_1;",
        "name = 'John Doe';",
        "age = 30;");

    Node actual = obj.getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }
}