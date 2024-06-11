import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.*;
import java.io.IOException;

public class JsAstTest {
  
  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "var a = 10;");
    JsAst jsAst = new JsAst(sourceFile);
    
    AbstractCompiler compiler = new Compiler();
    Node root = jsAst.getAstRoot(compiler);
    
    assertNotNull(root);
    assertEquals(Node.SCRIPT, root.getType());
    assertNotNull(root.getFirstChild());
    assertEquals(Node.VAR, root.getFirstChild().getType());
    assertNotNull(root.getFirstChild().getFirstChild());
    assertEquals(Node.NAME, root.getFirstChild().getFirstChild().getType());
    assertEquals("a", root.getFirstChild().getFirstChild().getString());
    assertNotNull(root.getFirstChild().getLastChild());
    assertEquals(Node.NUMBER, root.getFirstChild().getLastChild().getType());
    assertEquals("10", root.getFirstChild().getLastChild().getString());
  }
  
  @Test
  public void testParse() {
    SourceFile sourceFile = new SourceFile("test.js", "function add(a, b) { return a + b; }");
    JsAst jsAst = new JsAst(sourceFile);
    
    AbstractCompiler compiler = new Compiler();
    jsAst.parse(compiler);
    
    assertNotNull(jsAst.root);
    assertEquals(Node.SCRIPT, jsAst.root.getType());
    assertNotNull(jsAst.root.getFirstChild());
    assertEquals(Node.FUNCTION, jsAst.root.getFirstChild().getType());
    assertNotNull(jsAst.root.getFirstChild().getFirstChild());
    assertEquals(Node.NAME, jsAst.root.getFirstChild().getFirstChild().getType());
    assertEquals("add", jsAst.root.getFirstChild().getFirstChild().getString());
    assertNotNull(jsAst.root.getFirstChild().getLastChild());
    assertEquals(Node.RETURN, jsAst.root.getFirstChild().getLastChild().getType());
    assertNotNull(jsAst.root.getFirstChild().getLastChild().getFirstChild());
    assertEquals(Node.ADD, jsAst.root.getFirstChild().getLastChild().getFirstChild().getType());
    assertNotNull(jsAst.root.getFirstChild().getLastChild().getFirstChild().getFirstChild());
    assertEquals(Node.NAME, jsAst.root.getFirstChild().getLastChild().getFirstChild().getFirstChild().getType());
    assertEquals("a", jsAst.root.getFirstChild().getLastChild().getFirstChild().getFirstChild().getString());
    assertNotNull(jsAst.root.getFirstChild().getLastChild().getFirstChild().getLastChild());
    assertEquals(Node.NAME, jsAst.root.getFirstChild().getLastChild().getFirstChild().getLastChild().getType());
    assertEquals("b", jsAst.root.getFirstChild().getLastChild().getFirstChild().getLastChild().getString());
  }
  
  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "console.log('Hello, World!');");
    JsAst jsAst = new JsAst(sourceFile);
    
    AbstractCompiler compiler = new Compiler();
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
    
    jsAst.clearAst();
    assertNull(jsAst.root);
  }
  
  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js", "var a = 10;");
    JsAst jsAst = new JsAst(sourceFile);
    
    InputId inputId = jsAst.getInputId();
    assertNotNull(inputId);
    assertEquals("test.js", inputId.getIdName());
  }
  
  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "var a = 10;");
    JsAst jsAst = new JsAst(sourceFile);
    
    SourceFile returnedSourceFile = jsAst.getSourceFile();
    assertNotNull(returnedSourceFile);
    assertEquals(sourceFile, returnedSourceFile);
  }
  
  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "var a = 10;");
    JsAst jsAst = new JsAst(sourceFile);
    
    SourceFile newSourceFile = new SourceFile("test_new.js", "var b = 20;");
    jsAst.setSourceFile(newSourceFile);
    SourceFile returnedSourceFile = jsAst.getSourceFile();
    
    assertNotNull(returnedSourceFile);
    assertEquals(newSourceFile, returnedSourceFile);
  }
  
  @Test
  public void testParseWithIOException() {
    SourceFile sourceFile = new SourceFile("test.js", "var a = 10;");
    JsAst jsAst = new JsAst(sourceFile);
    
    AbstractCompiler compiler = new Compiler();
    
    try {
      jsAst.parse(compiler);
    } catch (IOException e) {
      fail("IOException occurred");
    }
    
    assertNotNull(jsAst.root);
    assertEquals(Node.SCRIPT, jsAst.root.getType());
    assertNotNull(jsAst.root.getFirstChild());
    assertEquals(Node.VAR, jsAst.root.getFirstChild().getType());
    assertNotNull(jsAst.root.getFirstChild().getFirstChild());
    assertEquals(Node.NAME, jsAst.root.getFirstChild().getFirstChild().getType());
    assertEquals("a", jsAst.root.getFirstChild().getFirstChild().getString());
    assertNotNull(jsAst.root.getFirstChild().getLastChild());
    assertEquals(Node.NUMBER, jsAst.root.getFirstChild().getLastChild().getType());
    assertEquals("10", jsAst.root.getFirstChild().getLastChild().getString());
  }
  
  @Test
  public void testParseWithParseException() {
    SourceFile sourceFile = new SourceFile("test.js", "function add(a, b) { return a + }");
    JsAst jsAst = new JsAst(sourceFile);
    
    AbstractCompiler compiler = new Compiler();
    jsAst.parse(compiler);
    
    assertEquals(Node.SCRIPT, jsAst.root.getType());
    assertEquals(Node.BLOCK, jsAst.root.getFirstChild().getType());
    assertEquals(Node.VAR, jsAst.root.getFirstChild().getFirstChild().getType());
    assertEquals(Node.NAME, jsAst.root.getFirstChild().getFirstChild().getFirstChild().getType());
    assertEquals("add", jsAst.root.getFirstChild().getFirstChild().getFirstChild().getString());
    assertEquals(Node.THROW, jsAst.root.getLastChild().getType());
    assertEquals(Node.NAME, jsAst.root.getLastChild().getFirstChild().getType());
    assertEquals("encountered unexpected token", jsAst.root.getLastChild().getFirstChild().getString());
  }
  
  @Test
  public void testPrepareAst() {
    SourceFile sourceFile = new SourceFile("test.js", "var a = 10;");
    JsAst jsAst = new JsAst(sourceFile);
    
    AbstractCompiler compiler = new Compiler();
    jsAst.parse(compiler);
    
    jsAst.prepareAst(jsAst.root);
    assertNotNull(jsAst.root.getStaticSourceFile());
  }

}