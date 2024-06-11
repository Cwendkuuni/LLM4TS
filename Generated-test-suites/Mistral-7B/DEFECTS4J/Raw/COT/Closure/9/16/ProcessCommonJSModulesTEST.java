package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.SuffixVarsCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * Test class for ProcessCommonJSModules.
 */
public class ProcessCommonJSModulesTest {

  private AbstractCompiler compiler;
  private CompilerInput input;
  private ProcessCommonJSModules commonJSModules;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    input = mock(CompilerInput.class);
    commonJSModules = new ProcessCommonJSModules(compiler, ".", true);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testGuessCjsModuleName() {
    assertEquals("module$libraryName", commonJSModules.guessCJSModuleName("libraryName/path/to/file.js"));
  }

  @Test
  public void testToModuleName() {
    assertEquals("module$libraryName", ProcessCommonJSModules.toModuleName("libraryName/path/to/file.js"));
    assertEquals("module$libraryName", ProcessCommonJSModules.toModuleName("./libraryName/path/to/file.js", "path/to/currentFile.js"));
  }

  @Test
  public void testNormalizeSourceName() {
    assertEquals("path/to/file", commonJSModules.normalizeSourceName("path/to/file.js"));
  }

  @Test
  public void testProcess() {
    Node node = mock(Node.class);
    CompilerInput ci = new CompilerInput("path/to/input", node, null, null);
    when(compiler.getInput()).thenReturn(ci);
    NodeTraversal traversal = mock(NodeTraversal.class);

    commonJSModules.process(input, node);

    NodeTraversal.traverse(compiler, node, new Node() {});
    verify(compiler).reportCodeChange();
  }

  @Test
  public void testScriptNodeCount() {
    Node script = mock(Node.class);
    Node parent = mock(Node.class);
    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.getInput()).thenReturn(input);

    when(traversal.getCurrentNode()).thenReturn(script);
    when(traversal.getParent()).thenReturn(parent);
    when(traversal.getSourceName()).thenReturn("path/to/script.js");

    Node scriptNode1 = mock(Node.class);
    Node scriptNode2 = mock(Node.class);
    when(script.getChildAtIndex(0)).thenReturn(scriptNode1);
    when(script.getChildAtIndex(1)).thenReturn(scriptNode2);

    commonJSModules.process(input, node);

    NodeTraversal.traverse(compiler, node, new Node() {});

    NodeTraversal.traverse(compiler, node, new ProcessCommonJsModulesCallback() {
      @Override
      public void visitScript(NodeTraversal t, Node script) {
        assertEquals(1, scriptNodeCount.incrementAndGet());
      }
    });
  }

  @Test
  public void testModuleExports() {
    Node moduleExports = IR.getprop(IR.name("module$exports"), IR.objectlit());
    Node script = IR.script();
    script.addChildToBack(moduleExports);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.getInput()).thenReturn(input);

    JSModule module = new JSModule("moduleName");
    commonJSModules.process(input, script);

    NodeTraversal.traverse(compiler, script, new Node() {});

    NodeTraversal.traverse(compiler, script, new ProcessCommonJsModulesCallback() {
      @Override
      public void visitModuleExports(Node prop) {
        assertSame(moduleExports, prop);
      }
    });
  }

  @Test(expected = RuntimeException.class)
  public void testUriSyntaxException() {
    Node requireNode = IR.call(IR.getprop(IR.name("require"), IR.string("./moduleName")), IR.string("moduleName"));
    Node script = IR.script();
    script.addChildToBack(requireNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.getInput()).thenReturn(input);

    commonJSModules.process(input, script);
  }

  @Test
  public void testSuffixVarsCallback() {
    Node node = IR.script();
    Node varName = IR.name("varName");
    node.addChildToBack(varName);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.getInput()).thenReturn(input);

    commonJSModules.process(input, node);

    NodeTraversal.traverse(compiler, node, new SuffixVarsCallback("suffix"));

    assertEquals("varName$$suffix", node.getChildAtIndex(node.getChildCount() - 1).getString());
  }
}