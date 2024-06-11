package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.ParserConfig;
import com.google.javascript.rhino.ParserRunner;
import com.google.javascript.rhino.SourceName;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.Node;

import java.io.IOException;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class JsAstTest {

    @Test
    public void testGetAstRoot() {
        SourceFile sourceFile = Mockito.mock(SourceFile.class);
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        ParserConfig parserConfig = Mockito.mock(ParserConfig.class);
        ParserRunner parserRunner = Mockito.mock(ParserRunner.class);
        Logger logger = Mockito.mock(Logger.class);

        Preconditions.checkState(sourceFile != null);
        Preconditions.checkState(compiler != null);
        Preconditions.checkState(parserConfig != null);
        Preconditions.checkState(parserRunner != null);
        Preconditions.checkState(logger != null);

        String sourceCode = "function foo() {}";

        Mockito.when(sourceFile.getName()).thenReturn("test.js");
        Mockito.when(sourceFile.getCode()).thenReturn(sourceCode.getBytes());

        Node ast = Mockito.mock(Node.class);
        Node oldAst = Mockito.mock(Node.class);

        InputId inputId = new InputId("test.js");

        Mockito.when(parserRunner.parse(sourceFile, sourceCode.getBytes(), parserConfig,
                compiler.getErrorReporter(), logger)).thenReturn(new ParserRunner.ParseResult(ast, oldAst));

        JsAst jsAst = new JsAst(sourceFile);
        Node result = jsAst.getAstRoot(compiler);

        assertNotNull(result);
        assertEquals(ast, result);
        Mockito.verify(compiler).setOldParseTree(inputId, oldAst);
    }

    @Test
    public void testClearAst() {
        SourceFile sourceFile = Mockito.mock(SourceFile.class);
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(null));
    }

    @Test
    public void testGetInputId() {
        SourceFile sourceFile = Mockito.mock(SourceFile.class);
        JsAst jsAst = new JsAst(sourceFile);
        InputId result = jsAst.getInputId();
        assertEquals("test.js", result.getName());
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = Mockito.mock(SourceFile.class);
        JsAst jsAst = new JsAst(sourceFile);
        SourceFile result = jsAst.getSourceFile();
        assertEquals(sourceFile, result);
    }

    @Test
    public void testSetSourceFile() {
        SourceFile oldSourceFile = Mockito.mock(SourceFile.class);
        SourceFile newSourceFile = Mockito.mock(SourceFile.class);

        Preconditions.checkState(oldSourceFile != null);
        Preconditions.checkState(newSourceFile != null);

        String oldFileName = "oldFile.js";
        String newFileName = "newFile.js";

        Mockito.when(oldSourceFile.getName()).thenReturn(oldFileName);
        Mockito.when(newSourceFile.getName()).thenReturn(newFileName);

        JsAst jsAst = new JsAst(oldSourceFile);
        jsAst.setSourceFile(newSourceFile);

        assertEquals(newFileName, jsAst.getFileName());
    }

    @Test
    public void testParseWithSuccess() throws IOException {
        SourceFile sourceFile = Mockito.mock(SourceFile.class);
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        ParserConfig parserConfig = Mockito.mock(ParserConfig.class);
        ParserRunner parserRunner = Mockito.mock(ParserRunner.class);
        Logger logger = Mockito.mock(Logger.class);

        Preconditions.checkState(sourceFile != null);
        Preconditions.checkState(compiler != null);
        Preconditions.checkState(parserConfig != null);
        Preconditions.checkState(parserRunner != null);
        Preconditions.checkState(logger != null);

        String sourceCode = "function foo() {}";

        Mockito.when(sourceFile.getName()).thenReturn("test.js");
        Mockito.when(sourceFile.getCode()).thenReturn(sourceCode.getBytes());

        Node ast = Mockito.mock(Node.class);
        Node oldAst = Mockito.mock(Node.class);

        InputId inputId = new InputId("test.js");

        Mockito.when(parserRunner.parse(sourceFile, sourceCode.getBytes(), parserConfig,
                compiler.getErrorReporter(), logger)).thenReturn(new ParserRunner.ParseResult(ast, oldAst));

        JsAst jsAst = new JsAst(sourceFile);
        jsAst.parse(compiler);

        assertNotNull(jsAst.getAstRoot(compiler));
    }

    @Test
    public void testParseWithError() throws IOException {
        SourceFile sourceFile = Mockito.mock(SourceFile.class);
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        ParserConfig parserConfig = Mockito.mock(ParserConfig.class);
        ParserRunner parserRunner = Mockito.mock(ParserRunner.class);
        Logger logger = Mockito.mock(Logger.class);

        Preconditions.checkState(sourceFile != null);
        Preconditions.checkState(compiler != null);
        Preconditions.checkState(parserConfig != null);
        Preconditions.checkState(parserRunner != null);
        Preconditions.checkState(logger != null);

        String sourceCode = "function foo() {";

        Mockito.when(sourceFile.getName()).thenReturn("test.js");
        Mockito.when(sourceFile.getCode()).thenReturn(sourceCode.getBytes());

        JsAst jsAst = new JsAst(sourceFile);
        jsAst.parse(compiler);

        assertNull(jsAst.getAstRoot(compiler));
    }

    @Test
    public void testSetOldParseTree() {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        InputId inputId = new InputId("test.js");
        Node oldAst = Mockito.mock(Node.class);

        Preconditions.checkState(compiler != null);

        JsAst jsAst = new JsAst(null);
        jsAst.setOldParseTree(inputId, oldAst);

        assertEquals(oldAst, jsAst.getOldParseTree());
    }

    @Test
    public void testGetOldParseTree() {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        Node oldAst = Mockito.mock(Node.class);

        Preconditions.checkState(compiler != null);

        JsAst jsAst = new JsAst(null);
        jsAst.setOldParseTree(null, oldAst);

        assertEquals(oldAst, jsAst.getOldParseTree());
    }

    @Test
    public void testPrepareAst() {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        Node root = Mockito.mock(Node.class);

        Preconditions.checkState(compiler != null);

        JsAst jsAst = new JsAst(null);
        jsAst.parse(compiler);
        jsAst.prepareAst(root);

        Mockito.verify(compiler).prepareAst(root);
    }

    @Test
    public void testSetStaticSourceFile() {
        Node root = Mockito.mock(Node.class);
        SourceFile sourceFile = Mockito.mock(SourceFile.class);

        Preconditions.checkState(root != null);
        Preconditions.checkState(sourceFile != null);

        JsAst jsAst = new JsAst(null);
        jsAst.setStaticSourceFile(sourceFile);

        assertEquals(sourceFile, root.getStaticSourceFile());
    }
}