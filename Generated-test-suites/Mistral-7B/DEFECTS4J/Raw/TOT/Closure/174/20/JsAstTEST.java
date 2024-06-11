import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ParsingTestUtil.*;

import com.google.javascript.JsCompilationTest;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class JsAstTest {

  @Test
  public void testJsAstParsing() throws Exception {
    String sourceCode = "function foo() { return 42; }";

    SourceFile sourceFile = new SourceFile("test.js", sourceCode);
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst ast = new JsAst(sourceFile);

    Node astRoot = ast.getAstRoot(compiler);
    assertEquals("Unexpected AST root type.", IR.functionDeclaration(), astRoot);

    Node functionNode = assertFunctionNode(astRoot, "foo");
    Node functionBody = functionNode.getBody();
    assertEquals("Unexpected function body type.", IR.returnStatement(), functionBody);
    Node returnStatement = functionBody.getFirstChild();
    assertEquals("Unexpected return statement value.", IR.intLiteral(42), returnStatement);
  }

  @Test
  public void testJsAstErrorHandling() throws Exception {
    String sourceCode = "function foo() { throw new Error('Test error.'); }";

    SourceFile sourceFile = new SourceFile("test.js", sourceCode);
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst ast = new JsAst(sourceFile);

    Node astRoot = ast.getAstRoot(compiler);
    assertEquals("Unexpected AST root type.", IR.script(), astRoot);
  }

  @Test
  public void testJsAstClearAst() throws Exception {
    String sourceCode = "function foo() { return 42; }";

    SourceFile sourceFile = new SourceFile("test.js", sourceCode);
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst ast = new JsAst(sourceFile);

    Node astRoot = ast.getAstRoot(compiler);
    assertEquals("Unexpected AST root type.", IR.functionDeclaration(), astRoot);

    ast.clearAst();

    astRoot = ast.getAstRoot(compiler);
    assertEquals("AST should be null after clearAst() call.", null, astRoot);
  }
}