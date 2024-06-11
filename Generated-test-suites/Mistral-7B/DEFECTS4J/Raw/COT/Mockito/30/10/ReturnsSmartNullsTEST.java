import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 returnsSmartNulls = mock(ReturnsSmartNulls.class, Answers.CALLS_REAL_METHODS);
 }

 // Test case for answer(InvocationOnMock) method
 @Test(expected = NullPointerException.class)
 public void testAnswerNullInvocation() {
 returnsSmartNulls.answer(null);
 }

 @Test
 public void testAnswerEmptyArray() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object result = returnsSmartNulls.answer(invocation);
 assertNull(result);
 }

 @Test
 public void testAnswerNullReturnType() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMethod()).thenReturn(new Method(Object.class, "getName"));
 Object result = returnsSmartNulls.answer(invocation);
 assertNull(result);
 }

 @Test
 public void testAnswerMockableReturnType() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMethod()).thenReturn(new Method(String.class, "getName"));
 String result = (String) returnsSmartNulls.answer(invocation);
 assertNull(result);
 verify(returnsSmartNulls).answer(invocation);
 }

 // Test case for ThrowingInterceptor class
 @Test(expected = NullPointerException.class)
 public void testThrowingInterceptorNullInvocation() {
 new ReturnsSmartNulls.ThrowingInterceptor(null);
 }

 @Test
 public void testThrowingInterceptorToStringMethod() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls.ThrowingInterceptor(invocation);
 String result = (String) throwingInterceptor.intercept(null, Mockito.mock(Method.class), new Object[0], Mockito.mock(MethodProxy.class));
 assertEquals("SmartNull returned by unstubbed method on mock", result);
 }

 @Test(expected = NullPointerException.class)
 public void testThrowingInterceptorNonToStringMethod() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls.ThrowingInterceptor(invocation);
 throwingInterceptor.intercept(null, Mockito.mock(Method.class), new Object[0], Mockito.mock(MethodProxy.class));
 }
}