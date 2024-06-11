import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ProcessCommonJSModulesTest {

    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "test/");
        String moduleName = pcm.guessCJSModuleName("test/module.js");
        assertEquals("module$module", moduleName);
    }

    @Test
    public void testToModuleName() {
        String moduleName = ProcessCommonJSModules.toModuleName("test/module.js");
        assertEquals("module$module", moduleName);
    }

    @Test
    public void testToModuleNameWithRelativeAddressing() {
        String moduleName = ProcessCommonJSModules.toModuleName("../test/module.js", "main/module.js");
        assertEquals("module$main$test$module", moduleName);
    }

    @Test
    public void testProcessCommonJSModulesConstructor() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "test/");
        assertNotNull(pcm);
    }

    @Test
    public void testProcessCommonJSModulesProcess() {
        // Test process method by passing empty externs and root nodes.
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "test/");
        pcm.process(null, null);
        // No exceptions expected, test passes.
        assertTrue(true);
    }

    @Test
    public void testSuffixVarsCallback() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "test/");
        ProcessCommonJSModules.SuffixVarsCallback callback = pcm.new SuffixVarsCallback("module");
        // Test with a global variable.
        String varName = "myVar";
        Node varNode = new Node(varName);
        NodeTraversal t = null; // Dummy NodeTraversal for testing purpose
        callback.visit(t, varNode, null);
        assertEquals(varName + "$$module", varNode.getString());
    }
}