package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ComputableFunction;
import org.spaceroots.mantissa.roots.RootsFinder;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.roots.FunctionAdd;
import org.spaceroots.mantissa.roots.FunctionSubtract;
import org.spaceroots.mantissa.roots.FunctionNonConverging;
import org.spaceroots.mantissa.roots.FunctionMultipleRoots;
import org.spaceroots.mantissa.roots.FunctionMultiply;
import org.spaceroots.mantissa.roots.ConvergenceCheckerLinear;
import org.spaceroots.mantissa.roots.ConvergenceCheckerQuadratic;
import org.spaceroots.mantissa.roots.ConvergenceCheckerCubic;

public class BrentSolverTest {

 @Test
 public void testFindRootValidInput() throws FunctionException {
 // Test 1
 }

 @Test
 public void testFindRootSameX() throws FunctionException {
 // Test 2
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRootZeroIterations() throws FunctionException {
 // Test 3
 }

 @Test
 public void testFindRootValidInputB() throws FunctionException {
 // Test 4
 }

 @Test
 public void testFindRootNonConvergingFunction() throws FunctionException {
 // Test 5
 }

 @Test(expected = NullPointerException.class)
 public void testFindRootNullChecker() throws FunctionException {
 // Test 6
 }

 @Test
 public void testFindRootValidInputC() throws FunctionException {
 // Test 7
 }

 @Test
 public void testFindRootMultipleRoots() throws FunctionException {
 // Test 8
 }

 @Test(expected = NullPointerException.class)
 public void testFindRootNullFunction() throws FunctionException {
 // Test 9
 }

}