package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.InvocationOnMock;
import org.mockito.MockSettings;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationContainerImpl;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationMatcher;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.GenericMetadataSupport;
import org.mockito.internal.util.reflection.GenericType;
import org.mockito.internal.util.reflection.Reflections;
import org.mockito.invocation.Invocation;

public class ReturnsDeepStubsTest {

 private static final Class<?> CLASS_WITH_NESTED_GENERICS = GenericNest.class;

 @Test
 public void testGetMockWithSimpleType() {
 MockSettings mockSettings = mockSettings().name("simpleTypeMock");
 InternalMockHandler<Object> handler = new MockitoCore().getMockHandler(mock(Object.class, mockSettings).getMock());
 InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

 Object stub = getMock(container, Object.class);
 verify(stub).toString();
 }

 @Test
 public void testGetMockWithNestedGenericType() {
 MockSettings mockSettings = mockSettings().name("nestedTypeMock");
 InternalMockHandler<Object> handler = new MockitoCore().getMockHandler(mock(Object.class, mockSettings).getMock());
 InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

 Class<?> genericNestType = CLASS_WITH_NESTED_GENERICS;
 Object nestedTypeMock = getMock(container, genericNestType);

 verify(nestedTypeMock).entrySet();
 Map<Comparable<?>, Set<Number>> map = mock(Map.class);
 when(nestedTypeMock.entrySet()).thenReturn(map.entrySet());
 Set<Number> set = mock(Set.class);
 when(map.get(any())).thenReturn(set);
 Number number = mock(Number.class);
 when(set.iterator()).thenReturn(Collections.singletonList(number).iterator());

 Number numberReturned = ((ReturnsDeepStubs) ((Reflections.getFieldValue(nestedTypeMock, "answer")))).answer(mock(InvocationOnMock.class));
 assertEquals(number, numberReturned);
 }

 @Test
 public void testRecordDeepStubMock() throws Exception {
 MockSettings mockSettings = mockSettings().name("recordDeepStubMockTest");
 InternalMockHandler<Object> handler = new MockitoCore().getMockHandler(mock(Object.class, mockSettings).getMock());
 InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

 Object mock = mock(Object.class, mockSettings);
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Reflections.setFieldValue(mock, "answer", returnsDeepStubs);
 InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
 container.addAnswer(invocationMatcher, false);

 InvocationOnMock invocation = mock(InvocationOnMock.class);
 Invocation invocationToStub = mock(Invocation.class);
 when(invocationToStub.getTarget()).thenReturn(mock);
 when(invocationMatcher.matches(invocationToStub)).thenReturn(true);
 when(invocationToStub.getMethod()).thenReturn(mockMethod(Object.class, "testMethod"));

 Object stub = getMock(container, Object.class);
 stub.testMethod();
 verify(mock).testMethod();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetMockWithNonMockableType() {
 MockSettings mockSettings = mockSettings().name("nonMockableTypeTest");
 InternalMockHandler<Object> handler = new MockitoCore().getMockHandler(mock(Object.class, mockSettings).getMock());
 InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

 Class<?> nonMockableType = int[].class;
 getMock(container, nonMockableType);
 }

 private Object getMock(InvocationContainerImpl container, Class<?> clazz) throws Throwable {
 Object mock = container.getInvocationForStubbing().getMock();
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Reflections.setFieldValue(mock, "answer", returnsDeepStubs);
 return returnsDeepStubs.getMock(new InvocationOnMock(mock), new GenericMetadataSupport(clazz));
 }

 private Method mockMethod(Class<?> clazz, String methodName) {
 try {
 return Reflections.getMethod(clazz, methodName);
 } catch (NoSuchMethodException e) {
 throw new RuntimeException(e);
 }
 }
}