package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

public class JsAstTest {
    private static final Logger logger_ = Logger.getLogger(JsAst.class.getName());
    private JsAst jsAst;
    private SourceFile sourceFile;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        sourceFile = mock(SourceFile.class);
        when(sourceFile.getName()).thenReturn("test.js");
        jsAst = new JsAst(sourceFile);
        compiler = mock(AbstractCompiler.class);
    }

    @Test
    public void testGetAstRoot() {
        Node root = IR.root();
        when(compiler.getParserConfig()).thenReturn(ParserConfig.ES6);
        when(sourceFile.getCode()).thenReturn("");
        JsAst.ParserRunner.ParseResult parseResult = new JsAst.ParserRunner.ParseResult(root, null);
        try {
            when(JsAst.ParserRunner.parse(sourceFile, "", ParserConfig.ES6, compiler.getDefaultErrorReporter(), logger_)).thenReturn(parseResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Node astRoot = jsAst.getAstRoot(compiler);
        assertEquals(root, astRoot);
    }

    @Test
    public void testClearAst() {
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(compiler));
    }

    @Test
    public void testGetInputId() {
        InputId inputId = new InputId("test.js");
        assertEquals(inputId, jsAst.getInputId());
    }

    @Test
    public void testGetSourceFile() {
        assertEquals(sourceFile, jsAst.getSourceFile());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile file = mock(SourceFile.class);
        when(file.getName()).thenReturn("test.js");
        jsAst.setSourceFile(file);
        assertEquals(file, jsAst.getSourceFile());
    }

    @Test
    public void testParse() {
        Node root = IR.root();
        JsAst.ParserRunner.ParseResult parseResult = new JsAst.ParserRunner.ParseResult(root, null);
        try {
            when(JsAst.ParserRunner.parse(sourceFile, "", compiler.getParserConfig(), compiler.getDefaultErrorReporter(), logger_)).thenReturn(parseResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsAst.parse(compiler);
        assertEquals(root, jsAst.getAstRoot(compiler));
    }
}