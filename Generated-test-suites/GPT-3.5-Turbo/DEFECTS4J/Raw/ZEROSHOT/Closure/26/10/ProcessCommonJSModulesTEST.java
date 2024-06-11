import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ProcessCommonJSModulesTest {

  private AbstractCompiler compiler;
  private ProcessCommonJSModules pass;
  private Node externs;
  private Node root;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    pass = new ProcessCommonJSModules(compiler, ".");
    externs = IR.root(IR.script());
    root = IR.root(IR.script());
  }

  @Test
  public void testProcess() {
    pass.process(externs, root);
    // TODO: Add assertions
  }

  @Test
  public void testGuessCJSModuleName() {
    String filename = "test.js";
    String expected = "module$test";
    String result = pass.guessCJSModuleName(filename);
    assertEquals(expected, result);
  }

  @Test
  public void testToModuleName() {
    String filename = "test.js";
    String expected = "module$test";
    String result = ProcessCommonJSModules.toModuleName(filename);
    assertEquals(expected, result);
  }

  @Test
  public void testToModuleNameWithCurrentFilename() {
    String requiredFilename = "./test.js";
    String currentFilename = "src/main.js";
    String expected = "module$test";
    String result = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals(expected, result);
  }

  @Test
  public void testVisitRequireCall() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node require = IR.call(IR.name("require"), IR.string("test"));
    Node parent = IR.exprResult(require);
    pass.visitRequireCall(traversal, require, parent);
    // TODO: Add assertions
  }

  @Test
  public void testVisitScript() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node script = IR.script();
    pass.visitScript(traversal, script);
    // TODO: Add assertions
  }

  @Test
  public void testEmitOptionalModuleExportsOverride() {
    Node script = IR.script();
    String moduleName = "module$test";
    pass.emitOptionalModuleExportsOverride(script, moduleName);
    // TODO: Add assertions
  }

  @Test
  public void testVisitModuleExports() {
    Node prop = IR.getprop(IR.name("module"), IR.string("exports"));
    pass.visitModuleExports(prop);
    // TODO: Add assertions
  }

  @Test
  public void testSuffixVarsCallback() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node n = IR.name("test");
    Node parent = IR.exprResult(n);
    pass.new SuffixVarsCallback("suffix").visit(traversal, n, parent);
    // TODO: Add assertions
  }
}