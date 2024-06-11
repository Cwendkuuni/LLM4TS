package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalConstructor() {
        MultiDirectional optimizer = new MultiDirectional();
        assertNotNull(optimizer);

        MultiDirectional optimizerWithCustomCoef = new MultiDirectional(1.5, 0.4);
        assertNotNull(optimizerWithCustomCoef);
    }

    @Test
    public void testIterateSimplex_ReflectionStep_ReturnsBestPoint() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[5];
        simplex[0] = new RealPointValuePair(new double[] {0.0, 0.0}, 0.0, false);
        simplex[1] = new RealPointValuePair(new double[] {1.0, 1.0}, 2.0, false);
        simplex[2] = new RealPointValuePair(new double[] {2.0, 2.0}, 4.0, false);
        simplex[3] = new RealPointValuePair(new double[] {3.0, 3.0}, 5.0, false);
        simplex[4] = new RealPointValuePair(new double[] {4.0, 4.0}, 6.0, false);

        Comparator<RealPointValuePair> comparator = (o1, o2) -> o1.getValue() - o2.getValue();

        optimizer.iterateSimplex(comparator, simplex);

        RealPointValuePair bestPoint = simplex[0];
        RealPointValuePair reflectedPoint = optimizer.evaluateNewSimplex(simplex, 1.0, comparator);

        PointValuePair expectedReflectedPoint = new PointValuePair(2.0, 1.0);
        RealPointValuePair expectedBestPointAfterReflection = new RealPointValuePair(new double[] {1.2, 1.2}, 1.2, false);

        assertEquals(expectedReflectedPoint.getValue(), reflectedPoint.getValue(), 0.001);
        assertEquals(expectedBestPointAfterReflection, bestPoint);
    }

    @Test
    public void testIterateSimplex_ReflectionStep_AcceptsReflectedSimplex() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[5];
        simplex[0] = new RealPointValuePair(new double[] {0.0, 0.0}, 0.0, false);
        simplex[1] = new RealPointValuePair(new double[] {1.0, 1.0}, 2.0, false);
        simplex[2] = new RealPointValuePair(new double[] {2.0, 2.0}, 4.0, false);
        simplex[3] = new RealPointValuePair(new double[] {3.0, 3.0}, 5.0, false);
        simplex[4] = new RealPointValuePair(new double[] {4.0, 4.0}, 6.0, false);

        Comparator<RealPointValuePair> comparator = (o1, o2) -> o1.getValue() - o2.getValue();

        optimizer.iterateSimplex(comparator, simplex);

        RealPointValuePair bestPoint = simplex[0];
        RealPointValuePair reflectedPoint = optimizer.evaluateNewSimplex(simplex, 1.0, comparator);
        RealPointValuePair[] reflectedSimplex = new RealPointValuePair[5];

        for (int i = 0; i < 5; i++) {
            reflectedSimplex[i] = simplex[i];
        }

        optimizer.simplex = reflectedSimplex;

        optimizer.iterateSimplex(comparator);

        PointValuePair expectedBestPointAfterReflection = new PointValuePair(1.2, 1.2);

        assertEquals(expectedBestPointAfterReflection.getValue(), bestPoint.getValue(), 0.001);
    }

    @Test
    public void testIterateSimplex_ExpansionStep_AcceptsExpandedSimplex() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[5];
        simplex[0] = new RealPointValuePair(new double[] {0.0, 0.0}, 0.0, false);
        simplex[1] = new RealPointValuePair(new double[] {1.0, 1.0}, 2.0, false);
        simplex[2] = new RealPointValuePair(new double[] {2.0, 2.0}, 4.0, false);
        simplex[3] = new RealPointValuePair(new double[] {3.0, 3.0}, 5.0, false);
        simplex[4] = new RealPointValuePair(new double[] {4.0, 4.0}, 6.0, false);

        Comparator<RealPointValuePair> comparator = (o1, o2) -> o1.getValue() - o2.getValue();

        optimizer.iterateSimplex(comparator, simplex);

        RealPointValuePair bestPoint = simplex[0];
        RealPointValuePair reflectedPoint = optimizer.evaluateNewSimplex(simplex, 1.0, comparator);
        RealPointValuePair expandedPoint = optimizer.evaluateNewSimplex(simplex, 2.0, comparator);

        RealPointValuePair[] expandedSimplex = new RealPointValuePair[5];

        for (int i = 0; i < 5; i++) {
            expandedSimplex[i] = simplex[i];
        }

        expandedSimplex[0] = expandedPoint;

        optimizer.simplex = expandedSimplex;

        optimizer.iterateSimplex(comparator);

        PointValuePair expectedBestPointAfterExpansion = new PointValuePair(1.44, 1.44);

        assertEquals(expectedBestPointAfterExpansion.getValue(), bestPoint.getValue(), 0.001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testIterateSimplex_ThrowsFunctionEvaluationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[5];
        simplex[0] = new RealPointValuePair(new double[] {0.0, 0.0}, 0.0, false);
        simplex[1] = new RealPointValuePair(new double[] {1.0, 1.0}, 2.0, false);
        simplex[2] = new RealPointValuePair(new double[] {2.0, 2.0}, 4.0, false);
        simplex[3] = new RealPointValuePair(new double[] {3.0, 3.0}, 5.0, false);
        simplex[4] = new RealPointValuePair(new double[] {4.0, 4.0}, Double.POSITIVE_INFINITY, false);

        Comparator<RealPointValuePair> comparator = (o1, o2) -> o1.getValue() - o2.getValue();

        optimizer.iterateSimplex(comparator, simplex);
    }

    @Test(expected = OptimizationException.class)
    public void testIterateSimplex_ThrowsOptimizationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[5];
        simplex[0] = new RealPointValuePair(new double[] {0.0, 0.0}, 0.0, false);
        simplex[1] = new RealPointValuePair(new double[] {1.0, 1.0}, 2.0, false);
        simplex[2] = new RealPointValuePair(new double[] {2.0, 2.0}, 4.0, false);
        simplex[3] = new RealPointValuePair(new double[] {3.0, 3.0}, 5.0, false);
        simplex[4] = new RealPointValuePair(new double[] {4.0, 4.0}, 6.0, false);

        Comparator<RealPointValuePair> comparator = (o1, o2) -> o1.getValue() - o2.getValue();

        optimizer.setMaxEvaluations(1);

        optimizer.iterateSimplex(comparator, simplex);
    }

    @Test
    public void testIterateSimplex_ContractionStep_AcceptsContractedSimplex() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[5];
        simplex[0] = new RealPointValuePair(new double[] {0.0, 0.0}, 0.0, false);
        simplex[1] = new RealPointValuePair(new double[] {1.0, 1.0}, 2.0, false);
        simplex[2] = new RealPointValuePair(new double[] {2.0, 2.0}, 4.0, false);
        simplex[3] = new RealPointValuePair(new double[] {3.0, 3.0}, 5.0, false);
        simplex[4] = new RealPointValuePair(new double[] {4.0, 4.0}, 6.0, false);

        Comparator<RealPointValuePair> comparator = (o1, o2) -> o1.getValue() - o2.getValue();

        optimizer.iterateSimplex(comparator, simplex);

        RealPointValuePair bestPoint = simplex[0];
        RealPointValuePair contractedPoint = optimizer.evaluateNewSimplex(simplex, 0.5, comparator);

        RealPointValuePair[] contractedSimplex = new RealPointValuePair[5];

        for (int i = 0; i < 5; i++) {
            contractedSimplex[i] = simplex[i];
        }

        contractedSimplex[0] = contractedPoint;

        optimizer.simplex = contractedSimplex;

        optimizer.iterateSimplex(comparator);

        PointValuePair expectedBestPointAfterContraction = new PointValuePair(0.75, 0.75);

        assertEquals(expectedBestPointAfterContraction.getValue(), bestPoint.getValue(), 0.001);
    }

    @Test
    public void testIterateSimplex_ConvergenceCheck() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[5];
        simplex[0] = new RealPointValuePair(new double[] {0.0, 0.0}, 0.0, false);
        simplex[1] = new RealPointValuePair(new double[] {1.0, 1.0}, 2.0, false);
        simplex[2] = new RealPointValuePair(new double[] {2.0, 2.0}, 4.0, false);
        simplex[3] = new RealPointValuePair(new double[] {3.0, 3.0}, 5.0, false);
        simplex[4] = new RealPointValuePair(new double[] {4.0, 4.0}, 6.0, false);

        Comparator<RealPointValuePair> comparator = (o1, o2) -> o1.getValue() - o2.getValue();

        optimizer.setConvergenceChecker(new RealConvergenceChecker(1e-6));

        optimizer.iterateSimplex(comparator, simplex);

        assertTrue(optimizer.hasConverged());
    }

}