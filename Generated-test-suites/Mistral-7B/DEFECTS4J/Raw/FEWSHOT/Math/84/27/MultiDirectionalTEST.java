package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealConvergenceChecker;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.StoppingCondition;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalDefaultConstructor() {
        MultiDirectional optimizer = new MultiDirectional();
        assertEquals(2.0, optimizer.getKhi(), 0.001);
        assertEquals(0.5, optimizer.getGamma(), 0.001);
    }

    @Test
    public void testMultiDirectionalCustomConstructor() {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.7);
        assertEquals(3.0, optimizer.getKhi(), 0.001);
        assertEquals(0.7, optimizer.getGamma(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiDirectionalNegativeKhi() {
        new MultiDirectional(-1.0, 0.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiDirectionalNegativeGamma() {
        new MultiDirectional(2.0, -0.5);
    }

    @Test
    public void testIterateSimplex() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] xSmallest = {1.0, 2.0};
        RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(xSmallest, 2.3, false);
        original[1] = new RealPointValuePair(new double[]{1.1, 2.1}, 2.4, false);
        original[2] = new RealPointValuePair(new double[]{1.2, 2.2}, 2.5, false);

        RealPointValuePair best = original[0];
        RealPointValuePair reflected = null;
        RealPointValuePair contracted = null;
        RealPointValuePair simplexPoint = null;

        Comparator<RealPointValuePair> comparator = new RealPointValuePairComparator();

        try {
            optimizer.iterateSimplex(comparator);

            best = optimizer.getBestPoint();
            reflected = optimizer.getReflectedPoint();
            contracted = optimizer.getContractedPoint();
            simplexPoint = optimizer.getCurrentPoint();

            assertEquals(best.getFunctionValue(), 2.3, 0.001);
            assertEquals(reflected.getFunctionValue(), 2.5, 0.001);
            assertNull(contracted);
            assertArrayEquals(simplextPoint.getPointRef(), new double[]{1.1, 2.1}, 0.001);

        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            throw new TestFailureException("Unexpected exception: " + e.getMessage());
        }

        // Reflection test
        double[] xSmallestReflected = {1.2, 2.0};
        RealPointValuePair reflectedOriginal = new RealPointValuePair(xSmallestReflected, 2.6, false);
        optimizer = new MultiDirectional();
        original[0] = best;
        original[1] = reflectedOriginal;

        try {
            optimizer.iterateSimplex(comparator);

            best = optimizer.getBestPoint();
            reflected = optimizer.getReflectedPoint();
            contracted = optimizer.getContractedPoint();
            simplexPoint = optimizer.getCurrentPoint();

            assertEquals(best.getFunctionValue(), 2.3, 0.001);
            assertEquals(reflected.getFunctionValue(), 2.55, 0.001);
            assertNull(contracted);
            assertArrayEquals(simplextPoint.getPointRef(), new double[]{1.2, 2.0}, 0.001);

        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            throw new TestFailureException("Unexpected exception: " + e.getMessage());
        }

        // Expansion test
        double[] xSmallestExpanded = {1.1, 2.1};
        RealPointValuePair expandedOriginal = new RealPointValuePair(xSmallestExpanded, 2.7, false);
        optimizer = new MultiDirectional();
        original[0] = best;
        original[1] = reflected;
        original[2] = expandedOriginal;

        try {
            optimizer.iterateSimplex(comparator);

            best = optimizer.getBestPoint();
            reflected = optimizer.getReflectedPoint();
            contracted = optimizer.getContractedPoint();
            simplexPoint = optimizer.getCurrentPoint();

            assertEquals(best.getFunctionValue(), 2.3, 0.001);
            assertEquals(reflected.getFunctionValue(), 2.5, 0.001);
            assertNull(contracted);
            assertArrayEquals(simplextPoint.getPointRef(), xSmallestExpanded, 0.001);

        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            throw new TestFailureException("Unexpected exception: " + e.getMessage());
        }

        // Contraction test
        double[] xSmallestContracted = {1.15, 2.1};
        RealPointValuePair contractedOriginal = new RealPointValuePair(xSmallestContracted, 2.8, false);
        optimizer = new MultiDirectional();
        original[0] = best;
        original[1] = reflected;
        original[2] = expandedOriginal;
        original[3] = contractedOriginal;

        try {
            optimizer.iterateSimplex(comparator);

            best = optimizer.getBestPoint();
            reflected = optimizer.getReflectedPoint();
            contracted = optimizer.getContractedPoint();
            simplexPoint = optimizer.getCurrentPoint();

            assertEquals(best.getFunctionValue(), 2.3, 0.001);
            assertEquals(reflected.getFunctionValue(), 2.5, 0.001);
            assertArrayEquals(contracted.getPointRef(), xSmallestContracted, 0.001);
            assertArrayEquals(simplextPoint.getPointRef(), xSmallestContracted, 0.001);

        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            throw new TestFailureException("Unexpected exception: " + e.getMessage());
        }

        // Convergence test
        RealPointValuePair convergencePoint = new RealPointValuePair(new double[]{1.0, 2.0}, 2.0, false);
        optimizer = new MultiDirectional();
        original[0] = best;
        original[1] = reflected;
        original[2] = expandedOriginal;
        original[3] = contractedOriginal;
        original[4] = convergencePoint;

        try {
            optimizer.iterateSimplex(comparator);

            best = optimizer.getBestPoint();
            reflected = optimizer.getReflectedPoint();
            contracted = optimizer.getContractedPoint();
            simplexPoint = optimizer.getCurrentPoint();
            PointValuePair convergence = optimizer.getGlobalOptimum();

            assertEquals(best.getFunctionValue(), 2.3, 0.001);
            assertEquals(reflected.getFunctionValue(), 2.5, 0.001);
            assertNull(contracted);
            assertArrayEquals(simplextPoint.getPointRef(), convergence.getPoint(), 0.001);

        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            throw new TestFailureException("Unexpected exception: " + e.getMessage());
        }

    }

    @Test
    public void testEvaluateNewSimplex() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] xSmallest = {1.0, 2.0};
        RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(xSmallest, 2.3, false);

        try {
            RealPointValuePair result = optimizer.evaluateNewSimplex(original, 1.0, null);
            fail("FunctionEvaluationException expected");

        } catch (FunctionEvaluationException e) {
            // expected
        }

        try {
            RealPointValuePair result = optimizer.evaluateNewSimplex(original, 1.1, null);
            fail("IllegalArgumentException expected");

        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            optimizer = new MultiDirectional();
            RealPointValuePair reflected = optimizer.evaluateNewSimplex(original, 1.0, null);
            fail("FunctionEvaluationException expected");

        } catch (FunctionEvaluationException e) {
            // expected
        }

        try {
            optimizer = new MultiDirectional();
            RealPointValuePair reflected = optimizer.evaluateNewSimplex(original, 1.0, null);
            RealPointValuePair reflectedSimplex = new RealPointValuePair[3];
            reflectedSimplex[0] = original[0];
            reflectedSimplex[1] = reflected;
            optimizer.iterateSimplex(new RealPointValuePairComparator());
            RealPointValuePair result = optimizer.evaluateNewSimplex(original, 1.0, null);

            assertEquals(result.getFunctionValue(), 2.3, 0.001);

        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            throw new TestFailureException("Unexpected exception: " + e.getMessage());
        }

        try {
            optimizer = new MultiDirectional();
            RealPointValuePair reflected = optimizer.evaluateNewSimplex(original, 2.0, null);
            RealPointValuePair reflectedSimplex = new RealPointValuePair[3];
            reflectedSimplex[0] = original[0];
            reflectedSimplex[1] = reflected;
            optimizer.iterateSimplex(new RealPointValuePairComparator());
            RealPointValuePair result = optimizer.evaluateNewSimplex(original, 2.0, null);

            assertEquals(result.getFunctionValue(), 2.3, 0.001);

        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            throw new TestFailureException("Unexpected exception: " + e.getMessage());
        }

        try {
            optimizer = new MultiDirectional();
            RealPointValuePair reflected = optimizer.evaluateNewSimplex(original, 1.0, null);
            RealPointValuePair reflectedSimplex = new RealPointValuePair[3];
            reflectedSimplex[0] = original[0];
            reflectedSimplex[1] = reflected;
            optimizer.iterateSimplex(new RealPointValuePairComparator());
            RealPointValuePair result = optimizer.evaluateNewSimplex(original, 4.0, null);

            assertEquals(result.getFunctionValue(), Double.POSITIVE_INFINITY, 0.001);

        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            throw new TestFailureException("Unexpected exception: " + e.getMessage());
        }

        try {
            optimizer = new MultiDirectional();
            RealPointValuePair reflected = optimizer.evaluateNewSimplex(original, 1.0, null);
            RealPointValuePair reflectedSimplex = new RealPointValuePair[3];
            reflectedSimplex[0] = original[0];
            reflectedSimplex[1] = reflected;
            optimizer.iterateSimplex(new RealPointValuePairComparator());
            RealPointValuePair result = optimizer.evaluateNewSimplex(original, 0.1, null);

            assertEquals(result.getFunctionValue(), 2.3, 0.001);

        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            throw new TestFailureException("Unexpected exception: " + e.getMessage());
        }

        try {
            optimizer = new MultiDirectional();
            RealPointValuePair reflected = optimizer.evaluateNewSimplex(original, 1.0, null);
            RealPointValuePair reflectedSimplex = new RealPointValuePair[3];
            reflectedSimplex[0] = original[0];
            reflectedSimplex[1] = reflected;
            optimizer.iterateSimplex(new RealPointValuePairComparator());
            RealPointValuePair result = optimizer.evaluateNewSimplex(original, 1.1, new RealConvergenceChecker(1e-12, new SimpleGoalType(GoalType.MINIMIZE), new StoppingCondition()));

            assertEquals(result.getFunctionValue(), 2.3, 0.001);

        } catch (MaxEvalExceededException | NumberIsTooSmallException | FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            throw new TestFailureException("Unexpected exception: " + e.getMessage());
        }

    }

}