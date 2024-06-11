import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.Relationship;
import org.apache.commons.math3.optimization.linear.LinearConstraint;


public class SimplexSolverTest {
    
    @Test
    public void getPivotColumn_validTableau_returnColumnWithMostNegativeCoefficient() {
        // Arrange
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] {1, -2}, 0),
                                                    new LinearConstraint[] { 
                                                        new LinearConstraint(new double[] { 1 , 1 }, Relationship.LEQ, 1),
                                                        new LinearConstraint(new double[] { 1 , -1 }, Relationship.LEQ, 2)
                                                    },
                                                    GoalType.MAXIMIZE,
                                                    true, // restrict to non-negative
                                                    0.000001, 
                                                    10);
        
        SimplexSolver solver = new SimplexSolver();
        
        // Act
        Integer pivotColumn = solver.getPivotColumn(tableau);
        
        // Assert
        Assert.assertEquals(1, pivotColumn.intValue());
    }
    
    @Test
    public void getPivotRow_validTableauAndPivotColumn_returnRowWithMinimumRatio() {
        // Arrange
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] {1, -2}, 0),
                                                    new LinearConstraint[] { 
                                                        new LinearConstraint(new double[] { 1 , 1 }, Relationship.LEQ, 1),
                                                        new LinearConstraint(new double[] { 1 , -1 }, Relationship.LEQ, 2)
                                                    },
                                                    GoalType.MAXIMIZE,
                                                    true, // restrict to non-negative
                                                    0.000001, 
                                                    10);
        
        SimplexSolver solver = new SimplexSolver();
        Integer pivotColumn = solver.getPivotColumn(tableau);
        
        // Act
        Integer pivotRow = solver.getPivotRow(tableau, pivotColumn);
        
        // Assert
        Assert.assertEquals(2, pivotRow.intValue());
    }
    
    @Test(expected = UnboundedSolutionException.class)
    public void doIteration_unboundedSolution_throwsUnboundedSolutionException() throws MaxCountExceededException, UnboundedSolutionException {
        // Arrange
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] {1, -2}, 0),
                                                    new LinearConstraint[] { 
                                                        new LinearConstraint(new double[] { 1 , 1 }, Relationship.LEQ, 1),
                                                        new LinearConstraint(new double[] { 1 , -1 }, Relationship.LEQ, 2)
                                                    },
                                                    GoalType.MAXIMIZE,
                                                    true, // restrict to non-negative
                                                    0.000001, 
                                                    10);
        
        SimplexSolver solver = new SimplexSolver();
        
        // Act
        solver.doIteration(tableau);
    }
    
    @Test(expected = NoFeasibleSolutionException.class)
    public void solvePhase1_noFeasibleSolution_throwsNoFeasibleSolutionException() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        // Arrange
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] {1, -2}, 0),
                                                    new LinearConstraint[] { 
                                                        new LinearConstraint(new double[] { 1 , 1 }, Relationship.GEQ, 1),
                                                        new LinearConstraint(new double[] { 1 , -1 }, Relationship.LEQ, 2)
                                                    },
                                                    GoalType.MAXIMIZE,
                                                    true, // restrict to non-negative
                                                    0.000001, 
                                                    10);
        
        SimplexSolver solver = new SimplexSolver();
        
        // Act
        solver.solvePhase1(tableau);
    }
    
    @Test
    public void doOptimize_validTableau_returnsOptimalSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        // Arrange
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] {1, -2}, 0),
                                                    new LinearConstraint[] { 
                                                        new LinearConstraint(new double[] { 1 , 1 }, Relationship.LEQ, 1),
                                                        new LinearConstraint(new double[] { 1 , -1 }, Relationship.LEQ, 2)
                                                    },
                                                    GoalType.MAXIMIZE,
                                                    true, // restrict to non-negative
                                                    0.000001, 
                                                    10);
        
        SimplexSolver solver = new SimplexSolver();
        
        // Act
        PointValuePair solution = solver.doOptimize();
        
        // Assert
        Assert.assertArrayEquals(new double[] {1, 1}, solution.getPoint(), 0.000001);
        Assert.assertEquals(1, solution.getValue(), 0.000001);
    }
}