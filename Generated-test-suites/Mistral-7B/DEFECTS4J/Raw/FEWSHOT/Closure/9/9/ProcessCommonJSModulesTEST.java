package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Processor;
import com.google.javascript.jscomp.CompilationUnit;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerInput;
import com.google.common.collect.Sets;
import com.google.common.base.Preconditions;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String inputCode = "var foo = require('bar');";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "");
    CompilerInput input = new CompilerInput("test.js", inputCode, null);
    pcm.process(input, null);
    CompilationUnit cu = input.getCompiledUnit();

    Node root = cu.getTree();

    Node requireCall = NodeUtil.findFirstDescendantOfType(root, Node.CALL);
    Preconditions.checkState(requireCall != null, "Require call not found.");
    String moduleName = pcm.guessCJSModuleName(input.getSourceName());
    Node moduleRef = NodeUtil.findFirstDescendantOfType(requireCall, Node.NAME);
    Preconditions.checkState(moduleRef != null, "Module reference not found.");
    assertEquals(moduleName, moduleRef.getString());

    Node moduleExports = NodeUtil.findFirstDescendantOfType(root, Node.GETPROP, Node.NAME, "module.exports");
    Preconditions.checkState(moduleExports != null, "Module exports not found.");
    assertEquals("module$exports", moduleExports.getChildAtIndex(1).getString());

    Node googProvide = NodeUtil.findFirstDescendantOfType(root, Node.CALL, Node.NAME, "goog.provide");
    Preconditions.checkState(googProvide != null, "goog.provide not found.");
    assertEquals(moduleName, googProvide.getChildAtIndex(1).getString());

    Set<String> requires = cu.getInput().getRequires();
    assertEquals(1, requires.size());
    assertEquals("bar", requires.iterator().next());
  }

  @Test
  public void testProcessCommonJSModulesWithRelativeRequire() throws Exception {
    String inputCode = "var foo = require('./bar');";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "");
    CompilerInput input = new CompilerInput("test.js", inputCode, null);
    String currentFilename = "test.js";
    pcm.process(input, null);
    CompilationUnit cu = input.getCompiledUnit();

    Node root = cu.getTree();

    Node requireCall = NodeUtil.findFirstDescendantOfType(root, Node.CALL);
    Preconditions.checkState(requireCall != null, "Require call not found.");
    String moduleName = pcm.toModuleName(requireCall.getChildAtIndex(1).getString(), currentFilename);
    Node moduleRef = NodeUtil.findFirstDescendantOfType(requireCall, Node.NAME);
    Preconditions.checkState(moduleRef != null, "Module reference not found.");
    assertEquals(moduleName, moduleRef.getString());
  }

  @Test
  public void testProcessCommonJSModulesWithAbsoluteRequire() throws Exception {
    String inputCode = "var foo = require('/path/to/bar.js');";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "");
    CompilerInput input = new CompilerInput("test.js", inputCode, null);
    String currentFilename = "test.js";
    pcm.process(input, null);
    CompilationUnit cu = input.getCompiledUnit();

    Node root = cu.getTree();

    Node requireCall = NodeUtil.findFirstDescendantOfType(root, Node.CALL);
    Preconditions.checkState(requireCall != null, "Require call not found.");
    String moduleName = pcm.toModuleName(requireCall.getChildAtIndex(1).getString(), currentFilename);
    Node moduleRef = NodeUtil.findFirstDescendantOfType(requireCall, Node.NAME);
    Preconditions.checkState(moduleRef != null, "Module reference not found.");
    assertEquals(moduleName, moduleRef.getString());
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExports() throws Exception {
    String inputCode = "var foo = module.exports.bar;";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "");
    CompilerInput input = new CompilerInput("test.js", inputCode, null);
    pcm.process(input, null);
    CompilationUnit cu = input.getCompiledUnit();

    Node root = cu.getTree();

    Node moduleExports = NodeUtil.findFirstDescendantOfType(root, Node.GETPROP, Node.NAME, "module.exports");
    Preconditions.checkState(moduleExports != null, "Module exports not found.");
    Node exports = NodeUtil.findFirstDescendantOfType(moduleExports.getChildAtIndex(1), Node.GETPROP, Node.NAME, "bar");
    Preconditions.checkState(exports != null, "Variable bar not found.");
    assertEquals("bar", exports.getString());
  }
}

class Compiler extends AbstractCompiler {
  @Override
  public CompilerPass getPass(String passName) {
    if ("ProcessCommonJSModules".equals(passName)) {
      return new ProcessCommonJSModules(this, "", true);
    }
    return null;
  }
}