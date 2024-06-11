import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.Token;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RenameLabelsTest {

  @Test
  public void testRenameLabels() {
    String jsSource = ""
        + "function foo() {"
        + "  a: {"
        + "    x = 1;"
        + "  }"
        + "  b: {"
        + "    break a;"
        + "  }"
        + "}"
        + "function bar() {"
        + "  a: {"
        + "    x = 1;"
        + "  }"
        + "  b: {"
        + "    break a;"
        + "  }"
        + "}"
        + "function baz() {"
        + "  a: {"
        + "    continue a;"
        + "  }"
        + "  b: {"
        + "    continue a;"
        + "  }"
        + "}"
        + "function qux() {"
        + "  a: {"
        + "    break a;"
        + "  }"
        + "  b: {"
        + "    continue b;"
        + "  }"
        + "}"
        + "function quux() {"
        + "  a: {"
        + "    break a;"
        + "  }"
        + "  b: {"
        + "    continue b;"
        + "  }"
        + "  c: {"
        + "    break a;"
        + "  }"
        + "}";

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);

    TestSourceFile sourceFile = new TestSourceFile("test.js", jsSource);
    Node externs = null;
    Node root = sourceFile.getTree();

    // Test with removal of unreferenced labels.
    RenameLabels pass1 = new RenameLabels(compiler);
    pass1.process(externs, root);
    assertTrue(pass1.removeUnused);

    Node result = root;
    // Check that 'a' in 'foo' and 'bar' became the same name.
    Node labelA = result.getFirstChild().getFirstChild();
    Node labelAInFoo = labelA.getNextSibling();
    Node labelAInBar = labelA.getNextSibling().getNextSibling();
    String nameA = labelAInFoo.getString();
    String nameAInFoo = labelAInFoo.getFirstChild().getString();
    String nameAInBar = labelAInBar.getFirstChild().getString();
    assertTrue(nameA.equals(nameAInFoo));
    assertTrue(nameA.equals(nameAInBar));

    // Check that 'b' in 'foo' and 'bar' became the same name.
    Node labelB = labelAInBar.getNextSibling();
    Node labelBInFoo = labelAInFoo.getNextSibling();
    String nameB = labelB.getString();
    String nameBInFoo = labelBInFoo.getString();
    assertTrue(nameB.equals(nameBInFoo));

    // Check that labels in 'baz' and 'qux' did not change.
    Node labelC = result.getLastChild().getLastChild().getFirstChild();
    Node labelCInQux = result.getLastChild().getLastChild().getLastChild()
        .getLastChild().getLastChild();
    String nameC = labelC.getString();
    String nameCInQux = labelCInQux.getString();
    assertFalse(nameC.equals(nameCInQux));

    // Test without removal of unreferenced labels.
    RenameLabels pass2 = new RenameLabels(compiler, new DefaultNameSupplier(), false);
    pass2.process(externs, root);
    assertFalse(pass2.removeUnused);

    result = root;
    // Check that 'a' in 'foo' and 'bar' still have different names.
    labelA = result.getFirstChild().getFirstChild();
    labelAInFoo = labelA.getNextSibling();
    labelAInBar = labelA.getNextSibling().getNextSibling();
    nameA = labelAInFoo.getString();
    nameAInFoo = labelAInFoo.getFirstChild().getString();
    nameAInBar = labelAInBar.getFirstChild().getString();
    assertFalse(nameA.equals(nameAInFoo));

    // Check that 'b' in 'foo' and 'bar' still have different names.
    labelB = labelAInBar.getNextSibling();
    labelBInFoo = labelAInFoo.getNextSibling();
    nameB = labelB.getString();
    nameBInFoo = labelBInFoo.getString();
    assertFalse(nameB.equals(nameBInFoo));
  }
}