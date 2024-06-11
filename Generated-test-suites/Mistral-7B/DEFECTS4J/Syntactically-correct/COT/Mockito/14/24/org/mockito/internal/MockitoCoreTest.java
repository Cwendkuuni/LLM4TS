import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 }

 @Test
 public void testMockCreation() {
 mock = mockitoCore.mock(String.class, new MockSettingsImpl());
 Assert.assertNotNull(mock);
 }

 @Test
 public void testStubbing() {
 OngoingStubbing stubbing = mockitoCore.stub();
 stubbing.thenReturn("test");
 String result = (String) mockitoCore.when(mock).thenCallRealMethod().thenReturn("result");
 Assert.assertEquals("test", result);
 }

 @Test
 public void testDeprecatedOngoingStubbing() {
 DeprecatedOngoingStubbing stubbing = mockitoCore.stub((String) ArgumentMatchers.any());
 stubbing.thenAnswer(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "answer";
 }
 });
 String result = (String) mockitoCore.when((String) ArgumentMatchers.any()).thenCallRealMethod().thenReturn("result");
 Assert.assertEquals("answer", result);
 }

 @Test
 public void testOngoingStubbing() {
 OngoingStubbing stubbing = mockitoCore.when((String) ArgumentMatchers.any());
 stubbing.thenAnswer(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "answer";
 }
 });
 String result = (String) mockitoCore.when((String) ArgumentMatchers.any());
 Assert.assertEquals("answer", result);
 }

 @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
 public void testNotAMockException() {
 mockitoCore.verify((Object) new Object(), VerificationMode.times(1));
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 Object mock1 = mockitoCore.mock(String.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(String.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
 }

 @Test(expected = IllegalStateException.class)
 public void testReset() {
 mockitoCore.reset(mock);
 mockitoCore.verify(mock);
 }

 @Test
 public void testVerify() {
 mockitoCore.verify(mock, VerificationMode.times(1));
 }

 @Test
 public void testVerifyMode() {
 mockitoCore.verify(mock, VerificationMode.never());
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test(expected = NullPointerException.class)
 public void testNullPassedToVerify() {
 mockitoCore.verify(null, VerificationMode.times(1));
 }

 @Test(expected = NullPointerException.class)
 public void testNullPassedToVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullPassedWhenCreatingInOrder() {
 mockitoCore.inOrder(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullPassedToVerifyNoMoreInteractionsInOrder() {
 mockitoCore.verifyNoMoreInteractionsInOrder(null, Mockito.inOrder(mockitoCore.mock(String.class, new MockSettingsImpl())));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgumentPassedToVerifyNoMoreInteractionsInOrder() {
 mockitoCore.verifyNoMoreInteractionsInOrder(null, mockitoCore.mock(String.class, new MockSettingsImpl()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgumentPassedToInOrder() {
 mockitoCore.inOrder(new Object[]{null, mockitoCore.mock(String.class, new MockSettingsImpl())});
 }

 @Test(expected = IllegalStateException.class)
 public void testVerifyAfterReset() {
 mockitoCore.reset(mock);
 mockitoCore.verify(mock, VerificationMode.times(1));
 }

 @Test(expected = IllegalStateException.class)
 public void testVerifyAfterResetOngoingStubbing() {
 OngoingStubbing stubbing = mockitoCore.when((String) ArgumentMatchers.any());
 stubbing.thenAnswer(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "answer";
 }
 });
 mockitoCore.reset();
 mockitoCore.verify(mock, VerificationMode.times(1));
 }

 @Test(expected = IllegalStateException.class)
 public void testVerifyAfterStubbing() {
 OngoingStubbing stubbing = mockitoCore.when((String) ArgumentMatchers.any());
 stubbing.thenReturn("answer");
 mockitoCore.verify(mock, VerificationMode.times(1));
 }

 @Test(expected = IllegalStateException.class)
 public void testVerifyAfterStubbingVoid() {
 OngoingStubbing stubbing = mockitoCore.stubVoid((String) ArgumentMatchers.any());
 mockitoCore.verify(mock, VerificationMode.times(1));
 }

 @Test(expected = IllegalStateException.class)
 public void testVerifyAfterStubbingAnswer() {
 OngoingStubbing stubbing = mockitoCore.when((String) ArgumentMatchers.any());
 stubbing.thenAnswer(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "answer";
 }
 });
 mockitoCore.verify(mock, VerificationMode.times(1));
 }
}