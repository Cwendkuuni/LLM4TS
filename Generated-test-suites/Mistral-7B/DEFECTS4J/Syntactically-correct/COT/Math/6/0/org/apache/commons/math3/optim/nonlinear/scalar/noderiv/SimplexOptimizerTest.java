package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assume.assumeNotNull;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.goaltype.GoalType;
import org.apache.commons.math3.optim.maximization.MaxEval;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBound;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleOptimizationData;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.goaltype.GoalType.MINIMIZE;
import org.apache.commons.math3.optim.nonlinear.scalar.goaltype.GoalType.MAXIMIZE;
import org.apache.commons.math3.optim.nonlinear.vector.GoalTypeVector;
import org.apache.commons.math3.optim.nonlinear.vector.ObjectiveFunctionVector;
import org.apache.commons.math3.optim.nonlinear.vector.PointValuePairVector;
import org.apache.commons.math3.optim.nonlinear.vector.SimpleBoundVector;
import org.apache.commons.math3.optim.nonlinear.vector.SimpleOptimizationDataVector;
import org.apache.commons.math3.optim.nonlinear.vector.SimplePointValuePairVector;
import org.apache.commons.math3.optim.nonlinear.vector.SimpleSimplexVector;
import org.apache.commons.math3.optim.nonlinear.vector.SimpleValueCheckerVector;
import org.apache.commons.math3.optim.nonlinear.vector.SimpleVector;
import org.apache.commons.math3.optim.nonlinear.vector.goaltype.GoalTypeVector.MINIMIZE_ALL;
import org.apache.commons.math3.optim.nonlinear.vector.goaltype.GoalTypeVector.MAXIMIZE_ALL;
import org.apache.commons.math3.optim.nonlinear.vector.goaltype.GoalTypeVector.MINIMIZE_ONE;
import org.apache.commons.math3.optim.nonlinear.vector.goaltype.GoalTypeVector.MAXIMIZE_ONE;
import org.apache.commons.math3.optim.nonlinear.vector.objective.ObjectiveFunctionVectorWithMessage;
import org.apache.commons.math3.optim.nonlinear.vector.pointvaluepair.PointValuePairVectorWithMessage;
import org.apache.commons.math3.optim.nonlinear.vector.simplex.SimpleSimplexVectorWithMessage;
import org.apache.commons.math3.optim.nonlinear.vector.valuechecker.SimpleValueCheckerVectorWithMessage;
import org.apache.commons.math3.optim.nonlinear.vector.valuechecker.SimpleVectorValueChecker;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;

public class SimplexOptimizerTest {

    @org.junit.Test
    public void testConstructors() {
        // Default constructor
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assumeNotNull(optimizer);

        // Constructor with SimpleValueChecker
        SimpleValueChecker checker = new SimpleValueChecker();
        SimplexOptimizer optimizerWithChecker = new SimplexOptimizer(checker);
        assumeNotNull(optimizerWithChecker);
        assertEquals(checker, optimizerWithChecker.getConvergenceChecker());

        // Constructor with SimpleValueChecker and parameters
        SimpleValueChecker checkerWithParams = new SimpleValueChecker(1e-4, 1e-4);
        SimplexOptimizer optimizerWithCheckerAndParams = new SimplexOptimizer(checkerWithParams);
        assumeNotNull(optimizerWithCheckerAndParams);
        assertEquals(checkerWithParams, optimizerWithCheckerAndParams.getConvergenceChecker());
        assertEquals(1e-4, optimizerWithCheckerAndParams.getAbsoluteThreshold(), 1e-10);
        assertEquals(1e-4, optimizerWithCheckerAndParams.getRelativeThreshold(), 1e-10);

        // Constructor with null SimpleValueChecker throws NullArgumentException
        assertThrows(NullArgumentException.class, () -> new SimplexOptimizer(null));
    }

    @org.junit.Test
    public void testSetSimplex() {
        // Test default simplex
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assumeNotNull(optimizer);
        assertNull(optimizer.getSimplex());

        // Test setting simplex
        SimpleSimplex simplex = new SimpleSimplex(2);
        optimizer.setSimplex(simplex);
        assumeNotNull(optimizer.getSimplex());
        assertEquals(simplex, optimizer.getSimplex());

        // Test setting simplex with optimization data
        SimpleOptimizationData data = new SimpleOptimizationData(simplex);
        optimizer.optimize(new MockMultivariateFunction(), GoalType.MINIMIZE, new double[]{}, data);
        SimpleSimplex simplexFromData = (SimpleSimplex) data.getAbstractSimplex();
        assertEquals(simplexFromData, optimizer.getSimplex());

        // Test setting simplex with optimization data and recovery
        SimpleOptimizationData dataWithNullSimplex = new SimpleOptimizationData();
        optimizer.parseOptimizationData(dataWithNullSimplex);
        assertNull(optimizer.getSimplex());
    }

