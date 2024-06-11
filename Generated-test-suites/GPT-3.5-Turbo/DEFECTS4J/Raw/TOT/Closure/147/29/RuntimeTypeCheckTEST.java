import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testing.TestExternsBuilder;
import com.google.javascript.rhino.Node;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RuntimeTypeCheckTest {

  @Test
  public void testProcess_withValidJavaScript_compilesSuccessfully() throws Exception {
    String input = readFile("valid.js");
    AbstractCompiler compiler = createCompiler(input);
    RuntimeTypeCheck pass = new RuntimeTypeCheck(compiler, null);
    pass.process(compiler.getRoot(), compiler.getRoot().getLastChild());
    assertFalse(compiler.hasErrors());
  }

  @Test
  public void testProcess_withInvalidJavaScript_reportsErrors() throws Exception {
    String input = readFile("invalid.js");
    AbstractCompiler compiler = createCompiler(input);
    RuntimeTypeCheck pass = new RuntimeTypeCheck(compiler, null);
    pass.process(compiler.getRoot(), compiler.getRoot().getLastChild());
    assertTrue(compiler.hasErrors());
    assertEquals(1, compiler.getErrors().length);
    JSError error = compiler.getErrors()[0];
    // Check the specific error message or error code
    assertEquals("TypeError: unsupported binary operation", error.getDescription());
  }

  private String readFile(String filename) throws IOException {
    URL url = Resources.getResource(filename);
    return Resources.toString(url, Charsets.UTF_8);
  }

  private AbstractCompiler createCompiler(String code) {
    CompilerOptions options = new CompilerOptions();
    options.setExterns(TestExternsBuilder.EMPTY);

    SourceFile input = SourceFile.fromCode("input.js", code);
    return new Compiler(System.err).initOptions(options).addAllInputs(ImmutableList.of(input)).parseInputs();
  }
}