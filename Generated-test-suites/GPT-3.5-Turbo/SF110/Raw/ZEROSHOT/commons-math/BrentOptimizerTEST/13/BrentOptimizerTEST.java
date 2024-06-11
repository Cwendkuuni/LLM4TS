import org.junit.*;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRel() {
        new BrentOptimizer(0.0, 1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbs() {
        new BrentOptimizer(0.1, 0.0);
    }

    @Test
    public void testDoOptimizeMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0);

        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
        assertEquals(0.5, result.getPoint(), 0.001);
    }

    @Test
    public void testDoOptimizeMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, new SimpleValueChecker());

        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
        assertEquals(0.5, result.getPoint(), 0.001);
    }

    @Test
    public void testBestWithNull() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0);
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = new UnivariatePointValuePair(0.5, 0.1);

        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertNotNull(result);
        assertEquals(b.getPoint(), result.getPoint(), 0.001);
        assertEquals(b.getValue(), result.getValue(), 0.001);
    }

    @Test
    public void testBestWithNull2() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.5, 0.1);
        UnivariatePointValuePair b = null;

        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertNotNull(result);
        assertEquals(a.getPoint(), result.getPoint(), 0.001);
        assertEquals(a.getValue(), result.getValue(), 0.001);
    }

    @Test
    public void testBestMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.5, 0.1);
        UnivariatePointValuePair b = new UnivariatePointValuePair(0.7, 0.3);

        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertNotNull(result);
        assertEquals(a.getPoint(), result.getPoint(), 0.001);
        assertEquals(a.getValue(), result.getValue(), 0.001);
    }

    @Test
    public void testBestMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.5, 0.1);
        UnivariatePointValuePair b = new UnivariatePointValuePair(0.7, 0.3);

        UnivariatePointValuePair result = optimizer.best(a, b, false);
        assertNotNull(result);
        assertEquals(b.getPoint(), result.getPoint(), 0.001);
        assertEquals(b.getValue(), result.getValue(), 0.001);
    }
}