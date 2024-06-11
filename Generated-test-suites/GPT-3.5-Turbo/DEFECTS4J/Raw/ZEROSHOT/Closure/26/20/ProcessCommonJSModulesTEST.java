import org.junit.*;
import static org.junit.Assert.*;

public class ProcessCommonJSModulesTest {

  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules();
    String filename = "test.js";
    String expected = "module$test";
    String actual = pcm.guessCJSModuleName(filename);
    assertEquals(expected, actual);
  }

  @Test
  public void testToModuleName() {
    String filename = "test.js";
    String expected = "module$test";
    String actual = ProcessCommonJSModules.toModuleName(filename);
    assertEquals(expected, actual);
  }

  @Test
  public void testToModuleNameWithRelativeAddressing() {
    String requiredFilename = "./subdir/test.js";
    String currentFilename = "src/test.js";
    String expected = "module$subdir$test";
    String actual = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals(expected, actual);
  }

  @Test
  public void testProcess() {
    // Create a test compiler and input
    Compiler compiler = new Compiler();
    JSSourceFile input = JSSourceFile.fromCode("test.js", "console.log('Hello, world!');");
    
    // Create a new ProcessCommonJSModules instance
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, ".");
    
    // Process the input file
    pcm.process(compiler.getRoot().getFirstChild(), input);
    
    // Get the processed module
    JSModule module = pcm.getModule();
    
    // Check the processed module's name
    String expectedModuleName = "module$test";
    String actualModuleName = module.getName();
    assertEquals(expectedModuleName, actualModuleName);
    
    // Get the processed module's source code
    String expectedSource = "console.log('Hello, world!');";
    String actualSource = module.getSource();
    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testVisitRequireCall() {
    // Create a test compiler and input
    Compiler compiler = new Compiler();
    JSSourceFile input = JSSourceFile.fromCode("test.js", "var foo = require('bar');");
    
    // Create a new ProcessCommonJSModules instance
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, ".");
    
    // Add the required module to the input file
    compiler.getInput(input.getName()).addRequire("module$bar");
    
    // Process the input file
    pcm.process(compiler.getRoot().getFirstChild(), input);
    
    // Check the processed source code
    String expectedSource = "var foo = module$bar;";
    String actualSource = compiler.toSource(compiler.getRoot().getFirstChild());
    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testVisitScript() {
    // Create a test compiler and input
    Compiler compiler = new Compiler();
    JSSourceFile input = JSSourceFile.fromCode("test.js", "console.log('Hello, world!');");
    
    // Create a new ProcessCommonJSModules instance
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, ".");
    
    // Process the input file
    pcm.process(compiler.getRoot().getFirstChild(), input);
    
    // Check the processed module's name
    String expectedModuleName = "module$test";
    String actualModuleName = pcm.getModule().getName();
    assertEquals(expectedModuleName, actualModuleName);
    
    // Check the processed module's source code
    String expectedSource = "var module$test = {};\nconsole.log('Hello, world!');";
    String actualSource = compiler.toSource(compiler.getRoot().getFirstChild());
    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testEmitOptionalModuleExportsOverride() {
    // Create a test compiler and input
    Compiler compiler = new Compiler();
    JSSourceFile input = JSSourceFile.fromCode("test.js", "module.exports = { greeting: 'Hello' };");
    
    // Create a new ProcessCommonJSModules instance
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, ".");
    
    // Process the input file
    pcm.process(compiler.getRoot().getFirstChild(), input);
    
    // Check the processed module's name
    String expectedModuleName = "module$test";
    String actualModuleName = pcm.getModule().getName();
    assertEquals(expectedModuleName, actualModuleName);
    
    // Check the processed module's source code
    String expectedSource = "var module$test = {};\nif (module$test.module$exports) { module$test = module$test.module$exports; }\nmodule$test = { greeting: 'Hello' };";
    String actualSource = compiler.toSource(compiler.getRoot().getFirstChild());
    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testVisitModuleExports() {
    // Create a test compiler and input
    Compiler compiler = new Compiler();
    JSSourceFile input = JSSourceFile.fromCode("test.js", "module.exports = { greeting: 'Hello' };");
    
    // Create a new ProcessCommonJSModules instance
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, ".");
    
    // Process the input file
    pcm.process(compiler.getRoot().getFirstChild(), input);
    
    // Check the processed module's name
    String expectedModuleName = "module$test";
    String actualModuleName = pcm.getModule().getName();
    assertEquals(expectedModuleName, actualModuleName);
    
    // Check the processed module's source code
    String expectedSource = "var module$test = {};\nif (module$test.module$exports) { module$test = module$test.module$exports; }\nmodule$test = { greeting: 'Hello' };";
    String actualSource = compiler.toSource(compiler.getRoot().getFirstChild());
    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testSuffixVarsCallback() {
    // Create a test compiler and input
    Compiler compiler = new Compiler();
    JSSourceFile input = JSSourceFile.fromCode("test.js", "var foo = 3;");
    
    // Create a new ProcessCommonJSModules instance
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, ".");
    
    // Process the input file
    pcm.process(compiler.getRoot().getFirstChild(), input);
    
    // Check the processed module's name
    String expectedModuleName = "module$test";
    String actualModuleName = pcm.getModule().getName();
    assertEquals(expectedModuleName, actualModuleName);
    
    // Check the processed module's source code
    String expectedSource = "var module$test = {};\nif (module$test.module$exports) { module$test = module$test.module$exports; }\nvar foo$$module$test = 3;";
    String actualSource = compiler.toSource(compiler.getRoot().getFirstChild());
    assertEquals(expectedSource, actualSource);
  }
}