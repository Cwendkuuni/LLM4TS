package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserRunner;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();

    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);

    jsAst.clearAst();
    root = jsAst.getAstRoot(compiler);
    assertNotNull(root);

    Preconditions.checkState(jsAst.getInputId().equals(sourceFile.getName()));
    Preconditions.checkState(jsAst.getSourceFile().equals(sourceFile));
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);

    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(new AbstractCompiler()));
  }

  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);

    InputId inputId = jsAst.getInputId();
    checkState(inputId.getName().equals("test.js"));
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);

    SourceFile sourceFileResult = jsAst.getSourceFile();
    checkState(sourceFileResult.equals(sourceFile));
  }

  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile1 = new SourceFile("test1.js", "test content 1");
    JsAst jsAst = new JsAst(sourceFile1);

    SourceFile sourceFile2 = new SourceName("test2.js");
    jsAst.setSourceFile(sourceFile2);

    checkState(jsAst.getSourceFile().equals(sourceFile2));
  }

  @Test
  public void testParse() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();

    jsAst.parse(compiler);
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
  }

  @Test(expected = IOException.class)
  public void testParseWithIOException() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();

    jsAst.parse(compiler);
    compiler.report(JSError.make(AbstractCompiler.READ_ERROR, sourceFile.getName()));
  }

  @Test
  public void testParseWithNullAst() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();

    jsAst.parse(compiler);
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);

    jsAst.clearAst();
    root = jsAst.getAstRoot(compiler);
    assertNotNull(root);

    compiler.setOldParseTree(sourceFile.getName(), null);
    root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
  }
}