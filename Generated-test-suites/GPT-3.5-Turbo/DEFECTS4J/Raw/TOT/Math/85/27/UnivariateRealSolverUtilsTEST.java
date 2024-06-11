import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    private UnivariateRealFunction testFunction;
    private UnivariateRealFunction nullFunction;
    private double[] bracketResults;
    
    @Before
    public void setup() {
        testFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return 2 * x + 1; // y = 2x + 1
            }
        };
        
        nullFunction = null;
        
        bracketResults = new double[2];
    }
    
    @Test
    public void testSolve() {
        try {
            double result = UnivariateRealSolverUtils.solve(testFunction, -5, 5);
            assertEquals(-0.5, result, 0.001);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSolveNullFunction() {
        try {
            double result = UnivariateRealSolverUtils.solve(nullFunction, -5, 5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSolveInvalidBoundaries() {
        try {
            double result = UnivariateRealSolverUtils.solve(testFunction, 5, -5); // invalid boundaries
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSolveConvergenceException() {
        try {
            double result = UnivariateRealSolverUtils.solve(testFunction, -5, -5); // interval has zero width
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // success
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSolveFunctionEvaluationException() {
        UnivariateRealFunction faultyFunction = new UnivariateRealFunction() {
            public double value(double x) {
                throw new RuntimeException();
            }
        };
        
        try {
            double result = UnivariateRealSolverUtils.solve(faultyFunction, -5, 5);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // success
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSolveWithAbsoluteAccuracy() {
        try {
            double result = UnivariateRealSolverUtils.solve(testFunction, -5, 5, 0.1);
            assertEquals(-0.489, result, 0.001);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSolveWithInvalidAbsoluteAccuracy() {
        try {
            double result = UnivariateRealSolverUtils.solve(testFunction, -5, 5, -0.1); // invalid accuracy
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testBracket() {
        try {
            bracketResults = UnivariateRealSolverUtils.bracket(testFunction, 0, -1, 1);
            assertEquals(-1, bracketResults[0], 0.001);
            assertEquals(1, bracketResults[1], 0.001);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testBracketNullFunction() {
        try {
            bracketResults = UnivariateRealSolverUtils.bracket(nullFunction, 0, -1, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testBracketInvalidInitial() {
        try {
            bracketResults = UnivariateRealSolverUtils.bracket(testFunction, -2, -1, 1); // invalid initial
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testBracketInvalidBoundaries() {
        try {
            bracketResults = UnivariateRealSolverUtils.bracket(testFunction, 0, 1, -1); // invalid boundaries
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testBracketConvergenceException() {
        try {
            bracketResults = UnivariateRealSolverUtils.bracket(testFunction, 0, -5, 5); // maximumIterations reached
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // success
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testBracketFunctionEvaluationException() {
        UnivariateRealFunction faultyFunction = new UnivariateRealFunction() {
            public double value(double x) {
                throw new RuntimeException();
            }
        };
        
        try {
            bracketResults = UnivariateRealSolverUtils.bracket(faultyFunction, 0, -1, 1);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // success
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(-5, 5);
        assertEquals(0, result, 0.001);
        
        result = UnivariateRealSolverUtils.midpoint(-1, 3);
        assertEquals(1, result, 0.001);
    }

}