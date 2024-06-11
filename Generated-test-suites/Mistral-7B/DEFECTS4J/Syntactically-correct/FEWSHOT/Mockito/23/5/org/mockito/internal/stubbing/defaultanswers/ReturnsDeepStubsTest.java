package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 @Test
 public void testGetMock() {
 MockSettings mockSettings = mockSettings();
 Object mock = mock(Object.class, withSettings(mockSettings));

 InvocationOnMock invocation = mock(InvocationOnMock.class);
 InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);

 when(invocationContainer.getInvocationForStubbing()).thenReturn(invocation);

 assertEquals(mock, returnsDeepStubs.getMock(invocation, mockType(Object.class)));
 }

 @Test
 public void testGetMockWithGenericMetadata() throws Exception {
 Class<?> genericType = Map.class;
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(genericType);
 GenericMetadataSupport actualReturnTypeGenericMetadata = new GenericMetadataSupport(Map.class);

 Object mock = mock(Map.class, withSettingsUsing(returnTypeGenericMetadata));
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);

 when(invocationContainer.getInvocationForStubbing()).thenReturn(invocation);

 assertEquals(mock, returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata));
 }

 @Test
 public void testGetMockWithNestedGenericType() throws Exception {
 Class<?> mapClass = Map.class;
 Class<?> setClass = Set.class;
 Class<?> numberClass = Number.class;

 GenericMetadataSupport mapReturnTypeGenericMetadata = new GenericMetadataSupport(mapClass, Map.class, new Class[] { Set.class }, new Class[] { Number.class });
 GenericMetadataSupport actualMapReturnTypeGenericMetadata = new GenericMetadataSupport(Map.class, Map.class, new Class[] { Set.class }, new Class[] { Number.class });

 Object mapMock = mock(Map.class, withSettingsUsing(mapReturnTypeGenericMetadata));
 Object setMock = mock(Set.class, withSettings());
 Object numberMock = mock(Number.class, withSettings());
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);

 when(invocationContainer.getInvocationForStubbing()).thenReturn(invocation);

 when(mapMock.entrySet().iterator().next().getValue()).thenReturn(setMock);
 when(setMock.iterator().next()).thenReturn(numberMock);

 assertEquals(mapMock, returnsDeepStubs.getMock(invocation, mapReturnTypeGenericMetadata));
 }

 @Test
 public void testGetMockWithDeepStub() throws Exception {
 Class<?> listClass = List.class;
 Class<?> stringClass = String.class;

 Object listMock = mock(List.class, withSettingsUsing(mockType(List.class, String.class)));
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);

 when(invocationContainer.getInvocationForStubbing()).thenReturn(invocation);

 Object deepStub = returnsDeepStubs.getMock(invocation, mockType(List.class, String.class));

 verify(deepStub).add(eq("foo"));
 verify(deepStub).add(eq("bar"));

 List<String> list = (List<String>) deepStub;
 assertEquals(2, list.size());
 assertEquals("foo", list.get(0));
 assertEquals("bar", list.get(1));
 }

 @Test
 public void testGetMockWithNullInvocation() {
 assertNull(returnsDeepStubs.getMock(null, mockType(Object.class)));
 }

 @Test
 public void testGetMockWithNullReturnTypeGenericMetadata() {
 assertNull(returnsDeepStubs.getMock(mock(InvocationOnMock.class), null));
 }

 @Test
 public void testReturnsEmptyValues() throws Exception {
 Object mock = mock(Object.class, withSettings());
 InvocationOnMock invocation = mock(InvocationOnMock.class);

 when(invocation.getMock()).thenReturn(mock);

 assertEquals(Collections.emptyList(), returnsDeepStubs.getMock(invocation, mockType(List.class)));
 }

 @Test
 public void testAnswer() throws Exception {
 Object mock = mock(Object.class, withSettings());
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);

 when(invocationContainer.getInvocationForStubbing()).thenReturn(invocation);

 Object deepStub = mock(Object.class, withSettingsUsing(mockType(Object.class)));
 when(container.getStubbedInvocations()).thenReturn(Arrays.asList(mock(StubbedInvocationMatcher.class)));

 InvocationOnMock stubbedInvocationMatcherInvocation = mock(InvocationOnMock.class);
 StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
 when(stubbedInvocationMatcher.answer(invocation)).thenReturn(deepStub);
 when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);

 when(container.getInvocationForStubbing()).thenReturn(stubbedInvocationMatcherInvocation);
 when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);

 Object result = returnsDeepStubs.answer(invocation);
 assertEquals(deepStub, result);
 }

 @Test
 public void testAnswerWithNullInvocation() {
 assertNull(returnsDeepStubs.answer(null));
 }

 @Test
 public void testAnswerWithNullReturnTypeGenericMetadata() {
 assertNull(returnsDeepStubs.answer(mock(InvocationOnMock.class), null));
 }

 private MockSettings mockSettings() {
 return withSettings();
 }

 private GenericMetadataSupport mockType(Class<?>... types) {
 return new GenericMetadataSupport(types);
 }
}