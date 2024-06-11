import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

	@Test
	public void testAnswer() throws Throwable {
		// Create mock InvocationOnMock object
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		
		// Create ReturnsMoreEmptyValues object
		ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
		
		// Create ReturnsSmartNulls object
		ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
		
		// Set up delegate.answer() to return null
		when(delegate.answer(invocation)).thenReturn(null);
		
		// Set up invocation.getMethod().getReturnType() to return String class
		when(invocation.getMethod().getReturnType()).thenReturn(String.class);
		
		// Mock ClassImposterizer.INSTANCE.canImposterise() method to return true
		ClassImposterizer classImposterizer = mock(ClassImposterizer.class);
		when(classImposterizer.canImposterise(String.class)).thenReturn(true);
		
		// Mock ClassImposterizer.INSTANCE.imposterise() method to return a mock object
		Object mockObject = mock(Object.class);
		when(classImposterizer.imposterise(any(ThrowsInterceptor.class), eq(String.class))).thenReturn(mockObject);
		
		// Set the mock ClassImposterize.INSTANCE to the ReturnsSmartNulls class
		ClassImposterizer originalClassImposterizer = ClassImposterizer.INSTANCE;
		ClassImposterizer.INSTANCE = classImposterizer;
		
		// Call the answer() method and assert that it returns the mock object
		Object result = returnsSmartNulls.answer(invocation);
		assertEquals(mockObject, result);
		
		// Verify that delegate.answer() is called
		verify(delegate).answer(invocation);
		
		// Verify that invocation.getMethod().getReturnType() is called
		verify(invocation).getMethod().getReturnType();
		
		// Verify that ClassImposterizer.INSTANCE.canImposterise() is called
		verify(classImposterizer).canImposterise(String.class);
		
		// Verify that ClassImposterizer.INSTANCE.imposterise() is called
		verify(classImposterizer).imposterise(any(ThrowsInterceptor.class), eq(String.class));
		
		// Reset the ClassImposterize.INSTANCE to the original value
		ClassImposterizer.INSTANCE = originalClassImposterizer;
	}

}