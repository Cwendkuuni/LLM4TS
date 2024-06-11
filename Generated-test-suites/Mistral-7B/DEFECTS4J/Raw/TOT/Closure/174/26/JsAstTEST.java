import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static com.google.javascript.jscomp.testing.TopLevelTestFixture.createCompiler;
import static com.google.javascript.jscomp.testing.TopLevelTestFixture.checkAst;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testing.TopLevelTestFixture;

public class JsAstTest {

  @org.junit.Test
  public void testGenerateAst() {
    SourceFile sourceFile = new SourceFile("test.js", "test");
    AbstractCompiler compiler = createCompiler();

    JsAst ast = new JsAst(sourceFile);
    Node root = ast.getAstRoot(compiler);

    assertNotNull(root);
    checkAst(root);

    ast.clearAst();
    assertNull(ast.getAstRoot(compiler));

    // Error test
    sourceFile.setCode("syntax error");
    ast = new JsAst(sourceFile);
    assertNull(ast.getAstRoot(compiler));
  }
}