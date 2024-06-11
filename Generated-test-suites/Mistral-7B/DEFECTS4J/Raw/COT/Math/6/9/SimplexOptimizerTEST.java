package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.optimizers.SimplexOptimizer;
import org.apache.commons.math3.optim.nonlinear.NullVectorFunction;
import org.apache.commons.math3.optim.nonlinear.SimpleXPoint;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.search.AbstractSimplex;
import org.apache.commons.math3.optim.search.SimpleSimplex;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testEmptyConstructor() {
        // This constructor is deprecated and not used.
    }

    @Test
    public void testConstructorWithSimpleValueChecker() {
        final double relTolerance = 1.0e-6;
        final double absTolerance = 1.0e-6;
        final ConvergenceChecker<PointValuePair> checker
            = new SimpleValueChecker(relTolerance, absTolerance);
        final SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithRelAndAbsTolerance() {
        final double relTolerance = 1.0e-6;
        final double absTolerance = 1.0e-6;
        final SimplexOptimizer optimizer = new SimplexOptimizer(relTolerance, absTolerance);
        assertNotNull(optimizer);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetSimplexNullArgument() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);
    }

    @Test
    public void testSetSimplexValidAbstractSimplex() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();
        final AbstractSimplex simplex = new SimpleSimplex(2);
        optimizer.setSimplex(simplex);
        assertNotNull(optimizer.getSimplex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSimplexInvalidAbstractSimplex() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();
        assertThrows(IllegalArgumentException.class, () -> optimizer.setSimplex(null));
    }

    @Test
    public void testOptimizeWithNullMultivariateFunction() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();
        assertThrows(NullPointerException.class, () -> optimizer.optimize(1, null, GoalType.MINIMIZE, null));
    }

    @Test
    public void testOptimizeWithValidMultivariateFunction() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();
        final MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return FunctionUtils.square(x[0]) + FunctionUtils.square(x[1]);
            }
        };
        final PointValuePair result = optimizer.optimize(1, f, GoalType.MINIMIZE, new Object[0]);
        assertEquals(0.0, result.getValue(), 1.0e-6);
    }

    @Test
    public void testOptimizeWithGoalTypeNullArgument() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();
        assertThrows(NullPointerException.class, () -> optimizer.optimize(1, new MultivariateFunction() {
            public double value(double[] x) {
                return 0.0;
            }
        }, null, new Object[0]));
    }

    @Test
    public void testOptimizeWithValidGoalType() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();
        final MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return FunctionUtils.square(x[0]) + FunctionUtils.square(x[1]);
            }
        };
        final PointValuePair result = optimizer.optimize(1, f, GoalType.MINIMIZE, new Object[0]);
        assertEquals(0.0, result.getValue(), 1.0e-6);
    }

    @Test
    public void testOptimizeWithMaxEvalNegativeNumber() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(-1, new MultivariateFunction() {
            public double value(double[] x) {
                return 0.0;
            }
        }, GoalType.MINIMIZE, new Object[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNullOptimizationData() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(1, new MultivariateFunction() {
            public double value(double[] x) {
                return 0.0;
            }
        }, GoalType.MINIMIZE, null);
    }

    @Test
    public void testOptimizeWithValidOptimizationData() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();
        final MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return FunctionUtils.square(x[0]) + FunctionUtils.square(x[1]);
            }
        };
        final AbstractSimplex simplex = new SimpleSimplex(2);
        final PointValuePair[] optimizationData = new PointValuePair[] {
            new PointValuePair(new double[] {0.0, 0.0}, 1.0),
            new PointValuePair(new double[] {1.0, 0.0}, 2.0),
            new PointValuePair(new double[] {0.5, 0.5}, 3.0),
            new PointValuePair(new double[] {2.0, 0.0}, 4.0),
            new PointValuePair(new double[] {1.0, 1.0}, 5.0),
            new PointValuePair(new double[] {0.0, 2.0}, 6.0),
            new PointValuePair(new double[] {1.5, 1.0}, 7.0),
            new PointValuePair(new double[] {0.5, 2.0}, 8.0),
            new PointValuePair(new double[] {2.0, 2.0}, 9.0)
        };
        optimizer.parseOptimizationData(optimizationData);
        final PointValuePair result = optimizer.optimize(1, f, GoalType.MINIMIZE, optimizationData);
        assertEquals(0.0, result.getValue(), 1.0e-6);
    }

    @Test
    public void testParseOptimizationDataNullArgument() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.parseOptimizationData(null);
        assertNull(optimizer.getSimplex());
    }

    @Test
    public void testParseOptimizationDataWithValidAbstractSimplex() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();
        final AbstractSimplex simplex = new SimpleSimplex(2);
        optimizer.parseOptimizationData(new PointValuePairList(simplex.getPoints()).toArray());
        assertNotNull(optimizer.getSimplex());
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testDoOptimizeWithNullMultivariateFunction() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();
        assertThrows(NullPointerException.class, optimizer::doOptimize);
    }

    @Test
    public void testDoOptimizeWithValidMultivariateFunction() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();
        final MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return FunctionUtils.square(x[0]) + FunctionUtils.square(x[1]);
            }
        };
        final PointValuePair result = optimizer.doOptimize();
        assertEquals(0.0, result.getValue(), 1.0e-6);
    }

    @Test
    public void testComputeObjectiveValueWithNullMultivariateFunction() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();
        assertThrows(NullPointerException.class, () -> optimizer.computeObjectiveValue(null, new double[2]));
    }

    @Test
    public void testComputeObjectiveValueWithNullPoint() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();
        assertThrows(NullPointerException.class, () -> optimizer.computeObjectiveValue(new MultivariateFunction() {
            public double value(double[] x) {
                return 0.0;
            }
        }, null));
    }

    @Test
    public void testComputeObjectiveValueWithValidPoint() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();
        final MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return FunctionUtils.square(x[0]) + FunctionUtils.square(x[1]);
            }
        };
        final double objectiveValue = optimizer.computeObjectiveValue(f, new double[] {0.5, 0.5});
        assertEquals(2.5, objectiveValue, 1.0e-6);
    }
}