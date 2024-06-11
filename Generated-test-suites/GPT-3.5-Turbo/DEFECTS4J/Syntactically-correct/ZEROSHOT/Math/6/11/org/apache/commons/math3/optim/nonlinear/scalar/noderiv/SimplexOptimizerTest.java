import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer();
    }

    @Test
    public void testOptimizeInternal() {
        int maxEval = 100;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType);
        double[] expectedPoint = {0.0, 0.0};
        double expectedValue = 0.0;

        assertArrayEquals(expectedPoint, result.getPoint(), 0.0001);
        assertEquals(expectedValue, result.getValue(), 0.0001);
    }

    @Test
    public void testSetSimplex() {
        AbstractSimplex simplex = new AbstractSimplex() {
            public void build(double[] startPoint) {
                // implementation
            }
            public void evaluate(MultivariateFunction f, Comparator<PointValuePair> comparator) {
                // implementation
            }
            public void iterate(MultivariateFunction f, Comparator<PointValuePair> comparator) {
                // implementation
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
        };

        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.simplex);
    }
}