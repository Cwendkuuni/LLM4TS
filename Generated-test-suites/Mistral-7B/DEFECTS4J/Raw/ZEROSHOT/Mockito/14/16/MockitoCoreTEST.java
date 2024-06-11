package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.AnswerCalledWith;
import org.mockito.stubbing.AnswerReturns;
import org.mockito.stubbing.AnswerThrows;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyOrder;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.api.VerificationData.forObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;
 private Object spy;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mock = mock(Object.class);
 spy = Mockito.spy(Object.class);
 }

 @Test
 public void mock_should_return_mock_instance() {
 Object returnedMock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 assertNotNull(returnedMock);
 assertEquals(Object.class, returnedMock.getClass());
 }

 @Test
 public void mock_should_store_mock_instance_in_mocking_progress() {
 Object returnedMock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 assertNotNull(mockitoCore.mockingProgress.getLatestMock());
 assertEquals(returnedMock, mockitoCore.mockingProgress.getLatestMock());
 }

 @Test
 public void stub_should_return_ongoing_stubbing() {
 IOngoingStubbing ongoingStubbing = mockitoCore.stub();
 assertNotNull(ongoingStubbing);
 }

 @Test
 public void stub_should_throw_exception_when_no_ongoing_stubbing() {
 mockitoCore.reset();
 IOngoingStubbing ongoingStubbing = mockitoCore.stub();
 IOngoingStubbing stubbing = mockitoCore.stub();
 assertNotNull(stubbing);

 try {
 IOngoingStubbing stubbing2 = mockitoCore.stub();
 fail("Should have thrown exception");
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void stub_should_reset_when_pulled() {
 IOngoingStubbing ongoingStubbing = mockitoCore.stub();
 ongoingStubbing.thenReturn("Hello World");
 assertEquals("Hello World", ongoingStubbing.getMock().toString());

 IOngoingStubbing resetStubbing = mockitoCore.stub();
 mockitoCore.reset();
 assertNull(ongoingStubbing.getMock());
 }

 @Test
 public void when_should_return_ongoing_stubbing() {
 Object mockObject = mock(Object.class);
 OngoingStubbing<Object> ongoingStubbing = mockitoCore.when(mockObject);
 assertNotNull(ongoingStubbing);
 }

 @Test
 public void when_should_throw_exception_when_no_ongoing_stubbing() {
 mockitoCore.reset();
 Object mockObject = mock(Object.class);
 OngoingStubbing<Object> ongoingStubbing = mockitoCore.when(mockObject);
 OngoingStubbing<Object> stubbing = mockitoCore.when(mockObject);
 assertNotNull(stubbing);

 try {
 OngoingStubbing<Object> stubbing2 = mockitoCore.when(mockObject);
 fail("Should have thrown exception");
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void verify_should_not_throw_exception_when_mock_is_null() {
 mockitoCore.verify(null, VerificationMode.times(1));
 }

 @Test
 public void verify_should_throw_exception_when_mock_is_not_a_mock() {
 Object notAMock = new Object();
 try {
 mockitoCore.verify(notAMock, VerificationMode.times(1));
 fail("Should have thrown exception");
 } catch (NotAMockException e) {
 // expected
 }
 }

 @Test
 public void reset_should_reset_ongoing_stubbing() {
 mockitoCore.stub().thenReturn("Hello World");
 assertEquals("Hello World", mockitoCore.stub().getMock().toString());
 mockitoCore.reset();
 assertNull(mockitoCore.stub().getMock());
 }

 @Test
 public void reset_should_reset_all_mocks() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 mockitoCore.reset();
 assertNull(mockitoCore.mockingProgress.getLatestMock());
 }

 @Test
 public void verifyNoMoreInteractions_should_verify_no_more_interactions_on_mocks() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 mockitoCore.verifyNoMoreInteractions(mock1, mock2);
 }

 @Test
 public void verifyNoMoreInteractions_should_throw_exception_when_mocks_are_null() {
 try {
 mockitoCore.verifyNoMoreInteractions((Object[]) null);
 fail("Should have thrown exception");
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void verifyNoMoreInteractions_should_throw_exception_when_mocks_contain_null() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 Object nullMock = null;
 try {
 mockitoCore.verifyNoMoreInteractions(mock1, mock2, nullMock);
 fail("Should have thrown exception");
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void verifyNoMoreInteractions_should_throw_exception_when_mocks_are_not_mocks() {
 Object notAMock1 = new Object();
 Object notAMock2 = new Object();
 try {
 mockitoCore.verifyNoMoreInteractions(notAMock1, notAMock2);
 fail("Should have thrown exception");
 } catch (NotAMockException e) {
 // expected
 }
 }

 @Test
 public void verify_should_store_verification_data_in_mocking_progress() {
 Object mockObject = mock(Object.class);
 mockitoCore.verify(mockObject, VerificationMode.atLeastOnce());
 VerificationData data = mockitoCore.mockingProgress.getLatestVerificationData();
 assertNotNull(data);
 }

 @Test
 public void verifyInOrder_should_verify_interactions_in_order() {
 Object mockObject1 = mock(Object.class);
 Object mockObject2 = mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mockObject1, mockObject2);
 mockitoCore.when(mockObject1.toString()).thenReturn("mockObject1");
 mockitoCore.when(mockObject2.toString()).thenReturn("mockObject2");
 mockitoCore.verify(mockObject1).someMethod();
 mockitoCore.verify(mockObject2).someMethod();
 verifyOrder(inOrder);
 }

 @Test
 public void verifyInOrder_should_throw_exception_when_mocks_are_null() {
 try {
 InOrder inOrder = mockitoCore.inOrder((Object[]) null);
 fail("Should have thrown exception");
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void verifyInOrder_should_throw_exception_when_mocks_contain_null() {
 Object mockObject1 = mock(Object.class);
 Object nullMock = null;
 try {
 InOrder inOrder = mockitoCore.inOrder(mockObject1, nullMock);
 fail("Should have thrown exception");
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void verifyInOrder_should_throw_exception_when_mocks_are_not_mocks() {
 Object notAMockObject1 = new Object();
 Object notAMockObject2 = new Object();
 try {
 InOrder inOrder = mockitoCore.inOrder(notAMockObject1, notAMockObject2);
 fail("Should have thrown exception");
 } catch (NotAMockException e) {
 // expected
 }
 }

 @Test
 public void inOrder_should_return_in_order_context() {
 Object mockObject1 = mock(Object.class);
 Object mockObject2 = mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mockObject1, mockObject2);
 assertNotNull(inOrder);
 }

 @Test
 public void doAnswer_should_return_stubber() {
 Object mockObject = mock(Object.class);
 Stubber stubber = mockitoCore.doAnswer(Answer.callRealMethod());
 assertNotNull(stubber);
 }

 @Test
 public void stubVoid_should_return_void_method_stubbable() {
 Object mockObject = mock(Object.class);
 VoidMethodStubbable<Object> voidMethodStubbable = mockitoCore.stubVoid(mockObject);
 assertNotNull(voidMethodStubbable);
 }

 @Test
 public void validateMockitoUsage_should_validate_mockito_usage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Object mockObject = mock(Object.class);
 IOngoingStubbing ongoingStubbing = mockitoCore.stub(mockObject);
 ongoingStubbing.thenReturn("Hello World");
 assertEquals("Hello World", ongoingStubbing.getMock().toString());
 assertEquals(new InvocationOnMock("toString()", mockObject), mockitoCore.getLastInvocation());
 }
}