package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.junit.Test;

public class HashCodeBuilderTest {
    
    @Test
    public void testReflectionHashCode() {
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, "test");
        assertEquals(1612, hashCode);
    }
    
    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        assertEquals(54, builder.toHashCode());
    }
    
    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 5);
        assertEquals(186, builder.toHashCode());
    }
    
    // Include more test cases for other methods if needed
    
}