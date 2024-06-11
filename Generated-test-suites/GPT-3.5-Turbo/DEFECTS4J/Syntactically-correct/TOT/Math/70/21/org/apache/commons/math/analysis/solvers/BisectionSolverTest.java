import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    // Test case for BisectionSolver(UnivariateRealFunction f)
    @Test
    public void testConstructor1() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver(f);

        double expected = -1.9990234375;
        double actual = solver.solve(0, 3);

        assertEquals(expected, actual, 0.0001);
    }

    // Test case for BisectionSolver()
    @Test
    public void testConstructor2() {
        BisectionSolver solver = new BisectionSolver();

        double expected = 0.0;
        double actual = solver.solve(new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        }, -1, 1);

        assertEquals(expected, actual, 0.0001);
    }

    // Test case for solve(double min, double max, double initial)
    @Test
    public void testSolve1() {
        BisectionSolver solver = new BisectionSolver();

        double expected = -0.681396484375;
        double actual = solver.solve(new UnivariateRealFunction() {
            public double value(double x) {
                return x * x * x - x - 1;
            }
        }, -2, 2, 1.5);

        assertEquals(expected, actual, 0.0001);
    }

    // Test case for solve(double min, double max)
    @Test
    public void testSolve2() {
        BisectionSolver solver = new BisectionSolver();

        double expected = 1.0009765625;
        double actual = solver.solve(new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, -2, 3);

        assertEquals(expected, actual, 0.0001);
    }

    // Test case for solve(UnivariateRealFunction f, double min, double max, double initial)
    @Test
    public void testSolve3() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x * x - x - 1;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        double expected = 0.4375;
        double actual = solver.solve(f, 0, 2, 1);

        assertEquals(expected, actual, 0.0001);
    }

    // Test case for solve(UnivariateRealFunction f, double min, double max)
    @Test
    public void testSolve4() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 3 * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        double expected = 1.33349609375;
        double actual = solver.solve(f, 0, 3);

        assertEquals(expected, actual, 0.0001);
    }

    // Test case for solve(UnivariateRealFunction f, double min, double max) with invalid interval
    @Test(expected = IllegalArgumentException.class)
    public void testSolve5() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        solver.solve(f, 3, 0);
    }

    // Test case for solve(UnivariateRealFunction f, double min, double max) with max iterations exceeded
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolve6() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x * x - x - 1;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        solver.solve(f, -2, 2);
    }

}