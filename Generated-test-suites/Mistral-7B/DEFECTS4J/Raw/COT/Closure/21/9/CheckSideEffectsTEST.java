import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

@RunWith(JUnit4.class)
public class CheckSideEffectsTest {

  private static final String CHECK_SIDE_EFFECTS_CLASS =
      "com.google.javascript.jscomp.CheckSideEffects";

  @Test
  public void testCheckSideEffects() {
    AbstractCompiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);

    SourceFile sourceFile = new TestSourceFile(
        "testCheckSideEffects.js",
        "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  var c = a + b;\n" +
        "  x = foo();\n" + // should be '='?
        "  foo();;\n" + // probably just a stray-semicolon. Doesn't hurt to check though
        "  var d = 3;\n" +
        "  return a + b + c + d;\n" +
        "}\n" +
        "function foo() {\n" +
        "  return 'hello';\n" +
        "}\n");

    Node root = NodeFactory.compile(sourceFile);
    checkSideEffects.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    nodeTraversal.traverse(checkSideEffects, root, null);

    List<Node> problemNodes = checkSideEffects.getProblemNodes();

    assertTrue(problemNodes.size() == 3);

    Node xAssignment = problemNodes.get(0);
    assertTrue(xAssignment.isAssign());

    Node uselessSemicolon = problemNodes.get(1);
    assertTrue(uselessSemicolon.isComma());

    Node fooCallWithoutAssignment = problemNodes.get(2);
    assertTrue(fooCallWithoutAssignment.isCall());
  }

  @Test(expected = AssertionError.class)
  public void testProtectSideEffects() {
    AbstractCompiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, true);

    SourceFile sourceFile = new TestSourceFile(
        "testProtectSideEffects.js",
        "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  var c = a + b;\n" +
        "  JSCOMPILER_PRESERVE(a + b); // side-effect free code\n" +
        "  x = foo();\n" + // should be '='?
        "  foo();;\n" + // probably just a stray-semicolon. Doesn't hurt to check though
        "  var d = 3;\n" +
        "  return a + b + c + d;\n" +
        "}\n" +
        "function foo() {\n" +
        "  return 'hello';\n" +
        "}\n");

    Node root = NodeFactory.compile(sourceFile);
    checkSideEffects.process(null, root);
  }

  @Test
  public void testStripProtection() {
    AbstractCompiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, true);
    StripProtection stripProtection = new StripProtection(compiler);

    SourceFile sourceFile = new TestSourceFile(
        "testStripProtection.js",
        "function test() {\n" +
        "  JSCOMPILER_PRESERVE(1 + 1); // side-effect free code\n" +
        "  JSCOMPILER_PRESERVE(JSCOMPILER_PRESERVE(1) + 1); // should be removed\n" +
        "}\n");

    Node root = NodeFactory.compile(sourceFile);
    checkSideEffects.process(null, root);
    stripProtection.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    nodeTraversal.traverse(checkSideEffects, root, null);

    List<Node> problemNodes = checkSideEffects.getProblemNodes();

    assertTrue(problemNodes.size() == 1);

    Node jsCompilerPreserveCall = problemNodes.get(0);
    assertTrue(jsCompilerPreserveCall.isCall());

    Node funcName = jsCompilerPreserveCall.getFirstChild();
    assertTrue(funcName.isName());
    assertTrue(funcName.getString().equals("JSCOMPILER_PRESERVE"));

    assertTrue(jsCompilerPreserveCall.getLastChild().isNumber(2));
  }

  @Test
  public void testProtectAndStripProtection() {
    AbstractCompiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, true);
    StripProtectionstripProtection = new StripProtection(compiler);

    SourceFile sourceFile = new TestSourceFile(
        "testProtectAndStripProtection.js",
        "function test() {\n" +
        "  JSCOMPILER_PRESERVE(1 + 1); // side-effect free code\n" +
        "  JSCOMPILER_PRESERVE(JSCOMPILER_PRESERVE(1) + 1); // should be removed\n" +
        "  JSCOMPILER_PRESERVE(1 + 1); // side-effect free code\n" +
        "  JSCOMPILER_PRESERVE(JSCOMPILER_PRESERVE(1) + 1); // should be removed\n" +
        "}\n");

    Node root = NodeFactory.compile(sourceFile);
    checkSideEffects.process(null, root);
    stripProtection.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    nodeTraversal.traverse(checkSideEffects, root, null);

    List<Node> problemNodes = checkSideEffects.getProblemNodes();

    assertTrue(problemNodes.size() == 2);

    Node jsCompilerPreserveCall1 = problemNodes.get(0);
    assertTrue(jsCompilerPreserveCall1.isCall());

    Node funcName1 = jsCompilerPreserveCall1.getFirstChild();
    assertTrue(funcName1.isName());
    assertTrue(funcName1.getString().equals("JSCOMPILER_PRESERVE"));

    assertTrue(jsCompilerPreserveCall1.getLastChild().isNumber(2));

    Node jsCompilerPreserveCall2 = problemNodes.get(1);
    assertTrue(jsCompilerPreserveCall2.isCall());

    Node funcName2 = jsCompilerPreserveCall2.getFirstChild();
    assertTrue(funcName2.isName());
    assertTrue(funcName2.getString().equals("JSCOMPILER_PRESERVE"));

    assertTrue(jsCompilerPreserveCall2.getLastChild().isNumber(2));
  }
}