    @org.junit.Test
    public void testOptimize() {
        // Test optimization with default simplex and SimpleValueChecker
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assumeNotNull(optimizer);
        assertNull(optimizer.getSimplex());

        // Test optimization with default simplex and SimpleValueChecker
        // and unsupported number of dimensions
        MockMultivariateFunction f = new MockMultivariateFunction(1);
        PointValuePair result = optimizer.optimize(new MaxEval(10), f, GoalType.MINIMIZE, new double[]{});
        assertNull(result);

        // Test optimization with provided simplex and SimpleValueChecker
        SimpleSimplex simplex = new SimpleSimplex(2);
        optimizer.setSimplex(simplex);
        assumeNotNull(optimizer.getSimplex());

        // Test optimization with provided simplex and SimpleValueChecker
        // and unsupported goal type
        PointValuePair resultWithUnsupportedGoalType = optimizer.optimize(new MaxEval(10), f, GoalType.MAXIMIZE, new double[]{});
        assertNull(resultWithUnsupportedGoalType);

        // Test optimization with provided simplex, SimpleValueChecker,
        // and Minimize goal type
        double[] startPoint = {1.0, 2.0};
        PointValuePair resultMinimize = optimizer.optimize(new MaxEval(10), f, MINIMIZE, startPoint);
        assertNotNull(resultMinimize);
        SimplePointValuePairVector expectedResultMinimize = new SimplePointValuePairVector(startPoint, 0.0);
        assertEquals(expectedResultMinimize, resultMinimize);

        // Test optimization with provided simplex, SimpleValueChecker,
        // and Maximize goal type
        SimpleMultivariateFunction fMaximize = new SimpleMultivariateFunction(2);
        PointValuePair resultMaximize = optimizer.optimize(new MaxEval(10), fMaximize, MAXIMIZE, startPoint);
        assertNotNull(resultMaximize);
        SimplePointValuePairVector expectedResultMaximize = new SimplePointValuePairVector(startPoint, 1.0);
        assertEquals(expectedResultMaximize, resultMaximize);

        // Test optimization with provided simplex, SimpleValueChecker,
        // and Minimize goal type with bounds
        SimpleBoundVector bounds = new SimpleBoundVector(2);
        bounds.setBound(0, 0.0, 1.0);
        bounds.setBound(1, 0.0, 1.0);
        SimpleOptimizationDataVector optimizationData = new SimpleOptimizationDataVector(simplex, bounds);
        PointValuePair resultMinimizeWithBounds = optimizer.optimize(new MaxEval(10), f, MINIMIZE_ONE, startPoint, optimizationData);
        assertNotNull(resultMinimizeWithBounds);
        SimplePointValuePairVector expectedResultMinimizeWithBounds = new SimplePointValuePairVector(new double[]{0.5, 0.5}, 0.5);
        assertEquals(expectedResultMinimizeWithBounds, resultMinimizeWithBounds);

        // Test optimization with provided simplex, SimpleValueChecker,
        // and Minimize goal type with bounds and convergence checker
        SimpleValueCheckerVector valueChecker = new SimpleValueCheckerVector(new double[]{1e-4, 1e-4});
        SimpleOptimizationDataVector optimizationDataWithChecker = new SimpleOptimizationDataVector(simplex, bounds, valueChecker);
        PointValuePair resultMinimizeWithBoundsAndChecker = optimizer.optimize(new MaxEval(10), f, MINIMIZE_ALL, startPoint, optimizationDataWithChecker);
        assertNotNull(resultMinimizeWithBoundsAndChecker);
        SimplePointValuePairVector expectedResultMinimizeWithBoundsAndChecker = new SimplePointValuePairVector(new double[]{0.5, 0.5}, 0.5);
        assertEquals(expectedResultMinimizeWithBoundsAndChecker, resultMinimizeWithBoundsAndChecker);
    }

