import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create an instance of NonLinearConjugateGradientOptimizer
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                                                                  new SimpleValueChecker(),
                                                                                                  new BrentSolver(),
                                                                                                  new IdentityPreconditioner());
        // Set the initial step
        optimizer.setInitialStep(1.0);

        // Set the start point
        double[] startPoint = {1.0, 2.0, 3.0};
        optimizer.setStartPoint(startPoint);

        // Set the goal type
        optimizer.setGoalType(GoalType.MINIMIZE);

        // Set the maximum number of evaluations
        optimizer.setMaxEvaluations(1000);

        // Optimize the function
        PointValuePair result = optimizer.doOptimize();

        // Verify the result
        assertEquals(0.0, result.getValue(), 1e-15);
        double[] expectedPoint = {0.0, 0.0, 0.0};
        assertArrayEquals(expectedPoint, result.getPoint(), 1e-15);
    }
}