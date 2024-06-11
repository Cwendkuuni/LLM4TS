package org.apache.commons.cli;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

@RunWith(JUnit4.class)
public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", "test.js", null);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    Node root = Mockito.mock(Node.class);
    pass.process(input, root);

    assertNotNull(pass.getModule());
  }

  @Test
  public void testGuessCJSModuleName() {
    String filename = "test/test.js";
    String expected = "test$test";
    String result = ProcessCommonJSModules.toModuleName(filename);
    assertEquals(expected, result);
  }

  @Test
  public void testGuessCJSModuleNameWithRelativePath() {
    String current = "/path/to/test.js";
    String required = "./sub/subtest.js";
    String expected = "sub$subtest";
    String result = ProcessCommonJSModules.toModuleName(required, current);
    assertEquals(expected, result);
  }

  @Test
  public void testNormalizeSourceName() {
    String filename = "test.js";
    String expected = "test";
    String result = ProcessCommonJSModules.normalizeSourceName(filename);
    assertEquals(expected, result);
  }

  @Test
  public void testVisitRequireCall() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    Node require = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);
    String moduleName = "test";

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();

    Mockito.when(traversal.getInput()).thenReturn(new CompilerInput("test.js", "test.js", null));
    Mockito.when(require.getChildCount()).thenReturn(2);
    Mockito.when(require.getChildAtIndex(1)).thenReturn(Mockito.mock(Node.class));
    Mockito.when(require.getParent()).thenReturn(parent);

    callback.visit(traversal, require, parent);

    Node expectedModuleRef = IR.name(moduleName).srcref(require);
    Node actualModuleRef = Mockito.argThat(arg -> arg.getFirstChild().getQualifiedName()
        .equals(moduleName));

    Mockito.verify(parent).replaceChild(require, expectedModuleRef);
  }

  @Test
  public void testVisitScript() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    Node script = Mockito.mock(Node.class);
    String moduleName = "test";

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();

    Mockito.when(traversal.getInput()).thenReturn(new CompilerInput("test.js", "test.js", null));
    Mockito.when(script.getParent()).thenReturn(Mockito.mock(Node.class));
    Mockito.when(script.getSourceFileName()).thenReturn("test.js");

    callback.visit(traversal, script);

    String expectedModuleName = "test$test";
    String actualModuleName = Mockito.argThat(arg -> arg.getString().equals(expectedModuleName))
        .getFirstChild()
        .getString();

    Mockito.verify(script).addChildToFront(IR.var(IR.name(expectedModuleName), IR.objectlit()));
    Mockito.verify(script).addChildToFront(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
        IR.string(expectedModuleName)));
  }

  @Test
  public void testVisitRequireCallWithReportDependencies() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    Node require = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);
    String moduleName = "test";

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();

    CompilerInput input = new CompilerInput("test.js", "test.js", null);
    Mockito.when(traversal.getInput()).thenReturn(input);

    Mockito.when(require.getChildCount()).thenReturn(2);
    Mockito.when(require.getChildAtIndex(1)).thenReturn(Mockito.mock(Node.class));
    Mockito.when(require.getParent()).thenReturn(parent);

    callback.visit(traversal, require, parent);

    Mockito.verify(input).addRequire(moduleName);
  }

  @Test
  public void testVisitModuleExports() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    Node prop = Mockito.mock(Node.class);

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();

    String moduleName = "test";
    Mockito.when(prop.getParent()).thenReturn(Mockito.mock(Node.class));
    Mockito.when(prop.getChildCount()).thenReturn(2);
    Mockito.when(prop.getChildAtIndex(0)).thenReturn(Mockito.mock(Node.class));
    Mockito.when(prop.getChildAtIndex(1)).thenReturn(Mockito.mock(Node.class));
    Mockito.when(prop.getSourceFileName()).thenReturn("test.js");

    callback.visitModuleExports(prop);

    String expectedModule = "test";
    String actualModule = Mockito.argThat(arg -> arg.getString().equals(expectedModule))
        .getFirstChild()
        .getString();

    String expectedExports = "test$exports";
    String actualExports = Mockito.argThat(arg -> arg.getString().equals(expectedExports))
        .getFirstChild()
        .getString();

    Mockito.verify(prop.getChildAtIndex(0)).putProp(Node.ORIGINALNAME_PROP, "module");
    Mockito.verify(prop.getChildAtIndex(0)).setString(expectedModule);
    Mockito.verify(prop.getChildAtIndex(1)).putProp(Node.ORIGINALNAME_PROP, "exports");
    Mockito.verify(prop.getChildAtIndex(1)).setString(expectedExports);
  }
}