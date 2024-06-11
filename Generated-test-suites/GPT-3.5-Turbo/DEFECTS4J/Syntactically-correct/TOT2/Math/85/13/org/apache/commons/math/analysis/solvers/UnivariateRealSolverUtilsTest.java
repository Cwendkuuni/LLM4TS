import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveUsingDefaultSolver() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5;
            }
        };

        double result = UnivariateRealSolverUtils.solve(f, 0, 10);

        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void testSolveUsingDefaultSolverWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5;
            }
        };

        double result = UnivariateRealSolverUtils.solve(f, 3, 7, 0.0001);

        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * (x - 5);
            }
        };

        double[] bracket = UnivariateRealSolverUtils.bracket(f, 2, 1, 10);

        assertTrue(f.value(bracket[0]) * f.value(bracket[1]) <= 0);
    }

    @Test
    public void testMidpoint() {
        double midpoint = UnivariateRealSolverUtils.midpoint(0, 10);

        assertEquals(5.0, midpoint, 0.0001);
    }

    @Test
    public void testSolveWithNullFunction() {
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.solve(null, 0, 10);
        });
    }
    
    @Test
    public void testBracketWithInvalidBounds() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * (x - 5);
            }
        };

        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.bracket(f, 2, 10, 1);
        });
    }

    @Test
    public void testBracketMaxIterations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * (x - 5);
            }
        };

        assertThrows(ConvergenceException.class, () -> {
            UnivariateRealSolverUtils.bracket(f, 2, 1, 10, 1);
        });
    }

}