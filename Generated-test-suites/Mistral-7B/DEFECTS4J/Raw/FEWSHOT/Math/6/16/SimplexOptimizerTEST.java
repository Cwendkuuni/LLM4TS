package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.goaltypes.GoalType;
import org.apache.commons.math3.optimization.initialguess.InitialGuess;
import org.apache.commons.math3.optimization.nonlinear.MultivariateFunction;
import org.apache.commons.math3.optimization.simplextree.AbstractSimplex;
import org.apache.commons.math3.optimization.simplextree.AmaraSimplex;
import org.apache.commons.math3.optimization.simplextree.NelderMeadSimplex;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testSimplexOptimizer() {
        // Test with Amara simplex
        AbstractSimplex simplex = new AmaraSimplex();
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.setSimplex(simplex);

        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, new InitialGuess[0]);
        assertTrue(result != null);
        assertTrue(result.getValue() < 1.0);

        // Test with Nelder-Mead simplex
        simplex = new NelderMeadSimplex();
        optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.setSimplex(simplex);

        result = optimizer.optimize(100, function, GoalType.MINIMIZE, new InitialGuess[0]);
        assertTrue(result != null);
        assertTrue(result.getValue() < 1.0);

        // Test null check
        optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.setSimplex(null);
        assertNull(optimizer.optimize(100, function, GoalType.MINIMIZE, new InitialGuess[0]));
    }

    @Test
    public void testSetSimplex() {
        AbstractSimplex simplex = new AmaraSimplex();
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());

        optimizer.setSimplex(simplex);
        assertSame(simplex, optimizer.getSimplex());

        simplex = new NelderMeadSimplex();
        optimizer.setSimplex(simplex);
        assertSame(simplex, optimizer.getSimplex());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetSimplexNull() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.setSimplex(null);
    }

    @Test
    public void testParseOptimizationData() {
        AbstractSimplex simplex1 = new AmaraSimplex();
        OptimizationData data1 = new OptimizationData() {
            @Override
            public InitialGuess getInitialGuess() {
                return null;
            }

            @Override
            public AbstractSimplex getSimplex() {
                return simplex1;
            }
        };

        AbstractSimplex simplex2 = new NelderMeadSimplex();
        OptimizationData data2 = new OptimizationData() {
            @Override
            public InitialGuess getInitialGuess() {
                return null;
            }

            @Override
            public AbstractSimplex getSimplex() {
                return simplex2;
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.parseOptimizationData(data1, data2);

        AbstractSimplex simplex = optimizer.getSimplex();
        assertSame(simplex1, simplex);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOptimizationDataWrongOrder() {
        AbstractSimplex simplex = new AmaraSimplex();
        OptimizationData data1 = new OptimizationData() {
            @Override
            public InitialGuess getInitialGuess() {
                return null;
            }
        };

        OptimizationData data2 = new OptimizationData() {
            @Override
            public AbstractSimplex getSimplex() {
                return simplex;
            }

            @Override
            public InitialGuess getInitialGuess() {
                return null;
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.parseOptimizationData(data2, data1);
    }

    @Test
    public void testParseOptimizationDataEmptyArray() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.parseOptimizationData(new OptimizationData[0]);
        assertNull(optimizer.getSimplex());
    }

    @Test
    public void testParseOptimizationDataNullArray() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.parseOptimizationData(null);
        assertNull(optimizer.getSimplex());
    }
}