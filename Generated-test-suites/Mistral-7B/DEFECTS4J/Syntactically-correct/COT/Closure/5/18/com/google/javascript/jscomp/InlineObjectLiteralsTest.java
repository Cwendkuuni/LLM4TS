

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestCase;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest extends CompilerPassTestCase {

  public InlineObjectLiteralsTest() {
    super();
  }

  @Override
  protected CompilerPass getPass() {
    return new InlineObjectLiterals(compiler, safeNameIdSupplier);
  }

  /**
   * Test the basic functionality of InlineObjectLiterals by creating a simple
   * object literal and ensuring that it gets inlined correctly.
   */
  public void testBasicFunctionality() {
    String input = "{\n" +
        "  a: 'one',\n" +
        "  b: 2,\n" +
        "  c: function() {}\n" +
        "}";

    Node root = parseJsCode("(function() {" + input + "})()");
    getPass().process(externs, root);

    Scope scope = root.getLastChild();
    Var objVar = scope.getVar("obj");
    assertTrue(objVar.isDef());
    assertFalse(objVar.isExtern());

    Node obj = objVar.getInitializer();
    assertTrue(obj.isObjectLit());

    Map<String, Node> properties = Maps.newHashMap();
    for (Node child = obj.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isProp()) {
        String key = child.getString();
        Node value = child.getFirstChild();
        properties.put(key, value);
      }
    }

    Node a = properties.get("a");
    assertTrue(a.isString());
    assertEquals("one", a.asString());

    Node b = properties.get("b");
    assertTrue(b.isNumber());
    assertEquals(2, b.asInt());

    Node c = properties.get("c");
    assertTrue(c.isFunction());
  }

  /**
   * Test the case when an object literal is not inlinable because it is
   * self-referential.
   */
  public void testSelfReferentialObjectLiteral() {
    String input = "{\n" +
        "  x: function() { this.y = this.x; },\n" +
        "  y: 'two'\n" +
        "}";

    Node root = parseJsCode("(function() {" + input + "})()");
    getPass().process(externs, root);

    Scope scope = root.getLastChild();
    Var objVar = scope.getVar("obj");
    assertNull(objVar);
  }

  /**
   * Test the case when an object literal is inlinable but its property values
   * are not.
   */
  public void testInlinableObjectWithUninlinableProperties() {
    String input = "{\n" +
        "  a: { x: 1, y: 'two' },\n" +
        "  b: 2,\n" +
        "  c: function() {}\n" +
        "}";

    Node root = parseJsCode("(function() {" + input + "})()");
    getPass().process(externs, root);

    Scope scope = root.getLastChild();
    Var objVar = scope.getVar("obj");
    assertTrue(objVar.isDef());
    assertFalse(objVar.isExtern());

    Node obj = objVar.getInitializer();
    assertTrue(obj.isObjectLit());

    Map<String, Node> properties = Maps.newHashMap();
    for (Node child = obj.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isProp()) {
        String key = child.getString();
        Node value = child.getFirstChild();
        properties.put(key, value);
      }
    }

    Node a = properties.get("a");
    assertTrue(a.isObjectLit());

    Node aX = properties.get("a.x");
    assertTrue(aX.isNumber());
    assertEquals(1, aX.asInt());

    Node aY = properties.get("a.y");
    assertTrue(aY.isString());
    assertEquals("two", aY.asString());
  }

  /**
   * Test the case when an object literal is not inlinable because it is
   * defined in the externs.
   */
  public void testObjectLiteralInExterns() {
    String input = "{\n" +
        "  a: 'one'\n" +
        "}";

    Node root = parseJsCode("(function() { var obj = " + input + "; })()");
    getPass().process(externs, root);

    Scope scope = root.getLastChild();
    Var objVar = scope.getVar("obj");
    assertNull(objVar);
  }

  /**
   * Test the case when an object literal is not inlinable because it is
   * exported.
   */
  public void testObjectLiteralExported() {
    String input = "var obj = {\n" +
        "  a: 'one'\n" +
        "};\n" +
        "export obj;";

    Node root = parseJsCode(input);
    getPass().process(externs, root);

    Scope scope = root.getLastChild();
    Var objVar = scope.getVar("obj");
    assertNull(objVar);
  }

  /**
   * Test the case when an object literal is not inlinable because it is a
   * function object.
   */
  public void testFunctionObject() {
    String input = "{\n" +
        "  a: function() {},\n" +
        "  b: 2,\n" +
        "  c: function() {}\n" +
        "}";

    Node root = parseJsCode("(function() {" + input + "})()");
    getPass().process(externs, root);

    Scope scope = root.getLastChild();
    Var objVar = scope.getVar("obj");
    assertNull(objVar);
  }

  /**
   * Test the case when an object literal is not inlinable because it is
   * deleted.
   */
  public void testObjectLiteralDeleted() {
    String input = "var obj = {\n" +
        "  a: 1,\n" +
        "  b: 2,\n" +
        "};\n" +
        "delete obj.a;\n";

    Node root = parseJsCode(input);
    getPass().process(externs, root);

    Scope scope = root.getLastChild();
    Var objVar = scope.getVar("obj");
    assertNull(objVar);
  }

  
}