package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answer.Answer2;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 @Test
 public void testAnswerWithEmptyValues() {
 Object result = returnsDeepStubs.answer(ArgumentMatchers.any(Object.class));
 Assertions.assertNull(result);
 }

 @Test
 public void testAnswerWithMockableType() throws Exception {
 Object mock = Mockito.mock(Integer.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMock(mock);
 invocation.setMethod(Mockito.method(Integer.class, "testMethod"));

 Object result = returnsDeepStubs.answer(invocation);
 Assertions.assertEquals(mock, result);
 }

 @Test
 public void testAnswerWithNonMockableType() throws Exception {
 Object result = returnsDeepStubs.answer(ArgumentMatchers.any(String.class));
 Assertions.assertNull(result);
 }

 @Test
 public void testAnswerWithGenericType() throws Exception {
 InterfaceGenericsNest<Integer> mock = Mockito.mock(InterfaceGenericsNest.class, new ReturnsDeepStubs<>());
 Number number = mock.entrySet().iterator().next().getValue().iterator().next();

 Assertions.assertNotNull(number);
 }

 @Test
 public void testAnswerWithNestedGenericType() throws Exception {
 InterfaceGenericsNest<List<Map<Integer, Number>>> mock = Mockito.mock(InterfaceGenericsNest.class, new ReturnsDeepStubs<>());
 List<Map<Integer, Number>> list = mock.entrySet().iterator().next().getValue();
 Map<Integer, Number> map = list.get(0);
 Number number = map.iterator().next();

 Assertions.assertNotNull(number);
 }

 @Test
 public void testGetMockWithMatchingInvocation() throws Exception {
 Object mock = Mockito.mock(Integer.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMock(mock);
 invocation.setMethod(Mockito.method(Integer.class, "testMethod"));

 InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
 InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
 StubbedInvocationMatcher stubbedInvocationMatcher = new StubbedInvocationMatcher(Mockito.method(Integer.class, "testMethod"), Answer2.CALLS_REAL_METHOD);
 container.addStubbedInvocation(stubbedInvocationMatcher);

 Object result = returnsDeepStubs.getMock(invocation);
 Assertions.assertEquals(mock, result);
 }

 @Test
 public void testGetMockWithNotMatchingInvocation() throws Exception {
 Object mock = Mockito.mock(Integer.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMock(mock);
 invocation.setMethod(Mockito.method(String.class, "testMethod"));

 InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
 InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

 Object result = returnsDeepStubs.getMock(invocation);
 Assertions.assertNull(result);
 }

 @Test
 public void testRecordDeepStubMock() throws Exception {
 Object mock = Mockito.mock(Integer.class, new ReturnsDeepStubs<>());
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMock(mock);
 invocation.setMethod(Mockito.method(Integer.class, "testMethod"));

 Object result = returnsDeepStubs.recordDeepStubMock(invocation, new InvocationContainerImpl(Mockito.mock(Object.class)));
 Assertions.assertEquals(mock, result);
 }

 @Test(expected = Throwable.class)
 public void testAnswerWithThrowable() throws Exception {
 Object mock = Mockito.mock(Integer.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMock(mock);
 invocation.setMethod(Mockito.method(Integer.class, "testMethodThrowable"));
 invocation.getArgument(0).setValue("test");

 returnsDeepStubs.answer(invocation);
 }
}