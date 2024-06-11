package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {

 private MockitoCore mockitoCore = new MockitoCore();

 @Test
 public void testAnswer() {
 Object mock = mock(Object.class, new ReturnsDeepStubs());

 assertEquals(mock, answer(mock, eq(Object.class), any()));
 }

 @Test
 public void testGetMock() throws Exception {
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 InvocationContainerImpl container = (InvocationContainerImpl) getMockHandler(mock).getInvocationContainer();

 assertEquals(mock, getMock(mock, any(GenericMetadataSupport.class)));
 }

 @Test
 public void testCreateNewDeepStubMock() throws Exception {
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 GenericMetadataSupport returnTypeGenericMetadata = any(GenericMetadataSupport.class);

 assertEquals(mock, createNewDeepStubMock(mock, returnTypeGenericMetadata));
 }

 @Test
 public void testRecordDeepStubMock() throws Exception {
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 InvocationContainerImpl container = (InvocationContainerImpl) getMockHandler(mock).getInvocationContainer();

 Object deepStubMock = recordDeepStubMock(mock, container);

 verify(mock).answer(any(InvocationOnMock.class));
 assertEquals(mock, deepStubMock);
 }

 @Test
 public void testAnswerWithNestedGenerics() throws Exception {
 Class<Map<Integer, Set<Number>>> mapType = (Class<Map<Integer, Set<Number>>>) Class.forName("java.util.Map");
 Class<Set<Number>> setType = (Class<Set<Number>>) Class.forName("java.util.Set");
 Class<Number> numberType = (Class<Number>) Class.forName("java.lang.Number");

 Object mapMock = mock(mapType, new ReturnsDeepStubs());
 Object setMock = mock(setType, new ReturnsDeepStubs());
 Object numberMock = mock(numberType, new ReturnsDeepStubs());

 Object mapEntry = mock(Map.Entry.class, new ReturnsDeepStubs());
 Object mapEntrySetIterator = mock(java.util.Iterator.class, new ReturnsDeepStubs());
 Object mapEntryValueIterator = mock(java.util.Iterator.class, new ReturnsDeepStubs());

 when(mapEntry.getKey()).thenReturn(mapMock);
 when(mapEntry.getValue()).thenReturn(setMock);
 when(setMock.iterator()).thenReturn(mapEntrySetIterator);
 when(mapEntrySetIterator.next()).thenReturn(mapEntry);
 when(mapEntryValueIterator.next()).thenReturn(numberMock);

 Object mapEntrySet = mapMock;
 when(mapEntrySetIterator.hasNext()).thenReturn(true);
 when(mapEntrySetIterator.next()).thenReturn(mapEntry);

 Object mapEntryValue = numberMock;
 when(mapEntryValueIterator.hasNext()).thenReturn(true);
 when(mapEntryValueIterator.next()).thenReturn(mapEntryValue);

 Object mapEntryValueIteratorNext = mapEntryValue;
 when(mapEntryValueIterator.next()).thenReturn(mapEntryValue);

 assertEquals(mapMock, mapEntrySet.iterator().next().getValue().iterator().next());
 }

 private Object answer(Object mock, Class<?> returnType, InvocationOnMock invocation) throws Throwable {
 ReturnsDeepStubs returnsDeepStubs = (ReturnsDeepStubs) getAnswer(mock);
 return returnsDeepStubs.answer(invocation);
 }

 private Object getMock(Object mock, GenericMetadataSupport returnTypeGenericMetadata) throws Throwable {
 ReturnsDeepStubs returnsDeepStubs = (ReturnsDeepStubs) getAnswer(mock);
 return returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);
 }

 private Object createNewDeepStubMock(Object mock, GenericMetadataSupport returnTypeGenericMetadata) throws Throwable {
 ReturnsDeepStubs returnsDeepStubs = (ReturnsDeepStubs) getAnswer(mock);
 return returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);
 }

 private Object recordDeepStubMock(Object mock, InvocationContainerImpl container) throws Throwable {
 ReturnsDeepStubs returnsDeepStubs = (ReturnsDeepStubs) getAnswer(mock);
 return returnsDeepStubs.recordDeepStubMock(mock, container);
 }

 private Answer<?> getAnswer(Object mock) throws Throwable {
 InternalMockHandler handler = new MockUtil().getMockHandler(mock);
 return handler.getAnswer();
 }
}