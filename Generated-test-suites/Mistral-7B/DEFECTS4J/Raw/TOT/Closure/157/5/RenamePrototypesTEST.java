import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.Option.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Test;

public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypes() throws Exception {
    String input =
        "function Foo() {}\n" +
        "Foo.prototype.foo = function() {\n" +
        "  return 'oldFoo';\n" +
        "};\n" +
        "Foo.prototype.bar = function() {\n" +
        "  return 'oldBar';\n" +
        "};\n" +
        "Foo.prototype.baz = function() {\n" +
        "  return 'oldBaz';\n" +
        "};\n" +
        "var foo = new Foo();\n" +
        "console.log(foo.foo()); // logs 'oldFoo'\n" +
        "console.log(foo.bar()); // logs 'oldBar'\n" +
        "console.log(foo.baz()); // logs 'oldBaz'\n" +
        "delete Foo.prototype.baz;\n" +
        "console.log(foo.baz); // logs undefined\n" +
        "Foo.prototype.qux = function() {\n" +
        "  return 'oldQux';\n" +
        "};\n" +
        "console.log(foo.qux); // logs 'oldQux'\n" +
        "Foo.prototype.qux = null;\n" +
        "console.log(foo.qux); // logs null";

    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setOption(aggressiveRenaming, true);
    options.setOption(warnOnly, true);

    Compiler compiler = new Compiler();
    CompilerInput inputNode = NodeFactory.compile(input);
    CompilerResult result = compiler.compile(inputNode, options);

    if (result.errors.length > 0) {
      fail("Compilation failed: " + result.errors[0].message);
    }

    Node root = result.ast;

    RenamePrototypes renamePrototypes = new RenamePrototypes(
        compiler, true, null, null);
    renamePrototypes.process(
        result.inputs[0],
        result.inputs[1]);

    String expectedOutput =
        "function Foo() {}\n" +
        "Foo.prototype.foo = function() {\n" +
        "  return 'renamedFoo';\n" +
        "};\n" +
        "Foo.prototype.bar = function() {\n" +
        "  return 'renamedBar';\n" +
        "};\n" +
        "Foo.prototype.baz = function() {\n" +
        "  return 'renamedBaz';\n" +
        "};\n" +
        "var foo = new Foo();\n" +
        "console.log(foo.foo()); // logs 'renamedFoo'\n" +
        "console.log(foo.bar()); // logs 'renamedBar'\n" +
        "console.log(foo.baz()); // logs 'renamedBaz'\n" +
        "delete Foo.prototype.baz;\n" +
        "console.log(foo.baz); // logs undefined\n" +
        "Foo.prototype.qux = function() {\n" +
        "  return 'renamedQux';\n" +
        "};\n" +
        "console.log(foo.qux); // logs 'renamedQux'\n" +
        "Foo.prototype.qux = null;\n" +
        "console.log(foo.qux); // logs null";

    String actualOutput = NodeFactory.compileToString(result.ast);
    assertEquals(expectedOutput, actualOutput);
  }
}