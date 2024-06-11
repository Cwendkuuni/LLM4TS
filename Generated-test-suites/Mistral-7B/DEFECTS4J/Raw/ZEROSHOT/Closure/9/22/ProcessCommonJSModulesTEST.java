package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Processor;
import com.google.javascript.jscomp.test.TestSourceFile;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import java.util.Set;

public class ProcessCommonJSModulesTest extends CompilerPassTestBase {

  private final NodeFactory nodeFactory = processor.getFactory();

  @Override
  protected CompilerPass getPass() {
    return new ProcessCommonJSModules(processor, ".", true);
  }

  @Override
  protected CompilerPassManager getPassManager() {
    return new CompilerPassManager(processor, getPass());
  }

  @Override
  protected CompilerOptions getOptions() {
    CompilerOptions options = super.getOptions();
    options.setWarningLevel("JS_DEAD_CODE", CompilerOptions.WarningLevel.ERROR);
    return options;
  }

  @org.junit.Test
  public void processCommonJSModulesTest() throws Exception {
    String source =
        "// src/A.js\n"
            + "goog.provide('A');\n"
            + "goog.require('B');\n"
            + "exports.x = 1;\n"
            + "\n"
            + "// src/B.js\n"
            + "goog.provide('B');\n"
            + "goog.require('C');\n"
            + "exports.y = 2;\n"
            + "\n"
            + "// src/C.js\n"
            + "goog.provide('C');\n"
            + "exports.z = 3;\n";

    TestSourceFile inputFile = new TestSourceFile("src/A.js", source);
    Node root = process(inputFile.getSourceCode());

    Node module = getPass().getModule();
    assertNotNull(module);
    assertEquals("A", module.getName());
    assertEquals("A", root.getFirstChild().getString());

    Node requireCallNode = root.getChildAtIndex(1);
    Node requireFunctionCall = requireCallNode.getChildAtIndex(0);
    Node requireModuleName = requireFunctionCall.getChildAtIndex(0);
    assertEquals("B", requireModuleName.getString());

    Node exportsVar = root.getChildAtIndex(3);
    assertEquals("exports", exportsVar.getName());
    Node exportsObject = exportsVar.getFirstChild();
    assertEquals(1, exportsObject.getChildCount());
    Node exportsX = exportsObject.getChildAtIndex(0);
    assertEquals("x", exportsX.getName());
    assertEquals(1, exportsX.getNumber());

    requireCallNode = root.getChildAtIndex(4);
    requireFunctionCall = requireCallNode.getChildAtIndex(0);
    requireModuleName = requireFunctionCall.getChildAtIndex(0);
    assertEquals("C", requireModuleName.getString());

    JSModule moduleB = module.getChildAtIndex(1);
    assertNotNull(moduleB);
    assertEquals("B", moduleB.getName());

    JSModule moduleC = moduleB.getChildAtIndex(1);
    assertNotNull(moduleC);
    assertEquals("C", moduleC.getName());

    Node moduleCExports = moduleC.getChildAtIndex(0);
    assertEquals("C", moduleCExports.getName());
    Node moduleCExportsObject = moduleCExports.getFirstChild();
    assertEquals(1, moduleCExportsObject.getChildCount());
    Node moduleCExportsZ = moduleCExportsObject.getChildAtIndex(0);
    assertEquals("z", moduleCExportsZ.getName());
    assertEquals(3, moduleCExportsZ.getNumber());

    assertEquals(3, getPassManager().getNumCodeChanges());
  }

  /** JSModule class used for testing. */
  private static class JSModule {
    private final String name;
    private final Set<JSModule> children = Sets.newLinkedHashSet();

    JSModule(String name) {
      this.name = name;
    }

    void addAndOverrideModule(CompilerInput ci) {
      for (Node n : ci.getTopLevelNodes()) {
        if (n.isCall() &&
            "goog.provide".equals(n.getChildAtIndex(0).getString())) {
          String moduleName = n.getChildAtIndex(1).getString();
          if (moduleName.equals(name)) {
            replaceWithThis(n);
          }
        }
      }
      for (Node n : ci.getTopLevelNodes()) {
        if (n.isCall() &&
            "goog.require".equals(n.getChildAtIndex(0).getString())) {
          JSModule child = new JSModule(n.getChildAtIndex(1).getString());
          this.children.add(child);
          child.addAndOverrideModule(ci);
        }
      }
    }

    String getName() {
      return name;
    }

    JSModule getChildAtIndex(int index) {
      return children.iterator().next();
    }

    void replaceWithThis(Node node) {
      Node newNode = nodeFactory.newName(name);
      newNode.putProp(Node.ORIGINALNAME_PROP, node.getQualifiedName());
      node.getParent().replaceChild(node, newNode);
    }
  }

  private static Node getCurrentScriptNode(Node n) {
    while (true) {
      if (n.isScript()) {
        return n;
      }
      n = n.getParent();
    }
  }

  @org.junit.Before
  public void setUp() throws Exception {
    super.setUp();
    ProcessCommonJSModules p = mock(ProcessCommonJSModules.class);
    Preconditions.checkArgument(p instanceof CompilerPass,
        "Unexpected type for ProcessCommonJSModules mock.");
    when(p.process(eq(null), eq(null))).then(invocation -> {
      Node root = processor.getFactory().newScript(1);
      invocation.getArgument(0).process(root, null);
      return root;
    });
    when(p.getModule()).thenReturn(null);
    when(p.guessCJSModuleName(eq("src/A.js"))).thenReturn("A");
    when(p.guessCJSModuleName(eq("src/B.js"))).thenReturn("B");
    when(p.guessCJSModuleName(eq("src/C.js"))).thenReturn("C");
    when(p.toModuleName(eq("B"), eq("src/A.js"))).thenReturn("B");
    when(p.toModuleName(eq("C"), eq("src/A.js"))).thenReturn("C");
    when(p.normalizeSourceName(eq("src/A.js"))).thenReturn("src/A.js");
    when(p.getModule()).thenAnswer(invocation -> {
      JSModule module = new JSModule("A");
      process(new TestSourceFile("src/A.js", ""));
      return module;
    });
    processor.addPass(p);
  }

  @org.junit.After
  public void tearDown() throws Exception {
    super.tearDown();
    processor.removePass(getPass());
  }
}