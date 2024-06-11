import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.TestSourceFile;

public class RenameLabelsTest {

 private Compiler compiler;
 private CompilerOptions options;

 @Before
 public void setUp() {
 compiler = new Compiler();
 options = new CompilerOptions();
 }

 @Test
 public void testRenameLabels() {
 TestSourceFile source = new TestSourceFile(
 "testRenameLabels.js",
 "function foo() {\n" +
 "  a: {\n" +
 "    b: break a;\n" +
 "  }\n" +
 "  a: {\n" +
 "    b: break a;\n" +
 "  }\n" +
 "}\n" +
 "function bar() {\n" +
 "  a: {\n" +
 "    b: break a;\n" +
 "  }\n" +
 "  a: {\n" +
 "    b: break b;\n" +
 "  }\n" +
 "}\n" +
 "function baz() {\n" +
 "  for (var i = 0; i < 10; i++) {\n" +
 "    a: {\n" +
 "      if (i > 5) {\n" +
 "        break a;\n" +
 "      }\n" +
 "    }\n" +
 "  }\n" +
 "}\n");

 compiler.compile(source.getSourceCode(), options);
 Node root = source.getRootNode();
 NodeTraversal traversal = new NodeTraversal(root, compiler);
 new RenameLabels(compiler).process(root, root);

 // Check label names in foo
 Node foo = traversal.findFirstDescendantOfType(root, Node.FUNCTION);
 Node fooLabel = foo.getFirstChild();
 assertEquals("a", fooLabel.getFirstChild().getString());
 Node fooLabel2 = foo.getChild(1);
 assertEquals("a", fooLabel2.getFirstChild().getString());

 // Check label names in bar
 Node bar = traversal.findFirstDescendantOfType(root, Node.FUNCTION);
 Node barLabel = bar.getFirstChild();
 assertEquals("a", barLabel.getFirstChild().getString());
 Node barLabel2 = bar.getChild(1);
 assertEquals("b", barLabel2.getFirstChild().getString());

 // Check label names in baz
 Node baz = traversal.findFirstDescendantOfType(root, Node.FUNCTION);
 Node bazFor = baz.getChild(0);
 Node bazIf = bazFor.getChild(1);
 Node bazLabel = bazIf.getChild(0);
 assertEquals("a", bazLabel.getFirstChild().getString());
 }

 @Test
 public void testRemoveUnusedLabels() {
 TestSourceFile source = new TestSourceFile(
 "testRemoveUnusedLabels.js",
 "function foo() {\n" +
 "  a: {\n" +
 "    b: break a;\n" +
 "  }\n" +
 "  a: {\n" +
 "  }\n" +
 "}\n" +
 "function bar() {\n" +
 "  a: {\n" +
 "  }\n" +
 "}\n");

 CompilerOptions options = new CompilerOptions();
 options.setRemoveUnreachableCode(true);
 compiler.compile(source.getSourceCode(), options);
 Node root = source.getRootNode();
 NodeTraversal traversal = new NodeTraversal(root, compiler);
 new RenameLabels(compiler, new DefaultNameSupplier(), false).process(root, root);

 // Unused label should be removed
 Node foo = traversal.findFirstDescendantOfType(root, Node.FUNCTION);
 Node fooLabel = foo.getFirstChild();
 assertNull(fooLabel);
 }

 @Test
 public void testKeywordsAsLabels() {
 TestSourceFile source = new TestSourceFile(
 "testKeywordsAsLabels.js",
 "function foo() {\n" +
 "  var if: number = 5;\n" +
 "  a: {\n" +
 "    if: break a;\n" +
 "  }\n" +
 "}\n");
 throw new AssertionError("Keyword as label is not supported");
 }
}