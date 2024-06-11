import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.ProcessCommonJsModulesCallback;
import com.google.javascript.jscomp.SuffixVarsCallback;
import com.google.javascript.jscomp.JSModule;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private static final String TEST_MODULE_NAME = "testModule";
  private static final String TEST_INPUT_NAME = "testInput.js";
  private static final String TEST_REQUIRED_MODULE_NAME = "requiredModule";
  private static final String TEST_CURRENT_MODULE_NAME = "currentModule";

  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException, URISyntaxException {
    // Setup
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput(new ByteArrayInputStream(
        "/*input.js*/".getBytes()), TEST_INPUT_NAME);
    when(compiler.getErrorReporter()).thenReturn(mock(CompilerErrorReporter.class));

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler,
        ".");
    Node rootNode = mock(Node.class);

    // Test case 1: Simple CommonJS module
    when(rootNode.getChildCount()).thenReturn(1);
    Node scriptNode = mock(Node.class);
    when(scriptNode.isScript()).thenReturn(true);
    when(scriptNode.getString()).thenReturn("/*module testModule {" +
        "  exports = {foo: 1};" +
        "}*/");
    when(rootNode.getChildAtIndex(0)).thenReturn(scriptNode);

    processor.process(input, rootNode);

    JSModule module = processor.getModule();
    assertSame(module, module);
    assertEquals(TEST_MODULE_NAME, module.getName());
    assertEquals(1, module.getExportedVariables().size());
    assertEquals("foo", module.getExportedVariables().keySet().iterator().next());
    assertEquals(1, module.getExportedVariables().get("foo"));

    // Test case 2: CommonJS module with require
    when(rootNode.getChildCount()).thenReturn(2);
    Node requireNode = mock(Node.class);
    when(requireNode.isCall()).thenReturn(true);
    when(requireNode.getChildCount()).thenReturn(2);
    when(requireNode.getChildAtIndex(0)).thenReturn(IR.name("require"));
    when(requireNode.getChildAtIndex(1)).thenReturn(IR.string(TEST_REQUIRED_MODULE_NAME));

    Node scriptNode2 = mock(Node.class);
    when(scriptNode2.isScript()).thenReturn(true);

    when(rootNode.getChildAtIndex(1)).thenReturn(requireNode);
    when(rootNode.getChildAtIndex(0)).thenReturn(scriptNode2);

    processor.process(input, rootNode);

    // Test case 3: CommonJS module with require and exports
    when(scriptNode2.getString()).thenReturn("/*module testModule {" +
        "  require('" + TEST_REQUIRED_MODULE_NAME + "');" +
        "  exports = {foo: 1};" +
        "}*/");

    processor.process(input, rootNode);

    JSModule requiredModule = processor.getModule();
    assertNull(requiredModule);

    module = processor.getModule();
    assertEquals(TEST_MODULE_NAME, module.getName());
    assertEquals(1, module.getExportedVariables().size());
    assertEquals("foo", module.getExportedVariables().keySet().iterator().next());
    assertEquals(1, module.getExportedVariables().get("foo"));

    // Test case 4: CommonJS module with multiple require calls
    when(scriptNode2.getString()).thenReturn("/*module testModule {" +
        "  require('" + TEST_REQUIRED_MODULE_NAME + "');" +
        "  require('" + TEST_REQUIRED_MODULE_NAME + "');" +
        "  exports = {foo: 1};" +
        "}*/");

    processor.process(input, rootNode);

    requiredModule = processor.getModule(TEST_REQUIRED_MODULE_NAME);
    assertSame(requiredModule, requiredModule);
    assertEquals(TEST_REQUIRED_MODULE_NAME, requiredModule.getName());
    assertNull(requiredModule.getExportedVariables());

    module = processor.getModule();
    assertEquals(TEST_MODULE_NAME, module.getName());
    assertEquals(1, module.getExportedVariables().size());
    assertEquals("foo", module.getExportedVariables().keySet().iterator().next());
    assertEquals(1, module.getExportedVariables().get("foo"));

    // Test case 5: CommonJS module with allowMultipleEntries option false
    input.setOption("allowMultipleEntries", false);

    when(scriptNode2.getString()).thenReturn("/*module testModule {" +
        "  require('" + TEST_REQUIRED_MODULE_NAME + "');" +
        "  require('" + TEST_REQUIRED_MODULE_NAME + "');" +
        "  exports = {foo: 1};" +
        "}*/");

    processor.process(input, rootNode);

    module = processor.getModule();
    assertNull(module);

    // Test case 6: CommonJS module with moduleName parameter
    processor = new ProcessCommonJSModules(compiler, ".", TEST_MODULE_NAME);

    when(scriptNode2.getString()).thenReturn("/*module testModule {" +
        "  exports = {foo: 1};" +
        "}*/");

    processor.process(input, rootNode);

    module = processor.getModule();
    assertEquals(TEST_MODULE_NAME, module.getName());
    assertEquals(1, module.getExportedVariables().size());
    assertEquals("foo", module.getExportedVariables().keySet().iterator().next());
    assertEquals(1, module.getExportedVariables().get("foo"));

    // Test case 7: CommonJS module with currentFileName parameter
    input.setSourceName(TEST_CURRENT_MODULE_NAME);

    when(scriptNode2.getString()).thenReturn("/*module testModule {" +
        "  exports = {foo: 1};" +
        "}*/");

    processor.process(input, rootNode);

    module = processor.getModule();
    assertEquals(TEST_MODULE_NAME, module.getName());
    assertEquals(1, module.getExportedVariables().size());
    assertEquals("foo", module.getExportedVariables().keySet().iterator().next());
    assertEquals(1, module.getExportedVariables().get("foo"));

    // Test case 8: CommonJS module with require and currentFileName parameter
    when(scriptNode2.getString()).thenReturn("/*module testModule {" +
        "  require('" + TEST_REQUIRED_MODULE_NAME + "');" +
        "  exports = {foo: 1};" +
        "}*/");

    processor.process(input, rootNode);

    requiredModule = processor.getModule(TEST_REQUIRED_MODULE_NAME);
    assertNull(requiredModule);

    module = processor.getModule();
    assertEquals(TEST_MODULE_NAME, module.getName());
    assertEquals(1, module.getExportedVariables().size());
    assertEquals("foo", module.getExportedVariables().keySet().iterator().next());
    assertEquals(1, module.getExportedVariables().get("foo"));
  }
}