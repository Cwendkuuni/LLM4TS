package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {
    private class TestOptimizer extends AbstractLeastSquaresOptimizer {

        TestOptimizer() {
            super();
        }

        @Override
        protected VectorialPointValuePair doOptimize() {
            return null;
        }

    }

    private TestOptimizer optimizer;
    private double[] target;
    private double[] weights;
    private double[] startPoint;

    @Before
    public void setUp() {
        optimizer = new TestOptimizer();
        target = new double[]{1.0, 2.0, 3.0};
        weights = new double[]{0.5, 0.5, 0.5};
        startPoint = new double[]{0.0, 0.0, 0.0};
    }

    @Test
    public void testSetMaxIterations() {
        optimizer.setMaxIterations(100);
        assertEquals(100, optimizer.getMaxIterations());
    }

    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(200);
        assertEquals(200, optimizer.getMaxEvaluations());
    }

    @Test
    public void testSetConvergenceChecker() {
        assertEquals(null, optimizer.getConvergenceChecker());

        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
        assertEquals(SimpleVectorialValueChecker.class, optimizer.getConvergenceChecker().getClass());
    }

    @Test
    public void testIncrementIterationsCounter() {
        try {
            optimizer.incrementIterationsCounter();
            fail();
        } catch (OptimizationException e) {
            assertEquals("MaxIterations exceeded", e.getMessage());
        }
    }

    @Test
    public void testUpdateJacobian() {
        try {
            optimizer.updateJacobian();
            fail();
        } catch (FunctionEvaluationException e) {
            assertEquals("LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE", e.getMessage());
        }
    }

    @Test
    public void testUpdateResidualsAndCost() {
        try {
            optimizer.updateResidualsAndCost();
            fail();
        } catch (FunctionEvaluationException e) {
            assertEquals("LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE", e.getMessage());
        }
    }

    @Test
    public void testGetRMS() {
        double rms = optimizer.getRMS();
        assertTrue(Double.isNaN(rms));
    }

    @Test
    public void testGetChiSquare() {
        double chiSquare = optimizer.getChiSquare();
        assertTrue(Double.isNaN(chiSquare));
    }

    @Test
    public void testGetCovariances() {
        try {
            double[][] covariances = optimizer.getCovariances();
            fail();
        } catch (FunctionEvaluationException e) {
            assertEquals("LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE", e.getMessage());
        } catch (OptimizationException e) {
            assertEquals("LocalizedFormats.UNABLE_TO_COMPUTE_COVARIANCE_SINGULAR_PROBLEM", e.getMessage());
        }
    }

    @Test
    public void testGuessParametersErrors() {
        try {
            double[] errors = optimizer.guessParametersErrors();
            fail();
        } catch (FunctionEvaluationException e) {
            assertEquals("LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE", e.getMessage());
        } catch (OptimizationException e) {
            assertEquals("LocalizedFormats.NO_DEGREES_OF_FREEDOM", e.getMessage());
        }
    }

    @Test
    public void testOptimize() {
        try {
            VectorialPointValuePair result = optimizer.optimize(null, target, weights, startPoint);
            assertNull(result);
        } catch (FunctionEvaluationException e) {
            assertEquals("LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE", e.getMessage());
        } catch (OptimizationException e) {
            assertEquals("MaxEvaluations exceeded", e.getMessage());
        }
    }
}