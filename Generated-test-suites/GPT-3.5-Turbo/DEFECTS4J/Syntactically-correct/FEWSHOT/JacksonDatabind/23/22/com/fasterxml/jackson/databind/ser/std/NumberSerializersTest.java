package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Util;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = new ShortSerializer();

        Short value = 10;
        assertEquals(value.shortValue(), serializer.serialize(value));

        value = -10;
        assertEquals(value.shortValue(), serializer.serialize(value));
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = new IntegerSerializer();

        Integer value = 100;
        assertEquals(value.intValue(), serializer.serialize(value));

        value = -100;
        assertEquals(value.intValue(), serializer.serialize(value));
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer serializer = new IntLikeSerializer();

        Byte value = 5;
        assertEquals(value.intValue(), serializer.serialize(value));

        value = -5;
        assertEquals(value.intValue(), serializer.serialize(value));
    }

    @Test
    public void testLongSerializer() {
        LongSerializer serializer = new LongSerializer();

        Long value = 1000L;
        assertEquals(value.longValue(), serializer.serialize(value));

        value = -1000L;
        assertEquals(value.longValue(), serializer.serialize(value));
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer serializer = new FloatSerializer();

        Float value = 10.5f;
        assertEquals(value.floatValue(), serializer.serialize(value), 0.0);

        value = -10.5f;
        assertEquals(value.floatValue(), serializer.serialize(value), 0.0);
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer serializer = new DoubleSerializer();

        Double value = 10.5;
        assertEquals(value.doubleValue(), serializer.serialize(value), 0.0);

        value = -10.5;
        assertEquals(value.doubleValue(), serializer.serialize(value), 0.0);
    }
}