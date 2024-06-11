import static org.junit.Assert.*;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
    }

    @Test
    public void testSolveWithInitial() {
        // Typical use case
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(Math::cos, 0.5, 1.2, 1.0);
        assertEquals(1.0471975511965976, result, 1e-10);

        // Edge cases
        result = solver.solve(Math::cos, 0.0, 1.0, 0.0);
        assertEquals(0.7390851332151607, result, 1e-10);

        // Error scenarios
        try {
            solver.solve(Math::cos, 0.0, 1.0, 2.0);
            fail("Exception not thrown for invalid initial value");
        } catch (FunctionEvaluationException e) {
            assertTrue(true);
        } catch (MaxIterationsExceededException e) {
            fail("Incorrect exception thrown");
        }
    }

    @Test
    public void testSolveWithoutInitial() {
        // Typical use case
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(Math::sin, 0.5, 1.2);
        assertEquals(1.0471975511965976, result, 1e-10);

        // Edge cases
        result = solver.solve(Math::sin, 0.0, 1.0);
        assertEquals(0.8414709848078965, result, 1e-10);

        // Error scenarios
        try {
            solver.solve(Math::sin, 0.0, 1.0);
            fail("Exception not thrown for exceeding maximum iterations");
        } catch (MaxIterationsExceededException e) {
            assertTrue(true);
        } catch (FunctionEvaluationException e) {
            fail("Incorrect exception thrown");
        }
    }

}