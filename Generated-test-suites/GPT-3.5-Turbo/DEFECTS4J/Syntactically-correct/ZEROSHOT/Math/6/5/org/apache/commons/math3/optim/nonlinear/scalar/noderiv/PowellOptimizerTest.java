import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class PowellOptimizerTest {
    
    private PowellOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new PowellOptimizer(0.0001, 0.00001);
    }
    
    @After
    public void tearDown() {
        optimizer = null;
    }
    
    @Test
    public void testDoOptimize() {
        double[] startPoint = {1.0, 2.0, 3.0};
        GoalType goal = GoalType.MINIMIZE;
        double[] result = optimizer.doOptimize(startPoint, goal);
        double[] expectedResult = {0.0, 0.0, 0.0};
        assertArrayEquals(expectedResult, result, 0.00001);
    }
    
    @Test
    public void testComputeObjectiveValue() {
        PowellOptimizer optimizer = new PowellOptimizer();
        double[] point = {1.0, 2.0, 3.0};
        UnivariateFunction objFunction = new UnivariateFunction() {
            public double value(double x) {
                return x*x;
            }
        };
        double result = optimizer.computeObjectiveValue(point, objFunction);
        double expected = 14.0;
        assertEquals(expected, result, 0.00001);
    }
    
    @Test
    public void testNewPointAndDirection() {
        PowellOptimizer optimizer = new PowellOptimizer();
        double[] point = {1.0, 2.0, 3.0};
        double[] direction = {0.1, 0.2, 0.3};
        double[][] result = optimizer.newPointAndDirection(point, direction, 0.5);
        double[] expectedPoint = {1.05, 2.1, 3.15};
        double[] expectedDirection = {0.05, 0.1, 0.15};
        assertArrayEquals(expectedPoint, result[0], 0.00001);
        assertArrayEquals(expectedDirection, result[1], 0.00001);
    }
    
    @Test
    public void testLineSearch() {
        PowellOptimizer.LineSearch lineSearch = optimizer.new LineSearch(0.0001, 0.00001);
        double[] start = {1.0, 2.0, 3.0};
        double[] direction = {0.1, 0.2, 0.3};
        UnivariatePointValuePair result = lineSearch.search(start, direction);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(0.0, 0.0);
        assertEquals(expected, result);
    }
}