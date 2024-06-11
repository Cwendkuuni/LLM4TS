package org.apache.commons.lang3.builder;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode() {
        int hash = HashCodeBuilder.reflectionHashCode(1, 2, new Object());
        assertEquals(17, hash);
    }
    
    @Test
    public void testReflectionHashCodeWithTransients() {
        int hash = HashCodeBuilder.reflectionHashCode(1, 2, new Object(), true);
        assertEquals(17, hash);
    }
    
    @Test
    public void testReflectionHashCodeWithNullObject() {
        try {
            HashCodeBuilder.reflectionHashCode(1, 2, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    
    // Add more test cases for other methods
    
}