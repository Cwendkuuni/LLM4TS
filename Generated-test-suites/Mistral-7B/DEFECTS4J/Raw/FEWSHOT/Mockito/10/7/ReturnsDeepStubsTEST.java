package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationOnMock;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ReturnsDeepStubsTest {

 private static class MockType implements Map<Integer, Number> {
 }

 @Test
 public void testDeepStubsWithSimpleReturnType() {
 MockType mock = mock(MockType.class, new ReturnsDeepStubs());
 assertSame(mock, mock.entrySet().iterator().next().getValue().iterator().next());
 }

 @Test
 public void testDeepStubsWithGenericReturnType() {
 MockType mock = mock(MockType.class, new ReturnsDeepStubs());
 Type returnType = mock.getClass().getGenericInterfaces()[0];
 Type mapEntryType = ((ParameterizedType) returnType).getActualTypeArguments()[1];
 GenericDeclaration mapEntryTypeDeclaration = mapEntryType.getDeclaringClass();
 Map map = (Map) mock;
 assertSame(mock, map.entrySet().iterator().next().getValue().getClass().getDeclaredField("next").get(map.entrySet().iterator()).getValue());
 assertSame(mapEntryTypeDeclaration.getTypeName(), map.entrySet().iterator().next().getValue().getClass().getCanonicalName());
 }

 @Test
 public void testDeepStubsWithNestedGenericReturnType() {
 MockType mock = mock(MockType.class, new ReturnsDeepStubs());
 Type returnType = mock.getClass().getGenericInterfaces()[0];
 Type mapEntryType = ((ParameterizedType) returnType).getActualTypeArguments()[1];
 Type numberType = ((ParameterizedType) mapEntryType).getActualTypeArguments()[0];
 assertSame(mock, mock.entrySet().iterator().next().getValue().iterator().next().getValue().iterator().next());
 assertSame(Number.class, numberType.getTypeName().equals("java.lang.Number") ? Number.class : Class.forName(numberType.getTypeName()));
 }

 @Test
 public void testDeepStubsWithSameMockPassedTwice() {
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(mock).thenReturn(mock).given(invocation);
 when(mock).thenReturn(mock).given(invocation);
 assertEquals(mock, invocation.getMock());
 }

 @Test
 public void testDeepStubsWithStubbedInvocation() {
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 Object stub = mock(Object.class, new Answers() {
 @Override
 public Object answer(InvocationOnMock invocation) {
 return "stub";
 }
 });
 InvocationContainerImpl container = new InvocationContainerImpl(new StubbedInvocationMatcher(stub), new InvocationOnMock(mock, new Object[0]));
 when(mock).thenAnswer(new DeeplyStubbedAnswer(stub)).given(new InvocationOnMock(mock, new Object[0]));
 assertEquals(stub, mock.answer(new InvocationOnMock(mock, new Object[0])));
 }

 @Test(expected = IllegalStateException.class)
 public void testDeepStubsWithInstantiatedAnswer() {
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 Object answer = new Object();
 when(mock).thenReturn(answer).given(new InvocationOnMock(mock, new Object[0]));
 assertEquals(answer, mock.answer(new InvocationOnMock(mock, new Object[0]))); // this should throw an exception
 }

 @Test
 public void testDeepStubsWithSameMockPassedAsArgument() {
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 Object argMock = mock(Object.class, new ReturnsDeepStubs());
 when(mock.someMethod(argMock)).thenReturn(mock).given(new InvocationOnMock(mock, new Object[]{argMock}));
 assertEquals(mock, mock.someMethod(argMock));
 }

 @Test
 public void testDeepStubsWithNestedStubbedInvocations() {
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 Object innerMock = mock(Object.class, new ReturnsDeepStubs());
 InvocationContainerImpl container = new InvocationContainerImpl(new StubbedInvocationMatcher(innerMock), new InvocationOnMock(mock, new Object[0]));
 when(mock.someMethod()).thenAnswer(new DeeplyStubbedAnswer(innerMock)).given(new InvocationOnMock(mock, new Object[0]));
 assertEquals(innerMock, mock.someMethod());
 }

 @Test
 public void testDeepStubsWithComplexStubbedInvocations() {
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 Object innerMock = mock(Object.class, new ReturnsDeepStubs());
 Object anotherInnerMock = mock(Object.class, new ReturnsDeepStubs());
 InvocationContainerImpl container = new InvocationContainerImpl(new StubbedInvocationMatcher(anotherInnerMock), new InvocationOnMock(mock, new Object[0]));
 when(mock.someMethod()).thenAnswer(new DeeplyStubbedAnswer(innerMock)).given(new InvocationOnMock(mock, new Object[0]));
 when(innerMock.someMethod()).thenAnswer(new DeeplyStubbedAnswer(anotherInnerMock)).given(new InvocationOnMock(innerMock, new Object[0]));
 assertEquals(anotherInnerMock, mock.someMethod().someMethod());
 }
}