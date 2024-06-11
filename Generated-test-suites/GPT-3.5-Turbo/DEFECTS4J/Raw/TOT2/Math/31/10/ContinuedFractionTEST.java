import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ContinuedFractionTest {

    @Test
    public void testDefaultConstructor() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return n;
            }
        };
        // Test default constructor
        assertEquals(1.0, cf.evaluate(1.0), 0.001);
    }

    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0 / n;
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1.0;
            }
        };
        // Test getA method
        assertEquals(0.5, cf.getA(2, 1.0), 0.001);
    }

    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0 / n;
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1.0;
            }
        };
        // Test getB method
        assertEquals(3.0, cf.getB(2, 1.0), 0.001);
    }

    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0 / n;
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1.0;
            }
        };
        // Test evaluate method
        assertEquals(0.5, cf.evaluate(1.0), 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testInfiniteDivergence() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 0.0;
            }
        };
        // Test infinite divergence scenario
        cf.evaluate(1.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testNonConvergentContinuedFraction() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return n;
            }
        };
        // Test non-convergent continued fraction scenario
        cf.evaluate(1.0, 0.001, 1);
    }

}