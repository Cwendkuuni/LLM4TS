import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceName;

import java.util.Set;
import java.util.regex.Pattern;

public class ProcessCommonJSModulesTest {

  private static final String MODULE_SLASH = "/";
  private static final String MODULE_NAME_SEPARATOR = "$";
  private static final String MODULE_NAME_PREFIX = "module$";

  @org.junit.Before
  public void setUp() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", "test.js", new SourceName("test.js"));
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    Node root = NodeFactory.createRootNode();
    processor.process(input, root);
  }

  @org.junit.Test
  public void testGuessCJSModuleName() {
    String filename = "path/to/module.js";
    String expected = "module$path$to$module";
    assertEquals(expected, ProcessCommonJSModules.toModuleName(filename));
  }

  @org.junit.Test
  public void testGuessCJSModuleNameRelativePath() {
    String filename = "./relative/path/to/module.js";
    String expected = "module$./relative$path$to$module";
    assertEquals(expected, ProcessCommonJSModules.toModuleName(filename));
  }

  @org.junit.Test
  public void testGuessCJSModuleNameWithExtension() {
    String filename = "path/to/module.js";
    String expected = "module$path$to$module";
    assertEquals(expected, ProcessCommonJSModules.toModuleName(filename));
  }

  @org.junit.Test
  public void testGuessCJSModuleNameWithoutExtension() {
    String filename = "path/to/module";
    String expected = "module$path$to$module";
    assertEquals(expected, ProcessCommonJSModules.toModuleName(filename));
  }

  @org.junit.Test
  public void testNormalizeSourceName() {
    String filename = "path/to/module" + MODULE_SLASH;
    String expected = "path/to/module";
    assertEquals(expected, ProcessCommonJSModules.normalizeSourceName(filename));
  }

  @org.junit.Test
  public void testProcessCommonJSModules() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", "test.js", new SourceName("test.js"));
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    Node root = NodeFactory.createRootNode();
    processor.process(input, root);
    assertNull(processor.getModule());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithModule() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", "test.js", new SourceName("test.js"));
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    Node root = NodeFactory.createRootNode();
    JSModule module = mock(JSModule.class);
    when(compiler.getInput()).thenReturn(input);
    when(compiler.reportCodeChange()).thenReturn(null);
    when(compiler.addProvide(anyString())).thenAnswer(invocation -> module);

    processor.process(input, root);
    JSModule actual = processor.getModule();
    assertEquals(module, actual);
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithError() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", "test.js", new SourceName("test.js"));
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    Node root = NodeFactory.createRootNode();
    when(compiler.getInput()).thenReturn(input);
    when(compiler.reportCodeChange()).thenReturn(null);
    when(compiler.addProvide(anyString())).thenThrow(new RuntimeException());

    assertThrows(RuntimeException.class, () -> {
      processor.process(input, root);
    });
  }

  @org.junit.Test
  public void testProcessCommonJSModulesCallback() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", "test.js", new SourceName("test.js"));
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    Node root = NodeFactory.createScriptNode("", null, null);
    NodeTraversal traversal = new NodeTraversal(compiler, root, null);

    processor.process(input, root);

    Node requireCall = root.getFirstChild();
    assertNull(requireCall);

    Node script = root.getChildAtIndex(0);
    String expectedModuleName = "module$test";
    String expectedProvideCall = "goog.provide('" + expectedModuleName + "');";
    String expectedProvideCallPosition = "1:0";
    assertEquals(expectedModuleName, ProcessCommonJSModules.guessCJSModuleName(input.getSourceName().getName()));
    assertEquals(expectedProvideCall, script.getChildAtIndex(0).toString());
    assertEquals(expectedProvideCallPosition, script.getChildAtIndex(0).getSourcePosition().toString());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesCallbackWithRequireCall() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", "test.js", new SourceName("test.js"));
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    Node root = NodeFactory.createScriptNode("require('path/to/module');", null, null);
    NodeTraversal traversal = new NodeTraversal(compiler, root, null);

    processor.process(input, root);

    Node requireCall = root.getFirstChild();
    String expectedModuleName = "module$path$to$module";
    String expectedRequireCallPosition = "1:5";
    String expectedRequireCallArgumentPosition = "1:16";
    String expectedRequireCallArgument = "'path/to/module'";
    String expectedRequireCallArgumentPositionInSource = "3:7";
    String expectedModuleRefPosition = "2:0";
    String expectedModuleRef = "module$path$to$module";
    String expectedProvideCall = "goog.provide('" + expectedModuleName + "');";
    String expectedProvideCallPosition = "3:0";

    assertEquals(expectedModuleName, ProcessCommonJSModules.toModuleName(requireCall.getChildAtIndex(1).getString()));
    assertEquals(expectedModuleName, requireCall.getChildAtIndex(1).getString());
    assertEquals(expectedRequireCallPosition, requireCall.getSourcePosition().toString());
    assertEquals(expectedRequireCallArgument, requireCall.getChildAtIndex(1).getFirstChild().toString());
    assertEquals(expectedRequireCallArgumentPositionInSource, requireCall.getChildAtIndex(1).getChildAtIndex(0).getSourcePosition().toString());
    assertEquals(expectedModuleRef, root.getChildAtIndex(1).toString());
    assertEquals(expectedModuleRefPosition, root.getChildAtIndex(1).getSourcePosition().toString());
    assertEquals(expectedProvideCall, root.getChildAtIndex(2).toString());
    assertEquals(expectedProvideCallPosition, root.getChildAtIndex(2).getSourcePosition().toString());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesCallbackWithScript() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", "test.js", new SourceName("test.js"));
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    Node root = NodeFactory.createScriptNode("var module = module$path$to$module.module$exports;", null, null);
    NodeTraversal traversal = new NodeTraversal(compiler, root, null);

    processor.process(input, root);

    Node moduleNameNode = root.getChildAtIndex(0);
    Node moduleName = moduleNameNode.getChildAtIndex(0);
    Node exportsNode = moduleNameNode.getChildAtIndex(1);
    String expectedModuleName = "module$path$to$module";
    String expectedSuffix = "path$to$module";

    assertEquals(expectedModuleName, moduleName.getString());
    assertEquals(expectedSuffix, ProcessCommonJSModules.normalizeSourceName(input.getSourceName().getName()).substring(1));
    assertEquals("module$exports", exportsNode.getString());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesCallbackWithModuleExports() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", "test.js", new SourceName("test.js"));
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    Node root = NodeFactory.createScriptNode("module.exports = { foo: 1 };", null, null);
    NodeTraversal traversal = new NodeTraversal(compiler, root, null);

    processor.process(input, root);

    Node exportsNode = root.getChildAtIndex(0);
    Node suffixNode = root.getLastChild();
    String expectedSuffix = "test";

    assertEquals("module$exports", exportsNode.getString());
    assertEquals(expectedSuffix, suffixNode.getString());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesCallbackWithModuleExportsAndRequire() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", "test.js", new SourceName("test.js"));
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    Node root = NodeFactory.createScriptNode("module.exports = { foo: 1 }; require('path/to/module');", null, null);
    NodeTraversal traversal = new NodeTraversal(compiler, root, null);

    processor.process(input, root);

    Node exportsNode = root.getChildAtIndex(0);
    Node suffixNode = root.getLastChild();
    Node requireCall = root.getChildAtIndex(1);
    String expectedModuleName = "module$path$to$module";

    assertEquals("module$exports", exportsNode.getString());
    assertEquals(expectedModuleName, ProcessCommonJSModules.toModuleName(requireCall.getChildAtIndex(1).getString()));
  }

  @org.junit.Test
  public void testProcessCommonJSModulesCallbackWithModuleExportsAndRequireWithRelativePath() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", "test.js", new SourceName("test.js"));
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    Node root = NodeFactory.createScriptNode("module.exports = { foo: 1 }; require('./path/to/module');", null, null);
    NodeTraversal traversal = new NodeTraversal(compiler, root, null);

    processor.process(input, root);

    Node exportsNode = root.getChildAtIndex(0);
    Node suffixNode = root.getLastChild();
    Node requireCall = root.getChildAtIndex(1);
    String expectedModuleName = "module$.test$path$to$module";

    assertEquals("module$exports", exportsNode.getString());
    assertEquals(expectedModuleName, ProcessCommonJSModules.toModuleName(requireCall.getChildAtIndex(1).getString()));
  }

  @org.junit.Test
  public void testProcessCommonJSModulesCallbackWithModuleExportsAndRequireWithAbsolutePath() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", "test.js", new SourceName("test.js"));
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    Node root = NodeFactory.createScriptNode("module.exports = { foo: 1 }; require('/path/to/module');", null, null);
    NodeTraversal traversal = new NodeTraversal(compiler, root, null);

    processor.process(input, root);

    Node exportsNode = root.getChildAtIndex(0);
    Node suffixNode = root.getLastChild();
    Node requireCall = root.getChildAtIndex(1);
    String expectedModuleName = "module$/path$to$module";

    assertEquals("module$exports", exportsNode.getString());
    assertEquals(expectedModuleName, ProcessCommonJSModules.toModuleName(requireCall.getChildAtIndex(1).getString()));
  }
}