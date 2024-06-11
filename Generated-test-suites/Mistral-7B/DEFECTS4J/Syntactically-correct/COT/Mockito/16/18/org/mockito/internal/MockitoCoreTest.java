import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.JUnit4;
import org.mockito.stubbing.Answer;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 @Mock private Object mock;
 @Mock private Reporter reporter;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 mockitoCore = new MockitoCore();
 mockitoCore.reporter = reporter;
 }

 @Test
 public void testMock() {
 Class<Object> classToMock = Object.class;
 MockSettings mockSettings = MockSettings.defaultSettings();
 Object mockObject = mockitoCore.mock(classToMock, mockSettings);
 assertNotNull(mockObject);
 verify(reporter).noReports();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMockNullClass() {
 mockitoCore.mock(null, new MockSettingsImpl());
 }

 @Test
 public void testStub() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 assertNotNull(stubbing);
 }

 @Test(expected = IllegalStateException.class)
 public void testStubWithoutStubbingStarted() {
 mockitoCore.stub();
 mockitoCore.stub(); // should throw exception
 }

 @Test
 public void testStubWithAnswer() {
 Answer<Integer> answer = arg -> 42;
 Stubber stubber = mockitoCore.doAnswer(answer);
 when(mock).someMethod().then(stubber);
 assertEquals(42, mock.someMethod().invoke());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStubNullTarget() {
 mockitoCore.stub().then(arg -> {});
 }

 @Test
 public void testWhen() {
 when(mock.someMethod()).thenReturn("test");
 assertEquals("test", mock.someMethod());
 }

 @Test
 public void testVerify() {
 verify(mock).someMethod();
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyNullMock() {
 mockitoCore.verify(null, ArgumentMatchers.any(VerificationMode.class));
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNotAMock() {
 mockitoCore.verify(new Object(), ArgumentMatchers.any(VerificationMode.class));
 }

 @Test
 public void testReset() {
 mockitoCore.reset(mock);
 verify(mock, never()).someMethod();
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object[] mocks = new Object[]{mock};
 mockitoCore.verifyNoMoreInteractions(mocks);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsNull() {
 mockitoCore.verifyNoMoreInteractions();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsEmptyArray() {
 mockitoCore.verifyNoMoreInteractions();
 }

 @Test
 public void testInOrder() {
 Object mock1 = mock;
 Object mock2 = mock;
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 verify(mock1).someMethod();
 verify(mock2).someOtherMethod();
 inOrder.verify(mock1).someMethod();
 inOrder.verify(mock2).someOtherMethod();
 }

 @Test
 public void testResetStubbing() throws InvocationTargetException, IllegalAccessException {
 mockitoCore.stub().then(arg -> { throw new RuntimeException(); });
 mockitoCore.stub().thenReturn("test");
 Object stubbedObject = mockitoCore.mock(String.class);
 when(stubbedObject.toString()).thenCallRealMethod();
 assertEquals("test", stubbedObject.toString());
 mockitoCore.reset();
 assertEquals(Arrays.toString(stubbedObject.toString().getBytes()), stubbedObject.toString().getBytes());
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Invocation invocation = mockitoCore.getLastInvocation();
 assertNotNull(invocation);
 }
}