import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunction;
import org.apache.commons.math3.optim.simple.AbstractSimplex;
import org.apache.commons.math3.optim.simple.SimplexPoint;
import org.apache.commons.math3.test.AbstractTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimplexOptimizerTest extends AbstractTest {

    @Test
    public void testEmpty() {
        try {
            new SimplexOptimizer();
            fail("Should throw NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSimpleValueChecker() {
        AbstractSimplex simplex = new SimpleSimplex();
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(10, new MockFunction(), GoalType.MINIMIZE, new Object[]{});
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(new SimpleValueChecker(), optimizer.getConvergenceChecker());
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testCustomValueChecker() {
        AbstractSimplex simplex = new SimpleSimplex();
        SimplexOptimizer optimizer = new SimplexOptimizer(new CustomValueChecker());
        optimizer.optimize(10, new MockFunction(), GoalType.MINIMIZE, new Object[]{});
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertNotNull(optimizer.getConvergenceChecker());
        assertNotSame(new SimpleValueChecker(), optimizer.getConvergenceChecker());
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testCustomSimplex() {
        AbstractSimplex simplex = new CustomSimplex();
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(simplex);
        optimizer.optimize(10, new MockFunction(), GoalType.MINIMIZE, new Object[]{simplex});
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testMultipleCalls() {
        AbstractSimplex simplex = new SimpleSimplex();
        SimplexOptimizer optimizer = new SimplexOptimizer();
        double[] startPoint = new double[]{1.0, 1.0};
        optimizer.optimize(10, new MockFunction(), GoalType.MINIMIZE, new Object[]{startPoint});
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(simplex, optimizer.getSimplex());
        PointValuePair result = optimizer.optimize(10, new MockFunction(), GoalType.MINIMIZE, new Object[]{startPoint});
        assertEquals(result.getValue(), 0.0, 1e-15);
        assertEquals(simplex, optimizer.getSimplex());
    }

    private static class MockFunction implements MultivariateFunction {
        @Override
        public double value(double[] point) {
            return 0.0 - Math.pow(point[0] * point[0] + point[1] * point[1], 2);
        }
    }

    private static class CustomSimplex extends AbstractSimplex {
        @Override
        protected void build(double[] initialPoint) {
            // Empty implementation
        }

        @Override
        public SimplexPoint getPoint(int index) {
            return new SimplexPoint(new double[]{0.0, 0.0});
        }

        @Override
        public int getSize() {
            return 1;
        }
    }

    private static class CustomValueChecker
            extends SimpleValueChecker {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            return previous.getValue() < 0.0 && current.getValue() < 0.0;
        }
    }
}