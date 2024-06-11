import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls smartNullsAnswer;
 private Object mock;

 @Before
 public void setUp() {
 smartNullsAnswer = new ReturnsSmartNulls();
 mock = Mockito.mock(String.class, smartNullsAnswer);
 }

 @Test
 public void testSmartNullForStringMethod() {
 String expected = "Expected string";
 when(mock.toString()).thenReturn(expected);
 String result = (String) smartNullsAnswer.answer(new InvocationOnMock(mock, "toString", new Object[0]));
 assertEquals(expected, result);

 verify(mock, times(1)).toString();
 }

 @Test(expected = org.mockito.exceptions.misusing.SmartNullPointerException.class)
 public void testSmartNullForUnstubbedMethod() {
 when(mock.hashCode()).thenAnswer(smartNullsAnswer);
 assertNull(smartNullsAnswer.answer(new InvocationOnMock(mock, "hashCode", new Object[0])));
 }

 @Test
 public void testDelegateAnswer() {
 ArgumentCaptor<InvocationOnMock> invocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);
 mock = Mockito.mock(Integer.class, smartNullsAnswer);
 when(mock.toString()).thenReturn("Mockito");
 doReturn(1).when(mock).intValue();

 invokeMethodAndCheckReturnValue(mock, "toString");
 invokeMethodAndCheckReturnValue(mock, "intValue");
 }

 @Test
 public void testDelegateAnswerWithObjectReturnType() {
 mock = Mockito.mock(MyClass.class, smartNullsAnswer);
 when(mock.someMethod()).thenReturn(new MyClass());
 MyClass result = (MyClass) smartNullsAnswer.answer(new InvocationOnMock(mock, "someMethod", new Object[0]));
 assertNotNull(result);
 }

 @Test(expected = java.lang.NullPointerException.class)
 public void testDelegateAnswerWithNullReturnType() {
 mock = Mockito.mock(String[].class, smartNullsAnswer);
 when(mock.toString()).thenReturn(new String[]{"Mockito"});
 String[] result = (String[]) smartNullsAnswer.answer(new InvocationOnMock(mock, "toString", new Object[0]));
 assertNull(result);
 }

 private void invokeMethodAndCheckReturnValue(Object mock, String methodName) {
 Object result = smartNullsAnswer.answer(new InvocationOnMock(mock, methodName, new Object[0]));
 assertNotNull(result);
 }

 @Test(expected = org.mockito.exceptions.misusing.SmartNullPointerException.class)
 public void testSmartNullForToStringMethodWithFinalClass() {
 String finalClassString = "java.lang.String";
 when(mock.getClass()).thenReturn(Class.forName(finalClassString));
 when(mock.toString()).thenAnswer(smartNullsAnswer);
 String result = (String) smartNullsAnswer.answer(new InvocationOnMock(mock, "toString", new Object[0]));
 assertNull(result);
 }

 @Captor
 ArgumentCaptor<Location> locationCaptor;

 @Test
 public void testSmartNullPointerExceptionMessage() {
 when(mock.hashCode()).thenAnswer(smartNullsAnswer);
 smartNullsAnswer.answer(new InvocationOnMock(mock, "hashCode", new Object[0]));
 String exceptionMessage = ((SmartNullPointerException) Mockito.verify(mock, times(1)).hashCode())
 .getMessage();
 assertEquals("SmartNull returned by unstubbed hashCode() method on mock", exceptionMessage);
 }
}

class MyClass {
}