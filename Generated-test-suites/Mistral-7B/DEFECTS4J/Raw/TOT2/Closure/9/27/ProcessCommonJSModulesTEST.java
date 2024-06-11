import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    CompilerOptions options = new CompilerOptions();
    CompilerInput input = new CompilerInput("input.js", null);
    AbstractCompiler compiler = new Compiler(options);

    ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, ".");
    p.process(input, null);

    Node root = input.getRootNode();
    Node script = root.getFirstChild();

    // Basic functionality test
    assertEquals("goog.provide('module1');\ngoo.require('module2');", script.toString());

    // Edge case 1: Module name with special characters
    CompilerInput inputWithSpecialChars = new CompilerInput("inputWithSpecialChars.js", null);
    ProcessCommonJSModules pWithSpecialChars = new ProcessCommonJSModules(compiler, ".", true);
    pWithSpecialChars.process(inputWithSpecialChars, null);
    Node rootWithSpecialChars = inputWithSpecialChars.getRootNode();
    String expectedWithSpecialChars = "goog.provide('moduleWithSpecialChars$');\ngoo.require('moduleWithSpecialChars$');";
    assertEquals(expectedWithSpecialChars, rootWithSpecialChars.toString());

    // Edge case 2: Relative module path
    CompilerInput inputWithRelativePath = new CompilerInput("inputWithRelativePath.js", null);
    ProcessCommonJSModules pWithRelativePath = new ProcessCommonJSModules(compiler, ".", true);
    pWithRelativePath.process(inputWithRelativePath, null);
    Node rootWithRelativePath = inputWithRelativePath.getRootNode();
    String expectedWithRelativePath = "goog.provide('parentModule$');\ngoo.require('./childModule$');";
    assertEquals(expectedWithRelativePath, rootWithRelativePath.toString());

    // Edge case 3: Module exports
    CompilerInput inputWithModuleExports = new CompilerInput("inputWithModuleExports.js", null);
    ProcessCommonJSModules pWithModuleExports = new ProcessCommonJSModules(compiler, ".", true);
    pWithModuleExports.process(inputWithModuleExports, null);
    Node rootWithModuleExports = inputWithModuleExports.getRootNode();
    String expectedWithModuleExports = "goog.provide('moduleWithModuleExports$');\ngoo.require('moduleWithModuleExports$');\nmoduleWithModuleExports$.exports = function MyModule() {\n}\n";
    assertEquals(expectedWithModuleExports, rootWithModuleExports.toString());

    // Edge case 4: URISyntaxException
    try {
      CompilerInput inputWithInvalidURI = new CompilerInput("inputWithInvalidURI.js", new URI("invalid://uri"));
      ProcessCommonJSModules pWithInvalidURI = new ProcessCommonJSModules(compiler, ".", true);
      pWithInvalidURI.process(inputWithInvalidURI, null);
      fail("Expected URISyntaxException");
    } catch (URISyntaxException e) {
      // Expected
    }
  }
}