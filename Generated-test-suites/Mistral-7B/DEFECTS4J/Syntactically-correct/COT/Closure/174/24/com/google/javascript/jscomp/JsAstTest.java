import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public ErrorReporter getErrorReporter() {
        return new ErrorReporter() {
          @Override
          public void error(int line, String message, String file) {
            assertTrue(false);
          }
        };
      }

      @Override
      public ParserConfig getParserConfig() {
        return new ParserConfig();
      }
    };

    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.getAstRoot(new AbstractCompiler() {});
    jsAst.clearAst();
    assertNotNull(jsAst.getInputId());
  }

  @Test(expected = NullPointerException.class)
  public void testGetNullInputFile() {
    JsAst jsAst = new JsAst(null);
    jsAst.getAstRoot(new AbstractCompiler());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetInvalidSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.setSourceFile(new SourceFile("invalidName.js", "test content"));
  }

  @Test(expected = IOException.class)
  public void testParseIOException() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public ErrorReporter getErrorReporter() {
        return new ErrorReporter() {
          @Override
          public void error(int line, String message, String file) {
            throw new IOException("Test IO Exception");
          }
        };
      }
    };
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
  }

  @Test
  public void testParseSuccess() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public ErrorReporter getErrorReporter() {
        return new ErrorReporter() {};
      }

      @Override
      public ParserConfig getParserConfig() {
        return new ParserConfig();
      }
    };
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParseFail() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public ErrorReporter getErrorReporter() {
        return new ErrorReporter() {
          @Override
          public void error(int line, String message, String file) {
            assertTrue(true);
          }
        };
      }

      @Override
      public ParserConfig getParserConfig() {
        return new ParserConfig();
      }
    };
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParseNoAst() {
    SourceFile sourceFile = new SourceFile("test.js", "test content");
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public ErrorReporter getErrorReporter() {
        return new ErrorReporter() {};
      }

      @Override
      public ParserConfig getParserConfig() {
        return new ParserConfig();
      }
    };
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
    assertTrue(root instanceof Node);
    assertTrue(root.getClass().getName().equals("com.google.javascript.rhino.Node"));
  }
}