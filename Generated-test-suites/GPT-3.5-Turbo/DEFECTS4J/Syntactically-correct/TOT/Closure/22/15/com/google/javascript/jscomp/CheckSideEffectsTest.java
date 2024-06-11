import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

  @Test
  public void testProcess() {
    // Typical use case: testing a valid code snippet without any useless code
    CheckSideEffects checker = new CheckSideEffects(new Compiler(), CheckLevel.WARNING, false);
    Node externs = new Node(Token.SCRIPT);
    Node root = new Node(Token.SCRIPT);
    checker.process(externs, root);

    // No warnings should be reported
    assertEquals(0, checker.problemNodes.size());
  }

  @Test
  public void testHotSwapScript() {
    // Edge case: testing hot swapping a script with no original root
    CheckSideEffects checker = new CheckSideEffects(new Compiler(), CheckLevel.WARNING, true);
    Node scriptRoot = new Node(Token.SCRIPT);
    Node originalRoot = null;
    checker.hotSwapScript(scriptRoot, originalRoot);

    // No warnings should be reported
    assertEquals(0, checker.problemNodes.size());
  }

  @Test
  public void testVisit() {
    // Error scenario: testing a code snippet with a missing '+' operator
    CheckSideEffects checker = new CheckSideEffects(new Compiler(), CheckLevel.WARNING, true);
    NodeTraversal traversal = new NodeTraversal(new Compiler(), new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        checker.visit(t, n, parent);
      }
    });

    Node root = createCodeSnippetWithError();
    Node externs = new Node(Token.SCRIPT);
    traversal.traverse(externs, root);

    // One warning should be reported
    assertEquals(1, checker.problemNodes.size());
  }

  @Test
  public void testProtectSideEffects() {
    // Testing the protection of side-effect free nodes
    CheckSideEffects checker = new CheckSideEffects(new Compiler(), CheckLevel.WARNING, true);

    Node root = createCodeSnippetWithSideEffectFreeNodes();
    Node externs = new Node(Token.SCRIPT);
    checker.process(externs, root);

    // The problem nodes should be protected
    assertEquals(2, checker.problemNodes.size());
    assertTrue(checker.problemNodes.get(0).getParent().isCall());
    assertTrue(checker.problemNodes.get(1).getParent().isCall());
  }

  @Test
  public void testStripProtection() {
    // Testing the stripping of side-effect sync functions
    CheckSideEffects.StripProtection stripper = new CheckSideEffects.StripProtection(new Compiler());

    Node root = createCodeSnippetWithSideEffectSyncFunctions();
    Node externs = new Node(Token.SCRIPT);
    stripper.process(externs, root);

    // The side-effect sync functions should be removed
    assertFalse(root.toString().contains("JSCOMPILER_PRESERVE()"));
  }

  private Node createCodeSnippetWithError() {
    // Error scenario: code with a missing '+' operator
    Node root = new Node(Token.SCRIPT);
    Node expr1 = NodeUtil.newQName(root, "var");
    Node expr2 = NodeUtil.newQName(root, "s");
    Node expr3 = NodeUtil.newQName(root, "\"this string is \"");
    Node expr4 = NodeUtil.newQName(root, "\"continued on the next line but you forgot the +\"");
    Node expr5 = NodeUtil.newQName(root, "x");
    Node expr6 = NodeUtil.newQName(root, "foo()");
    Node expr7 = NodeUtil.newQName(root, "foo()");

    root.addChildToFront(expr1);
    root.addChildToBack(expr2);
    expr2.addChildToBack(expr3);
    expr2.addChildToBack(expr4);
    root.addChildToBack(expr5);
    root.addChildToBack(expr6);
    root.addChildToBack(expr7);

    return root;
  }

  private Node createCodeSnippetWithSideEffectFreeNodes() {
    // Code snippet with side-effect free nodes
    Node root = new Node(Token.SCRIPT);
    Node expr1 = NodeUtil.newQName(root, "var");
    Node expr2 = NodeUtil.newQName(root, "s");
    Node expr3 = NodeUtil.newQName(root, "\"this string is \"");
    Node expr4 = NodeUtil.newQName(root, "\"continued on the next line but you forgot the +\"");
    Node expr5 = NodeUtil.newQName(root, "x");
    Node expr6 = NodeUtil.newQName(root, "foo()");

    root.addChildToFront(expr1);
    root.addChildToBack(expr2);
    expr2.addChildToBack(expr3);
    expr2.addChildToBack(expr4);
    root.addChildToBack(expr5);
    root.addChildToBack(expr6);

    return root;
  }

  private Node createCodeSnippetWithSideEffectSyncFunctions() {
    // Code snippet with side-effect sync functions
    Node root = new Node(Token.SCRIPT);
    Node expr1 = NodeUtil.newQName(root, "var");
    Node expr2 = NodeUtil.newQName(root, "s");
    Node expr3 = NodeUtil.newQName(root, "x");

    root.addChildToFront(expr1);
    root.addChildToBack(expr2);
    root.addChildToBack(expr3);

    return root;
  }
}