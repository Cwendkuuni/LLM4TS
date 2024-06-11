import static org.junit.Assert.*;
import com.google.javascript.jscomp.JsAst;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.AbstractCompiler;
import com.google.javascript.rhino.ParserConfig;

public class JsAstTest {

  @org.junit.Test
  public void testJsAstInstance() {
    // Test constructor
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    assertNotNull(jsAst);
  }

  @org.junit.Test
  public void testGetAstRoot() {
    // Test getting AST root
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(new SourceFile("test.js"));
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @org.junit.Test
  public void testClearAst() {
    // Test clearing AST
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(new SourceFile("test.js"));
    jsAst.getAstRoot(compiler); // Initialize AST
    jsAst.clearAst();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);
  }

  @org.junit.Test
  public void testParse() {
    // Test parsing function
    ParserConfig parserConfig = new ParserConfig();
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(new SourceFile("test.js"));
    jsAst.parse(compiler);
    assertTrue(jsAst.getAstRoot(compiler) != null);
  }
}