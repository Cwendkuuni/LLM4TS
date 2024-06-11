import org.junit.Test;
import org.junit.Assert;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveWithDefaultSolver() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, 1, 3);
        Assert.assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolveWithCustomAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, 1, 3, 0.001);
        Assert.assertEquals(2.0, result, 0.001);
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double[] bracket = UnivariateRealSolverUtils.bracket(f, 2, 1, 3);
        Assert.assertEquals(2.0, bracket[0], 0.0001);
        Assert.assertEquals(3.0, bracket[1], 0.0001);
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(1, 3);
        Assert.assertEquals(2.0, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() {
        UnivariateRealSolverUtils.setup(null);
    }

    @Test
    public void testBracketFailure() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x + 4; // No root in this case
            }
        };
        try {
            UnivariateRealSolverUtils.bracket(f, 2, 1, 3);
            Assert.fail("ConvergenceException not thrown");
        } catch (ConvergenceException e) {
            // Expected exception
        } catch (FunctionEvaluationException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    // Add more test cases for edge cases and exceptions if needed

}