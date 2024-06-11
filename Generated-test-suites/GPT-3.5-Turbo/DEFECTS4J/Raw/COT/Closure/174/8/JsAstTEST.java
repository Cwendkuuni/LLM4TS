
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.parsing.ParserRunner;

import java.io.IOException;

import java.util.logging.Logger;

import org.junit.Assert;

import org.junit.Test;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
     SourceFile sourceFile = new SourceFile("testFile.js", "var x = 5;");
     JsAst jsAst = new JsAst(sourceFile);
     AbstractCompiler compiler = new Compiler();
     Node astRoot = jsAst.getAstRoot(compiler);
     Assert.assertNotNull(astRoot);
  }

  @Test
  public void testClearAst() {
     SourceFile sourceFile = new SourceFile("testFile.js", "var x = 5;");
     JsAst jsAst = new JsAst(sourceFile);
     jsAst.clearAst();
     AbstractCompiler compiler = new Compiler();
     Node astRoot = jsAst.getAstRoot(compiler);
     Assert.assertNull(astRoot);
  }

  @Test
  public void testGetInputId() {
     SourceFile sourceFile = new SourceFile("testFile.js", "var x = 5;");
     JsAst jsAst = new JsAst(sourceFile);
     InputId inputId = jsAst.getInputId();
     Assert.assertNotNull(inputId);
  }

  @Test
  public void testGetSourceFile() {
     SourceFile sourceFile = new SourceFile("testFile.js", "var x = 5;");
     JsAst jsAst = new JsAst(sourceFile);
     SourceFile result = jsAst.getSourceFile();
     Assert.assertEquals(sourceFile, result);
  }

  @Test
  public void testSetSourceFile() {
     SourceFile sourceFile = new SourceFile("testFile.js", "var x = 5;");
     JsAst jsAst = new JsAst(sourceFile);
     SourceFile newSourceFile = new SourceFile("newFile.js", "var y = 10;");
     jsAst.setSourceFile(newSourceFile);
     SourceFile result = jsAst.getSourceFile();
     Assert.assertEquals(newSourceFile, result);
  }
  
  @Test
  public void testParse() {
     SourceFile sourceFile = new SourceFile("testFile.js", "var x = 5;");
     JsAst jsAst = new JsAst(sourceFile);
     AbstractCompiler compiler = new Compiler();
     jsAst.parse(compiler);
     Node astRoot = jsAst.getAstRoot(compiler);
     Assert.assertNotNull(astRoot);
   }

}
