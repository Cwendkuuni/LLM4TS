package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
 Object mockObject = mock(String.class);

 @Test
 public void testAnswerNull() {
 Object result = returnsSmartNulls.answer(mock(InvocationOnMock.class));
 assertNull(result);
 }

 @Test
 public void testAnswerInstanceOfMock() {
 when(mockObject.toString()).thenReturn("SmartNull returned by unstubbed toString() method on mock");
 String result = (String) returnsSmartNulls.answer(mock(InvocationOnMock.class).withArguments(mockObject));
 assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
 }

 @Test
 public void testAnswerInstanceOfNotMockable() {
 assertNull(returnsSmartNulls.answer(mock(Integer.TYPE.class)));
 }

 @Test
 public void testAnswerInstanceOfMockableThroughCglib() {
 ClassImposterizer classImposterizer = Mockito.spy(ClassImposterizer.class);
 Class<?> mockClass = classImposterizer.imposterise(new Object() {}, Integer.TYPE);
 ReturnsSmartNulls returnsSmartNullsImposterised = spy(returnsSmartNulls);
 when(classImposterizer.canImposterise(Integer.TYPE)).thenReturn(true);
 when(classImposterizer.imposterise(Mockito.any(), Mockito.eq(Integer.TYPE))).thenReturn(mockClass);
 Object result = returnsSmartNullsImposterised.answer(mock(InvocationOnMock.class).withArguments(mockObject));
 assertNull(result);
 verify(classImposterizer).imposterise(Mockito.any(), Mockito.eq(Integer.TYPE));
 }

 @Test(expected = NullPointerException.class)
 public void testAnswerToStringMethod() {
 String result = (String) returnsSmartNulls.answer(mock(InvocationOnMock.class).withArguments(mockObject)).toString();
 }
}