import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.linear.ObjectiveFunction;
import org.apache.commons.math3.optimization.linear.Relationship;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import java.util.ArrayList;
import java.util.List;

public class SimplexSolverTest {
    
    private final double epsilon = 1.0e-6;
    private final int maxUlps = 10;
    
    @Test
    public void testGetPivotColumn() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = createTestTableau();
        
        Integer pivotColumn = simplexSolver.getPivotColumn(tableau);
        
        assertEquals(3, pivotColumn.intValue());
    }
    
    @Test
    public void testGetPivotRow() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = createTestTableau();
        
        Integer pivotRow = simplexSolver.getPivotRow(tableau, 3);
        
        assertEquals(2, pivotRow.intValue());
    }
    
    @Test
    public void testDoIteration() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = createTestTableau();
        double[][] expectedTableauData = {
            {1, -1, -1, -1, -1, 0},
            {0, 1, 0, -1, 0, 2},
            {0, 0, 1, 2, -1, 1},
            {0, 0, 0, 0, 0, 0}
        };
        
        simplexSolver.doIteration(tableau);
        
        assertTableauEquals(expectedTableauData, tableau);
    }
    
    @Test
    public void testSolvePhase1() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = createTestTableau();
        tableau.setNumArtificialVariables(2);
        tableau.initializePhase1();
        double[][] expectedTableauData = {
            {0, 0, 0, 0, 0, 0, 0, -2, -1, 1},
            {1, -1, -1, -1, -1, 0, 0, 1, 0, 0},
            {0, 1, 0, -1, 0, 0, 1, 0, 0, 0},
            {0, 0, 1, 2, -1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        
        try {
            simplexSolver.solvePhase1(tableau);
        } catch (NoFeasibleSolutionException ex) {
            // Ignore the exception since we expect it
        }
        
        assertTableauEquals(expectedTableauData, tableau);
    }
    
    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolution() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = createTestTableau();
        tableau.setNumArtificialVariables(2);
        tableau.initializePhase1();
        
        simplexSolver.solvePhase1(tableau);
    }
    
    @Test
    public void testDoOptimize() {
        SimplexSolver simplexSolver = new SimplexSolver();
        simplexSolver.setEpsilon(1.0e-12);
        simplexSolver.setMaxUlps(100);
        ObjectiveFunction function = new ObjectiveFunction(new double[] { 2, 3 });
        LinearConstraint constraint1 = new LinearConstraint(new double[] { 2, 1 }, Relationship.LEQ, 3);
        LinearConstraint constraint2 = new LinearConstraint(new double[] { -1, 2 }, Relationship.LEQ, 2);
        LinearConstraint constraint3 = new LinearConstraint(new double[] { 1, -1 }, Relationship.LEQ, 3);
        LinearConstraint[] constraints = { constraint1, constraint2, constraint3 };
        PointValuePair expected = new PointValuePair(new double[] { 1, 1 }, 5);
        
        PointValuePair result = simplexSolver.optimize(function, constraints);
        
        assertEquals(expected.getXRef()[0], result.getXRef()[0], epsilon);
        assertEquals(expected.getXRef()[1], result.getXRef()[1], epsilon);
        assertEquals(expected.getY(), result.getY(), epsilon);
    }
    
    private SimplexTableau createTestTableau() {
        double[][] tableauData = {
            {1, -1, -1, -1, -1, 0},
            {1, 2, 1, 1, 0, 4},
            {1, 1, 3, 2, 0, 6},
            {0, 1, 1, 1, 1, 3}
        };
        return new SimplexTableau(tableauData);
    }
    
    private void assertTableauEquals(double[][] expectedTableauData, SimplexTableau tableau) {
        double[][] tableauData = tableau.getData();
        assertEquals(expectedTableauData.length, tableauData.length);
        for (int i = 0; i < expectedTableauData.length; i++) {
            assertEquals(expectedTableauData[i].length, tableauData[i].length);
            for (int j = 0; j < expectedTableauData[i].length; j++) {
                assertEquals(expectedTableauData[i][j], tableauData[i][j], epsilon);
            }
        }
    }
    
}