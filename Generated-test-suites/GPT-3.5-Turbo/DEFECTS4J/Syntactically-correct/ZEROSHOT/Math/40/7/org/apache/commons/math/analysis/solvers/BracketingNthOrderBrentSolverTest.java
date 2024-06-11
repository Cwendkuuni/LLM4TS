import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDoSolve() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.doSolve();
    }

    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        int result = solver.getMaximalOrder();
    }

    @Test
    public void testSolve() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        }, -10, 10);
    }

    @Test
    public void testSolveWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        }, -10, 10, 0);
    }

}