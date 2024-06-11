package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {
    private static final double EPSILON = 1e-6;
    private static final int MAX_ULPS = 10;
    
    private SimplexSolver simplexSolver;
    
    @Before
    public void setUp() {
        simplexSolver = new SimplexSolver(EPSILON, MAX_ULPS);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(EPSILON, simplexSolver.epsilon, 0);
        assertEquals(MAX_ULPS, simplexSolver.maxUlps);
    }
    
    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] { 4, -2 }), 
                new LinearConstraint[] { new LinearConstraint(new double[] { 1, 1 }, Relationship.GEQ, 2), 
                                         new LinearConstraint(new double[] { -1, 2 }, Relationship.GEQ, 2) },
                GoalType.MAXIMIZE, true);
        
        assertEquals(0, simplexSolver.getPivotColumn(tableau).intValue());
        
        tableau.dropPhase1Objective();
        
        assertEquals(1, simplexSolver.getPivotColumn(tableau).intValue());
        
        tableau.subtractRow(1, 0, 2);
        
        assertNull(simplexSolver.getPivotColumn(tableau));
    }
    
    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] { 4, -2 }), 
                new LinearConstraint[] { new LinearConstraint(new double[] { 1, 1 }, Relationship.GEQ, 2), 
                                         new LinearConstraint(new double[] { -1, 2 }, Relationship.GEQ, 2) },
                GoalType.MAXIMIZE, true);
        
        assertEquals(1, simplexSolver.getPivotRow(tableau, 0).intValue());
        
        tableau.setEntry(2, 0, 0);
        
        assertEquals(1, simplexSolver.getPivotRow(tableau, 0).intValue());
        
        tableau.setEntry(0, 0, -1);
        
        assertEquals(0, simplexSolver.getPivotRow(tableau, 0).intValue());
        
        tableau.setEntry(2, 0, 2);
        
        assertNull(simplexSolver.getPivotRow(tableau, 0));
    }
    
    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1ThrowsNoFeasibleSolutionException() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] { 4, -2 }), 
                new LinearConstraint[] { new LinearConstraint(new double[] { 1, 1 }, Relationship.EQ, 3) },
                GoalType.MAXIMIZE, true);
        
        simplexSolver.solvePhase1(tableau);
    }
    
    @Test
    public void testSolvePhase1WithUnboundedSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] { 1, 1 }), 
                new LinearConstraint[] { new LinearConstraint(new double[] { 1, 1 }, Relationship.GEQ, 0) },
                GoalType.MINIMIZE, true);
        
        try {
            simplexSolver.solvePhase1(tableau);
        } catch (UnboundedSolutionException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] { 4, -2 }), 
                new LinearConstraint[] { new LinearConstraint(new double[] { 1, 1 }, Relationship.GEQ, 2), 
                                         new LinearConstraint(new double[] { -1, 2 }, Relationship.GEQ, 2) },
                GoalType.MAXIMIZE, true);
        
        simplexSolver.solvePhase1(tableau);
        
        assertEquals(2, tableau.getNumArtificialVariables());
        assertEquals(1, tableau.getArtificialVariableOffset());
    }
    
    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] { 2, -3 }), 
                new LinearConstraint[] { new LinearConstraint(new double[] { 2, 1 }, Relationship.LEQ, 10), 
                                         new LinearConstraint(new double[] { -1, 1 }, Relationship.LEQ, 1) },
                GoalType.MAXIMIZE, true);
        
        assertEquals(10, tableau.getEntry(2, 4), EPSILON);
        assertEquals(1, tableau.getEntry(3, 3), EPSILON);
        
        simplexSolver.doIteration(tableau);
        
        assertEquals(3, tableau.getEntry(2, 4), EPSILON);
        assertEquals(-2, tableau.getEntry(3, 3), EPSILON);
        assertEquals(1, tableau.getEntry(3, 4), EPSILON);
        
        simplexSolver.doIteration(tableau);
        
        assertEquals(6, tableau.getEntry(2, 4), EPSILON);
        assertEquals(-1, tableau.getEntry(2, 3), EPSILON);
        assertEquals(2, tableau.getEntry(2, 2), EPSILON);
        assertEquals(1, tableau.getEntry(3, 4), EPSILON);
    }
    
    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimizeThrowsNoFeasibleSolutionException() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] { -1, -1 }), 
                new LinearConstraint[] { new LinearConstraint(new double[] { 1, 0 }, Relationship.GEQ, 1), 
                                         new LinearConstraint(new double[] { 0, 1 }, Relationship.GEQ, 2) },
                GoalType.MINIMIZE, true);
        
        simplexSolver.doOptimize(tableau);
    }
    
    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] { 3, 1 }), 
                new LinearConstraint[] { new LinearConstraint(new double[] { -2, 1 }, Relationship.LEQ, 1), 
                                         new LinearConstraint(new double[] { 1, 2 }, Relationship.LEQ, 2) },
                GoalType.MINIMIZE, true);

        PointValuePair solution = simplexSolver.doOptimize(tableau);
        
        assertNotNull(solution);
        assertEquals(0.20000000000000004, solution.getPoint()[0], EPSILON);
        assertEquals(0.4, solution.getPoint()[1], EPSILON);
        assertEquals(1.0, solution.getValue(), EPSILON);
    }
}