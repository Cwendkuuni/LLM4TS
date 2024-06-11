import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testSetMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new MockOptimizer();
        optimizer.setMaxIterations(100);
        assertEquals(100, optimizer.getMaxIterations());
    }
    
    @Test
    public void testGetMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new MockOptimizer();
        optimizer.setMaxIterations(100);
        assertEquals(100, optimizer.getMaxIterations());
    }

    @Test
    public void testGetIterations() {
        AbstractLeastSquaresOptimizer optimizer = new MockOptimizer();
        optimizer.incrementIterationsCounter();
        assertEquals(1, optimizer.getIterations());
    }

    @Test
    public void testGetMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new MockOptimizer();
        optimizer.setMaxEvaluations(100);
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new MockOptimizer();
        optimizer.incrementObjectiveEvaluations();
        assertEquals(1, optimizer.getEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new MockOptimizer();
        optimizer.incrementJacobianEvaluations();
        assertEquals(1, optimizer.getJacobianEvaluations());
    }

    @Test
    public void testSetConvergenceChecker() {
        AbstractLeastSquaresOptimizer optimizer = new MockOptimizer();
        optimizer.setConvergenceChecker(new MockConvergenceChecker());
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceChecker() {
        AbstractLeastSquaresOptimizer optimizer = new MockOptimizer();
        optimizer.setConvergenceChecker(new MockConvergenceChecker());
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test(expected = OptimizationException.class)
    public void testIncrementIterationsCounter_MaxIterationsExceeded() {
        AbstractLeastSquaresOptimizer optimizer = new MockOptimizer();
        optimizer.setMaxIterations(10);
        for (int i = 0; i < 15; i++) {
            optimizer.incrementIterationsCounter();
        }
    }

    @Test
    public void testUpdateJacobian() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new MockOptimizer();
        optimizer.updateJacobian();
        assertNotNull(optimizer.getJacobian());
    }

    @Test
    public void testUpdateResidualsAndCost() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new MockOptimizer();
        optimizer.updateResidualsAndCost();
        assertNotNull(optimizer.getObjective());
        assertNotNull(optimizer.getResiduals());
        assertTrue(optimizer.getCost() >= 0);
    }

    @Test
    public void testGetRMS() {
        AbstractLeastSquaresOptimizer optimizer = new MockOptimizer();
        double rms = optimizer.getRMS();
        assertTrue(rms >= 0);
    }

    @Test
    public void testGetChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new MockOptimizer();
        double chiSquare = optimizer.getChiSquare();
        assertTrue(chiSquare >= 0);
    }

    @Test
    public void testGetCovariances() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new MockOptimizer();
        double[][] covariances = optimizer.getCovariances();
        assertNotNull(covariances);
        assertTrue(covariances.length > 0);
        assertTrue(covariances[0].length > 0);
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new MockOptimizer();
        double[] errors = optimizer.guessParametersErrors();
        assertNotNull(errors);
        assertTrue(errors.length > 0);
    }

    private class MockOptimizer extends AbstractLeastSquaresOptimizer {

        @Override
        protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
            return null;
        }
    }

    private class MockConvergenceChecker implements VectorialConvergenceChecker {

        @Override
        public boolean converged(int iteration, VectorialPointValuePair previous, VectorialPointValuePair current) {
            return false;
        }
    }

}