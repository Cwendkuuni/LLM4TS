package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

public class EnumSerializerTest {

    @Test
    public void testSerialize() throws IOException {
        EnumValues enumValues = new EnumValues(EnumTest.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues);

        JsonGenerator jsonGenerator = null;
        SerializerProvider serializerProvider = null;

        // Typical use case
        EnumTest enum1 = EnumTest.VALUE1;
        enumSerializer.serialize(enum1, jsonGenerator, serializerProvider);
        // Check the output
        // // TODO: Check the output

        // Edge cases
        // Null value
        enumSerializer.serialize(null, jsonGenerator, serializerProvider);
        // Check the output
        // // TODO: Check the output

        // Error scenarios
        EnumTest enum2 = EnumTest.UNKNOWN;
        enumSerializer.serialize(enum2, jsonGenerator, serializerProvider);
        // Check the output
        // // TODO: Check the output
    }

    private enum EnumTest {
        VALUE1, VALUE2, UNKNOWN;
    }
}