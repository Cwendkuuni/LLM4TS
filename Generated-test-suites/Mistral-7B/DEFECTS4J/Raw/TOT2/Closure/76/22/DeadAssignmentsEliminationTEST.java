import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class DeadAssignmentsEliminationTest {

 private AbstractCompiler compiler;
 private DeadAssignmentsElimination deadAssignmentsElimination;
 private NodeTraversal nodeTraversal;

 @Before
 public void setup() {
 compiler = mock(AbstractCompiler.class);
 deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
 nodeTraversal = mock(NodeTraversal.class);
 }

 @Test
 public void testSimpleAssignment() {
 Node root = Node.newText("x = 1;");
 NodeTraversal.traverse(compiler, root, nodeTraversal, deadAssignmentsElimination);
 verify(compiler, times(1)).reportCodeChange();
 }

 @Test
 public void testSimpleAssignmentWithDeadVariable() {
 Node root = Node.newText("x = 1; y = x; x = 2;");
 NodeTraversal.traverse(compiler, root, nodeTraversal, deadAssignmentsElimination);
 verify(compiler, times(1)).reportCodeChange();
 }

 @Test(expected = RuntimeException.class)
 public void testAssignmentToFunction() {
 Node root = Node.newText("function x() {} x = 1;");
 NodeTraversal.traverse(compiler, root, nodeTraversal, deadAssignmentsElimination);
 }

 @Test(expected = RuntimeException.class)
 public void testAssignmentToThis() {
 Node root = Node.newText("function x() { this = 1; }");
 NodeTraversal.traverse(compiler, root, nodeTraversal, deadAssignmentsElimination);
 }

 @Test
 public void testConditionalStatement() {
 Node root = Node.newText("if (true) { x = 1; } else { x = 2; }");
 NodeTraversal.traverse(compiler, root, nodeTraversal, deadAssignmentsElimination);
 // The result should be the same as if (true) { x = 1; }
 }

 @Test
 public void testConditionalStatementWithDeadBranch() {
 Node root = Node.newText("if (false) { x = 1; } else { x = 2; }");
 NodeTraversal.traverse(compiler, root, nodeTraversal, deadAssignmentsElimination);
 verify(compiler, times(1)).reportCodeChange();
 // The result should be the same as if (false) {}
 }

 @Test
 public void testAssignmentInsideLoop() {
 Node root = Node.newText("for (int i = 0; i < 10; i++) { x = i; }");
 NodeTraversal.traverse(compiler, root, nodeTraversal, deadAssignmentsElimination);
 verify(compiler, times(1)).reportCodeChange();
 // The result should be the same as for (int i = 0; i < 10; i++) {}
 }

 @Test
 public void testAssignmentInsideLoopWithDeadLoopVariable() {
 Node root = Node.newText("for (int i = 0; i < 10; i++) { x = i; i = 10; }");
 NodeTraversal.traverse(compiler, root, nodeTraversal, deadAssignmentsElimination);
 verify(compiler, times(1)).reportCodeChange();
 // The result should be the same as for (int i = 0; i < 10; i++) {}
 }

 @Test
 public void testAssignmentInsideSwitch() {
 Node root = Node.newText("switch (1) {" +
 "case 1:" +
 "x = 1;" +
 "break;" +
 "case 2:" +
 "x = 2;" +
 "}"
 );
 NodeTraversal.traverse(compiler, root, nodeTraversal, deadAssignmentsElimination);
 verify(compiler, times(1)).reportCodeChange();
 // The result should be the same as switch (1) { case 1: }
 }

 @Test
 public void testAssignmentInsideSwitchWithDeadCase() {
 Node root = Node.newText("switch (1) {" +
 "case 1:" +
 "x = 1;" +
 "case 2:" +
 "x = 2;" +
 "}"
 );
 NodeTraversal.traverse(compiler, root, nodeTraversal, deadAssignmentsElimination);
 verify(compiler, times(1)).reportCodeChange();
 // The result should be the same as switch (1) { case 1: }
 }

 @Test
 public void testMultipleAssignments() {
 Node root = Node.newText("x = 1; y = x;");
 NodeTraversal.traverse(compiler, root, nodeTraversal, deadAssignmentsElimination);
 verify(compiler, times(1)).reportCodeChange();
 }

 @Test
 public void testMultipleAssignmentsWithDeadVariable() {
 Node root = Node.newText("x = 1; y = x; x = 2;");
 NodeTraversal.traverse(compiler, root, nodeTraversal, deadAssignmentsElimination);
 verify(compiler, times(1)).reportCodeChange();
 }
}