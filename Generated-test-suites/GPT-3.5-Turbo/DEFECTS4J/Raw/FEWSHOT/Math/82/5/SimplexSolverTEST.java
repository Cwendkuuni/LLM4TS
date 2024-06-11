package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.optimization.OptimizationException;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {
    private double epsilon;
    private SimplexSolver solver;
    private SimplexTableau tableau;

    @Before
    public void setUp() {
        epsilon = 1.0e-6;
        solver = new SimplexSolver(epsilon);

        double[][] coefficients = {
                { 2, 3 },
                { 4, 2 },
                { 3, 1 }
        };
        double[] constants = { 10, 12, 7 };
        double[] objectiveCoefficients = { 5, 4 };

        tableau = new SimplexTableau(coefficients, constants, objectiveCoefficients,
                OptimizationType.MAXIMIZE, true, epsilon);
    }

    @Test
    public void testGetPivotColumn() {
        assertEquals(new Integer(1), solver.getPivotColumn(tableau));
        tableau.divideRow(1, 2.0);
        assertEquals(new Integer(0), solver.getPivotColumn(tableau));
        tableau.divideRow(2, 3.0);
        assertEquals(new Integer(1), solver.getPivotColumn(tableau));
    }

    @Test
    public void testGetPivotRow() {
        assertEquals(new Integer(0), solver.getPivotRow(1, tableau));
        tableau.divideRow(0, 5.0);
        assertEquals(new Integer(2), solver.getPivotRow(1, tableau));
        tableau.divideRow(2, 1.5);
        assertEquals(new Integer(0), solver.getPivotRow(1, tableau));
    }

    @Test 
    public void testDoIteration() throws OptimizationException {
        solver.doIteration(tableau);
        double[][] expectedCoefficients = {
                { 4.0, 0.0 },
                { 2.0, 1.0 },
                { 3.0, 0.0 }
        };
        double[] expectedConstants = { 4.0, 6.0, 2.0 };
        tableau.getCoefficients(expectedCoefficients);
        tableau.getConstants(expectedConstants);
        double[][] actualCoefficients = tableau.getCoefficients();
        double[] actualConstants = tableau.getConstants();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                assertEquals(expectedCoefficients[i][j], actualCoefficients[i][j], epsilon);
            }
            assertEquals(expectedConstants[i], actualConstants[i], epsilon);
        }
    }

    @Test
    public void testIsPhase1Solved() {
        assertFalse(solver.isPhase1Solved(tableau));
        tableau.divideRow(0, 2.0);
        assertTrue(solver.isPhase1Solved(tableau));
        tableau.subtractRow(2, 0, -1.5);
        assertFalse(solver.isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        assertFalse(solver.isOptimal(tableau));
        tableau.divideRow(0, 2.0);
        assertFalse(solver.isOptimal(tableau));
        tableau.subtractRow(2, 1, -2.5);
        assertTrue(solver.isOptimal(tableau));
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1WithNoFeasibleSolution() throws OptimizationException {
        tableau.divideRow(0, 5.0);
        solver.solvePhase1(tableau);
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        solver.solvePhase1(tableau);
        assertEquals(3, tableau.getWidth());
        assertEquals(3, tableau.getHeight());
        double[][] expectedCoefficients = {
                {2.0, 1.0, 0.000833333333}
        };
        double[] expectedConstants = { 0.000833333333 };
        tableau.getCoefficients(expectedCoefficients);
        tableau.getConstants(expectedConstants);
        double[][] actualCoefficients = tableau.getCoefficients();
        double[] actualConstants = tableau.getConstants();
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(expectedCoefficients[i][j], actualCoefficients[i][j], epsilon);
            }
            assertEquals(expectedConstants[i], actualConstants[i], epsilon);
        }
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        RealPointValuePair solution = solver.doOptimize();
        double[] expectedPoint = { 3.0, 2.0 };
        double expectedValue = 23.0;
        double[] actualPoint = solution.getPoint();
        double actualValue = solution.getValue();
        for (int i = 0; i < 2; i++) {
            assertEquals(expectedPoint[i], actualPoint[i], epsilon);
        }
        assertEquals(expectedValue, actualValue, epsilon);
    }
}