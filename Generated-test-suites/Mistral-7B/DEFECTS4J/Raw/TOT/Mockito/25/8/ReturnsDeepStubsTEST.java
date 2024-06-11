package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@PrepareForTest({ReturnsDeepStubs.class, GenericMetadataSupport.class})
@SuppressWarnings("unchecked")
public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mockMap;

 @Before
 public void setup() {
 returnsDeepStubs = new ReturnsDeepStubs();
 mockMap = Mockito.mock(Map.class, withSettings().defaultAnswer(returnsDeepStubs));
 }

 @Test
 public void testGetMock_whenInvokingMethodOnMock_thenReturnsDeepStub() throws Throwable {
 Map<Integer, List<Number>> map = (Map<Integer, List<Number>>) mockMap;
 List<Number> list = new ArrayList<>();
 list.add(1);
 when(map.entrySet().iterator().next().getValue()).thenReturn(list);

 List<Number> deepStubList = (List<Number>) map.entrySet().iterator().next().getValue().iterator().next();
 assertNotNull(deepStubList);
 assertEquals(1, deepStubList.get(0).intValue());
 }

 @Test
 public void testGetMock_whenInvokingMethodOnMockTwice_thenReturnsSameInstance() throws Throwable {
 Map<Integer, List<Number>> map = (Map<Integer, List<Number>>) mockMap;
 List<Number> list = new ArrayList<>();
 list.add(1);
 when(map.entrySet().iterator().next().getValue()).thenReturn(list);
 List<Number> deepStubList1 = (List<Number>) map.entrySet().iterator().next().getValue().iterator().next();
 List<Number> deepStubList2 = (List<Number>) map.entrySet().iterator().next().getValue().iterator().next();
 assertSame(deepStubList1, deepStubList2);
 }

 @Test
 public void testGetMock_whenInvokingMethodOnNonMockableType_thenReturnsEmptyValue() throws Throwable {
 Object nonMockableType = new Object() {};
 when(returnsDeepStubs.getMock(ArgumentMatchers.any(InvocationOnMock.class)))
         .thenThrow(new UnsupportedOperationException("Class is not mockable: " + nonMockableType.getClass().getName()));

 try {
 returnsDeepStubs.answer(ArgumentMatchers.any(InvocationOnMock.class));
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 assertEquals("Class is not mockable: " + nonMockableType.getClass().getName(), e.getMessage());
 }
 }

 @Test
 public void testRecordDeepStubMock() throws Throwable {
 Object mock = PowerMockito.mock(Object.class);
 InternalMockHandler handler = new MockUtil().getMockHandler(mock);
 InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
 doAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return mock;
 }
 }).when(mock).deepStubMockMethod();

 verify(mock, times(1)).deepStubMockMethod();
 assertEquals(mock, container.getInvocations().get(0).getTarget());
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordDeepStubMock_whenTargetIsAlreadyMocked_thenThrowsIllegalStateException() throws Throwable {
 Object mock = PowerMockito.mock(Object.class);
 InternalMockHandler handler = new MockUtil().getMockHandler(mock);
 InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
 doAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return mock;
 }
 }).when(mock).deepStubMockMethod();
 verify(mock, times(1)).deepStubMockMethod();
 verify(mock, times(1)).deepStubMockMethod(); // second call should throw an exception
 }
}