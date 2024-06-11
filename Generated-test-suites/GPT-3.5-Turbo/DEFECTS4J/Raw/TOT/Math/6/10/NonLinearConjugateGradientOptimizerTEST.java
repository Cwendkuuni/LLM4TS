import org.junit.*;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    private NonLinearConjugateGradientOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    }

    @Test
    public void testDoOptimize_minimumGoalType() {
        double[] startPoint = {1};
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);

        PointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertArrayEquals(startPoint, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test
    public void testDoOptimize_maximumGoalType() {
        double[] startPoint = {1};
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        PointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertArrayEquals(startPoint, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testFindUpperBound_unableToBracketOptimum() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 1;
            }
        };

        optimizer.findUpperBound(f, 0, 0.5);
    }

    @After
    public void tearDown() {
        optimizer = null;
    }

}