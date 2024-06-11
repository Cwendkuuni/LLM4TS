package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CompilerPass;

@RunWith(JUnit4.class)
public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CompilerInput input = new CompilerInput("test.js");
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", true);

    input.setSourceFile("test.js");
    input.setCompilationUnit(createTestScript("require('module').exports = 1;"));

    pass.process(input, null);

    Node root = input.getRootNode();
    Node moduleExports = root.getFirstChild();
    assertEquals(1, moduleExports.getNumber());
    assertNotNull(pass.getModule());
  }

  @Test
  public void testProcessCommonJSModulesWithDependency() {
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CompilerInput input = new CompilerInput("test.js");
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", true);

    input.setSourceFile("test.js");
    input.setSourceFile("dep.js", "require('dep').version = '1.0.0'");
    input.setCompilationUnit(createTestScript("require('dep').version;"));

    pass.process(input, null);

    Node root = input.getRootNode();
    Node requireCall = root.getFirstChild().getFirstChild();
    Node moduleRef = requireCall.getChildAtIndex(1);
    String depModuleName = toModuleName("dep.js");
    assertEquals("dep", moduleRef.getString());
    assertEquals("dep", requireCall.getChildAtIndex(0).getString());
    assertEquals("dep.version", moduleRef.getFirstChild().getString());
    assertEquals("1.0.0", moduleRef.getFirstChild().getFirstChild().getString());
    assertNotNull(pass.getModule());
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExports() {
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CompilerInput input = new CompilerInput("test.js");
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", true);

    input.setSourceFile("test.js");
    input.setSourceFile("dep.js", "module.exports = { version: '1.0.0' }");
    input.setCompilationUnit(createTestScript("console.log(require('dep').version);"));

    pass.process(input, null);

    Node root = input.getRootNode();
    Node requireCall = root.getFirstChild();
    Node moduleRef = requireCall.getChildAtIndex(1);
    String depModuleName = toModuleName("dep.js");
    assertEquals("dep", moduleRef.getString());
    assertEquals("dep", requireCall.getChildAtIndex(0).getString());
    Node moduleExports = moduleRef.getFirstChild();
    assertEquals("version", moduleExports.getFirstChild().getQualifiedName());
    assertEquals("1.0.0", moduleExports.getFirstChild().getFirstChild().getString());
    assertNotNull(pass.getModule());
  }

  @Test
  public void testProcessCommonJSModulesWithMultipleRequirements() {
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CompilerInput input = new CompilerInput("test.js");
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", true);

    input.setSourceFile("test.js");
    input.setSourceFile("dep1.js", "module.exports = 1");
    input.setSourceFile("dep2.js", "module.exports = 2");
    input.setCompilationUnit(createTestScript("console.log(require('dep1'), require('dep2'));"));

    pass.process(input, null);

    Node root = input.getRootNode();
    Node logCall = root.getFirstChild();
    Node logArgs = logCall.getFirstChild();
    Node dep1 = logArgs.getFirstChild();
    Node dep2 = logArgs.getFirstChildAtIndex(1);
    assertEquals(1, dep1.getNumber());
    assertEquals(2, dep2.getNumber());
    assertNotNull(pass.getModule());
  }

  private Node createTestScript(String code) {
    return IR.script(IR.text(code), null);
  }

  private String toModuleName(String filename) {
    return "module$" + filename.replaceAll("/", "$")
        .replaceAll(".js$", "")
        .replaceAll("-", "_");
  }
}