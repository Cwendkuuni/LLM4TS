
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.impl.StdScalarSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class NumberSerializersTest {
    @Test
    public void testSerializeShort() throws IOException {
        // Arrange
        Short value = 12345;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ShortSerializer serializer = ShortSerializer.instance;

        // Act
        serializer.serialize(value, gen, provider);

        // Assert
        verify(gen).writeNumber(value.shortValue());
    }


    @Test
    public void testSerializeInteger() throws IOException {
        // Arrange
        Integer value = 12345;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer();

        // Act
        serializer.serialize(value, gen, provider);

        // Assert
        verify(gen).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeIntLike() throws IOException {
        // Arrange
        Number value = 12345;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializers.IntLikeSerializer serializer = NumberSerializers.IntLikeSerializer.instance;

        // Act
        serializer.serialize(value, gen, provider);

        // Assert
        verify(gen).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeLong() throws IOException {
        // Arrange
        Long value = Long.MAX_VALUE;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializers.LongSerializer serializer = NumberSerializers.LongSerializer.instance;

        // Act
        serializer.serialize(value, gen, provider);

        // Assert
        verify(gen).writeNumber(value.longValue());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        // Arrange
        Float value = 123.45f;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializers.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;

        // Act
        serializer.serialize(value, gen, provider);

        // Assert
        verify(gen).writeNumber(value.floatValue());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        // Arrange
        Double value = 123.45;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializers.DoubleSerializer serializer = NumberSerializers.DoubleSerializer.instance;

        // Act
        serializer.serialize(value, gen, provider);

        // Assert
        verify(gen).writeNumber(value.doubleValue());
    }
}
