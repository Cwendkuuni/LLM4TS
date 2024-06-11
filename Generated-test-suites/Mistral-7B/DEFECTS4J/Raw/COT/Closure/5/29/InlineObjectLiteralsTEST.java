import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiteralWithSingleProperty() {
    String jsSource = "var obj = {'key': 'value'};";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_name");
    pass.process(null, Node.parse(jsSource));

    String result = compiler.toSource(Node.SCOPE_GLOBAL, null, null);
    Node expectedNode = Node.parse("var key = 'value';");

    assertEquals(expectedNode.toPrintString(), result);
  }

  @Test
  public void testInlineObjectLiteralWithMultipleProperties() {
    String jsSource = "var obj = {'key1': 'value1', 'key2': 'value2'};";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_name");
    pass.process(null, Node.parse(jsSource));

    String result = compiler.toSource(Node.SCOPE_GLOBAL, null, null);
    Node expectedNode = Node.parse("var key1 = 'value1';\nvar key2 = 'value2';");

    assertEquals(expectedNode.toPrintString(), result);
  }

  @Test
  public void testInlineObjectLiteralWithSelfReference() {
    String jsSource = "var obj = {'key': obj};";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_name");

    try {
      pass.process(null, Node.parse(jsSource));
      fail("Expected an error for self-referential object literals.");
    } catch (AssertionError e) {
      // Expected exception
    }
  }

  @Test
  public void testInlineObjectLiteralWithEmptyObject() {
    String jsSource = "var obj = {};";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_name");
    pass.process(null, Node.parse(jsSource));

    String result = compiler.toSource(Node.SCOPE_GLOBAL, null, null);
    Node expectedNode = Node.parse("// No changes for empty object literals.");

    assertEquals(expectedNode.toPrintString(), result);
  }

  @Test
  public void testInlineObjectLiteralWithNestedObject() {
    String jsSource = "var obj = {'key1': {'key2': 'value2'}};";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_name");
    pass.process(null, Node.parse(jsSource));

    String result = compiler.toSource(Node.SCOPE_GLOBAL, null, null);
    Node expectedNode = Node.parse("var key1_key2 = 'value2';");

    assertEquals(expectedNode.toPrintString(), result);
  }
}