import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;
import org.apache.commons.math3.optimization.direct.AbstractSimplex;
import org.apache.commons.math3.optimization.direct.NelderMeadSimplex;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;

public class SimplexOptimizerTest {

    @Test
    public void testConstructorDefault() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithChecker() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithThresholds() {
        SimplexOptimizer optimizer = new SimplexOptimizer(0.001, 0.01);
        assertNotNull(optimizer);
    }

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new NelderMeadSimplex(new double[][] {{1.0}, {2.0}, {3.0}});
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());    
    }

    @Test
    public void testOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0];
            }
        };
        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, new OptimizationData[0]);
        assertEquals(0.0, result.getValue(), 0.0001);
    }

}