import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.solvers.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x*x - 4;
            }
        };
        
        double x0 = 1;
        double x1 = 3;
        
        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1);
            assertEquals(2.0, result, 0.0001);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x*x - 4;
            }
        };
        
        double x0 = 1;
        double x1 = 3;
        double absoluteAccuracy = 0.0001;
        
        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
            assertEquals(2.0, result, absoluteAccuracy);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSolveNullFunction() {
        UnivariateRealFunction f = null;
        
        double x0 = 1;
        double x1 = 3;
        
        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1);
            fail("Exception not thrown");
        } catch (Exception e) {
        }
    }
    
    @Test(expected=ConvergenceException.class)
    public void testSolveConvergenceException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        
        double x0 = 1;
        double x1 = 3;
        
        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1);
            fail("Exception not thrown");
        } catch (Exception e) {
        }
    }
    
    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x*x - 4;
            }
        };
        
        double initial = 2.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        int maximumIterations = 100;
        
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
            assertEquals(1.0, result[0], 0.0001);
            assertEquals(3.0, result[1], 0.0001);
        } catch (ConvergenceException e) {
            fail("Exception thrown");
        }
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testBracketNullFunction() {
        UnivariateRealFunction f = null;
        
        double initial = 2.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        int maximumIterations = 100;
        
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
            fail("Exception not thrown");
        } catch (ConvergenceException e) {
        }
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testBracketInvalidBounds() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x*x - 4;
            }
        };
        
        double initial = 2.0;
        double lowerBound = 3.0;
        double upperBound = 1.0;
        int maximumIterations = 100;
        
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
            fail("Exception not thrown");
        } catch (ConvergenceException e) {
        }
    }
    
    @Test(expected=ConvergenceException.class)
    public void testBracketConvergenceException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        
        double initial = 2.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        int maximumIterations = 100;
        
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
            fail("Exception not thrown");
        } catch (ConvergenceException e) {
        }
    }
    
    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 3.0;
        
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(2.0, result, 0.0001);
    }
    
    @Test
    public void testSetup() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        
        try {
            UnivariateRealSolverUtils.setup(f);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSetupNullFunction() {
        UnivariateRealFunction f = null;
        
        try {
            UnivariateRealSolverUtils.setup(f);
            fail("Exception not thrown");
        } catch (Exception e) {
        }
    }
}