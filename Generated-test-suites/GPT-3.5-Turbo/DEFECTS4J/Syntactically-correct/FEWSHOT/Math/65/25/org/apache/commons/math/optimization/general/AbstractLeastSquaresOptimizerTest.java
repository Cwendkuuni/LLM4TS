package org.apache.commons.math.optimization.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {
    private AbstractLeastSquaresOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
    }

    @Test
    public void testSetAndGetMaxIterations() {
        optimizer.setMaxIterations(250);
        assertEquals(250, optimizer.getMaxIterations());
    }

    @Test
    public void testGetIterations() {
        optimizer.setMaxIterations(100);
        assertEquals(0, optimizer.getIterations());
        optimizer.incrementIterationsCounter();
        assertEquals(1, optimizer.getIterations());
    }

    @Test
    public void testSetAndGetMaxEvaluations() {
        optimizer.setMaxEvaluations(500);
        assertEquals(500, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        optimizer.setMaxEvaluations(200);
        assertEquals(0, optimizer.getEvaluations());
        optimizer.updateResidualsAndCost();
        assertEquals(1, optimizer.getEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        assertEquals(0, optimizer.getJacobianEvaluations());
        optimizer.updateJacobian();
        assertEquals(1, optimizer.getJacobianEvaluations());
    }

    @Test
    public void testSetAndGetConvergenceChecker() {
        VectorialConvergenceChecker checker = new VectorialConvergenceChecker() {
            @Override
            public boolean converged(int iteration, double[] previous, double[] current) {
                return false;
            }
        };
        optimizer.setConvergenceChecker(checker);
        assertTrue(optimizer.getConvergenceChecker() == checker);
    }

    @Test
    public void testIncrementIterationsCounter() {
        optimizer.setMaxIterations(5);
        try {
            for (int i = 0; i < 5; i++) {
                optimizer.incrementIterationsCounter();
            }
        } catch (Exception e) {
            // Shouldn't fail
        }
        try {
            optimizer.incrementIterationsCounter();
        } catch (Exception e) {
            assertEquals("Maximal number of iterations (5) exceeded", e.getMessage());
        }
    }

    @Test
    public void testUpdateJacobian() {
        // TODO
    }

    @Test
    public void testUpdateResidualsAndCost() {
        // TODO
    }

    @Test
    public void testGetRMS() {
        // TODO
    }

    @Test
    public void testGetChiSquare() {
        // TODO
    }

    @Test
    public void testGetCovariances() {
        // TODO
    }

    @Test
    public void testGuessParametersErrors() {
        // TODO
    }

    @Test
    public void testOptimize() {
        // TODO
    }

}