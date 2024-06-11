package org.apache.commons.lang3.builder;

import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        assertEquals(37, builder.toHashCode());

        builder = new HashCodeBuilder();
        builder.append(false);
        assertEquals(38, builder.toHashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new boolean[]{true, false, true});
        assertEquals(156262, builder.toHashCode());

        builder = new HashCodeBuilder();
        builder.append(null);
        assertEquals(37, builder.toHashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 5);
        assertEquals(186, builder.toHashCode());
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new byte[]{1, 2, 3});
        assertEquals(7713278, builder.toHashCode());

        builder = new HashCodeBuilder();
        builder.append(null);
        assertEquals(37, builder.toHashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        assertEquals(541, builder.toHashCode());
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new char[]{'a', 'b', 'c'});
        assertEquals(2385082, builder.toHashCode());

        builder = new HashCodeBuilder();
        builder.append(null);
        assertEquals(37, builder.toHashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(5.0);
        assertEquals(75743, builder.toHashCode());
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new double[]{1.2, 3.4, 5.6});
        assertEquals(0, builder.toHashCode());

        builder = new HashCodeBuilder();
        builder.append(null);
        assertEquals(37, builder.toHashCode());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(5.0f);
        assertEquals(1844963, builder.toHashCode());
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new float[]{1.2f, 3.4f, 5.6f});
        assertEquals(0, builder.toHashCode());

        builder = new HashCodeBuilder();
        builder.append(null);
        assertEquals(37, builder.toHashCode());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(5);
        assertEquals(186, builder.toHashCode());
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new int[]{1, 2, 3});
        assertEquals(3712, builder.toHashCode());

        builder = new HashCodeBuilder();
        builder.append(null);
        assertEquals(37, builder.toHashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(5L);
        assertEquals(186, builder.toHashCode());
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new long[]{1L, 2L, 3L});
        assertEquals(3328, builder.toHashCode());

        builder = new HashCodeBuilder();
        builder.append(null);
        assertEquals(37, builder.toHashCode());
    }

    @Test
    public void testAppendObject() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(obj1);
        assertTrue(builder.toHashCode() != builder.append(obj2).toHashCode());

        builder = new HashCodeBuilder();
        builder.append(null);
        assertEquals(37, builder.toHashCode());
    }

    @Test
    public void testAppendObjectArray() {
        Object[] array = new Object[]{new Object(), new Object()};

        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(array);
        assertEquals(187, builder.toHashCode());

        builder = new HashCodeBuilder();
        builder.append(null);
        assertEquals(37, builder.toHashCode());
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 5);
        assertEquals(186, builder.toHashCode());
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new short[]{1, 2, 3});
        assertEquals(3328, builder.toHashCode());

        builder = new HashCodeBuilder();
        builder.append(null);
        assertEquals(37, builder.toHashCode());
    }

    @Test
    public void testAppendSuper() {
        HashCodeBuilder subBuilder = new HashCodeBuilder();
        subBuilder.append(5);
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(subBuilder.toHashCode());
        assertEquals(6637, builder.toHashCode());
    }

    @Test
    public void testBuild() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(builder.toHashCode(), builder.build().intValue());
    }

    @Test
    public void testHashCode() {
        HashCodeBuilder builder1 = new HashCodeBuilder();
        HashCodeBuilder builder2 = new HashCodeBuilder();
        assertEquals(builder1.toHashCode(), builder2.toHashCode());
    }
}