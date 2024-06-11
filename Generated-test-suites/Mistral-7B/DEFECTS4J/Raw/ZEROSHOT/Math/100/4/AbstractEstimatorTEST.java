import org.apache.commons.math3.estimation.AbstractEstimator;
import org.apache.commons.math3.estimation.EstimationException;
import org.apache.commons.math3.estimation.EstimationProblem;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty method
            }
        };
        estimator.setMaxCostEval(10);
        Assert.assertEquals(10, estimator.getCostEvaluations());
        estimator.setMaxCostEval(5);
        Assert.assertEquals(5, estimator.getCostEvaluations());
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty method
            }
        };
        estimator.setMaxCostEval(10);
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        Assert.assertEquals(3, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty method
            }

            @Override
            protected void updateJacobian() {
                incrementJacobianEvaluationsCounter();
            }
        };
        estimator.updateJacobian();
        estimator.updateJacobian();
        Assert.assertEquals(2, estimator.getJacobianEvaluations());
    }

    @Test
    public void testGetRMS() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealVector getAllParameters() {
                return new ArrayRealVector(new double[]{1, 2});
            }

            @Override
            public RealVector getMeasurements() {
                return new ArrayRealVector(new double[]{1.1, 1.2, 1.3});
            }
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty method
            }
        };
        estimator.setMaxCostEval(10);
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        double rms = estimator.getRMS(problem);
        Assert.assertEquals(1.021152960382347, rms, 0.001);
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealVector getAllParameters() {
                return new ArrayRealVector(new double[]{1, 2});
            }

            @Override
            public RealVector getMeasurements() {
                return new ArrayRealVector(new double[]{1.1, 1.2, 1.3});
            }
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty method
            }
        };
        estimator.setMaxCostEval(10);
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        double chiSquare = estimator.getChiSquare(problem);
        Assert.assertEquals(3.13157859375, chiSquare, 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealVector getAllParameters() {
                return new ArrayRealVector(new double[]{1, 2});
            }

            @Override
            public RealVector[] getMeasurements() {
                return new RealVector[]{new ArrayRealVector(new double[]{1.1, 1.1}), new ArrayRealVector(new double[]{1.2, 1.2})};
            }
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty method
            }
        };
        estimator.setMaxCostEval(10);
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        RealArray2D covariances = estimator.getCovariances(problem);
        Assert.assertArrayEquals(new double[][]{
                {0.002017998616406249, 0.001373984115324825},
                {0.001373984115324825, 0.001553296273663555}
        }, covariances.getData(), 0.001);
    }

    @Test(expected = EstimationException.class)
    public void testGuessParametersErrorsWithSingularProblem() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealVector getAllParameters() {
                return new ArrayRealVector(new double[]{1, 1});
            }

            @Override
            public RealVector[] getMeasurements() {
                return new RealVector[]{new ArrayRealVector(new double[]{1, 1}), new ArrayRealVector(new double[]{1, 1})};
            }
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty method
            }
        };
        estimator.initializeEstimate(problem);
        estimator.guessParametersErrors(problem);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealVector getAllParameters() {
                return new ArrayRealVector(new double[]{1, 2});
            }

            @Override
            public RealVector[] getMeasurements() {
                return new RealVector[]{new ArrayRealVector(new double[]{1.1, 1.2}), new ArrayRealVector(new double[]{1.3, 1.4})};
            }
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty method
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        double[] errors = estimator.guessParametersErrors(problem);
        Assert.assertArrayEquals(new double[]{0.1021152960382348, 0.0700373781677213}, errors, 0.001);
    }

}