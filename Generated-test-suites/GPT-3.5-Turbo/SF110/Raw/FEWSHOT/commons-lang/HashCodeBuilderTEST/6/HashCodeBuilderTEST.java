package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HashCodeBuilderTest {
    @Test
    public void testConstructor() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder = new HashCodeBuilder(3, 5);
        assertEquals(3, builder.toHashCode());
    }

    @Test
    public void testAppend() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        builder.append(true);
        assertEquals(37, builder.toHashCode());

        builder.append(false);
        assertEquals(38, builder.toHashCode());

        builder.append((byte) 12);
        assertEquals(1042, builder.toHashCode());

        builder.append((char) 'a');
        assertEquals(35020, builder.toHashCode());

        builder.append(1.5);
        assertEquals(746369784, builder.toHashCode());

        builder.append(2.5f);
        assertEquals(747291034, builder.toHashCode());

        builder.append(10);
        assertEquals(1401187021, builder.toHashCode());

        builder.append(100L);
        assertEquals(127465224, builder.toHashCode());

        final Object obj = new Object();
        builder.append(obj);
        assertEquals(396648, builder.toHashCode());

        final Object[] arr = {1, 2, 3};
        builder.append(arr);
        assertEquals(662079325, builder.toHashCode());

        builder.append(Short.valueOf((short) 10));
        assertEquals(662079335, builder.toHashCode());

        builder.appendSuper(9999);
        assertEquals(672695118, builder.toHashCode());
    }

    @Test
    public void testReflectionHashCode() {
        final Object obj = new Object();
        final int hashCode = HashCodeBuilder.reflectionHashCode(obj, true);
        assertEquals(obj.hashCode(), hashCode);
    }

    @Test
    public void testReflectionAppend() {
        final Object obj = new Object();
        final HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder.reflectionAppend(obj, null, builder, true);
        assertEquals(obj.hashCode(), builder.toHashCode());
    }
}