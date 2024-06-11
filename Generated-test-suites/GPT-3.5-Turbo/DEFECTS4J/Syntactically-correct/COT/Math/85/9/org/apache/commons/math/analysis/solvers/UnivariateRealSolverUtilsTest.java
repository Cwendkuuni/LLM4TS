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
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(2.0, result, 0.0001);
    }

    @Test(expected=ConvergenceException.class)
    public void testSolveConvergenceException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 1.0 / x;
            }
        };
        double x0 = 0.1;
        double x1 = 0.2;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSolveIllegalArgumentException() {
        UnivariateRealFunction f = null;
        double x0 = 1.0;
        double x1 = 3.0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double x0 = 1.0;
        double x1 = 3.0;
        double absoluteAccuracy = 0.0001;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        assertEquals(2.0, result, 0.0001);
    }

    @Test(expected=ConvergenceException.class)
    public void testSolveWithAccuracyConvergenceException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 1.0 / x;
            }
        };
        double x0 = 0.1;
        double x1 = 0.2;
        double absoluteAccuracy = 0.0001;
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSolveWithAccuracyIllegalArgumentException() {
        UnivariateRealFunction f = null;
        double x0 = 1.0;
        double x1 = 3.0;
        double absoluteAccuracy = 0.0001;
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double initial = 2.5;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        assertEquals(2.0, result[0], 0.0001);
        assertEquals(3.0, result[1], 0.0001);
    }
    
    @Test(expected=ConvergenceException.class)
    public void testBracketConvergenceException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 1.0 / x;
            }
        };
        double initial = 0.1;
        double lowerBound = 0.0;
        double upperBound = 1.0;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testBracketIllegalArgumentException() {
        UnivariateRealFunction f = null;
        double initial = 2.5;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test
    public void testBracketWithMaxIterations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double initial = 2.5;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        int maximumIterations = 100;
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
        assertEquals(2.0, result[0], 0.0001);
        assertEquals(3.0, result[1], 0.0001);
    }
    
    @Test(expected=ConvergenceException.class)
    public void testBracketWithMaxIterationsConvergenceException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 1.0 / x;
            }
        };
        double initial = 0.1;
        double lowerBound = 0.0;
        double upperBound = 1.0;
        int maximumIterations = 100;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testBracketWithMaxIterationsIllegalArgumentException() {
        UnivariateRealFunction f = null;
        double initial = 2.5;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        int maximumIterations = 100;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }

    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 3.0;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(2.0, result, 0.0001);
    }
    
}