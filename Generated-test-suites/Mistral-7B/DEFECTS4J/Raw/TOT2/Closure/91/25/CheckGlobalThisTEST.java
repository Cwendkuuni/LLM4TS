import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.*;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;

public class CheckGlobalThisTest {
  @org.junit.Test
  public void testCheckGlobalThis() {
    String sourceCode = "function evil() { this.useful = undefined; }";

    Compiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    compiler.processString(sourceCode, null, null);
    Node ast = compiler.getSourceNode();

    NodeTraversal traversal = new NodeTraversal(ast);
    traversal. Traverse(checker);

    assertTrue(checker.compiler.getDiagnostics().size() > 0);
  }
}