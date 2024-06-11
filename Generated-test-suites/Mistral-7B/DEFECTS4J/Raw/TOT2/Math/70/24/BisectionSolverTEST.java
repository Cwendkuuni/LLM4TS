package org.junit.Test;
import static org.junit.Assert.assertNotNull;
import org.junit.Assert;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;

public class BisectionSolverTest {

@Test
public void testBisectionSolverConstruction() {
BisectionSolver solver = new BisectionSolver();
assertNotNull(solver);
}

@Test
public void testBisectionSolverConstructionFunction() {
UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
BisectionSolver solver = new BisectionSolver(f);
assertNotNull(solver);
}

@Test(expected = FunctionEvaluationException.class)
public void testBisectionSolverFunctionValueAtMin() throws FunctionEvaluationException {
UnivariateRealFunction f = new PolynomialFunction(new double[]{1, -1, 1}, new double[]{0, -1, 1});
BisectionSolver solver = new BisectionSolver();
double min = 0;
double max = 1;
solver.solve(f, min, max);
}

@Test
public void testBisectionSolverSameMinMax() {
UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
BisectionSolver solver = new BisectionSolver();
double min = 0;
double max = 0;
try {
solver.solve(f, min, max);
Assert.fail("Expected MaxIterationsExceededException or FunctionEvaluationException");
} catch (MaxIterationsExceededException e) {
// Expected result
} catch (FunctionEvaluationException e) {
Assert.fail("Unexpected FunctionEvaluationException");
}
}

@Test(expected = MaxIterationsExceededException.class)
public void testBisectionSolverMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
UnivariateRealFunction f = new PolynomialFunction(new double[]{1, -1, 1}, new double[]{0, -1, 1});
BisectionSolver solver = new BisectionSolver();
double min = 0;
double max = 1;
solver.setMaximalIterationCount(0);
solver.solve(f, min, max);
}

@Test(expected = ArithmeticException.class)
public void testBisectionSolverArithmeticException() throws MaxIterationsExceededException, FunctionEvaluationException {
UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
BisectionSolver solver = new BisectionSolver();
double min = 0;
double max = 1;
try {
solver.solve(f, min, max);
Assert.fail("Expected ArithmeticException");
} catch (ArithmeticException e) {
// Expected result
}
}
}