import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private Object mock;

 @Before
 public void setUp() {
 returnsSmartNulls = new ReturnsSmartNulls();
 mock = Mockito.mock(Object.class, returnsSmartNulls);
 }

 @Test
 public void testReturnsSmartNullForUnstubbedToStringMethod() {
 String result = (String) returnsSmartNulls.answer(mock, getToStringMethod());
 assertEquals("SmartNull returned by unstubbed toString method on mock", result);
 }

 @Test
 public void testReturnsDefaultValueForStubbedMethods() {
 when(mock.methodWithReturnValue(1)).thenReturn(1);
 int result = (int) returnsSmartNulls.answer(mock, getMethodWithReturnValue());
 assertEquals(1, result);
 }

 @Test
 public void testReturnsNullForUnstubbedMethodsOnNonMockableTypes() {
 assertNull(returnsSmartNulls.answer(mock, getMethodWithFinalReturnType()));
 }

 @Test(expected = NullPointerException.class)
 public void testThrowsNullPointerExceptionForUnstubbedMethods() {
 Object result = returnsSmartNulls.answer(mock, getMethodWithNullReturnType());
 fail("Expected NullPointerException but got " + result);
 }

 private Method getToStringMethod() {
 try {
 return mock.getClass().getMethod("toString");
 } catch (NoSuchMethodException e) {
 throw new RuntimeException(e);
 }
 }

 private Method getMethodWithReturnValue() {
 try {
 return mock.getClass().getMethod("methodWithReturnValue", int.class);
 } catch (NoSuchMethodException e) {
 throw new RuntimeException(e);
 }
 }

 private Method getMethodWithFinalReturnType() {
 try {
 return mock.getClass().getMethod("finalMethodWithReturnValue");
 } catch (NoSuchMethodException e) {
 throw new RuntimeException(e);
 }
 }

 private Method getMethodWithNullReturnType() {
 try {
 return mock.getClass().getMethod("methodWithNullReturnType");
 } catch (NoSuchMethodException e) {
 throw new RuntimeException(e);
 }
 }
}