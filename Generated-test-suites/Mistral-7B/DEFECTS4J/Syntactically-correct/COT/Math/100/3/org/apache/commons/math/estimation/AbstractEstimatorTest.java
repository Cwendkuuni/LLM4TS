import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import org.apache.commons.math.estimation.EstimationException;
import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.WeightedMeasurement;

public class AbstractEstimatorTest {

    @Test
    public void testSetAndGetMaxCostEval() throws Exception {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int costEvaluations = 0;
            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                costEvaluations++;
            }
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                updateResidualsAndCost();
                updateResidualsAndCost();
            }
        };
        assertEquals(0, estimator.getCostEvaluations());
        estimator.estimate(new DummyEstimationProblem());
        estimator.estimate(new DummyEstimationProblem());
        assertEquals(2, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int jacobianEvaluations = 0;
            @Override
            protected void incrementJacobianEvaluationsCounter() {
                jacobianEvaluations++;
            }
            @Override
            protected void updateJacobian() {
                incrementJacobianEvaluationsCounter();
            }
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                updateJacobian();
                updateJacobian();
            }
        };
        assertEquals(0, estimator.getJacobianEvaluations());
        estimator.estimate(new DummyEstimationProblem());
        estimator.estimate(new DummyEstimationProblem());
        assertEquals(2, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new AbstractEstimator() {
            WeightedMeasurement[] measurements = new WeightedMeasurement[1];
            EstimatedParameter[] parameters = new EstimatedParameter[1];
            int rows = 1, cols = 1;
            double[] jacobian = new double[rows * cols];
            @Override
            protected void updateJacobian() {
                jacobian[0] = 1.0;
            }
            @Override
            protected void incrementJacobianEvaluationsCounter() {
            }
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                updateJacobian();
            }
        };
        estimator.initializeEstimate(new DummyEstimationProblem());
        estimator.estimate(new DummyEstimationProblem());
        double[] expectedJacobian = new double[]{1.0};
        assertArrayEquals(expectedJacobian, estimator.jacobian);
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            WeightedMeasurement[] measurements = new WeightedMeasurement[1];
            EstimatedParameter[] parameters = new EstimatedParameter[1];
            double cost = Double.POSITIVE_INFINITY, costEvaluated = 0.0;
            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                costEvaluated = Math.sqrt(Math.random());
                cost = Math.sqrt(costEvaluated * costEvaluated + 1.0);
            }
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                updateResidualsAndCost();
            }
        };
        estimator.initializeEstimate(new DummyEstimationProblem());
        estimator.estimate(new DummyEstimationProblem());
        double expectedCost = Math.sqrt(Math.random() * Math.random() + 1.0);
        assertEquals(expectedCost, estimator.cost, 0.001);
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getRMS(EstimationProblem problem) {
                return Math.sqrt(problem.getWeightedMeasurements().length) * 0.1;
            }
        };
        EstimationProblem problem = new DummyEstimationProblem(1.0, new WeightedMeasurement[]{
            new WeightedMeasurement(0.1, 0.0),
            new WeightedMeasurement(0.2, 0.1),
            new WeightedMeasurement(0.3, 0.2)
        });
        assertEquals(0.3, estimator.getRMS(problem), 0.001);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getChiSquare(EstimationProblem problem) {
                double totalWeight = 0.0;
                for (WeightedMeasurement wm : problem.getWeightedMeasurements()) {
                    totalWeight += wm.getWeight();
                }
                return totalWeight;
            }
        };
        EstimationProblem problem = new DummyEstimationProblem(1.0, new WeightedMeasurement[]{
            new WeightedMeasurement(0.1, 0.0),
            new WeightedMeasurement(0.2, 0.1),
            new WeightedMeasurement(0.3, 0.2)
        });
        assertEquals(1.6, estimator.getChiSquare(problem), 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[][] getCovariances(EstimationProblem problem) throws EstimationException {
                return new RealMatrixImpl(new Array2DRowRealMatrix(new double[][]{
                    {1.0, 0.0},
                    {0.0, 1.0}
                })).inverse().getData();
            }
        };
        EstimationProblem problem = new DummyEstimationProblem(1.0, new WeightedMeasurement[]{
            new WeightedMeasurement(0.1, 0.0),
            new WeightedMeasurement(0.2, 0.1),
            new WeightedMeasurement(0.3, 0.2)
        });
        double[][] expectedCovariances = new double[][]{
            {2.3, -0.4},
            {-0.4, 1.5}
        };
        assertArrayEquals(expectedCovariances, estimator.getCovariances(problem));
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[] guessParametersErrors(EstimationProblem problem) throws EstimationException {
                return new double[]{Math.sqrt(2.0), Math.sqrt(3.0)};
            }
        };
        EstimationProblem problem = new DummyEstimationProblem(1.0, new WeightedMeasurement[]{
            new WeightedMeasurement(0.1, 0.0),
            new WeightedMeasurement(0.2, 0.1),
            new WeightedMeasurement(0.3, 0.2)
        });
        double[] expectedErrors = new double[]{Math.sqrt(Math.sqrt(2.0)), Math.sqrt(Math.sqrt(3.0))};
        assertArrayEquals(expectedErrors, estimator.guessParametersErrors(problem));
    }

    @Test(expected = EstimationException.class)
    public void testInitializeEstimateWithInvalidArguments() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                throw new RuntimeException("Should not be reached");
            }
        };
        new DummyEstimationProblem(1.0, new WeightedMeasurement[]{
            new WeightedMeasurement(0.1, 0.0),
            new WeightedMeasurement(0.2, 0.1),
            new WeightedMeasurement(0.3, 0.2)
        });
        estimator.initializeEstimate(new DummyEstimationProblem(1.0, new WeightedMeasurement[]{
            new WeightedMeasurement(0.1, 0.0),
            new WeightedMeasurement(0.2, 0.1),
            new WeightedMeasurement(0.3, 0.2)
        }));
    }

    @Test
    public void testEstimateWithInvalidArguments() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                fail("Should not be reached");
            }
        };
        EstimationProblem problem = new DummyEstimationProblem(1.0, new WeightedMeasurement[]{
            new WeightedMeasurement(0.1, 0.0),
            new WeightedMeasurement(0.2, 0.1),
            new WeightedMeasurement(0.3, 0.2)
        });
        assertThrows(EstimationException.class, () -> estimator.estimate(problem));
    }

    private static class DummyEstimationProblem implements EstimationProblem {

        private final double criterion;
        private final WeightedMeasurement[] measurements;

        public DummyEstimationProblem(double criterion, WeightedMeasurement[] measurements) {
            this.criterion = criterion;
            this.measurements = measurements;
        }

        @Override
        public double getCriterion() {
            return criterion;
        }

        @Override
        public double[][] getAllJacobians() {
            return new double[0][];
        }

        @Override
        public double[][] getAllHessians() {
            return new double[0][];
        }

        @Override
        public WeightedMeasurement[] getMeasurements() {
            return measurements;
        }

        @Override
        public WeightedMeasurement getMeasurement(int index) {
            return measurements[index];
        }

        @Override
        public int getMeasurementCount() {
            return measurements.length;
        }

        @Override
        public EstimatedParameter[] getAllParameters() {
            return new EstimatedParameter[0];
        }

        @Override
        public EstimatedParameter getParameter(int index) {
            return null;
        }

        @Override
        public int getParameterCount() {
            return 0;
        }

    }

}