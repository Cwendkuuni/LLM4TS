import org.apache.commons.math3.estimation.AbstractEstimator;
import org.apache.commons.math3.estimation.EstimationException;
import org.apache.commons.math3.estimation.EstimationProblem;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // method under test
            }
        };
        estimator.setMaxCostEval(5);
        Assert.assertEquals(5, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // method under test
            }
        };
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        Assert.assertEquals(5, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // method under test
            }
        };
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        Assert.assertEquals(5, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // method under test
            }
        };
        estimator.initializeEstimate(getTestProblem());

        estimator.updateJacobian();

        double[] expectedJacobian = new double[]{
                0.0, 0.1, // row 0
                0.2, 0.3, // row 1
                0.4, 0.5, // row 2
                0.6, 0.7, // row 3
                0.8, 0.9, // row 4
                1.0, 1.1, // row 5
        };

        Assert.assertArrayEquals(expectedJacobian, estimator.jacobian, 0.0);
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // method under test
            }
        };
        estimator.initializeEstimate(getTestProblem());

        estimator.updateResidualsAndCost();
        double expectedCost = Math.sqrt(1.0);
        Assert.assertEquals(expectedCost, estimator.cost, 0.0);

        double[] expectedResiduals = new double[]{
                -Math.sqrt(2.0),
                Math.sqrt(2.0),
                -Math.sqrt(3.0),
                Math.sqrt(3.0),
                -Math.sqrt(4.0),
                Math.sqrt(4.0),
        };

        Assert.assertArrayEquals(expectedResiduals, estimator.residuals, 0.0);
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // method under test
            }
        };
        EstimationProblem problem = getTestProblem();
        estimator.initializeEstimate(problem);

        estimator.updateResidualsAndCost();
        double expectedRMS = Math.sqrt(estimator.cost / problem.getMeasurements().length);
        Assert.assertEquals(expectedRMS, estimator.getRMS(problem), 0.0);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // method under test
            }
        };
        EstimationProblem problem = getTestProblem();
        estimator.initializeEstimate(problem);

        estimator.updateResidualsAndCost();
        double expectedChiSquare = 0.0;
        for (int i = 0; i < problem.getMeasurements().length; i++) {
            WeightedMeasurement wm = problem.getMeasurements()[i];
            double residual = wm.getResidual();
            expectedChiSquare += residual * residual / wm.getWeight();
        }

        Assert.assertEquals(expectedChiSquare, estimator.getChiSquare(problem), 0.0);
    }

    @Test(expected = EstimationException.class)
    public void testGetCovariancesThrowsException() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // method under test
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getMeasurementsLength() {
                return -1;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }

            @Override
            public double[] getAllParameters() {
                return new double[0];
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        estimator.getCovariances(problem);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // method under test
            }
        };
        EstimationProblem problem = getTestProblem();
        estimator.initializeEstimate(problem);

        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        double[] expectedErrors = new double[]{0.25, 0.25};
        Assert.assertArrayEquals(expectedErrors, estimator.guessParametersErrors(problem), 0.0);
    }

    private EstimationProblem getTestProblem() {
        WeightedMeasurement wm0 = new WeightedMeasurement(1.0, new double[]{1.0, 1.0});
        WeightedMeasurement wm1 = new WeightedMeasurement(2.0, new double[]{2.0, 2.0});
        WeightedMeasurement wm2 = new WeightedMeasurement(3.0, new double[]{3.0, 3.0});
        WeightedMeasurement wm3 = new WeightedMeasurement(4.0, new double[]{4.0, 4.0});
        WeightedMeasurement wm4 = new WeightedMeasurement(5.0, new double[]{5.0, 5.0});
        WeightedMeasurement wm5 = new WeightedMeasurement(6.0, new double[]{6.0, 6.0});

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getMeasurementsLength() {
                return 6;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[]{wm0, wm1, wm2, wm3, wm4, wm5};
            }

            @Override
            public double[] getAllParameters() {
                return new double[]{0.0, 0.0};
            }
        };

        for (int i = 0; i < problem.getMeasurements().length; i++) {
            problem.getMeasurements()[i].setResidual(Math.sqrt(Math.pow(2.0, i + 1)) - 1.0);
        }

        return problem;
    }

}