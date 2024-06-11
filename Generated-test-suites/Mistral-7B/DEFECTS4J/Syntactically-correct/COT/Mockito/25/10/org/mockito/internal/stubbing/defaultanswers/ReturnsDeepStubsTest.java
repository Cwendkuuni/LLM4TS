package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.Answer;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.lang.reflect.Type;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 @Test
 public void testAnswer_ReturnsExistingMock() {
 // Given
 Object mock = mock(String.class);
 String expected = "mock string";
 when(mock.toString()).thenReturn(expected);

 // When
 String result = (String) returnsDeepStubs.answer(new InvocationOnMock(mock, "toString()"));

 // Then
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testAnswer_ReturnsEmptyValuesForNonMockableType() {
 // Given
 Object nonMockableType = new Object() {};

 // When
 Object result = returnsDeepStubs.answer(new InvocationOnMock(nonMockableType, "toString()"));

 // Then
 Assert.assertNull(result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswer_ThrowsExceptionForNonInvocableType() {
 // Given
 Object nonInvocableType = new Object() {
 };

 // When & Then
 returnsDeepStubs.answer(new InvocationOnMock(nonInvocableType, "toString()"));
 }

 @Test
 public void testGetMock_ReturnsExistingStub() {
 // Given
 Object mock = mock(String.class);
 String expected = "mock string";
 when(mock.toString()).thenReturn(expected);

 // When
 Object result = returnsDeepStubs.getMock(new InvocationOnMock(mock, "toString()"));

 // Then
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testGetMock_ReturnsNewStub() {
 // Given
 Object mock = mock(String.class);
 String expected = "mock string";
 when(mock.toString()).thenReturn(expected);

 // When
 Object newStub = returnsDeepStubs.getMock(new InvocationOnMock(mock, "toString()"));

 // Then
 Assert.assertNotSame(newStub, mock);
 Assert.assertEquals(expected, newStub.toString());
 }

 @Test
 public void testRecordDeepStubMock_ReturnsNewMock() {
 // Given
 Object mock = mock(String.class);
 String expected = "mock string";
 when(mock.toString()).thenReturn(expected);

 // When
 Object newStub = returnsDeepStubs.recordDeepStubMock(new InvocationOnMock(mock, "toString()"), null);

 // Then
 Assert.assertNotSame(newStub, mock);
 Assert.assertEquals(expected, newStub.toString());
 }

 @Test
 public void testRecordDeepStubMock_AddsAnswerToContainer() {
 // Given
 Object mock = mock(String.class);
 String expected = "mock string";
 when(mock.toString()).thenReturn(expected);

 // When
 Object newStub = returnsDeepStubs.recordDeepStubMock(new InvocationOnMock(mock, "toString()"), null);

 // Then
 Answer<Object> answer = new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return newStub;
 }
 };
 Object container = new Object() {};
 ((InvocationContainerImpl) container).addAnswer(answer, false);

 // When & Then
 Assert.assertEquals(newStub, container.getClass().getMethod("answer", InvocationOnMock.class).invoke(container, new InvocationOnMock(mock, "toString()")));
 }

 @Test
 public void testAnswer_ReturnsExistingStubForNestedGenericTypes() {
 // Given
 Type genericType1 = new TypeToken<Map<String, Set<Number>>>() {}.getType();
 Map<String, Set<Number>> mock = mock(genericType1);
 Set<Number> set = mock.entrySet().iterator().next().getValue().iterator().next();
 Number number = set.iterator().next();

 // When
 Object result = returnsDeepStubs.answer(new InvocationOnMock(number, "intValue()"));

 // Then
 Assert.assertNotNull(result);
 // Assert.assertEquals(expected, result); // Uncomment when you have an expected value for Number
 }
}