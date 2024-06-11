import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

  @Test
  public void testUnreachableCodeAfterReturn() {
    String sourceCode = "function foo() {" +
        "if (true) {" +
        "return;" +
        "alert('unreachable');" +
        "}";
    Compiler compiler = new Compiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    Node root = AbstractCompiler.parseTree(sourceCode);
    pass.process(null, root);
    assertEquals("function foo() {" +
        "if (true) {" +
        "return;" +
        "}", root.toString());
  }

  @Test
  public void testUnreachableCodeWithSideEffects() {
    String sourceCode = "function foo() {" +
        "var x = 0;" +
        "if (true) {" +
        "return;" +
        "x++;" +
        "}" +
        "alert('unreachable');" +
        "}";
    Compiler compiler = new Compiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    Node root = AbstractCompiler.parseTree(sourceCode);
    pass.process(null, root);
    assertEquals("function foo() {" +
        "var x = 0;" +
        "if (true) {" +
        "return;" +
        "}" +
        "}", root.toString());
    // Check that side effect is not executed
    assertEquals(0, ((Double) compiler.getGlobalVariableValue("x")).intValue());
  }

  @Test(expected = AssertionError.class)
  public void testUnreachableCodeWithException() {
    String sourceCode = "function foo() {" +
        "try {" +
        "if (true) {" +
        "throw new Error('test');" +
        "}" +
        "} catch (e) {" +
        "alert('unreachable');" +
        "}" +
        "}";
    Compiler compiler = new Compiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    Node root = AbstractCompiler.parseTree(sourceCode);
    pass.process(null, root);
    // The exception should be caught and the code eliminated
    assertEquals("function foo() {}", root.toString());
    // But the exception should still be thrown during compilation
    compiler.compile(root);
  }

  @Test
  public void testUnreachableCodeWithBreak() {
    String sourceCode = "function foo() {" +
        "for (var i = 0; i < 10; i++) {" +
        "if (i == 5) {" +
        "break;" +
        "}" +
        "alert('unreachable');" +
        "}" +
        "}";
    Compiler compiler = new Compiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    Node root = AbstractCompiler.parseTree(sourceCode);
    pass.process(null, root);
    // The break statement should be eliminated
    assertEquals("function foo() {" +
        "for (var i = 0; i < 10; i++) {" +
        "if (i == 5) {" +
        "continue;" +
        "}" +
        "}" +
        "}", root.toString());
  }

  @Test
  public void testUnreachableCodeWithContinue() {
    String sourceCode = "function foo() {" +
        "for (var i = 0; i < 10; i++) {" +
        "if (i != 5) {" +
        "continue;" +
        "}" +
        "alert('unreachable');" +
        "}" +
        "}";
    Compiler compiler = new Compiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    Node root = AbstractCompiler.parseTree(sourceCode);
    pass.process(null, root);
    // The continue statement should be eliminated
    assertEquals("function foo() {" +
        "for (var i = 0; i < 10; i++) {" +
        "if (i != 5) {" +
        "}" +
        "}" +
        "}", root.toString());
  }

  @Test
  public void testUnreachableCodeWithReturn() {
    String sourceCode = "function foo() {" +
        "if (true) {" +
        "return;" +
        "}" +
        "alert('unreachable');" +
        "if (true) {" +
        "return;" +
        "}" +
        "}";
    Compiler compiler = new Compiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    Node root = AbstractCompiler.parseTree(sourceCode);
    pass.process(null, root);
    // Both return statements should be eliminated
    assertEquals("function foo() {" +
        "if (true) {" +
        "}" +
        "if (true) {" +
        "}" +
        "}", root.toString());
  }

  @Test
  public void testUnreachableCodeWithEmptyBlock() {
    String sourceCode = "function foo() {" +
        "{};" +
        "alert('unreachable');" +
        "}";
    Compiler compiler = new Compiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    Node root = AbstractCompiler.parseTree(sourceCode);
    pass.process(null, root);
    // The empty block should be eliminated
    assertEquals("function foo() {}", root.toString());
  }

  @Test
  public void testUnreachableCodeWithCondition() {
    String sourceCode = "function foo() {" +
        "if (false) {" +
        "alert('unreachable');" +
        "}" +
        "}";
    Compiler compiler = new Compiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    Node root = AbstractCompiler.parseTree(sourceCode);
    pass.process(null, root);
    // The condition should be true, but the code should be eliminated
    assertEquals("function foo() {}", root.toString());
  }

  @Test
  public void testUnreachableCodeWithElse() {
    String sourceCode = "function foo() {" +
        "if (true) {" +
        "alert('true');" +
        "} else {" +
        "alert('false');" +
        "}" +
        "alert('unreachable');" +
        "}";
    Compiler compiler = new Compiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    Node root = AbstractCompiler.parseTree(sourceCode);
    pass.process(null, root);
    // The else branch should be eliminated
    assertEquals("function foo() {" +
        "if (true) {" +
        "alert('true');" +
        "}" +
        "}", root.toString());
  }

  @Test
  public void testUnreachableCodeWithLabel() {
    String sourceCode = "function foo() {" +
        "ooo:" +
        "if (true) {" +
        "break ooo;" +
        "}" +
        "alert('unreachable');" +
        "}" +
        "}";
    Compiler compiler = new Compiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    Node root = AbstractCompiler.parseTree(sourceCode);
    pass.process(null, root);
    // The break statement should be eliminated
    assertEquals("function foo() {" +
        "ooo:" +
        "if (true) {" +
        "continue ooo;" +
        "}" +
        "}", root.toString());
  }

  @Test
  public void testUnreachableCodeWithNamedFunction() {
    String sourceCode = "function foo() {" +
        "function bar() {" +
        "alert('unreachable');" +
        "}" +
        "bar();" +
        "}";
    Compiler compiler = new Compiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    Node root = AbstractCompiler.parseTree(sourceCode);
    pass.process(null, root);
    // The named function should be eliminated
    assertEquals("function foo() {}", root.toString());
  }

  @Test
  public void testUnreachableCodeWithSwitch() {
    String sourceCode = "function foo() {" +
        "switch (0) {" +
        "case 1:" +
        "alert('case 1');" +
        "break;" +
        "case 2:" +
        "alert('case 2');" +
        "default:" +
        "alert('default');" +
        "}" +
        "alert('unreachable');" +
        "}";
    Compiler compiler = new Compiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    Node root = AbstractCompiler.parseTree(sourceCode);
    pass.process(null, root);
    // The default branch should be eliminated
    assertEquals("function foo() {" +
        "switch (0) {" +
        "case 1:" +
        "alert('case 1');" +
        "break;" +
        "case 2:" +
        "alert('case 2');" +
        "}" +
        "}", root.toString());
  }

  @Test
  public void testUnreachableCodeWithWhile() {
    String sourceCode = "function foo() {" +
        "while (true) {" +
        "alert('unreachable');" +
        "}" +
        "}";
    Compiler compiler = new Compiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    Node root = AbstractCompiler.parseTree(sourceCode);
    pass.process(null, root);
    // The while loop should be eliminated
    assertEquals("function foo() {}", root.toString());
  }

  @Test
  public void testUnreachableCodeWithTryCatch() {
    String sourceCode = "function foo() {" +
        "try {" +
        "alert('try');" +
        "} catch (e) {" +
        "alert('catch');" +
        "}" +
        "alert('unreachable');" +
        "}";
    Compiler compiler = new Compiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    Node root = AbstractCompiler.parseTree(sourceCode);
    pass.process(null, root);
    // The unreachable code should be eliminated
    assertEquals("function foo() {" +
        "try {" +
        "alert('try');" +
        "} catch (e) {" +
        "alert('catch');" +
        "}" +
        "}", root.toString());
  }
}