import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PeepholeOptimizationsPassTest {

    private PeepholeOptimizationsPass optimizationsPass;
    private AbstractCompiler compiler;
    private Node externs;
    private Node root;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        optimizationsPass = new PeepholeOptimizationsPass(compiler);
        externs = new Node();
        root = new Node();
    }

    @Test
    public void process_WithNullExternsAndRoot_ShouldNotCrash() {
        optimizationsPass.process(null, null);
    }

    @Test
    public void process_WithValidExternsAndRoot_ShouldNotCrash() {
        optimizationsPass.process(externs, root);
    }

    @Test
    public void process_WithExternsAndNullRoot_ShouldNotCrash() {
        optimizationsPass.process(externs, null);
    }

    @Test
    public void getCompiler_ShouldReturnSameCompilerInstance() {
        assertEquals(compiler, optimizationsPass.getCompiler());
    }

    @Test
    public void traverse_WithNullNode_ShouldNotCrash() {
        optimizationsPass.traverse(null);
    }

    @Test
    public void visit_WithNullNode_ShouldNotCrash() {
        optimizationsPass.visit(null);
    }

    @Test
    public void beginTraversal_ShouldNotCrash() {
        optimizationsPass.beginTraversal();
    }

    @Test
    public void endTraversal_ShouldNotCrash() {
        optimizationsPass.endTraversal();
    }

}