import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSChecker;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    String sourceCode = "function foo() { return 1; } " +
                        "var x = 0; " +
                        "x = foo(); " +
                        "var s = 'this string is '; " +
                        "// continued on the next line but you forgot the +";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    JSChecker checker = new JSChecker(compiler, options);

    Node ast = checker.parse(sourceCode, null, null);
    NodeTraversal traversal = new NodeTraversal(checker, ast, null);
    new CheckSideEffects(compiler, WARNING, false).process(null, ast);

    // Check that there is no warning for expected statement.
    List<Node> warnings = traversal.getCompiler().getDiagnostics().getSeverities(WARNING);
    assertTrue(warnings.isEmpty());

    // Test for useless code with missing '+'.
    sourceCode = "var s = 'this string is " +
                 "continued on the next line but you forgot the +';";
    ast = checker.parse(sourceCode, null, null);
    traversal = new NodeTraversal(checker, ast, null);
    new CheckSideEffects(compiler, WARNING, false).process(null, ast);

    warnings = traversal.getCompiler().getDiagnostics().getSeverities(WARNING);
    assertTrue(warnings.size() > 0);
    Node warningNode = warnings.get(0).getSourceNode();
    assertTrue(warningNode != null);
    assertTrue(warningNode.isExprStatement());
    assertTrue(warningNode.getFirstChild().isStringLiteral());
    assertTrue(warningNode.getLastChild().isName());
    assertTrue(warningNode.getLastChild().getString().equals("JSC_USELESS_CODE"));

    // Test for useless code with simple operator.
    sourceCode = "function add(a, b) { return a + b; } " +
                "var x = 0; " +
                "x = add(1, 1);";
    ast = checker.parse(sourceCode, null, null);
    traversal = new NodeTraversal(checker, ast, null);
    new CheckSideEffects(compiler, WARNING, false).process(null, ast);

    warnings = traversal.getCompiler().getDiagnostics().getSeverities(WARNING);
    assertTrue(warnings.isEmpty());

    // Test for useless code with complex expression.
    sourceCode = "function add(a, b) { return a + b; } " +
                "var x = 0; " +
                "x = add(Math.sqrt(2) * 3, 4);";
    ast = checker.parse(sourceCode, null, null);
    traversal = new NodeTraversal(checker, ast, null);
    new CheckSideEffects(compiler, WARNING, false).process(null, ast);

    warnings = traversal.getCompiler().getDiagnostics().getSeverities(WARNING);
    assertTrue(warnings.isEmpty());

    // Test for useless code inside a for loop.
    sourceCode = "function add(a, b) { return a + b; } " +
                "for (var i = 0; i < 10; i++) {" +
                "  var x = i; " +
                "  x = add(i, i); " +
                "}" +
                "var s = 'this string is '; " +
                "// continued on the next line but you forgot the +";
    ast = checker.parse(sourceCode, null, null);
    traversal = new NodeTraversal(checker, ast, null);
    new CheckSideEffects(compiler, WARNING, false).process(null, ast);

    warnings = traversal.getCompiler().getDiagnostics().getSeverities(WARNING);
    assertTrue(warnings.isEmpty());

    // Test for useless code inside a call expression.
    sourceCode = "function add(a, b) { return a + b; } " +
                "var x = 0; " +
                "eval(add(1, 1).toString());";
    ast = checker.parse(sourceCode, null, null);
    traversal = new NodeTraversal(checker, ast, null);
    new CheckSideEffects(compiler, WARNING, false).process(null, ast);

    warnings = traversal.getCompiler().getDiagnostics().getSeverities(WARNING);
    assertTrue(warnings.isEmpty());

    // Test for useless code with side-effect free functions.
    sourceCode = "function add(a, b) { return a + b; } " +
                "function foo() { return 1; } " +
                "var x = 0; " +
                "x = add(foo(), foo());";
    ast = checker.parse(sourceCode, null, null);
    traversal = new NodeTraversal(checker, ast, null);
    new CheckSideEffects(compiler, WARNING, false).process(null, ast);

    warnings = traversal.getCompiler().getDiagnostics().getSeverities(WARNING);
    assertTrue(warnings.isEmpty());

    // Test for useless code with side-effect free statements.
    sourceCode = "function foo() { return 1; } " +
                "var x = 0; " +
                "x = foo(); " +
                "x = foo();";
    ast = checker.parse(sourceCode, null, null);
    traversal = new NodeTraversal(checker, ast, null);
    new CheckSideEffects(compiler, WARNING, false).process(null, ast);

    warnings = traversal.getCompiler().getDiagnostics().getSeverities(WARNING);
    assertTrue(warnings.isEmpty());
  }

  @Test
  public void testProtectSideEffects() {
    String sourceCode = "function foo() { return 1; } " +
                       "function bar() { var x = 1; } " +
                       "var x = 0; " +
                       "x = foo(); " +
                       "bar();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    JSChecker checker = new JSChecker(compiler, options);

    sourceCode += "\n" +
                 "function JSCOMPILER_PROTECT_SIDE_EFFECTS() {}";

    Node ast = checker.parse(sourceCode, null, null);
    NodeTraversal traversal = new NodeTraversal(checker, ast, null);
    new CheckSideEffects(compiler, WARNING, true).process(null, ast);

    // Check that side-effect free statements are protected.
    List<Node> nodesToProtect = Lists.newArrayList();
    for (Node n = ast.getFirstChild(); n != null; n = n.getNext()) {
      if (n.isVar()) {
        if (n.getFirstChild().getString().equals("x")) {
          nodesToProtect.add(n);
        }
      }
    }

    for (Node node : nodesToProtect) {
      Node callNode = IR.call(IR.name("JSCOMPILER_PROTECT_SIDE_EFFECTS")).srcref(node);
      node.getParent().replaceChild(node, callNode);
      callNode.addChildToBack(node);
    }

    // Check that protected statements are not removed.
    NodeTraversal traversal2 = new NodeTraversal(checker, ast, null);
    new StripProtection(compiler).process(null, ast);

    for (Node n : nodesToProtect) {
      Node protectedNode = null;
      for (Node node = n.getFirstChild(); node != null; node = node.getNext()) {
        if (node.isCall() && node.getFirstChild().isName() && node.getFirstChild().getString().equals("JSCOMPILER_PROTECT_SIDE_EFFECTS")) {
          protectedNode = node;
          break;
        }
      }
      assertTrue(protectedNode != null);
    }
  }
}