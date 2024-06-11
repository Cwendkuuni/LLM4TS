import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProcessCommonJSModulesTest {

    private ProcessCommonJSModules processCommonJSModules;

    @Before
    public void setUp() {
        processCommonJSModules = new ProcessCommonJSModules(new AbstractCompiler(), ".");
    }

    @After
    public void tearDown() {
        processCommonJSModules = null;
    }

    @Test
    public void testGuessCJSModuleName() {
        String filename = "test.js";
        String moduleName = processCommonJSModules.guessCJSModuleName(filename);
        assertEquals("module$test", moduleName);
    }

    @Test
    public void testToModuleName_1() {
        String filename = "./test/test.js";
        String moduleName = ProcessCommonJSModules.toModuleName(filename);
        assertEquals("module$test$test", moduleName);
    }

    @Test
    public void testToModuleName_2() {
        String requiredFilename = "./test.js";
        String currentFilename = "./src/test.js";
        String moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals("module$test", moduleName);
    }

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        Node externs = new Node();
        Node root = new Node();
        ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".");
        pass.process(externs, root);
        // Assert something
    }

    @Test
    public void testToModuleName_3() {
        String filename = "./test-file.js";
        String moduleName = ProcessCommonJSModules.toModuleName(filename);
        assertEquals("module$test_file", moduleName);
    }

    @Test
    public void testToModuleName_4() {
        String requiredFilename = "./test.js";
        String currentFilename = "./src/test-file.js";
        String moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals("module$test", moduleName);
    }

    @Test
    public void testNormalizeSourceName() {
        String filename = "./src/test.js";
        String normalizedFilename = processCommonJSModules.normalizeSourceName(filename);
        assertEquals("src/test.js", normalizedFilename);
    }

    @Test
    public void testProcessCommonJsModulesCallback_visitRequireCall() {
        NodeTraversal t = new NodeTraversal();
        Node require = new Node();
        Node parent = new Node();
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visitRequireCall(t, require, parent);
        // Assert something
    }

    @Test
    public void testProcessCommonJsModulesCallback_visitScript() {
        NodeTraversal t = new NodeTraversal();
        Node script = new Node();
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visitScript(t, script);
        // Assert something
    }

    @Test
    public void testProcessCommonJsModulesCallback_emitOptionalModuleExportsOverride() {
        Node script = new Node();
        String moduleName = "test";
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.emitOptionalModuleExportsOverride(script, moduleName);
        // Assert something
    }

    @Test
    public void testProcessCommonJsModulesCallback_visitModuleExports() {
        Node prop = new Node();
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visitModuleExports(prop);
        // Assert something
    }

    @Test
    public void testSuffixVarsCallback_visit() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        String suffix = "test";
        ProcessCommonJSModules.SuffixVarsCallback callback = processCommonJSModules.new SuffixVarsCallback(suffix);
        callback.visit(t, n, parent);
        // Assert something
    }
}