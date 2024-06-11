import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls answer;
 private Object target;

 @Before
 public void setUp() {
 answer = new ReturnsSmartNulls();
 target = mock(TestClass.class, answer);
 }

 @Test
 public void testSmartNullReturnedByStubbedMethod() {
 when(target.method1()).thenReturn("SmartNull returned by stubbed method");
 String result = target.method1();
 verify(target).method1();
 assertNotNull(result);
 }

 @Test
 public void testNullReturnedByStubbedMethod() {
 when(target.method1()).thenReturn(null);
 String result = target.method1();
 verify(target).method1();
 assertNull(result);
 }

 @Test(expected = NullPointerException.class)
 public void testNullReturnedByUnstubbedMethod() {
 String result = target.method2();
 verify(target).method2();
 }

 @Test
 public void testSmartNullReturnedByUnstubbedMethod() {
 Answer<String> answer = Mockito.mock(Answer.class);
 when(answer.answer(ArgumentMatchers.any(InvocationOnMock.class))).then(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return new ReturnsSmartNulls().answer(invocation);
 }
 });

 when(target.method3()).thenAnswer(answer);
 String result = target.method3();
 verify(target).method3();
 assertNotNull(result);
 }

 @Test(expected = NullPointerException.class)
 public void testNullReturnedByUnstubbedMethodWithToString() {
 when(target.method4()).thenCallRealMethod();
 String result = target.method4();
 verify(target).method4();
 }
}

class TestClass {
 String method1() { return "result"; }
 String method2() { return null; }
 String method3() { return null; }
 String method4() { return null; }
}