package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, filenamePrefix);
        String result = pcm.guessCJSModuleName("test.js");
        assertEquals("module$test", result);
        result = pcm.guessCJSModuleName("test/test.js");
        assertEquals("module$test$test", result);
        result = pcm.guessCJSModuleName("../test.js");
        assertEquals("module$../test", result);
    }

    @Test
    public void testToModuleName() {
        String result = ProcessCommonJSModules.toModuleName("test.js");
        assertEquals("module$test", result);
        result = ProcessCommonJSModules.toModuleName("test/test.js");
        assertEquals("module$test$test", result);
        result = ProcessCommonJSModules.toModuleName("../test.js");
        assertEquals("module$../test", result);
    }

    @Test
    public void testToModuleNameWithCurrentFilename() {
        String result = ProcessCommonJSModules.toModuleName("test.js", "current/test.js");
        assertEquals("module$test", result);
        result = ProcessCommonJSModules.toModuleName("test/test.js", "current/test.js");
        assertEquals("module$test$test", result);
        result = ProcessCommonJSModules.toModuleName("../test.js", "current/test.js");
        assertEquals("module$../test", result);
    }

    @Test
    public void testProcess() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, filenamePrefix);
        pcm.process(externs, root);
        assertEquals(module, pcm.getModule());
    }

    @Test
    public void testVisitRequireCall() {
        ProcessCommonJSModules.ProcessCommonJsModulesCallback cb = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
        NodeTraversal t = new NodeTraversal(compiler, cb);
        Node require = IR.call(IR.name("require"), IR.string("test"));
        Node parent = new Node(Token.EXPR_RESULT, require);
        cb.visitRequireCall(t, require, parent);
        assertEquals(IR.name("module$test"), parent.getChildAtIndex(0));
    }

    @Test
    public void testVisitRequireCallNoReportDependencies() {
        ProcessCommonJSModules.ProcessCommonJsModulesCallback cb = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
        NodeTraversal t = new NodeTraversal(compiler, cb);
        Node require = IR.call(IR.name("require"), IR.string("test"));
        Node parent = new Node(Token.EXPR_RESULT, require);
        cb.reportDependencies = false;
        cb.visitRequireCall(t, require, parent);
        assertEquals(IR.name("module$test"), parent.getChildAtIndex(0));
        assertNull(t.getInput().getRequiredInput("test"));
    }

    @Test
    public void testVisitScript() {
        ProcessCommonJSModules.ProcessCommonJsModulesCallback cb = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
        NodeTraversal t = new NodeTraversal(compiler, cb);
        Node script = IR.script();
        cb.visitScript(t, script);
        assertEquals(IR.var(IR.name("module$test"), IR.objectlit()), script.getChildAtIndex(0));
    }

    @Test
    public void testVisitScriptNoReportDependencies() {
        ProcessCommonJSModules.ProcessCommonJsModulesCallback cb = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
        NodeTraversal t = new NodeTraversal(compiler, cb);
        Node script = IR.script();
        cb.reportDependencies = false;
        cb.visitScript(t, script);
        assertEquals(IR.var(IR.name("module$test"), IR.objectlit()), script.getChildAtIndex(0));
        assertNull(t.getInput().getProvidedInput("module$test"));
    }

    @Test
    public void testVisitModuleExports() {
        ProcessCommonJSModules.ProcessCommonJsModulesCallback cb = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
        Node prop = IR.getprop(IR.name("module"), IR.string("exports"));
        cb.visitModuleExports(prop);
        assertEquals("module$test", prop.getChildAtIndex(0).getString());
        assertEquals("module$exports", prop.getChildAtIndex(1).getString());
        assertTrue(cb.modulesWithExports.contains("module$test"));
    }
}