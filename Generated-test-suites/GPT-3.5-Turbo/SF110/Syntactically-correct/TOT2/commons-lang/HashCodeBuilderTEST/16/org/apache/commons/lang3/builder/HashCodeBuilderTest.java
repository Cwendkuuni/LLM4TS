package org.apache.commons.lang3.builder;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testConstructor() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertNotNull(builder);
    }

    @Test
    public void testReflectionHashCode() {
        Object object = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, object, false);
        assertNotNull(hashCode);
    }

    @Test
    public void testReflectionHashCodeWithNullObject() {
        try {
            HashCodeBuilder.reflectionHashCode(17, 37, null, false);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The object to build a hash code for must not be null", e.getMessage());
        }
    }

    // Add more test cases for other methods

}