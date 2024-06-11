import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testSetMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        optimizer.setMaxIterations(100);
        assert optimizer.getMaxIterations() == 100;
    }

    @Test
    public void testGetIterations() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        optimizer.incrementIterationsCounter();
        assert optimizer.getIterations() == 1;
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        optimizer.setMaxEvaluations(100);
        assert optimizer.getMaxEvaluations() == 100;
    }

    @Test
    public void testGetEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        optimizer.updateResidualsAndCost();
        assert optimizer.getEvaluations() == 1;
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        optimizer.updateJacobian();
        assert optimizer.getJacobianEvaluations() == 1;
    }

    @Test
    public void testSetConvergenceChecker() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
        assert optimizer.getConvergenceChecker() != null;
    }

    @Test
    public void testIncrementIterationsCounter() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        try {
            optimizer.incrementIterationsCounter();
        } catch (OptimizationException e) {
            assert e.getMessage().equals("Maximum number of iterations (100) exceeded");
        }
    }

    @Test
    public void testGetRMS() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        double rms = optimizer.getRMS();
        assert rms == 0.0;
    }

    @Test
    public void testGetChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        double chiSquare = optimizer.getChiSquare();
        assert chiSquare == 0.0;
    }

    @Test
    public void testGetCovariances() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        try {
            double[][] covariances = optimizer.getCovariances();
            assert covariances != null;
        } catch (FunctionEvaluationException | OptimizationException e) {
            assert false;
        }
    }

    @Test
    public void testGuessParametersErrors() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        try {
            double[] errors = optimizer.guessParametersErrors();
            assert errors != null;
        } catch (FunctionEvaluationException | OptimizationException e) {
            assert false;
        }
    }

    private class ConcreteLeastSquaresOptimizer extends AbstractLeastSquaresOptimizer {
        @Override
        protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
            return null;
        }
    }
}