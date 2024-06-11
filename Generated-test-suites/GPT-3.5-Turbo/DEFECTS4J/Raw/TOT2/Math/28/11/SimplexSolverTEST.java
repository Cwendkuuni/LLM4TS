import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.linear.UnboundedSolutionException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.PointValuePair;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] {1, 1}, 0),
                                                   null, GoalType.MAXIMIZE, false, 1.0e-6, 10);
        SimplexSolver solver = new SimplexSolver();

        Integer pivotCol = solver.getPivotColumn(tableau);

        assertNotNull(pivotCol);
        assertEquals(2, pivotCol.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] {1, 1}, 0),
                                                   null, GoalType.MAXIMIZE, false, 1.0e-6, 10);
        SimplexSolver solver = new SimplexSolver();

        Integer pivotRow = solver.getPivotRow(tableau, 2);

        assertNotNull(pivotRow);
    }

    @Test
    public void testDoOptimize() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[] {1, 1}, 0);
        SimplexTableau tableau = new SimplexTableau(f, null, GoalType.MAXIMIZE, false, 1.0e-6, 10);
        SimplexSolver solver = new SimplexSolver();

        PointValuePair result = null;
        try {
            result = solver.doOptimize();
        } catch (MaxCountExceededException e) {
            e.printStackTrace();
        } catch (UnboundedSolutionException e) {
            e.printStackTrace();
        } catch (NoFeasibleSolutionException e) {
            e.printStackTrace();
        }

        assertNotNull(result);
        RealVector solution = result.getPoint();
        assertEquals(0.0, f.getValue(solution), 1.0e-6);
    }

}