import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessorTest.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Processor;
import com.google.javascript.jscomp.test_util.J2JsTestRunner;

import java.io.IOException;
import java.util.Arrays;

public class ProcessCommonJSModulesTest {

  public static void main(String[] args) throws Exception {
    J2JsTestRunner runner = new J2JsTestRunner();
    runner.runTests("TestProcessCommonJSModules.js", ProcessCommonJSModulesTest.class);
  }

  @SuppressWarnings("unchecked")
  @CompileTests(1)
  public static void testProcessCommonJSModules() throws Exception {
    Processor processor = setupProcessor("testdata", new CompilerOptions());

    AbstractCompiler compiler = processor.getOptions().getCompiler();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    CompilerInput input = new CompilerInput(testfile("input/TestModule1.js"), null);
    CompilerInput output = process(processor, Arrays.asList(input));

    assertCompilesTo("testdata/output/TestModule1-compiled.js", output.getCode());
  }

  @CompileTests(1)
  public static void testProcessCommonJSModulesWithDependencies()
      throws Exception {
    Processor processor = setupProcessor("testdata", new CompilerOptions());

    AbstractCompiler compiler = processor.getOptions().getCompiler();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    CompilerInput input = new CompilerInput(testfile("input/TestModule1.js"), null);
    CompilerInput dep1 = new CompilerInput(testfile("input/TestModule2.js"), null);

    input.addDependency(dep1);

    CompilerInput output = process(processor, Arrays.asList(input));

    assertCompilesTo("testdata/output/TestModule1-compiled-with-dependency.js", output.getCode());
  }

  @CompileTests(1)
  public static void testProcessCommonJSModulesWithoutDependenciesReporting()
      throws Exception {
    Processor processor = setupProcessor("testdata", new CompilerOptions());

    AbstractCompiler compiler = processor.getOptions().getCompiler();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "", false);

    CompilerInput input = new CompilerInput(testfile("input/TestModule1.js"), null);

    CompilerInput output = process(processor, Arrays.asList(input));

    assertCompilesTo("testdata/output/TestModule1-compiled-no-dependencies.js", output.getCode());
  }

  @CompileTests(1)
  public static void testProcessCommonJSModulesWithExports() throws Exception {
    Processor processor = setupProcessor("testdata", new CompilerOptions());

    AbstractCompiler compiler = processor.getOptions().getCompiler();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    CompilerInput input = new CompilerInput(testfile("input/TestModuleWithExports.js"), null);

    CompilerInput output = process(processor, Arrays.asList(input));

    assertCompilesTo("testdata/output/TestModuleWithExports-compiled.js",
        output.getCode());
  }

  @CompileTests(1)
  public static void testProcessCommonJSModulesWithRelativeRequire()
      throws Exception {
    Processor processor = setupProcessor("testdata", new CompilerOptions());

    AbstractCompiler compiler = processor.getOptions().getCompiler();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    CompilerInput input = new CompilerInput(testfile("input/TestModuleWithRelativeRequire.js"), null);

    CompilerInput output = process(processor, Arrays.asList(input));

    assertCompilesTo("testdata/output/TestModuleWithRelativeRequire-compiled.js",
        output.getCode());
  }

  @CompileTests(1)
  public static void testProcessCommonJSModulesWithInvalidURI() throws Exception {
    Processor processor = setupProcessor("testdata", new CompilerOptions());

    AbstractCompiler compiler = processor.getOptions().getCompiler();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    CompilerInput input = new CompilerInput(testfile("input/TestModuleWithInvalidURI.js"), null);

    try {
      process(processor, Arrays.asList(input));
      fail("Expected CompilationError");
    } catch (CompilerError e) {
      // Expected
    }
  }

  @CompileTests(1)
  public static void testProcessCommonJSModulesWithInvalidRequire()
      throws Exception {
    Processor processor = setupProcessor("testdata", new CompilerOptions());

    AbstractCompiler compiler = processor.getOptions().getCompiler();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    CompilerInput input = new CompilerInput(testfile("input/TestModuleWithInvalidRequire.js"), null);

    try {
      process(processor, Arrays.asList(input));
      fail("Expected CompilationError");
    } catch (CompilerError e) {
      // Expected
    }
  }

  @CompileTests(1)
  public static void testProcessCommonJSModulesWithInvalidModuleName()
      throws Exception {
    Processor processor = setupProcessor("testdata", new CompilerOptions());

    AbstractCompiler compiler = processor.getOptions().getCompiler();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    CompilerInput input = new CompilerInput(testfile("input/TestModuleWithInvalidModuleName.js"), null);

    try {
      process(processor, Arrays.asList(input));
      fail("Expected CompilationError");
    } catch (CompilerError e) {
      // Expected
    }
  }

  @CompileTests(1)
  public static void testProcessCommonJSModulesWithInvalidNormalizedName()
      throws Exception {
    Processor processor = setupProcessor("testdata", new CompilerOptions());

    AbstractCompiler compiler = processor.getOptions().getCompiler();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    CompilerInput input = new CompilerInput(testfile("input/TestModuleWithInvalidNormalizedName.js"), null);

    try {
      process(processor, Arrays.asList(input));
      fail("Expected CompilationError");
    } catch (CompilerError e) {
      // Expected
    }
  }
}