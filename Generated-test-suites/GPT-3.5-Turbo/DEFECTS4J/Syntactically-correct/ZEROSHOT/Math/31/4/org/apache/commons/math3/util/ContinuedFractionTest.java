import org.junit.Test;
import org.junit.Assert;

public class ContinuedFractionTest {

    @Test
    public void testEvaluate_defaultEpsilon_maxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        double result = cf.evaluate(1.0);
        Assert.assertEquals(1.0, result, 0.000001);
    }

    @Test
    public void testEvaluate_customEpsilon_maxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 2.0;
            }
            protected double getB(int n, double x) {
                return 3.0;
            }
        };

        double result = cf.evaluate(2.0, 0.0001);
        Assert.assertEquals(1.4, result, 0.000001);
    }

    @Test
    public void testEvaluate_defaultEpsilon_customMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 3.0;
            }
            protected double getB(int n, double x) {
                return 4.0;
            }
        };

        double result = cf.evaluate(3.0, 100);
        Assert.assertEquals(1.7, result, 0.000001);
    }

    @Test
    public void testEvaluate_customEpsilon_customMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 4.0;
            }
            protected double getB(int n, double x) {
                return 5.0;
            }
        };

        double result = cf.evaluate(4.0, 0.001, 50);
        Assert.assertEquals(2.0, result, 0.000001);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluate_nonConvergent() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 0.0;
            }
            protected double getB(int n, double x) {
                return 0.0;
            }
        };

        double result = cf.evaluate(5.0);
        Assert.fail("Expected ConvergenceException, but evaluation did not fail");
    }
}