package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.MockitoCore;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;

public class ReturnsDeepStubsTest {

 private MockitoCore mockitoCore = new MockitoCore();

 @Test
 public void testAnswer() {
 MockSettings mockSettings = mockSettings().defaultAnswer(new ReturnsDeepStubs());
 Object mock = mockitoCore.mock(Object.class, mockSettings);

 InvocationOnMock invocation = mock(Object.class);
 when(mock.answer(invocation)).thenReturn("answer");

 assertEquals("answer", mock.answer(invocation));

 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(Object.class);
 Object deepStub = new ReturnsDeepStubs().getMock(invocation, returnTypeGenericMetadata);

 verify(deepStub).answer(invocation);
 }

 @Test
 public void testAnswer_Generic() {
 interface GenericsInterface<T> {
 T method();
 }

 interface GenericsNest<K extends Comparable<K> & Cloneable, T extends Number> extends Map<K, Set<T>> {
 }

 GenericsNest<Integer, Integer> mock = mock(GenericsNest.class, new ReturnsDeepStubs());
 Integer number = mock.entrySet().iterator().next().getValue().iterator().next();

 assertEquals(number, mock.entrySet().iterator().next().getValue().iterator().next());
 }

 @Test
 public void testAnswer_NestedGeneric() {
 interface GenericsInterface<T> {
 T method();
 }

 interface GenericsNest<K extends Comparable<K> & Cloneable, T extends Number> extends Map<K, Set<T>> {
 }

 GenericsNest<Integer, Integer> mock = mock(GenericsNest.class, new ReturnsDeepStubs());
 Set<Number> numbers = mock.entrySet().iterator().next().getValue();
 Number number = numbers.iterator().next();

 assertEquals(number, numbers.iterator().next());
 }

 @Test
 public void testAnswer_Null() {
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 assertNull(mock.answer(null));
 }

 @Test
 public void testAnswer_Throwable() {
 Object mock = mock(Throwable.class, new ReturnsDeepStubs());
 assertThrows(RuntimeException.class, () -> mock.answer(null));
 }

 @Test
 public void testGetMock_MatchesInvocationForVerification() {
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 StubbedInvocationMatcher stubbedInvocationMatcher = stub(mock, "method").thenReturn("stubbedValue");

 assertEquals("stubbedValue", mock.answer(mock("method")));

 verify(mock).method();
 }

 @Test
 public void testGetMock_DeepStub() {
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 Object deepStub = mock(Object.class, new ReturnsDeepStubs());

 when(mock.answer(mock("method"))).thenReturn(deepStub);

 assertEquals(deepStub, mock.answer(mock("method")));
 }

 @Test
 public void testGetMock_DifferentTypes() {
 Object mockObject = mock(Object.class, new ReturnsDeepStubs());
 Object mockList = mock(List.class, new ReturnsDeepStubs());
 Object mockSet = mock(Set.class, new ReturnsDeepStubs());

 assertNotSame(mockObject, mockList);
 assertNotSame(mockObject, mockSet);

 when(mockList.answer(mock("method"))).thenReturn(mockObject);
 when(mockSet.answer(mock("method"))).thenReturn(mockObject);

 assertEquals(mockObject, mockList.answer(mock("method")));
 assertEquals(mockObject, mockSet.answer(mock("method")));
 }

 @Test
 public void testCreateNewDeepStubMock() {
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(List.class);
 Object deepStub = new ReturnsDeepStubs().createNewDeepStubMock(returnTypeGenericMetadata);
 assertTrue(deepStub instanceof List);
 }

 @Test
 public void testWithSettingsUsing() {
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(List.class);
 MockSettings mockSettings = new ReturnsDeepStubs().withSettingsUsing(returnTypeGenericMetadata);
 Assert.assertNotNull(mockSettings.extraInterfaces());
 Assert.assertEquals(1, mockSettings.extraInterfaces().length);
 Assert.assertTrue(List.class.isAssignableFrom(mockSettings.getTypeToMock()));
 }

 @Test
 public void testRecordDeepStubMock() throws Exception {
 Object mock = mock(List.class, new ReturnsDeepStubs());
 InvocationContainerImpl container = (InvocationContainerImpl) new MockUtil().getMockHandler(mock).getInvocationContainer();
 Object deepStub = new Object() {};
 container.addAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return deepStub;
 }
 }, false);

 assertEquals(deepStub, mock.answer(mock("method")));
 }
}