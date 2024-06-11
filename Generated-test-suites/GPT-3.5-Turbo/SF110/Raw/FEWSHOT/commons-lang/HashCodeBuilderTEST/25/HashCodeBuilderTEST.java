package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertNotNull(builder);
    }

    @Test
    public void testHashCodeBuilderWithParameters() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 37);
        assertNotNull(builder);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        assertEquals(37, builder.toHashCode());

        builder.append(false);
        assertEquals(74, builder.toHashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] array = {true, false, true};
        builder.append(array);
        assertEquals(111, builder.toHashCode());

        builder = new HashCodeBuilder();
        boolean[] emptyArray = {};
        builder.append(emptyArray);
        assertEquals(0, builder.toHashCode());
    }
    
    // Continue with more test methods for other append methods...

}