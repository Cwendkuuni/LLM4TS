package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.util.FastMath;

import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x * (x - 3);
            }
        };
        try {
            solver.solve(100, new UnivariateRealFunction() {
                public double value(double x) {
                    return x * (x - 3);
                }
            }, 0, 2);
        } catch (ConvergenceException e) {
            fail(e.getMessage());
        }

        BaseSecantSolver solver2 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS) {
            protected double computeObjectiveValue(double x) {
               return x * (x - 2);
            }
        };
        try {
            solver2.solve(100, new UnivariateRealFunction() {
                public double value(double x) {
                    return x * (x - 2);
                }
            }, 0, 2);
        } catch (ConvergenceException e) {
            fail(e.getMessage());
        }

        BaseSecantSolver solver3 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            protected double computeObjectiveValue(double x) {
               return x * (x - 4);
            }
        };
         try {
            solver3.solve(100, new UnivariateRealFunction() {
                public double value(double x) {
                    return x * (x - 4);
                }
            }, 0, 2);
        } catch (ConvergenceException e) {
            fail(e.getMessage());
        }
    }
}