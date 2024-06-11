package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class CallsRealMethodsTest {
    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods answer = new CallsRealMethods();
        
        // Create a mock object
        MyClass myObject = mock(MyClass.class);
        
        // Define the behavior of the mock object
        when(myObject.getString()).thenAnswer(answer);
        
        // Test the mock object
        assertEquals("Real Method", myObject.getString());
    }
    
    // Define a class to be used as the mock object
    private class MyClass {
        public String getString() {
            return "Real Method";
        }
    }
}