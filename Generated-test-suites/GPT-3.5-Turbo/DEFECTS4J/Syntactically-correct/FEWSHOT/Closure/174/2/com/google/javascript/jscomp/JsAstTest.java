package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.deps.DependencyOptions;
import com.google.javascript.jscomp.parsing.Config;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.jscomp.parsing.ParserRunner.ParseResult;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;

import java.io.IOException;
import java.util.logging.Logger;

public class JsAstTest {
    private JsAst jsAst;
    private SourceFile sourceFile;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        sourceFile = mock(SourceFile.class);
        jsAst = new JsAst(sourceFile);
        compiler = mock(AbstractCompiler.class);
    }

    @Test
    public void testGetAstRoot() {
        Node astRoot = jsAst.getAstRoot(compiler);
    }

    @Test
    public void testClearAst() {
        jsAst.clearAst();
    }

    @Test
    public void testGetInputId() {
        InputId inputId = jsAst.getInputId();
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = jsAst.getSourceFile();
    }

    @Test
    public void testSetSourceFile() {
        SourceFile file = mock(SourceFile.class);
        jsAst.setSourceFile(file);
    }

    @Test
    public void testParse() throws IOException {
        Config config = new Config(ImmutableList.of(), Charsets.UTF_8, false);
        DependencyOptions options = new DependencyOptions();
        Logger logger = Logger.getLogger(JsAst.class.getName());

        ParseResult result = ParserRunner.parse(sourceFile, sourceFile.getCode(), config, options, logger);

        assertEquals(null, jsAst.getAstRoot(compiler));

        compiler.setOldParseTree(sourceFile.getName(), result.oldAst);
        Node root = result.ast;

        assertEquals(root, jsAst.getAstRoot(compiler));
        
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(compiler));
        
        compiler.report(JSError.make(AbstractCompiler.READ_ERROR, sourceFile.getName()));
        assertEquals(IR.script(), jsAst.getAstRoot(compiler));
        
        root.setStaticSourceFile(sourceFile);
        assertEquals(sourceFile, root.getStaticSourceFile());
    }
}