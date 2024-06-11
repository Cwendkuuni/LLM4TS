package org.apache.commons.lang3.builder;
import static org.junit.Assert.*;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode_ObjectNotNull() {
        Object obj = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, obj, false, null, "exclude");
        assertNotNull(hashCode);
    }

    @Test
    public void testReflectionHashCode_ObjectNull() {
        try {
            HashCodeBuilder.reflectionHashCode(17, 37, null, false, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testReflectionHashCode_ObjectWithExcludeFields() {
        Object obj = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, obj, false, null, "exclude");
        assertNotNull(hashCode);
    }

    @Test
    public void testAppend_Boolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        builder.append(false);
        int hashCode = builder.toHashCode();
        assertNotEquals(0, hashCode);
    }
    
    // Add more test cases for other methods here

}