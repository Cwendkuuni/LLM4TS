import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;

public class CrossModuleMethodMotionTest {

  @Test
  public void testProcess() {
    // Given
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, null, true);

    // When
    Node externs = NodeUtil.makeTextNode("module A {\n" +
        "  function foo() {\n" +
        "  }\n" +
        "}");
    Node source = NodeUtil.makeTextNode("module B {\n" +
        "  import A.*;\n" +
        "  function bar() {\n" +
        "    A.foo();\n" +
        "  }\n" +
        "}");
    pass.process(externs, source);

    // Then
    Node result = compiler.getAST();
    Node expected = NodeUtil.makeTextNode("module A {\n" +
        "  function foo() {\n" +
        "  }\n" +
        "}\n" +
        "module B {\n" +
        "  import A.*;\n" +
        "  function bar() {\n" +
        "    JSCompiler_stubMethod(0)();\n" +
        "  }\n" +
        "  JSCompiler_unstubMethod(0, function() {\n" +
        "    A.foo.call(this);\n" +
        "  });\n" +
        "}");
    assertEquals(expected.toString(), result.toString());
  }

  @Test
  public void testProcessWithNoMovements() {
    // Given
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, null, true);

    // When
    Node externs = NodeUtil.makeTextNode("module A {\n" +
        "  function foo() {\n" +
        "  }\n" +
        "}");
    Node source = NodeUtil.makeTextNode("module B {\n" +
        "  import A.*;\n" +
        "  function bar() {\n" +
        "    A.foo();\n" +
        "  }\n" +
        "  function baz() {\n" +
        "    A.foo();\n" +
        "  }\n" +
        "}");
    pass.process(externs, source);

    // Then
    Node result = compiler.getAST();
    Node expected = NodeUtil.makeTextNode("module A {\n" +
        "  function foo() {\n" +
        "  }\n" +
        "}\n" +
        "module B {\n" +
        "  import A.*;\n" +
        "  function bar() {\n" +
        "    A.foo.call(this);\n" +
        "  }\n" +
        "  function baz() {\n" +
        "    A.foo.call(this);\n" +
        "  }\n" +
        "}");
    assertEquals(expected.toString(), result.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcessWithNullExternRoot() {
    // Given
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, null, true);

    // When
    Node externs = null;
    Node root = NodeUtil.makeTextNode("module B {\n" +
        "  function bar() {\n" +
        "  }\n" +
        "}");
    pass.process(externs, root);

    // Then
    fail("Should throw an IllegalArgumentException");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcessWithNullRoot() {
    // Given
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, null, true);

    // When
    Node externs = NodeUtil.makeTextNode("module A {\n" +
        "  function foo() {\n" +
        "  }\n" +
        "}");
    Node root = null;
    pass.process(externs, root);

    // Then
    fail("Should throw an IllegalArgumentException");
  }
}