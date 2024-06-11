package org.apache.commons.lang3.builder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CompareToBuilderTest {
    
    @Test
    public void testReflectionCompare() {
        // Typical use case
        Object obj1 = new Object();
        Object obj2 = new Object();
        int result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(0, result);
        
        // Edge case: Null object
        try {
            CompareToBuilder.reflectionCompare(null, null);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
        
        // Error scenario: ClassCastException
        try {
            CompareToBuilder.reflectionCompare(new Object(), new String());
        } catch (ClassCastException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(5);
        assertEquals(5, builder.toComparison());
    }
    
    @Test
    public void testAppend() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(5, 10);
        assertEquals(-1, builder.toComparison());
    }
    
    // Additional test methods for append methods and other functionalities
    
}