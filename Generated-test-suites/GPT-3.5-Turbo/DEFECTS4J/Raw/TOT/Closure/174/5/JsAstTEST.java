import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {

    @Test
    public void testGetAstRoot() {
        // Typical use case: Compiler passed, root node is not null
        JsAst jsAst = new JsAst(new SourceFile("test.js", "var x = 5;"));
        AbstractCompiler compiler = new AbstractCompiler();
        Node root = jsAst.getAstRoot(compiler);
        assertNotNull(root);
    }

    @Test
    public void testClearAst() {
        // Typical use case: Ast root is cleared
        JsAst jsAst = new JsAst(new SourceFile("test.js", "var x = 5;"));
        AbstractCompiler compiler = new AbstractCompiler();
        jsAst.getAstRoot(compiler);
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(compiler));
    }

    @Test
    public void testGetInputId() {
        // Typical use case: InputId matches the provided source file name
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        InputId inputId = jsAst.getInputId();
        assertEquals(sourceFile.getName(), inputId.getIdName());
    }

    @Test
    public void testGetSourceFile() {
        // Typical use case: SourceFile matches the provided source file
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        SourceFile file = jsAst.getSourceFile();
        assertEquals(sourceFile, file);
    }

    @Test
    public void testSetSourceFile() {
        // Typical use case: SourceFile is successfully updated
        SourceFile sourceFile1 = new SourceFile("test.js", "var x = 5;");
        SourceFile sourceFile2 = new SourceFile("newtest.js", "var y = 10;");
        JsAst jsAst = new JsAst(sourceFile1);
        jsAst.setSourceFile(sourceFile2);
        SourceFile file = jsAst.getSourceFile();
        assertEquals(sourceFile2, file);
    }

    @Test
    public void testParse() {
        // Error scenario: IOException during parsing
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompiler();
        assertNull(jsAst.getAstRoot(compiler));
        jsAst.parse(compiler);
        assertNotNull(jsAst.getAstRoot(compiler));
    }

    @Test
    public void testParseWithErrors() {
        // Error scenario: Parsing with halting errors
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5; var;");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompiler();
        assertNull(jsAst.getAstRoot(compiler));
        jsAst.parse(compiler);
        assertNull(jsAst.getAstRoot(compiler)); // Root should be set to null if there are halting errors
    }

    @Test
    public void testPrepareAst() {
        // Typical use case: Root is prepared for compilation
        JsAst jsAst = new JsAst(new SourceFile("test.js", "var x = 5;"));
        AbstractCompiler compiler = new AbstractCompiler();
        jsAst.getAstRoot(compiler);
        jsAst.prepareAst(compiler);
        assertNotNull(compiler.getExternsRoot());
        assertNotNull(compiler.getJsRoot());
    }

    @Test
    public void testSetStaticSourceFile() {
        // Typical use case: Root's static source file is set
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompiler();
        jsAst.getAstRoot(compiler);
        jsAst.setStaticSourceFile();
        assertEquals(sourceFile, compiler.getRoot().getStaticSourceFile());
    }
}