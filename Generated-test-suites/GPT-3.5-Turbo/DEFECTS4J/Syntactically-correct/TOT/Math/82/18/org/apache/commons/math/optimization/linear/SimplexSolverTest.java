import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.linear.SimplexSolver;
import org.apache.commons.math.optimization.linear.SimplexTableau;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {

    private SimplexSolver simplexSolver;

    @Before
    public void setUp() {
        simplexSolver = new SimplexSolver();
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = createTableau(3, 4, new double[][] {
            {1, -2, 3, -4, 5},
            {6, -7, 8, -9, 10},
            {11, -12, 13, -14, 15}
        });

        assertEquals(Integer.valueOf(1), simplexSolver.getPivotColumn(tableau));
    }

    @Test
    public void testGetPivotColumn_NoNegativeCoefficient() {
        SimplexTableau tableau = createTableau(2, 3, new double[][] {
            {-1, -2, -3},
            {-4, -5, -6},
            {-7, -8, -9}
        });

        assertNull(simplexSolver.getPivotColumn(tableau));
    }

    @Test
    public void testGetPivotColumn_AllNegativeCoefficients() {
        SimplexTableau tableau = createTableau(2, 3, new double[][] {
            {-1, -2, -3},
            {-4, -5, -6},
            {-7, -8, -9}
        });

        assertNull(simplexSolver.getPivotColumn(tableau));
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = createTableau(3, 4, new double[][] {
            {1, -2, 3, -4, 5},
            {6, -7, 8, -9, 10},
            {11, -12, 13, -14, 15}
        });

        assertEquals(Integer.valueOf(2), simplexSolver.getPivotRow(1, tableau));
    }

    @Test
    public void testGetPivotRow_NoPositiveEntry() {
        SimplexTableau tableau = createTableau(2, 3, new double[][] {
            {-1, -2, -3},
            {-4, -5, -6},
            {-7, -8, -9}
        });

        assertNull(simplexSolver.getPivotRow(0, tableau));
    }

    @Test
    public void testGetPivotRow_AllPositiveEntries() {
        SimplexTableau tableau = createTableau(2, 3, new double[][] {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        });

        assertNull(simplexSolver.getPivotRow(1, tableau));
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = createTableau(3, 4, new double[][] {
            {1, -2, 3, -4, 5},
            {6, -7, 8, -9, 10},
            {11, -12, 13, -14, 15}
        });

        simplexSolver.doIteration(tableau);

        double[][] expectedTableauData = {
            {1, 0, 0.4, -0.4, 0.6},
            {0, 1, -1.6, 1.4, 2.4},
            {0, 0, 2.4, -2.4, 0.6}
        };
        assertTableauEquals(expectedTableauData, tableau);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIteration_UnboundedSolutionException() throws OptimizationException {
        SimplexTableau tableau = createTableau(2, 3, new double[][] {
            {1, 1, 1, 1},
            {-1, -1, -1, -1}
        });

        simplexSolver.doIteration(tableau);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = createTableau(3, 4, new double[][] {
            {1, -2, 3, -4, 5},
            {6, -7, 8, -9, 10},
            {11, -12, 13, -14, 15}
        });

        assertFalse(simplexSolver.isPhase1Solved(tableau));
    }

    @Test
    public void testIsPhase1Solved_AllPositiveCoefficients() {
        SimplexTableau tableau = createTableau(2, 3, new double[][] {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        });

        assertTrue(simplexSolver.isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = createTableau(3, 4, new double[][] {
            {1, -2, 3, -4, 5},
            {6, -7, 8, -9, 10},
            {11, -12, 13, -14, 15}
        });

        assertFalse(simplexSolver.isOptimal(tableau));
    }

    @Test
    public void testIsOptimal_AllPositiveCoefficients() {
        SimplexTableau tableau = createTableau(2, 3, new double[][] {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        });

        assertTrue(simplexSolver.isOptimal(tableau));
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexTableau tableau = createTableau(4, 5, new double[][] {
            {0, -1, 2, -3, 4, 5},
            {-6, 7, -8, 9, -10, 11},
            {12, -13, 14, -15, 16, 17},
            {18, -19, 20, -21, 22, 23}
        });

        simplexSolver.solvePhase1(tableau);

        double[][] expectedTableauData = {
            {1, 0, -0.5, 0.5, -0.5, 0.5},
            {0, 1, -2, 3, -4, 5},
            {0, 0, 0, 0, 0, 1},
            {0, 0, 0.5, -0.5, 0.5, 0.5}
        };
        assertTableauEquals(expectedTableauData, tableau);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1_NoFeasibleSolutionException() throws OptimizationException {
        SimplexTableau tableau = createTableau(2, 3, new double[][] {
            {1, 1, 1},
            {-1, -2, -3}
        });

        simplexSolver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        simplexSolver = new SimplexSolver(1.0e-10);

        SimplexTableau tableau = createTableau(4, 5, new double[][] {
            {0, -1, 2, -3, 4, 5},
            {-6, 7, -8, 9, -10, 11},
            {12, -13, 14, -15, 16, 17},
            {18, -19, 20, -21, 22, 23}
        });

        double[] f = {1, -2, 3, -4, 5};
        double[][] constraints = {
            {-6, 7, -8, 9, -10, 11},
            {12, -13, 14, -15, 16, 17},
            {18, -19, 20, -21, 22, 23}
        };

        double[] expectedSolution = {0.5, 1.5, 2.5, 3.5};
        double expectedValue = -0.5;

        RealPointValuePair result = simplexSolver.doOptimize();

        assertArrayEquals(expectedSolution, result.getPoint(), 1.0e-10);
        assertEquals(expectedValue, result.getValue(), 1.0e-10);
    }

    private void assertTableauEquals(double[][] expectedData, SimplexTableau actualTableau) {
        double[][] actualData = actualTableau.getData();
        assertEquals(expectedData.length, actualData.length);
        for (int i = 0; i < expectedData.length; i++) {
            assertArrayEquals(expectedData[i], actualData[i], 1.0e-10);
        }
    }

    private SimplexTableau createTableau(int numRows, int numColumns, double[][] data) {
        SimplexTableau tableau = new SimplexTableau(new double[numColumns][numRows + 1]);

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                tableau.setEntry(i, j, data[i][j]);
            }
        }

        return tableau;
    }

}