import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import java.util.Set;
import java.util.regex.Pattern;

public class ProcessCommonJSModulesTest {

  private CompilerInput createTestCompilerInput(String filename) {
    return new CompilerInput(filename, null, null, null);
  }

  private AbstractCompiler createTestCompiler() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getSourceFileName()).thenReturn("test.js");
    NodeFactory nodeFactory = mock(NodeFactory.class);
    when(compiler.getNodeFactory()).thenReturn(nodeFactory);
    CompilerPass pass = new ProcessCommonJSModules(compiler, "");
    when(compiler.addPass(Mockito.any(CompilerPass.class))).thenReturn(pass);
    return compiler;
  }

  @org.junit.Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules p = new ProcessCommonJSModules(null, "");
    String filename = "a/b/c/d.js";
    String actual = p.guessCJSModuleName(filename);
    String expected = "module$a$b$c$d";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testNormalizeSourceName() {
    ProcessCommonJSModules p = new ProcessCommonJSModules(null, "");
    String filename = "a" + System.getProperty("file.separator") + "b/c.js";
    String actual = p.normalizeSourceName(filename);
    String expected = "a/b/c.js";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testToModuleName() {
    String actual = ProcessCommonJSModules.toModuleName("module.js");
    String expected = "module$module.js";
    assertEquals(expected, actual);

    actual = ProcessCommonJSModules.toModuleName("./a.js", "module.js");
    expected = "module$a";
    assertEquals(expected, actual);

    actual = ProcessCommonJSModules.toModuleName("../a/b.js", "module.js");
    expected = "module$a/b";
    assertEquals(expected, actual);

    actual = ProcessCommonJSModules.toModuleName("a/b/c/d.js", "module.js");
    expected = "module$a/b/c/d";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testProcess() {
    AbstractCompiler compiler = createTestCompiler();
    CompilerInput input = createTestCompilerInput("test.js");
    ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "");
    p.process(input, null);
    assertNull(input.getProvides());
    assertNull(input.getRequires());
  }

  @org.junit.Test
  public void testProcessWithRequires() {
    AbstractCompiler compiler = createTestCompiler();
    CompilerInput input = createTestCompilerInput("test.js");
    ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "");
    Node root = IR.program();
    input.setRoot(root);
    Node requireCall = IR.call(IR.getprop(IR.name("require"), null),
        IR.string("a/b/c.js"));
    root.addChildToBack(requireCall);
    p.process(input, root);
    Set<String> requires = input.getRequires();
    Set<String> expected = Sets.newHashSet("a/b/c.js");
    assertEquals(expected, requires);
  }

  @org.junit.Test
  public void testProcessWithProvides() {
    AbstractCompiler compiler = createTestCompiler();
    CompilerInput input = createTestCompilerInput("test.js");
    ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "");
    Node scriptNode = IR.script("var a = 1;");
    scriptNode.setSourceFileName("a.js");
    input.setRoot(scriptNode);
    JSModule module = new JSModule("module$a");
    when(compiler.getScope()).thenReturn(new Scope(null));
    when(compiler.getScope().addVar(Mockito.anyString(), Mockito.anyInt()))
        .thenReturn(null);
    when(compiler.addPass(Mockito.any(CompilerPass.class))).thenReturn(p);
    p.process(input, scriptNode);
    Set<String> provides = input.getProvides();
    Set<String> expected = Sets.newHashSet("module$a");
    assertEquals(expected, provides);
  }

  @org.junit.Test
  public void testProcessWithReportDependencies() {
    AbstractCompiler compiler = createTestCompiler();
    CompilerInput input = createTestCompilerInput("test.js");
    ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "test");
    Node root = IR.program();
    input.setRoot(root);
    Node requireCall = IR.call(IR.getprop(IR.name("require"), null),
        IR.string("a/b/c.js"));
    root.addChildToBack(requireCall);
    p.process(input, root);
    Set<String> requires = input.getRequires();
    Set<String> expected = Sets.newHashSet("a/b/c.js");
    assertEquals(expected, requires);
    assertEquals("test$a/b/c.js", p.getModule().getOutputName());
  }

  @org.junit.Test
  public void testProcessWithReportDependenciesAndModuleExports() {
    AbstractCompiler compiler = createTestCompiler();
    CompilerInput input = createTestCompilerInput("test.js");
    ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "test");
    Node root = IR.program();
    input.setRoot(root);
    Node requireCall = IR.call(IR.getprop(IR.name("require"), null),
        IR.string("a/b/c.js"));
    Node exports = IR.exprResult(IR.assign(IR.name("module.exports"), null));
    root.addChildToBack(requireCall);
    root.addChildToBack(exports);
    Node exportsProp = IR.getprop(IR.name("module"), IR.string("exports"));
    root.addChildToBack(exportsProp);
    root.addChildToBack(IR.assign(exports, exportsProp));
    p.process(input, root);
    Set<String> requires = input.getRequires();
    Set<String> expected = Sets.newHashSet("a/b/c.js");
    assertEquals(expected, requires);
    assertEquals("test$a/b/c.js", p.getModule().getOutputName());
  }
}