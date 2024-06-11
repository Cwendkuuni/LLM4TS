import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPassTestUtil.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestUtil.TestCompiler;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.rhino.Node;

public class CrossModuleMethodMotionTest {

  @Test
  public void testMoveMethodsAcrossModules() {
    TestCompiler compiler = newTestCompiler();
    AbstractCompiler abstractCompiler = compiler.toAbstractCompiler();
    IdGenerator idGenerator = new IdGenerator();
    CompilerPass pass = new CrossModuleMethodMotion(abstractCompiler, idGenerator, true);

    // Create a simple JavaScript file with a prototype method that is used
    // by multiple modules.
    String sourceCode =
        "var Foo = function() {" +
        "  this.bar = function() {" +
        "    console.log('Foo.bar');" +
        "  };" +
        "};" +
        "var Foo2 = function() {" +
        "  this.foo = Foo.prototype.bar;" +
        "};" +
        "Foo.prototype.bar = function() {" +
        "  console.log('Foo.prototype.bar');" +
        "};";

    Node externs = Node.external();
    Node root = compiler.parse(sourceCode, "test.js", null, externs);

    pass.process(externs, root);

    // Assert that the method was moved into Foo2
    Node foo2Proto = root.lookupVarInThisScope("Foo2.prototype");
    assertNotNull(foo2Proto);
    Node foo2Bar = foo2Proto.lookupProperty("bar");
    assertEquals("function () {" +
        "  console.log('Foo2.prototype.bar');" +
        "}", foo2Bar.toString());

    // Assert that the original method in Foo is replaced with a stub
    Node fooProto = root.lookupVarInThisScope("Foo.prototype");
    assertNotNull(fooProto);
    Node fooBar = fooProto.lookupProperty("bar");
    assertEquals("function JSCompiler_stubMethod(JSCompiler_stubMethod_id) {" +
        "  return function() {" +
        "    return JSCompiler_stubMap[JSCompiler_stubMethod_id].apply(this, arguments);" +
        "  };" +
        "}", fooBar.toString());
  }

  @Test
  public void testMoveMethodsAcrossModulesWithError() {
    TestCompiler compiler = newTestCompiler();
    AbstractCompiler abstractCompiler = compiler.toAbstractCompiler();
    IdGenerator idGenerator = new IdGenerator();
    CompilerPass pass = new CrossModuleMethodMotion(abstractCompiler, idGenerator, true);

    // Create a JavaScript file with a prototype method that cannot be moved
    // because it is not declared in the deepest common module.
    String sourceCode =
        "var Foo = function() {" +
        "  this.bar = function() {" +
        "    console.log('Foo.bar');" +
        "  };" +
        "};" +
        "var Foo2 = function() {" +
        "  this.foo = Foo.prototype.bar;" +
        "};" +
        "var Foo3 = function() {" +
        "  Foo.prototype.bar = function() {" +
        "    console.log('Foo3.prototype.bar');" +
        "  };" +
        "};" +
        "Foo2.prototype.bar = function() {" +
        "  console.log('Foo2.prototype.bar');" +
        "};";

    Node externs = Node.external();
    Node root = compiler.parse(sourceCode, "test.js", null, externs);

    try {
      pass.process(externs, root);
      fail("Expected an error");
    } catch (RuntimeException e) {
      // Expected error: JSC_INTERNAL_ERROR_MODULE_DEPEND
      assertEquals("null deepest common module", e.getMessage());
    }
  }

  @Test
  public void testMoveMethodsAcrossModulesWithoutModifyingExterns() {
    TestCompiler compiler = newTestCompiler();
    AbstractCompiler abstractCompiler = compiler.toAbstractCompiler();
    IdGenerator idGenerator = new IdGenerator();
    CompilerPass pass = new CrossModuleMethodMotion(abstractCompiler, idGenerator, false);

    // Create a JavaScript file with a prototype method that cannot be moved
    // because it is declared in the externs file.
    String sourceCode =
        "/** @extern */" +
        "var Foo = function() {" +
        "  this.bar = function() {" +
        "    console.log('Foo.bar');" +
        "  };" +
        "};" +
        "var Foo2 = function() {" +
        "  this.foo = Foo.prototype.bar;" +
        "};" +
        "Foo2.prototype.bar = function() {" +
        "  console.log('Foo2.prototype.bar');" +
        "};";

    Node externs = Node.external();
    externs.setProperty("Foo", Node.OBJECT);
    Node root = compiler.parse(sourceCode, "test.js", null, externs);

    try {
      pass.process(externs, root);
      fail("Expected an error");
    } catch (RuntimeException e) {
      // Expected error: JSC_INTERNAL_ERROR_MODIFY_EXTERNS
      assertEquals("Cannot modify externs", e.getMessage());
    }
  }
}