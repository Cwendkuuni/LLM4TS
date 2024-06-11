import org.apache.commons.math3.util.ContinuedFraction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContinuedFractionTest {

    /*
    Method: getA(int n, double x)
    Test case:
    When n is negative and x is a negative value
    */
    @Test
    public void testGetANegativeNAndNegativeX() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n * -1;   // arbitrary return value
            }

            @Override
            protected double getB(int n, double x) {
                return 0;   // arbitrary return value
            }
        };

        double result = cf.getA(-1, -10);
        assertEquals(-1, result, 0.0001);
    }

    /*
    Method: getB(int n, double x)
    Test case:
    When n is zero and x is positive
    */
    @Test
    public void testGetBZeroNAndPositiveX() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0;   // arbitrary return value
            }

            @Override
            protected double getB(int n, double x) {
                return x;   // arbitrary return value
            }
        };

        double result = cf.getB(0, 10);
        assertEquals(10, result, 0.0001);
    }

    /*
    Method: evaluate(double x)
    Test case:
    When x is zero
    */
    @Test
    public void testEvaluateZeroX() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1;   // arbitrary return value
            }

            @Override
            protected double getB(int n, double x) {
                return 1;   // arbitrary return value
            }
        };

        double result = cf.evaluate(0);
        assertEquals(1, result, 0.0001);
    }

    /*
    Method: evaluate(double x, double epsilon)
    Test case:
    When epsilon is a small positive value
    */
    @Test
    public void testEvaluateSmallEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1;   // arbitrary return value
            }

            @Override
            protected double getB(int n, double x) {
                return 1;   // arbitrary return value
            }
        };

        double result = cf.evaluate(10, 0.0001);
        assertEquals(1, result, 0.0001);
    }

    /*
    Method: evaluate(double x, int maxIterations)
    Test case:
    When maxIterations is a negative value
    */
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateNegativeMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1;   // arbitrary return value
            }

            @Override
            protected double getB(int n, double x) {
                return 1;   // arbitrary return value
            }
        };

        cf.evaluate(10, -1);
    }

    /*
    Method: evaluate(double x, double epsilon, int maxIterations)
    Test case:
    When x is a large positive value and epsilon is a small positive value and maxIterations is a large value
    */
    @Test
    public void testEvaluateLargeValues() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1;   // arbitrary return value
            }

            @Override
            protected double getB(int n, double x) {
                return 1;   // arbitrary return value
            }
        };

        double result = cf.evaluate(1000000, 0.0001, 1000000);
        assertEquals(1, result, 0.0001);
    }

    /*
    Method: evaluate(double x, double epsilon, int maxIterations)
    Test case:
    When the algorithm fails to converge
    */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateAlgorithmFailsToConverge() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1;   // arbitrary return value
            }

            @Override
            protected double getB(int n, double x) {
                return 0;   // arbitrary return value
            }
        };

        cf.evaluate(2, 0.0001, 100);
    }

}