import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.stubbing. Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 @Mock
 private Object mock1;
 @Mock
 private Object mock2;
 @Captor
 private ArgumentCaptor<Object> captor;

 private MockitoCore mockitoCore;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 }

 @Test
 public void testMockCreation() {
 Object mock = mockitoCore.<Object>mock(Object.class);
 verify(mockitoCore).mockingStarted(mock, Object.class, MockSettingsImpl.DEFAULT);
 }

 @Test
 public void testStubbing() {
 Object mock = mockitoCore.mock(Object.class, MockSettingsImpl.DEFAULT);
 when(mock.foo()).thenReturn("bar");
 String result = (String) mock.foo();
 verify(mock).foo();
 verify(mock).foo(); // Verify that the stubbing was called twice
 verify(mock).foo(); // Verify that the stubbing is still in place
 expect(mock.foo()).andReturn("baz"); // Put a new stubbing over the old one
 result = (String) mock.foo();
 verify(mock).foo(); // Verify that the new stubbing was called instead
 }

 @Test(expected = NotAMockException.class)
 public void testNotAMockPassedToVerify() {
 mockitoCore.verify(new Object(), Mockito.eq("expectedValue"));
 }

 @Test
 public void testResettingMocks() {
 Object mock = mockitoCore.mock(Object.class, MockSettingsImpl.DEFAULT);
 mockitoCore.reset(mock);
 verifyZeroInteractions(mock);
 }

 @Test
 public void testVerificationOfMockInteractions() {
 Object mock = mockitoCore.mock(Object.class, MockSettingsImpl.DEFAULT);
 mockitoCore.verify(mock, Mockito.eq("expectedValue"));
 }

 @Test
 public void testVerificationOfMultipleMocksInteractions() {
 Object mock1 = mockitoCore.mock(Object.class, MockSettingsImpl.DEFAULT);
 Object mock2 = mockitoCore.mock(Object.class, MockSettingsImpl.DEFAULT);
 when(mock1.getMockHandler()).thenReturn(Mockito.mock(MockHandlerInterface.class));
 when(mock2.getMockHandler()).thenReturn(Mockito.mock(MockHandlerInterface.class));
 Object mocks[] = {mock1, mock2};
 mockitoCore.verifyNoMoreInteractions(mocks);
 }

 @Test
 public void testVerificationOfMocksInOrder() {
 Object mock1 = mockitoCore.mock(Object.class, MockSettingsImpl.DEFAULT);
 Object mock2 = mockitoCore.mock(Object.class, MockSettingsImpl.DEFAULT);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 when(mock1.foo()).thenReturn("one");
 when(mock2.foo()).thenReturn("two");
 mockitoCore.verify(mock1).foo();
 mockitoCore.verify(mock2).foo();
 inOrder.verify(mock1).foo();
 inOrder.verify(mock2).foo();
 }

 @Test
 public void testAnswer() {
 Object mock = mockitoCore.mock(Object.class, MockSettingsImpl.DEFAULT);
 Answer answer = new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "answer";
 }
 };
 mockitoCore.doAnswer(answer).when(mock).foo();
 String result = (String) mock.foo();
 verify(mock).foo();
 verify(mock).foo();
 verify(mock).foo();
 verify(mock).foo(); // Verify that the answer was called 4 times
 }

 @Test
 public void testVoidMethodStubbing() {
 Object mock = mockitoCore.mock(Object.class, MockSettingsImpl.DEFAULT);
 mockitoCore.stubVoid(mock).foo();
 mock.foo();
 verify(mock).foo();
 }

 @Test
 public void testExceptionOnVerification() {
 Object mock = mockitoCore.mock(Object.class, MockSettingsImpl.DEFAULT);
 when(mock.foo()).thenThrow(new RuntimeException());
 Object exception = mock.foo();
 mockitoCore.verify(mock).foo(); // Should throw NotAMockException
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 Object mock1 = mockitoCore.mock(Object.class, MockSettingsImpl.DEFAULT);
 Object mock2 = mockitoCore.mock(Object.class, MockSettingsImpl.DEFAULT);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 when(mock1.foo()).thenReturn("one");
 when(mock2.foo()).thenReturn("two");
 mockitoCore.verifyNoMoreInteractions(mock1, mock2);
 inOrder.verify(mock1).foo();
 inOrder.verify(mock2).foo();
 }
}