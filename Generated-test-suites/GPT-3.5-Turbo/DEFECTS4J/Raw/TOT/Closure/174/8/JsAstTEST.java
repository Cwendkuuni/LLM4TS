import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceFile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsAstTest {

    @Test
    public void testGetAstRoot() {
        SourceFile sourceFile = new SourceFile("testFile.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompiler();

        assertNull(jsAst.getAstRoot(compiler));
        compiler.report(JSError.make(AbstractCompiler.SYNTAX_ERROR, "testFile.js", 1, 6, "Unexpected token 'x'"));
        assertNull(jsAst.getAstRoot(compiler));

        jsAst.clearAst();
        assertEquals("SCRIPT", jsAst.getAstRoot(compiler).getToken().toString());
        assertEquals("VAR", jsAst.getAstRoot(compiler).getFirstChild().getToken().toString());
    }

    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("testFile.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompiler();

        jsAst.getAstRoot(compiler);
        jsAst.clearAst();

        assertNull(jsAst.getAstRoot(compiler));
    }

    @Test
    public void testGetInputId() {
        SourceFile sourceFile = new SourceFile("testFile.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);

        assertEquals("testFile.js", jsAst.getInputId().getId());
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = new SourceFile("testFile.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);

        assertEquals(sourceFile, jsAst.getSourceFile());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile1 = new SourceFile("testFile.js", "var x = 5;");
        SourceFile sourceFile2 = new SourceFile("testFile2.js", "var y = 10;");
        JsAst jsAst = new JsAst(sourceFile1);

        jsAst.setSourceFile(sourceFile2);
        assertEquals(sourceFile2, jsAst.getSourceFile());
    }
}