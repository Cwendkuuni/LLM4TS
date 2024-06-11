import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class BisectionSolverTest {

    private static final double EPSILON = 1e-6;
    
    private BisectionSolver solver;
    
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    
    @Before
    public void setUp() {
        solver = new BisectionSolver();
    }
    
    @Test
    public void testSolve_withValidFunction_validSolution() throws Exception {
        // Arrange
        double min = 0;
        double max = 10;
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2) - 25;
            }
        };
        
        // Act
        double result = solver.solve(f, min, max);
        
        // Assert
        assertEquals(5, result, EPSILON);
    }
    
    @Test
    public void testSolve_withInvalidFunction_ExceptionThrown() throws Exception {
        // Arrange
        double min = 0;
        double max = 10;
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        
        // Assert
        exception.expect(FunctionEvaluationException.class);
        
        // Act
        solver.solve(f, min, max);
    }
    
    @Test
    public void testSolve_withInvalidInterval_ExceptionThrown() throws Exception {
        // Arrange
        double min = 10;
        double max = 0;
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2) - 25;
            }
        };
        
        // Assert
        exception.expect(IllegalArgumentException.class);
        
        // Act
        solver.solve(f, min, max);
    }
    
    @Test
    public void testSolve_withMaxIterationsExceeded_ExceptionThrown() throws Exception {
        // Arrange
        double min = -10;
        double max = 10;
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2);
            }
        };
        
        // Assert
        exception.expect(MaxIterationsExceededException.class);
        
        // Act
        solver.solve(f, min, max);
    }
}