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

        assertEqual(optimizer.getMaxIterations(), 100);
    }

    @Test
    public void testGetMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };

        optimizer.setMaxIterations(100);

        assertEqual(optimizer.getMaxIterations(), 100);
    }

    @Test
    public void testGetIterations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };

        assertEqual(optimizer.getIterations(), 0);
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };

        optimizer.setMaxEvaluations(100);

        assertEqual(optimizer.getMaxEvaluations(), 100);
    }

    @Test
    public void testGetMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };

        optimizer.setMaxEvaluations(100);

        assertEqual(optimizer.getMaxEvaluations(), 100);
    }

    @Test
    public void testGetEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };

        assertEqual(optimizer.getEvaluations(), 0);
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };

        assertEqual(optimizer.getJacobianEvaluations(), 0);
    }

    @Test
    public void testSetConvergenceChecker() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        SimpleVectorialValueChecker checker = new SimpleVectorialValueChecker();

        optimizer.setConvergenceChecker(checker);

        assertEqual(optimizer.getConvergenceChecker(), checker);
    }

    @Test
    public void testGetConvergenceChecker() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        SimpleVectorialValueChecker checker = new SimpleVectorialValueChecker();

        optimizer.setConvergenceChecker(checker);

        assertEqual(optimizer.getConvergenceChecker(), checker);
    }

    @Test
    public void testGetRMS() {
        double[] target = {1, 2, 3};
        double[] weights = {1, 1, 1};
        double[] startPoint = {0, 0, 0};
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return null;
            }
        };

        optimizer.optimize(null, target, weights, startPoint);

        assertEqual(optimizer.getRMS(), 0, 0);
    }

    @Test
    public void testGetChiSquare() {
        double[] target = {1, 2, 3};
        double[] weights = {1, 1, 1};
        double[] startPoint = {0, 0, 0};
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return null;
            }
        };

        optimizer.optimize(null, target, weights, startPoint);

        assertEqual(optimizer.getChiSquare(), 0);
    }

    @Test
    public void testGetCovariances() {
        double[] target = {1, 2, 3};
        double[] weights = {1, 1, 1};
        double[] startPoint = {0, 0, 0};
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return null;
            }
        };

        optimizer.optimize(null, target, weights, startPoint);
        double[][] covariances = optimizer.getCovariances();

        assertEqual(covariances.length, 3);
        assertEqual(covariances[0].length, 3);
    }

    @Test
    public void testGuessParametersErrors() {
        double[] target = {1, 2, 3};
        double[] weights = {1, 1, 1};
        double[] startPoint = {0, 0, 0};
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return null;
            }
        };

        optimizer.optimize(null, target, weights, startPoint);
        double[] errors = optimizer.guessParametersErrors();

        assertEqual(errors.length, 3);
    }

}