import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WhenMinAndMaxAreZero_ReturnsZero() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x;
            }
        };

        // Act
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        }, 0, 0);

        // Assert
        Assert.assertEquals(0, result, 1e-6);
    }

    @Test
    public void testSolve_WhenMinIsZero_ReturnsMin() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x;
            }
        };

        // Act
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        }, 0, 5);

        // Assert
        Assert.assertEquals(0, result, 1e-6);
    }

    @Test
    public void testSolve_WhenMaxIsZero_ReturnsMax() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x;
            }
        };

        // Act
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        }, -5, 0);

        // Assert
        Assert.assertEquals(0, result, 1e-6);
    }

    @Test
    public void testSolve_WhenObjectiveValueIsZero_ReturnsRoot() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x * (x - 2) * (x + 2);
            }
        };

        // Act
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * (x - 2) * (x + 2);
            }
        }, -5, 5);

        // Assert
        Assert.assertEquals(0, result, 1e-6);
    }

    @Test
    public void testSolve_WhenStartValueIsRoot_ReturnsStartValue() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x * (x - 2) * (x + 2);
            }
        };

        // Act
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * (x - 2) * (x + 2);
            }
        }, -5, 5, 0);

        // Assert
        Assert.assertEquals(0, result, 1e-6);
    }

    @Test
    public void testSolve_WhenNoRootInRange_ThrowsConvergenceException() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x * (x - 2) * (x + 2);
            }
        };

        // Act & Assert
        try {
            solver.solve(100, new UnivariateRealFunction() {
                public double value(double x) {
                    return x * (x - 2) * (x + 2);
                }
            }, 3, 5);
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Pass
        }
    }

    @Test
    public void testSolve_WhenMethodIsRegulaFalsi_DoesNotThrowMathInternalError() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x * (x - 2) * (x + 2);
            }
        };

        // Act
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * (x - 2) * (x + 2);
            }
        }, -5, 5);

        // Assert
        Assert.assertTrue(true);
    }

    @Test
    public void testSolve_WhenMethodIsIllinois_DoesNotThrowMathInternalError() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x * (x - 2) * (x + 2);
            }
        };

        // Act
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * (x - 2) * (x + 2);
            }
        }, -5, 5);

        // Assert
        Assert.assertTrue(true);
    }

    @Test
    public void testSolve_WhenMethodIsPegasus_DoesNotThrowMathInternalError() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x * (x - 2) * (x + 2);
            }
        };

        // Act
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * (x - 2) * (x + 2);
            }
        }, -5, 5);

        // Assert
        Assert.assertTrue(true);
    }

}