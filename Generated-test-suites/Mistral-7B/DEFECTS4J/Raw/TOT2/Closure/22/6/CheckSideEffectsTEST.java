import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createNumberLiteral;
import static com.google.javascript.rhino.NodeFactory.createStringLiteral;
import static com.google.javascript.rhino.NodeFactory.createCallExpression;
import static com.google.javascript.rhino.NodeFactory.createObjectLiteral;
import static com.google.javascript.rhino.NodeFactory.createFunctionExpression;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.NodeFactory;

/**
 * Test for CheckSideEffects class.
 */
@RunWith(JUnit4.class)
public class CheckSideEffectsTest {
  private Compiler compiler;
  private Checker checker;

  @Before
  public void setUp() throws Exception {
    compiler = new Compiler();
    checker = new Checker(compiler.getOptions());
  }

  @Test
  public void testCheckSideEffects() {
    String code =
        "function foo() {" +
            "var s = \"this string is \" + " +
            "\"continued on the next line but you forgot the +\";" +
            "x == foo(); " +
            "foo();;" +
            "var a = [1, 2, 3];" +
            "for (var i = 0; i < a.length; i++) {" +
            "  var s = \"this string is \" + " +
            "    \"continued on the next line but you forgot the +\";" +
            "  i++;" +
            "}" +
            "}";

    Node root = NodeFactory.parse(code);
    checker.check(root, null, null);

    NodeTraversal traversal = new NodeTraversal(checker, root);
    new CheckSideEffects(checker, CheckLevel.WARNING, true).visitAll(traversal);

    // Check that no warnings were emitted
    assertFalse(compiler.getDiagnostics().hasErrors());
  }

  @Test
  public void testCheckSideEffectsWithWarning() {
    String code =
        "function foo() {" +
            "var s = \"this string is \" + " +
            "\"continued on the next line but you forgot the +\";" +
            "x == foo(); " +
            "foo();;" +
            "}";

    Node root = NodeFactory.parse(code);
    checker.check(root, null, null);

    NodeTraversal traversal = new NodeTraversal(checker, root);
    new CheckSideEffects(checker, CheckLevel.WARNING, true).visitAll(traversal);

    // Check that a warning was emitted
    List<Node> warnings = Lists.newArrayList(compiler.getDiagnostics().getDiagnostics());
    assertTrue(warnings.size() > 0);
  }

  @Test
  public void testCheckSideEffectsWithProtectedCode() {
    String code =
        "function foo() {" +
            "var s = \"this string is \" + " +
            "\"continued on the next line but you forgot the +\";" +
            "JSCOMPILER_PRESERVE(s);" +
            "x == foo(); " +
            "foo();;" +
            "}";

    Node root = NodeFactory.parse(code);
    checker.check(root, null, null);

    NodeTraversal traversal = new NodeTraversal(checker, root);
    new CheckSideEffects(checker, CheckLevel.WARNING, true).visitAll(traversal);

    // Check that no warnings were emitted
    assertFalse(compiler.getDiagnostics().hasErrors());
  }

  @Test
  public void testCheckSideEffectsWithUnprotectedCode() {
    String code =
        "function foo() {" +
            "JSCOMPILER_PRESERVE(s);" +
            "x == foo(); " +
            "foo();;" +
            "}";

    Node root = NodeFactory.parse(code);
    checker.check(root, null, null);

    NodeTraversal traversal = new NodeTraversal(checker, root);
    new CheckSideEffects(checker, CheckLevel.WARNING, true).visitAll(traversal);

    // Check that a warning was emitted
    List<Node> warnings = Lists.newArrayList(compiler.getDiagnostics().getDiagnostics());
    assertTrue(warnings.size() > 0);
  }
}