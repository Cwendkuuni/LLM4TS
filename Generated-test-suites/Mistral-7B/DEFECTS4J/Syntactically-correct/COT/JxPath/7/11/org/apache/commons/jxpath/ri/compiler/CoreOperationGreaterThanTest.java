package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.test.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class CoreOperationGreaterThanTest {

 private CoreOperationGreaterThan operation;
 private Expression arg1;
 private Expression arg2;
 private Double result;
 private Boolean expected;

 public CoreOperationGreaterThanTest(Double arg1, Double arg2, Boolean expected) {
 this.arg1 = new CoreNumberExpression(arg1);
 this.arg2 = new CoreNumberExpression(arg2);
 this.operation = new CoreOperationGreaterThan(arg1, arg2);
 this.result = null;
 this.expected = expected;
 }

 @Parameterized.Parameters(name = "{index}: {0} > {1} = {2}")
 public static Collection<Object[]> testData() {
 return Arrays.asList(new Object[][] {
 // Test cases for double values
 { 1.0d, 0.5d, true },
 { 1.0d, 1.0d, false },
 { 1.0d, 2.0d, true },
 { 0.5d, 1.0d, false },
 { 0.5d, 0.5d, false },
 { -1.0d, 1.0d, true },
 { -1.0d, -1.0d, false },
 { Double.NaN, Double.NaN, false },
 { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, false },
 { Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, false },
 // Test cases for null arguments
 { null, 1.0d, false },
 { 1.0d, null, false },
 { null, null, false }
 });
 }

 @Test
 public void testComputeValue() {
 try {
 InfoSet infoSet = TestUtils.createInfoSet();
 EvalContext context = new EvalContext();
 context.setInfoSet(infoSet);
 this.result = (Boolean) operation.computeValue(context);
 } catch (Exception e) {
 fail("Exception was thrown: " + e.getMessage());
 }

 assertTrue("Incorrect result: " + this.result + ", expected: " + this.expected, this.result.equals(this.expected));
}

 @Test(expected = ClassCastException.class)
 public void testComputeValueWithMismatchedTypes() {
 try {
 InfoSet infoSet = TestUtils.createInfoSet();
 EvalContext context = new EvalContext();
 context.setInfoSet(infoSet);
 operation.computeValue(context);
 fail("Expected ClassCastException not thrown");
 } catch (ClassCastException e) {
 // Expected
 }
 }

 @Test(expected = RuntimeException.class)
 public void testComputeValueWithNullArguments() {
 try {
 InfoSet infoSet = TestUtils.createInfoSet();
 EvalContext context = new EvalContext();
 context.setInfoSet(infoSet);
 operation.computeValue(context);
 fail("Expected RuntimeException not thrown");
 } catch (RuntimeException e) {
 // Expected
 }
 }
}