    @org.junit.Test
    public void testDoOptimize() {
        // Test doOptimize with null convergence checker throws NullArgumentException
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertThrows(NullArgumentException.class, () -> optimizer.doOptimize(null));

        // Test doOptimize with null simplex throws IllegalStateException
        optimizer.setSimplex(null);
        assertThrows(IllegalStateException.class, () -> optimizer.doOptimize());

        // Test doOptimize with default simplex and SimpleValueChecker
        SimpleSimplex simplex = new SimpleSimplex(2);
        optimizer.setSimplex(simplex);
        assumeNotNull(optimizer.getSimplex());

        // Test doOptimize with default simplex and SimpleValueChecker
        // and unsupported number of dimensions
        MockMultivariateFunction f = new MockMultivariateFunction(1);
        assertThrows(IllegalArgumentException.class, () -> optimizer.doOptimize(f, GoalType.MINIMIZE, new double[]{}));

        // Test doOptimize with provided simplex and SimpleValueChecker
        // and Minimize goal type
        double[] startPoint = {1.0, 2.0};
        MultivariateFunction function = new MockMultivariateFunction(2);
        PointValuePair resultMinimize = optimizer.doOptimize(function, MINIMIZE, startPoint);
        SimplePointValuePairVector expectedResultMinimize = new SimplePointValuePairVector(startPoint, 0.0);
        assertEquals(expectedResultMinimize, resultMinimize);

        // Test doOptimize with provided simplex and SimpleValueChecker
        // and Maximize goal type
        SimpleMultivariateFunction functionMaximize = new SimpleMultivariateFunction(2);
        PointValuePair resultMaximize = optimizer.doOptimize(functionMaximize, MAXIMIZE, startPoint);
        SimplePointValuePairVector expectedResultMaximize = new SimplePointValuePairVector(startPoint, 1.0);
        assertEquals(expectedResultMaximize, resultMaximize);

        // Test doOptimize with provided simplex, SimpleValueChecker,
        // and Minimize goal type with bounds
        SimpleBoundVector bounds = new SimpleBoundVector(2);
        bounds.setBound(0, 0.0, 1.0);
        bounds.setBound(1, 0.0, 1.0);
        SimpleOptimizationDataVector optimizationData = new SimpleOptimizationDataVector(simplex, bounds);
        PointValuePair resultMinimizeWithBounds = optimizer.doOptimize(function, MINIMIZE_ONE, startPoint, optimizationData);
        SimplePointValuePairVector expectedResultMinimizeWithBounds = new SimplePointValuePairVector(new double[]{0.5, 0.5}, 0.5);
        assertEquals(expectedResultMinimizeWithBounds, resultMinimizeWithBounds);

        // Test doOptimize with provided simplex, SimpleValueChecker,
        // and Minimize goal type with bounds and convergence checker
        SimpleValueCheckerVector valueChecker = new SimpleValueCheckerVector(new double[]{1e-4, 1e-4});
        SimpleOptimizationDataVector optimizationDataWithChecker = new SimpleOptimizationDataVector(simplex, bounds, valueChecker);
        PointValuePair resultMinimizeWithBoundsAndChecker = optimizer.doOptimize(function, MINIMIZE_ALL, startPoint, optimizationDataWithChecker);
        SimplePointValuePairVector expectedResultMinimizeWithBoundsAndChecker = new SimplePointValuePairVector(new double[]{0.5, 0.5}, 0.5);
        assertEquals(expectedResultMinimizeWithBoundsAndChecker, resultMinimizeWithBoundsAndChecker);
    }

    @org.junit.Test
    public void testParseOptimizationData() {
        // Test parseOptimizationData with null optimization data throws NullArgumentException
        assertThrows(NullArgumentException.class, () -> optimizer.parseOptimizationData(null));

        // Test parseOptimizationData with provided optimization data and no simplex
        SimpleOptimizationData optimizationData = new SimpleOptimizationData();
        optimizer.parseOptimizationData(optimizationData);
        assertNull(optimizer.getSimplex());

        // Test parseOptimizationData with provided optimization data and simplex
        SimpleOptimizationData optimizationDataWithSimplex = new SimpleOptimizationData(new SimpleSimplex(2));
        optimizer.parseOptimizationData(optimizationDataWithSimplex);
        SimpleSimplex simplexFromData = optimizer.getSimplex();
        assertEquals(simplexFromData, optimizationDataWithSimplex.getAbstractSimplex());
    }

    private static class MockMultivariateFunction implements MultivariateFunction {
        int dimensions;

        public MockMultivariateFunction(int dimensions) {
            this.dimensions = dimensions;
        }

        public double value(double[] point) {
            double sum = 0.0;
            for (int i = 0; i < point.length; i++) {
                sum += Math.pow(point[i], 2);
            }
            return sum;
        }

        public int getNumberOfVariables() {
            return dimensions;
        }
    }

    private static class SimpleMultivariateFunction implements MultivariateFunction {
        int dimensions;

        public SimpleMultivariateFunction(int dimensions) {
            this.dimensions = dimensions;
        }

        public double value(double[] point) {
            double sum = 0.0;
            for (int i = 0; i < point.length; i++) {
                sum += Math.pow(point[i], 2);
            }
            sum *= 2;
            return sum;
        }

        public int getNumberOfVariables() {
            return dimensions;
        }
    }

}