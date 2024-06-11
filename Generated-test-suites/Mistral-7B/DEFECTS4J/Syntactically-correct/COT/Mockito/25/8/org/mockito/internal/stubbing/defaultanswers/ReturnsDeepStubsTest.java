package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 @Test
 public void testAnswerMethodWithMockableType() {
 Class<?> mockableType = Mockito.class;
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class).invoke(mockableType, (Object[]) new Object[0]);
 Object deepStub = returnsDeepStubs.answer(invocation);
 Assert.assertSame(mock(mockableType), deepStub);
 }

 @Test(expected = IllegalStateException.class)
 public void testAnswerMethodWithNonMockableType() {
 Class<?> nonMockableType = String.class;
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class).invoke(nonMockableType, (Object[]) new Object[0]);
 returnsDeepStubs.answer(invocation);
 }

 @Test
 public void testGetMockMethodWithMatchedInvocation() {
 Class<?> mockableType = HashMap.class;
 InternalMockHandler mockHandler = MockUtil.getMockHandler(mock(mockableType));
 InvocationContainerImpl container = (InvocationContainerImpl) mockHandler.getInvocationContainer();
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class).invoke(mockableType, (Object[]) new Object[0]);
 when(container.getInvocationForStubbing()).thenReturn(invocation);

 Object deepStub = returnsDeepStubs.getMock(invocation);
 Assert.assertSame(mock(mockableType), deepStub);
 }

 @Test
 public void testGetMockMethodWithUnmatchedInvocation() {
 Class<?> mockableType = HashMap.class;
 InternalMockHandler mockHandler = MockUtil.getMockHandler(mock(mockableType));
 InvocationContainerImpl container = (InvocationContainerImpl) mockHandler.getInvocationContainer();
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class).invoke(ArrayList.class, (Object[]) new Object[0]);

 Object deepStub = returnsDeepStubs.getMock(invocation);
 Assert.assertNull(deepStub);
 }

 @Test
 public void testRecordDeepStubMockMethod() {
 Class<?> mockableType = HashMap.class;
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class).invoke(mockableType, (Object[]) new Object[0]);
 InternalMockHandler mockHandler = MockUtil.getMockHandler(mock(mockableType));
 InvocationContainerImpl container = (InvocationContainerImpl) mockHandler.getInvocationContainer();

 Object deepStub = returnsDeepStubs.recordDeepStubMock(invocation, container);
 Assert.assertSame(mock(mockableType), deepStub);

 Assert.assertNotNull(container.getAnswer(0));
 Answer<Object> answer = container.getAnswer(0);
 Object returnedValue = answer.answer(invocation);
 Assert.assertSame(deepStub, returnedValue);
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordDeepStubMockMethodWithAlreadyRegisteredAnswer() {
 Class<?> mockableType = HashMap.class;
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class).invoke(mockableType, (Object[]) new Object[0]);
 InternalMockHandler mockHandler = MockUtil.getMockHandler(mock(mockableType));
 InvocationContainerImpl container = (InvocationContainerImpl) mockHandler.getInvocationContainer();
 container.addAnswer(Mockito.mock(Answer.class), false);

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 try {
 returnsDeepStubs.recordDeepStubMock(invocation, container);
 } catch (IllegalStateException illegalStateException) {
 throw illegalStateException;
 }
 }

 @Test
 public void testRecordDeepStubMockMethodWithNullInvocation() {
 Class<?> mockableType = HashMap.class;
 InternalMockHandler mockHandler = MockUtil.getMockHandler(mock(mockableType));
 InvocationContainerImpl container = (InvocationContainerImpl) mockHandler.getInvocationContainer();
 InvocationOnMock invocation = null;

 try {
 returnsDeepStubs.recordDeepStubMock(invocation, container);
 Assert.fail();
 } catch (IllegalArgumentException illegalArgumentException) {
 // Expected
 }
 }

 @Test
 public void testActualParameterizedTypeMethod() {
 Class<?> mockableType = HashMap.class;
 Object mock = mock(mockableType);
 GenericMetadataSupport genericMetadataSupport = returnsDeepStubs.actualParameterizedType(mock);
 Assert.assertEquals(mockableType, genericMetadataSupport.getType());
 }
}