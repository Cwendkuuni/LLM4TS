import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        double result = optimizer.doOptimize();

        assertEquals(expectedResult, result, 1e-6);
    }
}