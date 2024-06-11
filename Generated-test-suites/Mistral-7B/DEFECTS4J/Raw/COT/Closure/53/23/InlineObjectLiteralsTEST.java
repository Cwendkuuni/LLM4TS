import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private CompilerPass inlineObjectLiterals;
  private CompilerOptions options;

  @Before
  public void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions();
    inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "testVarId_");
  }

  @Test
  public void testInlineObjectLiteral() {
    Node root = Node.objectLiteral(
        "obj",
        Node.property("a", Node.numberLiteral(1)),
        Node.property("b", Node.numberLiteral(2))
    );

    Node externs = Node.objectLiteral("externs", Node.nullNode());

    inlineObjectLiterals.process(externs, root);

    Node result = compiler.compile(root, options);
    Node expected = Node.objectLiteral(
        "obj",
        Node.property("a", Node.numberLiteral(1)),
        Node.property("b", Node.numberLiteral(2))
    );

    assertEquals(expected, result);
  }

  @Test
  public void testInlineObjectLiteralWithInitialValues() {
    Node root = Node.objectLiteral(
        "obj",
        Node.property("a", Node.numberLiteral(1)),
        Node.property("b", Node.numberLiteral(2)),
        Node.property("c", Node.numberLiteral(3))
    );

    Node externs = Node.objectLiteral("externs", Node.nullNode());

    inlineObjectLiterals.process(externs, root);

    Node result = compiler.compile(root, options);
    Node expected = Node.objectLiteral(
        "obj",
        Node.property("a", Node.numberLiteral(1)),
        Node.property("b", Node.numberLiteral(2)),
        Node.property("c", Node.numberLiteral(3))
    );

    assertEquals(expected, result);
  }

  @Test
  public void testInlineNestedObjectLiteral() {
    Node root = Node.objectLiteral(
        "obj",
        Node.property("a", Node.objectLiteral(
            "subObj",
            Node.property("x", Node.numberLiteral(1)),
            Node.property("y", Node.numberLiteral(2))
        )),
        Node.property("b", Node.numberLiteral(2))
    );

    Node externs = Node.objectLiteral("externs", Node.nullNode());

    inlineObjectLiterals.process(externs, root);

    Node result = compiler.compile(root, options);

    Node expected = Node.objectLiteral(
        "obj",
        Node.property("a", Node.objectLiteral(
            "subObj",
            Node.property("x", Node.newVar("JSCompiler_object_inline_x")),
            Node.property("y", Node.newVar("JSCompiler_object_inline_y"))
        )),
        Node.property("b", Node.numberLiteral(2))
    );

    assertEquals(expected, result);
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiteralWithCycle() {
    Node root = Node.objectLiteral(
        "obj1",
        Node.property("a", Node.objectLiteral(
            "obj2",
            Node.property("a", Node.objectReference("obj1"))
        )),
        Node.property("b", Node.numberLiteral(2))
    );

    Node externs = Node.objectLiteral("externs", Node.nullNode());

    inlineObjectLiterals.process(externs, root);

    compiler.compile(root, options);

    fail("Expected an assertion error due to the object literal cycle.");
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiteralWithSelfReference() {
    Node root = Node.objectLiteral(
        "obj",
        Node.property("a", Node.objectLiteral(
            "a", Node.numberLiteral(1)
        ))
    );

    Node externs = Node.objectLiteral("externs", Node.nullNode());

    inlineObjectLiterals.process(externs, root);

    compiler.compile(root, options);

    fail("Expected an assertion error due to the self-referential object literal.");
  }

  @Test
  public void testInlineObjectLiteralWithExportedVariable() {
    Node root = Node.script(
        "exports.obj = {" +
            "  a: 1," +
            "  b: 2" +
            "};"
    );

    Node externs = Node.objectLiteral("externs", Node.nullNode());

    inlineObjectLiterals.process(externs, root);

    Node result = compiler.compile(root, options);

    Node expected = Node.objectLiteral(
        "obj",
        Node.property("a", Node.newVar("JSCompiler_object_inline_a")),
        Node.property("b", Node.newVar("JSCompiler_object_inline_b"))
    );

    assertEquals(expected, result);
  }

  @Test
  public void testInlineObjectLiteralWithExportedVariableAsKey() {
    Node root = Node.script(
        "exports.obj = {" +
            "  key: {" +
            "    a: 1" +
            "  }," +
            "  b: 2" +
            "};"
    );

    Node externs = Node.objectLiteral("externs", Node.nullNode());

    inlineObjectLiterals.process(externs, root);

    Node result = compiler.compile(root, options);

    Node expected = Node.objectLiteral(
        "obj",
        Node.property("key", Node.objectLiteral(
            "a", Node.newVar("JSCompiler_object_inline_key_a")
        )),
        Node.property("b", Node.newVar("JSCompiler_object_inline_b"))
    );

    assertEquals(expected, result);
  }

  @Test
  public void testInlineObjectLiteralWithSelfReferentialKey() {
    Node root = Node.script(
        "obj = {" +
            "  key: obj," +
            "  a: 1" +
            "};"
    );

    Node externs = Node.objectLiteral("externs", Node.nullNode());

    inlineObjectLiterals.process(externs, root);

    Node result = compiler.compile(root, options);

    Node expected = Node.objectLiteral(
        "obj",
        Node.property("key", Node.newVar("JSCompiler_object_inline_key")),
        Node.property("a", Node.newVar("JSCompiler_object_inline_a"))
    );

    assertEquals(expected, result);
  }
}