import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp. DeadAssignmentsElimination;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DeadAssignmentsEliminationTest {

 private static AbstractCompiler compiler = new AbstractCompiler();

 @Test
 public void testDeadAssignmentsElimination() {
 Node root = Node.parse("function test() {" +
 "var a = 1;" +
 "var b = a;" +
 "a = 2;" +
 "return a;");

 DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
 eliminator.process(null, root);

 // Verify the output
 assertEquals(Node.toString(root), "function test() {" +
 "var a = 1;" +
 "return 2;" +
 "}");
 }

 @Test
 public void testDeadAssignmentsAfterConditional() {
 Node root = Node.parse("function test() {" +
 "var a = 1;" +
 "if (true) {" +
 " a = 2;" +
 "}" +
 "return a;");

 DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
 eliminator.process(null, root);

 // Verify the output
 assertEquals(Node.toString(root), "function test() {" +
 "var a = 1;" +
 "return 1;" +
 "}");
 }

 @Test
 public void testDeadAssignmentsAfterAssignment() {
 Node root = Node.parse("function test() {" +
 "var a = 1;" +
 "var b = a;" +
 "a = 2;" +
 "b = a;" +
 "return b;");

 DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
 eliminator.process(null, root);

 // Verify the output
 assertEquals(Node.toString(root), "function test() {" +
 "var a = 1;" +
 "var b = 1;" +
 "return b;" +
 "}");
 }

 @Test
 public void testDeadAssignmentsAfterForLoop() {
 Node root = Node.parse("function test() {" +
 "for (var i = 0; i < 5; i++) {" +
 " var a = i;" +
 " a = a + 1;" +
 "}" +
 "return a;");

 DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
 eliminator.process(null, root);

 // Verify the output
 assertEquals(Node.toString(root), "function test() {" +
 "for (var i = 0; i < 5; i++) {" +
 "}" +
 "return 5;" +
 "}");
 }

 @Test
 public void testDeadAssignmentsAfterWhileLoop() {
 Node root = Node.parse("function test() {" +
 "var i = 0;" +
 "while (true) {" +
 " var a = i;" +
 " a = a + 1;" +
 " if (a > 10) {" +
 " break;" +
 "}" +
 "}" +
 "return a;");

 DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
 eliminator.process(null, root);

 // Verify the output
 assertEquals(Node.toString(root), "function test() {" +
 "var i = 0;" +
 "while (true) {" +
 " if (i > 10) {" +
 " break;" +
 "}" +
 "}" +
 "return 10;" +
 "}");
 }

 @Test
 public void testDeadAssignmentsAfterReturn() {
 Node root = Node.parse("function test() {" +
 "var a = 1;" +
 "return a = 2;");

 DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
 eliminator.process(null, root);

 // Verify the output
 assertEquals(Node.toString(root), "function test() {" +
 "return 1;" +
 "}");
 }
}