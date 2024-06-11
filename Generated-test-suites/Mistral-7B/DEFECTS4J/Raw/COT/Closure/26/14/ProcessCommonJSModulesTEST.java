import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", "test.js");
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    Node root = NodeFactory.createRoot();
    NodeTraversal traversal = new NodeTraversal(compiler, root, new ProcessCommonJSModulesCallback());

    traversal.process(input, root);

    JSModule module = pass.getModule();
    assertEquals("test$", module.getName());
  }

  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules pass = new ProcessCommonJSModules(null, "");
    assertEquals("test$", pass.guessCJSModuleName("test.js"));
    assertEquals("test$sub$directory$test2$", pass.guessCJSModuleName("test/sub/directory/test2.js"));
    assertEquals("test$relative$test3$", pass.guessCJSModuleName("./test3.js"));
    assertEquals("test$parent$parent$test4$", pass.guessCJSModuleName("../parent/parent/test4.js"));
  }

  @Test(expected = RuntimeException.class)
  public void testToModuleName_URI_Syntax_Error() {
    ProcessCommonJSModules pass = new ProcessCommonJSModules(null, "");
    String currentFilename = "test.js";
    String requiredFilename = "./sub/directory/test2.js";
    pass.toModuleName(requiredFilename, currentFilename);
  }

  @Test
  public void testNormalizeSourceName() {
    ProcessCommonJSModules pass = new ProcessCommonJSModules(null, "");
    assertEquals("test", pass.normalizeSourceName("test.js"));
    assertEquals("test", pass.normalizeSourceName("test" + File.separator + "test.js"));
    assertEquals("test", pass.normalizeSourceName("test" + File.separator + "test" + File.separator + "test.js"));
  }

  @Test
  public void testProcessCommonJsModulesCallback_Require_Call() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", "test.js");
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    Node root = NodeFactory.createRoot();
    NodeTraversal traversal = new NodeTraversal(compiler, root, new ProcessCommonJsModulesCallback());

    Node requireCall = NodeFactory.createCall(IR.getprop(IR.name("require"), null),
        NodeFactory.createString("test"));
    root.addChild(requireCall);

    traversal.process(input, root);

    Node moduleRef = root.getChildAtIndex(0);
    assertEquals(3, moduleRef.getChildCount());
    assertEquals("test$", moduleRef.getChildAtIndex(0).getString());
    assertEquals("test", moduleRef.getChildAtIndex(1).getString());
    assertEquals("require", moduleRef.getChildAtIndex(2).getString());
  }

  @Test
  public void testProcessCommonJsModulesCallback_Script() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", "test.js");
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    Node root = NodeFactory.createRoot();
    NodeTraversal traversal = new NodeTraversal(compiler, root, new ProcessCommonJsModulesCallback());

    Node script = NodeFactory.createScript("var test = 'hello';");
    root.addChild(script);

    traversal.process(input, root);

    Node module = root.getChildAtIndex(0);
    assertEquals(2, module.getChildCount());
    assertEquals("test", module.getChildAtIndex(0).getString());
    assertEquals("module$", module.getChildAtIndex(1).getString());
    assertEquals("test$", module.getChildAtIndex(1).getFirstChild().getString());
    assertEquals("hello", module.getChildAtIndex(1).getChildAtIndex(0).getString());
  }

  @Test
  public void testProcessCommonJsModulesCallback_Module_Exports() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", "test.js");
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    Node root = NodeFactory.createRoot();
    NodeTraversal traversal = new NodeTraversal(compiler, root, new ProcessCommonJsModulesCallback());

    Node script = NodeFactory.createScript("var test = { exports: 'hello' };");
    root.addChild(script);

    traversal.process(input, root);

    Node module = root.getChildAtIndex(0);
    assertEquals(2, module.getChildCount());
    assertEquals("test", module.getChildAtIndex(0).getString());
    assertEquals("module$", module.getChildAtIndex(1).getString());
    assertEquals("test$", module.getChildAtIndex(1).getFirstChild().getString());
    Node exports = module.getChildAtIndex(1).getChildAtIndex(0);
    assertEquals("hello", exports.getString());
  }

  @Test(expected = AssertionError.class)
  public void testProcessCommonJsModulesCallback_Module_Exports_Missing() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", "test.js");
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    Node root = NodeFactory.createRoot();
    NodeTraversal traversal = new NodeTraversal(compiler, root, new ProcessCommonJsModulesCallback());

    Node script = NodeFactory.createScript("var test = {};");
    root.addChild(script);

    traversal.process(input, root);

    Node module = root.getChildAtIndex(0);
    assertEquals(2, module.getChildCount());
    assertEquals("test", module.getChildAtIndex(0).getString());
    assertEquals("module$", module.getChildAtIndex(1).getString());
    assertNull(module.getChildAtIndex(1).getChildAtIndex(0));
  }

  @Test
  public void testProcessCommonJsModulesCallback_Rename_Globals() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js", "test.js");
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    Node root = NodeFactory.createRoot();
    NodeTraversal traversal = new NodeTraversal(compiler, root, new ProcessCommonJsModulesCallback());

    Node script = NodeFactory.createScript("var test = 'hello'; var exports = 'world';");
    root.addChild(script);

    traversal.process(input, root);

    Node testVar = root.getChildAtIndex(0).getChildAtIndex(0);
    assertEquals("test$$test", testVar.getString());

    Node exportsVar = root.getChildAtIndex(0).getChildAtIndex(1);
    assertEquals("exports$$exports", exportsVar.getString());
  }

}