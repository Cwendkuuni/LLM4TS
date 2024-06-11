import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    private NonLinearConjugateGradientOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    }

    @Test
    public void testSetInitialStep() {
        optimizer.setInitialStep(0.5);
        assertEquals(0.5, optimizer.getInitialStep(), 0.0);
    }

    @Test
    public void testDoOptimize() {
        double[] startPoint = {0.0, 0.0};
        optimizer.setStartPoint(startPoint);
        optimizer.setMaxEvaluations(1000);
        optimizer.optimize();

        PointValuePair optimalSolution = optimizer.getOptimalSolution();
        assertNotNull(optimalSolution);

        double[] point = optimalSolution.getPoint();
        assertNotNull(point);

        assertEquals(2, point.length);
        assertEquals(-1.0, point[0], 0.0);
        assertEquals(1.0, point[1], 0.0);
    }

}