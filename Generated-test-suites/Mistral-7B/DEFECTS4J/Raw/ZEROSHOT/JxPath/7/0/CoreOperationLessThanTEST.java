package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanTest {

 private CoreOperationLessThan operation;
 private Expression arg1;
 private Expression arg2;
 private EvalContext context;

 @Before
 public void setup() {
 arg1 = Mockito.mock(Expression.class);
 arg2 = Mockito.mock(Expression.class);
 context = Mockito.spy(new EvalContext());
 operation = new CoreOperationLessThan(arg1, arg2);
 }

 @Test
 public void testComputeValueTrue() {
 // given
 double valueArg1 = 5.0;
 double valueArg2 = 10.0;

 // when
 Mockito.when(arg1.computeValue(context)).thenReturn(valueArg1);
 Mockito.when(arg2.computeValue(context)).thenReturn(valueArg2);

 // then
 Boolean result = operation.computeValue(context);
 Equals expected = Equals.THE_INSTANCE;
 assertEquals(expected.getValue(Boolean.TRUE), result);
 }

 @Test
 public void testComputeValueFalse() {
 // given
 double valueArg1 = 10.0;
 double valueArg2 = 5.0;

 // when
 Mockito.when(arg1.computeValue(context)).thenReturn(valueArg1);
 Mockito.when(arg2.computeValue(context)).thenReturn(valueArg2);

 // then
 Boolean result = operation.computeValue(context);
 Equals expected = Equals.THE_INSTANCE;
 assertEquals(expected.getValue(Boolean.FALSE), result);
 }

 @Test
 public void testGetSymbol() {
 // given

 // when
 String symbol = operation.getSymbol();

 // then
 assertEquals("<", symbol);
 }
}