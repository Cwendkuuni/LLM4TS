package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

    private Compiler compiler;
    private ProcessCommonJSModules pass;

    @Before
    public void setUp() {
        compiler = new Compiler();
        pass = new ProcessCommonJSModules(compiler, "test");
    }

    @Test
    public void testGuessCJSModuleName() {
        String filename = "testFile.js";
        String expected = "module$testFile";
        String result = pass.guessCJSModuleName(filename);
        assertEquals(expected, result);

        filename = "testFile/path/to/file.js";
        expected = "module$testFile$path$to$file";
        result = pass.guessCJSModuleName(filename);
        assertEquals(expected, result);
    }

    @Test
    public void testToModuleName() {
        String filename = "testFile.js";
        String expected = "module$testFile";
        String result = ProcessCommonJSModules.toModuleName(filename);
        assertEquals(expected, result);

        filename = ".";
        expected = "module$";
        result = ProcessCommonJSModules.toModuleName(filename);
        assertEquals(expected, result);

        filename = "./path/to/file.js";
        expected = "module$path$to$file";
        result = ProcessCommonJSModules.toModuleName(filename);
        assertEquals(expected, result);

        filename = "../file.js";
        expected = "module$file";
        result = ProcessCommonJSModules.toModuleName(filename);
        assertEquals(expected, result);
    }

    @Test
    public void testToModuleNameWithCurrentFilename() {
        String requiredFilename = "./sub/file.js";
        String currentFilename = "index.js";
        String expected = "module$sub$file";
        String result = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals(expected, result);

        requiredFilename = "../file.js";
        currentFilename = "src/index.js";
        expected = "module$file";
        result = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals(expected, result);

        requiredFilename = "sub/module.js";
        currentFilename = "src/index.js";
        expected = "module$sub$module";
        result = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals(expected, result);
    }

    @Test
    public void testProcess() {
        String code = "var a = require('./moduleA.js');\n" +
                "var b = require('../moduleB.js');\n" +
                "module.exports = {};";
        Node externs = compiler.parseTestCode("");
        Node root = compiler.parseTestCode(code);
        pass.process(externs, root);
        assertTrue(compiler.hasCodeChanged());
    }

    @Test
    public void testGuessCJSModuleNameWithFullPath() {
        String filename = "testFile.js";
        String expected = "module$testFile";
        String result = pass.guessCJSModuleName(filename);
        assertEquals(expected, result);
    }

    @Test
    public void testNormalizeSourceName() {
        String filename = "testFile.js";
        String expected = "testFile.js";
        String result = pass.normalizeSourceName(filename);
        assertEquals(expected, result);
    }

    @Test
    public void testProcessCommonJsModulesCallback() {
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = pass.new ProcessCommonJsModulesCallback();
        NodeTraversal t = new NodeTraversal(compiler, callback);
        Node n = compiler.parseTestCode("");
        Node parent = null;
        callback.visit(t, n, parent);
    }

    @Test
    public void testVisitRequireCall() {
        NodeTraversal t = new NodeTraversal(compiler, new ProcessCommonJSModules.ProcessCommonJsModulesCallback());
        Node require = new Node(
                IR.call(
                        IR.name("require"),
                        IR.string("./moduleA")
                )
        );
        Node parent = new Node(
                IR.exprResult(IR.name("a"))
        );
        pass.new ProcessCommonJsModulesCallback().visitRequireCall(t, require, parent);
        assertFalse(compiler.hasCodeChanged());
    }

    @Test
    public void testVisitScript() {
        NodeTraversal t = new NodeTraversal(compiler, new ProcessCommonJSModules.ProcessCommonJsModulesCallback());
        Node script = new Node("SCRIPT");
        pass.new ProcessCommonJsModulesCallback().visitScript(t, script);
        assertFalse(compiler.hasCodeChanged());
    }

    @Test
    public void testEmitOptionalModuleExportsOverride() {
        Node script = new Node(
                IR.block(
                        IR.exprResult(
                                IR.assign(
                                        IR.name("moduleName"),
                                        IR.getprop(IR.name("moduleName"), IR.string("module$exports"))
                                )
                        )
                )
        );
        String moduleName = "moduleName";
        pass.new ProcessCommonJsModulesCallback().emitOptionalModuleExportsOverride(script, moduleName);
        assertFalse(compiler.hasCodeChanged());
    }

    @Test
    public void testVisitModuleExports() {
        Node prop = new Node(
                IR.getprop(
                        IR.name("module.exports"),
                        IR.string("test")
                )
        );
        pass.new ProcessCommonJsModulesCallback().visitModuleExports(prop);
        assertFalse(compiler.hasCodeChanged());
    }

    @Test
    public void testSuffixVarsCallback() {
        String suffix = "testSuffix";
        NodeTraversal t = new NodeTraversal(compiler, new ProcessCommonJSModules.SuffixVarsCallback(suffix));
        Node n = new Node(IR.name("a"));
        Node parent = new Node(IR.var(IR.name("a")));
        pass.new SuffixVarsCallback(suffix).visit(t, n, parent);
        assertFalse(compiler.hasCodeChanged());
    }
}