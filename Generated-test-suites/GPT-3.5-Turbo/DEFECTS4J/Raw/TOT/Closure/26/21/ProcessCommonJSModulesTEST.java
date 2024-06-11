import static org.junit.Assert.*;

import org.junit.Test;

public class ProcessCommonJSModulesTest {

    @Test
    public void testGuessCJSModuleName_withValidFilename_returnsExpectedModuleName() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "");

        String filename = "./src/main/java/com/google/javascript/jscomp/TestModule.js";
        String expectedModuleName = "module$com$google$javascript$jscomp$TestModule";

        String actualModuleName = pcm.guessCJSModuleName(filename);

        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testGuessCJSModuleName_withFilenameContainingDotsAndHyphens_returnsExpectedModuleName() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "");

        String filename = "./src/main/java/com/google/javascript/jscomp/Test-Module.v1.0.js";
        String expectedModuleName = "module$com$google$javascript$jscomp$Test_Module_v1_0";

        String actualModuleName = pcm.guessCJSModuleName(filename);

        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testToModuleName_withValidFilename_returnsExpectedModuleName() {
        String filename = "./src/main/java/com/google/javascript/jscomp/TestModule.js";
        String expectedModuleName = "module$com$google$javascript$jscomp$TestModule";

        String actualModuleName = ProcessCommonJSModules.toModuleName(filename);

        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testToModuleName_withFilenameContainingDotsAndHyphens_returnsExpectedModuleName() {
        String filename = "./src/main/java/com/google/javascript/jscomp/Test-Module.v1.0.js";
        String expectedModuleName = "module$com$google$javascript$jscomp$Test_Module_v1_0";

        String actualModuleName = ProcessCommonJSModules.toModuleName(filename);

        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testToModuleName_withRequiredFilenameAndCurrentFilename_returnsExpectedModuleName() {
        String requiredFilename = "./src/main/java/com/google/javascript/jscomp/DependentModule.js";
        String currentFilename = "./src/main/java/com/google/javascript/jscomp/TestModule.js";
        String expectedModuleName = "module$com$google$javascript$jscomp$DependentModule";

        String actualModuleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);

        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testToModuleName_withRequiredFilenameContainingDotsAndHyphensAndCurrentFilename_returnsExpectedModuleName() {
        String requiredFilename = "./src/main/java/com/google/javascript/jscomp/Dependent-Module.v1.0.js";
        String currentFilename = "./src/main/java/com/google/javascript/jscomp/TestModule.js";
        String expectedModuleName = "module$com$google$javascript$jscomp$Dependent_Module_v1_0";

        String actualModuleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);

        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testProcess_withValidInput_callsTraverseMethod() {
        // Setup
        AbstractCompiler compiler = new AbstractCompiler();
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "");
        NodeTraversalMock nodeTraversalMock = new NodeTraversalMock(compiler, null, null);

        // Execute
        pcm.process(null, null);

        // Verify
        assertTrue(nodeTraversalMock.isTraverseMethodCalled);
    }

    @Test
    public void testGetModule_withoutCallingProcess_returnsNull() {
        // Setup
        AbstractCompiler compiler = new AbstractCompiler();
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "");

        // Execute
        JSModule actualModule = pcm.getModule();

        // Verify
        assertNull(actualModule);
    }

    @Test
    public void testGetModule_afterCallingProcess_returnsExpectedModule() {
        // Setup
        AbstractCompiler compiler = new AbstractCompiler();
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "");
        JSModule expectedModule = new JSModule("TestModule");

        // Execute
        pcm.process(null, null);
        JSModule actualModule = pcm.getModule();

        // Verify
        assertEquals(expectedModule.getName(), actualModule.getName());
    }

    private class NodeTraversalMock extends NodeTraversal {

        public boolean isTraverseMethodCalled = false;

        public NodeTraversalMock(AbstractCompiler compiler, Node externs, Node root) {
            super(compiler, externs, root);
        }

        @Override
        public void traverse(NodeTraversalCallback callback) {
            isTraverseMethodCalled = true;
        }
    }
}