import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.SimplexSolver;
import org.apache.commons.math.optimization.linear.AbstractLinearOptimizer;
import org.apache.commons.math.optimization.linear.SimplexTableau;
import org.apache.commons.math.optimization.linear.UnboundedSolutionException;
import org.apache.commons.math.optimization.linear.NoFeasibleSolutionException;
import org.junit.Test;

public class SimplexSolverTest {
    
    private static double EPSILON = 1.0e-6;
    
    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = createSimplexTableau();

        SimplexSolver solver = new SimplexSolver();
        
        assertEquals(new Integer(1), solver.getPivotColumn(tableau));
    }
    
    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = createSimplexTableau();

        SimplexSolver solver = new SimplexSolver();
        
        assertEquals(new Integer(2), solver.getPivotRow(1, tableau));
    }
    
    @Test
    public void testDoIteration() {
        SimplexTableau tableau = createSimplexTableau();

        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doIteration(tableau);
        } catch (OptimizationException e) {
            fail("OptimizationException should not be thrown");
        }
        
        double[][] expectedTableauData = {
            { 1.0, 2.0, 3.0, 4.0 },
            { 5.0, 6.0, 7.0, 8.0 },
            { 9.0, 10.0, 11.0, 12.0 }
        };
        
        assertTrue(compareTableauData(tableau.getTableau(), expectedTableauData));
    }
    
    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = createSimplexTableau();

        SimplexSolver solver = new SimplexSolver();

        assertFalse(solver.isPhase1Solved(tableau));
        
        tableau.divideRow(0, 0.5);
        
        assertTrue(solver.isPhase1Solved(tableau));
    }
    
    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = createSimplexTableau();

        SimplexSolver solver = new SimplexSolver();

        assertFalse(solver.isOptimal(tableau));
        
        tableau.subtractRow(0, 1, 1);
        
        assertTrue(solver.isOptimal(tableau));
    }
    
    @Test
    public void testSolvePhase1WithInfeasibleSolution() {
        SimplexTableau tableau = createSimplexTableau();
        tableau.subtractRow(1, 2, 1);
        tableau.subtractRow(0, 3, 1);

        SimplexSolver solver = new SimplexSolver();
        try {
            solver.solvePhase1(tableau);
            fail("NoFeasibleSolutionException should be thrown");
        } catch (NoFeasibleSolutionException e) {
            // pass
        } catch (OptimizationException e) {
            fail("OptimizationException should not be thrown");
        }
    }
    
    @Test
    public void testSolvePhase1WithFeasibleSolution() {
        SimplexTableau tableau = createSimplexTableau();

        SimplexSolver solver = new SimplexSolver();
        try {
            solver.solvePhase1(tableau);
        } catch (OptimizationException e) {
            fail("OptimizationException should not be thrown");
        }
        
        double[][] expectedTableauData = {
            { 1.0, 0.0, 0.0, 6.0 },
            { 0.0, 1.0, 0.0, 5.0 },
            { 0.0, 0.0, 1.0, 4.0 }
        };
        
        assertTrue(compareTableauData(tableau.getTableau(), expectedTableauData));
    }
    
    @Test
    public void testDoOptimizeWithUnboundedSolution() {
        SimplexTableau tableau = createSimplexTableau();
        
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doOptimize();
            fail("UnboundedSolutionException should be thrown");
        } catch (UnboundedSolutionException e) {
            // pass
        } catch (OptimizationException e) {
            fail("OptimizationException should not be thrown");
        }
    }
    
    @Test
    public void testDoOptimizeWithFeasibleSolution() {
        SimplexTableau tableau = createSimplexTableau();
        tableau.subtractRow(0, 2, 1);

        SimplexSolver solver = new SimplexSolver();
        try {
            RealPointValuePair solution = solver.doOptimize();
            
            double[] expectedSolution = { 6.0, 5.0, 4.0 };
            assertTrue(compareSolution(solution, expectedSolution));
        } catch (OptimizationException e) {
            fail("OptimizationException should not be thrown");
        }
    }
    
    private SimplexTableau createSimplexTableau() {
        double[][] tableauData = {
            { 1.0, 2.0, 3.0, 4.0 },
            { 5.0, 6.0, 7.0, 8.0 },
            { 9.0, 10.0, 11.0, 12.0 }
        };
        
        return new SimplexTableau(tableauData);
    }
    
    private boolean compareTableauData(double[][] actualTableau, double[][] expectedTableau) {
        if (actualTableau.length != expectedTableau.length) {
            return false;
        }
        
        for (int i = 0; i < actualTableau.length; i++) {
            if (actualTableau[i].length != expectedTableau[i].length) {
                return false;
            }
            
            for (int j = 0; j < actualTableau[i].length; j++) {
                if (!MathUtils.equals(actualTableau[i][j], expectedTableau[i][j], EPSILON)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private boolean compareSolution(RealPointValuePair actualSolution, double[] expectedSolution) {
        if (actualSolution.getPoint().length != expectedSolution.length) {
            return false;
        }
        
        for (int i = 0; i < actualSolution.getPoint().length; i++) {
            if (!MathUtils.equals(actualSolution.getPoint()[i], expectedSolution[i], EPSILON)) {
                return false;
            }
        }
        
        return true;
    }
}