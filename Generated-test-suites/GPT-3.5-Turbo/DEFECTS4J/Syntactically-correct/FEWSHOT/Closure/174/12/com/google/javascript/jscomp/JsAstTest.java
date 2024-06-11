
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.Test;

import java.io.IOException;

public class JsAstTest {
    @Test
    public void testConstructor() {
        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);

        assertNotNull(jsAst.getInputId());
        assertNotNull(jsAst.getSourceFile());
        assertEquals("test.js", jsAst.getSourceFile().getName());
        assertNull(jsAst.getAstRoot(null));
    }

    @Test
    public void testGetAstRoot() {
        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);

        AbstractCompiler compiler = new Compiler();
        Node astRoot = jsAst.getAstRoot(compiler);

        assertNull(astRoot);

        CompilerOptions options = new CompilerOptions();
        options.setParseJsDocDocumentation(false);
        JsAst ast = new JsAst(sourceFile);
        SourceAst astInstance = ast;
        Node root = astInstance.getAstRoot(new Compiler(options));
        assertNotNull(root);
    }

    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);

        AbstractCompiler compiler = new Compiler();
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);

        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(null));
    }

    @Test
    public void testGetInputId() {
        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);

        assertEquals("test.js", jsAst.getInputId().getIdName());
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);

        assertNotNull(jsAst.getSourceFile());
        assertEquals("test.js", jsAst.getSourceFile().getName());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);

        SourceFile newSourceFile = new SourceFile("newtest.js");
        jsAst.setSourceFile(newSourceFile);

        assertNotNull(jsAst.getSourceFile());
        assertEquals("newtest.js", jsAst.getSourceFile().getName());
    }

    @Test
    public void testParse() {
        CompilerOptions options = new CompilerOptions();
        options.setParseJsDocDocumentation(false);

        SourceFile sourceFile = new SourceFile("test.js");
        JsAst jsAst = new JsAst(sourceFile);

        Compiler compiler = new Compiler(options);
        jsAst.parse(compiler);

        assertNotNull(jsAst.getAstRoot(compiler));
        assertFalse(compiler.hasHaltingErrors());
    }
}
