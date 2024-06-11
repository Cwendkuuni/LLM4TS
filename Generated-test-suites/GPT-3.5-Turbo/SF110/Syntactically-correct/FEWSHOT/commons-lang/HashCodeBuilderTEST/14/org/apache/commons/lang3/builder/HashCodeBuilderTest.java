package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    private HashCodeBuilder builder;

    @Before
    public void setUp() {
        builder = new HashCodeBuilder();
    }

    @Test
    public void testAppend_boolean() {
        assertEquals(17 * 37 + 0, builder.append(true).toHashCode());
        assertEquals(17 * 37 + 1, builder.append(false).toHashCode());
    }

    @Test
    public void testAppend_booleanArray() {
        boolean[] array = {true, false, true};
        assertEquals(17 * 37 + 0 * 37 + 1 * 37 + 0, builder.append(array).toHashCode());
    }

    @Test
    public void testAppend_byte() {
        assertEquals(17 * 37 + 1, builder.append((byte) 1).toHashCode());
        assertEquals(17 * 37 + 0, builder.append((byte) 0).toHashCode());
    }

    @Test
    public void testAppend_byteArray() {
        byte[] array = {1, 2, 3};
        assertEquals(17 * 37 + 1 * 37 + 2 * 37 + 3, builder.append(array).toHashCode());
    }

    @Test
    public void testAppend_char() {
        assertEquals(17 * 37 + 'a', builder.append('a').toHashCode());
        assertEquals(17 * 37 + 'b', builder.append('b').toHashCode());
    }

    @Test
    public void testAppend_charArray() {
        char[] array = {'a', 'b', 'c'};
        assertEquals(17 * 37 + 'a' * 37 + 'b' * 37 + 'c', builder.append(array).toHashCode());
    }

    @Test
    public void testAppend_double() {
        assertEquals(17 * 37 + Double.hashCode(3.14), builder.append(3.14).toHashCode());
        assertEquals(17 * 37 + Double.hashCode(2.71), builder.append(2.71).toHashCode());
    }

    @Test
    public void testAppend_doubleArray() {
        double[] array = {3.14, 2.71, 1.41};
        assertEquals(17 * 37 + Double.hashCode(3.14) * 37 + Double.hashCode(2.71) * 37 + Double.hashCode(1.41), builder.append(array).toHashCode());
    }

    @Test
    public void testAppend_float() {
        assertEquals(17 * 37 + Float.floatToIntBits(3.14f), builder.append(3.14f).toHashCode());
        assertEquals(17 * 37 + Float.floatToIntBits(2.71f), builder.append(2.71f).toHashCode());
    }

    @Test
    public void testAppend_floatArray() {
        float[] array = {3.14f, 2.71f, 1.41f};
        assertEquals(17 * 37 + Float.floatToIntBits(3.14f) * 37 + Float.floatToIntBits(2.71f) * 37 + Float.floatToIntBits(1.41f), builder.append(array).toHashCode());
    }

    @Test
    public void testAppend_int() {
        assertEquals(17 * 37 + 42, builder.append(42).toHashCode());
        assertEquals(17 * 37 + -42, builder.append(-42).toHashCode());
    }

    @Test
    public void testAppend_intArray() {
        int[] array = {42, -42, 0};
        assertEquals(17 * 37 + 42 * 37 + -42 * 37 + 0, builder.append(array).toHashCode());
    }

    @Test
    public void testAppend_long() {
        assertEquals(17 * 37 + Long.hashCode(1234567890L), builder.append(1234567890L).toHashCode());
        assertEquals(17 * 37 + Long.hashCode(-1234567890L), builder.append(-1234567890L).toHashCode());
    }

    @Test
    public void testAppend_longArray() {
        long[] array = {1234567890L, -1234567890L, 0L};
        assertEquals(17 * 37 + Long.hashCode(1234567890L) * 37 + Long.hashCode(-1234567890L) * 37 + Long.hashCode(0L), builder.append(array).toHashCode());
    }

    @Test
    public void testAppend_Object() {
        Object obj = new Object();
        assertEquals(17 * 37 + obj.hashCode(), builder.append(obj).toHashCode());
    }

    @Test
    public void testAppend_ObjectArray() {
        Object[] array = {new Object(), new Object(), new Object()};
        int expected = 17 * 37;
        for (Object obj : array) {
            expected = expected * 37 + obj.hashCode();
        }
        assertEquals(expected, builder.append(array).toHashCode());
    }

    @Test
    public void testAppend_short() {
        assertEquals(17 * 37 + 42, builder.append((short) 42).toHashCode());
        assertEquals(17 * 37 + -42, builder.append((short) -42).toHashCode());
    }

    @Test
    public void testAppend_shortArray() {
        short[] array = {42, -42, 0};
        assertEquals(17 * 37 + 42 * 37 + -42 * 37 + 0, builder.append(array).toHashCode());
    }

    @Test
    public void testAppendSuper() {
        assertEquals(17 * 37 + 42, builder.appendSuper(42).toHashCode());
        assertEquals(17 * 37 + -42, builder.appendSuper(-42).toHashCode());
    }

    @Test
    public void testToHashCode() {
        builder.append(true);
        builder.append(1234567890L);
        builder.append("test");
        builder.appendSuper(42);
        assertEquals(17 * 37 + (true ? 0 : 1) * 37 + Long.hashCode(1234567890L) * 37 + "test".hashCode() * 37 + 42, builder.toHashCode());
    }
}