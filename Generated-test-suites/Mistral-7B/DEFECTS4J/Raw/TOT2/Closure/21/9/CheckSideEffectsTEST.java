import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;

import java.util.List;

public class CheckSideEffectsTest {

  @org.junit.Test
  public void testCheckSideEffects() {
    CheckLevel level = CheckLevel.WARNING;
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, false);

    String input =
        "function foo() {\n" +
            "  var x = 5;\n" +
            "  return x;\n" +
            "}" +
        "function main() {\n" +
            "  foo();\n" +
            "}" +
        "main();";

    Node root = IR.parse(input);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    List<Node> problems = checkSideEffects.problemNodes;

    // Basic functionality test
    assertTrue(problems.isEmpty());

    // Edge cases and exception handling scenarios

    // Test if warning is issued when there is a stray semicolon
    input =
        "function main() {\n" +
            "  ;\n" +
            "}" +
        "main();";

    root = IR.parse(input);
    checkSideEffects = new CheckSideEffects(compiler, level, false);
    checkSideEffects.process(null, root);
    problems = checkSideEffects.problemNodes;

    // The stray semicolon should not cause a warning
    assertTrue(problems.isEmpty());

    // Test if warning is issued when there is a missing '+' between string literals
    input =
        "function main() {\n" +
            "  var s = \"this string is ";
    root = IR.parse(input);
    checkSideEffects = new CheckSideEffects(compiler, level, false);
    checkSideEffects.process(null, root);
    problems = checkSideEffects.problemNodes;

    // A warning should be issued for the missing '+'
    assertFalse(problems.isEmpty());

    // Test if warning is issued for unnecessary statements
    input =
        "function main() {\n" +
            "  var x = 5;\n" +
            "  var y = x;\n" +
            "  y = x;\n" +
            "}" +
        "main();";

    root = IR.parse(input);
    checkSideEffects = new CheckSideEffects(compiler, level, false);
    checkSideEffects.process(null, root);
    problems = checkSideEffects.problemNodes;

    // No warnings should be issued for this case
    assertTrue(problems.isEmpty());
  }
}