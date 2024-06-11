import org.junit.Test;

public class ProcessCommonJSModulesTest {

  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
    assertEquals("module$filename", processCommonJSModules.guessCJSModuleName("filename"));
  }

  @Test
  public void testToModuleName() {
    assertEquals("module$filename", ProcessCommonJSModules.toModuleName("filename"));
  }

  @Test
  public void testToModuleNameWithRelativeAddressing() {
    assertEquals("module$folder$filename", ProcessCommonJSModules.toModuleName("./folder/filename", "currentFilename"));
  }

  @Test
  public void testProcess() {
    ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
    processCommonJSModules.process(null, null);
  }

  @Test
  public void testVisitRequireCall() {
    ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
    NodeTraversal t = null;
    Node require = null;
    Node parent = null;
    processCommonJSModules.visitRequireCall(t, require, parent);
  }

  @Test
  public void testVisitScript() {
    ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
    NodeTraversal t = null;
    Node script = null;
    processCommonJSModules.visitScript(t, script);
  }

  @Test
  public void testEmitOptionalModuleExportsOverride() {
    ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
    Node script = null;
    String moduleName = "module";
    processCommonJSModules.emitOptionalModuleExportsOverride(script, moduleName);
  }

  @Test
  public void testVisitModuleExports() {
    ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
    Node prop = null;
    processCommonJSModules.visitModuleExports(prop);
  }

  @Test
  public void testSuffixVarsCallback() {
    ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
    String suffix = "module";
    SuffixVarsCallback suffixVarsCallback = processCommonJSModules.new SuffixVarsCallback(suffix);
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    suffixVarsCallback.visit(t, n, parent);
  }
  
}