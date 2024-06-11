package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.withSettings;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

 private static Class<ReturnsDeepStubs> returnsDeepStubsClass;
 private static Class<ReturnsEmptyValues> returnsEmptyValuesClass;
 private static MockSettings mockSettings;
 private static InternalMockHandler<Object> mockHandler;
 private static MockitoCore mockitoCore;

 @BeforeClass
 public static void setupClass() {
 mockStatic(Mockito.class);
 mockitoCore = MockitoCore.getInstance();
 mockSettings = withSettings().serializable().defaultAnswer(Answers.RETURNS_DEEP_STUBS);
 mockHandler = new MockUtil().getMockHandler(mock(Map.class, mockSettings));
 returnsDeepStubsClass = ReturnsDeepStubs.class;
 returnsEmptyValuesClass = ReturnsEmptyValues.class;
 }

 @Test
 public void testAnswer() throws Exception {
 Object mock = mock(Map.class, mockSettings);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(mock);
 Object returnValue = new ReturnsDeepStubs().answer(invocation);
 assertSame(mock, returnValue);
 }

 @Test
 public void testNewDeepStubMock() throws Exception {
 Object returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
 Object deepStubMock = new ReturnsDeepStubs().newDeepStubMock(returnTypeGenericMetadata);
 assertSame(mockitoCore().mock(returnTypeGenericMetadata.rawType(), withSettings(mockSettings)).getMock(), deepStubMock);
 }

 @Test
 public void testRecordDeepStubAnswer() throws Exception {
 Object container = mock(InvocationContainerImpl.class);
 Object mock = mock(Object.class, withSettings().serializable());
 when(container.addAnswer((Answer<Object>) any(), anyBoolean())).thenReturn(mock);
 Object answer = new ReturnsDeepStubs().recordDeepStubAnswer(mock, container);
 assertSame(mock, answer);
 }

 @Test
 public void testReturnsDeepStubsAnswerUsing() throws Exception {
 Object returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs().returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
 assertSame(returnTypeGenericMetadata, returnsDeepStubs.actualParameterizedType(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTypeMockableWithNonMockableType() throws Exception {
 GenericMetadataSupport nonMockableMetadata = mock(GenericMetadataSupport.class);
 when(mockitoCore().isTypeMockable(nonMockableMetadata.rawType())).thenReturn(false);
 new ReturnsDeepStubs().answer(mock(InvocationOnMock.class));
 }

 @Test
 public void testAnswerWithDeepStub() throws Exception {
 Object container = mock(InvocationContainerImpl.class);
 Object mock = new ReturnsDeepStubs().newDeepStubMock(mock(GenericMetadataSupport.class));
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(mock);
 when(container.getInvocationForStubbing()).thenReturn(invocation);
 when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());
 Object answer = new ReturnsDeepStubs().answer(invocation);
 assertSame(mock, answer);
 }

 @Test
 public void testAnswerWithDeepStubFromContainer() throws Exception {
 Object container = mock(InvocationContainerImpl.class);
 Object mock = new ReturnsDeepStubs().newDeepStubMock(mock(GenericMetadataSupport.class));
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 Object stubbedMock = mock(Object.class, withSettings().serializable());
 when(container.getInvocationForStubbing()).thenReturn(invocation);
 when(container.addAnswer((Answer<Object>) any(), anyBoolean())).thenReturn(stubbedMock);
 when(container.getStubbedInvocations()).thenReturn(Arrays.asList(new StubbedInvocationMatcher(stubbedMock)));
 Object answer = new ReturnsDeepStubs().answer(invocation);
 assertSame(stubbedMock, answer);
 }

 @Test
 public void testDeepStubsWithSameGenericInfo() throws Exception {
 Object container = mock(InvocationContainerImpl.class);
 Object mockA = new ReturnsDeepStubs().newDeepStubMock(mock(GenericMetadataSupport.class));
 Object mockB = new ReturnsDeepStubs().newDeepStubMock(mock(GenericMetadataSupport.class));
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(container.getInvocationForStubbing()).thenReturn(invocation);
 when(container.getStubbedInvocations()).thenReturn(Arrays.asList(
 new StubbedInvocationMatcher(mockA),
 new StubbedInvocationMatcher(mockB)
 ));
 Object answerA = new ReturnsDeepStubs().answer(invocation);
 Object answerB = new ReturnsDeepStubs().answer(invocation);
 assertSame(mockA, answerA);
 assertSame(mockB, answerB);
 }

 @Test
 public void testDeepStubsWithDifferentGenericInfo() throws Exception {
 Object container = mock(InvocationContainerImpl.class);
 Object mockA = new ReturnsDeepStubs().newDeepStubMock(mock(GenericMetadataSupport.class));
 Object mockB = new ReturnsDeepStubs().newDeepStubMock(mock(GenericMetadataSupport.class, "TypeB"));
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(container.getInvocationForStubbing()).thenReturn(invocation);
 when(container.getStubbedInvocations()).thenReturn(Arrays.asList(
 new StubbedInvocationMatcher(mockA),
 new StubbedInvocationMatcher(mockB)
 ));
 Object answerA = new ReturnsDeepStubs().answer(invocation);
 Object answerB = new ReturnsDeepStubs().answer(invocation);
 assertNotSame(mockA, answerB);
 }

 @Test
 public void testAnswerWithNullInvocation() throws Exception {
 Object answer = new ReturnsDeepStubs().answer(null);
 assertNull(answer);
 }
}

class StubbedInvocationMatcher implements Answer<Object> {
 private final Object mock;

 StubbedInvocationMatcher(Object mock) {
 this.mock = mock;
 }

 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return mock;
 }
}