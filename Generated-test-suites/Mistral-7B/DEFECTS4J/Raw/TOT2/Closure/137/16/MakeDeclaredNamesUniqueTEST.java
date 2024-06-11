import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.TokenStream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MakeDeclaredNamesUniqueTest {

 private static final String TEST_CASE_1 =
     "function foo() {" +
     "  var a = 1;" +
     "  function bar() {" +
     "    var a = 2;" +
     "  }" +
     "  bar();" +
     "}";

 private static final String TEST_CASE_2 =
     "function foo() {" +
     "  function bar() {" +
     "    throw new Error('test')" +
     "  }" +
     "  try {" +
     "    bar();" +
     "  } catch (e) {" +
     "  }" +
     "}";

 private static final String TEST_CASE_3 =
     "function foo() {" +
     "  var a = 1;" +
     "  function bar() {" +
     "    var a = 2;" +
     "  }" +
     "  bar();" +
     "  var a = 3;" +
     "}";

 @Test
 public void testMakeDeclaredNamesUnique() {
   TokenStream ts = new TokenStream(TEST_CASE_1);
   Node rootNode = ts.rootNode();

   MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
   makeDeclaredNamesUnique.traverse(rootNode);

   assertEquals(1, rootNode.getFirstChild().getChildren().size());
   assertEquals(1, rootNode.getLastChild().getChildren().size());

   assertEquals("a1", rootNode.getFirstChild().getFirstChild().getString());
   assertEquals("bar0", rootNode.getFirstChild().getLastChild().getFirstChild().getString());

   // Test for the edge case of a function with the same name as a variable
   makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
   makeDeclaredNamesUnique.traverse(rootNode);

   ts = new TokenStream(TEST_CASE_2);
   rootNode = ts.rootNode();
   makeDeclaredNamesUnique.traverse(rootNode);

   assertEquals(1, rootNode.getFirstChild().getChildren().size());
   assertEquals(1, rootNode.getLastChild().getChildren().size());

   assertEquals("bar0", rootNode.getFirstChild().getLastChild().getString());

   // Test for the edge case of a variable with the same name as a catch exception
   makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
   makeDeclaredNamesUnique.traverse(rootNode);

   ts = new TokenStream(TEST_CASE_3);
   rootNode = ts.rootNode();
   makeDeclaredNamesUnique.traverse(rootNode);

   assertEquals(1, rootNode.getFirstChild().getChildren().size());
   assertEquals(3, rootNode.getLastChild().getChildren().size());

   assertEquals("a1", rootNode.getFirstChild().getFirstChild().getString());
   assertEquals("bar1", rootNode.getFirstChild().getLastChild(0).getString());
   assertEquals("a3", rootNode.getLastChild().getFirstChild().getString());
 }

 @Test(expected = RuntimeException.class)
 public void testMakeDeclaredNamesUniqueException() {
   TokenStream ts = new TokenStream(TEST_CASE_1);
   Node rootNode = ts.rootNode();

   MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(
       new InlineRenamer(
           () -> "RenamerException",
           "RENAMER_EXCEPTION_",
           true) {
     @Override
     public void visit(NodeTraversal t, Node n, Node parent) {
       throw new RuntimeException("Test Exception");
     }
   });

   makeDeclaredNamesUnique.traverse(rootNode);
 }

}