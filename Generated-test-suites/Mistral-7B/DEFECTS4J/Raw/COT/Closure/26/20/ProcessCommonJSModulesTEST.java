package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;
  private ProcessCommonJSModules processor;

  @Before
  public void setUp() throws Exception {
    compiler = new Compiler();
    nodeFactory = compiler.getFactory();
    compiler.setOption(CompilerOptions.LanguageMode.ECMASCRIPT6);
    processor = new ProcessCommonJSModules(compiler, "");
  }

  @Test
  public void testProcessSimpleCommonJSModule() throws IOException {
    String source = "module.exports = 1;";
    Node root = nodeFactory.parseTree(source);
    processor.process(null, root);
    Node result = compiler.getIntermediateRepresentation();

    // Check the rewritten code
    Node exports = result.getFirstChild();
    Node module = result.getChildAtIndex(1);
    assertEquals("exports", exports.getString());
    assertEquals("module", module.getString());
    Node value = exports.getFirstChild();
    assertEquals(Node.NUMBER_LITERAL, value.getType());
    assertEquals(1, value.getNumber().intValue());
  }

  @Test
  public void testProcessCommonJSModuleWithRequire() throws IOException {
    String source1 = "module.exports = require('./a.js');";
    Node root1 = nodeFactory.parseTree(source1);
    processor.process(null, root1);
    Node result1 = compiler.getIntermediateRepresentation();

    String source2 = "// a.js\nmodule.exports = 1;";
    Node root2 = nodeFactory.parseTree(source2);
    Node module2 = NodeTraversal.traverse(compiler, root2, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isScript()) {
          assertEquals("a.js", n.getSourceFileName());
          return;
        }
        if (n.isCall() && n.getChildCount() == 2 &&
            "require".equals(n.getFirstChild().getQualifiedName())) {
          JSModule module = processor.getModule();
          assertEquals("a.js", module.getModuleName());
        }
      }
    }).getChildAtIndex(0);

    // Check the rewritten code
    Node exports1 = result1.getFirstChild();
    Node requireCall = exports1.getFirstChild();
    Node moduleCall = requireCall.getFirstChild();
    assertEquals("goog.require", moduleCall.getQualifiedName());
    Node moduleArg = requireCall.getChildAtIndex(1);
    assertEquals("a", moduleArg.getString());
    Node module = result1.getChildAtIndex(1);
    assertEquals("a", module.getString());
    Node value = module.getFirstChild();
    assertEquals(Node.NUMBER_LITERAL, value.getType());
    assertEquals(1, value.getNumber().intValue());
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJSModuleWithRelativeRequire() throws IOException {
    String source1 = "module.exports = require('./a/b.js');";
    Node root1 = nodeFactory.parseTree(source1);
    processor.process(null, root1);

    String source2 = "// a/b.js\nmodule.exports = 1;";
    Node root2 = nodeFactory.parseTree(source2);
    Node module2 = NodeTraversal.traverse(compiler, root2, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isScript()) {
          assertEquals("a/b.js", n.getSourceFileName());
          return;
        }
        if (n.isCall() && n.getChildCount() == 2 &&
            "require".equals(n.getFirstChild().getQualifiedName())) {
          JSModule module = processor.getModule();
          assertEquals("a/b.js", module.getModuleName());
        }
      }
    }).getChildAtIndex(0);

    try {
      processor.process(null, root1);
      fail();
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @Test
  public void testProcessCommonJSModuleWithMultipleRequire() throws IOException {
    String source = "module.exports = require('./a.js'), require('./b.js');";
    Node root = nodeFactory.parseTree(source);
    processor.process(null, root);
    Node result = compiler.getIntermediateRepresentation();

    // Check the rewritten code
    Node exports = result.getFirstChild();
    Node requireCalls = exports.getChildren();
    assertEquals(2, requireCalls.getLength());

    String sourceA = "// a.js\nmodule.exports = 1;";
    Node rootA = nodeFactory.parseTree(sourceA);
    Node moduleA = NodeTraversal.traverse(compiler, rootA, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isScript()) {
          return;
        }
        if (n.isCall() && n.getChildCount() == 2 &&
            "require".equals(n.getFirstChild().getQualifiedName())) {
          JSModule module = processor.getModule();
          assertEquals("a.js", module.getModuleName());
        }
      }
    }).getChildAtIndex(0);

    String sourceB = "// b.js\nmodule.exports = 2;";
    Node rootB = nodeFactory.parseTree(sourceB);
    Node moduleB = NodeTraversal.traverse(compiler, rootB, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isScript()) {
          return;
        }
        if (n.isCall() && n.getChildCount() == 2 &&
            "require".equals(n.getFirstChild().getQualifiedName())) {
          JSModule module = processor.getModule();
          assertEquals("b.js", module.getModuleName());
        }
      }
    }).getChildAtIndex(0);

    for (int i = 0; i < requireCalls.getLength(); i++) {
      Node requireCall = requireCalls.getChildAtIndex(i);
      Node moduleCall = requireCall.getFirstChild();
      Node moduleArg = requireCall.getChildAtIndex(1);
      if (i == 0) {
        assertEquals("goog.require", moduleCall.getQualifiedName());
        assertEquals("a", moduleArg.getString());
        Node moduleARef = NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
          @Override
          public void visit(NodeTraversal t, Node n, Node parent) {
            if (n.isName() && n.getString().equals("a")) {
              return;
            }
          }
        }).getChildAtIndex(0);
        assertEquals(moduleARef, requireCall.getParent());
      } else {
        assertEquals("goog.require", moduleCall.getQualifiedName());
        assertEquals("b", moduleArg.getString());
        Node moduleBRef = NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
          @Override
          public void visit(NodeTraversal t, Node n, Node parent) {
            if (n.isName() && n.getString().equals("b")) {
              return;
            }
          }
        }).getChildAtIndex(0);
        assertEquals(moduleBRef, requireCall.getParent());
      }
    }

    Node module = result.getChildAtIndex(1);
    assertEquals("a, b", module.getString());
    Set<String> exports = Sets.newHashSet();
    NodeTraversal.traverse(compiler, module, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isName()) {
          exports.add(n.getString());
        }
      }
    });
    assertEquals(2, exports.size());
    assertTrue(exports.containsAll(Sets.newHashSet("a", "b")));
  }

  @Test
  public void testProcessCommonJSModuleWithModuleExports() throws IOException {
    String source = "module.exports = { a: 1, b: 2 };";
    Node root = nodeFactory.parseTree(source);
    processor.process(null, root);
    Node result = compiler.getIntermediateRepresentation();

    // Check the rewritten code
    Node exports = result.getFirstChild();
    assertEquals("exports", exports.getString());
    Node module = result.getChildAtIndex(1);
    assertEquals("Object", module.getString());
    Node properties = module.getFirstChild();
    assertEquals("Object.defineProperties", properties.getQualifiedName());
    Node params = properties.getFirstChild();
    assertEquals("Object", params.getType());
    Node target = params.getChildAtIndex(0);
    assertEquals("Object.prototype.hasOwnProperty", target.getQualifiedName());
    Node props = params.getChildAtIndex(1);
    Node propList = props.getFirstChild();
    assertEquals("Array.prototype.slice", propList.getQualifiedName());
    Node arguments = propList.getChildAtIndex(0);
    assertEquals(2, arguments.getLength());
    Node a = arguments.getChildAtIndex(0);
    assertEquals("a", a.getString());
    Node valueA = arguments.getChildAtIndex(1);
    assertEquals("1", valueA.getString());
    Node b = arguments.getChildAtIndex(1 + 1);
    assertEquals("b", b.getString());
    Node valueB = arguments.getChildAtIndex(1 + 1 + 1);
    assertEquals("2", valueB.getString());
  }

  @Test
  public void testProcessCommonJSModuleWithProvide() throws IOException {
    String source = "goog.provide('my.module');\nmodule.exports = 1;";
    Node root = nodeFactory.parseTree(source);
    processor.process(null, root);
    Node result = compiler.getIntermediateRepresentation();

    // Check the rewritten code
    Node provides = result.getFirstChild();
    assertEquals("my.module", provides.getString());
    Node module = result.getChildAtIndex(1);
    assertEquals("exports", module.getString());
    Node value = module.getFirstChild();
    assertEquals(Node.NUMBER_LITERAL, value.getType());
    assertEquals(1, value.getNumber().intValue());
  }
}