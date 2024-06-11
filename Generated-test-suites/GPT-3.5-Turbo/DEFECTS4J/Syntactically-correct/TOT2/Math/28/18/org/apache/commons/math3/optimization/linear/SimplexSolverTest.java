import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.Relationship;
import org.apache.commons.math3.optimization.linear.PolynomialFunction;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.linear.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.UnboundedSolutionException;

public class SimplexSolverTest {

    private SimplexSolver simplexSolver;

    @Before
    public void setUp() {
        simplexSolver = new SimplexSolver();
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1, 0}, 0),
                                                    new ArrayList<PolynomialFunction>(),
                                                    Relationship.EQ,
                                                    false,
                                                    1.0e-6,
                                                    10);
                                                    
        assertEquals(new Integer(1), simplexSolver.getPivotColumn(tableau));
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1, 0}, 0),
                                                    new ArrayList<PolynomialFunction>(),
                                                    Relationship.EQ,
                                                    false,
                                                    1.0e-6,
                                                    10);
                                                    
        assertEquals(new Integer(1), simplexSolver.getPivotRow(tableau, 1));
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1, 0}, 0),
                                                    new ArrayList<PolynomialFunction>(),
                                                    Relationship.EQ,
                                                    false,
                                                    1.0e-6,
                                                    10);

        simplexSolver.doIteration(tableau);

        assertTrue(tableau.isOptimal());
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1, 0}, 0),
                                                    new ArrayList<PolynomialFunction>(),
                                                    Relationship.EQ,
                                                    false,
                                                    1.0e-6,
                                                    10);

        PointValuePair result = simplexSolver.doOptimize(tableau);

        assertEquals(0.0, result.getValue(), 1.0e-6);
    }

}