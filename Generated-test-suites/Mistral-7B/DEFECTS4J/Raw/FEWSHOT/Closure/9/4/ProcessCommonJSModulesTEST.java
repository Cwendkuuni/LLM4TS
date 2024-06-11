package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

public class ProcessCommonJSModulesTest {

 private AbstractCompiler compiler;
 private CompilerInput input;
 private Node root;

 @Before
 public void setUp() {
 compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler, "");
 input = new CompilerInput("test.js", "test.js", null, null, options);
 root = Node.root();
 }

 @Test
 public void testProcessCommonJSModules() {
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "", false);
 p.process(input, root);
 NodeTraversal nt = NodeTraversal.forTree(compiler, root);
 assertNotNull(nt.getInput().getProvide());
 assertNotNull(nt.getInput().getRequire());
 }

 @Test
 public void testToModuleName() {
 String fileName = "src/main/java/com/example/module1.js";
 String expected = "module$com_example_module1";
 String result = ProcessCommonJSModules.toModuleName(fileName);
 assertEquals(expected, result);
 }

 @Test
 public void testToModuleNameRelative() {
 String currentFileName = "src/main/java/com/example/main.js";
 String requiredFileName = "module1.js";
 String expected = "module$com_example_module1";
 String result = ProcessCommonJSModules.toModuleName(requiredFileName, currentFileName);
 assertEquals(expected, result);
 }
}