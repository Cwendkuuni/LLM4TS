import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.*;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-6, 1e-6);
    }

    @Test
    public void testDoOptimize() {
        optimizer.setMin(0);
        optimizer.setMax(10);

        double[] expected = new double[] {0.5, 0.618033988749895, 0.3551392803206953};
        double[] actual = new double[3];

        for (int i = 0; i < 3; i++) {
            optimizer.setStartValue(expected[i]);
            UnivariatePointValuePair result = optimizer.doOptimize();
            actual[i] = result.getValue();
        }

        assertArrayEquals(expected, actual, 1e-6);
    }
}