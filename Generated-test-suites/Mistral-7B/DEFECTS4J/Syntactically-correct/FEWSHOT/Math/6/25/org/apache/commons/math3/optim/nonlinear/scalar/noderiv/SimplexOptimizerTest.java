package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.cdot.PointValues;
import org.apache.commons.math3.optim.initializer.StartPointGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.Simplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Foolbox2005;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNull(optimizer.getSimplex());

        Simplex simplex = new NelderMeadSimplex(new Foolbox2005(), new SimpleValueChecker());
        optimizer.setSimplex(simplex);
        assertSame(simplex, optimizer.getSimplex());
    }

    @Test
    public void testOptimizeWithNullFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(1, null, GoalType.MINIMIZE, new double[3]));
    }

    @Test
    public void testOptimizeWithNullGoalType() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertThrows(NullPointerException.class, () -> optimizer.optimize(1, new Foolbox2005(), null, new double[3]));
    }

    @Test
    public void testOptimizeWithEmptyData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimize(1, new Foolbox2005(), GoalType.MINIMIZE, new double[3]);
        assertNull(result);
    }

    @Test
    public void testOptimizeWithNoSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(1, new Foolbox2005(), GoalType.MINIMIZE, new double[3]);
        assertNull(optimizer.getSimplex());
    }

    @Test
    public void testOptimizeWithSimpleData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        Simplex simplex = new NelderMeadSimplex(new Foolbox2005(), new SimpleValueChecker());
        optimizer.setSimplex(simplex);

        double[] startPoint = new double[] { 1.0, 2.0, 3.0 };
        PointValuePair result = optimizer.optimize(1, new Foolbox2005(), GoalType.MINIMIZE, new double[][] { startPoint });
        assertNotNull(result);
        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertEquals(startPoint, result.getPoint(), 0.001);
        assertEquals(-15.0, result.getValue(), 0.001);
    }

    @Test
    public void testOptimizeWithMultipleData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        Simplex simplex = new NelderMeadSimplex(new Foolbox2005(), new SimpleValueChecker());
        optimizer.setSimplex(simplex);

        double[][] startPoints = new double[][] {
            new double[] { 1.0, 2.0, 3.0 },
            new double[] { 4.0, 5.0, 6.0 },
            new double[] { 7.0, 8.0, 9.0 }
        };

        PointValues pointValues = optimizer.optimize(3, new Foolbox2005(), GoalType.MINIMIZE, startPoints);
        assertEquals(3, pointValues.size());
        for (int i = 0; i < 3; i++) {
            PointValuePair result = pointValues.getPointValuePair(i);
            assertEquals(GoalType.MINIMIZE, result.getGoalType());
            assertArrayEquals(startPoints[i], result.getPoint(), 0.001);
        }

        double[] expectedValues = new double[] { -15.0, -12.0, -11.0 };
        for (int i = 0; i < 3; i++) {
            PointValuePair result = pointValues.getPointValuePair(i);
            assertEquals(expectedValues[i], result.getValue(), 0.001);
        }
    }

    @Test
    public void testOptimizeWithInitialGuess() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        Simplex simplex = new NelderMeadSimplex(new Foolbox2005(), new SimpleValueChecker());
        optimizer.setSimplex(simplex);

        double[] startPoint = new double[] { 1.0, 2.0, 3.0 };
        PointValuePair result = optimizer.optimize(1, new Foolbox2005(), GoalType.MINIMIZE,
                new OptimizationData[]{ new OptimizationData() {
                    public Object getParameterName() {
                        return "InitialGuess";
                    }

                    public Object getParameterValue() {
                        return new InitialGuess(startPoint);
                    }
                }});
        assertNotNull(result);
        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertEquals(startPoint, result.getPoint(), 0.001);
        assertEquals(-15.0, result.getValue(), 0.001);
    }
}