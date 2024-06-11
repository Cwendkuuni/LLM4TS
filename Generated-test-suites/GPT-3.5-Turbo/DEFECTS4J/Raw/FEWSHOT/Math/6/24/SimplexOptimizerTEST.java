import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testConstructorWithConvergenceChecker() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithThresholds() {
        double rel = 1e-6;
        double abs = 1e-4;
        SimplexOptimizer optimizer = new SimplexOptimizer(rel, abs);
        assertNotNull(optimizer);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeWithNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize() {
        AbstractSimplex simplex = new AbstractSimplex() {
            public void build(double[] startPoint) {
                // Build simplex
            }

            public void evaluate(MultivariateFunction evalFunc, Comparator<PointValuePair> comparator) {
                // Evaluate simplex
            }

            public int getSize() {
                return 0;
            }

            public PointValuePair getPoint(int index) {
                return null;
            }

            public PointValuePair[] getPoints() {
                return null;
            }

            public void iterate(MultivariateFunction evalFunc, Comparator<PointValuePair> comparator) {
                // Iterate simplex
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }

    @Test
    public void testOptimize() {
        int maxEval = 1000;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return 0.0;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        AbstractSimplex simplex = new AbstractSimplex() {
            public void build(double[] startPoint) {
                // Build simplex
            }

            public void evaluate(MultivariateFunction evalFunc, Comparator<PointValuePair> comparator) {
                // Evaluate simplex
            }

            public int getSize() {
                return 0;
            }

            public PointValuePair getPoint(int index) {
                return null;
            }

            public PointValuePair[] getPoints() {
                return null;
            }

            public void iterate(MultivariateFunction evalFunc, Comparator<PointValuePair> comparator) {
                // Iterate simplex
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(maxEval, f, goalType);
        assertNotNull(result);
    }
}