import org.junit.*;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.ProcessCommonJSModules;

public class ProcessCommonJSModulesTest {

  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null);
    assertEquals("module$filename", pcm.guessCJSModuleName("filename"));
  }

  @Test
  public void testToModuleName() {
    assertEquals(
        "module$filename",
        ProcessCommonJSModules.toModuleName("filename")
    );
  }

  @Test
  public void testToModuleNameWithRelativeAddressing() {
    assertEquals(
        "module$dir$nested",
        ProcessCommonJSModules.toModuleName("./dir/nested.js", "current.js")
    );
  }

  @Test
  public void testProcess() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null);
    Node externs = null;
    Node root = null;
    pcm.process(externs, root);
  }

  @Test
  public void testProcessWithFilenamePrefix() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "prefix/");
    Node externs = null;
    Node root = null;
    pcm.process(externs, root);
  }

  @Test
  public void testProcessWithReportDependencies() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null, true);
    Node externs = null;
    Node root = null;
    pcm.process(externs, root);
  }

  @Test
  public void testProcessCommonJsModulesCallback() {
    ProcessCommonJSModules.ProcessCommonJsModulesCallback cb =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    NodeTraversal t = null;
    Node require = null;
    Node parent = null;
    cb.visit(t, require, parent);
  }

  @Test
  public void testVisitRequireCall() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null);
    NodeTraversal t = null;
    Node require = null;
    Node parent = null;
    pcm.new ProcessCommonJsModulesCallback().visitRequireCall(t, require, parent);
  }

  @Test
  public void testVisitScript() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null);
    NodeTraversal t = null;
    Node script = null;
    pcm.new ProcessCommonJsModulesCallback().visitScript(t, script);
  }

  @Test
  public void testEmitOptionalModuleExportsOverride() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null);
    Node script = null;
    String moduleName = null;
    pcm.new ProcessCommonJsModulesCallback().emitOptionalModuleExportsOverride(script, moduleName);
  }

  @Test
  public void testVisitModuleExports() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null);
    Node prop = null;
    pcm.new ProcessCommonJsModulesCallback().visitModuleExports(prop);
  }

  @Test
  public void testSuffixVarsCallback() {
    ProcessCommonJSModules.SuffixVarsCallback cb = 
        new ProcessCommonJSModules.SuffixVarsCallback("suffix");
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    cb.visit(t, n, parent);
  }
  
}