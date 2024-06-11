import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        optimizer.setMaxIterations(100);
        assertEquals(100, optimizer.getMaxIterations());
    }

    @Test
    public void testIterationsCounter() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        optimizer.incrementIterationsCounter();
        assertEquals(1, optimizer.getIterations());
    }

    @Test(expected = OptimizationException.class)
    public void testIterationsCounterExceeded() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        optimizer.setMaxIterations(10);
        for (int i = 0; i <= 10; i++) {
            optimizer.incrementIterationsCounter();
        }
    }

    @Test
    public void testMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        optimizer.setMaxEvaluations(200);
        assertEquals(200, optimizer.getMaxEvaluations());
    }

    @Test
    public void testEvaluationsCounter() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        optimizer.updateResidualsAndCost();
        assertEquals(1, optimizer.getEvaluations());
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testEvaluationsCounterExceeded() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        optimizer.setMaxEvaluations(5);
        for (int i = 0; i <= 5; i++) {
            optimizer.updateResidualsAndCost();
        }
    }

    @Test
    public void testConvergenceChecker() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        optimizer.setConvergenceChecker(new TestConvergenceChecker());
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testRMS() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        double rms = optimizer.getRMS();
        assertEquals(0, rms, 0.00001);
    }

    @Test
    public void testChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        double chiSquare = optimizer.getChiSquare();
        assertEquals(0, chiSquare, 0.00001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testCovariances() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        optimizer.getCovariances();
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        optimizer.guessParametersErrors();
    }

    private class TestOptimizer extends AbstractLeastSquaresOptimizer {
        @Override
        protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
            return null;
        }
    }

    private class TestConvergenceChecker implements VectorialConvergenceChecker {
        @Override
        public boolean converged(int iteration, VectorialPointValuePair previous, VectorialPointValuePair current) {
            return false;
        }
    }

}