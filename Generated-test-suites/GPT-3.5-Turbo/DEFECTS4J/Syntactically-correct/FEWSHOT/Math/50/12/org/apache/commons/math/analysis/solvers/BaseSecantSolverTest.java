package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.analysis.UnivariateRealFunction;

import org.junit.Test;

public class BaseSecantSolverTest {
	@Test
	public void testSolve() {
		BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
		UnivariateRealFunction f = new UnivariateRealFunction() {
			public double value(double x) {
				return x * x - 4;
			}
		};
		assertEquals(2.0, solver.solve(100, f, 0, 3, AllowedSolution.ANY_SIDE), 1e-6);
		assertEquals(-2.0, solver.solve(100, f, -3, 0, AllowedSolution.RIGHT_SIDE), 1e-6);
		assertNull(solver.solve(100, f, -1, 1, AllowedSolution.ANY_SIDE));
	}
}