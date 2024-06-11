
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class PeepholeOptimizationsPassTest {
    private PeepholeOptimizationsPass peepholeOptimizationsPass;

    @Before
    public void setUp() {
        AbstractCompiler compiler = new AbstractCompiler();
        peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler);
    }

    @Test
    public void testProcess() {
        Node externs = new Node();
        Node root = new Node();
        peepholeOptimizationsPass.process(externs, root);
    }
}
