package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalDefaultConstructor() {
        MultiDirectional md = new MultiDirectional();
        assertEquals(2.0, md.khi, 0.01);
        assertEquals(0.5, md.gamma, 0.01);
    }

    @Test
    public void testMultiDirectionalCustomConstructor() {
        MultiDirectional md = new MultiDirectional(3.0, 0.7);
        assertEquals(3.0, md.khi, 0.01);
        assertEquals(0.7, md.gamma, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateSimplexNullComparator() {
        MultiDirectional md = new MultiDirectional();
        md.iterateSimplex(null);
    }

    @Test
    public void testIterateSimplexEmptySimplex() {
        MultiDirectional md = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());
        RealPointValuePair[] original = {};
        try {
            md.iterateSimplex(comparator);
            fail("FunctionEvaluationException or OptimizationException expected.");
        } catch (FunctionEvaluationException | OptimizationException e) {
            // Expected
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testIterateSimplexFunctionEvaluationException() throws Exception {
        MultiDirectional md = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());
        RealPointValuePair[] original = {new RealPointValuePair(new double[]{1.0}, 1.0, false), new RealPointValuePair(new double[]{2.0}, 2.0, false)};
        RealPointValuePair best = original[0];
        try {
            md.iterateSimplex(comparator);
            fail("Reflection, Expansion or Contraction should have been performed.");
        } catch (IllegalArgumentException | OptimizationException e) {
            // Expected
        }
        throw new FunctionEvaluationException("Test function evaluation exception.");
    }

    @Test(expected = OptimizationException.class)
    public void testIterateSimplexOptimizationException() throws Exception {
        MultiDirectional md = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());
        RealPointValuePair[] original = {new RealPointValuePair(new double[]{1.0}, 1.0, false), new RealPointValuePair(new double[]{2.0}, 2.0, false)};
        RealPointValuePair best = original[0];
        double maxEvaluations = 1;
        try {
            md.setMaxEvaluations(maxEvaluations);
            md.iterateSimplex(comparator);
            fail("Maximum number of evaluations exceeded.");
        } catch (FunctionEvaluationException | IllegalArgumentException e) {
            // Expected
        }
        throw new OptimizationException("Test optimization exception.");
    }

    @Test
    public void testIterateSimplexReflection() throws Exception {
        MultiDirectional md = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());
        RealPointValuePair[] original = {new RealPointValuePair(new double[]{1.0}, 1.0, false), new RealPointValuePair(new double[]{2.0}, 2.0, false)};
        RealPointValuePair best = original[0];
        RealPointValuePair reflected = md.iterateSimplex(comparator);
        assertNotNull(reflected);
        assertEquals(best.getValue(), reflected.getValue(), 0.01);
    }

    @Test
    public void testIterateSimplexExpansion() throws Exception {
        MultiDirectional md = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());
        RealPointValuePair[] original = {new RealPointValuePair(new double[]{1.0}, 1.0, false), new RealPointValuePair(new double[]{2.0}, 2.0, false)};
        RealPointValuePair best = original[0];
        RealPointValuePair reflected = md.iterateSimplex(comparator);
        RealPointValuePair expanded = md.iterateSimplex(comparator);
        assertNotNull(expanded);
        if (reflected.getValue() < expanded.getValue()) {
            // Reflection was better than expansion
            fail("Reflection was not replaced by expansion.");
        }
        assertEquals(best.getValue(), expanded.getValue(), 0.01);
    }

    @Test
    public void testIterateSimplexContraction() throws Exception {
        MultiDirectional md = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());
        RealPointValuePair[] original = {new RealPointValuePair(new double[]{1.0}, 1.0, false), new RealPointValuePair(new double[]{2.0}, 2.0, false)};
        RealPointValuePair best = original[0];
        RealPointValuePair reflected = md.iterateSimplex(comparator);
        RealPointValuePair contracted = md.iterateSimplex(comparator);
        assertNotNull(contracted);
        if (contracted.getValue() < best.getValue()) {
            // Contraction was better than best point
            fail("Contraction was not replaced by best point.");
        }
        assertEquals(best.getValue(), contracted.getValue(), 0.01);
    }

    @Test
    public void testEvaluateNewSimplexEmptySimplex() {
        MultiDirectional md = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());
        RealPointValuePair[] original = {};
        try {
            md.evaluateNewSimplex(original, 1.0, comparator);
            fail("FunctionEvaluationException expected.");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testEvaluateNewSimplexNullComparator() {
        MultiDirectional md = new MultiDirectional();
        RealPointValuePair[] original = {new RealPointValuePair(new double[]{1.0}, 1.0, false), new RealPointValuePair(new double[]{2.0}, 2.0, false)};
        try {
            md.evaluateNewSimplex(original, 1.0, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEvaluateNewSimplexFunctionEvaluationException() throws Exception {
        MultiDirectional md = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());
        RealPointValuePair[] original = {new RealPointValuePair(new double[]{1.0}, 1.0, false), new RealPointValuePair(new double[]{2.0}, 2.0, false)};
        try {
            md.evaluateNewSimplex(original, 1.0, comparator);
            fail("Reflection, Expansion or Contraction should have been performed.");
        } catch (FunctionEvaluationException | IllegalArgumentException e) {
            // Expected
        }
        throw new FunctionEvaluationException("Test function evaluation exception.");
    }

    @Test
    public void testEvaluateNewSimplexOptimizationException() throws Exception {
        MultiDirectional md = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());
        RealPointValuePair[] original = {new RealPointValuePair(new double[]{1.0}, 1.0, false), new RealPointValuePair(new double[]{2.0}, 2.0, false)};
        double maxEvaluations = 1;
        try {
            md.setMaxEvaluations(maxEvaluations);
            md.evaluateNewSimplex(original, 1.0, comparator);
            fail("Maximum number of evaluations exceeded.");
        } catch (FunctionEvaluationException | IllegalArgumentException e) {
            // Expected
        }
        throw new OptimizationException("Test optimization exception.");
    }

    @Test
    public void testEvaluateNewSimplexReflection() throws Exception {
        MultiDirectional md = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());
        RealPointValuePair[] original = {new RealPointValuePair(new double[]{1.0}, 1.0, false), new RealPointValuePair(new double[]{2.0}, 2.0, false)};
        RealPointValuePair best = original[0];
        RealPointValuePair reflected = md.evaluateNewSimplex(original, 1.0, comparator);
        assertNotNull(reflected);
        assertEquals(best.getValue(), reflected.getValue(), 0.01);
    }

    @Test
    public void testEvaluateNewSimplexExpansion() throws Exception {
        MultiDirectional md = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());
        RealPointValuePair[] original = {new RealPointValuePair(new double[]{1.0}, 1.0, false), new RealPointValuePair(new double[]{2.0}, 2.0, false)};
        RealPointValuePair best = original[0];
        RealPointValuePair reflected = md.evaluateNewSimplex(original, 1.0, comparator);
        RealPointValuePair expanded = md.evaluateNewSimplex(original, 2.0, comparator);
        assertNotNull(expanded);
        if (reflected.getValue() < expanded.getValue()) {
            // Reflection was better than expansion
            fail("Reflection was not replaced by expansion.");
        }
        assertEquals(best.getValue(), expanded.getValue(), 0.01);
    }

    @Test
    public void testEvaluateNewSimplexContraction() throws Exception {
        MultiDirectional md = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());
        RealPointValuePair[] original = {new RealPointValuePair(new double[]{1.0}, 1.0, false), new RealPointValuePair(new double[]{2.0}, 2.0, false)};
        RealPointValuePair best = original[0];
        RealPointValuePair reflected = md.evaluateNewSimplex(original, 1.0, comparator);
        RealPointValuePair contracted = md.evaluateNewSimplex(original, 0.5, comparator);
        assertNotNull(contracted);
        if (contracted.getValue() < best.getValue()) {
            // Contraction was better than best point
            fail("Contraction was not replaced by best point.");
        }
        assertEquals(best.getValue(), contracted.getValue(), 0.01);
    }

}