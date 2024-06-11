package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Assert;
import org.junit.Test;

public class BisectionSolverTest {
// Test constructor with no argument
@Test
public void testBisectionSolverConstruction() {
BisectionSolver solver = new BisectionSolver();
Assert.assertNotNull(solver);
}

// Test constructor with UnivariateRealFunction argument
@Test
public void testBisectionSolverConstructionWithFunction() {
UnivariateRealFunction function = new FunctionWithRoot();
BisectionSolver solver = new BisectionSolver(function);
Assert.assertNotNull(solver);
}

// Test solve method with min, max arguments
@Test
public void testSolveWithMinMaxArguments() throws Exception {
UnivariateRealFunction function = new FunctionWithRoot();
BisectionSolver solver = new BisectionSolver();
double min = 1.0;
double max = 3.0;
double expectedResult = 2.0;
double result = solver.solve(function, min, max);
Assert.assertEquals(expectedResult, result, 0.001);
}

// Test solve method with min, max, initial arguments
@Test
public void testSolveWithMinMaxInitialArguments() throws Exception {
UnivariateRealFunction function = new FunctionWithRoot();
BisectionSolver solver = new BisectionSolver();
double min = 1.0;
double max = 3.0;
double initial = 2.0;
double expectedResult = 2.0;
double result = solver.solve(function, min, max, initial);
Assert.assertEquals(expectedResult, result, 0.001);
}

// Test function with no roots
@Test(expected = FunctionEvaluationException.class)
public void testFunctionWithNoRoots() throws Exception {
UnivariateRealFunction function = new FunctionWithNoRoots();
BisectionSolver solver = new BisectionSolver();
double min = 1.0;
double max = 3.0;
solver.solve(function, min, max);
}

// Test function with multiple roots
@Test(expected = FunctionEvaluationException.class)
public void testFunctionWithMultipleRoots() throws Exception {
UnivariateRealFunction function = new FunctionWithMultipleRoots();
BisectionSolver solver = new BisectionSolver();
double min = 1.0;
double max = 3.0;
solver.solve(function, min, max);
}

// Test function with roots very close to the boundary
@Test
public void testFunctionWithRootsCloseToBoundary() throws Exception {
UnivariateRealFunction function = new FunctionWithRootsCloseToBoundary();
BisectionSolver solver = new BisectionSolver();
double min = 0.001;
double max = 0.002;
double expectedResult = 0.0015;
double result = solver.solve(function, min, max);
Assert.assertEquals(expectedResult, result, 0.0001);
}

// Test function with function evaluation exceptions
@Test(expected = FunctionEvaluationException.class)
public void testFunctionWithFunctionEvaluationException() throws Exception {
UnivariateRealFunction function = new FunctionWithThrowingException();
BisectionSolver solver = new BisectionSolver();
double min = 1.0;
double max = 3.0;
solver.solve(function, min, max);
}

// Test function with MaxIterationsExceededException
@Test(expected = MaxIterationsExceededException.class)
public void testFunctionWithMaxIterationsExceededException() throws Exception {
UnivariateRealFunction function = new FunctionWithMaxIterationsExceeded();
BisectionSolver solver = new BisectionSolver();
double min = 1.0;
double max = 3.0;
solver.setMaximalIterationCount(1);
solver.solve(function, min, max);
}
}

//