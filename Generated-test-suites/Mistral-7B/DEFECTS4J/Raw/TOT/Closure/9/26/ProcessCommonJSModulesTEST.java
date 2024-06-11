import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.ProcessCommonJSModules.JSModule;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    String source = "// Input file\n" +
        "var a = 1;\n" +
        "require('./b');\n" +
        "function b() {\n" +
        "  exports.c = 2;\n" +
        "}\n" +
        "exports.d = 3;\n";

    CompilerInput input = new CompilerInput(source, "input.js");
    CompilerPass pass = new ProcessCommonJSModules(new Compiler(), ".");
    pass.process(input, null);

    Node expectedOutput = IR.program(
        IR.block(
            IR.var(IR.name("a"), IR.number(1)),
            IR.block(
                IR.call(IR.getprop(IR.name("goog"), IR.string("require")),
                    IR.string("b")),
                IR.assign(IR.name("b$module$exports"), IR.block(
                    IR.var(IR.name("c"), IR.number(2))
                )),
                IR.assign(IR.name("b$module$exports$d"), IR.number(3))
            ),
            IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
                IR.string("b"))
        )
    );

    Node actualOutput = input.getOutput();

    assertEquals(expectedOutput.toSource(), actualOutput.toSource());

    JSModule module = pass.getModule();
    assertSame(module.getName(), "b");
  }

  @Test
  public void testProcessCommonJSModulesRelativePath() {
    String source = "// Input file\n" +
        "var a = 1;\n" +
        "require('./a');\n" +
        "function a() {\n" +
        "  exports.c = 2;\n" +
        "}\n" +
        "exports.d = 3;\n";

    CompilerInput input = new CompilerInput(source, "input.js");
    CompilerPass pass = new ProcessCommonJSModules(new Compiler(), ".");
    pass.process(input, null);

    Node expectedOutput = IR.program(
        IR.block(
            IR.var(IR.name("a$module$exports"), IR.block(
                IR.var(IR.name("c"), IR.number(2))
            )),
            IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
                IR.string("a"))
        )
    );

    Node actualOutput = input.getOutput();

    assertEquals(expectedOutput.toSource(), actualOutput.toSource());

    JSModule module = pass.getModule();
    assertSame(module.getName(), "a");
  }

  @Test
  public void testProcessCommonJSModulesNoModuleExports() {
    String source = "// Input file\n" +
        "var a = 1;\n" +
        "require('./b');\n" +
        "function b() {\n" +
        "}\n";

    CompilerInput input = new CompilerInput(source, "input.js");
    CompilerPass pass = new ProcessCommonJSModules(new Compiler(), ".");
    pass.process(input, null);

    Node expectedOutput = IR.program(IR.block(IR.var(IR.name("a"), IR.number(1))));

    Node actualOutput = input.getOutput();

    assertEquals(expectedOutput.toSource(), actualOutput.toSource());

    assertNull(pass.getModule());
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJSModulesUriSyntaxException() {
    String source = "// Input file\n" +
        "require('data:text/javascript;base64,');";

    CompilerInput input = new CompilerInput(source, "input.js");
    CompilerPass pass = new ProcessCommonJSModules(new Compiler(), ".");
    pass.process(input, null);
  }
}