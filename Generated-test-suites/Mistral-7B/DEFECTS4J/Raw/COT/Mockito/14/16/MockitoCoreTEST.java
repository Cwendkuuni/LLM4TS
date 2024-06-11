import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class MockitoCoreTest {

 private MockingProgress mockingProgressSpy;

 @Mock
 private Object mockObject;
 @Mock
 private List<Object> mockList;
 @Mock
 private InOrder inOrder;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 mockingProgressSpy = Mockito.spy(MockitoCore.mockingProgress);
 }

 @Test
 public void testMockCreation() {
 MockitoCore mockitoCore = new MockitoCore();
 Object mock = mockitoCore.mock(String.class, new MockSettingsImpl());
 verify(mockingProgressSpy).mockingStarted(mock, String.class, new MockSettingsImpl());
 }

 @Test
 public void testStubbing() {
 MockitoCore mockitoCore = new MockitoCore();
 IOngoingStubbing ongoingStubbing = mockitoCore.stub();
 OngoingStubbing<Object> stubbing = (OngoingStubbing<Object>) ongoingStubbing;
 stubbing.thenReturn("Hello Mockito!");
 Object mock = mockitoCore.mock(String.class, new MockSettingsImpl());
 when(mock.toString()).thenReturn("Mock Object");
 Object result = mockitoCore.verify(mock).toString();
 verify(mock).toString();
 verify(mock).toString(); // Verify that the stubbing was applied twice
 verify(mock).toString(); // Verify that the stubbing is still in effect
 verify(mock).toString(); // Verify that the stubbing is still in effect
 assertEquals("Hello Mockito!", result);
 }

 @Test
 public void testVerify() {
 MockitoCore mockitoCore = new MockitoCore();
 Object mock = mockitoCore.mock(String.class, new MockSettingsImpl());
 verify(mockitoCore).verify(mock, VerificationMode.times(1));
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyNullMock() {
 MockitoCore mockitoCore = new MockitoCore();
 mockitoCore.verify(null, VerificationMode.times(1));
 }

 @Test(expected = AssertionError.class)
 public void testNotAMock() {
 MockitoCore mockitoCore = new MockitoCore();
 mockitoCore.verify(new Object(), VerificationMode.times(1));
 }

 @Test
 public void testReset() {
 MockitoCore mockitoCore = new MockitoCore();
 Object mock1 = mockitoCore.mock(String.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(String.class, new MockSettingsImpl());
 mockitoCore.reset(mock1, mock2);
 verifyZeroInteractions(mock1, mock2);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 MockitoCore mockitoCore = new MockitoCore();
 Object mock = mockitoCore.mock(String.class, new MockSettingsImpl());
 when(mock.toString()).thenReturn("Mock Object");
 mockitoCore.verify(mock, VerificationMode.noMoreInteractions());
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyNoMoreInteractionsNull() {
 MockitoCore mockitoCore = new MockitoCore();
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNotAMock() {
 MockitoCore mockitoCore = new MockitoCore();
 mockitoCore.verifyNoMoreInteractions(new Object());
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 MockitoCore mockitoCore = new MockitoCore();
 Object mock1 = mockitoCore.mock(String.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(String.class, new MockSettingsImpl());
 when(mock1.toString()).thenReturn("Mock1");
 when(mock2.toString()).thenReturn("Mock2");
 inOrder(mock1, mock2).verify(mock1).toString();
 inOrder(mock1, mock2).verify(mock2).toString();
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyNoMoreInteractionsInOrderNull() {
 MockitoCore mockitoCore = new MockitoCore();
 mockitoCore.verifyNoMoreInteractionsInOrder(null, inOrder);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsInOrderNotAMock() {
 MockitoCore mockitoCore = new MockitoCore();
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(new Object()), inOrder);
 }

 @Test
 public void testInOrderCreation() {
 MockitoCore mockitoCore = new MockitoCore();
 InOrder inOrder = mockitoCore.inOrder(mockObject, mockList);
 }

 @Test(expected = NullPointerException.class)
 public void testInOrderNull() {
 MockitoCore mockitoCore = new MockitoCore();
 mockitoCore.inOrder(null);
 }

 @Test(expected = AssertionError.class)
 public void testInOrderNotAMock() {
 MockitoCore mockitoCore = new MockitoCore();
 mockitoCore.inOrder(new Object());
 }

 @Test
 public void testAnswer() {
 MockitoCore mockitoCore = new MockitoCore();
 Answer<Integer> answer = invocation -> 42;
 Stubber stubber = mockitoCore.doAnswer(answer);
 Object mock = mockitoCore.mock(Integer.class, new MockSettingsImpl());
 stubber.doAnswer(answer).thenReturn(12);
 when(mock.intValue()).thenReturn(1);
 int result = mockitoCore.verify(mock).intValue();
 verify(mock).intValue();
 verify(mock).intValue(); // Verify that the answer was applied twice
 assertEquals(42, result);
 }

 @Test
 public void testStubVoid() {
 MockitoCore mockitoCore = new MockitoCore();
 VoidMethodStubbable<Object> stub = mockitoCore.stubVoid(mockObject);
 stub.then();
 verify(mockObject).toString();
 }

 @Test
 public void testValidateMockitoUsage() {
 MockitoCore mockitoCore = new MockitoCore();
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 MockitoCore mockitoCore = new MockitoCore();
 Object mock = mockitoCore.mock(String.class, new MockSettingsImpl());
 when(mock.toString()).thenReturn("Mock Object");
 IOngoingStubbing ongoingStubbing = mockitoCore.stub();
 List<Invocation> invocations = ongoingStubbing.getRegisteredInvocations();
 OngoingStubbingImpl ongoingStubbingImpl = (OngoingStubbingImpl) ongoingStubbing;
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 verify(mock).toString();
 assertEquals(invocations.get(invocations.size() - 1), lastInvocation);
 }
}