import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.Mockito;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

public class ProcessCommonJSModulesTest {

  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(mock(AbstractCompiler.class), "test");
    String filename = "test.js";
    String expected = "module$test";
    String actual = pcm.guessCJSModuleName(filename);
    assertEquals(expected, actual);
  }

  @Test
  public void testToModuleName() {
    String filename = "test.js";
    String expected = "module$test";
    String actual = ProcessCommonJSModules.toModuleName(filename);
    assertEquals(expected, actual);
  }

  @Test
  public void testToModuleNameWithRelativeAddressing() {
    String requiredFilename = "./module.js";
    String currentFilename = "test.js";
    String expected = "module$test$module";
    String actual = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals(expected, actual);
  }

  @Test
  public void testProcess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "test");
    Node externs = IR.root();
    Node root = IR.root();
    pcm.process(externs, root);
    Mockito.verify(compiler).reportCodeChange();
  }

  @Test
  public void testVisitRequireCall() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "test");
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node require = IR.call(IR.name("require"), IR.string("module"));
    Node parent = IR.root();
    pcm.visitRequireCall(traversal, require, parent);
    String expected = "module$require";
    String actual = require.getFirstChild().getString();
    assertEquals(expected, actual);
  }

  @Test
  public void testVisitScript() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "test");
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node script = IR.script();
    pcm.visitScript(traversal, script);
    String expected = "module$test";
    String actual = script.getFirstChild().getString();
    assertEquals(expected, actual);
    Mockito.verify(compiler).reportCodeChange();
  }

  @Test
  public void testEmitOptionalModuleExportsOverride() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "test");
    Node script = IR.script();
    String moduleName = "module$test";
    pcm.emitOptionalModuleExportsOverride(script, moduleName);
    int expected = 1;
    int actual = script.getChildCount();
    assertEquals(expected, actual);
  }

  @Test
  public void testVisitModuleExports() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "test");
    Node prop = IR.name("module.exports");
    pcm.visitModuleExports(prop);
    String expected = "module$test";
    String actual = prop.getFirstChild().getString();
    assertEquals(expected, actual);
  }

  @Test
  public void testSuffixVarsCallback() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ProcessCommonJSModules.SuffixVarsCallback suffixCallback = new ProcessCommonJSModules.SuffixVarsCallback("test");
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node nameNode = IR.name("varName");
    Node scopeNode = IR.root();
    List<Var> vars = new ArrayList<>();
    vars.add(new Var(nameNode));
    Scope scope = new Scope(traversal, scopeNode);
    scope.getVars().put("varName", vars);
    Mockito.when(traversal.getScope()).thenReturn(scope);
    suffixCallback.visit(traversal, nameNode, scopeNode);
    String expected = "varName$$test";
    String actual = nameNode.getString();
    assertEquals(expected, actual);
  }
}