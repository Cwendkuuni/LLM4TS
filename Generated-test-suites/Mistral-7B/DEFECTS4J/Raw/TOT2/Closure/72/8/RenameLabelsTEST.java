import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.rhino.NodeType.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.SourceFile;

/**
 * RenameLabelsTest tests the RenameLabels CompilerPass.
 */
public class RenameLabelsTest {

  @Test
  public void testRenameLabels() {
    // Given a Compiler and CompilerOptions.
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    // When I traverse the Node tree with the RenameLabels CompilerPass.
    CompilerPass renameLabels = new RenameLabels(compiler);
    renameLabels.process(new SourceFile("test.js"), new TestSourceFile("test.js"));
    Node root = compiler.getRootNode();

    // Then the labels should be renamed.
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root);
    Traverse.traverse(nodeTraversal);
    LabelNamespace labelNamespace = nodeTraversal.getUserData(LabelNamespace.class);
    assertNotNull(labelNamespace);

    // Test label names in simple cases.
    assertEquals("a", labelNamespace.renameMap.get("a").id);
    assertEquals("b", labelNamespace.renameMap.get("b").id);
    assertEquals("c", labelNamespace.renameMap.get("c").id);

    // Test label names in nested cases.
    assertEquals(1, labelNamespace.renameMap.get("a1").id);
    assertEquals(2, labelNamespace.renameMap.get("a2").id);
    assertEquals(3, labelNamespace.renameMap.get("a3").id);
    assertEquals(4, labelNamespace.renameMap.get("b1").id);
    assertEquals(5, labelNamespace.renameMap.get("b2").id);

    // Test label names that are redefined.
    assertEquals(6, labelNamespace.renameMap.get("a").id);
    assertEquals(7, labelNamespace.renameMap.get("b").id);
    assertEquals(8, labelNamespace.renameMap.get("c").id);

    // Test label names that are shadowed.
    assertEquals(9, labelNamespace.renameMap.get("a").id);
    assertEquals(10, labelNamespace.renameMap.get("b").id);

    // Test label names that are removed because they are not referenced.
    assertNull(labelNamespace.renameMap.get("unusedLabel"));

    // Test label names that are renamed to invalid names because they
    // conflict with JavaScript keywords.
    assertNotNull(labelNamespace.renameMap.get("break"));
    assertNotNull(labelNamespace.renameMap.get("continue"));
  }

  /**
   * LabelNamespace class used to store the label information.
   */
  static class LabelNamespace {
    Map<String, LabelInfo> renameMap = new HashMap<String, LabelInfo>();
  }

  /**
   * LabelInfo class used to store the label depth and referencing status.
   */
  static class LabelInfo {
    int id;
    boolean referenced;

    LabelInfo(int id) {
      this.id = id;
    }
  }
}