package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationEqualTest {

 private CoreOperationEqual coreOperationEqual;
 private Expression arg1;
 private Expression arg2;
 private EvalContext context;

 @Before
 public void setUp() {
 arg1 = Mockito.mock(Expression.class);
 arg2 = Mockito.mock(Expression.class);
 context = Mockito.mock(EvalContext.class);
 coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 }

 @Test
 public void testComputeValue_whenBothArgsAreBooleanAndEqual_thenResultIsTrue() {
 Mockito.when(arg1.computeValue(context)).thenReturn(Boolean.TRUE);
 Mockito.when(arg2.computeValue(context)).thenReturn(Boolean.TRUE);

 Object result = coreOperationEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComputeValue_whenBothArgsAreBooleanAndNotEqual_thenResultIsFalse() {
 Mockito.when(arg1.computeValue(context)).thenReturn(Boolean.TRUE);
 Mockito.when(arg2.computeValue(context)).thenReturn(Boolean.FALSE);

 Object result = coreOperationEqual.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);

 Mockito.verify(context, Mockito.times(2)).setLastValue(Mockito.anyBoolean());
 }

 @Test
 public void testComputeValue_whenFirstArgIsBooleanAndSecondArgIsNumber_thenResultIsFalse() {
 Mockito.when(arg1.computeValue(context)).thenReturn(Boolean.TRUE);
 Mockito.when(arg2.computeValue(context)).thenReturn(5.0);

 Object result = coreOperationEqual.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 @Test
 public void testComputeValue_whenFirstArgIsNumberAndSecondArgIsBoolean_thenResultIsFalse() {
 Mockito.when(arg1.computeValue(context)).thenReturn(5.0);
 Mockito.when(arg2.computeValue(context)).thenReturn(Boolean.TRUE);

 Object result = coreOperationEqual.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 @Test
 public void testComputeValue_whenBothArgsAreNull_thenResultIsFalse() {
 Mockito.when(arg1.computeValue(context)).thenReturn(null);
 Mockito.when(arg2.computeValue(context)).thenReturn(null);

 Object result = coreOperationEqual.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 @Test
 public void testComputeValue_whenBothArgsAreNotNull_thenResultIsTrue() {
 Mockito.when(arg1.computeValue(context)).thenReturn("test");
 Mockito.when(arg2.computeValue(context)).thenReturn("test");

 Object result = coreOperationEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testGetSymbol_thenReturnEqualSymbol() {
 String symbol = coreOperationEqual.getSymbol();
 assertEquals("=", symbol);
 }
}