import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testConstructorDefault() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithConvergenceChecker() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithThresholds() {
        double rel = 0.1;
        double abs = 0.01;
        SimplexOptimizer optimizer = new SimplexOptimizer(rel, abs);
        assertNotNull(optimizer);
    }

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new Simplex();
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithoutSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(100, new MultivariateFunction() {
            public double value(double[] point) {
                return 0;
            }
        }, GoalType.MINIMIZE);
    }

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new Simplex();
        optimizer.setSimplex(simplex);

        PointValuePair result = optimizer.optimizeInternal(100, new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        }, GoalType.MINIMIZE);

        assertEquals(0.0, result.getValue(), 1e-10);
    }

    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new Simplex();
        double[] startPoint = {1.0, 1.0};
        simplex.build(startPoint);
        optimizer.setSimplex(simplex);

        PointValuePair result = optimizer.doOptimize();

        assertEquals(0.0, result.getValue(), 1e-10);
    }
}