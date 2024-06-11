import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.Constant;
import org.spaceroots.mantissa.functions.scalar.Identity;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRootValid() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    Constant constant = new Constant(1.0);

    assertTrue(solver.findRoot(constant, new ConvergenceChecker(), 100, 0.0, -1.0, 1.0, 1.0));
    assertEquals(1.0, solver.getRoot(), 1e-10);
  }

  @Test
  public void testFindRootInvalid() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    Identity identity = new Identity();

    assertFalse(solver.findRoot(identity, new ConvergenceChecker(), 100, 0.0, 1.0, 2.0, 4.0));
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();

    assertEquals(Double.NaN, solver.getRoot(), 0.0);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootException() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new Constant(0.0), new ConvergenceChecker(), 100, 0.0, 1.0, 2.0, 4.0);
  }
}