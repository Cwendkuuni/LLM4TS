import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testSetMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        optimizer.setMaxIterations(100);
        assert(optimizer.getMaxIterations() == 100);
    }

    @Test
    public void testGetMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        optimizer.setMaxIterations(100);
        assert(optimizer.getMaxIterations() == 100);
    }

    @Test
    public void testGetIterations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        optimizer.setIterations(10);
        assert(optimizer.getIterations() == 10);
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        optimizer.setMaxEvaluations(100);
        assert(optimizer.getMaxEvaluations() == 100);
    }

    @Test
    public void testGetMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        optimizer.setMaxEvaluations(100);
        assert(optimizer.getMaxEvaluations() == 100);
    }

    @Test
    public void testGetEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        optimizer.setEvaluations(10);
        assert(optimizer.getEvaluations() == 10);
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        optimizer.setJacobianEvaluations(10);
        assert(optimizer.getJacobianEvaluations() == 10);
    }

    @Test
    public void testSetConvergenceChecker() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        optimizer.setConvergenceChecker(null);
        assert(optimizer.getConvergenceChecker() == null);
    }

    @Test
    public void testGetConvergenceChecker() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        optimizer.setConvergenceChecker(null);
        assert(optimizer.getConvergenceChecker() == null);
    }

    @Test
    public void testIncrementIterationsCounter() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        optimizer.incrementIterationsCounter();
    }

    @Test
    public void testUpdateJacobian() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        optimizer.updateJacobian();
    }

    @Test
    public void testUpdateResidualsAndCost() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        optimizer.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        assert(optimizer.getRMS() == 0.0);
    }

    @Test
    public void testGetChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        assert(optimizer.getChiSquare() == 0.0);
    }

    @Test
    public void testGetCovariances() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        double[][] covariances = optimizer.getCovariances();
        assert(covariances.length == 0);
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        double[] errors = optimizer.guessParametersErrors();
        assert(errors.length == 0);
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        VectorialPointValuePair result = optimizer.optimize(null, null, null, null);
        assert(result == null);
    }
}