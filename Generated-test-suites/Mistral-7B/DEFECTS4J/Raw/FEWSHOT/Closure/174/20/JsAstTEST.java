package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.junit.runner.RunWith;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class JsAstTest {

  @Test
  public void testJsAstInstance() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    assertNotNull(jsAst);
  }

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions()) {
      @Override
      public ParserConfig getParserConfig() {
        return new ParserConfig();
      }
    };
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(new AbstractCompiler(new CompilerOptions())));
  }

  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    assertEquals(sourceFile.getName(), jsAst.getInputId().getName());
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    assertEquals(sourceFile, jsAst.getSourceFile());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile1 = new SourceFile("test1.js", "test content 1");
    SourceFile sourceFile2 = new SourceFile("test2.js", "test content 2");
    JsAst jsAst = new JsAst(sourceFile1);
    jsAst.setSourceFile(sourceFile2);
    assertEquals(sourceFile2, jsAst.getSourceFile());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetSourceFileDifferentFileName() {
    SourceFile sourceFile1 = new SourceFile("test1.js", "test content 1");
    SourceFile sourceFile2 = new SourceFile("test2.js", "test content 2");
    JsAst jsAst = new JsAst(sourceFile1);
    jsAst.setSourceFile(sourceFile2);
  }

  @Test
  public void testParse() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions()) {
      @Override
      public ParserConfig getParserConfig() {
        return new ParserConfig();
      }
    };
    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParseWithErrors() {
    SourceFile sourceFile = new SourceFile("test.js", "test content with errors");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions()) {
      @Override
      public ParserConfig getParserConfig() {
        return new ParserConfig();
      }
    };
    jsAst.parse(compiler);
    assertNull(jsAst.getAstRoot(compiler));
  }
}