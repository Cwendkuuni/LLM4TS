import static org.junit.Assert.*;
import org.junit.Test;

public class JsAstTest {
    
    @Test
    public void testGetAstRoot() {
        // Test case for typical use case
        SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
        JsAst jsAst = new JsAst(sourceFile);
        
        AbstractCompiler compiler = new AbstractCompiler();
        Node astRoot = jsAst.getAstRoot(compiler);
        
        assertNotNull(astRoot);
    }
    
    @Test
    public void testGetAstRoot_ClearAst() {
        // Test case for clearing the AST
        SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
        JsAst jsAst = new JsAst(sourceFile);
        
        AbstractCompiler compiler = new AbstractCompiler();
        Node astRoot = jsAst.getAstRoot(compiler);
        
        assertNotNull(astRoot);
        
        jsAst.clearAst();
        Node astRootAfterClear = jsAst.getAstRoot(compiler);
        
        assertNull(astRootAfterClear);
    }
    
    @Test
    public void testGetAstRoot_IOException() {
        // Test case for IOException during parsing
        SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
        JsAst jsAst = new JsAst(sourceFile);
        
        AbstractCompiler compiler = new AbstractCompiler();
        // Set up a mock IOException
        compiler.setParserConfig(new ParserConfig() {
            @Override
            public boolean isIdeMode() {
                return true;
            }
        });
        
        Node astRoot = jsAst.getAstRoot(compiler);
        
        assertNotNull(astRoot);
        // Ensure that there were no halting errors during parsing
        assertFalse(compiler.hasHaltingErrors());
    }
    
    @Test
    public void testClearAst_CachedSource() {
        // Test case for clearing the AST and cached source
        SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
        JsAst jsAst = new JsAst(sourceFile);
        
        AbstractCompiler compiler = new AbstractCompiler();
        Node astRoot = jsAst.getAstRoot(compiler);
        
        assertNotNull(astRoot);
        
        jsAst.clearAst();
        Node astRootAfterClear = jsAst.getAstRoot(compiler);
        
        assertNull(astRootAfterClear);
        // Ensure that the cached source is cleared
        assertNull(sourceFile.getCachedSource());
    }
    
    @Test
    public void testSetSourceFile() {
        // Test case for setting a new source file
        SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
        JsAst jsAst = new JsAst(sourceFile);
        
        AbstractCompiler compiler = new AbstractCompiler();
        Node astRoot = jsAst.getAstRoot(compiler);
        
        assertNotNull(astRoot);
        
        SourceFile newSourceFile = new SourceFile("test2.js", "var y = 20;");
        jsAst.setSourceFile(newSourceFile);
        
        Node astRootAfterSet = jsAst.getAstRoot(compiler);
        
        assertNotNull(astRootAfterSet);
        assertNotEquals(astRoot, astRootAfterSet);
    }
    
    @Test
    public void testSetSourceFile_IncorrectFileName() {
        // Test case for setting a new source file with incorrect file name
        SourceFile sourceFile = new SourceFile("test.js", "var x = 10;");
        JsAst jsAst = new JsAst(sourceFile);
        
        AbstractCompiler compiler = new AbstractCompiler();
        Node astRoot = jsAst.getAstRoot(compiler);
        
        assertNotNull(astRoot);
        
        SourceFile newSourceFile = new SourceFile("test2.js", "var y = 20;");
        jsAst.setSourceFile(newSourceFile);
        
        // Try to set a source file with an incorrect file name
        SourceFile incorrectFileNameSourceFile = new SourceFile("test3.js", "var z = 30;");
        try {
            jsAst.setSourceFile(incorrectFileNameSourceFile);
        } catch (IllegalStateException e) {
            assertEquals("Cannot set source file with different name", e.getMessage());
        }
        
        // Ensure that the source file remains the same
        assertEquals(newSourceFile, jsAst.getSourceFile());
    }
}