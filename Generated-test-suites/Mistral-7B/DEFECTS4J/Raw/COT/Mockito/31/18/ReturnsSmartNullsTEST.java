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
 public void setUp() {
 returnsSmartNulls = mock(ReturnsSmartNulls.class, Answers.RETURNS_DEEP_STUBS);
 }

 // Testing method: answer()
 @Test
 public void testAnswerMethod() {
 InvocationOnMock invocation = Mockito.mock(Object.class);
 when(returnsSmartNulls.answer(invocation)).thenCallRealMethod();
 Object result = returnsSmartNulls.answer(invocation);
 assertNull(result);
 }

 @Test(expected = NullPointerException.class)
 public void testAnswerMethodWithToStringInvoked() {
 InvocationOnMock invocation = Mockito.mock(String.class);
 when(returnsSmartNulls.answer(invocation)).thenCallRealMethod();
 String result = (String) returnsSmartNulls.answer(invocation);
 }

 // Testing inner class ThrowingInterceptor's intercept() method
 @Test(expected = org.mockito.exceptions.SmartNullPointerException.class)
 public void testInterceptMethodWithMethodThatReturnsString() {
 InvocationOnMock invocation = Mockito.mock(String.class);
 ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls.ThrowingInterceptor(invocation);
 throwingInterceptor.intercept(Mockito.mock(Object.class), Mockito.mock(Method.class), new Object[0], Mockito.mock(MethodProxy.class));
 }

 // Testing edge case: when return type is mockable and is a primitive type
 @Test
 public void testAnswerMethodWithPrimitiveTypeReturnType() {
 InvocationOnMock invocation = Mockito.mock(Byte.class);
 when(returnsSmartNulls.answer(invocation)).thenCallRealMethod();
 Byte result = (Byte) returnsSmartNulls.answer(invocation);
 assertNull(result);
 }

 // Testing exception handling: when return type is not mockable
 @Test(expected = IllegalArgumentException.class)
 public void testAnswerMethodWithUnmockableReturnType() {
 InvocationOnMock invocation = Mockito.mock(String.class);
 when(returnsSmartNulls.answer(invocation)).thenCallRealMethod();
 returnsSmartNulls.answer(invocation);
 }

 // Testing edge case: when return type is a final class
 @Test
 public void testAnswerMethodWithFinalClassReturnType() {
 InvocationOnMock invocation = Mockito.mock(java.lang.String.class);
 when(returnsSmartNulls.answer(invocation)).thenCallRealMethod();
 String result = (String) returnsSmartNulls.answer(invocation);
 assertNull(result);
 }
}