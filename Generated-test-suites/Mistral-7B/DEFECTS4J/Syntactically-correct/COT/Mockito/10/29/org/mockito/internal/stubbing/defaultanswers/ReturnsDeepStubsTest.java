package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.withSettings;

public class ReturnsDeepStubsTest {

 private static final Class<ReturnsDeepStubs> RETURNS_DEEP_STUBS_CLASS = ReturnsDeepStubs.class;

 @Test
 public void testAnswerMethodWithMock() {
  Object mock = Mockito.mock(Map.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
  InvocationOnMock invocation = mockMethodCall(mock, "entrySet");
  ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
  Object returnValue = returnsDeepStubs.answer(invocation);
  Assert.assertSame(mock, returnValue);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswerMethodWithNonMockableType() throws Throwable {
  Object mock = Mockito.mock(IOException.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
  InvocationOnMock invocation = mockMethodCall(mock, "close");
  ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
  returnsDeepStubs.answer(invocation);
 }

 @Test
 public void testDeepStub() throws Throwable {
  Object mock = Mockito.mock(Map.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
  InvocationOnMock invocation = mockMethodCall(mock, "entrySet").get(0).getValue().iterator().next();
  ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
  Object returnValue = returnsDeepStubs.deepStub(invocation, null);
  Assert.assertSame(mock, returnValue);
 }

 @Test
 public void testNewDeepStubMock() throws Throwable {
  Class<Map> mapClass = Map.class;
  GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
  when(returnTypeGenericMetadata.rawType()).thenReturn(mapClass);
  when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
  ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
  Object mock = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);
  Assert.assertSame(mapClass, mock.getClass());
 }

 @Test
 public void testWithSettingsUsing() throws Throwable {
  Class<Map> mapClass = Map.class;
  GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
  when(returnTypeGenericMetadata.rawType()).thenReturn(mapClass);
  when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
  when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[] { Set.class });
  Object mock = Mockito.mock(Map.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
  MockSettings mockSettings = Mockito.mock(MockSettings.class);
  when(mockSettings.serializable()).thenReturn(true);
  when(mockSettings.defaultAnswer(ArgumentMatchers.any())).thenReturn(Answers.RETURNS_DEEP_STUBS);
  Mockito.doReturn(mockSettings).when(mock).getMockSettings();
  ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
  Object deepStubMock = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
  Assert.assertSame(mapClass, deepStubMock.getClass());
  Assert.assertSame(mockSettings, deepStubMock.getMockSettings());
 }

 @Test
 public void testRecordDeepStubAnswer() throws Throwable {
  Object mock = Mockito.mock(Map.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
  InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
  when(container.getInvocationForStubbing()).thenReturn(mockMethodCall(mock, "entrySet").get(0));
  ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
  Object returnValue = returnsDeepStubs.recordDeepStubAnswer(mock, container);
  Assert.assertSame(mock, returnValue);
 }

 @Test(expected = NullPointerException.class)
 public void testRecordDeepStubAnswerWithNullMock() throws Throwable {
  Object mock = null;
  InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
  ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
  returnsDeepStubs.recordDeepStubAnswer(mock, container);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeepStubWithNullInvocationContainer() throws Throwable {
  Object mock = Mockito.mock(Map.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
  InvocationContainerImpl container = null;
  ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
  returnsDeepStubs.deepStub(mockMethodCall(mock, "entrySet").get(0), container);
 }

 @Test
 public void testDeepStubWithStubbedInvocationMatcher() throws Throwable {
  Object mock = Mockito.mock(Map.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
  InvocationOnMock stubbedInvocation = mockMethodCall(mock, "entrySet").get(0);
  StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);
  when(stubbedInvocationMatcher.getInvocation()).thenReturn(stubbedInvocation);
  when(stubbedInvocationMatcher.answer(any(InvocationOnMock.class))).thenReturn(Mockito.mock(Number.class));
  InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
  when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
  ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
  Object returnValue = returnsDeepStubs.deepStub(stubbedInvocation, null);
  Assert.assertSame(Mockito.mock(Number.class), returnValue);
 }

 @Test(expected = IllegalStateException.class)
 public void testDeepStubWithStubbedInvocationMatcherTwice() throws Throwable {
  Object mock = Mockito.mock(Map.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
  InvocationOnMock stubbedInvocation = mockMethodCall(mock, "entrySet").get(0);
  StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);
  when(stubbedInvocationMatcher.getInvocation()).thenReturn(stubbedInvocation);
  when(stubbedInvocationMatcher.answer(any(InvocationOnMock.class))).thenReturn(Mockito.mock(Number.class));
  InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
  when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
  ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
  Object firstReturnValue = returnsDeepStubs.deepStub(stubbedInvocation, null);
  Object secondReturnValue = returnsDeepStubs.deepStub(stubbedInvocation, null);
 }
}