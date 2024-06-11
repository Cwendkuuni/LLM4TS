import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDefaultConstructor() {
        // Arrange
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        
        // Act
        double result = solver.solve(100, Math::cos, 0, Math.PI);
        
        // Assert
        assertEquals(Math.PI / 2, result, 1e-6);
    }

    @Test
    public void testMaximalOrderConstructor() {
        // Arrange
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-10, 3);
        
        // Act
        double result = solver.solve(100, Math::cos, 0, Math.PI);
        
        // Assert
        assertEquals(Math.PI / 2, result, 1e-10);
    }

    @Test
    public void testSolveWithMaxEval() {
        // Arrange
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        
        // Act
        double result = solver.solve(10, Math::cos, 0, Math.PI);
        
        // Assert
        assertEquals(Double.NaN, result, 1e-6);
    }

    @Test
    public void testSolveWithStartValue() {
        // Arrange
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        
        // Act
        double result = solver.solve(100, Math::cos, 0, Math.PI, Math.PI / 2);
        
        // Assert
        assertEquals(Math.PI / 2, result, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        // Arrange
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        
        // Act
        double result = solver.solve(100, Math::cos, 0, Math.PI, AllowedSolution.RIGHT_SIDE);
        
        // Assert
        assertTrue(result >= Math.PI);
    }

    @Test
    public void testSolveWithInvalidMaximalOrder() {
        try {
            BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 1);
            fail("Should have thrown NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Pass
        }
    }

}