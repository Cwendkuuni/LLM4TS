package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.regex.Pattern;

public class ProcessCommonJSModulesTest {
    
    private AbstractCompiler compiler;
    private ProcessCommonJSModules pass;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        pass = new ProcessCommonJSModules(compiler, "");
    }

    @Test
    public void testProcess() {
        Node externs = IR.root();
        Node root = IR.root();
        pass.process(externs, root);
        // Verify that the compiler traversal is called correctly
        verify(compiler).reportCodeChange();
    }

    @Test
    public void testGuessCJSModuleName() {
        String filename = "test.js";
        String expected = "module$test";
        String result = pass.guessCJSModuleName(filename);
        assertEquals(expected, result);
    }

    @Test
    public void testToModuleName() {
        String filename = "test.js";
        String expected = "module$test";
        String result = ProcessCommonJSModules.toModuleName(filename);
        assertEquals(expected, result);
    }

    @Test
    public void testToModuleNameWithRelativeAddressing() {
        String requiredFilename = "./module.js";
        String currentFilename = "test.js";
        String expected = "module$module";
        String result = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals(expected, result);
    }

    @Test
    public void testNormalizeSourceName() {
        String filename = "path/to/file.js";
        String expected = "file.js";
        String result = pass.normalizeSourceName(filename);
        assertEquals(expected, result);
    }

    @Test
    public void testVisitRequireCall() {
        NodeTraversal traversal = mock(NodeTraversal.class);
        Node require = IR.call(IR.name("require"), IR.string("module"));
        Node parent = IR.exprResult(require).copyInformationFrom(require);
        Node script = IR.script().copyInformationFromForTree(require);
        when(traversal.getSourceName()).thenReturn("test.js");
        when(traversal.getInput()).thenReturn(new CompilerInput(null));
        script.addChildToFront(parent);
        pass.visitRequireCall(traversal, require, parent);
        // Verify that the require call is rewritten correctly
        assertEquals("module$module", require.getFirstChild().getString());
        assertEquals(IR.name("module$module").srcref(require), parent.getFirstChild());
        // Verify that the goog.require call is emitted correctly
        assertEquals(IR.exprResult(
                IR.call(IR.getprop(IR.name("goog"), IR.string("require")),
                        IR.string("module$module"))), script.getLastChild());
        // Verify that the reportCodeChange method is called
        verify(compiler).reportCodeChange();
    }

    @Test
    public void testVisitScript() {
        NodeTraversal traversal = mock(NodeTraversal.class);
        Node script = IR.script();
        String moduleName = "module$test";
        pass.visitScript(traversal, script);
        // Verify that the var statement is added correctly
        assertEquals(IR.var(IR.name(moduleName), IR.objectlit()), script.getFirstChild());
        // Verify that the goog.provide call is emitted correctly
        assertEquals(IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
                IR.string(moduleName))), script.getLastChild());
        // Verify that the emitOptionalModuleExportsOverride method is called
        verify(pass).emitOptionalModuleExportsOverride(script, moduleName);
        // Verify that the SuffixVarsCallback is traversing the script node
        verify(traversal).traverse(compiler, script, pass.new SuffixVarsCallback(moduleName));
        // Verify that the reportCodeChange method is called
        verify(compiler).reportCodeChange();
    }

    @Test
    public void testEmitOptionalModuleExportsOverride() {
        Node script = IR.script();
        String moduleName = "module$test";
        pass.emitOptionalModuleExportsOverride(script, moduleName);
        // Verify that the if statement is added correctly
        assertEquals(IR.ifNode(
                IR.getprop(IR.name(moduleName), IR.string("module$exports")),
                IR.block(IR.exprResult(IR.assign(IR.name(moduleName),
                        IR.getprop(IR.name(moduleName), IR.string("module$exports")))))), script.getLastChild());
    }

    @Test
    public void testVisitModuleExports() {
        Node prop = IR.getprop(IR.name("module"), IR.string("exports"));
        pass.visitModuleExports(prop);
        // Verify that the module.exports assignment is rewritten correctly
        assertEquals("module$test", prop.getChildAtIndex(0).getString());
        assertEquals("module$exports", prop.getChildAtIndex(1).getString());
    }

    @Test
    public void testSuffixVarsCallback() {
        NodeTraversal traversal = mock(NodeTraversal.class);
        Node var = IR.name("var");
        Node script = IR.script().copyInformationFrom(var);
        Node parent = IR.exprResult(var).copyInformationFrom(var);
        String moduleName = "module$test";
        when(traversal.getScope()).thenReturn(new Scope(null));
        script.addChildToFront(parent);
        pass.new SuffixVarsCallback(moduleName).visit(traversal, var, parent);
        // Verify that the global variable name is suffixed correctly
        assertEquals("var$$module$test", var.getString());
    }
}