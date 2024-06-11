import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

   @Test
   public void testAnswerReturnsSmartNulls() throws Throwable {
      ReturnsSmartNulls rs = new ReturnsSmartNulls();
      InvocationOnMock invocation = mock(InvocationOnMock.class);
      
      Object result = rs.answer(invocation);
      
      assertNull(result);
   }
   
   @Test
   public void testAnswerReturnsSmartNullsForUnstubbedToStringMethod() throws Throwable {
      ReturnsSmartNulls rs = new ReturnsSmartNulls();
      InvocationOnMock invocation = mock(InvocationOnMock.class);
      Method method = rs.getClass().getDeclaredMethod("intercept", Object.class, Method.class, Object[].class, MethodProxy.class);
      Object[] args = {null};
      when(invocation.getMethod()).thenReturn(method);
      when(invocation.getArguments()).thenReturn(args);
      
      Object result = rs.answer(invocation);
      
      assertEquals("SmartNull returned by unstubbed intercept() method on mock", result.toString());
   }
   
   @Test
   public void testAnswerReturnsSmartNullsForUnmockableReturnType() throws Throwable {
      ReturnsSmartNulls rs = new ReturnsSmartNulls();
      InvocationOnMock invocation = mock(InvocationOnMock.class);
      Method method = rs.getClass().getDeclaredMethod("intercept", Object.class, Method.class, Object[].class, MethodProxy.class);
      Class<?> returnType = String.class;
      Object[] args = {null};
      when(invocation.getMethod()).thenReturn(method);
      when(invocation.getMethod().getReturnType()).thenReturn(returnType);
      when(invocation.getArguments()).thenReturn(args);
      
      Object result = rs.answer(invocation);
      
      assertNull(result);
   }
   
   @Test
   public void testAnswerReturnsSmartNullsReturnsDefaultReturnValue() throws Throwable {
      ReturnsSmartNulls rs = new ReturnsSmartNulls();
      InvocationOnMock invocation = mock(InvocationOnMock.class);
      Method method = rs.getClass().getDeclaredMethod("intercept", Object.class, Method.class, Object[].class, MethodProxy.class);
      Class<?> returnType = Integer.class;
      Object[] args = {null};
      when(invocation.getMethod()).thenReturn(method);
      when(invocation.getMethod().getReturnType()).thenReturn(returnType);
      when(invocation.getArguments()).thenReturn(args);
      when(invocation.getMock()).thenReturn(rs);
      when(rs.answer(any())).thenReturn(5);
      
      Object result = rs.answer(invocation);
      
      assertEquals(5, result);
   }
}