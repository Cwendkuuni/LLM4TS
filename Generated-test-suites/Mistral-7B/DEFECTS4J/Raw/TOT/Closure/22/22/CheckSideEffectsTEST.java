import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckSideEffects;

public class CheckSideEffectsTest {

 private AbstractCompiler compiler;
 private CompilerOptions options;

 @Before
 public void setup() {
 compiler = new Compiler();
 options = new CompilerOptions();
 }

 @Test
 public void testCheckSideEffects() {
 // Test case for a simple expression that does not have any side effects
 String input = "function foo() { return 42; }";
 checkNoSideEffects(input);

 // Test case for a statement that does not have any side effects
 String input1 = "function foo() { var x = 42; }";
 checkNoSideEffects(input1);

 // Test case for a statement that has a side effect but is protected
 String input2 = "function foo() { JSCOMPILER_PRESERVE x = 42; }";
 checkSideEffectsProtected(input2);

 // Test case for a statement that has a side effect and is not protected
 String input3 = "function foo() { x = 42; }";
 checkSideEffectsNotProtected(input3);

 // Test case for a statement that has multiple side effects
 String input4 = "function foo() { x = 42; y = 43; }";
 checkSideEffectsNotProtected(input4);

 // Test case for a statement that has a side effect and is protected but should not be
 String input5 = "function foo() { JSCOMPILER_PRESERVE x = y; }";
 checkSideEffectsProtectedButShouldNotBe(input5);

 // Test case for a statement that has no side effects but is marked as having side effects
 String input6 = "function foo() { // TODO: mark this statement as having side effects JSCOMPILER_PRESERVE x = 42; }";
 checkSideEffectsMarkedAsHavingSideEffectsButDoesNot(input6);
 }

 private void checkNoSideEffects(String input) {
 Checker checker = new Checker(options);
 checker.check(input, null, null);
 List<Node> problemNodes = new CheckSideEffects(checker, CheckLevel.WARNING, false).getProblemNodes();
 assertFalse("No side effect statements found", !problemNodes.isEmpty());
 }

 private void checkSideEffectsProtected(String input) {
 Checker checker = new Checker(options);
 checker.check(input, null, null);
 List<Node> problemNodes = new CheckSideEffects(checker, CheckLevel.WARNING, true).getProblemNodes();
 assertTrue("Side effect statements found and protected", !problemNodes.isEmpty());

 // Verify that the protected side effect statements are not removed
 NodeTraversal traverser = new NodeTraversal(checker.getFactory(), checker.getOptions());
 Node root = checker.getAST();
 Node problemNode = problemNodes.get(0);
 Node replacement = NodeUtil.findNodeWithSourcePosition(root, problemNode.getSourcePosition());
 assertTrue("The protected side effect statement was not replaced with a call to JSCOMPILER_PRESERVE", replacement.isCall() && replacement.getFirstChild().getString().equals("JSCOMPILER_PRESERVE"));
 }

 private void checkSideEffectsNotProtected(String input) {
 Checker checker = new Checker(options);
 checker.check(input, null, null);
 List<Node> problemNodes = new CheckSideEffects(checker, CheckLevel.WARNING, false).getProblemNodes();
 assertTrue("Side effect statements found but not protected", !problemNodes.isEmpty());

 // Verify that the side effect statements are removed
 NodeTraversal traverser = new NodeTraversal(checker.getFactory(), checker.getOptions());
 Node root = checker.getAST();
 Node problemNode = problemNodes.get(0);
 Node replacement = NodeUtil.findNodeWithSourcePosition(root, problemNode.getSourcePosition());
 assertFalse("The side effect statement was not removed", replacement.isStatement());
 }

 private void checkSideEffectsProtectedButShouldNotBe(String input) {
 Checker checker = new Checker(options);
 checker.check(input, null, null);
 List<Node> problemNodes = new CheckSideEffects(checker, CheckLevel.WARNING, true).getProblemNodes();
 assertFalse("Side effect statements found and protected but should not be", !problemNodes.isEmpty());
 }

 private void checkSideEffectsMarkedAsHavingSideEffectsButDoesNot(String input) {
 Checker checker = new Checker(options);
 checker.check(input, null, null);
 List<Node> problemNodes = new CheckSideEffects(checker, CheckLevel.ERROR, false).getProblemNodes();
 assertFalse("Side effect statements marked as having side effects but do not have any", !problemNodes.isEmpty());
 }
}