
package org.apache.commons.lang3.builder;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testHashCode() {
        assertEquals(17, new HashCodeBuilder().toHashCode());
    }

    @Test
    public void testHashCodeWithInitialValueAndMultiplier() {
        assertEquals(31, new HashCodeBuilder(7, 11).toHashCode());
    }
    
    @Test
    public void testAppendBoolean() {
        boolean value = true;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        assertEquals(1237, builder.toHashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        boolean[] array = { true, false, true };
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        assertEquals(247591963, builder.toHashCode());
    }

    @Test
    public void testAppendByte() {
        byte value = 3;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        assertEquals(52451, builder.toHashCode());
    }

    @Test
    public void testAppendByteArray() {
        byte[] array = { 1, 2, 3 };
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        assertEquals(160161170, builder.toHashCode());
    }

    @Test
    public void testAppendChar() {
        char value = 'A';
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        assertEquals(65, builder.toHashCode());
    }

    @Test
    public void testAppendCharArray() {
        char[] array = { 'A', 'B', 'C' };
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        assertEquals(2235234, builder.toHashCode());
    }

    @Test
    public void testAppendDouble() {
        double value = 1.5;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        assertEquals(1152, builder.toHashCode());
    }

    @Test
    public void testAppendDoubleArray() {
        double[] array = { 1.1, 2.2, 3.3 };
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        assertEquals(155976411, builder.toHashCode());
    }

    @Test
    public void testAppendFloat() {
        float value = 1.1f;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        assertEquals(1082130432, builder.toHashCode());
    }

    @Test
    public void testAppendFloatArray() {
        float[] array = { 1.1f, 2.2f, 3.3f };
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        assertEquals(739435540, builder.toHashCode());
    }

    @Test
    public void testAppendInt() {
        int value = 5;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        assertEquals(50, builder.toHashCode());
    }

    @Test
    public void testAppendIntArray() {
        int[] array = { 1, 2, 3 };
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        assertEquals(2164747, builder.toHashCode());
    }

    @Test
    public void testAppendLong() {
        long value = 10L;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        assertEquals(26, builder.toHashCode());
    }

    @Test
    public void testAppendLongArray() {
        long[] array = { 1L, 2L, 3L };
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        assertEquals(76501461, builder.toHashCode());
    }

    @Test
    public void testAppendNull() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(null);
        assertEquals(17, builder.toHashCode());
    }

    @Test
    public void testAppendObject() {
        Object value = new Object();
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        assertEquals(1393820542, builder.toHashCode());
    }

    @Test
    public void testAppendObjectArray() {
        Object[] array = { "a", "b", "c" };
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        assertEquals(126546, builder.toHashCode());
    }

    @Test
    public void testAppendShort() {
        short value = 2;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(value);
        assertEquals(66, builder.toHashCode());
    }

    @Test
    public void testAppendShortArray() {
        short[] array = { 1, 2, 3 };
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        assertEquals(73026961, builder.toHashCode());
    }

    @Test
    public void testAppendSuper() {
        int superHashCode = 1234;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(superHashCode);
        assertEquals(56022574, builder.toHashCode());
    }

    @Test
    public void testBuild() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.build().intValue());
    }
}
