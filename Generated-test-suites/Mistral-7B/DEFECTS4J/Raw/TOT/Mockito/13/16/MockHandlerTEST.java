import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stub;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<TestMock> mockHandler;
 private TestMock mock;

 @Before
 public void setup() {
 mock = mock(TestMock.class);
 mockHandler = new MockHandler<>(mock);
 }

 @Test
 public void testHandleInvocationWithStubbing() {
 // Given
 when(mock.doVoidMethod()).thenReturn(1L);

 // When
 Object result = mockHandler.handle(new InvocationOnMock(mock, "doVoidMethod", new Object[0]));

 // Then
 verify(mock).doVoidMethod();
 verify(mock).doVoidMethod(); // verify that the stubbed method was called twice

 // Assert
 Assert.assertEquals(1L, result);
 }

 @Test
 public void testHandleInvocationWithoutStubbing() {
 // Given

 // When
 Object result = mockHandler.handle(new InvocationOnMock(mock, "doSomethingElse", new Object[0]));

 // Then
 // No verification or assertion should be performed since the invocation was not stubbed

 // Assert
 Assert.assertNull(result);
 }

 @Test(expected = NullPointerException.class)
 public void testHandleInvocationWithNullMock() {
 mockHandler = new MockHandler<>(null);

 // When and Then
 mockHandler.handle(new InvocationOnMock(mock, "doSomething", new Object[0]));
 }

 @Test
 public void testHandleInvocationWithVerificationMode() {
 // Given
 VerificationMode verificationMode = Mockito.verify(mock);

 // When
 mockHandler.handle(new InvocationOnMock(mock, "doSomethingElse", new Object[0]));

 // Then
 verify(mock).doSomething(); // verification mode should be re-added if the invocation doesn't match the stubbed one
 }

 @Test
 public void testHandleInvocationWithMultipleStubs() {
 // Given
 when(mock.doVoidMethod()).thenReturn(1L).thenReturn(2L);

 // When
 Object result1 = mockHandler.handle(new InvocationOnMock(mock, "doVoidMethod", new Object[0]));
 Object result2 = mockHandler.handle(new InvocationOnMock(mock, "doVoidMethod", new Object[0]));

 // Then
 verify(mock).doVoidMethod(); // verification and assertion for the first invocation
 verify(mock, Mockito.times(2)).doVoidMethod(); // verification and assertion for both invocations

 // Assert
 Assert.assertEquals(1L, result1);
 Assert.assertEquals(2L, result2);
 }

 @Test
 public void testHandleInvocationWithAnswer() {
 // Given
 Answer<Long> answer = arguments -> 1L;
 when(mock.doVoidMethod()).thenAnswer(answer);

 // When
 Object result = mockHandler.handle(new InvocationOnMock(mock, "doVoidMethod", new Object[0]));

 // Then
 verify(mock).doVoidMethod();

 // Assert
 Assert.assertEquals(1L, result);
 }

 @Test
 public void testHandleInvocationWithVoidMethodStubbable() {
 // Given
 VoidMethodStubbable<TestMock> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

 // When
 voidMethodStubbable.doNothing();

 // Then
 // No verification or assertion should be performed since the void method was stubbed
 }
}

class TestMock {
 void doVoidMethod() {}
 void doSomething() {}
}