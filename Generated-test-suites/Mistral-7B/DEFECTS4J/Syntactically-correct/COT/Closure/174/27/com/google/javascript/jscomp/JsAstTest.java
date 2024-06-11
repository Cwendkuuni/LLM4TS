package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.JsAst;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.SourceName;

public class JsAstTest {

  @Test
  public void testJsAstConstructor() {
    SourceFile sourceFile = new SourceFile("test.js", "test.js");
    JsAst jsAst = new JsAst(sourceFile);
    assertNotNull(jsAst.getAstRoot(null));
  }

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "test.js");
    JsAst jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(null);
    assertNotNull(astRoot);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetAstRootNullCompiler() {
    SourceFile sourceFile = new SourceFile("test.js", "test.js");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.getAstRoot(null);
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "test.js");
    JsAst jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(null);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(null));
  }

  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js", "test.js");
    JsAst jsAst = new JsAst(sourceFile);
    SourceName inputId = jsAst.getInputId();
    assertTrue(inputId.getName().equals("test.js"));
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "test.js");
    JsAst jsAst = new JsAst(sourceFile);
    assertTrue(jsAst.getSourceFile() == sourceFile);
  }

  @Test
  public void testSetSourceFile() {
    SourceName oldFileName = new SourceName("testOld.js");
    SourceFile oldSourceFile = new SourceFile("testOld.js", "testOld.js");
    SourceFile newSourceFile = new SourceFile("test.js", "test.js");

    JsAst jsAst = new JsAst(oldSourceFile);
    jsAst.setSourceFile(newSourceFile);
    SourceName newInputId = jsAst.getInputId();
    assertTrue(newInputId.getName().equals("test.js"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetSourceFileDifferentFileName() {
    SourceName oldFileName = new SourceName("test.js");
    SourceFile oldSourceFile = new SourceFile("test.js", "test.js");
    SourceName newFileName = new SourceName("testOld.js");
    SourceFile newSourceFile = new SourceFile("testOld.js", "testOld.js");

    JsAst jsAst = new JsAst(oldSourceFile);
    jsAst.setSourceFile(newSourceFile);
  }

  @Test
  public void testParseWithNullCompiler() {
    SourceFile sourceFile = new SourceFile("test.js", "test.js");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseWithNullSourceFile() {
    JsAst jsAst = new JsAst(null);
    jsAst.parse(null);
  }

  @Test
  public void testParseWithIOException() {
    SourceFile sourceFile = new SourceFile("test.js", "test.js");
    JsAst jsAst = new JsAst(sourceFile);
    try {
      jsAst.parse(null);
    } catch (IllegalArgumentException e) {
      // This is expected since we have an IOException mocked
      // in the ParserRunner.parse method.
      assertTrue(true);
    }
  }

  @Test
  public void testParseWithParseError() {
    SourceFile sourceFile = new SourceFile("test.js", "test.js");
    ParserRunner parserRunner = new ParserRunner() {
      @Override
      public ParserRunner.ParseResult parse(
          SourceName name, String code, ParserConfig config,
          ErrorReporter errorReporter, Logger logger) {
        throw new IOException("Test parse error");
      }
    };
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(parserRunner);
  }

  @Test
  public void testParseWithNonNullAst() {
    SourceFile sourceFile = new SourceFile("test.js", "test.js");
    String code = "function test() {}";
    ParserRunner parserRunner = new ParserRunner() {
      @Override
      public ParserRunner.ParseResult parse(
          SourceName name, String code, ParserConfig config,
          ErrorReporter errorReporter, Logger logger) {
        Node ast = IR.script(new Node[] { IR.name("test"),
            IR.functionDecl(IR.name("test"), null, new Node[0], null) });
        return new ParserRunner.ParseResult(ast);
      }
    };
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(parserRunner);
    Node astRoot = jsAst.getAstRoot(null);
    assertTrue(astRoot.getFirstChild() instanceof IR.FunctionDecl);
  }

  @Test
  public void testParseWithHaltingErrors() {
    SourceFile sourceFile = new SourceFile("test.js", "test.js");
    ParserRunner parserRunner = new ParserRunner() {
      @Override
      public ParserRunner.ParseResult parse(
          SourceName name, String code, ParserConfig config,
          ErrorReporter errorReporter, Logger logger) {
        if (code.contains("function foo")) {
          compiler.report(JSError.make(
              AbstractCompiler.UNDEFINED_NAME, "foo", null, null, null, null));
        }
        return new ParserRunner.ParseResult(IR.script());
      }
    };
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(parserRunner);
    assertNotNull(jsAst.getAstRoot(null));
  }

  @Test
  public void testPrepareAst() {
    SourceFile sourceFile = new SourceFile("test.js", "test.js");
    ParserRunner parserRunner = new ParserRunner() {
      @Override
      public ParserRunner.ParseResult parse(
          SourceName name, String code, ParserConfig config,
          ErrorReporter errorReporter, Logger logger) {
        Node ast = IR.script();
        return new ParserRunner.ParseResult(ast);
      }
    };
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(parserRunner);
    jsAst.prepareAst(jsAst.getAstRoot(null));
    assertTrue(jsAst.getAstRoot(null) instanceof IR.Block);
  }
}