import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex();
        optimizer.setSimplex(simplex);
        assertSame(simplex, optimizer.getSimplex());
    }

    @Test
    public void testParseOptimizationData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex();
        org.apache.commons.math3.optimization.InitialGuess initialGuess = new org.apache.commons.math3.optimization.InitialGuess();
        optimizer.parseOptimizationData(simplex, initialGuess);
        assertSame(simplex, optimizer.getSimplex());
    }

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimizeInternal(100, new MultivariateFunction() {
            public double value(double[] point) {
                return 0;
            }
        }, GoalType.MINIMIZE);
        assertNotNull(result);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex();
        simplex.build(new double[2]);
        simplex.evaluate(new MultivariateFunction() {
            public double value(double[] point) {
                return -1;
            }
        }, new Comparator<PointValuePair>() {
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }
}