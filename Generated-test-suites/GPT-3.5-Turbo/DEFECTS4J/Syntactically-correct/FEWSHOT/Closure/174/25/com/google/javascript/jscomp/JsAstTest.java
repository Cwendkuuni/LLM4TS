package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

public class JsAstTest {
    private static final Logger logger_ = Logger.getLogger(JsAstTest.class.getName());
    private static final long serialVersionUID = 1L;

    private JsAst jsAst;
    private SourceFile sourceFile;
    private String fileName;
    private Node root;

    @Before
    public void setUp() {
        sourceFile = new SourceFile("test.js");
        jsAst = new JsAst(sourceFile);
        fileName = sourceFile.getName();
        root = null;
    }

    @Test
    public void testConstructor() {
        assertNotNull(jsAst.getInputId());
        assertNotNull(jsAst.getSourceFile());
        assertEquals(fileName, jsAst.getSourceFile().getName());
    }

    @Test
    public void testGetAstRoot() {
        AbstractCompiler compiler = new AbstractCompiler();
        root = jsAst.getAstRoot(compiler);
        assertNotNull(root);
        assertSame(root, jsAst.getAstRoot(compiler));
    }

    @Test
    public void testClearAst() {
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(new AbstractCompiler()));
    }

    @Test
    public void testGetInputId() {
        assertNotNull(jsAst.getInputId());
        assertEquals(fileName, jsAst.getInputId().getId());
    }

    @Test
    public void testGetSourceFile() {
        assertNotNull(jsAst.getSourceFile());
        assertEquals(fileName, jsAst.getSourceFile().getName());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile newFile = new SourceFile("new.js");
        jsAst.setSourceFile(newFile);
        assertEquals(newFile, jsAst.getSourceFile());
        assertEquals("new.js", jsAst.getInputId().getId());
        jsAst.setSourceFile(sourceFile); // set it back for other tests
    }

    @Test
    public void testParse_validFile() {
        AbstractCompiler compiler = new AbstractCompiler();
        jsAst.parse(compiler);
        assertNotNull(root);
        assertSame(root, jsAst.getAstRoot(compiler));
    }

    @Test
    public void testParse_fileIOException() {
        AbstractCompiler compiler = new AbstractCompiler();
        sourceFile.setCode(null); // set code to null to trigger IOException
        jsAst.parse(compiler);
        assertNull(root);
    }

    @Test
    public void testParse_nullRoot() {
        AbstractCompiler compiler = new AbstractCompiler();
        compiler.setOldParseTree(fileName, null);
        jsAst.parse(compiler);
        assertNotNull(root);
        assertSame(IR.script(), root);
    }

    @Test
    public void testPrepareAst() {
        AbstractCompiler compiler = new AbstractCompiler();
        jsAst.prepareAst(root);
        // Verify the preparation is done correctly
    }
}