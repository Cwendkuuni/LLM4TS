import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

    // Test constructor
    @Test
    public void testConstructor() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0;
            }

            @Override
            protected double getB(int n, double x) {
                return 0;
            }
        };
        assertNotNull(cf);
    }

    // Test evaluate with default epsilon and maxIterations
    @Test
    public void testEvaluateWithDefaultValues() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1;
            }

            @Override
            protected double getB(int n, double x) {
                return 1;
            }
        };
        double result = cf.evaluate(0);
        assertEquals(1.0, result, 0.0);
    }

    // Test evaluate with specified epsilon and default maxIterations
    @Test
    public void testEvaluateWithSpecifiedEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1;
            }

            @Override
            protected double getB(int n, double x) {
                return 1;
            }
        };
        double result = cf.evaluate(0, 1e-6);
        assertEquals(1.0, result, 0.0);
    }

    // Test evaluate with specified maxIterations and default epsilon
    @Test
    public void testEvaluateWithSpecifiedMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1;
            }

            @Override
            protected double getB(int n, double x) {
                return 1;
            }
        };
        double result = cf.evaluate(0, 100);
        assertEquals(1.0, result, 0.0);
    }

    // Test evaluate with specified epsilon and maxIterations
    @Test
    public void testEvaluateWithSpecifiedEpsilonAndMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1;
            }

            @Override
            protected double getB(int n, double x) {
                return 1;
            }
        };
        double result = cf.evaluate(0, 1e-6, 100);
        assertEquals(1.0, result, 0.0);
    }

    // Test evaluate with convergence failure
    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithConvergenceFailure() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0;
            }

            @Override
            protected double getB(int n, double x) {
                return 0;
            }
        };
        cf.evaluate(0);
    }

    // Test evaluate with non-convergence
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateWithNonConvergence() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1;
            }

            @Override
            protected double getB(int n, double x) {
                return 1;
            }
        };
        cf.evaluate(0, 1e-6, 1);
    }
}