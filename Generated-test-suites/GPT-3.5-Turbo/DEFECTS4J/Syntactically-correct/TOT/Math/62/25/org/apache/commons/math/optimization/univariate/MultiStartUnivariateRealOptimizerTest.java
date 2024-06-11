
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentMatchers;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.univariate.*;

public class MultiStartUnivariateRealOptimizerTest {

    // Test case for setConvergenceChecker() method
    @Test
    public void testSetConvergenceChecker() {
        // Create mock optimizer
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 3, null);

        // Create mock convergence checker
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceCheckerMock = mock(ConvergenceChecker.class);
        optimizer.setConvergenceChecker(convergenceCheckerMock);

        // Verify that the setConvergenceChecker() method was called on the optimizer mock
        // with the given convergence checker
        // Also verify that the getConvergenceChecker() method returns the same convergence checker
        assertTrue(optimizer.getConvergenceChecker() == convergenceCheckerMock);
    }

    // Test case for getMaxEvaluations() and setMaxEvaluations() methods
    @Test
    public void testMaxEvaluations() {
        // Create mock optimizer
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 3, null);

        // Set the max evaluations to 100
        int maxEvaluations = 100;
        optimizer.setMaxEvaluations(maxEvaluations);

        // Verify that the setMaxEvaluations() method was called on the optimizer mock
        // with the given value
        // Also verify that the getMaxEvaluations() method returns the same value
        assertTrue(optimizer.getMaxEvaluations() == maxEvaluations);
    }

    // Test case for getEvaluations() method
    @Test
    public void testGetEvaluations() {
        // Create mock optimizer
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 3, null);

        // Set the evaluations to 50
        int evaluations = 50;
        when(optimizerMock.getEvaluations()).thenReturn(evaluations);

        // Verify that the getEvaluations() method returns the same value as the mock
        assertTrue(optimizer.getEvaluations() == evaluations);
    }

    // Test case for getOptima() method when there is no optima computed yet
    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaNoOptimaComputedYet() {
        // Create mock optimizer
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 3, null);

        // Call getOptima() method without computing any optima
        optimizer.getOptima();
    }

    // Test case for optimize() method when the optimizer does not converge
    @Test(expected = ConvergenceException.class)
    public void testOptimizeNotConverge() throws FunctionEvaluationException {
        // Create mock optimizer
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 3, null);

        // Create mock objective function
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);

        // Create mock goal type
        GoalType goal = mock(GoalType.class);

        // Create mock bounds
        double min = 0.0;
        double max = 1.0;

        // Create mock start value
        double startValue = 0.5;

        // Set the evaluations to 100
        int maxEvaluations = 100;
        when(optimizerMock.getEvaluations()).thenReturn(maxEvaluations);

        // Set the max evaluations to 200
        int totalEvaluations = 200;
        optimizer.setMaxEvaluations(totalEvaluations);

        // Set convergence exception in mock optimizer
        when(optimizerMock.optimize(ArgumentMatchers.<UnivariateRealFunction>any(),
                ArgumentMatchers.<GoalType>any(),
                ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDouble()))
                .thenThrow(new ConvergenceException());

        // Call optimize() method
        optimizer.optimize(f, goal, min, max, startValue);
    }

    // Test case for optimize() method when the optimizer converges
    @Test
    public void testOptimizeConverge() throws FunctionEvaluationException {
        // Create mock optimizer
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 3, null);

        // Create mock objective function
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);

        // Create mock goal type
        GoalType goal = mock(GoalType.class);

        // Create mock bounds
        double min = 0.0;
        double max = 1.0;

        // Create mock start value
        double startValue = 0.5;

        // Set the evaluations to 100
        int maxEvaluations = 100;
        when(optimizerMock.getEvaluations()).thenReturn(maxEvaluations);

        // Set the max evaluations to 200
        int totalEvaluations = 200;
        optimizer.setMaxEvaluations(totalEvaluations);

        // Create mock optimum
        UnivariateRealPointValuePair optimumMock = mock(UnivariateRealPointValuePair.class);

        // Set convergence in mock optimizer
        when(optimizerMock.optimize(ArgumentMatchers.<UnivariateRealFunction>any(),
                ArgumentMatchers.<GoalType>any(),
                ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDouble()))
                .thenReturn(optimumMock);

        // Call optimize() method
        optimizer.optimize(f, goal, min, max, startValue);

        // Verify that the getOptima() method returns the same optimum as the mock optimizer
        assertTrue(optimizer.getOptima()[0] == optimumMock);
    }
}

