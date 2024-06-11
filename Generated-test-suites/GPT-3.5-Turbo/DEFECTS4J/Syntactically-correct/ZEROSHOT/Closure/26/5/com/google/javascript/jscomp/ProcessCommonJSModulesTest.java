import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.google.javascript.jscomp.*;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.regex.Pattern;
import static org.junit.Assert.*;

public class ProcessCommonJSModulesTest {
  
  private AbstractCompiler compiler;
  private ProcessCommonJSModules processCommonJSModules;

  @Before
  public void setUp() {
    compiler = Mockito.mock(AbstractCompiler.class);
    processCommonJSModules = new ProcessCommonJSModules(compiler, ".");
  }

  @Test
  public void testGuessCJSModuleName() {
    String filename = "test.js";
    String expected = "module$test";
    String actual = processCommonJSModules.guessCJSModuleName(filename);
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
    String expected = "module$module";
    String actual = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals(expected, actual);
  }

  @Test
  public void testProcess() {
    Node externs = new Node();
    Node root = new Node();
    processCommonJSModules.process(externs, root);
    // assert something
  }

  @Test
  public void testVisitRequireCall() {
    NodeTraversal t = Mockito.mock(NodeTraversal.class);
    Node require = new Node(
        new Node(Token.NAME, "require"),
        new Node(Token.STRING, "module").srcref(1, 1, 1, 1)
    );
    Node parent = new Node(
        new Node(),
        new Node()
    );
    processCommonJSModules.visitRequireCall(t, require, parent);
    // assert something
  }

  @Test
  public void testVisitScript() {
    NodeTraversal t = Mockito.mock(NodeTraversal.class);
    Node script = new Node(
        new Node(Token.EMPTY),
        new Node(Token.SCRIPT)
    );
    processCommonJSModules.visitScript(t, script);
    // assert something
  }

  @Test
  public void testEmitOptionalModuleExportsOverride() {
    Node script = new Node(
        new Node(Token.NAME, "module"),
        new Node(Token.NAME, "exports")
    );
    String moduleName = "module$test";
    processCommonJSModules.emitOptionalModuleExportsOverride(script, moduleName);
    // assert something
  }

  @Test
  public void testVisitModuleExports() {
    Node prop = new Node(
        new Node(Token.NAME, "module"),
        new Node(Token.NAME, "exports")
    );
    processCommonJSModules.visitModuleExports(prop);
    // assert something
  }

  @Test
  public void testSuffixVarsCallback() {
    String suffix = "test";
    NodeTraversal t = Mockito.mock(NodeTraversal.class);
    Node n = new Node();
    Node parent = new Node();
    ProcessCommonJSModules.SuffixVarsCallback callback = 
        new ProcessCommonJSModules.SuffixVarsCallback(suffix);
    callback.visit(t, n, parent);
    // assert something
  }

}