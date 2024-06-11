package org.apache.commons.lang3.builder;
import static org.junit.Assert.*;
import org.junit.Test;

public class CompareToBuilderTest {
    
    @Test
    public void testReflectionCompare() {
        // Typical use case
        assertEquals(0, CompareToBuilder.reflectionCompare("abc", "abc"));
        
        // Edge case
        assertThrows(NullPointerException.class, () -> CompareToBuilder.reflectionCompare(null, "abc"));
    }
    
    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        CompareToBuilder result = builder.appendSuper(10);
        
        // Typical use case
        assertEquals(10, result.toComparison());
        
        // Edge case
        CompareToBuilder builder2 = new CompareToBuilder();
        CompareToBuilder result2 = builder2.appendSuper(0);
        assertEquals(0, result2.toComparison());
    }
    
    @Test
    public void testAppendObject() {
        CompareToBuilder builder = new CompareToBuilder();
        CompareToBuilder result = builder.append("abc", "def");
        
        // Typical use case
        assertTrue(result.toComparison() < 0);
        
        // Edge case
        CompareToBuilder builder2 = new CompareToBuilder();
        CompareToBuilder result2 = builder2.append("abc", "abc");
        assertEquals(0, result2.toComparison());
    }
    
    // Add more test cases for other methods as needed
    
}