package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.UnivariateOptimizer;
import org.apache.commons.math3.optimization.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optimization.BracketFinder;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariateFunctionWrapper;
import org.apache.commons.math3.util.Precision;

/**
 * Test class for PowellOptimizer
 */
public class PowellOptimizerTest {

    /**
     * Test constructor with default constraints and convergence checker
     */
    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        assertEquals(1e-6, optimizer.getRelativeThreshold(), 1e-8);
        assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 1e-8);
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with user-defined convergence checker
     */
    @Test
    public void testConstructorWithCustomChecker() {
        SimpleUnivariateValueChecker checker = new SimpleUnivariateValueChecker(1e-3, 1e-5);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, checker);
        assertEquals(1e-6, optimizer.getRelativeThreshold(), 1e-8);
        assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 1e-8);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    /**
     * Test optimization of a constant function
     */
    @Test
    public void testOptimizeConstantFunction() throws NotStrictlyPositiveException, NumberIsTooSmallException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        UnivariateFunction function = (x) -> 0.0;
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        assertArrayEquals(new double[]{}, result.getPoint(), 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
    }

    /**
     * Test optimization of a simple quadratic function
     */
    @Test
    public void testOptimizeQuadraticFunction() throws NotStrictlyPositiveException, NumberIsTooSmallException, MaxEvaluationsExceededException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        UnivariateFunction function = (x) -> Math.pow(x, 2);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        assertEquals(0.0, result.getPoint()[0], 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
    }

    /**
     * Test optimization of a simple quadratic function with user-defined convergence checker
     */
    @Test
    public void testOptimizeQuadraticFunctionWithCustomChecker() throws NotStrictlyPositiveException, NumberIsTooSmallException, MaxEvaluationsExceededException {
        SimpleUnivariateValueChecker checker = new SimpleUnivariateValueChecker(1e-3, 1e-5);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, checker);

        UnivariateFunction function = (x) -> Math.pow(x, 2);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        assertEquals(0.0, result.getPoint()[0], 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
    }

    /**
     * Test optimization of a simple quadratic function with a goal of maximization
     */
    @Test
    public void testOptimizeQuadraticFunctionMaximization() throws NotStrictlyPositiveException, NumberIsTooSmallException, MaxEvaluationsExceededException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        UnivariateFunction function = (x) -> Math.pow(x, 2);
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE);
        assertEquals(0.0, result.getPoint()[0], 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
    }

    /**
     * Test optimization of a simple quadratic function with a goal of maximization and user-defined convergence checker
     */
    @Test
    public void testOptimizeQuadraticFunctionMaximizationWithCustomChecker() throws NotStrictlyPositiveException, NumberIsTooSmallException, MaxEvaluationsExceededException {
        SimpleUnivariateValueChecker checker = new SimpleUnivariateValueChecker(1e-3, 1e-5);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, checker);

        UnivariateFunction function = (x) -> Math.pow(x, 2);
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE);
        assertEquals(0.0, result.getPoint()[0], 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
    }

    /**
     * Test optimization of a simple quadratic function with different starting points
     */
    @Test
    public void testOptimizeQuadraticFunctionDifferentStart() throws NotStrictlyPositiveException, NumberIsTooSmallException, MaxEvaluationsExceededException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        UnivariateFunction function = (x) -> Math.pow(x, 2);
        PointValuePair result1 = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{1.0, 1.0});
        PointValuePair result2 = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{-1.0, -1.0});

        assertEquals(0.0, result1.getPoint()[0], 1e-8);
        assertEquals(0.0, result1.getPoint()[1], 1e-8);
        assertEquals(0.0, result1.getValue(), 1e-8);

        assertEquals(0.0, result2.getPoint()[0], 1e-8);
        assertEquals(0.0, result2.getPoint()[1], 1e-8);
        assertEquals(0.0, result2.getValue(), 1e-8);

        // Check that both starting points converge to the same solution
        assertEquals(result1.getPoint(), result2.getPoint(), 1e-8);
    }

    /**
     * Test optimization of a simple quadratic function with different starting points and user-defined convergence checker
     */
    @Test
    public void testOptimizeQuadraticFunctionDifferentStartWithCustomChecker() throws NotStrictlyPositiveException, NumberIsTooSmallException, MaxEvaluationsExceededException {
        SimpleUnivariateValueChecker checker = new SimpleUnivariateValueChecker(1e-3, 1e-5);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, checker);

        UnivariateFunction function = (x) -> Math.pow(x[0], 2) + Math.pow(x[1], 2);
        PointValuePair result1 = optimizer.optimize(new UnivariateFunctionWrapper(function), GoalType.MINIMIZE, new double[]{1.0, 1.0});
        PointValuePair result2 = optimizer.optimize(new UnivariateFunctionWrapper(function), GoalType.MINIMIZE, new double[]{-1.0, -1.0});

        assertEquals(0.0, result1.getPoint()[0], 1e-8);
        assertEquals(0.0, result1.getPoint()[1], 1e-8);
        assertEquals(0.0, result1.getValue(), 1e-8);

        assertEquals(0.0, result2.getPoint()[0], 1e-8);
        assertEquals(0.0, result2.getPoint()[1], 1e-8);
        assertEquals(0.0, result2.getValue(), 1e-8);

        // Check that both starting points converge to the same solution
        assertEquals(result1.getPoint(), result2.getPoint(), 1e-8);
    }

    /**
     * Test optimization of a function with multiple local minimums
     */
    @Test
    public void testOptimizeMultiLocalMinima() throws NotStrictlyPositiveException, NumberIsTooSmallException, MaxEvaluationsExceededException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        UnivariateFunction function = (x) -> Math.pow(Math.sin(x), 2);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{1.0, 1.0});

        // Check that the algorithm found a local minimum
        assertTrue(Precision.isClose(result.getValue(), 1.0, 1e-8));
    }

    /**
     * Test optimization of a function with multiple local minimums and user-defined convergence checker
     */
    @Test
    public void testOptimizeMultiLocalMinimaWithCustomChecker() throws NotStrictlyPositiveException, NumberIsTooSmallException, MaxEvaluationsExceededException {
        SimpleUnivariateValueChecker checker = new SimpleUnivariateValueChecker(1e-3, 1e-5);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, checker);

        UnivariateFunction function = (x) -> Math.pow(Math.sin(x), 2);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{1.0, 1.0});

        // Check that the algorithm found a local minimum
        assertTrue(Precision.isClose(result.getValue(), 1.0, 1e-8));
    }
}