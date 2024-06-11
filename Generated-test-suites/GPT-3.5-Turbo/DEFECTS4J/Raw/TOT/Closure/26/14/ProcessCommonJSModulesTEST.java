import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.rhino.Node;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ProcessCommonJSModulesTest {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private Node externs;

  @Mock
  private Node rootNode;

  @Test
  public void testProcess() {
    ProcessCommonJSModules module = new ProcessCommonJSModules(compiler, ".\\");
    CompilerPass callback = module.new ProcessCommonJsModulesCallback();

    module.process(externs, rootNode);

    verify(compiler).traverse(eq(rootNode), eq(callback));
  }

  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules module = new ProcessCommonJSModules(compiler, ".\\");
    String filename = "test.js";
    String expectedModuleName = "module$test";

    String actualModuleName = module.guessCJSModuleName(filename);

    Assert.assertEquals(expectedModuleName, actualModuleName);
  }

  @Test
  public void testToModuleName() {
    ProcessCommonJSModules module = new ProcessCommonJSModules(compiler, ".\\");
    String filename = "test-file.js";
    String expectedModuleName = "module$test$file";

    String actualModuleName = module.toModuleName(filename);

    Assert.assertEquals(expectedModuleName, actualModuleName);
  }

  @Test
  public void testToModuleNameWithRelativeAddressing() {
    ProcessCommonJSModules module = new ProcessCommonJSModules(compiler, ".\\");
    String requiredFilename = "./my-module.js";
    String currentFilename = "/root/main.js";
    String expectedModuleName = "module$root$my$module";

    String actualModuleName = module.toModuleName(requiredFilename, currentFilename);

    Assert.assertEquals(expectedModuleName, actualModuleName);
  }

  @Test
  public void testToModuleNameWithoutRelativeAddressing() {
    ProcessCommonJSModules module = new ProcessCommonJSModules(compiler, ".\\");
    String requiredFilename = "my-module.js";
    String currentFilename = "/root/main.js";
    String expectedModuleName = "module$my$module";

    String actualModuleName = module.toModuleName(requiredFilename, currentFilename);

    Assert.assertEquals(expectedModuleName, actualModuleName);
  }

  @Test
  public void testNormalizeSourceNameWithPrefix() {
    ProcessCommonJSModules module = new ProcessCommonJSModules(compiler, ".\\");
    String filename = ".\\test.js";
    String expectedNormalizedName = "test.js";

    String actualNormalizedName = module.normalizeSourceName(filename);

    Assert.assertEquals(expectedNormalizedName, actualNormalizedName);
  }

  @Test
  public void testNormalizeSourceNameWithoutPrefix() {
    ProcessCommonJSModules module = new ProcessCommonJSModules(compiler, ".\\");
    String filename = "test.js";
    String expectedNormalizedName = "test.js";

    String actualNormalizedName = module.normalizeSourceName(filename);

    Assert.assertEquals(expectedNormalizedName, actualNormalizedName);
  }

  @Test
  public void testProcessCommonJsModulesCallback() {
    ProcessCommonJSModules module = new ProcessCommonJSModules(compiler, ".\\");
    CompilerPass callback = module.new ProcessCommonJsModulesCallback();

    // Mock nodes for testing

    // Call to require
    Node requireNode = mock(Node.class);
    Node callNode = mock(Node.class);
    Node requireNameNode = mock(Node.class);
    Node requireStringNode = mock(Node.class);
    when(requireNode.isCall()).thenReturn(true);
    when(requireNode.getChildCount()).thenReturn(2);
    when(requireNode.getFirstChild()).thenReturn(callNode);
    when(callNode.getQualifiedName()).thenReturn("require");
    when(requireNode.getChildAtIndex(1)).thenReturn(requireStringNode);
    when(requireStringNode.isString()).thenReturn(true);
    when(requireStringNode.getString()).thenReturn("requiredModule");

    // Script node
    Node scriptNode = mock(Node.class);
    when(scriptNode.isScript()).thenReturn(true);

    // GetProp node for module.exports
    Node moduleExportsNode = mock(Node.class);
    when(moduleExportsNode.isGetProp()).thenReturn(true);
    when(moduleExportsNode.getQualifiedName()).thenReturn("module.exports");

    // Test visiting require call
    callback.visit(mock(NodeTraversal.class), requireNode, null);

    // Verify require call has been visited and modified
    verify(requireNode).getFirstChild();
    verify(requireNode).getChildAtIndex(1);
    verify(requireStringNode).getString();
    verify(callNode).replaceChild(eq(requireNode), any(Node.class));

    // Test visiting script
    callback.visit(mock(NodeTraversal.class), scriptNode, null);

    // Verify script has been visited and modified
    verify(scriptNode, times(2)).addChildToFront(any(Node.class));

    // Test visiting module.exports
    callback.visit(mock(NodeTraversal.class), moduleExportsNode, null);

    // Verify module.exports has been visited and modified
    verify(moduleExportsNode).getChildAtIndex(0);
    verify(moduleExportsNode).getChildAtIndex(1);
    verify(moduleExportsNode).setString("module$exports");

    // Test getCurrentScriptNode method
    callback.getCurrentScriptNode(moduleExportsNode);

    // Verify getCurrentScriptNode returns the script node
    verify(scriptNode, times(2)).isScript();
  }
}