package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ProcessCommonJSModulesTest {

    @Mock
    private AbstractCompiler compiler;

    @Mock
    private CompilerInput input;

    @Mock
    private Node externs;

    @Mock
    private Node root;

    @Mock
    private NodeTraversal traversal;

    private ProcessCommonJSModules processCommonJSModules;
    private String filenamePrefix;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        filenamePrefix = "." + File.separator;
        processCommonJSModules = new ProcessCommonJSModules(compiler, filenamePrefix);
    }

    @Test
    public void testProcess() {
        when(compiler.getJsRoot()).thenReturn(root);
        processCommonJSModules.process(externs, root);
        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testGuessCJSModuleName() {
        String filename = "test.js";
        String expected = "module$test";
        String actual = processCommonJSModules.guessCJSModuleName(filename);
        assertEquals(expected, actual);
    }

    @Test
    public void testGuessCJSModuleNameWithPrefix() {
        String filename = "." + File.separator + "src" + File.separator + "test.js";
        String expected = "module$src$test";
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
    public void testToModuleNameWithRelativePath() {
        String requiredFilename = "./src/test.js";
        String currentFilename = "config.js";
        String expected = "module$src$test";
        String actual = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals(expected, actual);
    }

    @Test
    public void testToModuleNameWithParentDirectoryPath() {
        String requiredFilename = "../src/test.js";
        String currentFilename = "config.js";
        String expected = "module$src$test";
        String actual = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeSourceName() {
        String filename = "." + File.separator + "src" + File.separator + "test.js";
        String expected = "src" + File.separator + "test.js";
        String actual = processCommonJSModules.normalizeSourceName(filename);
        assertEquals(expected, actual);
    }

    @Test
    public void testVisitRequireCall() {
        Node require = IR.call(
                          IR.getprop(IR.name("require"), IR.string("module1")),
                          IR.string("module1"))
                          .setJSDocInfo(null);
        Node parent = IR.exprResult(require).setJSDocInfo(null);
        String moduleName = "module1";
        input.addRequire(moduleName);

        processCommonJSModules.visitRequireCall(traversal, require, parent);

        verify(input, times(1)).addRequire(moduleName);
        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testVisitScript() {
        Node script = IR.script()
                      .addChildToBack(IR.var(IR.name("module1"), IR.objectlit()))
                      .addChildToFront(IR.exprResult(
                          IR.call(IR.getprop(IR.name("goog"), IR.string("provide")), IR.string("module1"))))
                      .setJSDocInfo(null);
        String moduleName = "module1";
        String sourceName = "src/test.js";
        String normalizedSourceName = "test.js";
        input.setModule(any(JSModule.class));

        when(traversal.getSourceName()).thenReturn(sourceName);
        when(input.getSourceName()).thenReturn(sourceName);
        when(compiler.getJsRoot()).thenReturn(root);
        when(root.isScript()).thenReturn(true);
        when(root.getSourceFileName()).thenReturn(sourceName);
        when(compiler.getCodeChange()).thenReturn(false);
        when(compiler.reportCodeChange()).thenReturn(false);
        processCommonJSModules.visitScript(traversal, script);

        verify(input, times(1)).setModule(any(JSModule.class));
        verify(compiler, times(1)).getCodeChange();
        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testVisitModuleExports() {
        Node prop = IR.getprop(IR.name("module.exports"), IR.string("property"));
        processCommonJSModules.visitModuleExports(prop);
        assertEquals("module1", prop.getChildAtIndex(0).getString());
        assertEquals("module$exports", prop.getChildAtIndex(1).getString());
    }

    @Test
    public void testSuffixVarsCallback() {
        Node node = IR.name("var1");
        Node parent = new Node(-1, node);

        Scope scope = new Scope(scopeRoot, 0);
        scope.declare("var1", node, scope);
        when(traversal.getScope()).thenReturn(scope);
        when(node.getString()).thenReturn("var1");
        processCommonJSModules.new SuffixVarsCallback("module1").visit(traversal, node, parent);

        assertEquals("var1", node.getString());
    }

}