import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

  private ProcessCommonJSModules processCommonJSModules;

  @Before
  public void setUp() {
    processCommonJSModules = new ProcessCommonJSModules(null, "");
  }

  @Test
  public void testGuessCJSModuleName() {
    String expected = "module$filename";
    String actual = processCommonJSModules.guessCJSModuleName("filename");

    assertEquals(expected, actual);
  }

  @Test
  public void testToModuleName() {
    String expected = "module$filename";
    String actual = ProcessCommonJSModules.toModuleName("filename");

    assertEquals(expected, actual);
  }

  @Test
  public void testToModuleNameWithRelativePath() {
    String expected = "module$test$filename";
    String actual = ProcessCommonJSModules.toModuleName("./test/filename", "currentFilename");

    assertEquals(expected, actual);
  }

  @Test
  public void testNormalizeSourceName() {
    String expected = "filename";
    String actual = processCommonJSModules.normalizeSourceName(".\\filename");

    assertEquals(expected, actual);
  }

  @Test
  public void testVisitRequireCall() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node require = IR.call(IR.name("require"), IR.string("module"));
    Node parent = IR.root();
    parent.addChildToFront(require);

    processCommonJSModules.visitRequireCall(t, require, parent);

    Node expected = IR.name("module$module");
    Node actual = parent.getFirstChild();

    assertEquals(expected, actual);
  }

  @Test
  public void testVisitRequireCallWithReportDependencies() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node require = IR.call(IR.name("require"), IR.string("module"));
    Node parent = IR.root();
    parent.addChildToFront(require);

    processCommonJSModules.reportDependencies = true;
    processCommonJSModules.visitRequireCall(t, require, parent);

    assertTrue(t.getInput().getRequires().contains("module$module"));
  }

  @Test
  public void testVisitScript() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node script = IR.script();
    script.setStaticSourceFileName("filename");

    processCommonJSModules.visitScript(t, script);

    Node expected = IR.name("module$filename");
    Node actual = script.getFirstChild();

    assertEquals(expected, actual);
  }

  @Test
  public void testVisitModuleExports() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node prop = IR.getprop(IR.name("module.exports"), IR.string("property"));

    processCommonJSModules.visitModuleExports(prop);

    Node module = prop.getFirstChild();
    Node exports = prop.getLastChild();
    String moduleName = module.getString();
    String exportsName = exports.getString();

    assertEquals("module$filename", moduleName);
    assertEquals("module$exports", exportsName);
  }

  @Test
  public void testSuffixVarsCallback() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node script = IR.script();
    script.setStaticSourceFileName("filename");
    Node var = IR.var(IR.name("var"), IR.number(1));
    script.addChildToBack(var);

    ProcessCommonJSModules.SuffixVarsCallback callback = processCommonJSModules.new SuffixVarsCallback("filename");
    NodeTraversal.traverse(null, script, callback);

    String expected = "var$$filename";
    String actual = var.getFirstChild().getString();

    assertEquals(expected, actual);
  }
}