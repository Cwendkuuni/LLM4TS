package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

 @Test
 public void testBrentSolverConstructor() {
 BrentSolver brentSolver = new BrentSolver();
 assertDoubleNaN(brentSolver.getRoot());
 }

 // Test cases for the findRoot method go here

}