package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealConvergenceChecker;
import org.apache.commons.math3.optim.SimpleBoundingBox;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearch;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;
    private double[] xAxisPoints = {0.0, 1.0, 2.0, 3.0};
    private ObjectiveFunction function;

    @Before
    public void setup() {
        optimizer = new MultiDirectional();
        optimizer.setMaxIterations(100);
        optimizer.setConvergenceChecker(new RealConvergenceChecker(1.0E-8, 1.0E-8, 1.0E-8));
        function = new FibonacciSearch(new SimpleBoundingBox(xAxisPoints), GoalType.MINIMIZE);
    }

    /**
     * Test method for {@link MultiDirectional#MultiDirectional()}
     * @throws OptimizationException
     */
    @Test
    public void testMultiDirectionalConstructorNoArgs() throws OptimizationException {
        optimizer = new MultiDirectional();
        PointValuePair result = optimizer.optimize(function);
        Assert.assertEquals(0.0, result.getPoint()[0], 0.01);
        Assert.assertEquals(-1.0, result.getValue(), 0.01);
    }

    /**
     * Test method for {@link MultiDirectional#MultiDirectional(double, double)}
     * @throws OptimizationException
     */
    @Test
    public void testMultiDirectionalConstructorArgs() throws OptimizationException {
        optimizer = new MultiDirectional(1.5, 0.2);
        PointValuePair result = optimizer.optimize(function);
        Assert.assertEquals(1.0, result.getPoint()[0], 0.01);
        Assert.assertEquals(-1.61803398875, result.getValue(), 0.01);
    }

    /**
     * Test method for {@link MultiDirectional#iterateSimplex(Comparator)}
     * @throws FunctionEvaluationException
     * @throws OptimizationException
     * @throws IllegalArgumentException
     */
    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        PointValuePair result = optimizer.optimize(function);
        Assert.assertEquals(0.0, result.getPoint()[0], 0.01);
        Assert.assertEquals(-1.0, result.getValue(), 0.01);
    }

    /**
     * Test method for edge case: {@link MultiDirectional#iterateSimplex(Comparator)} when the function cannot be evaluated
     * @throws FunctionEvaluationException
     * @throws OptimizationException
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testIterateSimplexFunctionEvaluationException() throws FunctionEvaluationException, OptimizationException {
        function = new ObjectiveFunction() {
            @Override
            public double value(double[] point) {
                throw new UnsupportedOperationException();
            }
        };
        optimizer.optimize(function);
    }

    /**
     * Test method for edge case: {@link MultiDirectional#iterateSimplex(Comparator)} when the maximal number of evaluations is exceeded
     * @throws OptimizationException
     */
    @Test(expected = OptimizationException.class)
    public void testIterateSimplexMaxIterationsExceeded() throws OptimizationException {
        optimizer.setMaxIterations(0);
        optimizer.optimize(function);
    }

}