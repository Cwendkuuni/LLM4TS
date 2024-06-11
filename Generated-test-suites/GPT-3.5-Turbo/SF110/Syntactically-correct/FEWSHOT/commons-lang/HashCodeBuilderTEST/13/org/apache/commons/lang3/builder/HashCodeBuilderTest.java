package org.apache.commons.lang3.builder;

import org.junit.Test;

import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testConstructor() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder = new HashCodeBuilder(2, 3);
        assertEquals(2, builder.toHashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_invalidInitialValue() {
        new HashCodeBuilder(0, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_invalidMultiplier() {
        new HashCodeBuilder(2, 0);
    }

    @Test
    public void testAppend_boolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder.append(true);
        builder.append(false);
        assertEquals(357, builder.toHashCode());
    }

    @Test
    public void testAppend_booleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder.append(new boolean[]{true, true, false});
        assertEquals(3740, builder.toHashCode());
    }

    @Test
    public void testAppend_byte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder.append((byte) 5);
        builder.append((byte) 10);
        assertEquals(359, builder.toHashCode());
    }

    @Test
    public void testAppend_byteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder.append(new byte[]{1, 2, 3});
        assertEquals(523370, builder.toHashCode());
    }

    @Test
    public void testAppend_char() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder.append('a');
        builder.append('b');
        assertEquals(2309, builder.toHashCode());
    }

    @Test
    public void testAppend_charArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder.append(new char[]{'a', 'b', 'c'});
        assertEquals(8232088, builder.toHashCode());
    }

    @Test
    public void testAppend_double() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder.append(3.14);
        builder.append(2.71);
        assertEquals(359, builder.toHashCode());
    }

    @Test
    public void testAppend_doubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder.append(new double[]{1.1, 2.2, 3.3});
        assertEquals(356, builder.toHashCode());
    }

    @Test
    public void testAppend_float() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder.append(3.14f);
        builder.append(2.71f);
        assertEquals(359, builder.toHashCode());
    }

    @Test
    public void testAppend_floatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder.append(new float[]{1.1f, 2.2f, 3.3f});
        assertEquals(366, builder.toHashCode());
    }

    @Test
    public void testAppend_int() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder.append(42);
        builder.append(3);
        assertEquals(302, builder.toHashCode());
    }

    @Test
    public void testAppend_intArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder.append(new int[]{1, 2, 3});
        assertEquals(350, builder.toHashCode());
    }

    @Test
    public void testAppend_long() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder.append(1234567890L);
        builder.append(9876543210L);
        assertEquals(358, builder.toHashCode());
    }

    @Test
    public void testAppend_longArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder.append(new long[]{1L, 2L, 3L});
        assertEquals(322, builder.toHashCode());
    }

    @Test
    public void testAppend_object() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder.append(null);
        assertEquals(289, builder.toHashCode());

        builder.append("foo");
        builder.append(new Object());
        assertEquals(66192, builder.toHashCode());
    }

    @Test
    public void testAppend_objectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder.append(new Object[]{null, "foo", new Object()});
        assertEquals(7465, builder.toHashCode());
    }

    @Test
    public void testAppend_short() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder.append((short) 1);
        builder.append((short) 2);
        assertEquals(245, builder.toHashCode());
    }

    @Test
    public void testAppend_shortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder.append(new short[]{1, 2, 3});
        assertEquals(347, builder.toHashCode());
    }

    @Test
    public void testAppendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder.appendSuper(42);
        assertEquals(835, builder.toHashCode());
    }

    @Test
    public void testBuild() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(Integer.valueOf(17), builder.build());

        builder.append("test");
        assertEquals(Integer.valueOf(1481336), builder.build());

        builder.appendSuper(42);
        assertEquals(Integer.valueOf(9404533), builder.build());
    }

    @Test
    public void testHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.hashCode());

        builder.append("test");
        assertEquals(1481336, builder.hashCode());
    }
}