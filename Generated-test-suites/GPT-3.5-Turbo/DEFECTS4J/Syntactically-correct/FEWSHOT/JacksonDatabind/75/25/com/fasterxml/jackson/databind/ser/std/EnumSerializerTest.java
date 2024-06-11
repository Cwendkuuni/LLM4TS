package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.annotation.JsonFormat;

public class EnumSerializerTest {
    private EnumSerializer serializer;

    @Before
    public void setup() {
        EnumValues enumValues = EnumValues.constructFromName(null, TestEnum.class);
        serializer = new EnumSerializer(enumValues, false);
    }

    @Test
    public void testGetEnumValues() {
        EnumValues enumValues = serializer.getEnumValues();
        assertNotNull(enumValues);
        assertEquals(TestEnum.values().length, enumValues.size());
    }

    @Test
    public void testSerialize_AsIndex() {
        assertTrue(serializer._serializeAsIndex(null));
    }

    @Test
    public void testSerialize_AsOrdinal() {
        EnumSerializer serializer = new EnumSerializer(serializer.getEnumValues(), true);
        assertFalse(serializer._serializeAsIndex(null));
    }

    @Test
    public void testSerialize_WithToString() {
        EnumSerializer serializer = new EnumSerializer(serializer.getEnumValues(), null);
        assertFalse(serializer._serializeAsIndex(null));
    }

    @Test
    public void testIsShapeWrittenUsingIndex_Default() {
        assertNull(EnumSerializer._isShapeWrittenUsingIndex(TestEnum.class, null, true));
    }

    @Test
    public void testIsShapeWrittenUsingIndex_Any() {
        assertNull(EnumSerializer._isShapeWrittenUsingIndex(TestEnum.class, JsonFormat.Value.forShape(JsonFormat.Shape.ANY), true));
    }

    @Test
    public void testIsShapeWrittenUsingIndex_Scalar() {
        assertNull(EnumSerializer._isShapeWrittenUsingIndex(TestEnum.class, JsonFormat.Value.forShape(JsonFormat.Shape.SCALAR), true));
    }

    @Test
    public void testIsShapeWrittenUsingIndex_String() {
        assertFalse(EnumSerializer._isShapeWrittenUsingIndex(TestEnum.class, JsonFormat.Value.forShape(JsonFormat.Shape.STRING), true));
    }

    @Test
    public void testIsShapeWrittenUsingIndex_Natural() {
        assertFalse(EnumSerializer._isShapeWrittenUsingIndex(TestEnum.class, JsonFormat.Value.forShape(JsonFormat.Shape.NATURAL), true));
    }

    @Test
    public void testIsShapeWrittenUsingIndex_Array() {
        assertTrue(EnumSerializer._isShapeWrittenUsingIndex(TestEnum.class, JsonFormat.Value.forShape(JsonFormat.Shape.ARRAY), true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsShapeWrittenUsingIndex_InvalidShape() {
        EnumSerializer._isShapeWrittenUsingIndex(TestEnum.class, JsonFormat.Value.forShape(JsonFormat.Shape.OBJECT), true);
    }

    private enum TestEnum {
        VALUE1,
        VALUE2,
        VALUE3
    }
}