package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.internal.answers.ReturnsDeepStubs;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock;
 private InvocationContainerImpl container;

 @BeforeEach
 void setUp() {
 mock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
 returnsDeepStubs = Mockito.spy(new ReturnsDeepStubs());
 container = Mockito.spy(new InvocationContainerImpl());
 }

 @Test
 void testAnswerWithDeepStub() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeGenericMetadata = GenericMetadataSupport.forClass(Object.class);

 when(returnsDeepStubs.deepStub(eq(invocation), eq(returnTypeGenericMetadata)))
 .thenReturn(new Object() {});

 Object answer = returnsDeepStubs.answer(invocation);
 verify(container).addAnswer(argThat(isInstanceOf(DeeplyStubbedAnswer.class)), eq(false));
 assertEquals(mock, answer);
 }

 @Test
 void testAnswerWithDelegate() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

 when(container.getStubbedInvocations()).thenReturn(new ArrayList<>());
 when(mockitoCore().isTypeMockable(Object.class)).thenReturn(true);

 Object answer = returnsDeepStubs.answer(invocation);
 verify(container, times(0)).addAnswer(argThat(isInstanceOf(DeeplyStubbedAnswer.class)), eq(false));
 assertEquals(delegate().returnValueFor(Object.class), answer);
 }

 @Test
 void testNewDeepStubMock() {
 GenericMetadataSupport returnTypeGenericMetadata = GenericMetadataSupport.forClass(List.class);
 Object mock = newDeepStubMock(returnTypeGenericMetadata);
 verify(mockitoCore()).mock(List.class, argThat(withSettingsUsing(returnTypeGenericMetadata)));
 }

 @Test
 void testRecordDeepStubAnswer() {
 Object mock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object answer = returnsDeepStubs.recordDeepStubAnswer(mock, container);
 verify(container).addAnswer(argThat(isInstanceOf(DeeplyStubbedAnswer.class)), eq(false));
 assertEquals(mock, answer);
 }

 @Test
 void testActualParameterizedType() {
 Object mock = mock(List.class);
 CreationSettings mockSettings = MockUtil.getMockSettings(mock);
 GenericMetadataSupport actualParameterizedType = returnsDeepStubs.actualParameterizedType(mock);
 assertEquals(mockSettings.getTypeToMock().getType(), actualParameterizedType.getType());
 }
}

class NewDeepStubMock {

 @Test
 void test() {
 Object mock = newReturnsDeepStubs().newDeepStubMock(GenericMetadataSupport.forClass(List.class));
 verify(mockitoCore()).mock(List.class, argThat(withSettingsUsing(GenericMetadataSupport.forClass(List.class))));
 }

 private ReturnsDeepStubs newReturnsDeepStubs() {
 return Mockito.spy(new ReturnsDeepStubs());
 }
}

class DeeplyStubbedAnswer {

 @Test
 void test() {
 Object mock = mock(Object.class);
 DeeplyStubbedAnswer deeplyStubbedAnswer = new DeeplyStubbedAnswer(mock);
 assertEquals(mock, deeplyStubbedAnswer.answer(Mockito.mock(InvocationOnMock.class)));
 }
}

class LazyHolder {

 @Test
 void test() {
 MockitoCore mockitoCore = new MockitoCore();
 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
 assertEquals(mockitoCore, LazyHolder.MOCKITO_CORE);
 assertEquals(returnsEmptyValues, LazyHolder.DELEGATE);
 }
}