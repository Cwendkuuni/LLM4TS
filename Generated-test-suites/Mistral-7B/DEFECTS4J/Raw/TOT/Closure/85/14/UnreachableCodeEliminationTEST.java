import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.test.BaseCompilerTest;
import com.google.javascript.rhino.Node;

public class UnreachableCodeEliminationTest extends BaseCompilerTest {

  public void testSimpleReturn() {
    String jsCode = "function foo() {" +
                     "  return;// dead code" +
                     "  alert('unreachable');" +
                     "}";

    Compiler compiler = newCompiler();
    compiler.addInput("foo.js", jsCode);
    compiler.runPasses();
    Node result = compiler.getOutput("foo.js");
    assertEquals("function foo() {}\n", result.toSource());
  }

  public void testPrototypePropertyWithNoInitialization() {
    String jsCode = "MyClass.prototype.propertyName = \"value\";";

    Compiler compiler = newCompiler();
    compiler.addInput("MyClass.js", jsCode);
    compiler.runPasses();
    Node result = compiler.getOutput("MyClass.js");
    assertEquals("// no change\n", result.toSource());
  }

  public void testDeadCodeInsideConditional() {
    String jsCode = "function foo() {" +
                     "  if (true) {" +
                     "    return;// dead code" +
                     "  } else {" +
                     "    alert('unreachable');" +
                     "  }" +
                     "}";

    Compiler compiler = newCompiler();
    compiler.addInput("foo.js", jsCode);
    compiler.runPasses();
    Node result = compiler.getOutput("foo.js");
    assertEquals("function foo() {}\n", result.toSource());
  }

  public void testDeadCodeInsideLoop() {
    String jsCode = "function foo() {" +
                     "  for (int i = 0; i < 10; i++) {" +
                     "    if (i == 5) {" +
                     "      return;// dead code" +
                     "    }" +
                     "  }" +
                     "  alert('unreachable');" +
                     "}";

    Compiler compiler = newCompiler();
    compiler.addInput("foo.js", jsCode);
    compiler.runPasses();
    Node result = compiler.getOutput("foo.js");
    assertEquals("function foo() {}\n", result.toSource());
  }

  public void testDeadCodeInsideSwitchStatement() {
    String jsCode = "function foo() {" +
                     "  switch (1) {" +
                     "    case 1:" +
                     "      return;// dead code" +
                     "    case 2:" +
                     "      alert('unreachable');" +
                     "      break;" +
                     "  }" +
                     "}";

    Compiler compiler = newCompiler();
    compiler.addInput("foo.js", jsCode);
    compiler.runPasses();
    Node result = compiler.getOutput("foo.js");
    assertEquals("function foo() {}\n", result.toSource());
  }

  public void testDeadCodeInsideTryCatchBlock() {
    String jsCode = "try {" +
                     "  throw new Error('test');" +
                     "} catch (e) {" +
                     "  return;// dead code" +
                     "}";

    Compiler compiler = newCompiler();
    compiler.addInput("foo.js", jsCode);
    compiler.runPasses();
    Node result = compiler.getOutput("foo.js");
    assertEquals("try {" +
                  "  throw new Error('test');" +
                  "} catch (e) {}\n", result.toSource());
  }

  public void testDeadCodeInsideDoWhileLoop() {
    String jsCode = "function foo() {" +
                     "  do {" +
                     "    if (false) {" +
                     "      return;// dead code" +
                     "    }" +
                     "  } while (true);" +
                     "  alert('unreachable');" +
                     "}";

    Compiler compiler = newCompiler();
    compiler.addInput("foo.js", jsCode);
    compiler.runPasses();
    Node result = compiler.getOutput("foo.js");
    assertEquals("function foo() {}\n", result.toSource());
  }

  public void testRemoveNoOpStatements() {
    String jsCode = "function foo() {" +
                     "  a.b.MyClass.prototype.propertyName = null;" +
                     "  true;" +
                     "}";

    Compiler compiler = newCompiler(true, true);
    compiler.addInput("foo.js", jsCode);
    compiler.runPasses();
    Node result = compiler.getOutput("foo.js");
    assertEquals("function foo() {}\n", result.toSource());
  }

  public void testRemoveUnconditionalBranching() {
    String jsCode = "function foo() {" +
                     "  if (true) {" +
                     "    break labelB;" +
                     "  }" +
                     "labelA:" +
                     "  break labelB;" +
                     "}";

    Compiler compiler = newCompiler();
    compiler.addInput("foo.js", jsCode);
    compiler.runPasses();
    Node result = compiler.getOutput("foo.js");
    assertEquals("function foo() {}\n", result.toSource());
  }
}