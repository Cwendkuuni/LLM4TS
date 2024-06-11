package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.StoppingCriteria;
import org.apache.commons.math3.optim.nonlinear.function.LinearFunction;
import org.apache.commons.math3.optim.nonlinear.functions.PolynomialFunction;
import org.apache.commons.math3.optim.nonlinear.vector.goevolution.genetic.GeneticAlgorithm;
import org.apache.commons.math3.optim.nonlinear.vector.goevolution.genetic.Mutator;
import org.apache.commons.math3.optim.nonlinear.vector.goevolution.genetic.Selector;
import org.apache.commons.math3.optim.nonlinear.vector.goevolution.genetic.SimpleCrossover;
import org.apache.commons.math3.optim.nonlinear.vector.goevolution.genetic.TYpe1Mutator;
import org.apache.commons.math3.optim.nonlinear.vector.goevolution.genetic.TYpe2Mutator;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;
    private RealPointValuePair[] simplex;
    private Comparator<RealPointValuePair> comparator;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional(2.0, 0.5);
        comparator = new org.apache.commons.collections4.comparators.ComparatorChain();
        comparator.chain(new org.apache.commons.math3.optim.PointValuePairComparator());
    }

    @Test
    public void testIterateSimplexWithLinearFunction() throws Exception {
        LinearFunction function = new LinearFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        PointValuePair expectedResult = new PointValuePair(new double[]{1.0, 1.0}, 3.0);
        SimpleBounds bounds = new SimpleBounds(new double[]{-4.0, -4.0}, new double[]{4.0, 4.0});
        StoppingCriteria stoppingCriteria = new StoppingCriteria() {
            @Override
            public boolean isSatisfied(int iterations, PointValuePair currentBest, PointValuePair currentSolution) {
                return currentBest.getValue() < 2.5;
            }
        };

        optimizer.optimize(function, bounds, optimizer.getMaxIterations(), comparator, stoppingCriteria);
        RealPointValuePair actualResult = simplex[0];
        assertEquals(expectedResult.getPoint(), actualResult.getPointRef(), 0.001);
        assertEquals(expectedResult.getValue(), actualResult.getValue(), 0.001);
    }

    @Test
    public void testIterateSimplexWithPolynomialFunction() throws Exception {
        PolynomialFunction function = new PolynomialFunction(new double[]{-1.0, 1.0}, 2, new double[]{-1.0, 1.0});
        PointValuePair expectedResult = new PointValuePair(new double[]{1.0, 1.0}, 1.0);
        SimpleBounds bounds = new SimpleBounds(new double[]{-4.0, -4.0}, new double[]{4.0, 4.0});
        StoppingCriteria stoppingCriteria = new StoppingCriteria() {
            @Override
            public boolean isSatisfied(int iterations, PointValuePair currentBest, PointValuePair currentSolution) {
                return currentBest.getValue() < 0.5;
            }
        };

        optimizer.optimize(function, bounds, optimizer.getMaxIterations(), comparator, stoppingCriteria);
        RealPointValuePair actualResult = simplex[0];
        assertEquals(expectedResult.getPoint(), actualResult.getPointRef(), 0.001);
        assertEquals(expectedResult.getValue(), actualResult.getValue(), 0.001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testIterateSimplexWithException() throws Exception {
        LinearFunction function = new LinearFunction(new double[]{0.0}, new double[]{Double.NaN});
        SimpleBounds bounds = new SimpleBounds(new double[]{-4.0, -4.0}, new double[]{4.0, 4.0});
        StoppingCriteria stoppingCriteria = new StoppingCriteria() {};

        optimizer.optimize(function, bounds, optimizer.getMaxIterations(), comparator, stoppingCriteria);
    }

    @Test(expected = OptimizationException.class)
    public void testIterateSimplexWithMaxIterationsReached() throws Exception {
        LinearFunction function = new LinearFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        SimpleBounds bounds = new SimpleBounds(new double[]{-4.0, -4.0}, new double[]{4.0, 4.0});
        StoppingCriteria stoppingCriteria = new StoppingCriteria() {};

        optimizer.setMaxIterations(0);
        optimizer.optimize(function, bounds, optimizer.getMaxIterations(), comparator, stoppingCriteria);
    }

    @Test
    public void testEvaluateNewSimplexWithLinearFunction() throws Exception {
        LinearFunction function = new LinearFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        RealPointValuePair expectedResult = new RealPointValuePair(new double[]{1.0, 1.0}, 3.0);

        optimizer.evaluateNewSimplex(new RealPointValuePair[] {new RealPointValuePair(new double[]{-4.0, -4.0}, Double.NaN, false), expectedResult}, 1.0, comparator);
        RealPointValuePair actualResult = simplex[1];
        assertEquals(expectedResult.getPoint(), actualResult.getPointRef(), 0.001);
        assertEquals(expectedResult.getValue(), actualResult.getValue(), 0.001);
    }

    @Test
    public void testEvaluateNewSimplexWithPolynomialFunction() throws Exception {
        PolynomialFunction function = new PolynomialFunction(new double[]{-1.0, 1.0}, 2, new double[]{-1.0, 1.0});
        RealPointValuePair expectedResult = new RealPointValuePair(new double[]{1.0, 1.0}, 1.0);

        optimizer.evaluateNewSimplex(new RealPointValuePair[] {new RealPointValuePair(new double[]{-4.0, -4.0}, Double.NaN, false), expectedResult}, 2.0, comparator);
        RealPointValuePair actualResult = simplex[1];
        assertEquals(expectedResult.getPoint(), actualResult.getPointRef(), 0.001);
        assertEquals(expectedResult.getValue(), actualResult.getValue(), 0.001);
    }

    @Test
    public void testConstructorWithDefaultCoefficients() {
        MultiDirectional optimizer = new MultiDirectional();
        assertEquals(2.0, optimizer.khi, 0.001);
        assertEquals(0.5, optimizer.gamma, 0.001);
    }

    @Test
    public void testConstructorWithSpecifiedCoefficients() {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.7);
        assertEquals(3.0, optimizer.khi, 0.001);
        assertEquals(0.7, optimizer.gamma, 0.001);
    }

}