package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private MockitoCore mockitoCoreMock;
 private InternalMockHandler<Object> mockHandler;
 private InvocationContainerImpl invocationContainer;
 private Object deepStubMock;

 @Before
 public void setUp() {
 mockitoCoreMock = Mockito.mock(MockitoCore.class);
 returnsDeepStubs = new ReturnsDeepStubs();
 returnsDeepStubs.mockitoCore = mockitoCoreMock;
 mockHandler = new MockUtil().getMockHandler(Mockito.mock(Object.class));
 invocationContainer = (InvocationContainerImpl) mockHandler.getInvocationContainer();
 deepStubMock = returnsDeepStubs.createNewDeepStubMock(Mockito.mock(GenericMetadataSupport.class));
 }

 @Test
 public void testGetMockWithMatchingStub() throws Throwable {
 // given
 when(mockitoCoreMock.isTypeMockable(ArgumentMatchers.eq(Integer.class))).thenReturn(true);
 when(mockitoCoreMock.mock(ArgumentMatchers.eq(Integer.class), ArgumentMatchers.any())).thenReturn(1);
when(invocationContainer.getStubbedInvocations()).thenReturn(new ArrayList<>());

 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

 StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);
 invocationContainer.addStubbedInvocation(stubbedInvocationMatcher);
 stubbedInvocationMatcher.answer = (InvocationOnMock invocationArg) -> 2;

 // when
 Object result = returnsDeepStubs.answer(invocation);

 // then
 verify(stubbedInvocationMatcher).answer(invocation);
 assertEquals(2, result);
 }

 @Test(expected = Throwable.class)
 public void testGetMockWithNonMatchingStub() throws Throwable {
 // given
 when(mockitoCoreMock.isTypeMockable(ArgumentMatchers.eq(String.class))).thenReturn(false);

 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

 // when
 Object result = returnsDeepStubs.answer(invocation);

 // then
 throw new AssertionError("Expected exception, but no exception was thrown.");
 }

 @Test
 public void testCreateNewDeepStubMock() throws Throwable {
 // given
 when(mockitoCoreMock.isTypeMockable(ArgumentMatchers.eq(List.class))).thenReturn(true);

 // when
 Object deepStubMock = returnsDeepStubs.createNewDeepStubMock(Mockito.mock(GenericMetadataSupport.class));

 // then
 verify(mockitoCoreMock).mock(ArgumentMatchers.eq(List.class), ArgumentMatchers.any());
 assertEquals(List.class, deepStubMock.getClass());
 }

 @Test(expected = Throwable.class)
 public void testCreateNewDeepStubMockWithUnmockableType() throws Throwable {
 // given
 when(mockitoCoreMock.isTypeMockable(ArgumentMatchers.eq(Boolean.class))).thenReturn(false);

 // when
 Object deepStubMock = returnsDeepStubs.createNewDeepStubMock(Mockito.mock(GenericMetadataSupport.class));

 // then
 throw new AssertionError("Expected exception, but no exception was thrown.");
 }

 @Test
 public void testRecordDeepStubMock() throws Throwable {
 // given
 when(mockitoCoreMock.isTypeMockable(ArgumentMatchers.eq(Object[].class))).thenReturn(true);
 Object mock = Mockito.mock(Object[].class);
 when(invocationContainer.getInvocationForStubbing()).thenReturn(Mockito.mock(InvocationOnMock.class));
 when(invocationContainer.addAnswer(ArgumentMatchers.any(), ArgumentMatchers.eq(false))).thenReturn(1);

 // when
 Object deepStubMock = returnsDeepStubs.recordDeepStubMock(mock, invocationContainer);

 // then
 verify(invocationContainer).addAnswer(ArgumentMatchers.any(), ArgumentMatchers.eq(deepStubMock));
 assertEquals(deepStubMock, mock);
 }

 @Test(expected = Throwable.class)
 public void testRecordDeepStubMockWithNonMockableType() throws Throwable {
 // given
 when(mockitoCoreMock.isTypeMockable(ArgumentMatchers.eq(String[].class))).thenReturn(false);
 Object mock = Mockito.mock(String[].class);
 when(invocationContainer.getInvocationForStubbing()).thenReturn(Mockito.mock(InvocationOnMock.class));
 when(invocationContainer.addAnswer(ArgumentMatchers.any(), ArgumentMatchers.eq(false))).thenReturn(1);

 // when
 returnsDeepStubs.recordDeepStubMock(mock, invocationContainer);

 // then
 throw new AssertionError("Expected exception, but no exception was thrown.");
 }
}