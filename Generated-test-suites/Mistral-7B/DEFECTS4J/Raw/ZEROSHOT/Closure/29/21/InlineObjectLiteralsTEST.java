import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  @SuppressWarnings("unchecked")
  private static <T> List<T> asList(T... values) {
    return List.of(values);
  }

  @SuppressWarnings("unchecked")
  private static <K, V> Map<K, V> newImmutableMap(Map.Entry<K, V>... entries) {
    return ImmutableMap.ofEntries(entries);
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  public void testInlineObjectLiterals() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> null);

    String source = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: {'c': 2}" +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b.c;" +
        "  function bar() { return obj.b.c; }" +
        "  return x + y + bar();" +
        "}";

    Node root = NodePrinter.print(source, null);
    Node externs = null;
    pass.process(externs, root);

    NodeTraversal traverser = new NodeTraversal(root, new NodeTraversal.RootVisitor() {
      @Override
      public void visit(NodeTraversal nt) {
        nt.traverse(new NodeUtil.Visitor() {
          @Override
          public void visit(Node node) {
            if (node.isCall() && node.getFirstChild().isName(VAR_PREFIX + "obj")) {
              List<Node> children = node.getChildren();
              List<Node> args = children.subList(1, children.size());
              Map<String, Node> expectedArgs = newImmutableMap(
                  Map.entry("a", new Node(NumberLiterals.INT_ONE)),
                  Map.entry("b", new Node(
                      new ObjectLiteral(
                          new List<Node>(
                              new Node(StringLiterals.IDENTIFIER_C),
                              new Node(NumberLiterals.INT_TWO)))))
              );
              assertEquals(expectedArgs, Maps.newHashMap(args));
            }
          }
        }, NodeUtil.MATCH_ALL);
      }
    });
  }

  private static class ObjectLiteral extends Node {
    private final List<Node> properties;

    public ObjectLiteral(List<Node> properties) {
      this.properties = properties;
    }

    @Override
    public NodeType getNodeType() {
      return NodeType.OBJECT_LITERAL;
    }

    @Override
    public String toString() {
      return "ObjectLiteral{" + properties + "}";
    }

    @Override
    public void traverse(NodeTraversal nt) {
      for (Node property : properties) {
        nt.push("property");
        property.traverse(nt);
        nt.pop();
      }
    }
  }
}