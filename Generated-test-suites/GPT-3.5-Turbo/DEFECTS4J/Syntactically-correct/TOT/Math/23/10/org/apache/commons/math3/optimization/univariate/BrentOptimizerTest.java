import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.setMaxIterations(1000);
        optimizer.setConvergenceChecker(null);
    }

    @Test
    public void testDoOptimize() {
        // Typical use case
        double min = optimizer.optimize(1, 10, x -> Math.pow(x-5, 2));

        assertEquals(5.0, min, 1e-6);

        // Edge cases
        min = optimizer.optimize(1, 10, x -> Math.pow(x, 2));
        assertEquals(0.0, min, 1e-6);

        min = optimizer.optimize(1, 10, x -> Math.pow(11-x, 2));
        assertEquals(10.0, min, 1e-6);

        // Error scenarios
        try {
            optimizer.optimize(1, 10, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }

        try {
            optimizer.optimize(10, 1, x -> Math.pow(x, 2));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

}