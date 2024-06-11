import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testProcess() throws Exception {
    String code = "" +
        "var a = 1;\n" +
        "var b;\n" +
        "b = 2;\n" +
        "b += 3;\n" +
        "console.log(b);\n";
    String expected = "" +
        "var b;\n" +
        "b = 2;\n" +
        "b += 3;\n" +
        "console.log(b);\n";
    assertCodeTransformation(code, expected);
  }

  private void assertCodeTransformation(String code, String expected) throws Exception {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.initOptions(options);
    CompilerOptionsReflector.setIncrementalCheck(compiler.getOptions());
    compiler.disableThreads();
    JSModule module = new JSModule("test");
    module.add(SourceFile.fromCode("input", code));
    compiler.compile(EMPTY_EXTERNS, ImmutableList.of(module), options);

    for (JSError error : compiler.getWarnings()) {
      System.err.println(error.toString());
    }
    assertEquals(0, compiler.getWarnings().length);

    Node scriptNode = compiler.getScriptNode(0);
    String outputCode = compiler.toSource();
    assertEquals(expected, outputCode);
  }
}