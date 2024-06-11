import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;

public class CheckSideEffectsTest {

 private Compiler compiler;
 private CompilerOptions options;

 @Before
 public void setup() {
 compiler = new Compiler();
 options = new CompilerOptions(null);
 }

 @Test
 public void testCheckSideEffects() {
 // Test case 1: Simple statement without side-effect
 String code1 = "function foo() { return 42; }";
 Node node1 = NodeTraversal.parse(compiler, code1, null, options).getAst();
 CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 NodeTraversal.traverse(compiler, node1, checker);
 assertEquals(0, checker.problemNodes.size());

 // Test case 2: Statement with side-effect and warning generated
 String code2 = "function foo() { 42; }";
 Node node2 = NodeTraversal.parse(compiler, code2, null, options).getAst();
 checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 NodeTraversal.traverse(compiler, node2, checker);
 assertEquals(1, checker.problemNodes.size());
 Node problemNode = checker.problemNodes.get(0);
 assertEquals(Token.VOID, problemNode.getType());
 assertTrue(problemNode.getJSDocInfo() == null);

 // Test case 3: Statement with side-effect and protected from removal
 String code3 = "function foo() { document.write('Hello World'); }";
 Node node3 = NodeTraversal.parse(compiler, code3, null, options).getAst();
 checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
 NodeTraversal.traverse(compiler, node3, checker);
 assertEquals(1, checker.problemNodes.size());
 Node protectedNode = checker.problemNodes.get(0);
 assertFalse(protectedNode.getChildren().isEmpty());
 Node protectionCall = protectedNode.getChildren().get(0);
 assertNotNull(protectionCall.getJSDocInfo());

 // Test case 4: Statement without side-effect but is an expression
 String code4 = "function foo() { return true && false; }";
 Node node4 = NodeTraversal.parse(compiler, code4, null, options).getAst();
 checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 NodeTraversal.traverse(compiler, node4, checker);
 assertEquals(0, checker.problemNodes.size());

 // Test case 5: Statement without side-effect but is part of a larger expression
 String code5 = "function foo() { return 'Hello' + ' World'; }";
 Node node5 = NodeTraversal.parse(compiler, code5, null, options).getAst();
 checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 NodeTraversal.traverse(compiler, node5, checker);
 assertEquals(0, checker.problemNodes.size());

 // Test case 6: Statement within a loop
 String code6 = "function foo() { for (var i = 0; i < 10; i++) { } }";
 Node node6 = NodeTraversal.parse(compiler, code6, null, options).getAst();
 checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 NodeTraversal.traverse(compiler, node6, checker);
 assertEquals(0, checker.problemNodes.size());

 // Test case 7: Statement within an if statement
 String code7 = "function foo() { if (true) { } }";
 Node node7 = NodeTraversal.parse(compiler, code7, null, options).getAst();
 checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 NodeTraversal.traverse(compiler, node7, checker);
 assertEquals(0, checker.problemNodes.size());

 // Test case 8: Statement within a function call
 String code8 = "function foo() { return bar(); } function bar() { 42; }";
 Node node8 = NodeTraversal.parse(compiler, code8, null, options).getAst();
 checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 NodeTraversal.traverse(compiler, node8, checker);
 assertEquals(1, checker.problemNodes.size());
 Node problemNode8 = checker.problemNodes.get(0);
 assertNotNull(problemNode8.getParent());
 assertEquals(Token.FUNCTION, problemNode8.getParent().getType());
 assertEquals("bar", problemNode8.getParent().getQualifiedName().getString());
 }

 @After
 public void teardown() {
 // Clean up the compiler state
 compiler = null;
 options = null;
 }
}