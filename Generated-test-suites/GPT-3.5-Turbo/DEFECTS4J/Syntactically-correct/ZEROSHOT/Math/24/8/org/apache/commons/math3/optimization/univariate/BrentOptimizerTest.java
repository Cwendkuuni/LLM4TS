import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);
        optimizer.setMin(-10);
        optimizer.setMax(10);
        optimizer.setMaxEvaluations(1000);
        optimizer.setStartValue(0);
        UnivariatePointValuePair result = optimizer.optimize(100, new SinFunction());
        assertEquals(result.getPoint(), 0, 0.001);
        assertEquals(result.getValue(), 0, 0.001);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 1);
        UnivariatePointValuePair b = new UnivariatePointValuePair(0, 2);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertEquals(result.getPoint(), 0, 0.001);
        assertEquals(result.getValue(), 1, 0.001);
    }

}