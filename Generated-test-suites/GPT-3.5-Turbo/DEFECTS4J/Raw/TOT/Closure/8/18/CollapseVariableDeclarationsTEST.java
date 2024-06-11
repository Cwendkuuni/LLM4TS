import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceFile;
import org.junit.Before;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private CollapseVariableDeclarations collapseVariableDeclarations;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    options = new CompilerOptions();
    collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
  }

  @Test
  public void testCollapseVariableDeclarations_simple() {
    String input = 
        "var a;\n" +
        "var b = 1;\n" +
        "var c = 2;\n";
    String expectedOutput = "var a,b=1,c=2;";
    
    CompilerTestHelper.compileAndCheck(input, expectedOutput);
  }

  @Test
  public void testCollapseVariableDeclarations_assignments() {
    String input = 
        "a = true;\n" +
        "b = true;\n" +
        "var c = true;\n";
    String expectedOutput = "var c=b=a=true;";
    
    CompilerTestHelper.compileAndCheck(input, expectedOutput);
  }
  
  @Test
  public void testCollapseVariableDeclarations_insideIf() {
    String input =
        "if (x) {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "} else {\n" +
        "  var c = 3;\n" +
        "}\n";
    String expectedOutput =
        "if(x){var a=1,b=2;}else{var c=3;}";
    
    CompilerTestHelper.compileAndCheck(input, expectedOutput);
  }
  

  @Test
  public void testCollapseVariableDeclarations_notVar() {
    String input =
        "var a = 1;\n" +
        "let b = 2;\n" +
        "const c = 3;\n";
    String expectedOutput =
        "var a=1;\n" +
        "let b=2;\n" +
        "const c=3;";

    CompilerTestHelper.compileAndCheck(input, expectedOutput);
  }
  
  @Test
  public void testCollapseVariableDeclarations_redeclaration() {
    String input =
        "var a;\n" +
        "a = 1;\n" +
        "var a = 2;\n";
    
    String expectedOutput = "var a;\n" +
        "var a=1;\n" +
        "var /** @suppress {duplicate} */ a=2;";

    CompilerTestHelper.compileAndCheck(input, expectedOutput);
  }

  @Test
  public void testCollapseVariableDeclarations_redeclarationAfter() {
    String input =
        "var a = 1;\n" +
        "a = 2;\n" +
        "a = 3;\n";
    
    String expectedOutput = "var a;\n" +
        "a=1;\n" +
        "a=2;\n" +
        "a=3;";

    CompilerTestHelper.compileAndCheck(input, expectedOutput);
  }

  @Test
  public void testCollapseVariableDeclarations_noChange() {
    String input = 
        "var a = 1;\n" +
        "var b = 2;\n" +
        "var c = 3;\n";
    String expectedOutput = "var a=1,b=2,c=3;";

    CompilerTestHelper.compileAndCheck(input, expectedOutput);
  }

  private static class CompilerTestHelper {
    /**
     * Compile the input and check if it matches the expected output.
     */
    static void compileAndCheck(String input, String expectedOutput) {
      SourceFile inputSource = SourceFile.fromCode("input.js", input);
      SourceFile expectedOutputSource = SourceFile.fromCode("expected_output.js", expectedOutput);

      Compiler compiler = new Compiler();
      CompilerOptions options = new CompilerOptions();
      options.setLanguage(CompilerOptions.LanguageMode.ECMASCRIPT_2022);

      List<SourceFile> externs = AbstractCommandLineRunner.getDefaultExterns();

      Result result = compiler.compile(externs, Arrays.asList(inputSource), options);
      JSError[] errors = result.errors;

      assertTrue("Compilation should succeed", result.success);
      assertEquals(0, errors.length);

      String compiledSource = compiler.toSource();
      String expectedCompiledSource = expectedOutputSource.getCode();

      assertEquals(expectedCompiledSource.trim(), compiledSource.trim());
    }
  }
}