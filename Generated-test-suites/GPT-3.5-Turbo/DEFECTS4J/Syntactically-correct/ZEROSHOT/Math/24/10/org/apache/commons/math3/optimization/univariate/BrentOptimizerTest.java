import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);

        assertEquals(0, optimizer.computeObjectiveValue(0), 0.0001);
        assertEquals(10, optimizer.computeObjectiveValue(10), 0.0001);
        assertEquals(5, optimizer.computeObjectiveValue(5), 0.0001);

        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0, result.getPoint(), 0.0001);
        assertEquals(0, result.getValue(), 0.0001);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 5);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 3);
        boolean isMinim = true;
        
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        assertEquals(b.getPoint(), result.getPoint(), 0.0001);
        assertEquals(b.getValue(), result.getValue(), 0.0001);
    }
}