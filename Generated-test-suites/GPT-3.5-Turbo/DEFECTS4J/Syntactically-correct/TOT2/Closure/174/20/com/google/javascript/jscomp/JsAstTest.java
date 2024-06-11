package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;

public class JsAstTest {

  @Test
  public void testConstructor() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    assertNotNull(jsAst);
  }

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(new AbstractCompiler());
    assertNotNull(root);
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(new AbstractCompiler()));
  }

  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertNotNull(inputId);
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile getSourceFile = jsAst.getSourceFile();
    assertEquals("test.js", getSourceFile.getName());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = new SourceFile("new_test.js", "var y = 10;");
    jsAst.setSourceFile(newSourceFile);
    assertEquals("new_test.js", jsAst.getSourceFile().getName());
  }
  
  // Edge Cases and Exception Handling Test Cases can be added here

}