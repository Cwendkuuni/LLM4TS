import static org.junit.Assert.*;
import static com.google.javascript.rhino.Nodeitaire.asText;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testing.TestSourceFile;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

@RunWith(JUnit4.class)
public class JsAstTest {

  private AbstractCompiler compiler;
  private JsAst ast;
  private SourceFile sourceFile;
  private ErrorReporter errorReporter;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {
      @Override
      public CompilerOptions getOptions() {
        return new CompilerOptions();
      }

      @Override
      public ParserConfig getParserConfig() {
        return new ParserConfig();
      }

      @Override
      public void setErrorReporter(ErrorReporter reporter) {
        errorReporter = reporter;
      }

      @Override
      public boolean hasHaltingErrors() {
        return false;
      }
    };

    sourceFile = new TestSourceFile("TestJsAst.java", "print('Hello World');");
    ast = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    Node astRoot = ast.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertEquals("script", asText(astRoot.getClass()));
  }

  @Test
  public void testClearAst() {
    Node astRoot = ast.getAstRoot(compiler);
    ast.clearAst();
    Node newAstRoot = ast.getAstRoot(compiler);
    assertNull(newAstRoot);
  }

  @Test
  public void testGetInputId() {
    InputId inputId = ast.getInputId();
    assertEquals("TestJsAst.java", inputId.getName());
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = ast.getSourceFile();
    assertEquals("TestJsAst.java", sourceFile.getName());
  }

  @Test
  public void testSetSourceFile() {
    SourceName oldSourceName = sourceFile.getName();
    SourceFile newSourceFile = new TestSourceFile("NewTestJsAst.java", "print('New Hello World');");
    ast.setSourceFile(newSourceFile);
    SourceFile newSourceFileAfterSet = ast.getSourceFile();
    assertEquals("NewTestJsAst.java", newSourceFileAfterSet.getName());
    Preconditions.checkState(newSourceFileAfterSet != oldSourceFile);
  }

  @Test(expected = IllegalStateException.class)
  public void testSetSourceFileWithSameName() {
    SourceName oldSourceName = sourceFile.getName();
    ast.setSourceFile(sourceFile);
  }

  @Test
  public void testParse() {
    Node astRoot = ast.getAstRoot(compiler);
    assertNotNull(astRoot);

    ast.parse(compiler);
    Node newAstRoot = ast.getAstRoot(compiler);
    assertSame(astRoot, newAstRoot);
  }

  @Test(expected = IOException.class)
  public void testParseWithIOException() {
    compiler.setErrorReporter(new ErrorReporter() {
      @Override
      public void error(String message, Object... args) {}

      @Override
      public void fatal(String message, Object... args) throws IOException {
        throw new IOException("IOException Simulation");
      }
    });

    ast.parse(compiler);
  }

  @Test
  public void testParseWithErrors() {
    String invalidCode = "print('Hello World')"; // Misspelled print
    sourceFile = new TestSourceFile("TestJsAstWithErrors.java", invalidCode);
    ast = new JsAst(sourceFile);
    Node astRoot = ast.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertTrue(compiler.hasHaltingErrors());
  }
}