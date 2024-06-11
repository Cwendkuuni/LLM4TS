import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testDefaultConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer);
    }

    @Test
    public void testConvergenceCheckerConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        assertNotNull(optimizer);
    }

    @Test
    public void testRelAbsConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer(0.001, 0.01);
        assertNotNull(optimizer);
    }

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new NelderMeadSimplex(new double[][] { { 1.0 }, { 2.0 } });
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testInvalidOptimizationData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        try {
            optimizer.parseOptimizationData(new Object());
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }

}