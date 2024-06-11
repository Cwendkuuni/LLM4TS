import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.AnswerCalledWith;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*; // For given, when, then syntax

@RunWith(MockitoJUnitRunner.class)
public class ReturnsSmartNullsTest {

 private Object mock;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mock = Mockito.mock(Object.class, new ReturnsSmartNulls());
 }

 @Test
 public void testReturnsSmartNullWhenCalledWithToStringMethod() {
 given(mock.toString()).willReturn("SmartNull returned by unstubbed toString() method on mock");

 String result = (String) when(mock.toString()).thenReturn(null).getInvocation().getReturnValue();

 assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
 }

 @Test
 public void testReturnsNullWhenCalledWithUnstubbedMethod() {
 given(mock.unimplementedMethod()).willCallRealMethod();

 assertNull(mock.unimplementedMethod());
 }

 @Test
 public void testReturnsSmartNullWhenCalledWithUnstubbedFinalClassMethod() {
 given(mock.getClass()).willReturn(String.class);

 assertNull(mock.intern());
 }

 @Test
 public void testReturnsNullWhenCalledWithUnstubbedMethodThatThrowsException() {
 given(mock.unimplementedMethodThatThrowsException()).willThrow(new RuntimeException("Exception from unstubbed method"));

 try {
 mock.unimplementedMethodThatThrowsException();
 } catch (RuntimeException e) {
 new Reporter().smartNullPointerException(new Location(2, 3));
 fail("Exception from unstubbed method should have been wrapped with a SmartNullPointerException");
 }

 assertNull(mock.unimplementedMethodThatThrowsException());
 }

 @Test
 public void testDoesNotReturnSmartNullWhenCalledWithStubbedMethod() {
 given(mock.stubbedMethod(ArgumentMatchers.anyInt())).willReturn("ExpectedReturnValue");

 assertEquals("ExpectedReturnValue", mock.stubbedMethod(1));
 }

 @Test
 public void testReturnsSmartNullWhenCalledWithUnstubbedMethodThatReturnsNull() {
 given(mock.unimplementedMethodThatReturnsNull()).willCallRealMethod();

 assertNull(mock.unimplementedMethodThatReturnsNull());

 verify(mock).unimplementedMethodThatReturnsNull();
 verify(mock, times(1)).unimplementedMethodThatReturnsNull();
 verifyNoMoreInteractions(mock);

 AnswerCalledWith answer = new AnswerCalledWith(mock, invocation -> invocation.getMethod().getName().equals("unimplementedMethodThatReturnsNull"));
 then(mock).thenAnswer(answer).answer(ArgumentMatchers.any(InvocationOnMock.class)).shouldReturn(new SmartNull());
 }
}

class SmartNull {
 @Override
 public String toString() {
 return "SmartNull";
 }
}