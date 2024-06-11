import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 @Mock
 private Object mock;
 @Captor
 private ArgumentCaptor<Object[]> mockArrayArgCaptor;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 }

 @Test
 public void testMockCreation() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Mockito.verify(mockitoCore).mockingStarted(mock, Object.class, new MockSettingsImpl());
 }

 @Test
 public void testStubbing() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 when(mock.toString()).thenReturn("Test Mock");
 verify(mock).toString();
 String result = (String) stubbing.thenReturn("Test Result");
 verify(mock).toString();
 assertEquals("Test Result", result);
 }

 @Test
 public void testDeprecatedOngoingStubbing() {
 DeprecatedOngoingStubbing<Object> stubbing = mockitoCore.stub(mock);
 when(mock.toString()).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "Answer from DeprecatedOngoingStubbing";
 }
 });
 verify(mock).toString();
 assertEquals("Answer from DeprecatedOngoingStubbing", mockitoCore.stubbing().thenAnswer(invocation -> "Answer from DeprecatedOngoingStubbing").getMock().toString());
 }

 @Test
 public void testOngoingStubbing() {
 OngoingStubbing<Object> stubbing = mockitoCore.when(mock);
 when(mock.toString()).thenReturn("Test Mock");
 verify(mock).toString();
 String result = stubbing.thenReturn("Test Result");
 verify(mock).toString();
 assertEquals("Test Result", result);
 }

 @Test
 public void testVerify() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 verify(mock).toString();
 }

 @Test(expected = AssertionError.class)
 public void testVerifyOnNonMockObject() {
 mockitoCore.verify(new Object(), VerificationMode.times(1));
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock1 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 mockitoCore.verifyNoMoreInteractions(mock1, mock2);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsOnNonMockObject() {
 mockitoCore.verifyNoMoreInteractions(new Object());
 }

 @Test
 public void testVerifyInOrder() {
 Object mock1 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 when(mock1.toString()).thenReturn("Mock1");
 when(mock2.toString()).thenReturn("Mock2");
 mockitoCore.verify(mock1).toString();
 mockitoCore.verify(mock2).toString();
 inOrder.verify(mock1).toString();
 inOrder.verify(mock2).toString();
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 Object mock1 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 when(mock1.toString()).thenReturn("Mock1");
 when(mock2.toString()).thenReturn("Mock2");
 mockitoCore.verifyNoMoreInteractions(mock1, mock2);
 inOrder.verifyNoMoreInteractions();
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsInOrderOnNonMockObject() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock);
 mockitoCore.verifyNoMoreInteractions(mock);
 inOrder.verifyNoMoreInteractions();
 }

 @Test
 public void testAnswer() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Answer<Integer> answer = new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 42;
 }
 };
 OngoingStubbing<Object> stubbing = mockitoCore.doAnswer(answer).when(mock);
 assertEquals(42, stubbing.thenCallRealMethod().toString().length());
 }

 @Test
 public void testStubVoid() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 mockitoCore.stubVoid(mock).toString();
 verify(mock).toString();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 verify(mock).toString();
 assertEquals("toString()", lastInvocation.getMethod().getName());
 }
}