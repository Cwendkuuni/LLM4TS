import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.TREEVISIT_POSTORDER;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerUnit;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traverse;
import com.google.javascript.rhino.NodeTraversal;

import java.util.List;
import java.util.Set;

public class CollapseVariableDeclarationsTest {

  private static class TestCompiler extends AbstractCompiler {

    @Override
    public void process(List<CompilerUnit> units, Node externs, Node root) {
      super.process(units, externs, root);
      getOptions().setOptCollapseVariables(true);
    }
  }

  @org.junit.Test
  public void testCollapsingVariableDeclarations() {
    String input =
        "function foo() {" +
        "  var a;" +
        "  var b = 1;" +
        "  var c = 2;" +
        "  var d = 3;" +
        "  var e = 4;" +
        "  var f = 5;" +
        "  var g = 6;" +
        "}" +
        "function bar() {" +
        "  var a, b = 1, c = 2;" +
        "}" +
        "function baz() {" +
        "  var a = true;" +
        "  var a = false;" +
        "  var a = 1;" +
        "}" +
        "function qux() {" +
        "  var a = 1;" +
        "  var a = 2;" +
        "}" +
        "function quux() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var a = 3;" +
        "}" +
        "function coro() {" +
        "  function foo() {" +
        "    var a;" +
        "    var b = 1;" +
        "  }" +
        "  function bar() {" +
        "    var a, b = 1, c = 2;" +
        "  }" +
        "}" +
        "function quuxx() {" +
        "  function foo() {" +
        "    var a = 1;" +
        "    var a = 2;" +
        "  }" +
        "  function bar() {" +
        "    var a = 1;" +
        "    var b = 2;" +
        "    var a = 3;" +
        "  }" +
        "}" +
        "function quuux() {" +
        "  function foo() {" +
        "    var a = 1;" +
        "  }" +
        "  function bar() {" +
        "    var a = 1;" +
        "    var b = 2;" +
        "  }" +
        "  function baz() {" +
        "    var a = 1;" +
        "    var b = 2;" +
        "    var a = 3;" +
        "  }" +
        "}" +
        "function quuuux() {" +
        "  function foo() {" +
        "    var a = 1;" +
        "    var b = 1;" +
        "    var a = 2;" +
        "  }" +
        "  function bar() {" +
        "    var a = 1;" +
        "    var b = 2;" +
        "    var a = 3;" +
        "  }" +
        "}";

    AbstractCompiler compiler = new TestCompiler();
    CompilerUnit unit = new CompilerUnit(input, "test.js");
    Node root = unit.getRoot();

    CompilerPass pass = new CollapseVariableDeclarations(compiler);
    pass.process(unit.getExterns(), root);

    Traverse.traverse(root, new NodeChecker());
  }

  private static class NodeChecker extends NodeTraversal.AbstractPostOrderCallback {

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isFunction()) {
        List<Node> bodies = n.getBody();
        Node body = bodies.get(0);

        Scope scope = t.getScope();

        ImmutableList<Node> expectedBodies = ImmutableList.of(
            body,
            new Node(Token.BLOCK, ImmutableList.of(
                new Node(Token.VAR, ImmutableList.of(
                    new Node(Token.IDENT, "a"),
                    new Node(Token.ASSIGN, new Node(Token.NUM, 1), null)
                )),
                new Node(Token.VAR, ImmutableList.of(
                    new Node(Token.IDENT, "b"),
                    new Node(Token.ASSIGN, new Node(Token.NUM, 1), null)
                )),
                new Node(Token.VAR, ImmutableList.of(
                    new Node(Token.IDENT, "c"),
                    new Node(Token.ASSIGN, new Node(Token.NUM, 2), null)
                ))
            ))
        );

        assertEquals("Unexpected body for function 'foo'.", expectedBodies, bodies);
      }
    }
  }
}