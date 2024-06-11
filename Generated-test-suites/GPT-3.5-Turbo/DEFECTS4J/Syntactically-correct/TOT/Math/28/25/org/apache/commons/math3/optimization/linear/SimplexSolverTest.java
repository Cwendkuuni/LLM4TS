import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.Relationship;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.UnboundedSolutionException;
import org.apache.commons.math3.optimization.linear.NoFeasibleSolutionException;
import org.apache.commons.math3.optimization.linear.PointValuePair;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{1, 1}, 0);
        
        // Test typical use case
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.optimize(function,
                                                new LinearConstraintSet(new LinearConstraint(new double[]{1, 2}, Relationship.GEQ, 4),
                                                                        new LinearConstraint(new double[]{-3, 1}, Relationship.LEQ, 6)),
                                                GoalType.MAXIMIZE);
        assertEquals(2.0, result.getPoint()[0], 0.0001);
        assertEquals(1.0, result.getPoint()[1], 0.0001);
        assertEquals(2.0, result.getValue(), 0.0001);
        
        // Test edge case
        function = new LinearObjectiveFunction(new double[]{1, 2, 3}, 0);
        solver = new SimplexSolver();
        result = solver.optimize(function,
                                 new LinearConstraintSet(new LinearConstraint(new double[]{1, 1, 1}, Relationship.LEQ, 5),
                                                         new LinearConstraint(new double[]{2, 2, 2}, Relationship.LEQ, 8),
                                                         new LinearConstraint(new double[]{3, 3, 3}, Relationship.LEQ, 11)),
                                 GoalType.MAXIMIZE);
        assertEquals(1.6667, result.getPoint()[0], 0.0001);
        assertEquals(1.6667, result.getPoint()[1], 0.0001);
        assertEquals(1.6667, result.getPoint()[2], 0.0001);
        assertEquals(11.0, result.getValue(), 0.0001);
        
        // Test error scenario - Unbounded solution
        function = new LinearObjectiveFunction(new double[]{1, 1}, 0);
        solver = new SimplexSolver();
        try {
            result = solver.optimize(function,
                                     new LinearConstraintSet(new LinearConstraint(new double[]{1, 1}, Relationship.GEQ, 1)),
                                     GoalType.MAXIMIZE);
            fail("An exception should have been thrown.");
        } catch (UnboundedSolutionException e) {
            // Expected exception
        }
        
        // Test error scenario - No feasible solution
        function = new LinearObjectiveFunction(new double[]{1, 1}, 0);
        solver = new SimplexSolver();
        try {
            result = solver.optimize(function,
                                     new LinearConstraintSet(new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, -1)),
                                     GoalType.MAXIMIZE);
            fail("An exception should have been thrown.");
        } catch (NoFeasibleSolutionException e) {
            // Expected exception
        }
    }
}