package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.Path;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

 private CoreOperationGreaterThan operation;
 private Expression arg1;
 private Expression arg2;
 private EvalContext context;

 @Before
 public void setup() {
 arg1 = new CoreConstantExpression(new NodeInfo(new Path(""), new NodeTest(NodeTest.NUMBER, "10.0"), null));
 arg2 = new CoreConstantExpression(new NodeInfo(new Path(""), new NodeTest(NodeTest.NUMBER, "5.0"), null));
 operation = new CoreOperationGreaterThan(arg1, arg2);
 context = new EvalContext(new InfoSet(), null, null);
 }

 @Test
 public void testComputeValue_givenTwoNumbersGreaterThan_shouldReturnTrue() {
 double result = operation.computeValue(context).booleanValue();
 assertTrue(result);
 }

 @Test
 public void testComputeValue_givenTwoNumbersEqual_shouldReturnFalse() {
 arg2 = new CoreConstantExpression(new NodeInfo(new Path(""), new NodeTest(NodeTest.NUMBER, "10.0"), null));
 double result = operation.computeValue(context).booleanValue();
 assertFalse(result);
 }

 @Test
 public void testComputeValue_givenFirstNumberLessThanSecondNumber_shouldReturnFalse() {
 arg2 = new CoreConstantExpression(new NodeInfo(new Path(""), new NodeTest(NodeTest.NUMBER, "6.0"), null));
 double result = operation.computeValue(context).booleanValue();
 assertFalse(result);
 }

 @Test(expected = ClassCastException.class)
 public void testComputeValue_givenFirstNumberIsNotANumber_shouldThrowException() {
 arg1 = new CoreConstantExpression(new NodeInfo(new Path(""), new NodeTest(NodeTest.STRING, "5.0"), null));
 operation.computeValue(context);
 }

 @Test(expected = ClassCastException.class)
 public void testComputeValue_givenSecondNumberIsNotANumber_shouldThrowException() {
 arg2 = new CoreConstantExpression(new NodeInfo(new Path(""), new NodeTest(NodeTest.STRING, "5.0"), null));
 operation.computeValue(context);
 }

 @Test
 public void testGetSymbol() {
 String result = operation.getSymbol();
 assertEquals(">", result);
 }
}