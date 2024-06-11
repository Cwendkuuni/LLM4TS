import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 returnsSmartNulls = mock(ReturnsSmartNulls.class, Answers.RETURNS_DELEGATE_INSTANCE);
 }

 // Test case for answer() method with non-null return value
 @Test
 public void testAnswerWithNonNullReturnValue() {
 Object returnValue = new Object();
 InvocationOnMock invocation = Mockito.mock(Object.class);
 Mockito.when(invocation.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[0]));
 when(returnsSmartNulls.answer(Mockito.eq(invocation))).thenReturn(returnValue);

 Object result = returnsSmartNulls.answer(invocation);
 assertNotNull(result);
 assertEquals(returnValue, result);
 }

 // Test case for answer() method with null return value and mockable return type
 @Test
 public void testAnswerWithNullReturnValueAndMockableReturnType() {
 InvocationOnMock invocation = Mockito.mock(Object.class);
 when(returnsSmartNulls.answer(Mockito.eq(invocation))).thenReturn(null);
 when(returnsSmartNulls.answer(Mockito.eq(invocation))).thenAnswer(new ReturnsSmartNulls());

 Object result = returnsSmartNulls.answer(invocation);
 assertNull(result);
 verify(invocation).getMethod();
 }

 // Test case for answer() method with null return value and non-mockable return type
 @Test(expected = NullPointerException.class)
 public void testAnswerWithNullReturnValueAndNonMockableReturnType() {
 InvocationOnMock invocation = Mockito.mock(Object.class);
 when(returnsSmartNulls.answer(Mockito.eq(invocation))).thenReturn(null);

 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
 Object result = returnsSmartNulls.answer(invocation);
 assertNull(result);
 }

 // Test case for ThrowingInterceptor's intercept() method with toString() method call
 @Test
 public void testInterceptWithToStringMethodCall() {
 Object mock = mock(Object.class);
 InvocationOnMock invocation = Mockito.mock(Method.class);
 when(invocation.getName()).thenReturn("toString");
 when(mock.getClass()).thenReturn(String.class);
 when(returnsSmartNulls.answer(Mockito.eq(new InvocationOnMock(mock, invocation)))).thenReturn(null);
 when(returnsSmartNulls.answer(Mockito.eq(new InvocationOnMock(mock, invocation)))).thenAnswer(new ReturnsSmartNulls());

 String result = (String) returnsSmartNulls.answer(new InvocationOnMock(mock, invocation));
 assertNull(result);
 String expectedMessage = "SmartNull returned by unstubbed toString() method on mock";
 verify(new Reporter()).smartNullPointerException(Mockito.any(Location.class));
 verifyNoMoreInteractions(mock);
 }
}