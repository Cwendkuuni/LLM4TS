import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PowellOptimizerTest {

    @Test
    public void testConstructor1() {
        PowellOptimizer optimizer = new PowellOptimizer(0.1, 0.01);
        assertEquals(0.1, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(0.01, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test
    public void testConstructor2() {
        PowellOptimizer optimizer = new PowellOptimizer(0.1, 0.01, new CustomConvergenceChecker());
        assertEquals(0.1, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(0.01, optimizer.getAbsoluteThreshold(), 0.0);
    }

    // Add more test cases for other methods in PowellOptimizer class

    // Test for edge cases and exception handling scenarios

    @Test
    public void testEdgeCase1() {
        // Test edge case scenario
    }

    @Test
    public void testEdgeCase2() {
        // Test edge case scenario
    }

    @Test
    public void testExceptionHandling1() {
        // Test exception handling scenario
    }

    @Test
    public void testExceptionHandling2() {
        // Test exception handling scenario
    }

}