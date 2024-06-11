package org.apache.math.analysis.solvers.junit4;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

public class UnivariateRealSolverUtilsTest {
    private UnivariateRealFunction dummyFunction;
    private UnivariateRealFunction constantFunction;
    private UnivariateRealFunction linearFunction;
    private UnivariateRealFunction quadraticFunction;

    @Before
    public void setup() {
        dummyFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        };

        constantFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 5;
            }
        };

        linearFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 2 * x + 3;
            }
        };

        quadraticFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4 * x + 2;
            }
        };
    }

    @Test
    public void testSolve() {
        try {
            double x = UnivariateRealSolverUtils.solve(dummyFunction, 1.0, 5.0);
            Assert.assertEquals(0.0, x, 0.0);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testSolveWithAccuracy() {
        try {
            double x = UnivariateRealSolverUtils.solve(dummyFunction, 1.0, 5.0, 0.0001);
            Assert.assertEquals(0.0, x, 0.0001);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testBracket() {
        try {
            double[] bracket = UnivariateRealSolverUtils.bracket(dummyFunction, 1.0, 0.0, 5.0);
            Assert.assertEquals(-1.0, bracket[0], 0.0);
            Assert.assertEquals(1.0, bracket[1], 0.0);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testBracketWithIterations() {
        try {
            double[] bracket = UnivariateRealSolverUtils.bracket(dummyFunction, 1.0, 0.0, 5.0, 10);
            Assert.assertEquals(-1.0, bracket[0], 0.0);
            Assert.assertEquals(1.0, bracket[1], 0.0);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testMidpoint() {
        double midpoint = UnivariateRealSolverUtils.midpoint(1.0, 5.0);
        Assert.assertEquals(3.0, midpoint, 0.0);
    }
}