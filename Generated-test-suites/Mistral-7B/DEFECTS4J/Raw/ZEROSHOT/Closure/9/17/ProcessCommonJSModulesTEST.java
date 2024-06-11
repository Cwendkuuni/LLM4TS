package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceName;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private static final String TEST_FILE = "ProcessCommonJSModulesTest.java";

  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException, InterruptedException {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    Compiler compiler = new TestCompiler(options);
    String input = "function main() {" +
        "require('a');" +
        "require('b');" +
        "}" +
        "exports.main = main;" +
        "module.exports = {main: main};";
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    Node externs = NodeFactory.createExterns();
    Node root = NodeFactory.createScriptNode(input, SourceName.getScriptName(TEST_FILE));
    processor.process(externs, root);
    NodeTraversal traversal = NodeTraversal.forTree(compiler, root);
    Node main = traversal.findFunctionByName("main");
    assertNotNull(main);
    assertEquals(3, main.getChildCount());
    Node requireA = main.getChildAtIndex(0);
    Node requireB = main.getChildAtIndex(1);
    Node exports = main.getChildAtIndex(2);
    assertEquals(IR.CALL, requireA.getType());
    assertEquals("require", requireA.getFirstChild().getString());
    assertEquals("a", requireA.getChildAtIndex(1).getString());
    assertEquals(IR.CALL, requireB.getType());
    assertEquals("require", requireB.getFirstChild().getString());
    assertEquals("b", requireB.getChildAtIndex(1).getString());
    assertEquals(IR.OBJECTLIT, exports.getType());
    assertNotNull(exports.getFirstChild());
    Node mainProp = exports.getFirstChild();
    assertEquals("main", mainProp.getString());
    Node mainValue = mainProp.getChildAtIndex(0);
    assertEquals(IR.NAME, mainValue.getType());
    assertEquals("main", mainValue.getString());
    assertEquals(IR.REF, mainValue.getChildAtIndex(0).getType());
    assertEquals("main", mainValue.getChildAtIndex(0).getString());
    Set<String> requiredModules = Sets.newHashSet();
    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getChildCount() == 2 &&
            "require".equals(n.getFirstChild().getQualifiedName())) {
          String moduleName = toModuleName(n.getChildAtIndex(1).getString(),
              root.getSourceName().getInputFile().toURI().toString());
          requiredModules.add(moduleName);
        }
      }
    });
    assertEquals(2, requiredModules.size());
    assertEquals("a", requiredModules.iterator().next());
    assertEquals("b", requiredModules.iterator().next());
  }

  private static String toModuleName(String requiredFilename, String currentFilename) {
    return ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
  }
}