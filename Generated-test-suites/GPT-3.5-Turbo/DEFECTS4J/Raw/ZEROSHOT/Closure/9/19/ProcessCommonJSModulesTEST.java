import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class ProcessCommonJSModulesTest {

    private ProcessCommonJSModules processCommonJSModules;
    private AbstractCompiler compiler;
    private String filenamePrefix;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        filenamePrefix = ProcessCommonJSModules.DEFAULT_FILENAME_PREFIX;
        processCommonJSModules = new ProcessCommonJSModules(compiler, filenamePrefix);
    }

    @Test
    public void testProcess() {
        Node externs = new Node(Token.EMPTY);
        Node root = new Node(Token.EMPTY);
        processCommonJSModules.process(externs, root);
        // TODO: Add test assertions
    }

    @Test
    public void testGuessCJSModuleName() {
        String filename = "test.js";
        String expected = ProcessCommonJSModules.toModuleName(filename);
        String actual = processCommonJSModules.guessCJSModuleName(filename);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetModule() {
        JSModule expected = null;
        JSModule actual = processCommonJSModules.getModule();
        assertEquals(expected, actual);
    }

    @Test
    public void testToModuleName() {
        String filename = "test.js";
        String expected = ProcessCommonJSModules.toModuleName(filename);
        String actual = ProcessCommonJSModules.toModuleName(filename);
        assertEquals(expected, actual);
    }

    @Test
    public void testToModuleNameWithRelativeAddressing() {
        String requiredFilename = "./module.js";
        String currentFilename = "src/test.js";
        String expected = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        String actual = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeSourceName() {
        String filename = "src/test.js";
        String expected = filename.substring(filenamePrefix.length());
        String actual = processCommonJSModules.normalizeSourceName(filename);
        assertEquals(expected, actual);
    }

    @Test
    public void testProcessCommonJsModulesCallback() {
        // TODO: Add test assertions
    }

    @Test
    public void testVisitRequireCall() {
        // TODO: Add test assertions
    }

    @Test
    public void testVisitScript() {
        // TODO: Add test assertions
    }

    @Test
    public void testEmitOptionalModuleExportsOverride() {
        // TODO: Add test assertions
    }

    @Test
    public void testVisitModuleExports() {
        // TODO: Add test assertions
    }

    @Test
    public void testSuffixVarsCallback() {
        // TODO: Add test assertions
    }

}