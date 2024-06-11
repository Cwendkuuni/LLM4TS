import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.rhino.Node;

public class PeepholeOptimizationsPassTest {

    private AbstractCompiler compiler;
    private AbstractPeepholeOptimization[] peepholeOptimizations;
    private PeepholeOptimizationsPass pass;
    private Node externs;
    private Node root;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);

        peepholeOptimizations = new AbstractPeepholeOptimization[] {};

        pass = new PeepholeOptimizationsPass(compiler, peepholeOptimizations);

        externs = new Node();
        root = new Node();
    }

    @Test
    public void testConstructor() {
        assertEquals(compiler, pass.getCompiler());
        assertEquals(peepholeOptimizations, pass.peepholeOptimizations);
    }

    @Test
    public void testProcess() {
        PeepholeOptimizationsPass.PeepholeChangeHandler handler = mock(PeepholeOptimizationsPass.PeepholeChangeHandler.class);
        pass.compiler.addChangeHandler(handler);

        pass.process(externs, root);

        verify(compiler).addChangeHandler(handler);
        verify(compiler).removeChangeHandler(handler);
    }

    // TODO: Add test cases for all other methods

}