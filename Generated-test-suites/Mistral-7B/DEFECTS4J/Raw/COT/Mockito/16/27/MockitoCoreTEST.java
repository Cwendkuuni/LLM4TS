import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.VerificationModeFactory;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.internal.stubbing.Stubber;
import org.mockito.internal.verification.api.Stubbing;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

 private Reporter reporter;
 private MockUtil mockUtil;
 private MockingProgress mockingProgress;
 private Stubbing stubbing;
 private Invocation lastInvocation;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 reporter = MockitoCore.new Reporter();
 mockUtil = MockitoCore.new MockUtil();
 mockingProgress = MockitoCore.new ThreadSafeMockingProgress();
 }

 @Test
 public void testMock() {
 Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
 MockSettings mockSettings = new MockSettingsImpl();
 MockitoCore mockitoCore = MockitoCore.mock(mockitoCoreClass, mockSettings);
 assertNotNull(mockitoCore);
 }

 @Test
 public void testMockWithNullMockSettings() {
 Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
 MockSettings mockSettings = null;
 try {
 MockitoCore.mock(mockitoCoreClass, mockSettings);
 FailureHandler.fail("Expected NotNullException for null MockSettings");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testStub() {
 IOngoingStubbing stubbing = MockitoCore.stub();
 assertNotNull(stubbing);
 }

 @Test
 public void testStubWithNullStubbing() {
 IOngoingStubbing stubbing = null;
 try {
 MockitoCore.stub(stubbing);
 FailureHandler.fail("Expected IllegalStateException for null stubbing");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testWhen() {
 Object mock = Mockito.mock(Object.class);
 OngoingStubbing<Object> whenStubbing = MockitoCore.when(mock);
 whenStubbing.thenReturn("test");
 assertEquals("test", Mockito.invoke(mock, ""));
 }

 @Test
 public void testVerify() {
 Object mock = Mockito.mock(Object.class);
 try {
 MockitoCore.verify(mock, VerificationModeFactory.verifyNoMoreInteractions());
 } catch (AssertionError e) {
 // Expected
 }
 }

 @Test
 public void testVerifyWithNullMock() {
 try {
 MockitoCore.verify(null, VerificationModeFactory.verifyNoMoreInteractions());
 FailureHandler.fail("Expected NullPointerException for null mock");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testVerifyWithNotAMock() {
 Object notAMock = new Object();
 try {
 MockitoCore.verify(notAMock, VerificationModeFactory.verifyNoMoreInteractions());
 FailureHandler.fail("Expected NotAMockException for not a mock object");
 } catch (NotAMockException e) {
 // Expected
 }
 }

 @Test
 public void testReset() {
 Object mock = Mockito.mock(Object.class);
 MockitoCore.reset(mock);
 assertEquals(0, Mockito.mockingDetails(mock).getInvocations().size());
 }

 @Test
 public void testResetWithNullMock() {
 try {
 MockitoCore.reset(null);
 FailureHandler.fail("Expected NullPointerException for null mock");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testResetWithArrayOfNullMocks() {
 Object[] mocks = new Object[0];
 try {
 MockitoCore.reset(mocks);
 FailureHandler.fail("Expected IllegalArgumentException for null mocks array");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock = Mockito.mock(Object.class);
 Mockito.verify(mock, VerificationModeFactory.verifyNoMoreInteractions());
 }

 @Test
 public void testVerifyNoMoreInteractionsWithNullMock() {
 try {
 MockitoCore.verifyNoMoreInteractions(null);
 FailureHandler.fail("Expected NullPointerException for null mock");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testVerifyNoMoreInteractionsWithNotAMock() {
 Object notAMock = new Object();
 try {
 MockitoCore.verifyNoMoreInteractions(notAMock);
 FailureHandler.fail("Expected NotAMockException for not a mock object");
 } catch (NotAMockException e) {
 // Expected
 }
 }

 @Test
 public void testInOrder() {
 Object mock1 = Mockito.mock(Object.class);
 Object mock2 = Mockito.mock(Object.class);
 InOrder inOrder = MockitoCore.inOrder(mock1, mock2);
 }

 @Test
 public void testInOrderWithNullMocks() {
 try {
 MockitoCore.inOrder(null);
 FailureHandler.fail("Expected IllegalArgumentException for null mocks array");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testInOrderWithNotAMock() {
 Object notAMock = new Object();
 try {
 MockitoCore.inOrder(notAMock);
 FailureHandler.fail("Expected NotAMockException for not a mock object");
 } catch (NotAMockException e) {
 // Expected
 }
 }

 @Test
 public void testDoAnswer() {
 Object mock = Mockito.mock(Object.class);
 Answer<String> answer = invocation -> invocation.getArgument(0).toString();
 Stubber stubber = MockitoCore.doAnswer(answer);
 stubber.stub(mock);
 assertEquals("test", Mockito.invoke(mock, "test"));
 }

 @Test
 public void testStubVoid() {
 Object mock = Mockito.mock(Object.class);
 StubberImpl stubber = (StubberImpl) MockitoCore.stubbing();
 MockitoCore.stubVoid(mock);
 stubber.doCallRealMethod();
 }

 @Test
 public void testValidateMockitoUsage() {
 MockitoCore.validateMockitoUsage();
 }

 @Test(expected = AssertionError.class)
 public void testGetLastInvocationWithNoInvocations() {
 MockitoCore mockitoCore = MockitoCore.mock(MockitoCore.class, new MockSettingsImpl());
 }

 @Test
 public void testGetLastInvocation() {
 Object mock = Mockito.mock(Object.class);
 IOngoingStubbing ongoingStubbing = MockitoCore.stub(mock);
 ongoingStubbing.thenCallRealMethod();
 MockitoCore.verify(mock, VerificationModeFactory.verifyNoMoreInteractions());
 lastInvocation = MockitoCore.getLastInvocation();
 assertEquals(lastInvocation.getMethod(), mock.getClass().getMethod("", new Class[0]));
 assertEquals(lastInvocation.getArguments(), new Object[0]);
 }
}