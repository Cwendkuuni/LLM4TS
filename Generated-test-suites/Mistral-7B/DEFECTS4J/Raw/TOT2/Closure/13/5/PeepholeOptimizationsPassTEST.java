import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;
import org.junit.Test;

public class PeepholeOptimizationsPassTest {

  @Test
  public void testProcess() {
    Node externs = null;
    Node root = null;
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(null, new AbstractPeepholeOptimization[0]);
    pass.process(externs, root);
  }
}