import org.apache.commons.math.analysis.PolynomialFunction;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testDefaultAbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver.DefaultAbsoluteAccuracy();
        assertEquals(1e - 6, solver.getDefaultAbsoluteAccuracy(), 0);
    }

    @Test
    public void testConstructor() {
        double absoluteAccuracy = 0.1;
        BaseSecantSolver.Method method = BaseSecantSolver.Method.REGULA_FALSI;
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, method);

        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0);
        assertEquals(method, solver.getMethod());
    }

    @Test
    public void testMethodEnums() {
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, BaseSecantSolver.Method.valueOf("REGULA_FALSI"));
        assertEquals(BaseSecantSolver.Method.ILLINOIS, BaseSecantSolver.Method.valueOf("ILLINOIS"));
        assertEquals(BaseSecantSolver.Method.PEGASUS, BaseSecantSolver.Method.valueOf("PEGASUS"));
    }

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver.DefaultAbsoluteAccuracy() {
            protected double computeObjectiveValue(double x) {
                return x;
            }
        };

        assertEquals(0, solver.solve(10, new PolynomialFunction(new double[] {0, 1}), 0, 2, 1), 0);
    }

    @Test
    public void testDoSolve() {
        BaseSecantSolver solver = new BaseSecantSolver.DefaultAbsoluteAccuracy() {
            protected double computeObjectiveValue(double x) {
                return x;
            }
        };

        solver.setAbsoluteAccuracy(0.1);
        assertEquals(0, solver.doSolve(), 0);
    }

    @Test
    public void testInvalidSolve() {
        BaseSecantSolver solver = new BaseSecantSolver.DefaultAbsoluteAccuracy() {
            protected double computeObjectiveValue(double x) {
                return x;
            }
        };

        solver.setAbsoluteAccuracy(0.1);
        try {
            solver.solve(10, null, 0, 2, 1);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertEquals("math : null function in interval [0, 2]", e.getMessage());
        }
    }
}