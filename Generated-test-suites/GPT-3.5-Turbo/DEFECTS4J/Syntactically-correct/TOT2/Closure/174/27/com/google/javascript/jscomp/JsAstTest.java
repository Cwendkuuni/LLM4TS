import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.InputId;
import java.io.IOException;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(new Compiler());
    assertNotNull(root);
    assertEquals("SCRIPT", root.getToken());
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(new Compiler()));
  }

  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile1 = new SourceFile("test1.js", "var x = 5;");
    SourceFile sourceFile2 = new SourceFile("test2.js", "var y = 10;");
    JsAst jsAst = new JsAst(sourceFile1);
    jsAst.setSourceFile(sourceFile2);
    assertEquals(sourceFile2, jsAst.getSourceFile());
  }

  // Add more test cases for other methods as needed

}