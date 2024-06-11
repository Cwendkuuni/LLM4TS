package org.apache.commons.lang3.builder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        // Typical Use Case
        Object obj1 = new Object();
        Object obj2 = new Object();
        int result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(0, result);
        
        // Edge Case
        Object nullObj = null;
        try {
            CompareToBuilder.reflectionCompare(obj1, nullObj);
            fail("NullPointerException not thrown");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        CompareToBuilder result = builder.appendSuper(5);
        
        assertSame(builder, result);
    }
    
    @Test
    public void testAppendLong() {
        CompareToBuilder builder = new CompareToBuilder();
        CompareToBuilder result = builder.append(10L, 5L);
        
        assertEquals(1, result.toComparison());
    }
    
    // Add more test cases for other methods
    
}