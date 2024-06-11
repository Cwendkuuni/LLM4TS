import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-10, 1e-10);
    }

    @Test
    public void testConstructorWithInvalidRelativeThreshold() {
        assertThrows(NumberIsTooSmallException.class, () -> {
            optimizer = new BrentOptimizer(1e-20, 1e-10);
        });
    }

    @Test
    public void testConstructorWithInvalidAbsoluteThreshold() {
        assertThrows(NotStrictlyPositiveException.class, () -> {
            optimizer = new BrentOptimizer(1e-10, -1e-10);
        });
    }

    @Test
    public void testOptimize() {
        double result = optimizer.optimize(1000, x -> x * x, GoalType.MINIMIZE, 0, 10).getPoint();
        assertEquals(0, result, 1e-10); // Typical use case
    }

    @Test
    public void testOptimizeWithInvalidFunction() {
        assertThrows(IllegalArgumentException.class, () -> {
            optimizer.optimize(1000, null, GoalType.MINIMIZE, 0, 10);
        });
    }

}