import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optimization.direct.AbstractSimplex;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimplexOptimizerTest {

    private SimplexOptimizer optimizer;
    private AbstractSimplex simplex;
    private MultivariateFunction function;

    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-5, 1e-5));
        simplex = new Simplex(2);
        function = new RidgedMultiPeakFunction(2);
    }

    @Test
    public void testSimpleOptimization() {
        optimizer.optimize(100, function, GoalType.MINIMIZE, new double[]{1.0, 1.0});
        PointValuePair result = optimizer.getPointValuePair();
        Assert.assertEquals(0.0, result.getValue(), 1e-5);
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 1e-5);
    }

    @Test
    public void testMaximization() {
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.optimize(100, function, GoalType.MAXIMIZE, new double[]{1.0, 1.0});
        PointValuePair result = optimizer.getPointValuePair();
        Assert.assertEquals(2.0, result.getValue(), 1e-5);
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 1e-5);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullSimplex() {
        optimizer.setSimplex(null);
        optimizer.optimize(100, function, GoalType.MINIMIZE, new double[]{1.0, 1.0});
    }

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        optimizer.setSimplex(simplex);
        optimizer.optimize(100, null, GoalType.MINIMIZE, new double[]{1.0, 1.0});
    }

    @Test
    public void testConvergenceCheck() {
        optimizer.setConvergenceChecker(new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                double diff = Math.abs(previous.getValue() - current.getValue());
                return diff < 1e-6;
            }
        });
        optimizer.optimize(100, function, GoalType.MINIMIZE, new double[]{1.0, 1.0});
        PointValuePair result = optimizer.getPointValuePair();
        Assert.assertEquals(0.0, result.getValue(), 1e-5);
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 1e-5);
    }

    @Test
    public void testInvalidGoalType() {
        optimizer.setGoalType(GoalType.MAXIMIZE_INFO);
        try {
            optimizer.optimize(100, function, GoalType.MAXIMIZE_INFO, new double[]{1.0, 1.0});
            Assert.fail("An IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testChangeNumberOfParameters() {
        optimizer.optimize(100, function, GoalType.MINIMIZE, new double[]{1.0, 1.0});
        optimizer.setSimplex(new Simplex(3));
        optimizer.optimize(100, function, GoalType.MINIMIZE, new double[]{1.0, 1.0, 1.0});
    }
}

class RidgedMultiPeakFunction implements MultivariateFunction {
    private int peakNumber;
    private double[] peakHeights;
    private double ridgeHeight;
    private int dimension;

    public RidgedMultiPeakFunction(int peakNumber, double ridgeHeight) {
        this.peakNumber = peakNumber;
        this.ridgeHeight = ridgeHeight;
        this.dimension = 2;
        peakHeights = new double[peakNumber];
        for (int i = 0; i < peakNumber; i++) {
            peakHeights[i] = ridgeHeight + 1.0 + i;
        }
    }

    @Override
    public double value(double[] point) {
        int index = (int) Math.floor(Math.max(point[0], point[1])) % peakNumber;
        return peakHeights[index];
    }
}