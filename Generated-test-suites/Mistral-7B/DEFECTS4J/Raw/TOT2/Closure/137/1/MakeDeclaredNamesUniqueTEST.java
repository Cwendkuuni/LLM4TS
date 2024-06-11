import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeTraversal;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MakeDeclaredNamesUniqueTest {

 private Node createTestNode(String code) {
 Node node = new NodeTraversal(new Node("source")) {
 @Override
 public Node traverse(NodeTraversal t, Node n, Node parent) {
 this.setCurrent(n);
 t.accept(MakeDeclaredNamesUnique.this, this, parent);
 return null;
 }
 }.process(new TokenStream(new TokenStream.Source(code)));
 return node;
 }

 @Test
 public void testSimpleVariableDeclaration() {
 String code = "var a = 1,\nb = 2;";
 Node node = createTestNode(code);
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal.traverse(null, node, null);
 assertEquals("a", node.lookupVariable("a").getName().getString());
 assertEquals("b", node.lookupVariable("b").getName().getString());
 }

 @Test
 public void testFunctionDeclaration() {
 String code = "function foo() {" +
 "var a = 1,\nb = 2;" +
 "}";
 Node node = createTestNode(code);
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal.traverse(null, node, null);
 assertEquals("a", node.getFirstChild().getLastChild().lookupVariable("a").getName().getString());
 assertEquals("b", node.getFirstChild().getLastChild().lookupVariable("b").getName().getString());
 }

 @Test
 public void testFunctionCall() {
 String code = "function foo() {" +
 "var a = 1;" +
 "}" +
 "function bar() {" +
 "foo();" +
 "}" +
 "bar();";
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 Node node = createTestNode(code);
 NodeTraversal.traverse(null, node, null);
 assertEquals("foo$0", node.getFirstChild().getLastChild().getFirstChild().getName().getString());
 }

 @Test(expected = RuntimeException.class)
 public void testDuplicateVariableDeclarationInFunction() {
 String code = "function foo() {" +
 "var a = 1;" +
 "var a = 2;" +
 "}";
 Node node = createTestNode(code);
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal.traverse(null, node, null);
 }

 @Test(expected = RuntimeException.class)
 public void testDuplicateVariableDeclarationInGlobalScope() {
 String code = "var a = 1;" +
 "var a = 2";
 Node node = createTestNode(code);
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 try {
 NodeTraversal.traverse(null, node, null);
 } catch (RuntimeException e) {
 //expected
 }
 }

 @Test
 public void testVariableDeclarationWithDuplicateNameInParentScope() {
 String code = "function foo() {" +
 "var a = 1;" +
 "}" +
 "function bar() {" +
 "var a = 2;" +
 "foo();" +
 "}";
 Node node = createTestNode(code);
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal.traverse(null, node, null);
 assertEquals("a$1", node.getFirstChild().getLastChild().getFirstChild().getName().getString());
 }

 @Test
 public void testVariableDeclarationWithDuplicateNameInChildScope() {
 String code = "function foo() {" +
 "var a = 1;" +
 "function bar() {" +
 "var a = 2;" +
 "}" +
 "}" +
 "foo();";
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 Node node = createTestNode(code);
 NodeTraversal.traverse(null, node, null);
 assertEquals("a$0", node.getFirstChild().getName().getString());
 assertEquals("a$1", node.getFirstChild().getFirstChild().getFirstChild().getName().getString());
 }

 @Test
 public void testVariableDeclarationWithDuplicateNameInChildScopeWithRemoveConstness() {
 String code = "function foo() {" +
 "const a = 1;" +
 "function bar() {" +
 "var a = 2;" +
 "}" +
 "}" +
 "foo();";
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(new InlineRenamer(
 Suppliers.memoize(() -> ""), "", true));
 Node node = createTestNode(code);
 NodeTraversal.traverse(null, node, null);
 assertEquals("a$0", node.getFirstChild().getName().getString());
 assertEquals("a$1", node.getFirstChild().getFirstChild().getFirstChild().getName().getString());
 assertEquals(false, node.getFirstChild().getFirstChild().getFirstChild().hasProp("IS_CONSTANT_NAME"));
 }

 @Test
 public void testVariableDeclarationWithDuplicateNameInGlobalScopeWithRemoveConstness() {
 String code = "const a = 1" +
 "const a = 2";
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(new InlineRenamer(
 Suppliers.memoize(() -> ""), "", true));
 Node node = createTestNode(code);
 try {
 NodeTraversal.traverse(null, node, null);
 } catch (RuntimeException e) {
 //expected
 }
 }

 @Test
 public void testFunctionDeclarationWithDuplicateNameInParentScope() {
 String code = "function foo() {" +
 "function a() {}" +
 "}" +
 "function bar() {" +
 "function a() {}" +
 "}";
 Node node = createTestNode(code);
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal.traverse(null, node, null);
 assertEquals("a$0", node.getFirstChild().getName().getString());
 assertEquals("a$1", node.getLastChild().getName().getString());
 }

}