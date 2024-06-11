package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private InvocationContainerImpl container;
 private InternalMockHandler<Object> handler;
 private Object mockMap;
 private Object mockSet;
 private Object mockNumber;

 @Before
 public void setup() {
 this.returnsDeepStubs = new ReturnsDeepStubs();
 this.container = new InvocationContainerImpl();
 this.handler = new MockUtil().getMockHandler(this.mockMap = mock(Map.class));
 }

 @Test
 public void deepStubsForMap() throws Exception {
 // Test creating a deep stub for a Map
 Object mockMapEntrySet = mockMap.entrySet().iterator().next();
 Object mockMapEntryValueIterator = mockMapEntrySet.getValue().iterator().next();
 Object mockMapEntryValueIteratorNext = mockMapEntryValueIterator.iterator().next();

 when(mockMapEntryValueIterator.iterator()).thenReturn(mockMapEntryValueIterator);
 when(mockMapEntryValueIterator.next()).thenReturn(mockMapEntryValueIteratorNext);

 when(mockMapEntrySet.getValue()).thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return mockMapEntryValueIterator;
 }
 });

 when(mockMap.entrySet()).thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return mockMapEntrySet;
 }
 });

 Object mockMapDeepStub = container.getMockHandler(mockMap).getMock();

 assertEquals(mockMapDeepStub, mockMapDeepStub.entrySet().iterator().next().getValue().iterator().next());
 }

 @Test
 public void deepStubsForNestedGenericTypes() throws Exception {
 // Test creating a deep stub for a generic type that contains another generic type
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }

 GenericsNest<Number> mockGenericsNest = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);

 assertEquals(mockGenericsNest, mockGenericsNest.entrySet().iterator().next().getValue().iterator().next());
 }

 @Test(expected = IllegalStateException.class)
 public void deepStubsForNonMockableType() throws Exception {
 // Test error case: creating a deep stub for a non-mockable type
 when(handler.getInvocationForStubbing()).thenThrow(new IllegalStateException("Cannot mock this type"));

 when(handler.getMockHandler(this.mockNumber = new Number() {})).thenReturn(handler);

 try {
 Object mockNumberDeepStub = container.getMockHandler(mockNumber).getMock();
 } finally {
 verify(handler).getInvocationForStubbing();
 }
 }

 @Test
 public void deepStubsForAlreadyMockedType() throws Exception {
 // Test edge case: creating a deep stub for a type that is already a mock
 when(handler.getInvocationForStubbing()).thenReturn(new InvocationOnMock(Mockito.mock(Object.class), "testMethod", new Class[0]));
 when(container.getStubbedInvocations()).thenReturn(new ArrayList<>());
 when(container.getInvocationForStubbing()).thenReturn(new InvocationOnMock(mockMap, "testMethod", new Class[0]));

 Object mockMapDeepStub = container.getMockHandler(mockMap).getMock();

 verify(container).addAnswer(ArgumentMatchers.isA(Answer.class), ArgumentMatchers.eq(false), ArgumentMatchers.anyInt());
 verify(handler).getInvocationForStubbing();
 assertEquals(mockMapDeepStub, mockMapDeepStub.entrySet().iterator().next().getValue().iterator().next());
 }

 @Test
 public void deepStubsNestedInsideOneAnother() throws Exception {
 // Test creating deep stubs that are nested inside one another
 Object deepStub1 = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
 Object deepStub2 = mock(Object.class, Answers.RETURNS_DEEP_STUBS);

 when(deepStub1.deepStub2()).thenReturn(deepStub2);
 when(deepStub2.deepStub1()).thenReturn(deepStub1);

 Object deepStub1DeepStub = container.getMockHandler(deepStub1).getMock();
 Object deepStub2DeepStub = container.getMockHandler(deepStub2).getMock();

 assertEquals(deepStub1DeepStub, deepStub2DeepStub.deepStub1());
 assertEquals(deepStub2DeepStub, deepStub1DeepStub.deepStub2());
 }
}