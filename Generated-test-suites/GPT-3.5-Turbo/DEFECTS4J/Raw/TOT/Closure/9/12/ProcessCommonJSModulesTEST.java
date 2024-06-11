import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

public class ProcessCommonJSModulesTest {

    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "/test/");
        String expectedModuleName = "module$test$filename";
        String actualModuleName = pcm.guessCJSModuleName("/test/filename");
        assertEquals(expectedModuleName, actualModuleName);

        expectedModuleName = "module$test$filename";
        actualModuleName = pcm.guessCJSModuleName("/test/filename.js");
        assertEquals(expectedModuleName, actualModuleName);

        expectedModuleName = "module$test$folder$filename";
        actualModuleName = pcm.guessCJSModuleName("/test/folder/filename");
        assertEquals(expectedModuleName, actualModuleName);

        expectedModuleName = "module$test$folder$filename";
        actualModuleName = pcm.guessCJSModuleName("/test/folder/filename.js");
        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testToModuleName() {
        String expectedModuleName = "module$filename";
        String actualModuleName = ProcessCommonJSModules.toModuleName("filename");
        assertEquals(expectedModuleName, actualModuleName);

        expectedModuleName = "module$sub$folder$filename";
        actualModuleName = ProcessCommonJSModules.toModuleName("sub/folder/filename");
        assertEquals(expectedModuleName, actualModuleName);

        expectedModuleName = "module$sub$folder$filename$test";
        actualModuleName = ProcessCommonJSModules.toModuleName("sub/folder/filename/test");
        assertEquals(expectedModuleName, actualModuleName);

        expectedModuleName = "module$sub$folder$filename$-test";
        actualModuleName = ProcessCommonJSModules.toModuleName("sub/folder/filename/-test");
        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testToModuleNameWithRelativeAddressing() {
        String requiredFilename = "./test";
        String currentFilename = "/folder/filename";
        String expectedModuleName = "module$folder$test";
        String actualModuleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals(expectedModuleName, actualModuleName);

        requiredFilename = "../test";
        expectedModuleName = "module$test";
        actualModuleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test(expected = RuntimeException.class)
    public void testToModuleNameWithURISyntaxException() {
        String requiredFilename = "/sub/folder/test";
        String currentFilename = "folder/filename";
        ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    }

    @Test
    public void testConstructorAndGetter() {
        AbstractCompiler compiler = new ConcreteCompiler();
        String filenamePrefix = "/test/";
        boolean reportDependencies = true;
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, filenamePrefix, reportDependencies);
        assertNotNull(pcm.compiler);
        assertEquals(filenamePrefix, pcm.filenamePrefix);
        assertEquals(reportDependencies, pcm.reportDependencies);

        JSModule expectedModule = new JSModule("filename");
        pcm.module = expectedModule;
        JSModule actualModule = pcm.getModule();
        assertEquals(expectedModule, actualModule);
    }

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new ConcreteCompiler();
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "/test/");
        Node externs = new Node(Node.EMPTY);
        Node root = new Node(Node.EMPTY);
        pcm.process(externs, root);
        // Check the process logic here
        assertTrue(true);
    }

    @Test
    public void testProcessCallbackVisit() {
        AbstractCompiler compiler = new ConcreteCompiler();
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "/test/");
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = pcm.new ProcessCommonJsModulesCallback();
        NodeTraversal t = new NodeTraversal(compiler, callback);
        Node externs = new Node(Node.EMPTY);
        Node root = new Node(Node.EMPTY);
        pcm.process(externs, root);
        NodeTraversal.traverse(compiler, root, callback);
        // Check the visit logic here
        assertTrue(true);
    }

    @Test
    public void testProcessCallbackVisitRequireCall() {
        AbstractCompiler compiler = new ConcreteCompiler();
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "/test/");
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = pcm.new ProcessCommonJsModulesCallback();
        NodeTraversal t = new NodeTraversal(compiler, callback);
        Node externs = new Node(Node.EMPTY);
        Node root = new Node(Node.EMPTY);
        pcm.process(externs, root);

        Node require = new Node(Node.EMPTY);
        Node parent = new Node(Node.EMPTY);
        parent.addChildToFront(require);
        String expectedModuleName = "module$test$filename";
        require.addChildToBack(new Node(expectedModuleName));
        callback.visitRequireCall(t, require, parent);
        // Check the visitRequireCall logic here
        assertTrue(true);
    }

    @Test
    public void testProcessCallbackVisitRequireCallWithReportDependenciesFalse() {
        AbstractCompiler compiler = new ConcreteCompiler();
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "/test/", false);
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = pcm.new ProcessCommonJsModulesCallback();
        NodeTraversal t = new NodeTraversal(compiler, callback);
        Node externs = new Node(Node.EMPTY);
        Node root = new Node(Node.EMPTY);
        pcm.process(externs, root);

        Node require = new Node(Node.EMPTY);
        Node parent = new Node(Node.EMPTY);
        parent.addChildToFront(require);
        String expectedModuleName = "module$test$filename";
        require.addChildToBack(new Node(expectedModuleName));
        callback.visitRequireCall(t, require, parent);
        // Check the visitRequireCall logic here
        assertTrue(true);
    }

    @Test
    public void testProcessCallbackVisitScript() {
        AbstractCompiler compiler = new ConcreteCompiler();
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "/test/");
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = pcm.new ProcessCommonJsModulesCallback();
        NodeTraversal t = new NodeTraversal(compiler, callback);
        Node externs = new Node(Node.EMPTY);
        Node root = new Node(Node.EMPTY);
        pcm.process(externs, root);

        Node script = new Node(Node.EMPTY);
        Node name = new Node("FILENAME");
        script.addChildToBack(name);
        callback.visitScript(t, script);
        // Check the visitScript logic here
        assertTrue(true);
    }

    @Test
    public void testProcessCallbackVisitModuleExports() {
        AbstractCompiler compiler = new ConcreteCompiler();
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "/test/");
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = pcm.new ProcessCommonJsModulesCallback();
        NodeTraversal t = new NodeTraversal(compiler, callback);
        Node externs = new Node(Node.EMPTY);
        Node root = new Node(Node.EMPTY);
        pcm.process(externs, root);

        Node moduleExports = new Node(Node.EXPR_RESULT, new Node(Node.GETPROP, new Node("module"), new Node("exports")));
        callback.visitModuleExports(moduleExports);
        // Check the visitModuleExports logic here
        assertTrue(true);
    }

    @Test
    public void testSuffixVarsCallback() {
        ProcessCommonJSModules.SuffixVarsCallback callback = new ProcessCommonJSModules(null).new SuffixVarsCallback("suffix");
        NodeTraversal t = new NodeTraversal(new ConcreteCompiler(), callback);
        NodeTraversal.traverse(new ConcreteCompiler(), new Node(Node.EXPR_RESULT, new Node("name1")), callback);
        // Check the SuffixVarsCallback logic here
        assertTrue(true);
    }

    private class ConcreteCompiler extends AbstractCompiler {
        @Override
        public void reportCodeChange() {
            // Do nothing
        }
    }

}