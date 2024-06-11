import static org.junit.Assert.*;
import static com.google.javascript.jscomp.parser.test.ParseUtils.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

import org.junit.Before;
import org.junit.Test;

public class JsAstTest {
  private AbstractCompiler compiler;
  private SourceFile sourceFile;

  @Before
  public void setUp() {
    compiler = new CompilerOptions();
    sourceFile = new SourceFile("Test.js", "function testFunction() { return 1; }");
  }

  @Test
  public void testParseSuccess() {
    JsAst ast = new JsAst(sourceFile);
    AbstractCompiler compiler = new CompilerOptions();
    Node root = ast.getAstRoot(compiler);
    assertEquals("function testFunction() { return 1; }", sourceFile.getCode());
    assertEquals(root.getClass(), IR.FunctionDeclaration.class);
    assertEquals(root.getChild(0).getClass(), IR.Name.class);
    assertEquals(root.getChild(1).getClass(), IR.Identifier.class);
    assertEquals(root.getChild(1).getValue(), "testFunction");
    assertEquals(root.getChild(2).getClass(), IR.Punctuation.class);
    assertEquals(root.getChild(2).getValue(), "{");
    assertEquals(root.getChild(3).getClass(), IR.StatementList.class);
    assertEquals(root.getChild(3).getChildCount(), 1);
    assertEquals(root.getChild(4).getClass(), IR.ReturnStatement.class);
    assertEquals(root.getChild(4).getChild(0).getClass(), IR.NumericLiteral.class);
    assertEquals(root.getChild(4).getChild(0).getValue(), 1);
  }

  @Test
  public void testParseFailure() {
    JsAst ast = new JsAst(sourceFile);
    AbstractCompiler compiler = new CompilerOptions();
    compiler.setOptions(new CompilerOptions().withWarningLevel(CompilerOptions.WarningLevel.ERROR));
    Node root = ast.getAstRoot(compiler);
    assertNull(root);
    assertEquals(compiler.getDiagnostics().size(), 1);
    JSError error = compiler.getDiagnostics().get(0);
    assertEquals(error.getCode(), AbstractCompiler.PARSE_ERROR);
    assertEquals(error.getSourceName().getName(), "Test.js");
    assertEquals(error.getMessage(), "Syntax error");
  }

  @Test
  public void testClearAst() {
    JsAst ast = new JsAst(sourceFile);
    AbstractCompiler compiler = new CompilerOptions();
    Node root = ast.getAstRoot(compiler);
    assertEquals(root != null, true);
    ast.clearAst();
    assertNull(ast.getAstRoot(compiler));
  }

  @Test
  public void testSetSourceFile() {
    JsAst ast = new JsAst(sourceFile);
    AbstractCompiler compiler = new CompilerOptions();
    Node root = ast.getAstRoot(compiler);
    assertEquals(root != null, true);
    sourceFile.setCode("function newFunction() { return 2; }");
    ast.setSourceFile(sourceFile);
    Node newRoot = ast.getAstRoot(compiler);
    assertEquals(root.getClass(), IR.FunctionDeclaration.class);
    assertEquals(newRoot.getClass(), IR.FunctionDeclaration.class);
    assertEquals(newRoot.getChild(0).getValue(), "newFunction");
  }
}