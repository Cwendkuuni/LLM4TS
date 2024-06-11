import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    String filename = "input.js";
    String inputCode = "var a = require('moduleA');";
    CompilerInput input = new CompilerInput(filename, inputCode);
    AbstractCompiler compiler = new AbstractCompiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    processor.process(input, null);

    JSModule module = processor.getModule();
    Node root = input.getRoot();

    // Check that require call was replaced with direct reference to module
    assertEquals("moduleA", root.getChildAtIndex(0).getFirstChild()
        .getString());

    // Check that goog.provide and goog.require were emitted
    assertEquals(2, root.getChildCount());
    Node googProvide = root.getChildAtIndex(0);
    Node googRequire = root.getChildAtIndex(1);
    assertEquals("goog.provide(\"moduleA\");", googProvide.toString());
    assertEquals("goog.require(\"moduleA\");", googRequire.toString());

    // Check that module exports were renamed to module$exports
    Node moduleExports = root.getFirstChild().getFirstChild().getChildAtIndex(1);
    assertEquals("module$exports", moduleExports.getString());

    // Check that global variables were renamed to avoid conflicts
    Node globalVariable = root.getChildAtIndex(2);
    assertEquals("a$$ProcessCommonJSModules", globalVariable.getName().getIdentifier());
  }

  @Test(expected = RuntimeException.class)
  public void testInvalidModuleName() {
    String filename = "input.js";
    String inputCode = "require('./invalid-filename.js');";
    CompilerInput input = new CompilerInput(filename, inputCode);
    AbstractCompiler compiler = new AbstractCompiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", true);
    processor.process(input, null);
  }

  @Test
  public void testRelativeModuleRequirement() {
    String filename = "moduleB.js";
    String inputCode = "require('./moduleA');";
    CompilerInput input = new CompilerInput(filename, inputCode);
    AbstractCompiler compiler = new AbstractCompiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", true);
    processor.process(input, null);

    JSModule module = processor.getModule();
    Node root = input.getRoot();

    // Check that require call was replaced with direct reference to module
    assertEquals("moduleA", root.getChildAtIndex(0).getFirstChild()
        .getString());

    // Check that goog.provide and goog.require were emitted
    assertEquals(2, root.getChildCount());
    Node googProvide = root.getChildAtIndex(0);
    Node googRequire = root.getChildAtIndex(1);
    assertEquals("goog.provide(\"moduleA\");", googProvide.toString());
    assertEquals("goog.require(\"moduleA\");", googRequire.toString());

    // Check that module exports were renamed to module$exports
    Node moduleExports = root.getFirstChild().getFirstChild().getChildAtIndex(1);
    assertEquals("module$exports", moduleExports.getString());

    // Check that module names are resolved correctly for relative require calls
    Node requireCall = root.getChildAtIndex(0);
    Node moduleName = requireCall.getFirstChild();
    String moduleAFileName = toModuleName("moduleA.js", filename);
    assertEquals(moduleAFileName, moduleName.getString());
  }

  @Test
  public void testModuleWithExports() {
    String filename = "moduleC.js";
    String inputCode = "module.exports = { a: 1 };";
    CompilerInput input = new CompilerInput(filename, inputCode);
    AbstractCompiler compiler = new AbstractCompiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", true);
    processor.process(input, null);

    JSModule module = processor.getModule();
    Node root = input.getRoot();

    // Check that module exports were renamed to module$exports
    Node moduleExports = root.getFirstChild().getFirstChild().getChildAtIndex(1);
    assertEquals("module$exports", moduleExports.getString());

    // Check that module exports were properly overridden
    Node override = root.getChildAtIndex(1);
    assertEquals(1, override.getChildCount());
    Node property = override.getChildAtIndex(0);
    assertEquals("a", property.getFirstChild().getString());
    assertEquals(IR.number(1), property.getChildAtIndex(1).getNumber());
  }

  private static String toModuleName(String requiredFilename,
      String currentFilename) {
    return ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
  }
}