import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.MockUtil;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private MockingProgress mockingProgress;
 private MockUtil mockUtil;
 private Object mock;
 private Answer answer;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mockitoCore = new MockitoCore();
 mockingProgress = mockitoCore.mockingProgress;
 mockUtil = mockitoCore.mockUtil;
 }

 @Test
 public void testMockCreation() {
 Class<?> mockClass = Mockito.class;
 MockSettings mockSettings = new MockSettingsImpl();
 T mock = mockitoCore.mock(mockClass, mockSettings, false);
 Assert.assertNotNull(mock);
 }

 @Test
 public void testMockCreationWithResetOngoingStubbingTrue() {
 Class<?> mockClass = Mockito.class;
 MockSettings mockSettings = new MockSettingsImpl();
 T mock = mockitoCore.mock(mockClass, mockSettings, true);
 Assert.assertNotNull(mock);
 // Add test to check if ongoing stubbing is reset
 }

 @Test(expected = RuntimeException.class)
 public void testMockCreationWithInvalidMockSettings() {
 Class<?> mockClass = Mockito.class;
 MockSettings mockSettings = null;
 T mock = mockitoCore.mock(mockClass, mockSettings, false);
 }

 @Test
 public void testStubbing() {
 mock = Mockito.mock(Object.class);
 IOngoingStubbing stubbing = mockitoCore.stub();
 stubbing.thenReturn("testValue");
 String result = (String) mock.invokeMethod("testMethod", new Object[]{});
 Assert.assertEquals("testValue", result);
 }

 @Test
 public void testStubbingDeprecated() {
 mock = Mockito.mock(Object.class);
 DeprecatedOngoingStubbing<Object> deprecatedStubbing = mockitoCore.stub(mock);
 deprecatedStubbing.thenReturn("testValue");
 String result = (String) mock.invokeMethod("testMethod", new Object[]{});
 Assert.assertEquals("testValue", result);
 }

 @Test
 public void testStubbingWhen() {
 mock = Mockito.mock(Object.class);
 OngoingStubbing<Object> whenStubbing = mockitoCore.when(mock);
 whenStubbing.thenReturn("testValue");
 String result = (String) mock.invokeMethod("testMethod", new Object[]{});
 Assert.assertEquals("testValue", result);
 }

 @Test
 public void testVerification() {
 mock = Mockito.mock(Object.class);
 mockitoCore.verify(mock, VerificationMode.RETURN);
 }

 @Test(expected = AssertionError.class)
 public void testVerificationWithNullMock() {
 mockitoCore.verify(null, VerificationMode.RETURN);
 }

 @Test(expected = AssertionError.class)
 public void testVerificationWithNotAMock() {
 Object notAMock = new Object();
 mockitoCore.verify(notAMock, VerificationMode.RETURN);
 }

 @Test
 public void testReset() {
 mock = Mockito.mock(Object.class);
 mockitoCore.reset(mock);
 }

 @Test
 public void testResetWithMultipleMocks() {
 mock = Mockito.mock(Object1.class);
 Object obj2 = new Object2();
 mockitoCore.reset(mock, obj2);
 }

 @Test(expected = AssertionError.class)
 public void testResetWithNullMock() {
 mockitoCore.reset(null);
 }

 @Test(expected = AssertionError.class)
 public void testResetWithNullMock2() {
 mockitoCore.reset(null, new Object2());
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mock = Mockito.mock(Object.class);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsWithNullMock() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsWithNotAMock() {
 Object notAMock = new Object();
 mockitoCore.verifyNoMoreInteractions(notAMock);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsWithMultipleMocks() {
 mock = Mockito.mock(Object1.class);
 Object obj2 = new Object2();
 mockitoCore.verifyNoMoreInteractions(mock, obj2);
 }

 @Test
 public void testInOrder() {
 mock = Mockito.mock(Object.class);
 Object mock2 = Mockito.mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock, mock2);
 }

 @Test
 public void testDoAnswer() {
 mock = Mockito.mock(Object.class);
 answer = Mockito.answer(new Answer() {
 @Override public Object answer(InvocationOnMock invocation) throws Throwable {
 return "testValue";
 }
 });
 Stubber stubber = mockitoCore.doAnswer(answer);
 stubber.doAnswer(answer).thenReturn("testValue2");
 String result = (String) mock.invokeMethod("testMethod", new Object[]{});
 Assert.assertEquals("testValue", result);
 }

 @Test
 public void testStubVoid() {
 mock = Mockito.mock(Object.class);
 VoidMethodStubbable<Object> voidMethodStubbable = mockitoCore.stubVoid(mock);
 voidMethodStubbable.thenReturnVoid();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test(expected = RuntimeException.class)
 public void testGetLastInvocationWithNoInvocations() {
 mockitoCore.getLastInvocation();
 }

 @Test
 public void testGetLastInvocationWithInvocations() throws InvocationTargetException {
 mock = Mockito.mock(Object.class);
 OngoingStubbing<Object> whenStubbing = mockitoCore.when(mock);
 whenStubbing.thenReturn("testValue");
 Object[] arguments = new Object[]{};
 Invocation invocation = mockitoCore.getLastInvocation();
 Assert.assertEquals("testValue", invocation.getArgument(0));
 Assert.assertEquals(mock.getClass(), invocation.getMock());
 Assert.assertEquals("testMethod", invocation.getMethod().getName());
 Assert.assertEquals(arguments, invocation.getArguments());
 }
}