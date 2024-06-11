import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    private NonLinearConjugateGradientOptimizer optimizer;

    @Before
    public void setup() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker());
    }

    @Test
    public void testDoOptimize_minimize_goalType() {
        double[] startPoint = {1, 2, 3};
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setConvergenceChecker(new SimpleValueChecker(1e-5, 1e-5));

        PointValuePair result = optimizer.optimize();

        assertNotNull(result);
        assertArrayEquals(startPoint, result.getPoint(), 1e-5);
        assertEquals(0, result.getValue(), 1e-5);
    }

    @Test
    public void testDoOptimize_maximize_goalType() {
        double[] startPoint = {1, 2, 3};
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setConvergenceChecker(new SimpleValueChecker(1e-5, 1e-5));

        PointValuePair result = optimizer.optimize();

        assertNotNull(result);
        assertArrayEquals(startPoint, result.getPoint(), 1e-5);
        assertEquals(0, result.getValue(), 1e-5);
    }

    @Test
    public void testDoOptimize_initialStep_negative() {
        optimizer.setInitialStep(-1);

        try {
            optimizer.optimize();
            fail("MathIllegalStateException not thrown");
        } catch (MathIllegalStateException e) {
            assertEquals(LocalizedFormats.NON_POSITIVE_INITIAL_STEP, e.getSpecifier());
            assertEquals(-1, e.getParts()[0]);
        }
    }

    @Test
    public void testDoOptimize_converged() {
        double[] startPoint = {1, 2, 3};
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setConvergenceChecker(new SimpleValueChecker(1e-5, 1e-5));

        PointValuePair result1 = optimizer.optimize();
        PointValuePair result2 = optimizer.optimize();

        assertNotNull(result1);
        assertNotNull(result2);
        assertArrayEquals(startPoint, result1.getPoint(), 1e-5);
        assertArrayEquals(startPoint, result2.getPoint(), 1e-5);
        assertEquals(0, result1.getValue(), 1e-5);
        assertEquals(0, result2.getValue(), 1e-5);
        assertNotSame(result1, result2);
    }

}