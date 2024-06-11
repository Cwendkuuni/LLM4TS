package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {
    private AbstractCompiler compiler;
    private ProcessCommonJSModules processCommonJSModules;

    @Before
    public void setUp() {
        compiler = new Compiler();
        processCommonJSModules = new ProcessCommonJSModules(compiler, "./");
    }

    @Test
    public void testProcess() {
        String externs = "externs.js";
        String root = "root.js";
        compiler.compile(externs + root);

        assertEquals(2, compiler.getInputs().size());
        assertNotNull(processCommonJSModules.getModule());
    }

    @Test
    public void testGuessCJSModuleName() {
        String filename = "./src/com/google/javascript/jscomp/ProcessCommonJSModulesTest.js";
        String expected = "module$src$com$google$javascript$jscomp$ProcessCommonJSModulesTest";
        String result = processCommonJSModules.guessCJSModuleName(filename);
        assertEquals(expected, result);
    }

    @Test
    public void testToModuleName() {
        String filename = "./src/com/google/javascript/jscomp/ProcessCommonJSModulesTest.js";
        String expected = "module$src$com$google$javascript$jscomp$ProcessCommonJSModulesTest";
        String result = ProcessCommonJSModules.toModuleName(filename);
        assertEquals(expected, result);
    }

    @Test
    public void testToModuleNameWithRelativeAddressing() {
        String requiredFilename = "../utils.js";
        String currentFilename = "./src/com/google/javascript/jscomp/ProcessCommonJSModulesTest.js";
        String expected = "module$src$com$google$javascript$utils";
        String result = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals(expected, result);
    }

    @Test
    public void testProcessCommonJsModulesCallback_visitModuleExports() {
        Node prop = new Node(
            Token.GETPROP,
            new Node(Token.NAME, new Node(Token.STRING, "module.exports")),
            new Node(Token.STRING, "foo"));
        String moduleName = "module$src$com$google$javascript$jscomp$ProcessCommonJSModulesTest";
        processCommonJSModules.processCommonJsModulesCallback.visitModuleExports(prop);

        assertEquals(moduleName, prop.getFirstChild().getString());
        assertEquals("module$exports", prop.getLastChild().getString());
        assertTrue(processCommonJSModules.processCommonJsModulesCallback.modulesWithExports.contains(moduleName));
    }

    @Test
    public void testProcessCommonJsModulesCallback_visitRequireCall() {
        Node require = new Node(
            Token.GETPROP,
            new Node(Token.NAME, new Node(Token.STRING, "require")),
            new Node(Token.STRING, "foo"));
        Node parent = new Node(Token.SCRIPT);
        Node script = new Node(Token.SCRIPT, require);
        parent.addChildToFront(script);
        processCommonJSModules.processCommonJsModulesCallback.visitRequireCall(null, require, parent);
        Node result = parent.getFirstChild();

        assertEquals(Token.NAME, result.getToken());
        assertEquals("module$src$com$google$javascript$jscomp$foo", result.getString());
        assertTrue(result.isName());
        assertTrue(script.hasOneChild());
        assertTrue(script.getFirstChild().isExprResult());
        assertTrue(script.getFirstChild().getFirstChild().isCall());
        assertTrue(script.getFirstChild().getFirstChild().getLastChild().isString());
        assertEquals("goog.require", script.getFirstChild().getFirstChild().getFirstChild().getQualifiedName());
        assertEquals("module$src$com$google$javascript$jscomp$foo", script.getFirstChild().getFirstChild().getLastChild().getString());
    }

    @Test
    public void testProcessCommonJsModulesCallback_visitScript() {
        Node script = new Node(Token.SCRIPT);
        Node t = new Node(Token.GETPROP);
        t.addChildToFront(script);
        processCommonJSModules.processCommonJsModulesCallback.visitScript(null, script);
        Node result = script.getFirstChild();

        assertNotNull(result);
        assertEquals(Token.VAR, result.getToken());
        assertEquals("module$src$com$google$javascript$jscomp$ProcessCommonJSModulesTest", result.getFirstChild().getString());
        assertTrue(result.isVar());
        assertTrue(script.hasTwoChildren());
        assertTrue(script.getChildAtIndex(1).isExprResult());
        assertTrue(script.getChildAtIndex(1).getFirstChild().isCall());
        assertTrue(script.getChildAtIndex(1).getFirstChild().getLastChild().isString());
        assertEquals("goog.provide", script.getChildAtIndex(1).getFirstChild().getFirstChild().getQualifiedName());
        assertEquals("module$src$com$google$javascript$jscomp$ProcessCommonJSModulesTest", script.getChildAtIndex(1).getFirstChild().getLastChild().getString());
    }

    @Test
    public void testSuffixVarsCallback_visit() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        Scope.Var var = new Scope.Var("foo", null, true);
        compiler.getTopScope().declare(var);
        Node node = new Node(Token.NAME, new Node(Token.STRING, "foo"));
        processCommonJSModules.suffixVarsCallback.visit(t, node, null);

        assertEquals("foo$$module$src$com$google$javascript$jscomp$ProcessCommonJSModulesTest", node.getString());
        assertEquals("foo", node.getProp(Node.ORIGINALNAME_PROP));
    }
}