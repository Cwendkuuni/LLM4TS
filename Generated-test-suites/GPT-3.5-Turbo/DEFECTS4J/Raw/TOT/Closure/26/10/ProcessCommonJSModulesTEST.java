import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.JSSourceFile;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testing.CompilerTestCase;

public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private ProcessCommonJSModules pass;

  @Before
  public void setUp() {
    compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    pass = new ProcessCommonJSModules(compiler, ".");
    pass.initCompilerOptionsIfTesting(options);
  }

  @Test
  public void testProcess_withRequireCall_shouldChangeRequireToModuleRef() {
    String input = "var module = require('moduleName');";
    String expectedOutput = "var module = moduleName;";

    String output = compile(input);

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testProcess_withRequireCall_shouldAddGoogRequireToScript() {
    String input = "var module = require('moduleName');";
    String expectedOutput = "goog.require('moduleName'); var module = moduleName;";

    String output = compile(input, true);

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testProcess_withScript_shouldAddGoogProvide() {
    String input = "var module = module || {}; module.exports = {};";
    String expectedOutput = "goog.provide('moduleName'); var module = module || {}; module.exports = {};";

    String output = compile(input, true);

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testProcess_withScript_shouldAppendSuffixToGlobalVars() {
    String input = "var x = 1; var y = 2; module.exports = {};";
    String expectedOutput = "var x$$moduleName = 1; var y$$moduleName = 2; module.exports = {};";

    String output = compile(input, true);

    assertEquals(expectedOutput, output);
  }

  private String compile(String input) {
    return compile(input, false);
  }

  private String compile(String input, boolean reportDependencies) {
    CompilerTestCase tester = new CompilerTestCase() {
      @Override
      protected CompilerPass getProcessor(Compiler compiler) {
        return pass;
      }
    };
    tester.setExpectParseWarningsThisTest();
    tester.config.setClosurePass(true);
    tester.config.enableClosurePass(reportDependencies);

    CompilerOptions options = tester.getOptions();
    compiler.initOptions(options);
    compiler.compile(SourceFile.fromCode("input.js", input), JSSourceFile.fromCode("externs.js", ""), options);

    return compiler.toSource();
  }

}