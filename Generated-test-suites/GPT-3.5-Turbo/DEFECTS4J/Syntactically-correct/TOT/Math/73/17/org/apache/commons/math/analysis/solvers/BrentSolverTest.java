import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class BrentSolverTest {

   @Mock
   private ComputableFunction functionMock;

   @Mock
   private ConvergenceChecker checkerMock;

   @Test
   public void testFindRoot_WhenRootFound_ShouldReturnTrue() throws FunctionException {
      // Arrange
      BrentSolver solver = new BrentSolver();
      double x0 = 1.0;
      double f0 = 0.0;
      double x1 = 2.0;
      double f1 = 0.0;

      when(functionMock.valueAt(x0)).thenReturn(f0);
      when(functionMock.valueAt(x1)).thenReturn(f1);
      when(checkerMock.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.HIGH);

      // Act
      boolean result = solver.findRoot(functionMock, checkerMock, 100, x0, f0, x1, f1);

      // Assert
      assertTrue(result);
      assertEquals(x1, solver.getRoot(), 0.0001);
   }

   @Test
   public void testFindRoot_WhenNoRootFound_ShouldReturnFalse() throws FunctionException {
      // Arrange
      BrentSolver solver = new BrentSolver();
      double x0 = 1.0;
      double f0 = 0.0;
      double x1 = 2.0;
      double f1 = 0.0;

      when(functionMock.valueAt(x0)).thenReturn(f0);
      when(functionMock.valueAt(x1)).thenReturn(f1);
      when(checkerMock.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.NO_CONVERGENCE);

      // Act
      boolean result = solver.findRoot(functionMock, checkerMock, 100, x0, f0, x1, f1);

      // Assert
      assertFalse(result);
   }

   @Test
   public void testFindRoot_WhenConvergenceLow_ShouldReturnTrue() throws FunctionException {
      // Arrange
      BrentSolver solver = new BrentSolver();
      double x0 = 1.0;
      double f0 = 0.0;
      double x1 = 2.0;
      double f1 = 0.0;

      when(functionMock.valueAt(x0)).thenReturn(f0);
      when(functionMock.valueAt(x1)).thenReturn(f1);
      when(checkerMock.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.LOW);

      // Act
      boolean result = solver.findRoot(functionMock, checkerMock, 100, x0, f0, x1, f1);

      // Assert
      assertTrue(result);
      assertEquals(x0, solver.getRoot(), 0.0001);
   }

   @Test
   public void testFindRoot_WhenConvergenceHigh_ShouldReturnTrue() throws FunctionException {
      // Arrange
      BrentSolver solver = new BrentSolver();
      double x0 = 1.0;
      double f0 = 0.0;
      double x1 = 2.0;
      double f1 = 0.0;

      when(functionMock.valueAt(x0)).thenReturn(f0);
      when(functionMock.valueAt(x1)).thenReturn(f1);
      when(checkerMock.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.HIGH);

      // Act
      boolean result = solver.findRoot(functionMock, checkerMock, 100, x0, f0, x1, f1);

      // Assert
      assertTrue(result);
      assertEquals(x1, solver.getRoot(), 0.0001);
   }

   @Test
   public void testFindRoot_WhenInterpolationAcceptable_ShouldReturnTrue() throws FunctionException {
      // Arrange
      BrentSolver solver = new BrentSolver();
      double x0 = 1.0;
      double f0 = 0.0;
      double x1 = 2.0;
      double f1 = 0.0;

      when(functionMock.valueAt(x0)).thenReturn(f0);
      when(functionMock.valueAt(x1)).thenReturn(f1);
      when(checkerMock.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.NO_CONVERGENCE);
      when(functionMock.valueAt(eq(1.5))).thenReturn(0.2);

      // Act
      boolean result = solver.findRoot(functionMock, checkerMock, 100, x0, f0, x1, f1);

      // Assert
      assertTrue(result);
      assertEquals(1.5, solver.getRoot(), 0.0001);
   }

   @Test
   public void testFindRoot_WhenInterpolationNotAcceptable_ShouldReturnTrue() throws FunctionException {
      // Arrange
      BrentSolver solver = new BrentSolver();
      double x0 = -1.0;
      double f0 = -0.5;
      double x1 = 1.0;
      double f1 = 0.5;

      when(functionMock.valueAt(x0)).thenReturn(f0);
      when(functionMock.valueAt(x1)).thenReturn(f1);
      when(checkerMock.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.NO_CONVERGENCE);
      when(functionMock.valueAt(eq(0.0))).thenReturn(1.0);

      // Act
      boolean result = solver.findRoot(functionMock, checkerMock, 100, x0, f0, x1, f1);

      // Assert
      assertTrue(result);
      assertEquals(0.0, solver.getRoot(), 0.0001);
   }

   @Test
   public void testFindRoot_WhenIterationsExceeded_ShouldReturnFalse() throws FunctionException {
      // Arrange
      BrentSolver solver = new BrentSolver();
      double x0 = -1.0;
      double f0 = -0.5;
      double x1 = 1.0;
      double f1 = 0.5;

      when(functionMock.valueAt(x0)).thenReturn(f0);
      when(functionMock.valueAt(x1)).thenReturn(f1);
      when(checkerMock.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.NO_CONVERGENCE);
      when(functionMock.valueAt(eq(0.0))).thenReturn(0.1);

      // Act
      boolean result = solver.findRoot(functionMock, checkerMock, 5, x0, f0, x1, f1);

      // Assert
      assertFalse(result);
   }
}