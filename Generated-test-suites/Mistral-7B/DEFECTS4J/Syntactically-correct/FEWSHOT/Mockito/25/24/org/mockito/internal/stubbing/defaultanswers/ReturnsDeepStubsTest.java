package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReturnsDeepStubsTest {

 private InternalMockHandler<Object> handler;
 private InvocationContainerImpl container;
 private Object mock;

 @Before
 public void setUp() {
 handler = new MockUtil().getMockHandler(mockitoMock());
 container = (InvocationContainerImpl) handler.getInvocationContainer();
 }

 @Test
 public void testReturnsDeepStubsWhenMatchingInvocation() {
 // Given
 when(mock(String.class, new ReturnsDeepStubs())).thenReturn("mockString");

 // When & Then
 InvocationOnMock invocation = mock(String.class).andReturn("testString").invocationMock();
 Object answer = container.getAnswer(invocation);

 verify(mock).answer(invocation);
 assertInstanceOf(String.class, answer);
 assertEquals("mockString", answer);
 }

 @Test
 public void testReturnsDeepStubsWhenNoMatchingInvocation() {
 // Given
 when(mock(String.class, new ReturnsDeepStubs())).thenReturn("mockString");

 // When & Then
 InvocationOnMock invocation = mock(Integer.class).invocationMock();
 Object answer = container.getAnswer(invocation);

 assertNull(answer);
 }

 @Test
 public void testGetMockForClass() {
 // Given
 when(mock(String[].class, new ReturnsDeepStubs())).thenReturn(new String[0]);

 // When & Then
 Object answer = container.getAnswer(mock(String[].class).invocationMock());
 assertInstanceOf(String[].class, answer);
 }

 @Test
 public void testGetMockForParameterizedType() {
 // Given
 Type type = new ParameterizedType() {
 @Override
 public Type getRawType() {
 return Map.class;
 }

 @Override
 public Type[] getActualTypeArguments() {
 return new Type[] { String.class, Set.class };
 }

 @Override
 public Type getOwnerType() {
 return null;
 }
 };
 when(mock(type, new ReturnsDeepStubs())).thenReturn(mock(Map.class));

 // When & Then
 InvocationOnMock invocation = mock(Map.Entry.class).andReturn(new Map.Entry<String, Number>(mock(String.class), mock(Number.class))).invocationMock();
 Object answer = container.getAnswer(invocation);

 assertInstanceOf(Map.class, answer);
 }

 @Test(expected = RuntimeException.class)
 public void testThrowsExceptionForNonMockableType() {
 // Given
 when(mock(Long.TYPE, new ReturnsDeepStubs())).thenCallRealMethod();

 // When & Then
 InvocationOnMock invocation = mock(Number[].class).invocationMock();
 container.getAnswer(invocation);
 }
}