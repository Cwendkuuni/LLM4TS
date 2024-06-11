package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.ReturnsDeepStubs;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 @Test
 public void testAnswerWithDeepStub() throws Exception {
 Object mockType = mock(Object.class, new ReturnsDeepStubs());
 Object deepStub = mock(Object.class);
 when(deepStub.someMethod()).thenReturn("deepStubReturnValue");

 Object result = returnsDeepStubs.answer(new InvocationOnMock(mockType, "someMethod"));
 assertEquals("deepStubReturnValue", result);

 verify(deepStub).someMethod();
 }

 @Test
 public void testAnswerWithEmptyValues() throws Exception {
 Class<?> stringType = String.class;
 Object emptyString = returnsDeepStubs.delegate.returnValueFor(stringType);
 assertEquals("", emptyString);
 }

 @Test
 public void testGetMockWithMatchedInvocation() throws Exception {
 Object mockType = mock(Object.class, new ReturnsDeepStubs());
 Object deepStub = mock(Object.class);
 when(deepStub.someMethod()).thenReturn("deepStubReturnValue");
 StubbedInvocationMatcher invocationMatcher = argumentCaptor(Object.class);
 Object invocation = new InvocationOnMock(mockType, "someMethod");
 doAnswer(invocationMatcher.capture()).when(mockType).someMethod();

 Object result = returnsDeepStubs.answer(invocation);
 assertEquals("deepStubReturnValue", result);
 assertEquals(deepStub, invocationMatcher.getValue());
 }

 @Test
 public void testGetMockWithNotMatchedInvocation() throws Exception {
 Object mockType = mock(Object.class, new ReturnsDeepStubs());
 Object deepStub = mock(Object.class, new ReturnsDeepStubs());
 when(deepStub.someMethod()).thenReturn("deepStubReturnValue");
 Object anotherDeepStub = mock(Object.class, new ReturnsDeepStubs());

 Object result = returnsDeepStubs.answer(new InvocationOnMock(mockType, "someOtherMethod"));
 assertNotSame(deepStub, result);
 assertNotSame(anotherDeepStub, result);
 }

 @Test(expected = Throwable.class)
 public void testGetMockWithNull() throws Exception {
 Object result = returnsDeepStubs.answer(null);
 fail("should throw exception");
 }
}