import static org.junit.Assert.*;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    
    @Test
    public void testSolve() {
        // Typical use case
        double result1 = UnivariateRealSolverUtils.solve(new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5);
        assertEquals(2, result1, 0.0001);
        
        // Edge case: Interval is at the boundary values
        double result2 = UnivariateRealSolverUtils.solve(new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, -5, 5);
        assertEquals(2, result2, 0.0001);
        
        // Error scenario: Function is null
        try {
            double result3 = UnivariateRealSolverUtils.solve(null, 0, 5);
            fail("No exception thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("function is null", e.getMessage());
        }
    }
    
    @Test
    public void testSolveWithAccuracy() {
        // Typical use case
        double result1 = UnivariateRealSolverUtils.solve(new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5, 0.001);
        assertEquals(2, result1, 0.001);
        
        // Edge case: Interval is at the boundary values
        double result2 = UnivariateRealSolverUtils.solve(new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, -5, 5, 0.001);
        assertEquals(2, result2, 0.001);
        
        // Error scenario: Function is null
        try {
            double result3 = UnivariateRealSolverUtils.solve(null, 0, 5, 0.001);
            fail("No exception thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("function is null", e.getMessage());
        }
        
        // Error scenario: Invalid interval
        try {
            double result4 = UnivariateRealSolverUtils.solve(new UnivariateRealFunction() {
                public double value(double x) {
                    return x * x - 4;
                }
            }, 5, 0, 0.001);
            fail("No exception thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("invalid bracketing parameters:  lower bound=5.0,  initial=0.0, upper bound=0.0", e.getMessage());
        }
        
        // Error scenario: Invalid absolute accuracy
        try {
            double result5 = UnivariateRealSolverUtils.solve(new UnivariateRealFunction() {
                public double value(double x) {
                    return x * x - 4;
                }
            }, 0, 5, -0.001);
            fail("No exception thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("bad value for absolute accuracy: -0.001", e.getMessage());
        }
    }
    
    @Test
    public void testBracket() {
        // Typical use case
        double[] result1 = UnivariateRealSolverUtils.bracket(new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 3, 2, 5);
        assertEquals(2, result1[0], 0.0001);
        assertEquals(4, result1[1], 0.0001);
        
        // Edge case: Interval is at the boundary values
        double[] result2 = UnivariateRealSolverUtils.bracket(new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, -5, -5, 5);
        assertEquals(-4, result2[0], 0.0001);
        assertEquals(-3, result2[1], 0.0001);
        
        // Error scenario: Function is null
        try {
            double[] result3 = UnivariateRealSolverUtils.bracket(null, 3, 2, 5);
            fail("No exception thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("function is null", e.getMessage());
        }
        
        // Error scenario: Invalid interval
        try {
            double[] result4 = UnivariateRealSolverUtils.bracket(new UnivariateRealFunction() {
                public double value(double x) {
                    return x * x - 4;
                }
            }, 3, 5, 2);
            fail("No exception thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("invalid bracketing parameters:  lower bound=5.0,  initial=3.0, upper bound=2.0", e.getMessage());
        }
        
        // Error scenario: Maximum iterations is non-positive
        try {
            double[] result5 = UnivariateRealSolverUtils.bracket(new UnivariateRealFunction() {
                public double value(double x) {
                    return x * x - 4;
                }
            }, 3, 2, 5, 0);
            fail("No exception thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("bad value for maximum iterations number: 0", e.getMessage());
        }
        
        // Error scenario: Maximum iterations are exceeded
        try {
            double[] result6 = UnivariateRealSolverUtils.bracket(new UnivariateRealFunction() {
                public double value(double x) {
                    return x * x - 4;
                }
            }, 3, 2, 5, 3);
            fail("No exception thrown");
        } catch (ConvergenceException e) {
            assertEquals("number of iterations=3, maximum iterations=3, initial=3.0, lower bound=2.0, upper bound=5.0, final a value=1.0, final b value=6.0, f(a)=-3.0, f(b)=20.0", e.getMessage());
        }
    }
    
    @Test
    public void testMidpoint() {
        double result1 = UnivariateRealSolverUtils.midpoint(2, 5);
        assertEquals(3.5, result1, 0.0001);
        
        double result2 = UnivariateRealSolverUtils.midpoint(-4, 4);
        assertEquals(0, result2, 0.0001);
    }
}