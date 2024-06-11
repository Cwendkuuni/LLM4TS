import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        // Test case 1
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        double x01 = 1;
        double x11 = 3;
        try {
            double result = UnivariateRealSolverUtils.solve(f1, x01, x11);
            assertEquals(2.0, result, 0.0001);
        } catch (ConvergenceException ce) {
            fail("ConvergenceException thrown");
        } catch (FunctionEvaluationException fee) {
            fail("FunctionEvaluationException thrown");
        }

        // Test case 2
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x) - 0.5;
            }
        };
        double x02 = 0;
        double x12 = Math.PI;
        try {
            double result = UnivariateRealSolverUtils.solve(f2, x02, x12);
            assertEquals(Math.PI / 6, result, 0.0001);
        } catch (ConvergenceException ce) {
            fail("ConvergenceException thrown");
        } catch (FunctionEvaluationException fee) {
            fail("FunctionEvaluationException thrown");
        }

        // Test case 3
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.exp(x) - 10;
            }
        };
        double x03 = 1;
        double x13 = 3;
        try {
            double result = UnivariateRealSolverUtils.solve(f3, x03, x13, 0.00001);
            assertEquals(2.302585093, result, 0.0001);
        } catch (ConvergenceException ce) {
            fail("ConvergenceException thrown");
        } catch (FunctionEvaluationException fee) {
            fail("FunctionEvaluationException thrown");
        }

    }

    @Test
    public void testBracket() {
        // Test case 1
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        double initial1 = 1;
        double lowerBound1 = -10;
        double upperBound1 = 10;
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f1, initial1, lowerBound1, upperBound1);
            assertEquals(-1.0, result[0], 0.0001);
            assertEquals(3.0, result[1], 0.0001);
        } catch (ConvergenceException ce) {
            fail("ConvergenceException thrown");
        } catch (FunctionEvaluationException fee) {
            fail("FunctionEvaluationException thrown");
        }

        // Test case 2
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x) - 0.5;
            }
        };
        double initial2 = -Math.PI / 4;
        double lowerBound2 = -Math.PI;
        double upperBound2 = Math.PI;
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f2, initial2, lowerBound2, upperBound2);
            assertEquals(-Math.PI / 2, result[0], 0.0001);
            assertEquals(0, result[1], 0.0001);
        } catch (ConvergenceException ce) {
            fail("ConvergenceException thrown");
        } catch (FunctionEvaluationException fee) {
            fail("FunctionEvaluationException thrown");
        }

        // Test case 3
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.exp(x) - 10;
            }
        };
        double initial3 = 2;
        double lowerBound3 = -10;
        double upperBound3 = 10;
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f3, initial3, lowerBound3, upperBound3, 20);
            assertEquals(1.0, result[0], 0.0001);
            assertEquals(3.0, result[1], 0.0001);
        } catch (ConvergenceException ce) {
            fail("ConvergenceException thrown");
        } catch (FunctionEvaluationException fee) {
            fail("FunctionEvaluationException thrown");
        }
    }

    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 3.0;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(2.0, result, 0.0001);
    }

}