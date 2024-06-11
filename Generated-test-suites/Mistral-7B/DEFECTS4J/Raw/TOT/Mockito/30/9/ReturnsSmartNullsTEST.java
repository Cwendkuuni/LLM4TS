import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private Object mock;

 @Before
 public void setUp() {
 mock = mock(Object.class, returnsSmartNulls);
 returnsSmartNulls = Mockito.spy(returnsSmartNulls);
 }

 @Test
 public void testReturnsSmartNullForUnstubbedMethodCall() {
 Object result = returnsSmartNulls.answer(new InvocationOnMock(mock, new MethodTestDouble(), new Object[0]));
 assertEquals("SmartNull returned by unstubbed method on mock", result.toString());
 }

 @Test
 public void testReturnsDelegateValue() {
 ReturnsMoreEmptyValues delegate = Mockito.spy(new ReturnsMoreEmptyValues());
 Answer<Object> delegateSpy = Mockito.spy(delegate);
 ReturnsSmartNulls returnsSmartNullsWithDelegate = new ReturnsSmartNulls(delegateSpy);
 Object result = returnsSmartNullsWithDelegate.answer(new InvocationOnMock(mock, new MethodTestDouble(), new Object[0]));
 assertEquals(delegateSpy.answer(new InvocationOnMock(mock, new MethodTestDouble(), new Object[0])), result);
 }

 @Test
 public void testReturnsNullForNonMockableType() {
 Class<Integer> integerClass = Integer.class;
 ReturnsSmartNulls returnsSmartNullsForInteger = new ReturnsSmartNulls();
 Object result = returnsSmartNullsForInteger.answer(new InvocationOnMock(mock, new MethodTestDouble(), new Object[]{integerClass}));
 assertEquals(null, result);
 }

 @Test
 public void testThrowsExceptionForToStringMethod() throws Exception {
 Method toStringMethod = Object.class.getMethod("toString");
 InvocationOnMock invocation = new InvocationOnMock(mock, toStringMethod, new Object[0]);
 ReturnsSmartNulls returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
 doThrow(RuntimeException.class).when(returnsSmartNulls).answer(invocation);
 returnsSmartNulls.answer(invocation);
 }

 private static class MethodTestDouble {
 }
}