import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.ContinuedFraction;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.util.LocalizedFormats;

import static org.junit.Assert.*;

public class ContinuedFractionTest {

    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 2.0 * n;
            }

            protected double getB(int n, double x) {
                return n + 1.0;
            }
        };

        double result = cf.evaluate(1.0);
        assertEquals(0.5, result, 0.0001);
    }

    @Test
    public void testEvaluate_withEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 2.0 * n;
            }

            protected double getB(int n, double x) {
                return n + 1.0;
            }
        };

        double result = cf.evaluate(1.0, 0.0001);
        assertEquals(0.5, result, 0.0001);
    }

    @Test
    public void testEvaluate_withMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 2.0 * n;
            }

            protected double getB(int n, double x) {
                return n + 1.0;
            }
        };

        double result = cf.evaluate(1.0, Integer.MAX_VALUE);
        assertEquals(0.5, result, 0.0001);
    }

    @Test
    public void testEvaluate_withEpsilonAndMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 2.0 * n;
            }

            protected double getB(int n, double x) {
                return n + 1.0;
            }
        };

        double result = cf.evaluate(1.0, 0.0001, Integer.MAX_VALUE);
        assertEquals(0.5, result, 0.0001);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluate_divergence() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        cf.evaluate(1.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluate_withEpsilon_divergence() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        cf.evaluate(1.0, 0.0001);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluate_withMaxIterations_nonConvergent() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        cf.evaluate(1.0, 100);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluate_divergence_infinite() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return Double.POSITIVE_INFINITY;
            }

            protected double getB(int n, double x) {
                return Double.NEGATIVE_INFINITY;
            }
        };

        cf.evaluate(1.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluate_withEpsilon_divergence_infinite() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return Double.POSITIVE_INFINITY;
            }

            protected double getB(int n, double x) {
                return Double.NEGATIVE_INFINITY;
            }
        };

        cf.evaluate(1.0, 0.0001);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluate_divergence_nan() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return Double.NaN;
            }

            protected double getB(int n, double x) {
                return Double.NaN;
            }
        };

        cf.evaluate(1.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluate_withEpsilon_divergence_nan() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return Double.NaN;
            }

            protected double getB(int n, double x) {
                return Double.NaN;
            }
        };

        cf.evaluate(1.0, 0.0001);
    }
}