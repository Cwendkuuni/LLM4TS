package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

    private ProcessCommonJSModules processCommonJSModules;

    @Before
    public void setUp() {
        processCommonJSModules = new ProcessCommonJSModules(new AbstractCompiler(), ".");
    }

    @Test
    public void testGuessCJSModuleName() {
        String filename = "test.js";
        assertEquals("module$test", processCommonJSModules.guessCJSModuleName(filename));

        filename = "dir/test.js";
        assertEquals("module$dir$test", processCommonJSModules.guessCJSModuleName(filename));

        filename = "./test.js";
        assertEquals("module$test", processCommonJSModules.guessCJSModuleName(filename));

        filename = "../test.js";
        assertEquals("module$test", processCommonJSModules.guessCJSModuleName(filename));
    }

    @Test
    public void testToModuleName() {
        String filename = "test.js";
        assertEquals("module$test", ProcessCommonJSModules.toModuleName(filename));

        filename = "dir/test.js";
        assertEquals("module$dir$test", ProcessCommonJSModules.toModuleName(filename));

        filename = "./test.js";
        assertEquals("module$test", ProcessCommonJSModules.toModuleName(filename));

        filename = "../test.js";
        assertEquals("module$test", ProcessCommonJSModules.toModuleName(filename));
    }

    @Test
    public void testToModuleNameWithCurrentFilename() {
        String requiredFilename = "required.js";
        String currentFilename = "current.js";
        assertEquals("module$required", ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename));

        currentFilename = "dir/current.js";
        assertEquals("module$dir$required", ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename));

        currentFilename = "dir/test.js";
        assertEquals("module$test$required", ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename));

        currentFilename = "./current.js";
        assertEquals("module$required", ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename));

        currentFilename = "../current.js";
        assertEquals("module$required", ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename));
    }

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        Node externs = new Node();
        Node root = new Node();
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, ".");
        processCommonJSModules.process(externs, root);
    }

    @Test
    public void testVisitRequireCall() {
        NodeTraversal t = new NodeTraversal();
        Node require = new Node();
        Node parent = new Node();
        require.addChildToFront(new Node("require"));
        require.addChildToBack(new Node("requiredModule"));
        parent.addChildToFront(require);
  
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
            processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visitRequireCall(t, require, parent);
    }

    @Test
    public void testVisitScript() {
        NodeTraversal t = new NodeTraversal();
        Node script = new Node();
        script.setSourceFileName("test.js");
  
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
            processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visitScript(t, script);
    }

    @Test
    public void testEmitOptionalModuleExportsOverride() {
        Node script = new Node();
        script.setSourceFileName("test.js");
  
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
            processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.emitOptionalModuleExportsOverride(script, "module");
    }

    @Test
    public void testVisitModuleExports() {
        Node prop = new Node();
        Node module = new Node("module");
        Node exports = new Node("exports");
        prop.addChildToBack(module);
        prop.addChildToBack(exports);
  
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
            processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visitModuleExports(prop);
    }

    @Test
    public void testSuffixVarsCallback() {
        NodeTraversal t = new NodeTraversal();
        Node node = new Node();
        Node parent = new Node();
        parent.addChildToFront(node);
  
        ProcessCommonJSModules.SuffixVarsCallback callback =
            processCommonJSModules.new SuffixVarsCallback("suffix");
        callback.visit(t, node, parent);
    }
}