import static org.junit.Assert.assertThat;
import static com.google.common.truth.Truth.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;
import com.google.javascript.jscomp.testing.TypeCheckerFacade;
import org.junit.Before;
import org.junit.Test;

public class PeepholeOptimizationsPassTest {
  private Compiler compiler;
  private PeepholeOptimizationsPass pass;

  @Before
  public void setUp() {
    compiler = new Compiler();
    pass = new PeepholeOptimizationsPass(compiler, new ConstantFoldingPass());
  }

  @Test
  public void testProcess() {
    Node root = TypeCheckerFacade.checkType(
        "function foo() { return 42 + 42; }", new NodeOperations());
    pass.process(null, root);
    Node result = compiler.getOutput();
    assertThat(result.toJavaString()).isEqualTo("function foo() { return 84; }");
  }
}