import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setup() {
        optimizer = new BrentOptimizer(0.0001, 0.0001);
    }

    @Test
    public void testDoOptimize() {
        double min = optimizer.getMin();
        double max = optimizer.getMax();
        double startValue = optimizer.getStartValue();

        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);

        double result = optimizer.doOptimize();

        assertEquals(0.0, result, 0.0001);

        optimizer.setMin(min);
        optimizer.setMax(max);
        optimizer.setStartValue(startValue);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);

        UnivariatePointValuePair result = optimizer.best(a, b, true);

        assertEquals(2.0, result.getPoint(), 0.0001);
        assertEquals(5.0, result.getValue(), 0.0001);

        result = optimizer.best(a, b, false);

        assertEquals(1.0, result.getPoint(), 0.0001);
        assertEquals(10.0, result.getValue(), 0.0001);
    }
}