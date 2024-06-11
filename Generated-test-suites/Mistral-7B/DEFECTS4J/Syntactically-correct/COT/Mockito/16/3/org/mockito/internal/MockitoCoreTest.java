import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.creation.MockType;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.AnswerStub;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;
 private Object stub;
 private InOrder inOrder;
 private AnswerStub answerStub;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mock = mock(Object.class);
 stub = mock(Object.class);
 inOrder = inOrder(mock, stub);
 answerStub = AnswerStub.returnValues(1);
 }

 // Test for mock() method
 @Test
 public void testMock() {
 MockSettings mockSettings = new MockSettingsImpl();
 MockType mockType = mockitoCore.mock(Object.class, mockSettings, false);
 assertNotNull(mockType);
 assertEquals(mock.getClass(), mockType.getMock());
 }

 // Test for stub() method
 @Test
 public void testStub() {
 IOngoingStubbing ongoingStubbing = mockitoCore.stub();
 assertNotNull(ongoingStubbing);
 assertEquals(mockitoCore.stub(), ongoingStubbing);
 }

 // Test for when() method
 @Test
 public void testWhen() {
 when(mock).thenReturn(1);
 assertEquals(1, mockitoCore.verify(mock).someMethodCall());
 }

 // Test for verify() method
 @Test(expected = AssertionError.class)
 public void testVerifyNullMock() {
 mockitoCore.verify(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNotAMock() {
 mockitoCore.verify(new Object());
 }

 @Test
 public void testVerify() {
 mockitoCore.verify(mock, VerificationMode.times(1));
 }

 // Test for reset() method
 @Test
 public void testReset() {
 mockitoCore.reset(mock, stub);
 assertEquals(0, mockitoCore.getLastInvocation().getInvocationType().getNumberOfInvocations());
 }

 // Test for verifyNoMoreInteractions() method
 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNull() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNotAMock() {
 mockitoCore.verifyNoMoreInteractions(new Object());
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(mock, stub);
 }

 // Test for inOrder() method
 @Test
 public void testInOrder() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 inOrder(mock1, mock2).verify(mock1).someMethodCall();
 inOrder(mock1, mock2).verify(mock2).someOtherMethodCall();
 }

 // Test for doAnswer() method
 @Test
 public void testDoAnswer() {
 when(mock).thenAnswer(answerStub);
 assertEquals(1, mockitoCore.verify(mock).someMethodCall());
 }

 // Test for stubVoid() method
 @Test
 public void testStubVoid() {
 mockitoCore.stubVoid(mock).someVoidMethod();
 verify(mock).someVoidMethod();
 }

 // Test for validateMockitoUsage() method
 @Test(expected = AssertionError.class)
 public void testValidateMockitoUsageNullMock() {
 mockitoCore.validateMockitoUsage(null);
 }

 @Test(expected = AssertionError.class)
 public void testValidateMockitoUsageNotAMock() {
 mockitoCore.validateMockitoUsage(new Object());
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage(mock);
 }

 // Test for getLastInvocation() method
 @Test
 public void testGetLastInvocation() {
 when(mock.someMethodCall()).thenReturn(1);
 mockitoCore.verify(mock).someMethodCall();
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 assertEquals(1, lastInvocation.getArgumentValues()[0]);
 }
}