import static org.junit.Assert.*;
import org.junit.*;
import java.util.ArrayList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceFile;

public class RenameLabelsTest {

  private static CompilerOptions getCompilerOptions() {
    CompilerOptions options = new CompilerOptions();
    // Add any necessary options
    return options;
  }

  @Test
  public void testProcess_withValidInputs_shouldRenameLabels() {
    String input =
        "function bar() {\n" +
        "  a: {\n" +
        "    b: {\n" +
        "      foo();\n" +
        "    }\n" +
        "  }\n" +
        "  a: {\n" +
        "    b: break a;\n" +
        "  }\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = getCompilerOptions();
    ArrayList<SourceFile> inputFiles = new ArrayList<>();
    inputFiles.add(SourceFile.fromCode("input.js", input));

    compiler.compile(externs, inputFiles, options);

    String expectedOutput =
        "function bar(){a:{b:{foo()}}a:{b: break a}}";
    String actualOutput = compiler.toSource();

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testProcess_withInvalidInputs_shouldThrowError() {
    String input =
        "function bar() {\n" +
        "  a: {\n" +
        "    b: {\n" +
        "      foo();\n" +
        "    }\n" +
        "  }\n" +
        "  a {\n" +  // invalid syntax, missing colon
        "    b: break a;\n" +
        "  }\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = getCompilerOptions();
    ArrayList<SourceFile> inputFiles = new ArrayList<>();
    inputFiles.add(SourceFile.fromCode("input.js", input));

    try {
      compiler.compile(externs, inputFiles, options);
      fail("Expected CompilerException to be thrown");
    } catch (Exception e) {
      assertTrue(e instanceof JSError);
      assertEquals("Parse error. Illegal token: a", e.getMessage());
    }
  }
}