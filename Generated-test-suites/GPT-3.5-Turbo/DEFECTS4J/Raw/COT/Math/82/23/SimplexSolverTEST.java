import org.junit.Test;

public class SimplexSolverTest {

  @Test
  public void getPivotColumnTest() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

    Integer pivotCol = solver.getPivotColumn(tableau);

    // assertions
    // ...
  }

  @Test
  public void getPivotRowTest() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

    Integer pivotRow = solver.getPivotRow(col, tableau);

    // assertions
    // ...
  }

  @Test
  public void doIterationTest() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

    solver.doIteration(tableau);

    // assertions
    // ...
  }

  @Test
  public void isPhase1SolvedTest() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

    boolean isSolved = solver.isPhase1Solved(tableau);

    // assertions
    // ...
  }

  @Test
  public void isOptimalTest() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

    boolean isOptimal = solver.isOptimal(tableau);

    // assertions
    // ...
  }

  @Test
  public void solvePhase1Test() {
    SimplexSolver solver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

    solver.solvePhase1(tableau);

    // assertions
    // ...
  }

  @Test
  public void doOptimizeTest() {
    SimplexSolver solver = new SimplexSolver();
    RealPointValuePair result = solver.doOptimize();

    // assertions
    // ...
  }
}