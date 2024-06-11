import static org.junit.Assert.*;
import static com.google.javascript.jscomp.testing.CodeErrors.expectErrors;
import static com.google.javascript.jscomp.testing.CodeErrors.expectWarning;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.jscomp.test_util.AbstractCompilerTest;
import com.google.javascript.jscomp.test_util.CompilationTestBuilder;
import com.google.javascript.rhino.SourceFile;
import com.google.javascript.rhino.token.SourceID;

@RunWith(JUnit4.class)
public class JsAstTest extends AbstractCompilerTest {

  @Test
  public void testJsAst() {
    CompilationTestBuilder cb = newCompilationTestBuilder();
    SourceFile sourceFile = cb.addSourceFile("Test.js", "function test() {\n" +
        "  return 1;\n" +
        "}");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = getDefaultCompiler();

    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertEquals(SCRIPT, astRoot.getType());

    FunctionDeclaration functionDeclaration = (FunctionDeclaration) astRoot.getFirstChild();
    assertEquals("test", functionDeclaration.getName().getName());
    FunctionBody functionBody = functionDeclaration.getBody();
    assertEquals(RETURN_STATEMENT, functionBody.getType());
    ReturnStatement returnStatement = (ReturnStatement) functionBody.getFirstChild();
    assertEquals(NumberLiteral.class, returnStatement.getValue().getClass());
    assertEquals(1.0, ((NumberLiteral) returnStatement.getValue()).getValue());

    jsAst.clearAst();
    astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertNull(astRoot.getFirstChild());

    cb.expectErrorsInOrder(JSError.make(JSError.PARSE_ERROR, SourceID.sourceIdForInputId(jsAst.getInputId()), "Unexpected identifier 'test'. Expected ';'."));
    cb.addSourceFile("Test2.js", "function test() {\n" +
        "  test // expected error\n" +
        "}");
    jsAst = new JsAst(sourceFile);
    compiler = getDefaultCompiler();
    jsAst.getAstRoot(compiler);
    astRoot = compiler.getOldParseTree(sourceFile.getName());
    assertNotNull(astRoot);
    assertEquals(SCRIPT, astRoot.getType());
  }

  @Test
  public void testParseError() {
    CompilationTestBuilder cb = newCompilationTestBuilder();
    SourceFile sourceFile = cb.addSourceFile("Test.js", "function test() {\n" +
        "  test // error\n" +
        "}");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = getDefaultCompiler();

    expectErrors(compiler, JSError.make(JSError.PARSE_ERROR, SourceID.sourceIdForInputId(jsAst.getInputId()), "Unexpected identifier 'test'. Expected ';'."));
    jsAst.getAstRoot(compiler);
    assertNull(jsAst.getAstRoot(compiler));

    SourceFile sourceFile2 = cb.addSourceFile("Test2.js", "function test() {\n" +
        "  test // warning\n" +
        "}");
    JsAst jsAst2 = new JsAst(sourceFile2);
    compiler = getDefaultCompiler();
    compiler.setOptions(new CompilerOptions());
    compiler.setCompilerFlag("--warn_unused_identifiers", false);

    expectWarning(compiler, JSError.make(JSError.WARNING, SourceID.sourceIdForInputId(jsAst2.getInputId()), "Identifier 'test' is unused.", 1, 7));
    jsAst2.getAstRoot(compiler);
  }

  @Test
  public void testClearAst() {
    CompilationTestBuilder cb = newCompilationTestBuilder();
    SourceFile sourceFile = cb.addSourceFile("Test.js", "function test() {\n" +
        "  return 1;\n" +
        "}");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = getDefaultCompiler();

    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    jsAst.clearAst();
    astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);
  }

}