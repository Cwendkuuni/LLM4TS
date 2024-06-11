import org.junit.*;
import static org.junit.Assert.assertEquals;

public class AbstractLeastSquaresOptimizerTest {
    @Test
    public void testGetMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };

        optimizer.setMaxIterations(200);

        assertEquals(200, optimizer.getMaxIterations());
    }

    @Test
    public void testGetIterations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };

        optimizer.incrementIterationsCounter();
        optimizer.incrementIterationsCounter();
        
        int expectedIterations = 2;

        assertEquals(expectedIterations, optimizer.getIterations());
    }

    @Test
    public void testSetAndGetMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };

        optimizer.setMaxEvaluations(1000);

        assertEquals(1000, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };

        optimizer.updateResidualsAndCost();
        
        int expectedEvaluations = 1;

        assertEquals(expectedEvaluations, optimizer.getEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };

        optimizer.updateJacobian();
        optimizer.updateJacobian();

        int expectedJacobianEvaluations = 2;

        assertEquals(expectedJacobianEvaluations, optimizer.getJacobianEvaluations());
    }

    @Test
    public void testSetAndGetConvergenceChecker() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };

        VectorialConvergenceChecker convergenceChecker = new SimpleVectorialValueChecker();

        optimizer.setConvergenceChecker(convergenceChecker);

        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testIncrementIterationsCounter() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };

        try {
            optimizer.incrementIterationsCounter();
            optimizer.incrementIterationsCounter();
            optimizer.incrementIterationsCounter();
        } catch (OptimizationException e) {
            // Do nothing
        }

        int expectedIterations = 3;

        assertEquals(expectedIterations, optimizer.getIterations());
    }

    @Test
    public void testUpdateJacobian() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };

        try {
            optimizer.updateJacobian();
            optimizer.updateJacobian();
            optimizer.updateJacobian();
            optimizer.updateJacobian();
        } catch (FunctionEvaluationException e) {
            // Do nothing
        }

        int expectedJacobianEvaluations = 4;

        assertEquals(expectedJacobianEvaluations, optimizer.getJacobianEvaluations());
    }

    @Test
    public void testUpdateResidualsAndCost() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };

        try {
            optimizer.updateResidualsAndCost();
            optimizer.updateResidualsAndCost();
            optimizer.updateResidualsAndCost();
        } catch (FunctionEvaluationException e) {
            // Do nothing
        }

        int expectedEvaluations = 3;

        assertEquals(expectedEvaluations, optimizer.getEvaluations());
    }

    @Test
    public void testGetRMS() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };

        double expectedRMS = 5.0;

        assertEquals(expectedRMS, optimizer.getRMS(), 0.0);
    }

    @Test
    public void testGetChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };

        double expectedChiSquare = 10.0;

        assertEquals(expectedChiSquare, optimizer.getChiSquare(), 0.0);
    }

    @Test
    public void testGetCovariances() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };

        double[][] expectedCovariances = new double[][] {{1.0, 0.0}, {0.0, 1.0}};

        for (int i = 0; i < expectedCovariances.length; i++) {
            assertArrayEquals(expectedCovariances[i], optimizer.getCovariances()[i], 0.0);
        }
    }

    @Test
    public void testGuessParametersErrors() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };

        double[] expectedErrors = new double[] {1.0, 1.0};

        assertArrayEquals(expectedErrors, optimizer.guessParametersErrors(), 0.0);
    }
}