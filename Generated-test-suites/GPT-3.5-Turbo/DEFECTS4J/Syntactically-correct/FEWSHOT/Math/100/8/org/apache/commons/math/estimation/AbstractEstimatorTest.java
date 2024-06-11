/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;

    @Before
    public void setUp() {
        estimator = new AbstractEstimator() {
            public void estimate(EstimationProblem problem) throws EstimationException {
                // Dummy implementation
            }
        };
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(100);
        assertEquals(100, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        assertEquals(0, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        assertEquals(0, estimator.getJacobianEvaluations());
    }

    @Test
    public void testIncrementJacobianEvaluationsCounter() {
        estimator.incrementJacobianEvaluationsCounter();
        assertEquals(1, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateResidualsAndCost() {
        // Setup
        EstimationException exception = new EstimationException("maximal number of evaluations exceeded ({0})", new Object[] { 100 });
        estimator.setMaxCostEval(100);
        estimator.updateResidualsAndCost();
        assertEquals(1, estimator.getCostEvaluations());

        // Test exception thrown
        try {
            estimator.updateResidualsAndCost();
            fail("EstimationException not thrown");
        } catch (EstimationException e) {
            assertTrue(e.getMessage().contains(exception.getMessage()));
        }
    }

    @Test
    public void testGetRMS() {
        // Setup
        EstimationProblem problem = new EstimationProblem() {
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 1, 1) };
            }
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[0];
            }
        };
        assertEquals(1, estimator.getRMS(problem), 1e-15);
    }

    @Test
    public void testGetChiSquare() {
        // Setup
        EstimationProblem problem = new EstimationProblem() {
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 1, 1) };
            }
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[0];
            }
        };
        assertEquals(1, estimator.getChiSquare(problem), 1e-15);
    }

    @Test
    public void testGetCovariances() {
        // Setup
        EstimationProblem problem = new EstimationProblem() {
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 1, 1) };
            }
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[0];
            }
        };

        // Verify EstimationException thrown
        try {
            estimator.getCovariances(problem);
            fail("EstimationException not thrown");
        } catch (EstimationException e) {
            assertTrue(e.getMessage().contains("unable to compute covariances: singular problem"));
        }
    }

    @Test
    public void testGuessParametersErrors() {
        // Setup
        EstimationProblem problem = new EstimationProblem() {
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 1, 1) };
            }
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[0];
            }
        };

        // Verify EstimationException thrown
        try {
            estimator.guessParametersErrors(problem);
            fail("EstimationException not thrown");
        } catch (EstimationException e) {
            assertTrue(e.getMessage().contains("no degrees of freedom"));
        }
    }

    @Test
    public void testInitializeEstimate() {
        // Setup
        EstimationProblem problem = new EstimationProblem() {
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 1, 1) };
            }
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[0];
            }
        };

        // Verify initialization
        estimator.initializeEstimate(problem);
        assertEquals(0, estimator.getCostEvaluations());
        assertEquals(0, estimator.getJacobianEvaluations());
        assertEquals(1, estimator.measurements.length);
        assertEquals(0, estimator.parameters.length);
        assertEquals(1, estimator.rows);
        assertEquals(0, estimator.cols);
        assertEquals(1, estimator.jacobian.length);
        assertEquals(1, estimator.residuals.length);
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost, 1e-15);
    }

}