import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsSmartNullsTest {

 private static final Class<ReturnsSmartNulls> RETURNS_SMART_NULLS = ReturnsSmartNulls.class;

 @Test
 public void testAnswerNullMethod() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class, Answers.RETURNS_NULLS);
     Method method = Mockito.method(Void.class, new Class[0]);
     InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
     invocation.setMethod(method);

     Object result = returnsSmartNulls.answer(invocation);
     assertNull(result);
 }

 @Test
 public void testAnswerMethodThatCanBeMocked() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
     Method method = Mockito.method(String.class, new Class[0]);
     InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
     invocation.setMethod(method);

     Mockito.when(returnsSmartNulls.answer(Mockito.any(InvocationOnMock.class))).thenReturn("Mocked return value");
     String result = (String) returnsSmartNulls.answer(invocation);
     assertEquals("Mocked return value", result);
 }

 @Test(expected = RuntimeException.class)
 public void testToStringMethod() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
     Method method = Mockito.method(String.class, new Class[0]);
     InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
     invocation.setMethod(method);

     try {
         returnsSmartNulls.answer(invocation);
     } catch (Throwable e) {
         String exceptionMessage = e.getMessage();
         assertTrue(exceptionMessage.contains("SmartNull returned by unstubbed method on mock"));
         throw new RuntimeException(exceptionMessage);
     }
 }

 @Test
 public void testAnswerMethodWithNullReturnType() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
     Method method = Mockito.method(Void.TYPE, new Class[0]);
     InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
     invocation.setMethod(method);

     Object result = returnsSmartNulls.answer(invocation);
     assertNull(result);
 }

 @Test
 public void testAnswerMethodWithFinalReturnType() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
     Method method = Mockito.method(String.class, new Class[0], "final");
     InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
     invocation.setMethod(method);

     Object result = returnsSmartNulls.answer(invocation);
     assertNull(result);
 }

 @Test
 public void testAnswerMethodWithThrowingInterceptor() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
     Method method = Mockito.method(String.class, new Class[0]);
     InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
     invocation.setMethod(method);

     verify(returnsSmartNulls).new ThrowingInterceptor(invocation);
 }

 @Test(expected = org.mockito.exceptions.Reporter.NullPointerException.class)
 public void testAnswerMethodWithNullReturnValue() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
     Method method = Mockito.method(String.class, new Class[0]);
     InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
     invocation.setMethod(method);

     Object result = null;
     try {
         result = returnsSmartNulls.answer(invocation);
     } catch (Throwable e) {
         // Expected null pointer exception
     }
     assertNull(result);
 }

 @Test(expected = org.mockito.exceptions.Reporter.NullPointerException.class)
 public void testAnswerMethodWithNullMethod() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
     InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

     try {
         returnsSmartNulls.answer(invocation);
     } catch (Throwable e) {
         // Expected null pointer exception
     }
 }
}