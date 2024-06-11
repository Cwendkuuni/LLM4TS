import com.google.javascript.jscomp.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProcessCommonJSModulesTest {

    private ProcessCommonJSModules processCommonJSModules;

    @Before
    public void setUp() {
        processCommonJSModules = new ProcessCommonJSModules(new Compiler(), ".");
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
        String requiredFilename = "../folder/test.js";
        String currentFilename = "src/test.js";
        String expected = "module$folder$test";
        String actual = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals(expected, actual);
    }

    @Test
    public void testProcessFileSystemRewrite() {
        // TODO: Implement test case for ProcessCommonJSModules.process
    }

    @Test
    public void testNormalizeSourceName() {
        String filename = "./src/test.js";
        String expected = "src/test.js";
        String actual = processCommonJSModules.normalizeSourceName(filename);
        assertEquals(expected, actual);
    }

    @Test
    public void testVisitRequireCall() {
        // TODO: Implement test case for ProcessCommonJSModules.ProcessCommonJsModulesCallback.visitRequireCall
    }

    @Test
    public void testVisitScript() {
        // TODO: Implement test case for ProcessCommonJSModules.ProcessCommonJsModulesCallback.visitScript
    }

    @Test
    public void testEmitOptionalModuleExportsOverride() {
        // TODO: Implement test case for ProcessCommonJSModules.ProcessCommonJsModulesCallback.emitOptionalModuleExportsOverride
    }

    @Test
    public void testVisitModuleExports() {
        // TODO: Implement test case for ProcessCommonJSModules.ProcessCommonJsModulesCallback.visitModuleExports
    }

    @Test
    public void testSuffixVarsCallback() {
        // TODO: Implement test case for ProcessCommonJSModules.SuffixVarsCallback
    }

}

