import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.NodeBuilder;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.List;

public class MakeDeclaredNamesUniqueTest {

  private static Node createNodes(String... nodeStrings) {
    NodeTraversal traversal = new NodeTraversal() {};
    Node root = traversal.parse(new NodeBuilder().addTokens(nodeStrings).build());
    return root;
  }

  @org.junit.Test
  public void testUniqueNames() {
    String jsSource =
        "function foo() {" +
        "  var x = 1;" +
        "  var y = 2;" +
        "  var z = x;" +
        "  var x = 3;" +
        "  function bar() {" +
        "    var x = 4;" +
        "  }" +
        "}" +
        "function baz() {" +
        "  var x = 1;" +
        "  function qux() {" +
        "    var x = 2;" +
        "  }" +
        "}" +
        "function quux() {" +
        "  var x = 1;" +
        "  function quuz() {" +
        "    var x = 2;" +
        "  }" +
        "}" +
        "var x = 5;" +
        "function qux() {" +
        "  var x = 6;" +
        "}";

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Node node = NodeTraversal.traverse(makeDeclaredNamesUnique, createNodes(jsSource), null);

    Scope scope = node.getScope();
    List<String> names = Arrays.asList(
      "x",
      "y",
      "z",
      "foo",
      "bar",
      "baz",
      "qux",
      "quuz",
      "quux"
    );

    for (String name : names) {
      assertEquals(name, scope.getVar(name).getNameNode().getString());
    }
  }

  @org.junit.Test
  public void testUniqueFunctionNames() {
    String jsSource =
        "function foo() {" +
        "  function bar() {" +
        "  }" +
        "}" +
        "function baz() {" +
        "  function qux() {" +
        "  }" +
        "}" +
        "function quux() {" +
        "  function quuz() {" +
        "  }" +
        "}" +
        "function quuz() {" +
        "  function qux() {" +
        "  }" +
        "}" +
        "function qux() {" +
        "}" +
        "function qux() {" +
        "}" +
        "function qux() {" +
        "}" +
        "function qux() {" +
        "}" +
        "function qux() {" +
        "}";

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Node node = NodeTraversal.traverse(makeDeclaredNamesUnique, createNodes(jsSource), null);

    Scope scope = node.getScope();
    List<String> names = Arrays.asList(
      "foo",
      "bar",
      "baz",
      "qux",
      "quuz",
      "quuz_1",
      "quux",
      "quuz_2",
      "qux_3",
      "qux_4",
      "qux_5",
      "qux_6"
    );

    for (int i = 0; i < names.size(); i++) {
      String name = names.get(i);
      assertEquals(name, scope.getVar(name).getNameNode().getString());
    }
  }

  @org.junit.Test
  public void testGlobalVariables() {
    String jsSource = "var x = 1;";

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Node node = NodeTraversal.traverse(makeDeclaredNamesUnique, createNodes(jsSource), null);

    Scope scope = node.getScope();
    assertEquals("x", scope.getVar("x").getNameNode().getString());
  }

  @org.junit.Test
  public void testScopeChain() {
    String jsSource = "function foo() {" +
        "  var x = 1;" +
        "  function bar() {" +
        "    var x = 2;" +
        "  }" +
        "}";

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Node node = NodeTraversal.traverse(makeDeclaredNamesUnique, createNodes(jsSource), null);

    Scope scope = node.getScope();
    assertEquals("x_1", scope.getVar("x").getNameNode().getString());
    assertEquals("bar_x", scope.getVar("x").getNameNode().getParent().getNameNode().getString());
  }

  @org.junit.Test
  public void testNestedScopes() {
    String jsSource = "function foo() {" +
        "  function bar() {" +
        "    var x = 1;" +
        "    function qux() {" +
        "      var x = 2;" +
        "    }" +
        "  }" +
        "}";

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Node node = NodeTraversal.traverse(makeDeclaredNamesUnique, createNodes(jsSource), null);

    Scope scope = node.getScope();
    assertEquals("foo_bar_x_1", scope.getVar("x").getNameNode().getString());
    assertEquals("qux_x", scope.getVar("x").getNameNode().getParent().getNameNode().getNameNode().getNameNode().getString());
  }

  @org.junit.Test
  public void testThrowStatement() {
    String jsSource = "function foo() {" +
        "  try {" +
        "    throw new Error('bla');" +
        "  } catch (e) {" +
        "  }" +
        "  var x = 1;" +
        "}";

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Node node = NodeTraversal.traverse(makeDeclaredNamesUnique, createNodes(jsSource), null);

    Scope scope = node.getScope();
    assertEquals("foo_x", scope.getVar("x").getNameNode().getString());
  }

  @org.junit.Test
  public void testErrorThrownInTry() {
    String jsSource = "function foo() {" +
        "  try {" +
        "    throw new Error('bla');" +
        "  } catch (e) {" +
        "    var x = 1;" +
        "  }" +
        "}";

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    assertThrows(RuntimeException.class, () -> {
      NodeTraversal.traverse(makeDeclaredNamesUnique, createNodes(jsSource), null);
    });
  }

  @org.junit.Test
  public void testGlobalVariableWithSameNameAsFunction() {
    String jsSource = "var x = 1;" +
        "function x() {}";

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    assertThrows(RuntimeException.class, () -> {
      NodeTraversal.traverse(makeDeclaredNamesUnique, createNodes(jsSource), null);
    });
  }

  @org.junit.Test
  public void testGlobalVariableWithSameNameAsVariable() {
    String jsSource = "var x = 1;" +
        "var x = 2";

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    assertThrows(RuntimeException.class, () -> {
      NodeTraversal.traverse(makeDeclaredNamesUnique, createNodes(jsSource), null);
    });
  }
}