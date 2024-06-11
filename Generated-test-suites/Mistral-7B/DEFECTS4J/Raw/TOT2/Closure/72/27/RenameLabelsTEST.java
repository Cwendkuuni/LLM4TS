import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameLabels.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.test_util.TestSourceFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenameLabelsTest {

  private static String testSource =
      "function testRenameLabels() {" +
          "  a: {" +
          "    label1: break a;" +
          "  }" +
          "  a: {" +
          "    label2: continue a;" +
          "  }" +
          "  a: {" +
          "    break label1;" +
          "    continue label2;" +
          "  }" +
          "  b: {" +
          "    a: {" +
          "      break a;" +
          "    }" +
          "  }" +
          "}";

  @Test
  public void testRenameLabels() throws IOException {
    Parser parser = new Parser();
    Node root = parser.parse(new TestSourceFile(testSource), null, null);

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameLabels pass = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    pass.process(root, null);

    NodeTraversal nt = new NodeTraversal(root, pass);
    NodeTraversal.traverse(compiler, root, nt);

    Map<String, Integer> labelCounts = new HashMap<>();
    traverseNodes(root, new LabelTraverser(labelCounts));

    assertEquals(3, labelCounts.size());
    List<Map.Entry<String, Integer>> labelEntries = new ArrayList<>(labelCounts.entrySet());
    Map.Entry<String, Integer> label1 = labelEntries.get(0);
    Map.Entry<String, Integer> label2 = labelEntries.get(1);
    Map.Entry<String, Integer> label3 = labelEntries.get(2);

    // Labels should be renamed to "a1", "a2", "a3" respectively.
    assertEquals("a1", label1.getKey());
    assertEquals("a2", label2.getKey());
    assertEquals("a3", label3.getKey());

    // Labels should have unique ids.
    assertNotEquals(label1.getKey(), label2.getKey());
    assertNotEquals(label1.getKey(), label3.getKey());
    assertNotEquals(label2.getKey(), label3.getKey());

    // Labels should be renamed in the correct order.
    int id1 = label1.getValue();
    int id2 = label2.getValue();
    int id3 = label3.getValue();
    assertTrue(id1 < id2 && id1 < id3);
    assertTrue(id2 < id3);
  }

  private static void traverseNodes(Node node, NodeVisitor visitor) {
    if (node != null) {
      visitor.visit(node);
      for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
        traverseNodes(child, visitor);
      }
    }
  }

  private static interface NodeVisitor {
    void visit(Node node);
  }

  private static class LabelTraverser implements NodeVisitor {

    private final Map<String, Integer> labelMap;

    LabelTraverser(Map<String, Integer> labelMap) {
      this.labelMap = labelMap;
    }

    @Override
    public void visit(Node node) {
      if (node.getType() == Token.LABEL) {
        String labelName = node.getFirstChild().getString();
        Integer labelId = labelMap.get(labelName);
        if (labelId == null) {
          labelMap.put(labelName, labelMap.size());
        }
      }
    }
  }
}