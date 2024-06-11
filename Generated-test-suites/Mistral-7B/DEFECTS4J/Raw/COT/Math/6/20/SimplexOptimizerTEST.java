package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeWeightedSum;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleOptimizationData;
import org.apache.commons.math3.optim.nonlinear.vector.general.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.vector.general.AbstractVectorFunction;
import org.junit.Test;

/**
 * Test class for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {
    /** Simple 2D function for testing. */
    private static final MultivariateFunction FUNCTION = new AbstractVectorFunction(2) {
        @Override
        public double value(double[] x) {
            return FunctionUtils.pow(FunctionUtils.sum(x), 2);
        }
    };

    /** Simple 2D simplex for testing. */
    private static final AbstractSimplex SIMPLEX = new SimpleSimplex(3);

    /**
     * Tests {@link SimplexOptimizer#SimplexOptimizer()} constructor.
     * @throws NullArgumentException if {@code simplex} is {@code null}.
     */
    @Test
    public void testConstructor() throws NullArgumentException {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer.getSimplex());
    }

    /**
     * Tests {@link SimplexOptimizer#SimplexOptimizer(SimpleValueChecker)}
     * constructor.
     */
    @Test
    public void testConstructorChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1.0e-6, 1.0e-6));
        assertNotNull(optimizer.getSimplex());
        assertEquals(1.0e-6, optimizer.getConvergenceChecker().getRelativeThreshold(), 1.0e-8);
        assertEquals(1.0e-6, optimizer.getConvergenceChecker().getAbsoluteThreshold(), 1.0e-8);
    }

    /**
     * Tests {@link SimplexOptimizer#SimplexOptimizer(double, double)}
     * constructor.
     */
    @Test
    public void testConstructorThresholds() {
        SimplexOptimizer optimizer = new SimplexOptimizer(1.0e-6, 1.0e-6);
        assertNotNull(optimizer.getSimplex());
        assertEquals(1.0e-6, optimizer.getConvergenceChecker().getRelativeThreshold(), 1.0e-8);
        assertEquals(1.0e-6, optimizer.getConvergenceChecker().getAbsoluteThreshold(), 1.0e-8);
    }

    /**
     * Tests {@link SimplexOptimizer#setSimplex(AbstractSimplex)} method.
     * @throws NullArgumentException if {@code simplex} is {@code null}.
     */
    @Test
    public void testSetSimplex() throws NullArgumentException {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(SIMPLEX);
        assertSame(SIMPLEX, optimizer.getSimplex());
    }

    /**
     * Tests {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[])}
     * method with {@link GoalType#MINIMIZE}.
     */
    @Test
    public void testMinimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(SIMPLEX);

        double[] startPoint = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(100, FUNCTION, GoalType.MINIMIZE, startPoint);

        assertNotNull(result);
        assertEquals(0.0, result.getValue(), 1.0e-6);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1.0e-6);
    }

    /**
     * Tests {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[])}
     * method with {@link GoalType#MAXIMIZE}.
     */
    @Test
    public void testMaximize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(SIMPLEX);

        double[] startPoint = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(100, FUNCTION, GoalType.MAXIMIZE, startPoint);

        assertNotNull(result);
        assertEquals(1.0, Math.pow(result.getValue(), 0.5), 1.0e-6);
        assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 1.0e-6);
    }

    /**
     * Tests {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[], OptimizationData[])}
     * method with {@link GoalType#MINIMIZE}.
     */
    @Test
    public void testMinimizeWithData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(SIMPLEX);

        double[] startPoint = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(100, FUNCTION, GoalType.MINIMIZE, startPoint,
                new SimpleOptimizationData(new double[]{0.0, 0.0}));

        assertNotNull(result);
        assertEquals(0.0, result.getValue(), 1.0e-6);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1.0e-6);
    }

    /**
     * Tests {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[], OptimizationData[])}
     * method with {@link GoalType#MAXIMIZE}.
     */
    @Test
    public void testMaximizeWithData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(SIMPLEX);

        double[] startPoint = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(100, FUNCTION, GoalType.MAXIMIZE, startPoint,
                new SimpleOptimizationData(new double[]{0.0, 0.0}));

        assertNotNull(result);
        assertEquals(1.0, Math.pow(result.getValue(), 0.5), 1.0e-6);
        assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 1.0e-6);
    }

    /**
     * Tests {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[], OptimizationData[])}
     * method with {@link GoalTypeWeightedSum}.
     */
    @Test
    public void testWeightedSum() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(SIMPLEX);

        double[] startPoint = {1.0, 1.0};
        double[] weights = {0.5, 0.5};
        PointValuePair result = optimizer.optimize(100, new ObjectiveFunction(FUNCTION),
                GoalTypeWeightedSum.IDENTITY, startPoint, new SimpleOptimizationData(weights));

        assertNotNull(result);
        assertEquals(1.0 / Math.sqrt(2.0), result.getValue(), 1.0e-6);
        assertArrayEquals(new double[]{0.5, 0.5}, result.getPoint(), 1.0e-6);
    }

    /**
     * Test that {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[])}
     * throws an exception if {@code simplex} is not set.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithoutSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(100, FUNCTION, GoalType.MINIMIZE, new double[]{0.0, 0.0});
    }
}

class SimpleSimplex extends AbstractSimplex {
    public SimpleSimplex(int size) {
        super(size);
    }

    @Override
    public void build(double[] initialPoint) {
        // Implementation for testing purposes
    }

    @Override
    public void evaluate(MultivariateFunction function, Comparator<PointValuePair> comparator) {
        // Implementation for testing purposes
    }

    @Override
    public PointValuePair[] getPoints() {
        // Implementation for testing purposes
        return new PointValuePair[0];
    }

    @Override
    public int getSize() {
        return 3;
    }

    @Override
    public void iterate(MultivariateFunction function, Comparator<PointValuePair> comparator) {
        // Implementation for testing purposes
    }
}