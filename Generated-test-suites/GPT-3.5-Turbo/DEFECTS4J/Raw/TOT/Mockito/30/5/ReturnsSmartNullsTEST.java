import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {
	
	@Test
	public void testAnswer_validReturnType_ReturnsSmartNull() {
		// Arrange
		ReturnsSmartNulls answer = new ReturnsSmartNulls();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod().getReturnType()).thenReturn(String.class);
		when(ClassImposterizer.INSTANCE.canImposterise(String.class)).thenReturn(true);
		
		// Act
		Object result = answer.answer(invocation);
		
		// Assert
		assertTrue(result instanceof MethodInterceptor);
	}
	
	@Test
	public void testAnswer_invalidReturnType_ReturnsNull() {
		// Arrange
		ReturnsSmartNulls answer = new ReturnsSmartNulls();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);
		when(ClassImposterizer.INSTANCE.canImposterise(Integer.class)).thenReturn(false);
		
		// Act
		Object result = answer.answer(invocation);
		
		// Assert
		assertNull(result);
	}
	
	@Test
	public void testIntercept_toStringMethod_ReturnsSmartNull() throws Throwable {
		// Arrange
		ReturnsSmartNulls.ThrowingInterceptor interceptor = new ReturnsSmartNulls().new ThrowingInterceptor(mock(InvocationOnMock.class));
		Method method = Object.class.getMethod("toString");
		
		// Act
		Object result = interceptor.intercept(null, method, null, null);
		
		// Assert
		assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
	}
	
	@Test(expected = Reporter.SmartNullPointerException.class)
	public void testIntercept_nonToStringMethod_ThrowsSmartNullPointerException() throws Throwable {
		// Arrange
		ReturnsSmartNulls.ThrowingInterceptor interceptor = new ReturnsSmartNulls().new ThrowingInterceptor(mock(InvocationOnMock.class));
		Method method = Object.class.getMethod("hashCode");
		
		// Act
		interceptor.intercept(null, method, null, null);
		
		// Assert (Expects exception)
	}
}