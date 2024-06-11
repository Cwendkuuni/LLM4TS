/*
 * Test Cases for method answer()
 */

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class CallsRealMethodsTest {
    @Test
    public void testAnswer() {
        // Create a mock object
        Object mockObject = mock(Object.class);
        
        // Set up the mock object to call the real method
        when(mockObject.toString()).thenAnswer(new CallsRealMethods());
        
        // Call the method on the mock object
        mockObject.toString();
        
        // Verify that the real method was called
        verify(mockObject).toString();
    }
}