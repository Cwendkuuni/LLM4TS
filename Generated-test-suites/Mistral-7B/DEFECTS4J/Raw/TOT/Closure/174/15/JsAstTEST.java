import static org.junit.Assert.*;
import static com.google.javascript.jscomp.JsAst.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;

public class JsAstTest {

  @org.junit.Test
  public void testJsAstCreation() {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    assertNotNull(jsAst);
    assertEquals("test.js", jsAst.getInputId().getName());
  }

  @org.junit.Test
  public void testJsAstParseSuccess() {
    Compiler compiler = new Compiler();
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);

    String code = "function testFunction() {\n" +
            "  var x = 1;\n" +
            "  return x;\n" +
            "}";

    sourceFile.setCode(code);
    jsAst.parse(compiler);

    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
    assertEquals(Node.FUNCTION_DECL, root.getType());
    FunctionDeclaration funcDecl = (FunctionDeclaration) root;
    assertEquals("testFunction", funcDecl.getName());
  }

  @org.junit.Test
  public void testJsAstParseFailure() {
    Compiler compiler = new Compiler();
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);

    String code = "function testFunction() {\n" +
            "  var x = 1;\n" +
            "} SyntaxError";

    sourceFile.setCode(code);
    jsAst.parse(compiler);

    assertNull(jsAst.getAstRoot(compiler));
  }

  @org.junit.Test
  public void testJsAstClearAst() {
    Compiler compiler = new Compiler();
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);

    String code = "function testFunction() {\n" +
            "  var x = 1;\n" +
            "  return x;\n" +
            "}";

    sourceFile.setCode(code);
    jsAst.parse(compiler);

    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);

    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }
}