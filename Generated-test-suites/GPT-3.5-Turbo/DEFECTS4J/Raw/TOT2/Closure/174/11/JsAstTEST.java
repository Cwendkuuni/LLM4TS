import static org.junit.Assert.*;
import org.junit.Test;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    // Test typical case
    // Create a dummy SourceFile for testing
    SourceFile dummySourceFile = new SourceFile("dummy.js", "var x = 10;");
    JsAst jsAst = new JsAst(dummySourceFile);
    Node rootNode = jsAst.getAstRoot(new DummyCompiler());
    assertNotNull(rootNode);

    // Test edge case - SourceFile with empty code
    SourceFile emptySourceFile = new SourceFile("empty.js", "");
    JsAst emptyJsAst = new JsAst(emptySourceFile);
    Node emptyRootNode = emptyJsAst.getAstRoot(new DummyCompiler());
    assertNotNull(emptyRootNode);
  }

  @Test
  public void testClearAst() {
    // Test typical case
    SourceFile dummySourceFile = new SourceFile("dummy.js", "var x = 10;");
    JsAst jsAst = new JsAst(dummySourceFile);
    jsAst.getAstRoot(new DummyCompiler());
    jsAst.clearAst();
    assertNull(jsAst.getRoot());

    // Test edge case - Calling clearAst on JsAst instance with no root
    SourceFile emptySourceFile = new SourceFile("empty.js", "");
    JsAst emptyJsAst = new JsAst(emptySourceFile);
    emptyJsAst.clearAst(); // Should not throw any exceptions
  }

  @Test
  public void testGetInputId() {
    // Test typical case
    SourceFile dummySourceFile = new SourceFile("dummy.js", "var x = 10;");
    JsAst jsAst = new JsAst(dummySourceFile);
    InputId inputId = jsAst.getInputId();
    assertNotNull(inputId);

    // Test edge case - Getting InputId when JsAst is initialized with null SourceFile
    JsAst nullSourceFileJsAst = new JsAst(null);
    InputId nullSourceInputId = nullSourceFileJsAst.getInputId();
    assertNull(nullSourceInputId);
  }

  @Test
  public void testGetSourceFile() {
    // Test typical case
    SourceFile dummySourceFile = new SourceFile("dummy.js", "var x = 10;");
    JsAst jsAst = new JsAst(dummySourceFile);
    SourceFile sourceFile = jsAst.getSourceFile();
    assertNotNull(sourceFile);

    // Test edge case - JsAst instance with null SourceFile
    JsAst nullSourceFileJsAst = new JsAst(null);
    SourceFile nullSourceFile = nullSourceFileJsAst.getSourceFile();
    assertNull(nullSourceFile);
  }

  @Test
  public void testSetSourceFile() {
    // Test typical case
    SourceFile dummySourceFile = new SourceFile("dummy.js", "var x = 10;");
    JsAst jsAst = new JsAst(dummySourceFile);
    SourceFile newSourceFile = new SourceFile("newdummy.js", "var y = 20;");
    jsAst.setSourceFile(newSourceFile);
    assertEquals(newSourceFile, jsAst.getSourceFile());

    // Test edge case - Setting SourceFile with different name
    JsAst differentNameJsAst = new JsAst(dummySourceFile);
    SourceFile differentFileNameSource = new SourceFile("different.js", "var z = 30;");
    assertThrows(IllegalStateException.class, () -> differentNameJsAst.setSourceFile(differentFileNameSource));
  }

  // Additional tests for exception handling scenarios can be added here

}

class DummyCompiler extends AbstractCompiler {
  @Override
  public void report(JSError error) {
    // Dummy implementation
  }
}