package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationLessThanTest {

 @Mock
 private EvalContext context;

 @InjectMocks
 private CoreOperationLessThan coreOperationLessThan;

 @Before
 public void setUp() {
 // Set up mocks
 }

 @Test
 public void testComputeValue_whenArg1IsLessThanArg2_thenReturnsTrue() {
 // Given
 when(args[0].computeValue(context)).thenReturn(1.0);
 when(args[1].computeValue(context)).thenReturn(2.0);

 // When
 Object result = coreOperationLessThan.computeValue(context);

 // Then
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComputeValue_whenArg1IsGreaterOrEqualArg2_thenReturnsFalse() {
 // Given
 when(args[0].computeValue(context)).thenReturn(2.0);
 when(args[1].computeValue(context)).thenReturn(1.0);

 // When
 Object result = coreOperationLessThan.computeValue(context);

 // Then
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValue_whenArg1AndArg2AreEqual_thenReturnsFalse() {
 // Given
 when(args[0].computeValue(context)).thenReturn(1.0);
 when(args[1].computeValue(context)).thenReturn(1.0);

 // When
 Object result = coreOperationLessThan.computeValue(context);

 // Then
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testGetSymbol_thenReturnsLessThanSymbol() {
 // When
 String symbol = coreOperationLessThan.getSymbol();

 // Then
 assertEquals("<", symbol);
 }
}