import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    NonLinearConjugateGradientOptimizer optimizer;
    double[] startPoint;
    GoalType goalType;
    int maxEvaluations;

    @Before
    public void setUp() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        startPoint = new double[]{1.0, 2.0, 3.0};
        goalType = GoalType.MINIMIZE;
        maxEvaluations = 100;
    }

    @Test
    public void testDoOptimize() {
        PointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
        double[] point = result.getPoint();
        assertNotNull(point);
        assertEquals(3, point.length);
    }

    @Test
    public void testSetInitialStep() {
        double initialStep = 0.5;
        optimizer.setInitialStep(initialStep);
        assertEquals(initialStep, optimizer.getInitialStep(), 1e-15);
    }
}