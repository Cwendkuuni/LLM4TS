package org.apache.commons.lang3.builder;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilderConstructor() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertNotNull(builder);
        assertEquals(17, builder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderConstructorWithArgs() {
        HashCodeBuilder builder = new HashCodeBuilder(23, 31);
        assertNotNull(builder);
        assertEquals(23, builder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        assertEquals(707, builder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] array = {true, false, true};
        builder.append(array);
        assertEquals(403, builder.toHashCode());
    }

    // Add more test cases for other methods as required

    @Test
    public void testEdgeCasesAndExceptionHandling() {
        // Test edge cases and exception handling scenarios here
    }
}