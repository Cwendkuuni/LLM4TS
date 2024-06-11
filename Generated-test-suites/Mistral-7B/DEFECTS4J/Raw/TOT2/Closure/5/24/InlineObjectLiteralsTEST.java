import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private CompilerPass inlineObjectLiterals;

  @Before
  public void setUp() {
    compiler = new Compiler();
    inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "testVar");
  }

  @Test
  public void testInlineObjectLiterals_SimpleAssignment() {
    String code = "var obj = {a: 1, b: 2};";
    Node root = Node.parse(code);

    inlineObjectLiterals.process(Node.rootNode(compiler.getExterns()), root);

    Scope scope = root.getFirstChild().getLastChild().getScope();
    Var objVar = scope.getVar("obj");

    Node objLiteral = objVar.getInitializer();
    assertEquals(2, objLiteral.getNumChildren());

    Node a = objLiteral.getFirstChild();
    Node aValue = a.getFirstChild();
    assertEquals("a", a.getString());
    assertEquals(Token.NUMBER_LITERAL, aValue.getNodeType());
    assertEquals(1, aValue.getInt());

    Node b = objLiteral.getChildAtIndex(1);
    Node bValue = b.getFirstChild();
    assertEquals("b", b.getString());
    assertEquals(Token.NUMBER_LITERAL, bValue.getNodeType());
    assertEquals(2, bValue.getInt());
  }

  @Test
  public void testInlineObjectLiterals_AssignmentWithDottedSyntax() {
    String code = "var obj = {a: {b: 3}};";
    Node root = Node.parse(code);

    inlineObjectLiterals.process(Node.rootNode(compiler.getExterns()), root);

    Scope scope = root.getFirstChild().getLastChild().getScope();
    Var objVar = scope.getVar("obj");

    Node objLiteral = objVar.getInitializer();
    assertEquals(1, objLiteral.getNumChildren());

    Node a = objLiteral.getFirstChild();
    assertEquals("a", a.getString());
    assertTrue(a.isObjectLit());

    Node aObjectLiteral = a.getFirstChild();
    assertEquals(1, aObjectLiteral.getNumChildren());

    Node b = aObjectLiteral.getFirstChild();
    Node bValue = b.getFirstChild();
    assertEquals("b", b.getString());
    assertEquals(Token.NUMBER_LITERAL, bValue.getNodeType());
    assertEquals(3, bValue.getInt());
  }

  @Test
  public void testInlineObjectLiterals_GetProperty() {
    String code = "var obj = {a: 1}; var x = obj.a;";
    Node root = Node.parse(code);

    inlineObjectLiterals.process(Node.rootNode(compiler.getExterns()), root);

    Scope scope = root.getFirstChild().getLastChild().getScope();
    Var objVar = scope.getVar("obj");
    Var xVar = scope.getVar("x");

    Node objLiteral = objVar.getInitializer();
    assertEquals(1, objLiteral.getNumChildren());

    Node a = objLiteral.getFirstChild();
    Node aValue = a.getFirstChild();
    assertEquals("a", a.getString());
    assertEquals(Token.NAME, aValue.getNodeType());
    assertEquals("a", aValue.getString());

    Node getProp = xVar.getInitializer();
    assertEquals(Token.GET_PROP, getProp.getNodeType());
    assertEquals(objLiteral, getProp.getParent());
    assertEquals(1, getProp.getNumChildren());
    Node propName = getProp.getFirstChild();
    assertEquals("a", propName.getString());
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiterals_SelfAssignment() {
    String code = "var obj = {a: obj};";
    Node root = Node.parse(code);

    inlineObjectLiterals.process(Node.rootNode(compiler.getExterns()), root);

    Scope scope = root.getFirstChild().getLastChild().getScope();
    Var objVar = scope.getVar("obj");

    Node objLiteral = objVar.getInitializer();
    assertEquals(1, objLiteral.getNumChildren());

    Node a = objLiteral.getFirstChild();
    Node aValue = a.getFirstChild();
    assertEquals("a", a.getString());
    assertEquals(Token.OBJECT_LIT, aValue.getNodeType());

    Node obj = aValue.getFirstChild();
    Node objName = obj.getFirstChild();
    assertEquals("obj", objName.getString());

    assertTrue(obj.getParent().isAssign());
    Reference selfReference = obj.getParent().getFirstChild();
    assertEquals("obj", selfReference.getString());
    assertEquals(objVar, selfReference.getVar());
  }

  @Test
  public void testInlineObjectLiterals_SetProperty() {
    String code = "var obj = {}; obj.a = 1;";
    Node root = Node.parse(code);

    inlineObjectLiterals.process(Node.rootNode(compiler.getExterns()), root);

    Scope scope = root.getFirstChild().getLastChild().getScope();
    Var objVar = scope.getVar("obj");

    Node objLiteral = objVar.getInitializer();
    assertEquals(0, objLiteral.getNumChildren());

    Node setProp = objVar.getInitializer().getFirstChild();
    assertEquals(Token.SET_PROP, setProp.getNodeType());
    assertEquals(objLiteral, setProp.getParent());
    assertEquals(1, setProp.getNumChildren());
    Node propName = setProp.getFirstChild();
    assertEquals("a", propName.getString());

    Node value = setProp.getChildAtIndex(1);
    assertEquals(1, value.getNumChildren());
    Node numberLiteral = value.getFirstChild();
    assertEquals(Token.NUMBER_LITERAL, numberLiteral.getNodeType());
    assertEquals(1, numberLiteral.getInt());
  }

  @Test
  public void testInlineObjectLiterals_NestedObjectLiterals() {
    String code = "var obj = {a: {b: 1, c: 2}};";
    Node root = Node.parse(code);

    inlineObjectLiterals.process(Node.rootNode(compiler.getExterns()), root);

    Scope scope = root.getFirstChild().getLastChild().getScope();
    Var objVar = scope.getVar("obj");

    Node objLiteral = objVar.getInitializer();
    assertEquals(1, objLiteral.getNumChildren());

    Node a = objLiteral.getFirstChild();
    assertEquals("a", a.getString());
    assertTrue(a.isObjectLit());

    Node aObjectLiteral = a.getFirstChild();
    assertEquals(2, aObjectLiteral.getNumChildren());

    Node b = aObjectLiteral.getFirstChild();
    Node bValue = b.getFirstChild();
    assertEquals("b", b.getString());
    assertEquals(Token.NUMBER_LITERAL, bValue.getNodeType());
    assertEquals(1, bValue.getInt());

    Node c = aObjectLiteral.getChildAtIndex(1);
    Node cValue = c.getFirstChild();
    assertEquals("c", c.getString());
    assertEquals(Token.NUMBER_LITERAL, cValue.getNodeType());
    assertEquals(2, cValue.getInt());
  }

  @Test
  public void testInlineObjectLiterals_PropertyNameWithQuotes() {
    String code = "var obj = {'propertyName': 1};";
    Node root = Node.parse(code);

    inlineObjectLiterals.process(Node.rootNode(compiler.getExterns()), root);

    Scope scope = root.getFirstChild().getLastChild().getScope();
    Var objVar = scope.getVar("obj");

    Node objLiteral = objVar.getInitializer();
    assertEquals(1, objLiteral.getNumChildren());

    Node propertyName = objLiteral.getFirstChild();
    assertEquals("propertyName", propertyName.getString());
    assertEquals(Token.STRING_LITERAL, propertyName.getNodeType());
    assertEquals("propertyName", propertyName.getStringValue());

    Node value = objLiteral.getChildAtIndex(1);
    assertEquals(1, value.getNumChildren());
    Node numberLiteral = value.getFirstChild();
    assertEquals(Token.NUMBER_LITERAL, numberLiteral.getNodeType());
    assertEquals(1, numberLiteral.getInt());
  }

  @Test
  public void testInlineObjectLiterals_EmptyObjectLiteral() {
    String code = "var obj = {};";
    Node root = Node.parse(code);

    inlineObjectLiterals.process(Node.rootNode(compiler.getExterns()), root);

    Scope scope = root.getFirstChild().getLastChild().getScope();
    Var objVar = scope.getVar("obj");

    Node objLiteral = objVar.getInitializer();
    assertEquals(0, objLiteral.getNumChildren());
  }

  @Test
  public void testInlineObjectLiterals_FunctionWithObjectLiteral() {
    String code = "function foo() { var obj = {a: 1}; }";
    Node root = Node.parse(code);

    inlineObjectLiterals.process(Node.rootNode(compiler.getExterns()), root);

    Scope functionScope = root.getFirstChild().getLastChild();
    Var objVar = functionScope.getVar("obj");

    Node objLiteral = objVar.getInitializer();
    assertEquals(1, objLiteral.getNumChildren());

    Node a = objLiteral.getFirstChild();
    assertEquals("a", a.getString());
    assertTrue(a.isObjectLit());

    Node aObjectLiteral = a.getFirstChild();
    assertEquals(1, aObjectLiteral.getNumChildren());

    Node value = aObjectLiteral.getFirstChild();
    assertEquals(1, value.getNumChildren());
    Node numberLiteral = value.getFirstChild();
    assertEquals(Token.NUMBER_LITERAL, numberLiteral.getNodeType());
    assertEquals(1, numberLiteral.getInt());
  }

  @Test
  public void testInlineObjectLiterals_ObjectLiteralInCondition() {
    String code = "if ({a: 1}.a == 1) {}";
    Node root = Node.parse(code);

    inlineObjectLiterals.process(Node.rootNode(compiler.getExterns()), root);

    Node condition = root.getFirstChild().getLastChild();
    assertEquals(Token.IF, condition.getNodeType());

    Node lhs = condition.getFirstChild();
    Node getProp = lhs.getFirstChild();
    assertEquals(Token.GET_PROP, getProp.getNodeType());
    assertEquals(1, getProp.getNumChildren());
    Node objLiteral = getProp.getLastChild();
    assertEquals(1, objLiteral.getNumChildren());

    Node propName = objLiteral.getFirstChild();
    assertEquals("a", propName.getString());

    Node rhs = condition.getChildAtIndex(1);
    Node numberLiteral = rhs.getFirstChild();
    assertEquals(Token.NUMBER_LITERAL, numberLiteral.getNodeType());
    assertEquals(1, numberLiteral.getInt());
  }
}