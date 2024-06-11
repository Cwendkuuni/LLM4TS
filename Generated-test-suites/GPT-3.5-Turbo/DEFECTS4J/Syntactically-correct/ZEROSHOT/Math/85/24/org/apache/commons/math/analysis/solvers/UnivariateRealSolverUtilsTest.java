import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        
        double x0 = 1.0;
        double x1 = 3.0;
        
        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1);
            assertEquals(result, 2.0, 0.000001);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };
        
        double x0 = 1.0;
        double x1 = 3.0;
        double absoluteAccuracy = 0.000001;
        
        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
            assertEquals(result, 1.570796, 0.000001);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x - 2, 3) - 1;
            }
        };
        
        double initial = 1.0;
        double lowerBound = -5.0;
        double upperBound = 5.0;
        
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            assertEquals(result[0], 0.0, 0.000001);
            assertEquals(result[1], 2.0, 0.000001);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testBracketWithIterations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        
        double initial = 1.0;
        double lowerBound = -5.0;
        double upperBound = 5.0;
        int maximumIterations = 10;
        
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
            assertEquals(result[0], 0.0, 0.000001);
            assertEquals(result[1], 2.0, 0.000001);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testMidpoint() {
        double a = 0.0;
        double b = 2.0;
        
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(result, 1.0, 0.000001);
    }
}