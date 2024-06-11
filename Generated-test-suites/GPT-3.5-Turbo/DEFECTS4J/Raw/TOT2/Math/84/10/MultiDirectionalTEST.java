import static org.junit.Assert.*;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testDefaultConstructor() {
        MultiDirectional md = new MultiDirectional();
        assertNotNull(md);
        assertEquals(2.0, md.khi, 0.0);
        assertEquals(0.5, md.gamma, 0.0);
    }

    @Test
    public void testCustomConstructor() {
        MultiDirectional md = new MultiDirectional(3.0, 0.7);
        assertNotNull(md);
        assertEquals(3.0, md.khi, 0.0);
        assertEquals(0.7, md.gamma, 0.0);
    }

    @Test
    public void testIterateSimplex() {
        MultiDirectional md = new MultiDirectional();
        // Add test for iterateSimplex method
        // Test for FunctionEvaluationException, OptimizationException and IllegalArgumentException
    }

    @Test
    public void testEvaluateNewSimplex() {
        MultiDirectional md = new MultiDirectional();
        // Add test for evaluateNewSimplex method
        // Test for FunctionEvaluationException, OptimizationException
    }

    @Test
    public void testEdgeCases() {
        MultiDirectional md = new MultiDirectional();
        // Add test cases for edge cases
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        MultiDirectional md = new MultiDirectional();
        // Add test case to check for IllegalArgumentException
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        MultiDirectional md = new MultiDirectional();
        // Add test case to check for FunctionEvaluationException
    }

    @Test(expected = OptimizationException.class)
    public void testOptimizationException() {
        MultiDirectional md = new MultiDirectional();
        // Add test case to check for OptimizationException
    }

}