package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValues;
import org.apache.commons.math3.optim.nonlinear.scalar.RealVector;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Simplex;
import org.junit.Test;

public class SimplexOptimizerTest {
    @Test
    public void testParseOptimizationData() {
        // Test parsing when AbstractSimplex is present
        AbstractSimplex sim = new Simplex();
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        OptimizationData[] data = {new OptimizationData() {
            {
                setSimplex(sim);
            }
        }};
        optimizer.parseOptimizationData(data);
        assertEquals(sim, optimizer.getSimplex());

        // Test parsing when AbstractSimplex is not present
        OptimizationData[] emptyData = new OptimizationData[0];
        assertNull(optimizer.parseOptimizationData(emptyData).getSimplex());
    }

    @Test
    public void testOptimizeWithNullArg() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(1, null, GoalType.MINIMIZE, new double[3]));
    }

    @Test
    public void testOptimizeWithNullFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertThrows(NullPointerException.class, () -> optimizer.optimize(1, null, GoalType.MINIMIZE, new double[3]));
    }

    @Test
    public void testOptimizeWithEmptyGoalType() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(1, f -> 0.0, null, new double[3]));
    }

    @Test
    public void testOptimizeBasic() {
        MultivariateFunction f = x -> FunctionUtils.quadraticForm(x, 2.0);
        SimplexOptimizer optimizer = new SimplexOptimizer();
        double[] startPoint = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(10, f, GoalType.MINIMIZE, startPoint);
        assertEquals(-2.0, result.getValue(), 1e-6);
        assertArrayEquals(startPoint, result.getPoint());
    }

    @Test
    public void testOptimizeMaximize() {
        MultivariateFunction f = x -> -FunctionUtils.quadraticForm(x, 2.0);
        SimplexOptimizer optimizer = new SimplexOptimizer();
        double[] startPoint = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(10, f, GoalType.MAXIMIZE, startPoint);
        assertEquals(2.0, result.getValue(), 1e-6);
        assertArrayEquals(startPoint, result.getPoint());
    }

    @Test
    public void testConvergence() {
        MultivariateFunction f = x -> Math.pow(Math.PI - Math.pow(x[0], 2) - Math.pow(x[1], 2), 2);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-4, 1e-4));
        double[] startPoint = {1.0, 1.0};
        optimizer.optimize(10, f, GoalType.MINIMIZE, startPoint);
        PointValuePair worstPrevious = optimizer.getWorstPrevious();
        PointValuePair worstCurrent = optimizer.getWorstCurrent();
        assertEquals(worstPrevious.getValue(), worstCurrent.getValue(), 1e-4);
        assertEquals(worstPrevious.getPoint(), worstCurrent.getPoint(), 1e-4);
    }

    @Test
    public void testConvergenceEmpty() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-4, 1e-4));
        assertNull(optimizer.getWorstPrevious());
    }

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new Simplex();
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    private MultivariateFunction f(int n) {
        return x -> PointValues.computePointValue(x, new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < n; i++) {
                    sum += Math.pow(x[i], 2);
                }
                return sum;
            }
        });
    }
}