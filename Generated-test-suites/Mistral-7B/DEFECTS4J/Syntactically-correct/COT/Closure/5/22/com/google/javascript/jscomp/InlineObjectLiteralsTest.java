import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class InlineObjectLiteralsTest {

  public static Test suite() {
    return new TestSuite(InlineObjectLiteralsTest.class);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }

  

  private static void testInlineObjectLiterals(CompilerPass pass) {
    // Given
    String testCode = "function foo() {"
        + "  var obj = {"
        + "    a: 1,"
        + "    b: {c: 2}"
        + "  };"
        + "  var x = obj.a;"
        + "  var y = obj.b.c;"
        + "}"
        + "function bar() {"
        + "  var obj = {"
        + "    a: 1,"
        + "    b: function() {},"
        + "    c: [1],"
        + "    d: null,"
        + "    e: function() {},"
        + "    f: Object.prototype.toString.call(obj)"
        + "  };"
        + "  var x = obj.a;"
        + "  var y = obj.b;"
        + "  var z = obj.c[0]";
        + "}"
        + "function baz() {"
        + "  var obj = {"
        + "    a: function() {}, "
        + "    b: {c: 1},"
        + "    c: function() {},"
        + "    d: null,"
        + "    e: {f: function() {}},"
        + "  };"
        + "  var x = obj.a();"
        + "  var y = obj.b.c;"
        + "  var z = obj.c();"
        + "}"
        + "function qux() {"
        + "  function quux() {"
        + "    var obj = {"
        + "      a: 1,"
        + "      b: 2,"
        + "      c: {d: 3}"
        + "    };"
        + "    return obj.b * obj.c.d;"
        + "  }"
        + "}"
        + "function quuz() {"
        + "  var obj = {"
        + "    a: {b: 1, c: 2},"
        + "  };"
        + "  return obj.a.b + obj.a.c;"
        + "}"
        + "function quaz() {"
        + "  var obj = {"
        + "    a: {b: 1, c: {d: 2}},"
        + "  };"
        + "  return obj.a.b.d;"
        + "}";

    // When
    Node externs = new Node(Token.SCRIPTLET, null);
    Node root = new Node(Token.SCRIPT, null);
    root.setSourceFile("test.js");
    NodeUtil.parseCompilationUnit(testCode, externs, root);
    CompilerPass passInstance = new InlineObjectLiterals(null, null);
    passInstance.process(externs, root);

    // Then
    Scope rootScope = root.getFirstChild();
    assertFalse(rootScope.getVar("obj") instanceof Var);

    for (String testFunctionName : Lists.newArrayList("foo", "bar", "baz")) {
      Node functionDeclaration = findFunctionDeclaration(rootScope, testFunctionName);
      assertEquals("Function declaration for " + testFunctionName, functionDeclaration.getType(), Node.FUNCTION_DECL);
      Node functionBody = functionDeclaration.getFirstChild();
      Node statement = functionBody.getFirstChild();

      if (testFunctionName.equals("qux")) {
        // InlineObjectLiterals does not inline function declarations
        assertNotNull(findVariableDeclaration(rootScope, "obj"));
        assertNotNull(findVariableDeclaration(rootScope, "x"));
        assertNotNull(findVariableDeclaration(rootScope, "y"));
        assertNotNull(findVariableDeclaration(rootScope, "z"));
        continue;
      }

      Var objVar = findVariableDeclaration(rootScope, "obj");
      Var xVar = findVariableDeclaration(rootScope, "x");
      Var yVar = findVariableDeclaration(rootScope, "y");
      Var zVar = findVariableDeclaration(rootScope, "z");

      assertNotNull(objVar);
      assertNull(objVar.isGlobal());
      assertNull(objVar.isExtern());
      assertNull(objVar.isConstant());
      assertNull(objVar.isVarParameter());
      assertNull(objVar.isInitOnly());
      assertNull(objVar.isReadOnly());
      assertNull(objVar.isShadowed());
      assertEquals("obj", objVar.name);
      assertEquals(Node.VAR, objVar.type);
      assertNull(objVar.getInitializer());
      assertNotNull(objVar.scope);
      assertEquals(rootScope, objVar.scope);
      assertEquals(0, objVar.indices.size());
      assertNull(objVar.getDeclaredName());

      assertEquals("a", xVar.name);
      assertEquals(Node.VAR, xVar.type);
      assertEquals(Node.NUMBER_LITERAL, xVar.getInitializer().getNodeType());
      assertEquals(1.0, xVar.getInitializer().toNumber().doubleValue(), 0.0);
      assertEquals(objVar.scope, xVar.scope);
      assertEquals(0, xVar.indices.size());
      assertNull(xVar.getDeclaredName());

      assertEquals("b", yVar.name);
      if (testFunctionName.equals("foo")) {
        assertEquals(Node.VAR, yVar.type);
        assertEquals(Node.NUMBER_LITERAL, yVar.getInitializer().getNodeType());
        assertEquals(2.0, yVar.getInitializer().toNumber().doubleValue(), 0.0);
        assertEquals(objVar.scope, yVar.scope);
        assertEquals(0, yVar.indices.size());
        assertNull(yVar.getDeclaredName());
      } else {
        assertEquals(Node.GET_PROP, yVar.type);
        assertEquals(objVar, yVar.ref);
        assertEquals("c", yVar.getChildAtIndex(1).getString());
        assertEquals(Node.NUMBER_LITERAL, yVar.getInitializer().getNodeType());
        assertEquals(2.0, yVar.getInitializer().toNumber().doubleValue(), 0.0);
        assertEquals(objVar.scope, yVar.scope);
        assertEquals(1, yVar.indices.size());
        assertNull(yVar.getDeclaredName());
      }

      assertEquals("c", zVar.name);
      if (testFunctionName.equals("foo")) {
        assertEquals(Node.VAR, zVar.type);
        assertEquals(Node.NUMBER_LITERAL, zVar.getInitializer().getNodeType());
        assertEquals(1.0, zVar.getInitializer().toNumber().doubleValue(), 0.0);
        assertEquals(objVar.scope, zVar.scope);
        assertEquals(0, zVar.indices.size());
        assertNull(zVar.getDeclaredName());
      } else if (testFunctionName.equals("bar")) {
        assertEquals(Node.VAR, zVar.type);
        assertEquals(Node.NUMBER_LITERAL, zVar.getInitializer().getNodeType());
        assertEquals(1.0, zVar.getInitializer().toNumber().doubleValue(), 0.0);
        assertEquals(objVar.scope, zVar.scope);
        assertEquals(0, zVar.indices.size());
        assertNull(zVar.getDeclaredName());
      } else {
        assertEquals(Node.GET_PROP, zVar.type);
        assertEquals(objVar, zVar.ref);
        assertEquals(Node.INDEX, zVar.getChildAtIndex(1).getNodeType());
        assertEquals(Node.NUMBER_LITERAL, zVar.getChildAtIndex(1).getFirstChild().getNodeType());
        assertEquals(0, zVar.getChildAtIndex(1).toNumber().intValue());
        assertEquals(Node.NUMBER_LITERAL, zVar.getInitializer().getNodeType());
        assertEquals(1.0, zVar.getInitializer().toNumber().doubleValue(), 0.0);
        assertEquals(objVar.scope, zVar.scope);
        assertEquals(1, zVar.indices.size());
        assertNull(zVar.getDeclaredName());
      }
    }
  }

  private static Node findFunctionDeclaration(Scope scope, String functionName) {
    for (Node child : scope.getVars()) {
      if (child.isFunction()) {
        Node functionNameNode = child.getFirstChild();
        assertEquals(functionName, functionNameNode.getString());
        return child;
      }
    }
    throw new RuntimeException("Function declaration not found: " + functionName);
  }

  private static Node findVariableDeclaration(Scope scope, String variableName) {
    for (Node child : scope.getVars()) {
      if (child.isVar() && child.name.equals(variableName)) {
        return child;
      }
    }
    throw new RuntimeException("Variable declaration not found: " + variableName);
  }

  
}