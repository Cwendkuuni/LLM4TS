import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;

public class ProcessCommonJSModulesTest {

  private ProcessCommonJSModules processCommonJSModules;

  @Before
  public void setUp() {
    processCommonJSModules = new ProcessCommonJSModules(null, "");
  }

  @Test
  public void testGuessCJSModuleName() {
    String filename1 = "path/to/module.js";
    String expected1 = "module$path$to$module";
    String actual1 = processCommonJSModules.guessCJSModuleName(filename1);
    assertEquals(expected1, actual1);

    String filename2 = "module.js";
    String expected2 = "module$module";
    String actual2 = processCommonJSModules.guessCJSModuleName(filename2);
    assertEquals(expected2, actual2);
  }

  @Test
  public void testToModuleName() {
    String filename1 = "path/to/module.js";
    String expected1 = "module$path$to$module";
    String actual1 = ProcessCommonJSModules.toModuleName(filename1);
    assertEquals(expected1, actual1);

    String filename2 = "module.js";
    String expected2 = "module$module";
    String actual2 = ProcessCommonJSModules.toModuleName(filename2);
    assertEquals(expected2, actual2);
  }

  @Test
  public void testToModuleNameWithRelativeAddressing() {
    String requiredFilename1 = "./module1";
    String currentFilename1 = "path/to/module.js";
    String expected1 = "module$path$to$module1";
    String actual1 = ProcessCommonJSModules.toModuleName(requiredFilename1, currentFilename1);
    assertEquals(expected1, actual1);

    String requiredFilename2 = "../module2";
    String currentFilename2 = "path/to/module.js";
    String expected2 = "module$path$module2";
    String actual2 = ProcessCommonJSModules.toModuleName(requiredFilename2, currentFilename2);
    assertEquals(expected2, actual2);
  }

  @Test
  public void testNormalizeSourceName() {
    String filename1 = "path/to/module.js";
    String expected1 = "to/module.js";
    String actual1 = processCommonJSModules.normalizeSourceName(filename1);
    assertEquals(expected1, actual1);

    String filename2 = "module.js";
    String expected2 = "module.js";
    String actual2 = processCommonJSModules.normalizeSourceName(filename2);
    assertEquals(expected2, actual2);

    String filename3 = "path/to/module.module.js";
    String expected3 = "to/module.module.js";
    String actual3 = processCommonJSModules.normalizeSourceName(filename3);
    assertEquals(expected3, actual3);
  }

  @Test
  public void testProcess_RequireCall() {
    // Test require("module1")
    // Original: require("module1")
    // Expected: module1
    // Expected goog.require: var module1 = goog.require("module1");
    String code1 = "require(\"module1\");";
    String expected1 = "module1;\nvar module1 = goog.require(\"module1\");\n";
    String actual1 = process(code1);
    assertEquals(expected1, actual1);

    // Test require("../path/module2")
    // Original: require("../path/module2")
    // Expected: module$path$module2
    // Expected goog.require: var module$path$module2 = goog.require("module$path$module2");
    String code2 = "require(\"../path/module2\");";
    String expected2 = "module$path$module2;\nvar module$path$module2 = goog.require(\"module$path$module2\");\n";
    String actual2 = process(code2);
    assertEquals(expected2, actual2);
  }

  @Test
  public void testProcess_Script() {
    // Test script
    // Original: script
    // Expected: var module$script = {};
    // Expected goog.provide: goog.provide("module$script");
    String code = "script";
    String expected = "var module$script = {};\n" +
        "goog.provide(\"module$script\");\n";
    String actual = process(code);
    assertEquals(expected, actual);
  }

  @Test
  public void testProcess_ModuleExports() {
    // Test module.exports
    // Original: module.exports
    // Expected: module$exports
    String code = "module.exports";
    String expected = "module$exports";
    String actual = process(code);
    assertEquals(expected, actual);
  }

  @Test
  public void testSuffixVarsCallback() {
    Scope.Var var1 = new Scope.Var("var1", false);
    var1.setGlobal();
    Scope.Var var2 = new Scope.Var("var2", false);
    var2.setGlobal();
    processCommonJSModules.processVars(var1, var2);

    // Test var1
    // Original: var1
    // Expected: var1$$suffix
    String name1 = "var1";
    String expected1 = "var1$$suffix";
    String actual1 = processCommonJSModules.suffixVarName(name1);
    assertEquals(expected1, actual1);

    // Test var2
    // Original: var2
    // Expected: var2$$suffix
    String name2 = "var2";
    String expected2 = "var2$$suffix";
    String actual2 = processCommonJSModules.suffixVarName(name2);
    assertEquals(expected2, actual2);

    // Test var3 (not global var)
    // Original: var3
    // Expected: var3
    String name3 = "var3";
    String expected3 = "var3";
    String actual3 = processCommonJSModules.suffixVarName(name3);
    assertEquals(expected3, actual3);
  }

  private String process(String code) {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerInput input = new CompilerInput(SourceFile.fromCode("test", code));
    compiler.initOptions(new CompilerOptions());
    Node root = compiler.parseInputs(input);
    processCommonJSModules.process(null, root);
    return compiler.toSource();
  }

}