import static org.junit.Assert.*;
import static com.google.javascript.jscomp.JSError.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testing.ProcessCompilation;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class JsAstTest {

  @Test
  public void testJsAst_ValidFile() {
    SourceFile sourceFile = new SourceFile("test.js", "function test() { print('Hello World'); }");
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(compiler);

    ProcessCompilation.process(compiler, root, null);
    assertEquals(0, compiler.getDiagnostics().size());
    assertTrue(root instanceof Node.Block);
  }

  @Test
  public void testJsAst_EmptyFile() {
    SourceFile sourceFile = new SourceFile("test.js", "");
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(compiler);

    ProcessCompilation.process(compiler, root, null);
    assertTrue(root instanceof Node.Script);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testJsAst_InvalidFile() {
    SourceFile sourceFile = new SourceFile("test.js", "function test() { print('Hello World'); } print('Hello');");
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(compiler);

    ProcessCompilation.process(compiler, root, null);
    assertEquals(1, compiler.getDiagnostics().size());
    assertTrue(compiler.getDiagnostics().get(0).getMessage().contains("Unexpected token 'print'"));
    assertNull(root);
  }

  @Test
  public void testJsAst_IOException() {
    SourceFile sourceFile = new SourceFile("test.js", null);
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(sourceFile);
    compiler.setErrorReporter(error -> { throw new IOException("Test IOException"); });

    jsAst.parse(compiler);
    assertEquals(1, compiler.getDiagnostics().size());
    assertTrue(compiler.getDiagnostics().get(0).getMessage().contains("Test IOException"));
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testJsAst_OldParseTree() {
    SourceFile sourceFile = new SourceFile("test.js", "function test() { print('Hello World'); }");
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(sourceFile);
    Preconditions.checkState(jsAst.getSourceFile() != null, "SourceFile should not be null.");
    Preconditions.checkState(jsAst.getInputId() != null, "InputId should not be null.");

    Node oldAst = IR.script();
    jsAst.setOldParseTree("test.js", oldAst);
    Node root = jsAst.getAstRoot(compiler);

    ProcessCompilation.process(compiler, root, null);
    assertEquals(1, compiler.getDiagnostics().size());
    assertTrue(compiler.getDiagnostics().get(0).getMessage().contains("SourceFile 'test.js' has already been parsed."));
    assertEquals(oldAst, root);
  }

  @Test
  public void testJsAst_NullSourceFile() {
    SourceFile sourceFile = null;
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(sourceFile);

    try {
      Node root = jsAst.getAstRoot(compiler);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      assertTrue(e.getMessage().contains("SourceFile must not be null."));
    }
  }
}