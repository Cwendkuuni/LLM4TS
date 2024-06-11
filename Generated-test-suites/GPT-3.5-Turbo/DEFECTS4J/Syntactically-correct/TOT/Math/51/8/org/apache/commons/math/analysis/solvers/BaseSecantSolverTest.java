import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    private static final double ABSOLUTE_ACCURACY = 1e-6;
    private static final double RELATIVE_ACCURACY = 1e-6;
    private static final double FUNCTION_VALUE_ACCURACY = 1e-15;
    private static final double MAX_VALUE = 1000.0;
    private static final double MIN_VALUE = -1000.0;

    private BaseSecantSolver solver;

    @Before
    public void setup() {
        solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, RELATIVE_ACCURACY, FUNCTION_VALUE_ACCURACY,
                BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve_WhenRootIsPresentWithinDefaultAbsoluteAccuracy_ReturnsRoot() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double expectedRoot = 5.0;

        // Act
        double actualRoot = solver.solve(100, f, MIN_VALUE, MAX_VALUE);

        // Assert
        assertEquals(expectedRoot, actualRoot, ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_WhenRootIsPresentWithinAbsoluteAccuracy_ReturnsRoot() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double expectedRoot = 5.0;

        // Act
        double actualRoot = solver.solve(100, f, MIN_VALUE, MAX_VALUE, ABSOLUTE_ACCURACY);

        // Assert
        assertEquals(expectedRoot, actualRoot, ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_WhenRootIsPresentWithinRelativeAccuracy_ReturnsRoot() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double expectedRoot = 5.0;

        // Act
        double actualRoot = solver.solve(100, f, MIN_VALUE, MAX_VALUE, RELATIVE_ACCURACY);

        // Assert
        assertEquals(expectedRoot, actualRoot, RELATIVE_ACCURACY * expectedRoot);
    }

    @Test
    public void testSolve_WhenRootIsNotPresent_ReturnsClosestEstimatedRoot() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double expectedRoot = 5.0;

        // Act
        double actualRoot = solver.solve(100, f, MIN_VALUE, 4.9, 0.0);

        // Assert
        assertEquals(expectedRoot, actualRoot, ABSOLUTE_ACCURACY);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testSolve_WhenRootIsNotPresentWithinMaxIterations_ThrowsMathIllegalStateException() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };

        // Act
        solver.solve(5, f, MIN_VALUE, 4.9);
    }

    @Test
    public void testSolve_WhenRootIsOnTheLeftSideAndAllowedSideIsLeft_ReturnsRoot() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double expectedRoot = 5.0;
        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.LEFT_SIDE);

        // Act
        double actualRoot = solver.solve(100, f, MIN_VALUE, 4.9);

        // Assert
        assertEquals(expectedRoot, actualRoot, ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_WhenRootIsOnTheLeftSideAndAllowedSideIsRight_ThrowsMathIllegalStateException() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        // Act
        try {
            solver.solve(100, f, MIN_VALUE, 4.9);
        } catch (MathIllegalArgumentException ex) {
            // Assert
            assertEquals("Initial guess is not in bracketing interval", ex.getMessage());
            throw ex;
        }
    }

    @Test
    public void testSolve_WhenRootIsOnTheRightSideAndAllowedSideIsRight_ReturnsRoot() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double expectedRoot = 5.0;
        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        // Act
        double actualRoot = solver.solve(100, f, 5.1, MAX_VALUE);

        // Assert
        assertEquals(expectedRoot, actualRoot, ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_WhenRootIsOnTheRightSideAndAllowedSideIsLeft_ThrowsMathIllegalStateException() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.LEFT_SIDE);

        // Act
        try {
            solver.solve(100, f, 5.1, MAX_VALUE);
        } catch (MathIllegalArgumentException ex) {
            // Assert
            assertEquals("Initial guess is not in bracketing interval", ex.getMessage());
            throw ex;
        }
    }

    @Test
    public void testSolve_WhenRootIsBelow_Min_ReturnsRoot() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double expectedRoot = 5.0;
        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.BELOW_SIDE);

        // Act
        double actualRoot = solver.solve(100, f, Double.NEGATIVE_INFINITY, 4.9);

        // Assert
        assertEquals(expectedRoot, actualRoot, ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_WhenRootIsAbove_Min_ThrowsMathIllegalStateException() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.BELOW_SIDE);

        // Act
        try {
            solver.solve(100, f, Double.NEGATIVE_INFINITY, 5.1);
        } catch (MathIllegalArgumentException ex) {
            // Assert
            assertEquals("Initial guess is not in bracketing interval", ex.getMessage());
            throw ex;
        }
    }

    @Test
    public void testSolve_WhenRootIsAbove_Max_ReturnsRoot() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double expectedRoot = 5.0;
        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.ABOVE_SIDE);

        // Act
        double actualRoot = solver.solve(100, f, 5.1, Double.POSITIVE_INFINITY);

        // Assert
        assertEquals(expectedRoot, actualRoot, ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_WhenRootIsBelow_Max_ThrowsMathIllegalStateException() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.ABOVE_SIDE);

        // Act
        try {
            solver.solve(100, f, 4.9, Double.POSITIVE_INFINITY);
        } catch (MathIllegalArgumentException ex) {
            // Assert
            assertEquals("Initial guess is not in bracketing interval", ex.getMessage());
            throw ex;
        }
    }
}