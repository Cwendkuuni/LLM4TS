import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Parser;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

  @Test
  public void testRuntimeTypeCheck() throws Exception {
    String sourceCode =
        "function Foo() {}\n" +
        "function FooBar(foo) {\n" +
        "  assertInstanceOf(foo, Foo);\n" +
        "  return 42;\n" +
        "}";

    CompilerOptions options = new CompilerOptions();
    options.setShowWarnings(false);
    options.setWarningLevel("QUIRKS", "experimental");
    Compiler compiler = new Compiler();
    CompilerPass pass = new RuntimeTypeCheck(compiler, "function(warning, expr) {\n" +
        "  console.log('Warning: " + expr + " is not an instance of Foo.');\n" +
        "}");
    CompilerResult result = compiler.compile(options, sourceCode, null, pass);

    Parser parser = new Parser();
    Node tree = parser.parse(result.toString(), null, null);
    NodeTraversal traversal = new NodeTraversal(tree, new NodeFactory());

    Node functionNode = NodeUtil.getFunctionNodeByName(tree, "FooBar");
    Node callNode = findCallNode(traversal, functionNode, "assertInstanceOf");

    assertTrue(callNode != null);

    Node argNode = callNode.getFirstChild().getNext();
    Node arg = (Node) argNode.getFirstChild();

    Node expectedTypeNode = NodeUtil.getQualifiedNameNode(
        tree, "Foo", Node.NEW, Node.NO_INIT);

    assertEquals(expectedTypeNode, arg.getNext());
  }

  private Node findCallNode(NodeTraversal traversal, Node functionNode,
      String functionName) {
    for (Node child : functionNode.getChildren()) {
      if (NodeUtil.isFunctionCall(child)) {
        Node callNode = child;
        Node callee = callNode.getFirstChild();
        if (callee.isString() && callee.getStringValue().equals(functionName)) {
          return callNode;
        }
      } else if (NodeUtil.isBlock(child)) {
        Node block = child;
        for (Node grandchild : block.getChildren()) {
          Node node = findCallNode(traversal, functionNode, functionName);
          if (node != null) {
            return node;
          }
        }
      }
    }
    return null;
  }
}