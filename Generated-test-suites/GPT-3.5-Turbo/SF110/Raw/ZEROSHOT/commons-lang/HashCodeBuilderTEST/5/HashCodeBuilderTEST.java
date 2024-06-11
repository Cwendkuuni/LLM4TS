package org.apache.commons.lang3.builder;

import org.junit.Test;
import org.junit.Assert;
import java.util.*;

public class HashCodeBuilderTest {
    
    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int expected = 17 * 37 + 0;
        int actual = builder.toHashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new boolean[]{true, false});
        int expected = (17 * 37 + 0) * 37 + 1;
        int actual = builder.toHashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 5);
        int expected = 17 * 37 + 5;
        int actual = builder.toHashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new byte[]{0, 1, 2});
        int expected = (17 * 37 + 0) * 37 + 1;
        expected = (expected * 37) + 2;
        int actual = builder.toHashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        int expected = 17 * 37 + 'a';
        int actual = builder.toHashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new char[]{'a', 'b', 'c'});
        int expected = (17 * 37 + 'a') * 37 + 'b';
        expected = (expected * 37) + 'c';
        int actual = builder.toHashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(3.14);
        long bits = Double.doubleToLongBits(3.14);
        int expected = 17 * 37 + (int) (bits ^ bits >> 32);
        int actual = builder.toHashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new double[]{2.71, 3.14});
        long bits1 = Double.doubleToLongBits(2.71);
        int expected = (17 * 37 + (int) (bits1 ^ bits1 >> 32)) * 37 + (int) (bits1 ^ bits1 >> 32);
        int actual = builder.toHashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.23f);
        int expected = 17 * 37 + Float.floatToIntBits(1.23f);
        int actual = builder.toHashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new float[]{1.23f, 4.56f});
        int expected = (17 * 37 + Float.floatToIntBits(1.23f)) * 37 + Float.floatToIntBits(4.56f);
        int actual = builder.toHashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(42);
        int expected = 17 * 37 + 42;
        int actual = builder.toHashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new int[]{1, 2, 3});
        int expected = (17 * 37 + 1) * 37 + 2;
        expected = (expected * 37) + 3;
        int actual = builder.toHashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(123456789L);
        int expected = 17 * 37 + (int) (123456789L ^ 123456789L >> 32);
        int actual = builder.toHashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new long[]{123456789L, 987654321L});
        int expected = (17 * 37 + (int) (123456789L ^ 123456789L >> 32)) * 37 + (int) (987654321L ^ 987654321L >> 32);
        int actual = builder.toHashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object object = new Object();
        builder.append(object);
        int expected = 17 * 37 + object.hashCode();
        int actual = builder.toHashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object obj1 = new Object();
        Object obj2 = new Object();
        builder.append(new Object[]{obj1, obj2});
        int expected = (17 * 37 + obj1.hashCode()) * 37 + obj2.hashCode();
        int actual = builder.toHashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 7);
        int expected = 17 * 37 + 7;
        int actual = builder.toHashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new short[]{7, 8, 9});
        int expected = (17 * 37 + 7) * 37 + 8;
        expected = (expected * 37) + 9;
        int actual = builder.toHashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAppendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int superHashCode = 1234;
        builder.appendSuper(superHashCode);
        int expected = 17 * 37 + superHashCode;
        int actual = builder.toHashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBuild() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int expected = builder.toHashCode();
        int actual = builder.build();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int expected = builder.toHashCode();
        int actual = builder.hashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testReflectionHashCode() {
        Object object = new Object();
        int expected = Objects.hash(object);
        int actual = HashCodeBuilder.reflectionHashCode(object);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testReflectionHashCodeWithTransients() {
        Object object = new Object();
        int expected = Objects.hash(object);
        int actual = HashCodeBuilder.reflectionHashCode(object, true);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testReflectionHashCodeWithExcludes() {
        Object object = new Object();
        int expected = Objects.hash(object);
        int actual = HashCodeBuilder.reflectionHashCode(object, Arrays.asList("field1", "field2"));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testReflectionHashCodeWithReflectUpToClass() {
        Object object = new Object();
        int expected = Objects.hash(object);
        int actual = HashCodeBuilder.reflectionHashCode(object, true, Object.class);
        Assert.assertEquals(expected, actual);
    }
    
}