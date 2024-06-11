package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilationUnit;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traverse;
import com.google.javascript.rhino.IR;

/**
 * Test cases for ProcessCommonJSModules class.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    CompilerInput input = Mockito.mock(CompilerInput.class);
    Node root = Mockito.mock(Node.class);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    CompilationUnit cu = Mockito.mock(CompilationUnit.class);
    Mockito.when(input.getCompilationUnit()).thenReturn(cu);
    Mockito.when(cu.getRootNode()).thenReturn(root);

    processor.process(input, root);

    Traverse traverse = new Traverse(compiler, root, new ProcessCommonJsModulesCallback());
    traverse.traverseAll();

    assertNotNull(processor.getModule());
  }

  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(null, "");

    assertEquals("module$myModule", processor.guessCJSModuleName("myModule.js"));
    assertEquals("module$./myModule", processor.guessCJSModuleName("./myModule.js"));
    assertEquals("module$../myModule", processor.guessCJSModuleName("../myModule.js"));
  }

  @Test
  public void testToModuleName() {
    assertEquals("module$myModule", ProcessCommonJSModules.toModuleName("myModule.js"));
    assertEquals("module$./myModule", ProcessCommonJSModules.toModuleName("./myModule.js"));
    assertEquals("module$../myModule", ProcessCommonJSModules.toModuleName("../myModule.js"));
    assertEquals("module$myModule$subModule", ProcessCommonJSModules.toModuleName("myModule/subModule.js"));
    assertEquals("module$myModule$subModule$subSubModule", ProcessCommonJSModules.toModuleName("myModule/subModule/subSubModule.js"));
  }

  @Test
  public void testNormalizeSourceName() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(null, "");

    assertEquals("myModule", processor.normalizeSourceName("./myModule.js"));
    assertEquals("../myModule", processor.normalizeSourceName("../myModule.js"));
  }

  @Test
  public void testProcessCommonJsModulesCallback() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    CompilerInput input = Mockito.mock(CompilerInput.class);
    Node root = Mockito.mock(Node.class);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    CompilationUnit cu = Mockito.mock(CompilationUnit.class);
    Mockito.when(input.getCompilationUnit()).thenReturn(cu);
    Mockito.when(cu.getRootNode()).thenReturn(root);

    ProcessCommonJsModulesCallback callback = new ProcessCommonJsModulesCallback();

    Node requireCall = IR.call(IR.getprop(IR.name("require"), IR.string("myModule")), null).srcref(root);
    root.addChildToFront(requireCall.cloneTree());

    Node script = IR.script("exports = function() { return 42; }").srcref(root);
    root.addChildToBack(script.cloneTree());

    Traverse traverse = new Traverse(compiler, root, callback);
    traverse.traverseAll();

    Node googProvide = callback.provide;
    Node googRequire = callback.require;

    assertEquals(1, callback.requireCount);
    assertEquals("myModule", callback.requireModuleNames.get(0));

    assertNotNull(googProvide);
    assertEquals("myModule", googProvide.getString());

    assertNotNull(googRequire);
    assertEquals("myModule", googRequire.getString());

    Node googRequireCall = IR.call(googRequire, IR.string("myModule")).srcref(root);
    Node scriptBody = script.getChildAtIndex(0);

    assertEquals(1, scriptBody.getChildCount());
    assertEquals(42, ((IR.call) scriptBody.getChildAtIndex(0).getValue().value.intVal()));

    assertEquals("exports", script.getChildAtIndex(0).getString());
    assertEquals("myModule.exports", scriptBody.getString());
  }
}

class ProcessCommonJsModulesCallback extends AbstractPostOrderCallback {
  int requireCount = 0;
  Node require;
  Node provide;
  Set<String> requireModuleNames = Sets.newHashSet();

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    if (n.isCall() && n.getChildCount() == 2 &&
            "require".equals(n.getFirstChild().getString())) {
      require = n;
      requireCount++;
      String moduleName = ProcessCommonJSModules.toModuleName(n.getChildAtIndex(1).getString(), parent.getSourceFileName());
      requireModuleNames.add(moduleName);
    }
  }
}