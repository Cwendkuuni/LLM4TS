import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static com.google.javascript.jscomp.CompilerPasses.DEVIRTUALIZE_PROTOTYPE_METHODS;
import static com.google.javascript.jscomp.CompilerPasses.DEVIRTUALIZE_PROTOTYPE_METHODS_ENABLED;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeTraversingCompiler;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.Node;

public class DevirtualizePrototypeMethodsTest {

  @Test
  public void testDevirtualizePrototypeMethods() {
    String sourceCode =
        "function A() {}\n" +
        "A.prototype.accumulate = function(value) {" +
        "  this.total += value; return this.total" +
        "};\n" +
        "var a = new A();\n" +
        "var total = a.accumulate(2);";

    CompilerOptions options = new CompilerOptions();
    options.setOption(DEVIRTUALIZE_PROTOTYPE_METHODS_ENABLED, true);

    Compiler compiler = new NodeTraversingCompiler(options);
    Node root = AbstractSyntaxTree.parseJs(sourceCode, null, null);

    CompilerPass devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(null, root);

    String expectedCode =
        "var accumulate = function(self, value) {" +
        "  self.total += value; return self.total" +
        "};\n" +
        "var a = new A();\n" +
        "var total = accumulate(a, 2);";

    String actualCode = AbstractSyntaxTree.syntaxTreeToString(root);
    assertEquals(expectedCode, actualCode);
  }
}