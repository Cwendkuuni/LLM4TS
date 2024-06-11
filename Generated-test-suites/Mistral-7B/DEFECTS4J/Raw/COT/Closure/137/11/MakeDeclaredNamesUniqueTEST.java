import static org.junit.Assert.*;

import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import java.util.Map;

public class MakeDeclaredNamesUniqueTest {

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_EmptyCodeBlock() {
    Node rootNode = Node.createObject();
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, null);
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse();
    assertEquals(0, makeDeclaredNamesUnique.nameStack.size());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_SimpleVariableDeclaration() {
    String code = "var x = 1;";
    Node rootNode = Node.parse(code);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, null);
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse();
    assertEquals(1, makeDeclaredNamesUnique.nameStack.size());
    assertEquals("x", makeDeclaredNamesUnique.nameStack.peek().getLastDeclaredName());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_FunctionDeclaration() {
    String code = "function foo(x) {}";
    Node rootNode = Node.parse(code);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, null);
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse();
    assertEquals(2, makeDeclaredNamesUnique.nameStack.size());
    assertEquals("x", makeDeclaredNamesUnique.nameStack.peek().getLastDeclaredName());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_NestedFunctionDeclaration() {
    String code = "function foo() {" +
                  "  function bar() {}" +
                  "}";
    Node rootNode = Node.parse(code);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, null);
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse();
    assertEquals(3, makeDeclaredNamesUnique.nameStack.size());
    assertEquals("bar", makeDeclaredNamesUnique.nameStack.peek().getLastDeclaredName());
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testMakeDeclaredNamesUnique_NullNode() {
    Node rootNode = null;
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, null);
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse();
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testMakeDeclaredNamesUnique_InvalidNode() {
    String code = "1 + 1;";
    Node rootNode = Node.parse(code);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, null);
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse();
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_SameNameInDifferentScopes() {
    String code = "function foo() {" +
                  "  var x = 1;" +
                  "} function foo() {" +
                  "  var x = 2;" +
                  "}" +
                  "function bar() {" +
                  "  var x = 1;" +
                  "}";
    Node rootNode = Node.parse(code);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, null);
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse();
    assertEquals(6, makeDeclaredNamesUnique.nameStack.size());
    assertEquals("x_1", makeDeclaredNamesUnique.nameStack.peek().getLastDeclaredName());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_SameNameInSameScope() {
    String code = "function foo() {" +
                  "  var x = 1;" +
                  "  var x = 2;" +
                  "}" +
                  "function bar() {" +
                  "  var x = 1;" +
                  "}";
    Node rootNode = Node.parse(code);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, null);
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse();
    assertEquals(8, makeDeclaredNamesUnique.nameStack.size());
    assertEquals("x_2", makeDeclaredNamesUnique.nameStack.peek().getLastDeclaredName());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_GlobalVariableDeclaration() {
    String code = "var x = 1; function foo() {}";
    Node rootNode = Node.parse(code);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, null);
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse();
    assertEquals(2, makeDeclaredNamesUnique.nameStack.size());
    assertEquals("x", makeDeclaredNamesUnique.nameStack.peek().getLastDeclaredName());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_GlobalFunctionDeclaration() {
    String code = "var x = 1; function foo() {}";
    Node rootNode = Node.parse(code);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, null);
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse();
    assertEquals(2, makeDeclaredNamesUnique.nameStack.size());
    assertEquals("foo", makeDeclaredNamesUnique.nameStack.peek().getLastDeclaredName());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_GlobalVariableAndFunctionDeclaration() {
    String code = "var x = 1; function foo() {}";
    Node rootNode = Node.parse(code);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, null);
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse();
    assertEquals(3, makeDeclaredNamesUnique.nameStack.size());
    assertEquals("foo", makeDeclaredNamesUnique.nameStack.peek().getLastDeclaredName());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_GlobalVariableAndFunctionDeclarationInDifferentOrder() {
    String code = "function foo() {} var x = 1;";
    Node rootNode = Node.parse(code);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, null);
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse();
    assertEquals(3, makeDeclaredNamesUnique.nameStack.size());
    assertEquals("x", makeDeclaredNamesUnique.nameStack.peek().getLastDeclaredName());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_FunctionWithParameters() {
    String code = "function foo(x, y) {}";
    Node rootNode = Node.parse(code);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, null);
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse();
    assertEquals(5, makeDeclaredNamesUnique.nameStack.size());
    assertEquals("x", makeDeclaredNamesUnique.nameStack.peek().getLastDeclaredName());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_FunctionWithRecursiveCall() {
    String code = "function foo(x) {" +
                  "  if (x > 0) {" +
                  "    x--;" +
                  "    foo(x);" +
                  "  }}" +
                  "foo(3);";
    Node rootNode = Node.parse(code);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, null);
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse();
    assertEquals(6, makeDeclaredNamesUnique.nameStack.size());
    assertEquals("x", makeDeclaredNamesUnique.nameStack.peek().getLastDeclaredName());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_FunctionWithCatchBlock() {
    String code = "try {" +
                  "  throw new Error('Error');" +
                  "} catch (ex) {" +
                  "  console.log(ex.message);" +
                  "}" +
                  "function foo() {}";
    Node rootNode = Node.parse(code);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, null);
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse();
    assertEquals(6, makeDeclaredNamesUnique.nameStack.size());
    assertEquals("ex", makeDeclaredNamesUnique.nameStack.peek().getLastDeclaredName());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_FunctionWithVariableDeclarationAndCatchBlock() {
    String code = "try {" +
                  "  throw new Error('Error');" +
                  "} catch (ex) {" +
                  "  var x = 1;" +
                  "  console.log(ex.message);" +
                  "}" +
                  "function foo() {}";
    Node rootNode = Node.parse(code);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, null);
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse();
    assertEquals(7, makeDeclaredNamesUnique.nameStack.size());
    assertEquals("ex", makeDeclaredNamesUnique.nameStack.peek().getLastDeclaredName());
  }

  private static class DummyScope implements Scope {
    @Override
    public void declare(String name, Node nameNode, Node initialValue, Node parent) {}

    @Override
    public void declareFinal(String name, Node nameNode, Node initialValue, Node parent) {}

    @Override
    public void declareWithInitialization(String name, Node nameNode, Node initialValue, Node parent) {}

    @Override
    public void declareConstant(String name, Node nameNode, Node parent) {}

    @Override
    public void delete(String name) {}

    @Override
    public Var getVar(String name) {
      return null;
    }

    @Override
    public void setParent(Scope parent) {}

    @Override
    public boolean isDeclared(String name, boolean recurse) {
      return false;
    }

    @Override
    public boolean isDeclared(String name) {
      return false;
    }

    @Override
    public boolean isGlobal() {
      return true;
    }

    @Override
    public Node getRootNode() {
      return null;
    }
  }
}