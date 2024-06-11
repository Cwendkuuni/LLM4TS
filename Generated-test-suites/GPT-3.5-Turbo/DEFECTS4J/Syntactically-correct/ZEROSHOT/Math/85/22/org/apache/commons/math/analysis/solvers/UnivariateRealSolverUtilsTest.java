import static org.junit.Assert.*;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    
    @Test
    public void solveTest1() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double x0 = 0;
        double x1 = 4;
        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1);
            assertEquals(2, result, 0);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }
    
    @Test
    public void solveTest2() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double x0 = 0;
        double x1 = 4;
        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1, 0.01);
            assertEquals(2, result, 0.01);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }
    
    @Test
    public void bracketTest1() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double initial = 1;
        double lowerBound = 0;
        double upperBound = 4;
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            assertEquals(0, result[0], 0);
            assertEquals(4, result[1], 0);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }
    
    @Test
    public void bracketTest2() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double initial = 1;
        double lowerBound = 0;
        double upperBound = 4;
        int maximumIterations = 100;
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
            assertEquals(0, result[0], 0);
            assertEquals(4, result[1], 0);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }
    
    @Test
    public void midpointTest() {
        double a = 1;
        double b = 3;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(2, result, 0);
    }
    
}