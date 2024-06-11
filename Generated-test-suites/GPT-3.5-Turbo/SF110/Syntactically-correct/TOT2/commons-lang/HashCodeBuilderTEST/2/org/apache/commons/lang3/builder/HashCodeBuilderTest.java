package org.apache.commons.lang3.builder;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {
    
    @Test
    public void testReflectionHashCode_ObjectNull() {
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, null);
        assertEquals(0, hashCode);
    }
    
    @Test
    public void testReflectionHashCode_ObjectNotNull() {
        Object obj = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, obj);
        assertNotNull(hashCode);
    }
    
    @Test
    public void testAppend_boolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int hashCode = builder.toHashCode();
        assertEquals(37, hashCode);
    }
    
    @Test
    public void testAppendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(100);
        int hashCode = builder.toHashCode();
        assertEquals(137, hashCode);
    }
    
    @Test
    public void testException_IncorrectInitialValue() {
        try {
            HashCodeBuilder builder = new HashCodeBuilder(2, 37);
            fail("Should throw an IllegalArgumentException for incorrect initial value");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testException_IncorrectMultiplier() {
        try {
            HashCodeBuilder builder = new HashCodeBuilder(17, 2);
            fail("Should throw an IllegalArgumentException for incorrect multiplier");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
}