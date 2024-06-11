import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Test;

public class BaseSecantSolverTest {
    @Test
    public void testSolve() {
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver.Method method = BaseSecantSolver.Method.REGULA_FALSI;
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, method) {
            @Override
            protected double computeObjectiveValue(double arg0) throws MathInternalError {
                // TODO Auto-generated method stub
                return 0;
            }
        };
        
        int maxEval = 100;
        UnivariateRealFunction f = null;
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;
        
        assertEquals(Double.NaN, solver.solve(maxEval, f, min, max, allowedSolution), absoluteAccuracy);
        
        solver = new BaseSecantSolver(absoluteAccuracy, method) {
            @Override
            protected double computeObjectiveValue(double arg0) throws MathInternalError {
                // TODO Auto-generated method stub
                return 1.0;
            }
        };
        
        assertEquals(Double.NaN, solver.solve(maxEval, f, min, max, allowedSolution), absoluteAccuracy);
        
        
    }

    @Test
    public void testSolve1() {
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver.Method method = BaseSecantSolver.Method.REGULA_FALSI;
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, method) {
            @Override
            protected double computeObjectiveValue(double arg0) throws MathInternalError {
                // TODO Auto-generated method stub
                return 0;
            }
        };
        
        int maxEval = 100;
        UnivariateRealFunction f = null;
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;
        
        assertEquals(Double.NaN, solver.solve(maxEval, f, min, max, startValue, allowedSolution), absoluteAccuracy);
        
        solver = new BaseSecantSolver(absoluteAccuracy, method) {
            @Override
            protected double computeObjectiveValue(double arg0) throws MathInternalError {
                // TODO Auto-generated method stub
                return 1.0;
            }
        };
        
        assertEquals(Double.NaN, solver.solve(maxEval, f, min, max, startValue, allowedSolution), absoluteAccuracy);
    }

    @Test
    public void testSolve2() {
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver.Method method = BaseSecantSolver.Method.REGULA_FALSI;
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, method) {
            @Override
            protected double computeObjectiveValue(double arg0) throws MathInternalError {
                // TODO Auto-generated method stub
                return 0;
            }
        };
        
        int maxEval = 100;
        UnivariateRealFunction f = null;
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;
        
        assertEquals(Double.NaN, solver.solve(maxEval, f, min, max, startValue), absoluteAccuracy);
        
        solver = new BaseSecantSolver(absoluteAccuracy, method) {
            @Override
            protected double computeObjectiveValue(double arg0) throws MathInternalError {
                // TODO Auto-generated method stub
                return 1.0;
            }
        };
        
        assertEquals(Double.NaN, solver.solve(maxEval, f, min, max, startValue), absoluteAccuracy);
    }

    // other test methods
    
    @Test
    public void testBaseSecantSolver() {
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver.Method method = BaseSecantSolver.Method.REGULA_FALSI;
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, method) {
            @Override
            protected double computeObjectiveValue(double arg0) throws MathInternalError {
                // TODO Auto-generated method stub
                return 0;
            }
        };

        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(method, solver.getMethod());
        
        double relativeAccuracy = 1e-3;
        double functionValueAccuracy = 1e-4;
        solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method) {
            @Override
            protected double computeObjectiveValue(double arg0) throws MathInternalError {
                // TODO Auto-generated method stub
                return 0;
            }
        };

        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0.0);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(functionValueAccuracy, solver.getFunctionValueAccuracy(), 0.0);
        assertEquals(method, solver.getMethod());
    }

    // other test methods
}