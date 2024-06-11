import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

  private AbstractCompiler compiler;
  private SourceFile sourceFile;
  private JsAst jsAst;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    sourceFile = new SourceFile("test.js", "test content");
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test(expected = NullPointerException.class)
  public void testGetAstRootNullCompiler() {
    jsAst.getAstRoot(null);
  }

  @Test
  public void testClearAst() {
    jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    Node newAstRoot = jsAst.getAstRoot(compiler);
    assertNotNull(newAstRoot);
    assertEquals(IR.script(), newAstRoot);
  }

  @Test(expected = IllegalStateException.class)
  public void testSetSourceFileDifferentFileName() {
    jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = new SourceFile("newFileName.js", "new content");
    jsAst.setSourceFile(newSourceFile);
  }

  @Test(expected = IOException.class)
  public void testParseIOException() {
    jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void report(JSError error) {
        // do nothing, let the test fail with IOException
      }
    };
    jsAst.parse(compiler);
  }

  @Test(expected = NullPointerException.class)
  public void testParseNullCompiler() {
    jsAst = new JsAst(sourceFile);
    jsAst.parse(null);
  }

  @Test
  public void testParseEmptyFile() {
    sourceFile.setCode("");
    jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertEquals(IR.script(), astRoot);
  }

  @Test
  public void testParseSimpleStatement() {
    sourceFile.setCode("let x = 1;");
    jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertEquals(IR.statementList(), astRoot.getFirstChild());
    assertEquals(IR.variableStatementList(), astRoot.getFirstChild().getFirstChild());
    assertEquals(IR.statementList(), astRoot.getFirstChild().getLastChild());
    assertEquals(IR.varDeclarationList(), astRoot.getFirstChild().getFirstChild().getLastChild());
    assertEquals(IR.variableDeclarationList(),  astRoot.getFirstChild().getFirstChild().getLastChild().getLastChild());
    assertEquals(IR.variableDeclarationList(), astRoot.getFirstChild().getFirstChild().getFirstChild().getLastChild().getLastChild());
    assertEquals(IR.variableDeclaration(), astRoot.getFirstChild().getFirstChild().getFirstChild().getFirstChild());
    assertEquals(IR.name("x"), astRoot.getFirstChild().getFirstChild().getFirstChild().getFirstChild().getLastChild());
    assertEquals(IR.assignment("=", IR.numericLiteral(1), IR.name("x")));
  }

  @Test
  public void testParseFunctionDeclaration() {
    sourceFile.setCode("function add(x, y) {return x + y;}");
    jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertEquals(IR.statementList(), astRoot.getFirstChild());
    assertEquals(IR.functionDeclaration(), astRoot.getFirstChild().getFirstChild());
    assertEquals(IR.name("add"), astRoot.getFirstChild().getFirstChild().getName());
    assertEquals(IR.parameterList(), astRoot.getFirstChild().getFirstChild().getParameterList());
    assertEquals(IR.parameterList().param(IR.name("x")), astRoot.getFirstChild().getFirstChild().getParameterList().getFirstChild());
    assertEquals(IR.parameterList().param(IR.name("y")), astRoot.getFirstChild().getFirstChild().getParameterList().getLastChild());
    assertEquals(IR.statementList(), astRoot.getFirstChild().getFirstChild().getLastChild());
    assertEquals(IR.returnStatement(), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild());
    assertEquals(IR.returnStatement().getExpression(), IR.binaryOperator(IR.op_PLUS, IR.name("x"), IR.name("y")));
  }

  @Test
  public void testParseCompoundStatement() {
    sourceFile.setCode("function add(x, y) {return x + y;}\n" +
        "let sum = add(1, 2);");
    jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertEquals(IR.statementList(), astRoot.getFirstChild());
    assertEquals(IR.functionDeclaration(), astRoot.getFirstChild().getFirstChild());
    // function body contains compound statement
    assertEquals(IR.statementList(), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild());
    assertEquals(IR.variableDeclarationList(), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getLastChild());
    assertEquals(IR.variableDeclarationList(), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getLastChild());
    assertEquals(IR.variableDeclaration(), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getFirstChild());
    assertEquals(IR.name("sum"), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getFirstChild().getName());
    assertEquals(IR.assignment("=", IR.name("sum"), IR.name("add")), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getFirstChild().getLastChild().getLastChild());
    assertEquals(IR.name("add"), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getFirstChild().getLastChild().getFirstChild());
    assertEquals(IR.callExpression(), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getFirstChild().getLastChild().getFirstChild());
    assertEquals(IR.name("add"), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getFirstChild().getLastChild().getFirstChild().getName());
    assertEquals(IR.argumentsList(), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getLastChild().getFirstChild().getLastChild().getLastChild());
    assertEquals(IR.argumentsList().argumentList(), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getLastChild().getFirstChild().getLastChild().getLastChild().getFirstChild());
    assertEquals(IR.numericLiteral(1), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getLastChild().getFirstChild().getLastChild().getLastChild().getFirstChild().getFirstChild());
    assertEquals(IR.numericLiteral(2), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getLastChild().getFirstChild().getLastChild().getLastChild().getLastChild().getFirstChild().getLastChild());
  }

  @Test
  public void testParseFunctionCall() {
    sourceFile.setCode("function add(x, y) {return x + y;}\n" +
        "let sum = add(1, 2);");
    jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertEquals(IR.statementList(), astRoot.getFirstChild());
    assertEquals(IR.functionDeclaration(), astRoot.getFirstChild().getFirstChild());
    // function body contains compound statement
    assertEquals(IR.statementList(), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild());
    assertEquals(IR.variableDeclarationList(), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getLastChild());
    assertEquals(IR.variableDeclarationList(), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getLastChild());
    assertEquals(IR.variableDeclaration(), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getFirstChild());
    assertEquals(IR.name("sum"), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getFirstChild().getName());
    assertEquals(IR.assignment("=", IR.name("sum"), IR.name("add")), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getFirstChild().getLastChild().getLastChild().getLastChild());
    assertEquals(IR.name("add"), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getFirstChild().getLastChild().getFirstChild().getName());
    assertEquals(IR.callExpression(), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild());
    assertEquals(IR.name("add"), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getFirstChild().getLastChild().getFirstChild().getName());
    assertEquals(IR.argumentsList(), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getFirstChild().getLastChild().getFirstChild().getLastChild().getLastChild());
    assertEquals(IR.argumentsList().argumentList(), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getFirstChild().getLastChild().getFirstChild().getLastChild().getLastChild().getFirstChild());
    assertEquals(IR.numericLiteral(1), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getLastChild().getFirstChild().getLastChild().getLastChild().getLastChild().getFirstChild().getFirstChild());
    assertEquals(IR.numericLiteral(2), astRoot.getFirstChild().getFirstChild().getLastChild().getFirstChild().getFirstChild().getLastChild().getFirstChild().getLastChild().getLastChild().getLastChild().getFirstChild().getLastChild());

    // function call in a statement
    assertEquals(IR.statementList(), astRoot.getFirstChild().getLastChild());
    assertEquals(IR.expressionStatement(), astRoot.getFirstChild().getLastChild().getFirstChild());
    assertEquals(IR.callExpression(), astRoot.getFirstChild().getLastChild().getFirstChild().getExpression());
    assertEquals(IR.name("add"), astRoot.getFirstChild().getLastChild().getFirstChild().getExpression().getName());
    assertEquals(IR.argumentsList(), astRoot.getFirstChild().getLastChild().getFirstChild().getExpression().getArguments());
    assertEquals(IR.argumentsList().argumentList(), astRoot.getFirstChild().getLastChild().getFirstChild().getExpression().getArguments().getFirstChild());
    assertEquals(IR.numericLiteral(1), astRoot.getFirstChild().getLastChild().getFirstChild().getExpression().getArguments().getFirstChild().getFirstChild());
    assertEquals(IR.numericLiteral(2), astRoot.getFirstChild().getLastChild().getFirstChild().getExpression().getArguments().getFirstChild().getLastChild());
  }
}