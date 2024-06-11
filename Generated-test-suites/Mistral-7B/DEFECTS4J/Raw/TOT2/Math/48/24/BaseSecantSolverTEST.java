package org.apache.commons.math.analysis.solvers.test;

 import static org.junit.Assert.*;
 import org.apache.commons.math3.analysis.*;
 import org.apache.commons.math3.analysis.solvers.*;
 import org.apache.commons.math3.exception.ConvergenceException;
 import org.apache.commons.math3.exception.MathIllegalArgumentException;
 import org.apache.commons.math3.exception.NumberIsTooSmallException;
 import org.apache.commons.math3.util.Precision;
 import org.junit.Test;

 public class BaseSecantSolverTest {

 @Test
 public void testSolver_RegulaFalsi() {
 UnivariateRealFunction f = new FunctionWithRoots(-1.0, 1.0, 0.0);
 BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, RegulaFalsiSolver.METHOD);
 double root = solver.solve(10, f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
 assertEquals(0.0, Precision.abs(root), 1e-6);
 }

 @Test(expected = ConvergenceException.class)
 public void testSolver_RegulaFalsi_ConvergenceException() {
 UnivariateRealFunction f = new FunctionWithRoots(0.0, 1.0, 0.0);
 BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-12, RegulaFalsiSolver.METHOD);
 solver.solve(10, f, 0.0, 1.0, AllowedSolution.ANY_SIDE);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testSolver_RegulaFalsi_MathIllegalArgumentException() {
 UnivariateRealFunction f = new FunctionWithRoots(-1.0, 1.0, 0.0);
 BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(-1.0, RegulaFalsiSolver.METHOD);
 solver.solve(10, f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testSolver_RegulaFalsi_NumberIsTooSmallException() {
 UnivariateRealFunction f = new FunctionWithRoots(0.0, 1.0, 0.0);
 BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-16, RegulaFalsiSolver.METHOD);
 solver.solve(10, f, 0.0, 1.0, AllowedSolution.ANY_SIDE);
 }

 @Test
 public void testSolver_Illinois() {
 UnivariateRealFunction f = new FunctionWithRoots(-1.0, 1.0, 0.0);
 BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, IllinoisSolver.METHOD);
 double root = solver.solve(10, f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
 assertEquals(0.0, Precision.abs(root), 1e-6);
 }

 @Test
 public void testSolver_Pegasus() {
 UnivariateRealFunction f = new FunctionWithRoots(0.1, 1.0, 0.0);
 BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, PegasusSolver.METHOD);
 double root = solver.solve(10, f, 0.1, 1.0, AllowedSolution.ANY_SIDE);
 assertEquals(0.0, Precision.abs(root), 1e-6);
 }

 @Test
 public void testSolver_AllowedSolution_LeftSide() {
 UnivariateRealFunction f = new FunctionWithRoots(-1.0, 1.0, 0.0);
 BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, RegulaFalsiSolver.METHOD);
 double root = solver.solve(10, f, -1.0, 1.0, AllowedSolution.LEFT_SIDE);
 assertTrue(root < 0.0);
 }

 @Test
 public void testSolver_AllowedSolution_RightSide() {
 UnivariateRealFunction f = new FunctionWithRoots(-1.0, 1.0, 0.0);
 BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, RegulaFalsiSolver.METHOD);
 double root = solver.solve(10, f, -1.0, 1.0, AllowedSolution.RIGHT_SIDE);
 assertTrue(root > 0.0);
 }

 @Test
 public void testSolver_AllowedSolution_Below() {
 UnivariateRealFunction f = new FunctionWithRoots(0.0, 1.0, 0.0);
 BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, RegulaFalsiSolver.METHOD);
 double root = solver.solve(10, f, -1.0, 1.0, AllowedSolution.BELOW_SIDE);
 assertFalse(root > 0.0);
 }

 @Test
 public void testSolver_AllowedSolution_Above() {
 UnivariateRealFunction f = new FunctionWithRoots(0.0, 1.0, 0.0);
 BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, RegulaFalsiSolver.METHOD);
 double root = solver.solve(10, f, -1.0, 1.0, AllowedSolution.ABOVE_SIDE);
 assertFalse(root < 0.0);
 }

 }

 class FunctionWithRoots implements UnivariateRealFunction {

 private double min;
 private double max;
 private double root;

 public FunctionWithRoots(double min, double max, double root) {
 this.min = min;
 this.max = max;
 this.root = root;
 }

 @Override
 public double value(double x) {
 if (x < min) {
 return Double.POSITIVE_INFINITY;
 } else if (x > max) {
 return Double.NEGATIVE_INFINITY;
 } else {
 return x - root;
 }
 }
 }