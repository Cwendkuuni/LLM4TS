package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealFunction;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Amoeba;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for MultiDirectional class.
 */
public class MultiDirectionalTest {

    private ObjectiveFunction function;
    private RealFunction testFunction;

    @Before
    public void setUp() {
        testFunction = new TestFunction();
        function = new ObjectiveFunction(testFunction, new SimpleBounds(-10.0, 10.0));
    }

    /**
     * Test MultiDirectional optimizer with default coefficients.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testDefaultCoefficients() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        Amoeba amoeba = new Amoeba(function, GoalType.MINIMIZE, 100, 0.5, 1.0E-6, 1.0E-6);
        optimizer.setMaxIterations(50);
        optimizer.setConvergenceChecker(null);
        NelderMeadSimplex simplex = optimizer.optimize(function, GoalType.MINIMIZE);
        PointValuePair expectedResult = new PointValuePair(0.0, 0.0);
        assertEquals(expectedResult, simplex.getPoint());
    }

    /**
     * Test MultiDirectional optimizer with specified coefficients.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testSpecifiedCoefficients() throws Exception {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.8);
        Amoeba amoeba = new Amoeba(function, GoalType.MINIMIZE, 100, 0.5, 1.0E-6, 1.0E-6);
        optimizer.setMaxIterations(50);
        optimizer.setConvergenceChecker(null);
        NelderMeadSimplex simplex = optimizer.optimize(function, GoalType.MINIMIZE);
        PointValuePair expectedResult = new PointValuePair(0.0, 0.0);
        assertEquals(expectedResult, simplex.getPoint());
    }

    /**
     * Test function for testing MultiDirectional optimizer.
     */
    private static class TestFunction implements RealFunction {

        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2) * Math.exp(-Math.pow(x, 2));
        }

        @Override
        public double derivative(double x) {
            return 2 * Math.sin(x) * Math.cos(x) * Math.exp(-Math.pow(x, 2)) - 2 * Math.pow(x, 2) * Math.exp(-Math.pow(x, 2));
        }

    }

}