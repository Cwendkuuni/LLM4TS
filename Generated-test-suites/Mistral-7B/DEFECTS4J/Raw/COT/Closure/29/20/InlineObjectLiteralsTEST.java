import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest extends CompilerPassTestBase {

  @org.junit.Test
  public void testInlineObjectLiterals() {
    String sourceCode = "function foo() {" +
        "  var obj = {a: 1, b: 2};" +
        "  // Use the object literal" +
        "  console.log(obj.a);" +
        "}" +
        "foo();";

    AbstractCompiler compiler = newAbstractCompiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, getSafeNameIdSupplier());

    Node root = parseJs(sourceCode);
    pass.process(null, root);

    Node expectedCode = parseJs("function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  // Use the variables" +
        "  console.log(a);" +
        "}" +
        "foo();");

    Node actualCode = getTransformedCode(root);
    assertEquals(expectedCode.toSource(), actualCode.toSource());
  }
}