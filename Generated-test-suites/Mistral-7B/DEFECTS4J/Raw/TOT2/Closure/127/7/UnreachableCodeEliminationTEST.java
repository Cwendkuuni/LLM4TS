import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import org.junit.Assert;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

  @Test
  public void testEmptyFunction() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = new Node("function() {}");
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);
    String expected = "function() {}";
    String actual = compiler.toSource(root);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFunctionWithReturnAndDeadCode() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = new Node("function foo() {" +
        "return;" +
        "alert('unreachable');" +
        "}");
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);
    String expected = "function foo() {return;}";
    String actual = compiler.toSource(root);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFunctionWithDeadCodeAndSideEffects() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = new Node("function foo() {" +
        "a.b.MyClass.prototype.propertyName;" +
        "return;" +
        "alert('unreachable');" +
        "}");
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);
    String expected = "function foo() {return;}";
    String actual = compiler.toSource(root);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFunctionWithExceptionHandling() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = new Node("function foo() {" +
        "try {" +
        "  throw new Error('Error');" +
        "} catch(e) {" +
        "  alert('Caught error: ' + e.message);" +
        "}" +
        "alert('Function completed normally');" +
        "}");
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);
    String expected = "function foo() {" +
        "try {" +
        "  throw new Error('Error');" +
        "} catch(e) {" +
        "  alert('Caught error: ' + e.message);" +
        "}" +
        "}";
    String actual = compiler.toSource(root);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFunctionWithReturnAndEmptyBlock() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = new Node("function foo() {" +
        "return;" +
        "}");
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);
    String expected = "function foo() {return;}";
    String actual = compiler.toSource(root);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFunctionWithBreakAndContinue() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = new Node("function foo() {" +
        "for (int i = 0; i < 10; i++) {" +
        "  if (i == 5) {" +
        "    break;" +
        "  } else if (i == 7) {" +
        "    continue;" +
        "  } else {" +
        "    // This code is dead because the loop will never reach here" +
        "    // after the break or continue statements." +
        "    System.out.println(i);" +
        "  }" +
        "}" +
        "}");
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);
    String expected = "function foo() {" +
        "for (int i = 0; i < 10; i++) {" +
        "  if (i == 5) {" +
        "    break;" +
        "  } else if (i == 7) {" +
        "    continue;" +
        "  }" +
        "}" +
        "}";
    String actual = compiler.toSource(root);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFunctionWithReturnAndNoSideEffects() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = new Node("function foo() {" +
        "return true;" +
        "}");
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);
    String expected = "function foo() {return true;}" +
        "function foo() {}";
    String actual = compiler.toSource(root);
    Assert.assertTrue(actual.contains(expected));
  }

  @Test
  public void testFunctionWithReturnAndSideEffects() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = new Node("function foo() {" +
        "return true;" +
        "document.getElementById('myDiv').style.color = 'red';" +
        "}");
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);
    String expected = "function foo() {return true;}" +
        "function foo() {" +
        "  document.getElementById('myDiv').style.color = 'red';" +
        "}" +
        "function foo() {return true;}";
    String actual = compiler.toSource(root);
    Assert.assertTrue(actual.contains(expected));
  }

  @Test
  public void testFunctionWithDeadCodeAndNoSideEffects() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = new Node("function foo() {" +
        "a.b.MyClass.prototype.propertyName;" +
        "return true;" +
        "}");
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);
    String expected = "function foo() {return true;}";
    String actual = compiler.toSource(root);
    Assert.assertEquals(expected, actual);
  }
}