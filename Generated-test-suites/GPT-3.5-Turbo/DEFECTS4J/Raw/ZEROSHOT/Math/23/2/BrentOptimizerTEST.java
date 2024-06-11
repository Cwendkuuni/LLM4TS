import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setup() {
        optimizer = new BrentOptimizer(0.001, 0.001);
    }

    @Test
    public void testDoOptimize_minimize() {
        double[] values = {1, 2, 3, 4, 5};
        UnivariatePointValuePair expected = new UnivariatePointValuePair(1, -1.0);
        UnivariatePointValuePair actual = optimizer.doOptimize();

        assertEquals(expected.getPoint(), actual.getPoint(), 0.001);
        assertEquals(expected.getValue(), actual.getValue(), 0.001);
    }

    @Test
    public void testDoOptimize_maximize() {
        double[] values = {1, 2, 3, 4, 5};
        UnivariatePointValuePair expected = new UnivariatePointValuePair(5, 1.0);
        UnivariatePointValuePair actual = optimizer.doOptimize();

        assertEquals(expected.getPoint(), actual.getPoint(), 0.001);
        assertEquals(expected.getValue(), actual.getValue(), 0.001);
    }

    @Test
    public void testBest_minimize_aBiggerThanB() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, -1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, -2.0);
        UnivariatePointValuePair expected = b;
        UnivariatePointValuePair actual = optimizer.best(a, b, true);

        assertEquals(expected.getPoint(), actual.getPoint(), 0.001);
        assertEquals(expected.getValue(), actual.getValue(), 0.001);
    }

    @Test
    public void testBest_minimize_bBiggerThanA() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, -2.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, -1.0);
        UnivariatePointValuePair expected = b;
        UnivariatePointValuePair actual = optimizer.best(a, b, true);

        assertEquals(expected.getPoint(), actual.getPoint(), 0.001);
        assertEquals(expected.getValue(), actual.getValue(), 0.001);
    }

    @Test
    public void testBest_maximize_aBiggerThanB() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 2.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 1.0);
        UnivariatePointValuePair expected = a;
        UnivariatePointValuePair actual = optimizer.best(a, b, false);

        assertEquals(expected.getPoint(), actual.getPoint(), 0.001);
        assertEquals(expected.getValue(), actual.getValue(), 0.001);
    }

    @Test
    public void testBest_maximize_bBiggerThanA() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 2.0);
        UnivariatePointValuePair expected = b;
        UnivariatePointValuePair actual = optimizer.best(a, b, false);

        assertEquals(expected.getPoint(), actual.getPoint(), 0.001);
        assertEquals(expected.getValue(), actual.getValue(), 0.001);
    }
}