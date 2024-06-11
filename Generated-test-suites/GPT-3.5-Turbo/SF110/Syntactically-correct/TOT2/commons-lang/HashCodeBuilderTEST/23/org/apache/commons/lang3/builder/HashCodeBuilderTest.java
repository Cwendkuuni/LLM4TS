package org.apache.commons.lang3.builder;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode() {
        Object obj = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, obj, false, null);
        assertNotNull(hashCode);
    }

    @Test
    public void testReflectionHashCodeWithNullObject() {
        try {
            HashCodeBuilder.reflectionHashCode(17, 37, null, false, null);
            fail("Exception not thrown for null object");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        assertEquals(37, builder.toHashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new boolean[]{true, false, true});
        assertEquals(1106, builder.toHashCode());
    }

    // Add more test cases for other methods as needed

}