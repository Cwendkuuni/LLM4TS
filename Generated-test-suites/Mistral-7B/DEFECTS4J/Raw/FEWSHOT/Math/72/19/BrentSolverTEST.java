package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalaramatica.ComplexComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalaramatica.FunctionWithComplexArgument;
import org.spaceroots.mantissa.functions.scalaramatica.FunctionWithComplexResult;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * JUnit 4 test cases for BrentSolver class
 */
public class BrentSolverTest {

  @Test
  public void testFindRootRealFunction() throws FunctionException {
    ComputableFunction function = new FunctionWithComplexArgument() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - 0.5;
      }
    };
    double x0 = 1.0;
    double x1 = 2.0;
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        return (Math.abs(f) < 0.01) ? ConvergenceChecker.BOTH : ConvergenceChecker.NONE;
      }
    };
    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, 10, x0, Math.sin(x0), x1, Math.sin(x1));
    assertEquals(1.5707963267948966, solver.getRoot(), 0.00001);
  }

  @Test
  public void testFindRootComplexFunction() throws FunctionException {
    ComputableFunction function = new FunctionWithComplexArgument() {
      @Override
      public ComplexDouble valueAt(ComplexDouble z) {
        ComplexDouble c1 = ComplexDouble.valueOf(1.0);
        ComplexDouble c2 = ComplexDouble.valueOf(Math.PI);
        ComplexDouble c3 = z.add(c2);
        ComplexDouble c4 = c1.multiply(c3);
        ComplexDouble c5 = c3.subtract(c1);
        ComplexDouble c6 = c4.divide(c5);
        return c6;
      }
    };
    ComplexDouble z0 = new ComplexDouble(0.0, 0.0);
    ComplexDouble z1 = new ComplexDouble(1.0, 0.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(ComplexDouble x, ComplexDouble f, ComplexDouble x2, ComplexDouble f2) {
        ComplexDouble realDiff = x.subtract(x2).getRealPart();
        ComplexDouble imaginaryDiff = x.subtract(x2).getImaginaryPart();
        double tolerance = 1e-5;
        if (Math.abs(realDiff.getRealPart()) < tolerance && Math.abs(realDiff.getImaginaryPart()) < tolerance
            && Math.abs(f.getRealPart()) < tolerance && Math.abs(f.getImaginaryPart()) < tolerance) {
          return ConvergenceChecker.BOTH;
        }
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver solver = new BrentSolver();
    ComplexDouble root = solver.findRoot(function, checker, 10, z0, function.valueAt(z0), z1, function.valueAt(z1))
        .getRealPart();
    ComplexDouble expectedRoot = new ComplexDouble(0.7125523928717192, 0.5365575933565573);
    assertEquals(expectedRoot.getRealPart(), root, 1e-5);
    assertEquals(expectedRoot.getImaginaryPart(), solver.getRootImaginaryPart(), 1e-5);
  }

  @Test
  public void testFindRootNoRoots() throws FunctionException {
    ComputableFunction function = new FunctionWithComplexArgument() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) + 1.0;
      }
    };
    double x0 = 0.0;
    double x1 = 2.0;
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver solver = new BrentSolver();
    assertFalse(solver.findRoot(function, checker, 10, x0, Math.sin(x0), x1, Math.sin(x1)).isRootFound());
  }

  @Test
  public void testFindRootZeroFunction() throws FunctionException {
    ComputableFunction function = new FunctionWithComplexArgument() {
      @Override
      public double valueAt(double x) {
        return 0.0;
      }
    };
    double x0 = 0.0;
    double x1 = 0.0;
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver solver = new BrentSolver();
    assertEquals(x0, solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)).getRoot(), 0.0);
  }

  @Test
  public void testFindRootMaxIterations() throws FunctionException {
    ComputableFunction function = new FunctionWithComplexArgument() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - 0.5;
      }
    };
    double x0 = 0.0;
    double x1 = 1.0;
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver solver = new BrentSolver();
    assertFalse(solver.findRoot(function, checker, 0, x0, Math.sin(x0), x1, Math.sin(x1)).isRootFound());
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - 0.5;
      }
    }, new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        return ConvergenceChecker.NONE;
      }
    }, 10, 0.0, Math.sin(0.0), 1.0, Math.sin(1.0));
    double expectedRoot = 1.5707963267948966;
    assertEquals(expectedRoot, solver.getRoot(), 0.00001);
  }

  @Test
  public void testGetRootImaginaryPart() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new FunctionWithComplexArgument() {
      @Override
      public ComplexDouble valueAt(ComplexDouble z) {
        ComplexDouble c1 = ComplexDouble.valueOf(1.0);
        ComplexDouble c2 = ComplexDouble.valueOf(Math.PI);
        ComplexDouble c3 = z.add(c2);
        ComplexDouble c4 = c1.multiply(c3);
        ComplexDouble c5 = c3.subtract(c1);
        ComplexDouble c6 = c4.divide(c5);
        return c6;
      }
    };
    ComplexDouble z0 = new ComplexDouble(0.0, 0.0);
    ComplexDouble z1 = new ComplexDouble(1.0, 0.0);
    solver.findRoot(function, new ConvergenceChecker() {
      @Override
      public int converged(ComplexDouble x, ComplexDouble f, ComplexDouble x2, ComplexDouble f2) {
        ComplexDouble realDiff = x.subtract(x2).getRealPart();
        ComplexDouble imaginaryDiff = x.subtract(x2).getImaginaryPart();
        double tolerance = 1e-5;
        if (Math.abs(realDiff.getRealPart()) < tolerance && Math.abs(realDiff.getImaginaryPart()) < tolerance
            && Math.abs(f.getRealPart()) < tolerance && Math.abs(f.getImaginaryPart()) < tolerance) {
          return ConvergenceChecker.BOTH;
        }
        return ConvergenceChecker.NONE;
      }
    }, 10, z0, function.valueAt(z0));
    ComplexDouble expectedRoot = new ComplexDouble(0.7125523928717192, 0.5365575933565573);
    assertEquals(expectedRoot.getRealPart(), solver.getRoot(), 1e-5);
    assertEquals(expectedRoot.getImaginaryPart(), solver.getRootImaginaryPart(), 1e-5);
  }

  @Test
  public void testGetRootNull() {
    BrentSolver solver = new BrentSolver();
    assertNull(solver.getRoot());
  }
}