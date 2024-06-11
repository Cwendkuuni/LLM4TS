import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link BrentSolver}
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private ComputableFunction function;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    function = mock(ComputableFunction.class);
    checker = mock(ConvergenceChecker.class);
  }

  @After
  public void tearDown() {
    solver = null;
    function = null;
    checker = null;
  }

  /**
   * Test finding root with a simple function
   * @throws FunctionException
   */
  @Test
  public void testFindRootSimpleFunction() throws FunctionException {
    // given
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(-1.0);

    // when
    boolean rootFound = solver.findRoot(function, checker, 100, 0.0, 1.0, 1.0, 0.0);

    // then
    assertEquals(1.0, solver.getRoot(), 0.0);
    assertEquals(true, rootFound);
  }

  /**
   * Test finding root with a complex function
   * @throws FunctionException
   */
  @Test
  public void testFindRootComplexFunction() throws FunctionException {
    // given
    when(function.valueAt(0.0)).thenReturn(Math.exp(1.0) - 2.0);
    when(function.valueAt(1.0)).thenReturn(Math.exp(1.0) - 4.0);

    // when
    boolean rootFound = solver.findRoot(function, checker, 100, 0.0, 1.0, 1.0, 0.0);

    // then
    assertEquals(Math.E - 2.0, solver.getRoot(), 0.0);
    assertEquals(true, rootFound);
  }

  /**
   * Test finding root with a function that doesn't have a root
   * @throws FunctionException
   */
  @Test(expected = FunctionException.class)
  public void testFindRootNoRootFunction() throws FunctionException {
    // given
    when(function.valueAt(0.0)).thenReturn(1.0);
    when(function.valueAt(1.0)).thenReturn(1.0);

    // when
    solver.findRoot(function, checker, 100, 0.0, 1.0, 1.0, 0.0);

    // then (assertion is not needed here as test is expected to throw an exception)
  }

  /**
   * Test finding root with a function with a non-converging interval
   * @throws FunctionException
   */
  @Test(expected = FunctionException.class)
  public void testFindRootNonConvergingIntervalFunction() throws FunctionException {
    // given
    when(function.valueAt(0.0)).thenReturn(0.0);
    when(function.valueAt(1.0)).thenReturn(0.0);

    // when
    solver.findRoot(function, checker, 100, 0.0, 1.0, 1.0, 0.0);

    // then (assertion is not needed here as test is expected to throw an exception)
  }

}