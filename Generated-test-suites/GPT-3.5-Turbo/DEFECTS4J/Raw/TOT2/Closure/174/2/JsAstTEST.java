import static org.junit.Assert.*;
import org.junit.Test;

public class JsAstTest {

    // Test case for JsAst constructor
    @Test
    public void testJsAstConstructor() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
        JsAst jsAst = new JsAst(sourceFile);
        
        assertNotNull(jsAst);
        assertEquals("test.js", jsAst.getInputId().getId());
        assertEquals(sourceFile, jsAst.getSourceFile());
        assertNull(jsAst.getAstRoot(null));
    }

    // Test case for getAstRoot method
    @Test
    public void testGetAstRoot() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
        JsAst jsAst = new JsAst(sourceFile);

        AbstractCompiler compiler = new AbstractCompiler();
        compiler.initOptions(new CompilerOptions());
        Node astRoot = jsAst.getAstRoot(compiler);

        assertNotNull(astRoot);
        assertEquals("SCRIPT", astRoot.getToken().name());
    }

    // Test case for clearAst method
    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
        JsAst jsAst = new JsAst(sourceFile);

        AbstractCompiler compiler = new AbstractCompiler();
        compiler.initOptions(new CompilerOptions());
        Node astRoot = jsAst.getAstRoot(compiler);

        assertNotNull(astRoot);

        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(null));
    }

    // Test case for setSourceFile method
    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile1 = new SourceFile("test.js", "var x = 10;");
        SourceFile sourceFile2 = new SourceFile("test2.js", "var y = 20;");

        JsAst jsAst = new JsAst(sourceFile1);

        jsAst.setSourceFile(sourceFile2);
        assertEquals(sourceFile2, jsAst.getSourceFile());
    }
}