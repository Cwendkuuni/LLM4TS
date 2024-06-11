package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.goaltype.GoalType;
import org.apache.commons.math3.optimization.multivariate.BracketFinder;
import org.apache.commons.math3.optimization.multivariate.BrentOptimizer;
import org.apache.commons.math3.optimization.multivariate.PowellOptimizer;
import org.apache.commons.math3.optimization.multivariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateFunction;
import org.junit.Test;

public class PowellOptimizerTest {

    @Test
    public void testPowellOptimizer() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        // Test with a simple test function
        UnivariateFunction function = new SimpleUnivariateFunction("sin") {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        assertNotNull(result);
        assertEquals(0.0, result.getValue(), 1e-6);
        assertEquals(Math.PI / 2.0, result.getPoint(), 1e-6);

        // Test with a multivariate function
        MultivariateFunction multivariateFunction = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) * Math.cos(x[1]), 2.0);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };
        result = optimizer.optimize(multivariateFunction, GoalType.MINIMIZE);
        assertNotNull(result);
        assertEquals(-1.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[] { Math.PI / 2.0, 0.0 }, result.getPoint(), 1e-6);

        // Test with a user-defined convergence checker
        PowellOptimizer optimizerCustomChecker = new PowellOptimizer(1e-4, 1e-4, new SimpleUnivariateValueChecker(1e-8, 1e-8));
        UnivariateFunction functionCustomChecker = new SimpleUnivariateFunction("sin") {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BracketFinder bracketFinder = new BracketFinder();
        PointValuePair bracket = bracketFinder.search(functionCustomChecker, GoalType.MINIMIZE, 0, 1);
        UnivariatePointValuePair optimizedPoint = optimizerCustomChecker.optimize(functionCustomChecker, GoalType.MINIMIZE, bracket.getLo(), bracket.getHi(), bracket.getMid());
        assertNotNull(optimizedPoint);
        assertEquals(0.0, optimizedPoint.getValue(), 1e-8);
        assertEquals(Math.PI / 2.0, optimizedPoint.getPoint(), 1e-8);
    }

    @Test
    public void testLineSearch() {
        BrentOptimizer lineSearch = new BrentOptimizer(1e-6, 1e-6, new SimpleUnivariateValueChecker(1e-6, 1e-6));
        UnivariateFunction function = new SimpleUnivariateFunction("sin") {
            @Override
            public double value(double alpha) {
                return Math.sin(alpha);
            }
        };
        PointValuePair result = lineSearch.optimize(function, GoalType.MINIMIZE, -1.0, 1.0, 0.0);
        assertNotNull(result);
        assertEquals(0.0, result.getValue(), 1e-6);
        assertEquals(-Math.PI / 2.0, result.getPoint(), 1e-6);
    }
}