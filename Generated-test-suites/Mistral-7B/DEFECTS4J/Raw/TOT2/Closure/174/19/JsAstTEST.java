package com.google.javascript.jscomp;

import com.google.javascript.jscomp.SourceFile;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class JsAstTest {
  @Test
  public void testJsAstConstructor() {
    SourceFile sourceFile = new SourceFile("source.js");
    JsAst jsAst = new JsAst(sourceFile);
    assertNotNull(jsAst);
  }

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("source.js");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();

    Node actual = jsAst.getAstRoot(compiler);
    assertNotNull(actual);
  }

  @Test
  public void testGetInputIdNull() {
    JsAst jsAst = new JsAst(null);
    assertNull(jsAst.getInputId());
  }

  @Test
  public void testGetSourceFileNull() {
    JsAst jsAst = new JsAst(new SourceFile("source.js"));
    jsAst.setSourceFile(null);
    assertNull(jsAst.getSourceFile());
  }

  @Test(expected = IOException.class)
  public void testParseIOException() {
    SourceFile sourceFile = new SourceFile("source.js");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();

    try {
      jsAst.parse(compiler);
    } catch (IOException e) {
      // Pass, since we expect an IOException to be thrown.
    }
  }

  @Test
  public void testSetSourceFileNull() {
    JsAst jsAst = new JsAst(new SourceFile("source.js"));
    jsAst.setSourceFile(null);
    assertNull(jsAst.getSourceFile());
  }

  @Test
  public void testSetSourceFileDifferentName() {
    JsAst jsAst = new JsAst(new SourceFile("source.js"));
    SourceFile newSourceFile = new SourceFile("different.js");
    jsAst.setSourceFile(newSourceFile);

    assertEquals("different.js", jsAst.getSourceFile().getName());
  }
}