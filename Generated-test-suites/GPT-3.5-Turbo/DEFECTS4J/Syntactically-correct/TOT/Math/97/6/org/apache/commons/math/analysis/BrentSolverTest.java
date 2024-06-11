package org.spaceroots.mantissa.roots;

import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BrentSolverTest {

    private static final double DELTA = 1e-15;

    @Test
    public void testFindRoot_typicalCase() throws FunctionException {
        // Arrange
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x1, double f1, double x2, double f2) {
                return f1 * f2 >= 0 ? HIGH : LOW;
            }
        };
        int maxIter = 1000;
        double x0 = -10;
        double f0 = function.valueAt(x0);
        double x1 = 10;
        double f1 = function.valueAt(x1);

        // Act
        boolean found = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        // Assert
        Assert.assertTrue(found);
        Assert.assertEquals(2.0, solver.getRoot(), DELTA);
    }

    @Test
    public void testFindRoot_edgeCases() throws FunctionException {
        // Arrange
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x1, double f1, double x2, double f2) {
                return f1 * f2 >= 0 ? HIGH : LOW;
            }
        };
        int maxIter = 1000;
        double x0 = Double.MIN_VALUE;
        double f0 = function.valueAt(x0);
        double x1 = Double.MAX_VALUE;
        double f1 = function.valueAt(x1);

        // Act
        boolean found = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        // Assert
        Assert.assertTrue(found);
        Assert.assertEquals(2.0, solver.getRoot(), DELTA);
    }

    @Test
    public void testFindRoot_errorScenario() throws FunctionException {
        // Arrange
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                throw new FunctionException("Cannot evaluate function");
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x1, double f1, double x2, double f2) {
                return f1 * f2 >= 0 ? HIGH : LOW;
            }
        };
        int maxIter = 1000;
        double x0 = 0;
        double f0 = function.valueAt(x0);
        double x1 = 10;
        double f1 = function.valueAt(x1);

        // Act
        boolean found = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        // Assert
        Assert.assertFalse(found);
        Assert.assertEquals(Double.NaN, solver.getRoot(), DELTA);
    }
}