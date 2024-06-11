package org.apache.commons.lang3.builder;

import org.junit.Test;

import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode() {
        Object object = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, object);
        assertNotNull(hashCode);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        assertEquals(53, builder.toHashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        assertEquals(97, builder.toHashCode());
    }
}