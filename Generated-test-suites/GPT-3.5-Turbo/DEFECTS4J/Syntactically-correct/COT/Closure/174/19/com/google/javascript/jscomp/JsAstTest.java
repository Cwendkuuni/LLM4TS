/*
 * JUnit 4 test cases for JsAst class
 */

import static org.junit.Assert.assertEquals;
import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.Asserts;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

    private JsAst jsAst;
    private SourceFile sourceFile;

    @Before
    public void setUp() {
        sourceFile = new SourceFile("test.js", "var x = 5;");
        jsAst = new JsAst(sourceFile);
    }

    @After
    public void tearDown() {
        jsAst.clearAst();
        sourceFile.clearCachedSource();
    }

    @Test
    public void testGetAstRoot() {
        AbstractCompiler compiler = new Compiler();
        Node astRoot = jsAst.getAstRoot(compiler);
        Asserts.assertNode(astRoot).hasType(Token.SCRIPT);
        Asserts.assertNode(astRoot).hasChildCount(1);
        Asserts.assertNode(astRoot).hasChildren(Token.VAR);
    }

    @Test
    public void testClearAst() {
        AbstractCompiler compiler = new Compiler();
        jsAst.getAstRoot(compiler);
        Node astRootBeforeClear = jsAst.getAstRoot(compiler);
        jsAst.clearAst();
        Node astRootAfterClear = jsAst.getAstRoot(compiler);
        Asserts.assertNode(astRootBeforeClear).hasSameValue(astRootAfterClear);
    }

    @Test
    public void testGetInputId() {
        InputId inputId = jsAst.getInputId();
        Asserts.assertString(inputId.getId()).isEqualTo("test.js");
    }

    @Test
    public void testGetSourceFile() {
        SourceFile file = jsAst.getSourceFile();
        Asserts.assertString(file.getName()).isEqualTo("test.js");
        Asserts.assertString(file.getCode()).isEqualTo("var x = 5;");
    }

    @Test
    public void testSetSourceFile() {
        SourceFile newFile = new SourceFile("new_test.js", "var y = 10;");
        jsAst.setSourceFile(newFile);
        SourceFile file = jsAst.getSourceFile();
        Asserts.assertString(file.getName()).isEqualTo("new_test.js");
        Asserts.assertString(file.getCode()).isEqualTo("var y = 10;");
    }
}