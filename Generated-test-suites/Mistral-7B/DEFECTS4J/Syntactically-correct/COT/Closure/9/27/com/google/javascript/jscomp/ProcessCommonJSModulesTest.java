import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceName;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput(new SourceName("test.js"), "test.js");
    String filenamePrefix = "test/";

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, filenamePrefix);

    Node root = NodeFactory.createScriptlet("", new Node[0]);
    pass.process(input, root);

    // Check that the expected code changes have been made
    Node moduleNameNode = root.findFirstChildByName("module");
    assertEquals("test$", moduleNameNode.getString());

    Node exportsNode = root.findFirstChildByName("exports$");
    assertNull(exportsNode);

    // Check that the JSModule object has been created
    JSModule module = pass.getModule();
    assertEquals("test", module.getName());
  }

  @Test
  public void testProcessRequireCall() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput(new SourceName("test.js"), "test.js");
    String filenamePrefix = "test/";

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, filenamePrefix, false);

    Node requireCall = NodeFactory.createCall("require", new Node[]{"module$test"});
    Node root = NodeFactory.createScriptlet("", new Node[0]);

    // Perform the code change
    pass.processRequireCall(mock(NodeTraversal.class), requireCall, root);

    // Check that the expected code changes have been made
    Node requireNode = root.findFirstChildByName("goog.require");
    assertEquals("module$test", requireNode.getChildAtIndex(0).getString());

    Node requireCallResult = root.findFirstChildByName("require");
    assertNull(requireCallResult);
  }

  @Test(expected = RuntimeException.class)
  public void testProcessRequireCallWithInvalidURI() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput(new SourceName("test.js"), "test.js");
    String filenamePrefix = "test/";

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, filenamePrefix, false);

    Node requireCall = NodeFactory.createCall("require", new Node[]{"../invalidURI.js"});
    Node root = NodeFactory.createScriptlet("", new Node[0]);

    // Perform the code change
    pass.processRequireCall(mock(NodeTraversal.class), requireCall, root);
  }

  @Test
  public void testProcessScriptNode() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerPassManager passManager = mock(CompilerPassManager.class);
    CompilerInput input = new CompilerInput(new SourceName("test.js"), "test.js");
    String filenamePrefix = "test/";

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, filenamePrefix);
    pass.setPassManager(passManager);

    Node scriptNode = NodeFactory.createScriptlet("", new Node[0]);
    scriptNode.setSourceFileName("test.js");

    // Perform the code change
    pass.visitScript(mock(NodeTraversal.class), scriptNode);

    // Check that the expected code changes have been made
    Node moduleNameNode = scriptNode.findFirstChildByName("module");
    assertEquals("test", moduleNameNode.getString());

    Node googProvideNode = scriptNode.findFirstChildByName("goog.provide");
    assertEquals("test", googProvideNode.getChildAtIndex(0).getString());

    Node moduleExportsNode = scriptNode.findFirstChildByName("module$exports");
    assertNull(moduleExportsNode);

    Node exportsNode = scriptNode.findFirstChildByName("exports");
    assertNull(exportsNode);

    Node rewrittenCodeNode = scriptNode.getLastChild();
    assertEquals("goog.provide('test');\n" +
        "goog.require('module$test');\n" +
        "// Rewritten code goes here\n", rewrittenCodeNode.getString());
  }

  @Test
  public void testProcessModuleExports() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerPassManager passManager = mock(CompilerPassManager.class);
    CompilerInput input = new CompilerInput(new SourceName("test.js"), "test.js");
    String filenamePrefix = "test/";

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, filenamePrefix);
    pass.setPassManager(passManager);

    Node moduleNode = NodeFactory.createName("module");
    Node exportsNode = NodeFactory.createName("exports");
    Node scriptNode = NodeFactory.createScriptlet("", new Node[] {moduleNode, exportsNode});
    scriptNode.setSourceFileName("test.js");

    // Perform the code change
    pass.visitModuleExports(moduleNode);

    // Check that the expected code changes have been made
    Node ifNode = scriptNode.findFirstChildByName("if");
    assertEquals("if (module.module$exports) { module = module.module$exports; }", ifNode.getChildAtIndex(0).getString());
  }
}