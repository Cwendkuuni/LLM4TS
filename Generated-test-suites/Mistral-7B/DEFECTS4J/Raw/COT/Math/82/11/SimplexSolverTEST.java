package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class SimplexSolverTest {

 private SimplexSolver simplexSolver;
 private SimplexTableau tableauMock;

 @Before
 public void setUp() {
 simplexSolver = new SimplexSolver(0.000001);
 tableauMock = Mockito.mock(SimplexTableau.class);
 }

 @Test
 public void testGetPivotColumn() {
 // Given: A simple tableau with some values
 Mockito.when(tableauMock.getEntry(0, 0)).thenReturn(-1.0);
 Mockito.when(tableauMock.getEntry(0, 1)).thenReturn(-2.0);
 Mockito.when(tableauMock.getEntry(0, 2)).thenReturn(-3.0);
 Mockito.when(tableauMock.getEntry(0, 3)).thenReturn(-4.0);
 Mockito.when(tableauMock.getWidth()).thenReturn(4);

 // When: The pivot column is retrieved
 Integer pivotColumn = simplexSolver.getPivotColumn(tableauMock);

 // Then: The pivot column is the one with the most negative coefficient
 assertEquals(0, pivotColumn.intValue());
 }

 @Test
 public void testGetPivotRowNull() {
 // Given: A simple tableau with some values
 Mockito.when(tableauMock.getEntry(0, 0)).thenReturn(-1.0);
 Mockito.when(tableauMock.getEntry(0, 1)).thenReturn(-2.0);
 Mockito.when(tableauMock.getEntry(1, 1)).thenReturn(-3.0);
 Mockito.when(tableauMock.getWidth()).thenReturn(3);

 // When: The pivot row is null
 assertNull(simplexSolver.getPivotRow(0, tableauMock));
 }

 @Test
 public void testGetPivotRow() {
 // Given: A simple tableau with some values
 Mockito.when(tableauMock.getEntry(0, 0)).thenReturn(1.0);
 Mockito.when(tableauMock.getEntry(0, 1)).thenReturn(-2.0);
 Mockito.when(tableauMock.getEntry(1, 1)).thenReturn(3.0);
 Mockito.when(tableauMock.getWidth()).thenReturn(3);

 // When: The pivot row is retrieved
 Integer pivotRow = simplexSolver.getPivotRow(0, tableauMock);

 // Then: The pivot row is the one with the minimum ratio
 assertEquals(1, pivotRow.intValue());
 }

 @Test(expected = UnboundedSolutionException.class)
 public void testDoIterationThrowsUnboundedSolutionException() {
 // Given: A simple tableau with some values
 Mockito.when(tableauMock.getEntry(0, 0)).thenReturn(1.0);
 Mockito.when(tableauMock.getEntry(0, 1)).thenReturn(Double.NEGATIVE_INFINITY);
 Mockito.when(tableauMock.getHeight()).thenReturn(2);

 // When: An iteration is performed
 simplexSolver.doIteration(tableauMock);
 }

 @Test(expected = NoFeasibleSolutionException.class)
 public void testSolvePhase1ThrowsNoFeasibleSolutionException() {
 // Given: A simple tableau with some values
 Mockito.when(tableauMock.getEntry(0, 0)).thenReturn(1.0);
 Mockito.when(tableauMock.getEntry(0, 1)).thenReturn(-2.0);
 Mockito.when(tableauMock.getEntry(0, 2)).thenReturn(1.0);
 Mockito.when(tableauMock.getEntry(1, 1)).thenReturn(1.0);
 Mockito.when(tableauMock.getEntry(1, 2)).thenReturn(-1.0);
 Mockito.when(tableauMock.getRhsOffset()).thenReturn(1.0);
 Mockito.when(tableauMock.getWidth()).thenReturn(3);

 // When: Phase 1 is solved
 simplexSolver.solvePhase1(tableauMock);
 }

 @Test
 public void testIsPhase1Solved() {
 // Given: A simple tableau with some values
 Mockito.when(tableauMock.getNumArtificialVariables()).thenReturn(1);
 Mockito.when(tableauMock.getEntry(0, 0)).thenReturn(-1.0);
 Mockito.when(tableauMock.getEntry(0, 1)).thenReturn(-2.0);
 Mockito.when(tableauMock.getEntry(0, 2)).thenReturn(-3.0);
 Mockito.when(tableauMock.getEntry(0, 3)).thenReturn(-4.0);
 Mockito.when(tableauMock.getWidth()).thenReturn(4);

 // When: Phase 1 is checked for being solved
 boolean phase1Solved = simplexSolver.isPhase1Solved(tableauMock);

 // Then: Phase 1 is not solved
 assertFalse(phase1Solved);
 }

 @Test
 public void testIsOptimal() {
 // Given: A simple tableau with some values
 Mockito.when(tableauMock.getNumArtificialVariables()).thenReturn(0);
 Mockito.when(tableauMock.getEntry(0, 0)).thenReturn(-1.0);
 Mockito.when(tableauMock.getEntry(0, 1)).thenReturn(-2.0);
 Mockito.when(tableauMock.getEntry(0, 2)).thenReturn(-3.0);
 Mockito.when(tableauMock.getEntry(0, 3)).thenReturn(-4.0);
 Mockito.when(tableauMock.getWidth()).thenReturn(4);

 // When: The problem is checked for being optimal
 boolean optimal = simplexSolver.isOptimal(tableauMock);

 // Then: The problem is optimal
 assertTrue(optimal);
 }

 @Test
 public void testDoOptimize() {
 // Given: A simple optimization problem with some values
 double[][] f = {{-1.0, -2.0, -3.0}, {-4.0, -5.0, -6.0}};
 double[][] constraints = {{-1.0, 1.0, 2.0, 3.0}, {-2.0, 1.0, 1.0, 2.0}, {-3.0, 1.0, 1.0, 1.0}};
 LinearObjectiveFunction goal = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 1.0);
 boolean restrictToNonNegative = false;

 // When: The optimization is performed
 RealPointValuePair solution = simplexSolver.doOptimize();

 // Then: The optimization returns a valid solution
 assertEquals(2, solution.getNumberOfPoints());
 assertEquals(Precision.round(Arrays.asList(1.0, 1.0).get(0), 5), solution.getPoint(0).getComponent(0), 0.00001);
 assertEquals(Precision.round(Arrays.asList(1.0, 1.0).get(1), 5), solution.getPoint(0).getComponent(1), 0.00001);
 assertEquals(Precision.round(1.0, 5), solution.getValue(), 0.00001);
 }
}