import org.junit.Test;

import static org.junit.Assert.*;

public class JsAstTest {

    @Test
    public void testGetAstRoot() {
        // Test case 1
        SourceFile sourceFile1 = new SourceFile("testFile1.js", "var x = 10;");
        JsAst jsAst1 = new JsAst(sourceFile1);
        AbstractCompiler compiler1 = new AbstractCompiler();
        Node astRoot1 = jsAst1.getAstRoot(compiler1);
        assertNotNull(astRoot1);

        // Test case 2
        SourceFile sourceFile2 = new SourceFile("testFile2.js", "function add(a, b) {\n" +
                "    return a + b;\n" +
                "}\n" +
                "\n" +
                "var result = add(5, 10);");
        JsAst jsAst2 = new JsAst(sourceFile2);
        AbstractCompiler compiler2 = new AbstractCompiler();
        Node astRoot2 = jsAst2.getAstRoot(compiler2);
        assertNotNull(astRoot2);
    }

    @Test
    public void testClearAst() {
        // Test case 1
        SourceFile sourceFile1 = new SourceFile("testFile1.js", "var x = 10;");
        JsAst jsAst1 = new JsAst(sourceFile1);
        AbstractCompiler compiler1 = new AbstractCompiler();
        jsAst1.clearAst();
        assertNull(jsAst1.getAstRoot(compiler1));

        // Test case 2
        SourceFile sourceFile2 = new SourceFile("testFile2.js", "function add(a, b) {\n" +
                "    return a + b;\n" +
                "}\n" +
                "\n" +
                "var result = add(5, 10);");
        JsAst jsAst2 = new JsAst(sourceFile2);
        AbstractCompiler compiler2 = new AbstractCompiler();
        jsAst2.clearAst();
        assertNull(jsAst2.getAstRoot(compiler2));
    }

    @Test
    public void testGetInputId() {
        SourceFile sourceFile = new SourceFile("testFile.js", "var x = 10;");
        JsAst jsAst = new JsAst(sourceFile);
        InputId inputId = jsAst.getInputId();
        assertNotNull(inputId);
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = new SourceFile("testFile.js", "var x = 10;");
        JsAst jsAst = new JsAst(sourceFile);
        SourceFile result = jsAst.getSourceFile();
        assertEquals(sourceFile, result);
    }

    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile = new SourceFile("testFile.js", "var x = 10;");
        JsAst jsAst = new JsAst(sourceFile);
        SourceFile newSourceFile = new SourceFile("newTestFile.js", "var y = 20;");
        jsAst.setSourceFile(newSourceFile);
        assertEquals(newSourceFile, jsAst.getSourceFile());
    }
}