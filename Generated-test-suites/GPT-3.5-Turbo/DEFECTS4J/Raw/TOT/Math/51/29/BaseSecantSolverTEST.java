import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.util.FastMath;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    @Test
    public void testDefaultAbsoluteAccuracy() {
        double tol = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(0.1, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(tol, solver.getAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testDefault(){
        BaseSecantSolver solver = new BaseSecantSolver(0.1, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(BaseSecantSolver.AllowedSolution.ANY_SIDE, solver.getAllowedSolutions());
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    @Test
    public void testGetSolution(){
        BaseSecantSolver solver = new BaseSecantSolver(0.1, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(0.0, solver.getFunctionValueAccuracy(), 0.0);
    }
}