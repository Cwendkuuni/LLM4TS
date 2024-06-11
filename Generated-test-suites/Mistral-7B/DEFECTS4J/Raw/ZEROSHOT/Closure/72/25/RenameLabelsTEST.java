import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameLabels.*;

import com.google.common.base.Suppliers;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  @SuppressWarnings("unchecked")
  private AbstractCompiler createCompiler() {
    return new Compiler() {
      @Override
      public NodeUtil nodeUtil() {
        return null;
      }
    };
  }

  @SuppressWarnings("unchecked")
  private RenameLabels createPass(AbstractCompiler compiler) {
    return new RenameLabels(compiler, Suppliers.newSupplier(), true);
  }

  @Test
  public void testRenameLabels() {
    AbstractCompiler compiler = createCompiler();
    RenameLabels pass = createPass(compiler);

    String code = "function bar() {" +
        "  a: {" +
        "    b: {" +
        "      foo();" +
        "    }" +
        "  }" +
        "  a: {" +
        "    b: break a;" +
        "  }" +
        "}";

    Node externs = Node.createTree(code);
    Node root = Node.createTree("{ bar: function() {}}");

    pass.process(externs, root);

    NodeTraversal nt = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, new ProcessLabels() {
      @Override
      public void visitLabel(Node node, Node parent) {
        String name = node.getFirstChild().getString();
        LabelInfo li = getLabelInfo(name);
        assertEquals(li.id, Integer.parseInt(name.substring(1)) - 1);
      }
    });
  }

  private LabelInfo getLabelInfo(String name) {
    ((ProcessLabels) NodeTraversal.traversalContext().getCallback())
        .namespaceStack.peek().renameMap.get(name);
  }
}