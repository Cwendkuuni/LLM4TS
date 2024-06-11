import org.apache.commons.math3.util.ContinuedFraction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Assert;
import org.junit.Test;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1 / (n + x);
            }

            @Override
            protected double getB(int n, double x) {
                return 1;
            }
        };

        Assert.assertEquals(cf.getA(0, 1), 1.0, 0.0001);
        Assert.assertEquals(cf.getA(1, 1), 0.5, 0.0001);
        Assert.assertEquals(cf.getA(2, 1), 0.3333, 0.0001);
    }

    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1 / (n + x);
            }

            @Override
            protected double getB(int n, double x) {
                return 2 * n;
            }
        };

        Assert.assertEquals(cf.getB(0, 1), 0.0, 0.0001);
        Assert.assertEquals(cf.getB(1, 1), 2.0, 0.0001);
        Assert.assertEquals(cf.getB(2, 1), 4.0, 0.0001);
    }

    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1 / (n + x);
            }

            @Override
            protected double getB(int n, double x) {
                return 1;
            }
        };

        Assert.assertEquals(cf.evaluate(1), 1.0, 0.0001);
        Assert.assertEquals(cf.evaluate(2), 0.5, 0.0001);
        Assert.assertEquals(cf.evaluate(3), 0.3333, 0.0001);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateConvergenceException() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1 / (n + x);
            }
            
            @Override
            protected double getB(int n, double x) {
                return 1;
            }
        };

        // Edge case: x = 0 should result in convergence exception
        cf.evaluate(0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxCountExceededException() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1 / (n + x);
            }

            @Override
            protected double getB(int n, double x) {
                return 1;
            }
        };

        // Edge case: maxIterations = 0 should result in max count exceeded exception
        cf.evaluate(1, 0);
    }

}