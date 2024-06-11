package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.Node;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {
 CoreOperationLessThanOrEqual coreOperationLessThanOrEqual;

 @Before
 public void setUp() {
 coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(2.0));
 }

 @Test
 public void testCoreOperationLessThanOrEqual() {
 assertNotNull(coreOperationLessThanOrEqual);
 }

 @Test
 public void testComputeValueTrue() {
 EvalContext context = new EvalContext();
 context.setCurrentNode(new Node());
 when(context.getInfoSet().getStringData("test")).thenReturn("1.5");
 when(context.getInfoSet().getNumberData("test2")).thenReturn(2.5);
 double result = coreOperationLessThanOrEqual.computeValue(context);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComputeValueFalse() {
 EvalContext context = new EvalContext();
 context.setCurrentNode(new Node());
 when(context.getInfoSet().getStringData("test")).thenReturn("1.5");
 when(context.getInfoSet().getNumberData("test2")).thenReturn(3.5);
 double result = coreOperationLessThanOrEqual.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testGetSymbol() {
 String symbol = coreOperationLessThanOrEqual.getSymbol();
 assertEquals("<=", symbol);
 }

 @Test
 public void testComputeValueNaN() {
 EvalContext context = new EvalContext();
 context.setCurrentNode(new Node());
 when(context.getInfoSet().getStringData("test")).thenReturn("NaN");
 when(context.getInfoSet().getNumberData("test2")).thenReturn(Double.NaN);
 double result = coreOperationLessThanOrEqual.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValueInfinity() {
 EvalContext context = new EvalContext();
 context.setCurrentNode(new Node());
 when(context.getInfoSet().getStringData("test")).thenReturn("Inf");
 when(context.getInfoSet().getNumberData("test2")).thenReturn("Inf");
 double result = coreOperationLessThanOrEqual.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValueNegativeNumbers() {
 EvalContext context = new EvalContext();
 context.setCurrentNode(new Node());
 when(context.getInfoSet().getStringData("test")).thenReturn("test");
 when(context.getInfoSet().getNumberData("test2")).thenReturn(-8.0);
 double result = coreOperationLessThanOrEqual.computeValue(context);
 assertEquals(Boolean.TRUE, result);
 }
}