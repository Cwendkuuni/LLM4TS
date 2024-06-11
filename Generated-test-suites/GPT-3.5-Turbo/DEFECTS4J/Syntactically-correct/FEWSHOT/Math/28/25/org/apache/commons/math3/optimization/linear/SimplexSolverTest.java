/*
Corresponding JUnit 4 Test Cases:
*/
package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.junit.Test;

public class SimplexSolverTest {
    
    private static final double EPSILON = 1e-6;
    
    @Test
    public void testDoOptimizeMinimize() throws Exception {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[] { 1, 1 }, 0);
        LinearConstraint constraint1 = new LinearConstraint(new double[] { 1, 0 }, Relationship.LEQ, 5);
        LinearConstraint constraint2 = new LinearConstraint(new double[] { 0, 1 }, Relationship.LEQ, 4);
        LinearConstraint[] constraints = { constraint1, constraint2 };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize(function, constraints, GoalType.MINIMIZE, false);
        double[] expectedPoint = {0, 3};
        assertEquals(GoalType.MINIMIZE, solution.getGoalType());
        assertTrue(solution.getPoint().length == expectedPoint.length);
        for (int i = 0; i < expectedPoint.length; i++) {
            assertEquals(expectedPoint[i], solution.getPoint()[i], EPSILON);
        }
        assertEquals(3, solution.getValue(), EPSILON);
    }
    
    @Test
    public void testDoOptimizeMaximize() throws Exception {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[] { 1, 1 }, 0);
        LinearConstraint constraint1 = new LinearConstraint(new double[] { 1, 0 }, Relationship.LEQ, 5);
        LinearConstraint constraint2 = new LinearConstraint(new double[] { 0, 1 }, Relationship.LEQ, 4);
        LinearConstraint[] constraints = { constraint1, constraint2 };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize(function, constraints, GoalType.MAXIMIZE, false);
        double[] expectedPoint = {5, 0};
        assertEquals(GoalType.MAXIMIZE, solution.getGoalType());
        assertTrue(solution.getPoint().length == expectedPoint.length);
        for (int i = 0; i < expectedPoint.length; i++) {
            assertEquals(expectedPoint[i], solution.getPoint()[i], EPSILON);
        }
        assertEquals(5, solution.getValue(), EPSILON);
    }
    
    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimizeNoSolution() throws Exception {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[] { 1, 1 }, 0);
        LinearConstraint constraint1 = new LinearConstraint(new double[] { 1, 0 }, Relationship.LEQ, 5);
        LinearConstraint constraint2 = new LinearConstraint(new double[] { 0, 1 }, Relationship.LEQ, -4);
        LinearConstraint[] constraints = { constraint1, constraint2 };
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize(function, constraints, GoalType.MINIMIZE, false);
    }
    
    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimizeUnboundedSolution() throws Exception {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[] { 1, 1 }, 0);
        LinearConstraint constraint1 = new LinearConstraint(new double[] { 1, 0 }, Relationship.LEQ, 5);
        LinearConstraint constraint2 = new LinearConstraint(new double[] { 0, 1 }, Relationship.LEQ, Double.POSITIVE_INFINITY);
        LinearConstraint[] constraints = { constraint1, constraint2 };
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize(function, constraints, GoalType.MINIMIZE, false);
    }
    
    @Test(expected = MaxCountExceededException.class)
    public void testDoOptimizeMaxIterations() throws Exception {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[] { 1, 1 }, 0);
        LinearConstraint constraint1 = new LinearConstraint(new double[] { 1, 0 }, Relationship.LEQ, 5);
        LinearConstraint constraint2 = new LinearConstraint(new double[] { 0, 1 }, Relationship.LEQ, 4);
        LinearConstraint[] constraints = { constraint1, constraint2 };
        SimplexSolver solver = new SimplexSolver(1e-8, 10);
        solver.doOptimize(function, constraints, GoalType.MINIMIZE, false);
    }
    
    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimizeNoFeasibleSolution() throws Exception {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[] { 1, 1 }, 0);
        LinearConstraint constraint1 = new LinearConstraint(new double[] { -1, 0 }, Relationship.LEQ, -5);
        LinearConstraint constraint2 = new LinearConstraint(new double[] { 0, -1 }, Relationship.LEQ, -4);
        LinearConstraint[] constraints = { constraint1, constraint2 };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize(function, constraints, GoalType.MINIMIZE, true);
        double[] expectedPoint = {0, 0};
        assertEquals(GoalType.MINIMIZE, solution.getGoalType());
        assertTrue(solution.getPoint().length == expectedPoint.length);
        for (int i = 0; i < expectedPoint.length; i++) {
            assertEquals(expectedPoint[i], solution.getPoint()[i], EPSILON);
        }
        assertEquals(0, solution.getValue(), EPSILON);
    }

}