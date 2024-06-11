package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 // Test for getMock method when invocation matches a stubbed invocation
 @Test
 public void testGetMockWithStubbedInvocation() {
  interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
  }

  GenericsNest<Number> mock = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);
  Number number = mock.entrySet().iterator().next().getValue().iterator().next();

  // Stubbed invocation match
  Number numberStub = mock(Number.class, Answers.RETURNS_MOCK);
  mock.entrySet().iterator().next().setValue(numberStub);

  // Call the getMock method with the same invocation as the stubbed one
  Number actualNumber = mock.entrySet().iterator().next().getValue().iterator().next();

  // Verify that the returned deep stub is the same as the stubbed one
  verify(numberStub).equals(actualNumber);
 }

 // Test for getMock method when invocation does not match any stubbed invocation
 @Test(expected = IllegalStateException.class)
 public void testGetMockWithNoMatchingStubbedInvocation() {
  interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
  }

  GenericsNest<Number> mock = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);

  // Call the getMock method with an invocation that does not match any stubbed invocation
  Object actualMock = mock.entrySet().iterator().next().getValue().iterator().next();

  // This test should throw an IllegalStateException as no deep stub was created
  Mockito.verifyNoMoreInteractions(actualMock);
 }

 // Test for createNewDeepStubMock method when the return type is not mockable
 @Test(expected = IllegalStateException.class)
 public void testCreateNewDeepStubMockWithUnmockableType() {
  class UnmockableClass {
  }

  ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

  try {
   UnmockableClass mock = returnsDeepStubs.createNewDeepStubMock(GenericMetadataSupport.forType(UnmockableClass.class));
  } catch (IllegalStateException e) {
   // Expected exception
  }
 }

 // Test for recordDeepStubMock method when adding an answer to the invocation container
 @Test
 public void testRecordDeepStubMock() {
  interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
  }

  GenericsNest<Number> mock = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);

  // Mock a deep stub for the getMock method
  GenericsNest<Number> deepStub = mock(GenericsNest.class, Answers.RETURNS_MOCK);
  mock.answer(new Answer<Object>() {
   @Override
   public Object answer(InvocationOnMock invocation) throws Throwable {
    return deepStub;
   }
  });

  // Call the getMock method and verify that the deep stub was returned
  GenericsNest<Number> actualMock = mock.entrySet().iterator().next().getValue().iterator().next();
  verify(deepStub).equals(actualMock);
 }

 // Test for recordDeepStubMock method when adding an answer to the invocation container with verify-mode set to false
 @Test
 public void testRecordDeepStubMockWithVerifyModeFalse() {
  interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
  }

  GenericsNest<Number> mock = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);

  // Mock a deep stub for the getMock method with verify-mode set to false
  GenericsNest<Number> deepStub = mock(GenericsNest.class, Answers.RETURNS_MOCK);
  mock.answer(new Answer<Object>() {
   @Override
   public Object answer(InvocationOnMock invocation) throws Throwable {
    return deepStub;
   }
  }, false);

  // Call the getMock method and verify that the deep stub was not verified
  GenericsNest<Number> actualMock = mock.entrySet().iterator().next().getValue().iterator().next();
  Mockito.verifyNoMoreInteractions(deepStub);
 }
}