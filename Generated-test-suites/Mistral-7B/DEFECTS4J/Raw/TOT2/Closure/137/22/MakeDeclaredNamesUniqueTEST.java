import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testSimpleVariableDeclaration() {
    Node rootNode = Node.newTextNode("var x = 123;");
    NodeTraversal traversal = NodeTraversal.create(null, null, null, null);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal, rootNode, null);

    Scope scope = traversal.getScope();
    Var x = scope.getVar("x");
    String expectedName = "x$0";
    String actualName = x.getNameNode().getString();

    assertThat(actualName).isEqualTo(expectedName);
  }

  @Test
  public void testSimpleFunctionDeclaration() {
    Node rootNode = Node.newTextNode("function x(){}");
    NodeTraversal traversal = NodeTraversal.create(null, null, null, null);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal, rootNode, null);

    Scope scope = traversal.getScope();
    Var x = scope.getVar("x");
    String expectedName = "x$0";
    String actualName = x.getNameNode().getString();

    assertThat(actualName).isEqualTo(expectedName);
  }

  @Test
  public void testFunctionWithParameterDeclaration() {
    Node rootNode = Node.newTextNode(
        "function x(a, b) {\n" +
        "  var c = a + b;\n" +
        "  return c;\n" +
        "}");
    NodeTraversal traversal = NodeTraversal.create(null, null, null, null);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal, rootNode, null);

    Scope scope = traversal.getScope();
    Var a = scope.getVar("a");
    Var b = scope.getVar("b");
    Var c = scope.getVar("c");

    String expectedNameA = "a$0";
    String expectedNameB = "b$1";
    String expectedNameC = "c$2";

    String actualNameA = a.getNameNode().getString();
    String actualNameB = b.getNameNode().getString();
    String actualNameC = c.getNameNode().getString();

    assertThat(actualNameA).isEqualTo(expectedNameA);
    assertThat(actualNameB).isEqualTo(expectedNameB);
    assertThat(actualNameC).isEqualTo(expectedNameC);
  }

  @Test
  public void testNestedFunctionDeclaration() {
    Node rootNode = Node.newTextNode(
        "function x() {\n" +
        "  function y() {} \n" +
        "  return;\n" +
        "}");
    NodeTraversal traversal = NodeTraversal.create(null, null, null, null);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal, rootNode, null);

    Scope scope = traversal.getScope();
    Var x = scope.getVar("x");
    Var y = scope.getVar("y");

    String expectedNameX = "x$0";
    String expectedNameY = "y$1";

    String actualNameX = x.getNameNode().getString();
    String actualNameY = y.getNameNode().getString();

    assertThat(actualNameX).isEqualTo(expectedNameX);
    assertThat(actualNameY).isEqualTo(expectedNameY);
  }

  @Test(expected = AssertionError.class)
  public void testDuplicateVariableDeclaration() {
    Node rootNode = Node.newTextNode("var x = 123;\n" +
        "var x = 456;");
    NodeTraversal traversal = NodeTraversal.create(null, null, null, null);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal, rootNode, null);

    Scope scope = traversal.getScope();
    Var x = scope.getVar("x");
  }

  @Test
  public void testGlobalVariableDeclaration() {
    Node rootNode = Node.newTextNode("var x = 123;");
    NodeTraversal traversal = NodeTraversal.create(null, null, null, null);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal, rootNode, null);

    Scope scope = traversal.getScope();
    Var x = scope.getVar("x");

    String expectedName = "x";
    String actualName = x.getNameNode().getString();

    assertThat(actualName).isEqualTo(expectedName);
  }

  @Test
  public void testGlobalFunctionDeclaration() {
    Node rootNode = Node.newTextNode("function x(){}");
    NodeTraversal traversal = NodeTraversal.create(null, null, null, null);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal, rootNode, null);

    Scope scope = traversal.getScope();
    Var x = scope.getVar("x");

    String expectedName = "x";
    String actualName = x.getNameNode().getString();

    assertThat(actualName).isEqualTo(expectedName);
  }

  @Test
  public void testDuplicateFunctionDeclaration() {
    Node rootNode = Node.newTextNode("function x() {} \n" +
        "function x() {}");
    NodeTraversal traversal = NodeTraversal.create(null, null, null, null);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal, rootNode, null);

    Scope scope = traversal.getScope();
    Var x = scope.getVar("x");

    String expectedName = "x$0";
    String actualName = x.getNameNode().getString();

    assertThat(actualName).isEqualTo(expectedName);
  }

  @Test
  public void testVariableDeclarationInFunctionScope() {
    Node rootNode = Node.newTextNode("function x() {\n" +
        "  var x = 123;\n" +
        "}");
    NodeTraversal traversal = NodeTraversal.create(null, null, null, null);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal, rootNode, null);

    Scope scope = traversal.getScope();
    Var x = scope.getVar("x");

    String expectedName = "x$0";
    String actualName = x.getNameNode().getString();

    assertThat(actualName).isEqualTo(expectedName);
  }

  @Test
  public void testFunctionCall() {
    Node rootNode = Node.newTextNode("function x() {} \n" +
        "x();");
    NodeTraversal traversal = NodeTraversal.create(null, null, null, null);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal, rootNode, null);

    Scope scope = traversal.getScope();
    Var x = scope.getVar("x");

    String expectedName = "x$0";
    String actualName = x.getNameNode().getString();

    assertThat(actualName).isEqualTo(expectedName);
  }

  @Test
  public void testVariableDeclarationInBlockScope() {
    Node rootNode = Node.newTextNode("{\n" +
        "  var x = 123;\n" +
        "}");
    NodeTraversal traversal = NodeTraversal.create(null, null, null, null);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal, rootNode, null);

    Scope scope = traversal.getScope();
    Var x = scope.getVar("x");

    String expectedName = "x$0";
    String actualName = x.getNameNode().getString();

    assertThat(actualName).isEqualTo(expectedName);
  }

  @Test
  public void testFunctionDeclarationWithVariableShadowing() {
    Node rootNode = Node.newTextNode("function x() {\n" +
        "  var x = 456;\n" +
        "}");
    NodeTraversal traversal = NodeTraversal.create(null, null, null, null);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal, rootNode, null);

    Scope scope = traversal.getScope();
    Var xFunction = scope.getVar("x");
    Var xVariable = scope.getVar("x");

    String expectedNameFunction = "x$0";
    String expectedNameVariable = "x$1";

    String actualNameFunction = xFunction.getNameNode().getString();
    String actualNameVariable = xVariable.getNameNode().getString();

    assertThat(actualNameFunction).isEqualTo(expectedNameFunction);
    assertThat(actualNameVariable).isEqualTo(expectedNameVariable);
  }

  @Test
  public void testFunctionDeclarationWithSameNameInParentScope() {
    Node rootNode = Node.newTextNode("function x() {\n" +
        "  function x() {} \n" +
        "}");
    NodeTraversal traversal = NodeTraversal.create(null, null, null, null);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal, rootNode, null);

    Scope parentScope = traversal.getScope();
    Var xParent = parentScope.getVar("x");
    Scope childScope = traversal.getScope(traversal.getLastFunctionNode());
    Var xChild = childScope.getVar("x");

    String expectedNameParent = "x$0";
    String expectedNameChild = "x$1";

    String actualNameParent = xParent.getNameNode().getString();
    String actualNameChild = xChild.getNameNode().getString();

    assertThat(actualNameParent).isEqualTo(expectedNameParent);
    assertThat(actualNameChild).isEqualTo(expectedNameChild);
  }

  @Test
  public void testFunctionDeclarationWithParameterSameNameAsParentScopeVariable() {
    Node rootNode = Node.newTextNode("function x(x) {} \n" +
        "var x = 123;");
    NodeTraversal traversal = NodeTraversal.create(null, null, null, null);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal, rootNode, null);

    Scope parentScope = traversal.getScope();
    Var xParent = parentScope.getVar("x");
    Scope functionScope = traversal.getScope(traversal.getLastFunctionNode());
    Var xFunction = functionScope.getVar("x");

    String expectedNameParent = "x$0";
    String expectedNameFunction = "x$1";

    String actualNameParent = xParent.getNameNode().getString();
    String actualNameFunction = xFunction.getNameNode().getString();

    assertThat(actualNameParent).isEqualTo(expectedNameParent);
    assertThat(actualNameFunction).isEqualTo(expectedNameFunction);
  }
}