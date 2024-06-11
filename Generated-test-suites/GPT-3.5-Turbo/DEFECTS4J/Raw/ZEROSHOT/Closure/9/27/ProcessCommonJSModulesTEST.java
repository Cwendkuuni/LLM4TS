import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

  @Before
  public void setUp() {

  }

  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
    String result = processCommonJSModules.guessCJSModuleName("example.js");
    assertEquals("module$example", result);
  }

  @Test
  public void testToModuleName() {
    String result = ProcessCommonJSModules.toModuleName("example.js");
    assertEquals("module$example", result);
  }

  @Test
  public void testToModuleNameWithRelativeAddressing() {
    String result = ProcessCommonJSModules.toModuleName("./example.js", "folder/module.js");
    assertEquals("module$folder$example", result);
  }

  @Test
  public void testProcess() {
    ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
    processCommonJSModules.process(null, null);
    // assert something
  }

  @Test
  public void testProcessWithFilenamePrefix() {
    ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, ".js/");
    processCommonJSModules.process(null, null);
    // assert something
  }

  @Test
  public void testGuessCJSModuleNameWithFilenamePrefix() {
    ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, ".js/");
    String result = processCommonJSModules.guessCJSModuleName(".js/example.js");
    assertEquals("module$example", result);
  }

  @Test
  public void testVisitRequireCall() {
    ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
    NodeTraversal t = new NodeTraversal();
    Node require = new Node();
    require.addChildToFront(new Node("require"));
    require.addChildToBack(new Node("module.js"));
    Node parent = new Node();
    processCommonJSModules.visitRequireCall(t, require, parent);
    // assert something
  }

  @Test
  public void testVisitRequireCallWithReportDependencies() {
    ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "", true);
    NodeTraversal t = new NodeTraversal();
    Node require = new Node();
    require.addChildToFront(new Node("require"));
    require.addChildToBack(new Node("module.js"));
    Node parent = new Node();
    processCommonJSModules.visitRequireCall(t, require, parent);
    // assert something
  }

  @Test
  public void testVisitModuleExports() {
    ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
    Node prop = new Node();
    prop.addChildToFront(new Node("module"));
    prop.addChildToBack(new Node("exports"));
    processCommonJSModules.visitModuleExports(prop);
    // assert something
  }

  @Test
  public void testSuffixVarsCallback() {
    ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
    SuffixVarsCallback callback = processCommonJSModules.new SuffixVarsCallback("example");
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    callback.visit(t, n, parent);
    // assert something
  }
  
}