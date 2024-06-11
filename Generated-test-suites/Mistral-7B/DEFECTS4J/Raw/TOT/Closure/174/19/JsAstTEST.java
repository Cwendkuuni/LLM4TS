import static org.junit.Assert.*;
import static com.google.javascript.jscomp.parser.test.ParserTests.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class JsAstTest {

  @Test
  public void testValidJavaScriptFile() {
    SourceFile sourceFile = new SourceFile("test.js", "function testFunction() { return 1; }");
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst ast = new JsAst(sourceFile);
    Node root = ast.getAstRoot(compiler);

    // Check if the parse was successful
    assertNotNull(root);
    assertEquals(NodeType.SCRIPT, root.getType());

    // Check if the parse tree matches the expected output
    Node expectedRoot = parseAndReturnRoot("function testFunction() { return 1; }");
    assertTreeEquals(expectedRoot, root);
  }

  @Test
  public void testInvalidJavaScriptFile() {
    SourceFile sourceFile = new SourceFile("test.js", "function testFunction() { return 1; }/**/");
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst ast = new JsAst(sourceFile);

    try {
      ast.getAstRoot(compiler);
      fail("Expected a parse error");
    } catch (AssertionError e) {
      // Expected parsing error, so no need to do anything
    }
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "function testFunction() { return 1; }");
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst ast = new JsAst(sourceFile);
    Node root = ast.getAstRoot(compiler);

    // Clear the ast and check if the root is null
    ast.clearAst();
    assertNull(ast.getAstRoot(compiler));
  }
}