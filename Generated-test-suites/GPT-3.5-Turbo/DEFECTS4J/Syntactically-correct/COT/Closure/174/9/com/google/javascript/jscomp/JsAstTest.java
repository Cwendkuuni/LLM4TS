
package com.google.javascript.jscomp;

import org.junit.Test;
import com.google.javascript.rhino.Node;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    // Create a sample SourceFile
    SourceFile sourceFile = new SourceFile("test.js", "function foo() { return 42; }");

    // Create a JsAst instance
    JsAst jsAst = new JsAst(sourceFile);

    // Create a dummy compiler instance for testing purposes
    AbstractCompiler compiler = new AbstractCompiler();

    // Call the getAstRoot method and check if it returns a Node
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testClearAst() {
    // Create a sample SourceFile
    SourceFile sourceFile = new SourceFile("test.js", "function foo() { return 42; }");

    // Create a JsAst instance
    JsAst jsAst = new JsAst(sourceFile);

    // Call the clearAst method and check if the root Node is null
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(new AbstractCompiler()));
  }

  @Test
  public void testGetInputId() {
    // Create a sample SourceFile
    SourceFile sourceFile = new SourceFile("test.js", "function foo() { return 42; }");

    // Create a JsAst instance
    JsAst jsAst = new JsAst(sourceFile);

    // Call the getInputId method and check if it returns the expected InputId
    InputId inputId = jsAst.getInputId();
    assertEquals("test.js", inputId.getIdName());
  }

  @Test
  public void testGetSourceFile() {
    // Create a sample SourceFile
    SourceFile sourceFile = new SourceFile("test.js", "function foo() { return 42; }");

    // Create a JsAst instance
    JsAst jsAst = new JsAst(sourceFile);

    // Call the getSourceFile method and check if it returns the expected SourceFile
    SourceFile returnedSourceFile = jsAst.getSourceFile();
    assertEquals(sourceFile, returnedSourceFile);
  }

  @Test
  public void testSetSourceFile() {
    // Create a sample SourceFile
    SourceFile sourceFile1 = new SourceFile("test1.js", "function foo() { return 42; }");
    SourceFile sourceFile2 = new SourceFile("test2.js", "function bar() { return 42; }");

    // Create a JsAst instance
    JsAst jsAst = new JsAst(sourceFile1);

    // Call the setSourceFile method with a different SourceFile and check if it changes the SourceFile
    jsAst.setSourceFile(sourceFile2);
    assertEquals(sourceFile2, jsAst.getSourceFile());
  }

}
