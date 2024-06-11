import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CheckSideEffectsTest {

  private static CompilerInput createInput(String jsCode) {
    SourceFile source = SourceFile.create("test.js", jsCode);
    CompilerInput input = new CompilerInput(source);
    input.setOptions(new CompilerOptions());
    return input;
  }

  private static CheckSideEffects createChecker(boolean protectSideEffectFreeCode) {
    Checker checker = new Checker();
    CheckSideEffects checkerSideEffects = new CheckSideEffects(checker, CheckLevel.WARNING, protectSideEffectFreeCode);
    return checkerSideEffects;
  }

  @org.junit.Test
  public void testCheckSideEffects() throws IOException {
    String jsCode = "var x = 1; // side-effect\n" +
            "function foo() {\n" +
            "  return 2; // no side-effect\n" +
            "}";

    CheckSideEffects checkerSideEffects = createChecker(false);
    CompilerInput input = createInput(jsCode);
    Node ast = NodeTraversal.traverse(checkerSideEffects, null, input);
    List<Node> problems = checkerSideEffects.getProblemNodes();
    NodeTraversal.traverse(new StripProtection(checkerSideEffects), null, ast);
    checkerSideEffects.check(input, ast);
    assertNull(problems);
  }

  @org.junit.Test
  public void testCheckSideEffectsProtectSideEffectFreeCode() throws IOException {
    String jsCode = "function foo() {\n" +
            "  // no side-effect\n" +
            "}" +
            "// unused code\n" +
            "var x = 1; // side-effect\n" +
            "foo();";

    CheckSideEffects checkerSideEffects = createChecker(true);
    CompilerInput input = createInput(jsCode);
    Node ast = NodeTraversal.traverse(checkerSideEffects, null, input);
    List<Node> problems = checkerSideEffects.getProblemNodes();
    NodeTraversal.traverse(new StripProtection(checkerSideEffects), null, ast);
    checkerSideEffects.check(input, ast);
    assertFalse(problems.isEmpty());
    assertEquals(1, problems.size());
    Node problemNode = problems.get(0);
    assertTrue(problemNode.isExprResult());
    assertNotNull(problemNode.getFirstChild());
    assertEquals("JSCOMPILER_PRESERVE", problemNode.getFirstChild().getString());
  }

  @org.junit.Test
  public void testCheckSideEffectsUselessCode() throws IOException {
    String jsCode = "function foo() {\n" +
            "  return 2; // no side-effect\n" +
            "}" +
            "function bar() {\n" +
            "  // no side-effect\n" +
            "}" +
            "var x = 1; // side-effect\n" +
            "x == foo(); // useless code\n" +
            "bar(); // useless code\n" +
            "foo();; // probably just a stray-semicolon\n" +
            "x = 3; // side-effect\n";

    CheckSideEffects checkerSideEffects = createChecker(false);
    CompilerInput input = createInput(jsCode);
    Node ast = NodeTraversal.traverse(checkerSideEffects, null, input);
    List<Node> problems = checkerSideEffects.getProblemNodes();
    NodeTraversal.traverse(new StripProtection(checkerSideEffects), null, ast);
    checkerSideEffects.check(input, ast);
    assertTrue(problems.isEmpty());
  }

  @org.junit.Test
  public void testCheckSideEffectsCornerCase() throws IOException {
    String jsCode = "function foo() {\n" +
            "  return 2; // no side-effect\n" +
            "}" +
            "function bar() {\n" +
            "  return 3; // no side-effect\n" +
            "}" +
            "var x = 1; // side-effect\n" +
            "x = foo(); // assignment to the same variable\n" +
            "x = bar(); // assignment to the same variable\n" +
            "x = 5; // side-effect\n";

    CheckSideEffects checkerSideEffects = createChecker(false);
    CompilerInput input = createInput(jsCode);
    Node ast = NodeTraversal.traverse(checkerSideEffects, null, input);
    List<Node> problems = checkerSideEffects.getProblemNodes();
    NodeTraversal.traverse(new StripProtection(checkerSideEffects), null, ast);
    checkerSideEffects.check(input, ast);
    assertTrue(problems.isEmpty());
  }

  @org.junit.Test
  public void testCheckSideEffectsEval() throws IOException {
    String jsCode = "function foo() {\n" +
            "  return 2; // no side-effect\n" +
            "}" +
            "var x = 1; // side-effect\n" +
            "eval(\"x = 3;\\nfoo();\\n\");";

    CheckSideEffects checkerSideEffects = createChecker(false);
    CompilerInput input = createInput(jsCode);
    Node ast = NodeTraversal.traverse(checkerSideEffects, null, input);
    List<Node> problems = checkerSideEffects.getProblemNodes();
    NodeTraversal.traverse(new StripProtection(checkerSideEffects), null, ast);
    checkerSideEffects.check(input, ast);
    assertTrue(problems.isEmpty());
  }

  @org.junit.Test
  public void testCheckSideEffectsProtectSideEffectFreeCodeCornerCase() throws IOException {
    String jsCode = "function foo() {\n" +
            "  return 2; // no side-effect\n" +
            "}" +
            "function bar() {\n" +
            "  return 3; // no side-effect\n" +
            "}" +
            "var x = 1; // side-effect\n" +
            "x = foo(); // assignment to the same variable\n" +
            "x = bar(); // assignment to the same variable\n" +
            "x = 5; // side-effect\n";

    CheckSideEffects checkerSideEffects = createChecker(true);
    CompilerInput input = createInput(jsCode);
    Node ast = NodeTraversal.traverse(checkerSideEffects, null, input);
    List<Node> problems = checkerSideEffects.getProblemNodes();
    NodeTraversal.traverse(new StripProtection(checkerSideEffects), null, ast);
    checkerSideEffects.check(input, ast);
    assertFalse(problems.isEmpty());
    assertEquals(3, problems.size());
    assertFalse(problems.get(0).getFirstChild().isComma());
    assertFalse(problems.get(1).getFirstChild().isComma());
    assertFalse(problems.get(2).getFirstChild().isComma());
  }

  @org.junit.Test
  public void testCheckSideEffectsError() throws IOException {
    String jsCode = "function foo() {\n" +
            "  return 2; // no side-effect\n" +
            "}" +
            "function bar() {\n" +
            "  return 3; // no side-effect\n" +
            "}" +
            "var x = 1; // side-effect\n" +
            "x = foo(); // assignment to the same variable\n" +
            "x = bar(); // assignment to the same variable\n" +
            "x = 5; // side-effect\n" +
            "return foo(); // error, function return statement\n";

    CheckSideEffects checkerSideEffects = createChecker(false);
    CompilerInput input = createInput(jsCode);
    Node ast = NodeTraversal.traverse(checkerSideEffects, null, input);
    List<Node> problems = checkerSideEffects.getProblemNodes();
    NodeTraversal.traverse(new StripProtection(checkerSideEffects), null, ast);
    checkerSideEffects.check(input, ast);
    assertFalse(problems.isEmpty());
    assertEquals(1, problems.size());
    assertEquals("JSC_USELESS_CODE", problems.get(0).getJSDocInfo().getType().getName());
  }

  @org.junit.Test
  public void testCheckSideEffectsProtectSideEffectFreeCodeError() throws IOException {
    String jsCode = "function foo() {\n" +
            "  return 2; // no side-effect\n" +
            "}" +
            "function bar() {\n" +
            "  return 3; // no side-effect\n" +
            "}" +
            "var x = 1; // side-effect\n" +
            "x = foo(); // assignment to the same variable\n" +
            "x = bar(); // assignment to the same variable\n" +
            "x = 5; // side-effect\n" +
            "return foo(); // error, function return statement\n";

    CheckSideEffects checkerSideEffects = createChecker(true);
    CompilerInput input = createInput(jsCode);
    Node ast = NodeTraversal.traverse(checkerSideEffects, null, input);
    List<Node> problems = checkerSideEffects.getProblemNodes();
    NodeTraversal.traverse(new StripProtection(checkerSideEffects), null, ast);
    checkerSideEffects.check(input, ast);
    assertFalse(problems.isEmpty());
    assertEquals(2, problems.size());
    assertFalse(problems.get(0).getFirstChild().isComma());
    assertFalse(problems.get(1).getFirstChild().isComma());
  }
}