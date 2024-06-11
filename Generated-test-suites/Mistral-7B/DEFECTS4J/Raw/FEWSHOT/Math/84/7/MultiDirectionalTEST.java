package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;

import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.UnboundedDomainException;

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
    public void testIterateSimplexReflectionAccepted() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[5];
        simplex[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 3.0, false);
        simplex[1] = new RealPointValuePair(new double[]{3.0, 1.0}, 5.0, false);
        simplex[2] = new RealPointValuePair(new double[]{2.0, 2.0}, 4.0, false);
        simplex[3] = new RealPointValuePair(new double[]{0.5, 3.5}, 6.0, false);
        simplex[4] = new RealPointValuePair(new double[]{3.5, 0.5}, 7.0, false);

        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return (int) (o1.getValue() - o2.getValue());
            }
        };

        optimizer.iterateSimplex(comparator);

        PointValuePair expected = new PointValuePair(new double[]{2.0, 2.0}, 4.0);
        assertEquals(expected.getPoint(), simplex[0].getPointRef(), 0.001);
        assertEquals(expected.getValue(), simplex[0].getValue(), 0.001);
    }

    @Test
    public void testIterateSimplexReflectionRejected() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[5];
        simplex[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 3.0, false);
        simplex[1] = new RealPointValuePair(new double[]{3.0, 1.0}, 5.0, false);
        simplex[2] = new RealPointValuePair(new double[]{2.0, 2.0}, 4.0, false);
        simplex[3] = new RealPointValuePair(new double[]{0.5, 3.5}, 6.0, false);
        simplex[4] = new RealPointValuePair(new double[]{3.5, 0.5}, 7.0, false);

        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return (int) (o1.getValue() - o2.getValue());
            }
        };

        optimizer.iterateSimplex(comparator);

        PointValuePair expectedExpanded = new PointValuePair(new double[]{1.4142135625, 2.4142135625}, 4.8284271248);
        PointValuePair expectedReflected = new PointValuePair(new double[]{2.2580645161, 2.2580645161}, 4.75625);

        PointValuePair reflected = new PointValuePair(simplextoPointValuePair(simplex[1]));
        PointValuePair expanded = new PointValuePair(simplextoPointValuePair(simplex[simplex.length - 1]));

        assertTrue(reflected.getValue() > expectedReflected.getValue());
        assertTrue(expanded.getValue() < expectedExpanded.getValue());
    }

    @Test
    public void testIterateSimplexContractionAccepted() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[5];
        simplex[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 3.0, false);
        simplex[1] = new RealPointValuePair(new double[]{3.0, 1.0}, 5.0, false);
        simplex[2] = new RealPointValuePair(new double[]{2.0, 2.0}, 4.0, false);
        simplex[3] = new RealPointValuePair(new double[]{0.5, 3.5}, 6.0, false);
        simplex[4] = new RealPointValuePair(new double[]{3.5, 0.5}, 7.0, false);

        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return (int) (o1.getValue() - o2.getValue());
            }
        };

        optimizer.iterateSimplex(comparator);
        optimizer.iterateSimplex(comparator);

        PointValuePair expectedContracted = new PointValuePair(new double[]{1.1, 2.1}, 3.24);
        assertEquals(expectedContracted.getPoint(), simplex[0].getPointRef(), 0.001);
        assertEquals(expectedContracted.getValue(), simplex[0].getValue(), 0.001);
    }

    @Test
    public void testIterateSimplexContractionRejected() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[5];
        simplex[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 3.0, false);
        simplex[1] = new RealPointValuePair(new double[]{3.0, 1.0}, 5.0, false);
        simplex[2] = new RealPointValuePair(new double[]{2.0, 2.0}, 4.0, false);
        simplex[3] = new RealPointValuePair(new double[]{0.5, 3.5}, 6.0, false);
        simplex[4] = new RealPointValuePair(new double[]{3.5, 0.5}, 7.0, false);

        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return (int) (o1.getValue() - o2.getValue());
            }
        };

        optimizer.iterateSimplex(comparator);
        optimizer.iterateSimplex(comparator);

        PointValuePair expectedReflected = new PointValuePair(new double[]{1.8, 1.8}, 3.288);
        PointValuePair expectedExpanded = new PointValuePair(new double[]{1.4142135625, 1.4142135625}, 3.4641016153);

        PointValuePair reflected = new PointValuePair(simplextoPointValuePair(simplex[1]));
        PointValuePair expanded = new PointValuePair(simplextoPointValuePair(simplex[simplex.length - 1]));

        assertTrue(reflected.getValue() > expectedReflected.getValue());
        assertTrue(expanded.getValue() < expectedExpanded.getValue());
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testIterateSimplexFunctionEvaluationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[5];
        simplex[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 3.0, false);
        simplex[1] = new RealPointValuePair(new double[]{3.0, 1.0}, 5.0, false);
        simplex[2] = new RealPointValuePair(new double[]{2.0, 2.0}, 4.0, false);
        simplex[3] = new RealPointValuePair(new double[]{0.5, 3.5}, 6.0, false);
        simplex[4] = new RealPointValuePair(new double[]{3.5, 0.5}, 7.0, false);

        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                throw new UnsupportedOperationException();
            }
        };

        optimizer.iterateSimplex(comparator);
    }

    @Test(expected = OptimizationException.class)
    public void testIterateSimplexMaxEvaluationsExceeded() throws Exception {
        MultiDirectional optimizer = new MultiDirectional() {
            @Override
            public int getMaxIterations() {
                return 1;
            }
        };

        RealPointValuePair[] simplex = new RealPointValuePair[5];
        simplex[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 3.0, false);
        simplex[1] = new RealPointValuePair(new double[]{3.0, 1.0}, 5.0, false);
        simplex[2] = new RealPointValuePair(new double[]{2.0, 2.0}, 4.0, false);
        simplex[3] = new RealPointValuePair(new double[]{0.5, 3.5}, 6.0, false);
        simplex[4] = new RealPointValuePair(new double[]{3.5, 0.5}, 7.0, false);

        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return (int) (o1.getValue() - o2.getValue());
            }
        };

        optimizer.iterateSimplex(comparator);
    }

    @Test
    public void testGetKhi() {
        MultiDirectional optimizer = new MultiDirectional();
        assertEquals(2.0, optimizer.getKhi(), 0.001);
    }

    @Test
    public void testSetKhi() {
        MultiDirectional optimizer = new MultiDirectional();
        optimizer.setKhi(3.5);
        assertEquals(3.5, optimizer.getKhi(), 0.001);
    }

    @Test
    public void testGetGamma() {
        MultiDirectional optimizer = new MultiDirectional();
        assertEquals(0.5, optimizer.getGamma(), 0.001);
    }

    @Test
    public void testSetGamma() {
        MultiDirectional optimizer = new MultiDirectional();
        optimizer.setGamma(0.8);
        assertEquals(0.8, optimizer.getGamma(), 0.001);
    }

    @Test
    public void testRealConvergenceCheckerAccepted() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[5];
        simplex[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 3.0, false);
        simplex[1] = new RealPointValuePair(new double[]{3.0, 1.0}, 5.0, false);
        simplex[2] = new RealPointValuePair(new double[]{2.0, 2.0}, 4.0, false);
        simplex[3] = new RealPointValuePair(new double[]{0.5, 3.5}, 6.0, false);
        simplex[4] = new RealPointValuePair(new double[]{3.5, 0.5}, 7.0, false);

        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return (int) (o1.getValue() - o2.getValue());
            }
        };

        RealConvergenceChecker convergenceChecker = new RealConvergenceChecker() {
            @Override
            public boolean isConverged(RealPointValuePair current, RealPointValuePair best, double terminationThreshold) {
                return current.getValue() <= best.getValue() + terminationThreshold;
            }
        };

        optimizer.setConvergenceChecker(convergenceChecker);

        optimizer.iterateSimplex(comparator);

        assertTrue(convergenceChecker.isConverged(simplex[0], simplex[0], 0.001));
    }

    @Test
    public void testRealConvergenceCheckerNotAccepted() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[5];
        simplex[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 3.0, false);
        simplex[1] = new RealPointValuePair(new double[]{3.0, 1.0}, 5.0, false);
        simplex[2] = new RealPointValuePair(new double[]{2.0, 2.0}, 4.0, false);
        simplex[3] = new RealPointValuePair(new double[]{0.5, 3.5}, 6.0, false);
        simplex[4] = new RealPointValuePair(new double[]{3.5, 0.5}, 7.0, false);

        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return (int) (o1.getValue() - o2.getValue());
            }
        };

        RealConvergenceChecker convergenceChecker = new RealConvergenceChecker() {
            @Override
            public boolean isConverged(RealPointValuePair current, RealPointValuePair best, double terminationThreshold) {
                return current.getValue() > best.getValue() + terminationThreshold;
            }
        };

        optimizer.setConvergenceChecker(convergenceChecker);

        optimizer.iterateSimplex(comparator);

        assertFalse(convergenceChecker.isConverged(simplex[0], simplex[0], 0.001));
    }

    private PointValuePair simplextoPointValuePair(RealPointValuePair simplex) {
        return new PointValuePair(simplex.getPointRef(), simplex.getValue());
    }

}