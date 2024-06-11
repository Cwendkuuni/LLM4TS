import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            public void estimate(EstimationProblem problem) {}
        };

        estimator.setMaxCostEval(100);
        assertEquals(100, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            public void estimate(EstimationProblem problem) {}
        };

        assertEquals(0, estimator.getCostEvaluations());

        estimator.setMaxCostEval(100);
        estimator.estimate(someEstimationProblem);
        assertEquals(1, estimator.getCostEvaluations());

        estimator.estimate(someEstimationProblem);
        assertEquals(2, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            public void estimate(EstimationProblem problem) {}
        };

        assertEquals(0, estimator.getJacobianEvaluations());

        estimator.estimate(someEstimationProblem);
        assertEquals(1, estimator.getJacobianEvaluations());

        estimator.estimate(someEstimationProblem);
        assertEquals(2, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new AbstractEstimator() {
            public void estimate(EstimationProblem problem) {}
        };

        estimator.updateJacobian();

        // assert something about the updated jacobian
        // e.g. that it is not null or empty
        assertNotNull(estimator.getJacobian());
        assertNotEquals(0, estimator.getJacobian().length);
    }

    @Test
    public void testIncrementJacobianEvaluationsCounter() {
        AbstractEstimator estimator = new AbstractEstimator() {
            public void estimate(EstimationProblem problem) {}
        };

        int initialCount = estimator.getJacobianEvaluations();
        estimator.incrementJacobianEvaluationsCounter();
        assertEquals(initialCount + 1, estimator.getJacobianEvaluations());

        estimator.incrementJacobianEvaluationsCounter();
        assertEquals(initialCount + 2, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateResidualsAndCost() {
        AbstractEstimator estimator = new AbstractEstimator() {
            public void estimate(EstimationProblem problem) {}
        };

        estimator.updateResidualsAndCost();

        // assert something about the updated residuals and cost
        // e.g. that they are not null or empty
        assertNotNull(estimator.getResiduals());
        assertNotEquals(0, estimator.getResiduals().length);
        assertNotEquals(0, estimator.getCost());
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            public void estimate(EstimationProblem problem) {}
        };

        double rms = estimator.getRMS(someEstimationProblem);

        // assert something about the computed RMS value
        // e.g. that it is greater than or equal to zero
        assertTrue(rms >= 0);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            public void estimate(EstimationProblem problem) {}
        };

        double chiSquare = estimator.getChiSquare(someEstimationProblem);

        // assert something about the computed chi-square value
        // e.g. that it is greater than or equal to zero
        assertTrue(chiSquare >= 0);
    }

    @Test
    public void testGetCovariances() {
        AbstractEstimator estimator = new AbstractEstimator() {
            public void estimate(EstimationProblem problem) {}
        };

        double[][] covariances = estimator.getCovariances(someEstimationProblem);

        // assert something about the computed covariances matrix
        // e.g. that it is not null or empty
        assertNotNull(covariances);
        assertNotEquals(0, covariances.length);
        assertNotEquals(0, covariances[0].length);
    }

    @Test
    public void testGuessParametersErrors() {
        AbstractEstimator estimator = new AbstractEstimator() {
            public void estimate(EstimationProblem problem) {}
        };

        double[] errors = estimator.guessParametersErrors(someEstimationProblem);

        // assert something about the computed errors
        // e.g. that they are not null or empty
        assertNotNull(errors);
        assertNotEquals(0, errors.length);
    }

    @Test
    public void testInitializeEstimate() {
        AbstractEstimator estimator = new AbstractEstimator() {
            public void estimate(EstimationProblem problem) {}
        };

        estimator.initializeEstimate(someEstimationProblem);

        // assert something about the initialized values
        // e.g. that the costEvaluations and jacobianEvaluations counters are zero
        assertEquals(0, estimator.getCostEvaluations());
        assertEquals(0, estimator.getJacobianEvaluations());
    }

    @Test
    public void testEstimate() {
        AbstractEstimator estimator = new AbstractEstimator() {
            public void estimate(EstimationProblem problem) {
                // implementation of the estimate method
            }
        };

        // test the estimate method with some estimation problem
        estimator.estimate(someEstimationProblem);

        // assert something about the estimated parameters
        // e.g. that they are not null or empty
        assertNotNull(someEstimationProblem.getAllParameters());
        assertNotEquals(0, someEstimationProblem.getAllParameters().length);
    }

}