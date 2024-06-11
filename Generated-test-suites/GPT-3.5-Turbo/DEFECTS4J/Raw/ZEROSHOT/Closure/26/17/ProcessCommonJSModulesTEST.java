import static org.junit.Assert.*;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

    ProcessCommonJSModules processCommonJSModules;

    @Test
    public void testGuessCJSModuleName() {
        String filename = "test.js";
        String expected = "module$test";
        String result = processCommonJSModules.guessCJSModuleName(filename);
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
    public void testToModuleNameWithRelativeAddressing() {
        String requiredFilename = "../test.js";
        String currentFilename = "src/test.js";
        String expected = "module$test";
        String result = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals(expected, result);
    }

    @Test
    public void testProcess() {
        Node externs = new Node();
        Node root = new Node();
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, filenamePrefix);
        processCommonJSModules.process(externs, root);
    }

    @Test
    public void testProcessWithReportDependencies() {
        Node externs = new Node();
        Node root = new Node();
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, filenamePrefix, true);
        processCommonJSModules.process(externs, root);
    }

    @Test
    public void testGetModule() {
        JSModule expected = new JSModule("testModule");
        processCommonJSModules.module = expected;
        JSModule result = processCommonJSModules.getModule();
        assertEquals(expected, result);
    }

    @Test
    public void testPostOrderCallback() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visit(t, n, parent);
    }

    @Test
    public void testVisitRequireCall() {
        NodeTraversal t = new NodeTraversal();
        Node require = new Node();
        Node parent = new Node();
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visitRequireCall(t, require, parent);
    }

    @Test
    public void testVisitScript() {
        NodeTraversal t = new NodeTraversal();
        Node script = new Node();
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visitScript(t, script);
    }

    @Test
    public void testEmitOptionalModuleExportsOverride() {
        Node script = new Node();
        String moduleName = "testModule";
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.emitOptionalModuleExportsOverride(script, moduleName);
    }

    @Test
    public void testVisitModuleExports() {
        Node prop = new Node();
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visitModuleExports(prop);
    }

    @Test
    public void testSuffixVarsCallback() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        String suffix = "testSuffix";
        ProcessCommonJSModules.SuffixVarsCallback callback = processCommonJSModules.new SuffixVarsCallback(suffix);
        callback.visit(t, n, parent);
    }
}