package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mockObject;

 @Before
 public void setup() {
 mockObject = Mockito.mock(Object.class, new ReturnsDeepStubs());
 returnsDeepStubs = Mockito.mock(ReturnsDeepStubs.class);
 }

 @Test
 public void testAnswerWithSameType() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(returnsDeepStubs.answer(ArgumentMatchers.eq(invocation)))
 .thenReturn(mockObject);

 Object result = returnsDeepStubs.answer(invocation);
 assertEquals(mockObject, result);
 }

 @Test
 public void testAnswerWithDifferentType() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(returnsDeepStubs.answer(ArgumentMatchers.eq(invocation)))
 .thenReturn(new String("test"));

 Object result = returnsDeepStubs.answer(invocation);
 assertNotSame(mockObject, result);
 }

 @Test(expected = Throwable.class)
 public void testAnswerWithThrowable() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(returnsDeepStubs.answer(ArgumentMatchers.eq(invocation)))
 .thenThrow(new RuntimeException("Test exception"));

 returnsDeepStubs.answer(invocation);
 fail("An exception should have been thrown");
 }

 @Test
 public void testGetMockWithMatchingInvocation() {
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 Mockito.when(container.getInvocationForStubbing()).thenReturn(Mockito.mock(InvocationOnMock.class));
 Mockito.when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[0]);

 Mockito.when(returnsDeepStubs.getMock(ArgumentMatchers.eq(container), ArgumentMatchers.instanceOf(GenericMetadataSupport.class)))
 .thenReturn(mockObject);

 Object result = returnsDeepStubs.getMock(container, Mockito.mock(GenericMetadataSupport.class));
 assertEquals(mockObject, result);
 }

 @Test
 public void testGetMockWithNonMatchingInvocation() {
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 Mockito.when(container.getInvocationForStubbing()).thenReturn(Mockito.mock(InvocationOnMock.class));
 Mockito.when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[0]);

 Mockito.when(returnsDeepStubs.getMock(ArgumentMatchers.eq(container), ArgumentMatchers.instanceOf(GenericMetadataSupport.class)))
 .thenReturn(Mockito.mock(Object.class));

 assertNotSame(mockObject, returnsDeepStubs.getMock(container, Mockito.mock(GenericMetadataSupport.class)));
 }
}