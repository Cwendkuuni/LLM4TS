import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test_util.TreeMatcher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MakeDeclaredNamesUniqueTest {

  static Node createTestNode(String code) {
    TokenStream ts = new TokenStream(new TokenStream.SourceInfo("test.js", 1, 1));
    ts.fillFromString(code);
    Node rootNode = ts.parseProgram();
    return rootNode;
  }

  static Node createTestFunctionNode(String code) {
    Node rootNode = createTestNode(code);
    return rootNode.getFirstChild();
  }

  static Node createTestVariableNode(String code, String variableName) {
    Node rootNode = createTestNode(code);
    Node variableNode = null;
    for (Node c = rootNode.getFirstChild(); c != null; c = c.getNext()) {
      if (NodeUtil.isVarDeclaration(c) && c.getString().equals(variableName)) {
        variableNode = c;
        break;
      }
    }
    return variableNode;
  }

  static void assertRenamedName(Node rootNode, String oldName, String newName) {
    Node nameNode = null;
    for (Node c = rootNode.getFirstChild(); c != null; c = c.getNext()) {
      if (c.getType() == Token.NAME && c.getString().equals(oldName)) {
        nameNode = c;
        break;
      }
    }
    assertEquals(newName, nameNode.getString());
  }

  static void assertNoNameRenamed(Node rootNode, String name) {
    Node nameNode = null;
    for (Node c = rootNode.getFirstChild(); c != null; c = c.getNext()) {
      if (c.getType() == Token.NAME && c.getString().equals(name)) {
        nameNode = c;
        break;
      }
    }
    assertNull(nameNode);
  }

  @Test
  public void testMakeDeclaredNamesUnique_SimpleFunction() {
    Node rootNode = createTestFunctionNode(
        "function foo() {}"
    );

    MakeDeclaredNamesUnique mdu = new MakeDeclaredNamesUnique();
    NodeTraversal nt = new NodeTraversal(rootNode, null);
    nt.traverse(mdu);

    assertRenamedName(rootNode, "foo", "foo");
  }

  @Test
  public void testMakeDeclaredNamesUnique_FunctionWithVariable() {
    Node rootNode = createTestFunctionNode(
        "function foo() { var x = 1; }"
    );

    MakeDeclaredNamesUnique mdu = new MakeDeclaredNamesUnique();
    NodeTraversal nt = new NodeTraversal(rootNode, null);
    nt.traverse(mdu);

    assertRenamedName(rootNode, "x", "x_0");
  }

  @Test
  public void testMakeDeclaredNamesUnique_FunctionWithVariableAndFunction() {
    Node rootNode = createTestFunctionNode(
        "function foo() { var x = 1; function bar() {} }"
    );

    MakeDeclaredNamesUnique mdu = new MakeDeclaredNamesUnique();
    NodeTraversal nt = new NodeTraversal(rootNode, null);
    nt.traverse(mdu);

    assertRenamedName(rootNode, "x", "x_0");
    assertRenamedName(rootNode, "bar", "bar_0");
  }

  @Test
  public void testMakeDeclaredNamesUnique_FunctionWithVariableAndReusedVariable() {
    Node rootNode = createTestFunctionNode(
        "function foo() { var x = 1; var x = 2; }"
    );

    MakeDeclaredNamesUnique mdu = new MakeDeclaredNamesUnique();
    NodeTraversal nt = new NodeTraversal(rootNode, null);
    nt.traverse(mdu);

    assertRenamedName(rootNode, "x", "x_0");
    assertRenamedName(rootNode, "x", "x_1");
  }

  @Test
  public void testMakeDeclaredNamesUnique_BlockWithVariable() {
    Node rootNode = createTestNode(
        "function foo() { var x = 1; }"
    );

    MakeDeclaredNamesUnique mdu = new MakeDeclaredNamesUnique();
    NodeTraversal nt = new NodeTraversal(rootNode, null);
    nt.traverse(mdu);

    assertRenamedName(rootNode, "x", "x_0");
  }

  @Test
  public void testMakeDeclaredNamesUnique_BlockWithVariableAndFunction() {
    Node rootNode = createTestNode(
        "function foo() { var x = 1; function bar() {} }"
    );

    MakeDeclaredNamesUnique mdu = new MakeDeclaredNamesUnique();
    NodeTraversal nt = new NodeTraversal(rootNode, null);
    nt.traverse(mdu);

    assertRenamedName(rootNode, "x", "x_0");
    assertRenamedName(rootNode, "bar", "bar_0");
  }

  @Test
  public void testMakeDeclaredNamesUnique_BlockWithVariableAndReusedVariable() {
    Node rootNode = createTestNode(
        "function foo() { var x = 1; var x = 2; }"
    );

    MakeDeclaredNamesUnique mdu = new MakeDeclaredNamesUnique();
    NodeTraversal nt = new NodeTraversal(rootNode, null);
    nt.traverse(mdu);

    assertRenamedName(rootNode, "x", "x_0");
    assertRenamedName(rootNode, "x", "x_1");
  }

  @Test
  public void testMakeDeclaredNamesUnique_BlockWithGlobalVariable() {
    Node rootNode = createTestNode(
        "var x = 1; function foo() {}"
    );

    MakeDeclaredNamesUnique mdu = new MakeDeclaredNamesUnique();
    NodeTraversal nt = new NodeTraversal(rootNode, null);
    nt.traverse(mdu);

    assertNoNameRenamed(rootNode, "x");
  }

  @Test
  public void testMakeDeclaredNamesUnique_BlockWithGlobalFunction() {
    Node rootNode = createTestNode(
        "function foo() {}"
    );

    Node varNode = createTestVariableNode(rootNode.toString(), "x");
    rootNode = varNode.getParent();

    MakeDeclaredNamesUnique mdu = new MakeDeclaredNamesUnique();
    NodeTraversal nt = new NodeTraversal(rootNode, null);
    nt.traverse(mdu);

    assertNoNameRenamed(rootNode, "foo");
  }

  @Test
  public void testMakeDeclaredNamesUnique_BlockWithGlobalVariableAndFunction() {
    Node rootNode = createTestNode(
        "var x = 1; function foo() {}"
    );

    Node varNode = createTestVariableNode(rootNode.toString(), "x");
    Node functionNode = rootNode.getFirstChild();
    rootNode = varNode.getParent();

    MakeDeclaredNamesUnique mdu = new MakeDeclaredNamesUnique();
    NodeTraversal nt = new NodeTraversal(rootNode, null);
    nt.traverse(mdu);

    assertNoNameRenamed(rootNode, "x");
    assertNoNameRenamed(functionNode, "x");
  }

  @Test(expected = RuntimeException.class)
  public void testMakeDeclaredNamesUnique_RecursiveFunction() {
    Node rootNode = createTestFunctionNode(
        "function foo(x) { function foo(y) {} }"
    );

    MakeDeclaredNamesUnique mdu = new MakeDeclaredNamesUnique();
    NodeTraversal nt = new NodeTraversal(rootNode, null);
    nt.traverse(mdu);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMakeDeclaredNamesUnique_NullNode() {
    MakeDeclaredNamesUnique mdu = new MakeDeclaredNamesUnique();
    NodeTraversal nt = new NodeTraversal(null, null);
    nt.traverse(mdu);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMakeDeclaredNamesUnique_NullParentNode() {
    Node rootNode = createTestFunctionNode("function foo() {}");
    MakeDeclaredNamesUnique mdu = new MakeDeclaredNamesUnique();
    NodeTraversal nt = new NodeTraversal(rootNode, null);
    nt.traverse(mdu);
    Node recursiveFunctionNode = rootNode.getFirstChild();

    rootNode = null;
    NodeTraversal nt2 = new NodeTraversal(recursiveFunctionNode, null);
    nt2.traverse(mdu);
  }
}