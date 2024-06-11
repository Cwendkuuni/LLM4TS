import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {

   @Test
   public void testConstructor() {
       SourceFile sourceFile = new SourceFile("test.js", "var x = 1;");
       JsAst jsAst = new JsAst(sourceFile);
       assertNotNull(jsAst);
       assertEquals(sourceFile.getName(), jsAst.getInputId().getId());
       assertEquals(sourceFile.getName(), jsAst.getSourceFile().getName());
   }

   @Test
   public void testGetAstRoot() {
       AbstractCompiler compiler = new Compiler();
       SourceFile sourceFile = new SourceFile("test.js", "var x = 1;");
       JsAst jsAst = new JsAst(sourceFile);
       Node astRoot = jsAst.getAstRoot(compiler);
       assertNotNull(astRoot);
   }

   @Test
   public void testClearAst() {
       SourceFile sourceFile = new SourceFile("test.js", "var x = 1;");
       JsAst jsAst = new JsAst(sourceFile);
       jsAst.clearAst();
       assertNull(jsAst.getAstRoot(null));
   }

   @Test
   public void testGetInputId() {
       SourceFile sourceFile = new SourceFile("test.js", "var x = 1;");
       JsAst jsAst = new JsAst(sourceFile);
       InputId inputId = jsAst.getInputId();
       assertNotNull(inputId);
       assertEquals(sourceFile.getName(), inputId.getId());
   }

   @Test
   public void testGetSourceFile() {
       SourceFile sourceFile = new SourceFile("test.js", "var x = 1;");
       JsAst jsAst = new JsAst(sourceFile);
       SourceFile getFile = jsAst.getSourceFile();
       assertNotNull(getFile);
       assertEquals(sourceFile, getFile);
   }

   @Test
   public void testSetSourceFile() {
       SourceFile sourceFile = new SourceFile("test.js", "var x = 1;");
       JsAst jsAst = new JsAst(sourceFile);
       SourceFile newFile = new SourceFile("new.js", "var y = 2;");
       jsAst.setSourceFile(newFile);
       assertEquals(newFile, jsAst.getSourceFile());
   }

   @Test
   public void testEmptySourceFileParsing() {
       AbstractCompiler compiler = new Compiler();
       SourceFile sourceFile = new SourceFile("empty.js", "");
       JsAst jsAst = new JsAst(sourceFile);
       Node astRoot = jsAst.getAstRoot(compiler);
       assertNotNull(astRoot);
       // Additional assertions for empty source file parsing
   }

   @Test
   public void testIOExceptionDuringParsing() {
       AbstractCompiler compiler = new Compiler();
       SourceFile sourceFile = new SourceFile("test.js", "var x = 1;");
       JsAst jsAst = new JsAst(sourceFile);
       // Additional setup to simulate IOException during parsing
       compiler.setParserConfig(/* set parser config to cause IOException */);
       Node astRoot = jsAst.getAstRoot(compiler);
       assertNotNull(astRoot);
       // Additional assertions for IOException handling
   }
}