import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    // Test Constructor 1
    @Test
    public void testConstructor1() {
        double rel = 0.01;
        double abs = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        assertNotNull(optimizer);
    }

    // Test Constructor 2
    @Test
    public void testConstructor2() {
        double rel = 0.01;
        double abs = 0.001;
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<>();
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        assertNotNull(optimizer);
    }

    // Test doOptimize()
    @Test
    public void testDoOptimize() {
        double rel = 0.01;
        double abs = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Set up optimizer parameters
        optimizer.setMin(0.0);
        optimizer.setMax(1.0);
        optimizer.setStartValue(0.5);

        // Test optimization with a simple function
        optimizer.setObjectiveFunction(x -> x * x);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0.0, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);

        // Test optimization with a different objective function
        optimizer.setObjectiveFunction(x -> x * x - 1);
        result = optimizer.doOptimize();
        assertEquals(1.0, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    // Test best()
    @Test
    public void testBest() {
        double rel = 0.01;
        double abs = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Test when a is null
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertEquals(b, result);

        // Test when b is null
        a = new UnivariatePointValuePair(0.0, 0.0);
        b = null;
        result = optimizer.best(a, b, true);
        assertEquals(a, result);

        // Test when values are equal and minimization is true
        a = new UnivariatePointValuePair(0.0, 0.0);
        b = new UnivariatePointValuePair(1.0, 0.0);
        result = optimizer.best(a, b, true);
        assertEquals(a, result);

        // Test when values are equal and minimization is false
        a = new UnivariatePointValuePair(0.0, 0.0);
        b = new UnivariatePointValuePair(1.0, 0.0);
        result = optimizer.best(a, b, false);
        assertEquals(b, result);

        // Test when a has a smaller value and minimization is true
        a = new UnivariatePointValuePair(0.0, 0.0);
        b = new UnivariatePointValuePair(1.0, 1.0);
        result = optimizer.best(a, b, true);
        assertEquals(a, result);

        // Test when a has a smaller value and minimization is false
        a = new UnivariatePointValuePair(0.0, 0.0);
        b = new UnivariatePointValuePair(1.0, 1.0);
        result = optimizer.best(a, b, false);
        assertEquals(b, result);

        // Test when b has a smaller value and minimization is true
        a = new UnivariatePointValuePair(1.0, 1.0);
        b = new UnivariatePointValuePair(0.0, 0.0);
        result = optimizer.best(a, b, true);
        assertEquals(b, result);

        // Test when b has a smaller value and minimization is false
        a = new UnivariatePointValuePair(1.0, 1.0);
        b = new UnivariatePointValuePair(0.0, 0.0);
        result = optimizer.best(a, b, false);
        assertEquals(a, result);
    }

}

