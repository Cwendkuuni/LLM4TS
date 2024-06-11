import static org.junit.Assert.*;
import static com.google.javascript.jscomp.JsAst.*;

import org.junit.Test;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    // Given: A SourceFile instance
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);

    // When: Calling getAstRoot method
    Node root = jsAst.getAstRoot(new StaticCompiler());

    // Then: The root of the AST should not be null
    assertNotNull(root);
  }

  @Test
  public void testClearAst() {
    // Given: A SourceFile instance and a JsAst instance
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);

    // When: Calling clearAst method
    jsAst.clearAst();

    // Then: The root of the AST should be null
    assertNull(jsAst.getAstRoot(new StaticCompiler()).getFirstChild());
  }

  @Test(expected = NullPointerException.class)
  public void testGetAstRootWithNullCompiler() {
    // Given: A SourceFile instance and a JsAst instance
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);

    // When: Calling getAstRoot method with a null AbstractCompiler instance
    jsAst.getAstRoot(null);

    // Then: A NullPointerException should be thrown
  }

  // Add edge cases and exception handling scenarios here
}