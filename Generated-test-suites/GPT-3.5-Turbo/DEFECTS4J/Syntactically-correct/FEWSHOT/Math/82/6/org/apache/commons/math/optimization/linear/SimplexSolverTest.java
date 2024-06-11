package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Test;

public class SimplexSolverTest {
    private static final double EPSILON = 1.0e-6;
    
    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = createTableau();

        // Test with a tableau containing negative entries
        Integer pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        assertEquals(new Integer(0), pivotColumn);
        
        // Test with a tableau containing only positive entries
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, 2.0);
        tableau.setEntry(0, 2, 3.0);
        pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        assertNull(pivotColumn);
    }
    
    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = createTableau();
        
        // Test with a tableau containing positive entries
        Integer pivotRow = new SimplexSolver().getPivotRow(0, tableau);
        assertEquals(new Integer(1), pivotRow);
        
        // Test with a tableau containing negative entries
        tableau.setEntry(1, 0, -3.0);
        pivotRow = new SimplexSolver().getPivotRow(0, tableau);
        assertNull(pivotRow);
    }
    
    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnboundedSolutionException() throws OptimizationException {
        SimplexTableau tableau = createTableau();
        tableau.setEntry(1, 0, -1.0); // Make the solution unbounded
        new SimplexSolver().doIteration(tableau);
    }
    
    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = createTableau();
        new SimplexSolver().doIteration(tableau);
        
        // Check that pivot element is 1.0
        assertEquals(1.0, tableau.getEntry(1, 1), EPSILON);
        
        // Check that rest of the pivot column is 0.0
        assertEquals(0.0, tableau.getEntry(0, 1), EPSILON);
        assertEquals(0.0, tableau.getEntry(2, 1), EPSILON);
    }
    
    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = createTableau();
        assertTrue(new SimplexSolver().isPhase1Solved(tableau));
        
        // Test with a tableau containing negative entries
        tableau.setEntry(0, 0, -2.0);
        assertFalse(new SimplexSolver().isPhase1Solved(tableau));
    }
    
    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = createTableau();
        assertTrue(new SimplexSolver().isOptimal(tableau));
        
        // Test with a tableau containing negative entries
        tableau.setEntry(0, 0, -2.0);
        assertFalse(new SimplexSolver().isOptimal(tableau));
    }
    
    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolutionException() throws OptimizationException {
        SimplexTableau tableau = createTableau();
        tableau.setEntry(0, 1, -1.0); // Make W non-zero
        new SimplexSolver().solvePhase1(tableau);
    }
    
    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexTableau tableau = createTableau();
        new SimplexSolver().solvePhase1(tableau);
        
        // Check that W is 0.0
        assertEquals(0.0, tableau.getEntry(0, tableau.getRhsOffset()), EPSILON);
    }
    
    @Test
    public void testDoOptimize() throws OptimizationException {
        double[] coef = {5, 7, 3};
        double[][] matrix = {
            {-1, 1, 3, 9},
            {2, 3, -1, 15},
            {4, -1, 2, 12},
        };
        
        LinearObjectiveFunction f = new LinearObjectiveFunction(coef, 0);
        LinearConstraintSet constraints = new LinearConstraintSet(matrix, Relationship.LEQ, new double[]{2, -1, 3});
        
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.doOptimize(f, constraints);
        
        // Check the optimal solution
        assertEquals(1.0, solution.getPoint()[0], EPSILON);
        assertEquals(2.0, solution.getPoint()[1], EPSILON);
        assertEquals(3.0, solution.getPoint()[2], EPSILON);
        assertEquals(20.0, solution.getValue(), EPSILON);
    }
    
    private SimplexTableau createTableau() {
        double[][] tableauData = {
            {1.0, -1.0,  1.0,  0.0,  0.0},
            {1.0,  1.0, -1.0,  1.0,  1.0}
        };
        
        double[] coefficients = {5.0, 3.0, -2.0};
        Relationship[] relationships = {Relationship.LEQ, Relationship.GEQ};
        double[] rhsValues = {7.0, -1.0};
        
        return new SimplexTableau(tableauData, coefficients, relationships, rhsValues, false, EPSILON);
    }
}