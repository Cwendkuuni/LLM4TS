import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    @Test
    public void testOptimize_StartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        double result = optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) { return x * x; }
        }, GoalType.MINIMIZE, -5, 5, 0);

        assertEquals(0.0, result, 1e-10);
    }

    @Test
    public void testOptimize_NoStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        double result = optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) { return x * x; }
        }, GoalType.MINIMIZE, -5, 5);

        assertEquals(0.0, result, 1e-10);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        try {
            double result = optimizer.doOptimize();
            fail("UnsupportedOperationException should have been thrown");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testLocalMin_InvalidTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        try {
            optimizer.localMin(true, new UnivariateRealFunction() {
                public double value(double x) { return x * x; }
            }, GoalType.MINIMIZE, -5, 0, 5, -1, 0);
            fail("NotStrictlyPositiveException should have been thrown");
        } catch (NotStrictlyPositiveException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testLocalMin_InvalidAbsoluteAccuracy() {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        try {
            optimizer.localMin(true, new UnivariateRealFunction() {
                public double value(double x) { return x * x; }
            }, GoalType.MINIMIZE, -5, 0, 5, 1, -1);
            fail("NotStrictlyPositiveException should have been thrown");
        } catch (NotStrictlyPositiveException e) {
            assertNotNull(e);
        }
    }
}