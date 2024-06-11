import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito. internal.MockitoCore;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.stubbing.Answer.*;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.inOrder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock1;
 private Object mock2;
 private Object realObject;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mock1 = mockitoCore.mock(String.class, new MockSettingsImpl());
 mock2 = mockitoCore.mock(String.class, new MockSettingsImpl());
 realObject = new Object();
 }

 @Test
 public void testMockCreation() {
 Object mock = mockitoCore.mock(String.class, new MockSettingsImpl());
 assertNotNull(mock);
 }

 @Test
 public void testStubbing() {
 OngoingStubbing<String> stubbing = mockitoCore.stub(ArgumentMatchers.eq("Hello"));
 stubbing.thenReturn("World");
 assertEquals("World", mockitoCore.verify(mock1).toString());
 }

 @Test
 public void testDeprecatedStubbing() {
 DeprecatedOngoingStubbing<String> stubbing = mockitoCore.stub("Hello");
 stubbing.thenReturn("World");
 assertEquals("World", mockitoCore.verify(mock1).toString());
 }

 @Test
 public void testOngoingStubbing() {
 OngoingStubbing<String> stubbing = mockitoCore.when(mock1).thenReturn("World");
 assertEquals("World", mockitoCore.verify(mock1).toString());
 }

 @Test
 public void testVerification() {
 verify(mock1).toString();
 verifyNoMoreInteractions(mock1);
 }

 @Test
 public void testVerificationInOrder() {
 List<Object> mocks = Arrays.asList(mock1, mock2);
 InOrder inOrder = inOrder(mock1, mock2);
 mockitoCore.verifyNoMoreInteractions(mocks.toArray());
 inOrder.verify(mock1).toString();
 inOrder.verify(mock2).toString();
 }

 @Test
 public void testVerificationMode() {
 verify(mock1, VerificationMode.times(2)).toString();
 }

 @Test
 public void testAnswer() {
 int counter = 0;
 Answer<Integer> answer = new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 counter++;
 return 42;
 }
 };
 OngoingStubbing<Integer> stubbing = mockitoCore.doAnswer(answer).when(mock1).someMethod();
 stubbing.thenCallRealMethod();
 assertEquals(42, mockitoCore.verify(mock1).someMethod().intValue());
 assertEquals(1, counter);
 }

 @Test
 public void testVoidMethodStubbing() {
 mockitoCore.stubVoid(mock1).someVoidMethod();
 mockitoCore.verify(mock1).someVoidMethod();
 }

 @Test(expected = AssertionError.class)
 public void testNullPassedToVerify() {
 mockitoCore.verify(null, VerificationMode.never());
 }

 @Test(expected = AssertionError.class)
 public void testNotAMockPassedToVerify() {
 mockitoCore.verify(realObject, VerificationMode.never());
 }

 @Test(expected = AssertionError.class)
 public void testNotAMockPassedToVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(new Object());
 }

 @Test(expected = AssertionError.class)
 public void testMocksHaveToBePassedToVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions();
 }

 @Test(expected = AssertionError.class)
 public void testMocksHaveToBePassedWhenCreatingInOrder() {
 mockitoCore.inOrder();
 }

 @Test(expected = AssertionError.class)
 public void testNullPassedWhenCreatingInOrder() {
 mockitoCore.inOrder(null);
 }

 @Test(expected = AssertionError.class)
 public void testNotAMockPassedWhenCreatingInOrder() {
 mockitoCore.inOrder(realObject);
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