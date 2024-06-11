import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testConstructorDefaultConvergenceChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorCustomConvergenceChecker() {
        SimpleValueChecker checker = new SimpleValueChecker();
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        assertNotNull(optimizer);
    }

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new MultiDirectionalSimplex(new double[] {0.1, 0.2});
        optimizer.setSimplex(simplex);
        assertNotNull(optimizer.getSimplex());
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeWithoutSettingSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(100, new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1];
            }
        }, GoalType.MINIMIZE);
    }

    @Test
    public void testOptimize() {
        AbstractSimplex simplex = new MultiDirectionalSimplex(new double[] {0.1, 0.2});
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(100, new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1];
            }
        }, GoalType.MINIMIZE);
        assertNotNull(result);
    }

}