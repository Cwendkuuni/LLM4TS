package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class NumberSerializersTest {
    private NumberSerializers.Base<?> baseInstance;
    private JsonGenerator jsonGeneratorMock;
    private SerializerProvider serializerProviderMock;
    private TypeSerializer typeSerializerMock;

    @Before
    public void setup() {
        baseInstance = new NumberSerializers.Base<Number>(Number.class, null, null) {
            @Override
            public Object serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider,
                    TypeSerializer typeSer) throws IOException {
                return null;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            }
        };
        jsonGeneratorMock = mock(JsonGenerator.class);
        serializerProviderMock = mock(SerializerProvider.class);
        typeSerializerMock = mock(TypeSerializer.class);
    }

    @Test
    public void testGetSchema() {
        assertNull(baseInstance.getSchema(serializerProviderMock, null));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws IOException {
        baseInstance.acceptJsonFormatVisitor(jsonFormatVisitorWrapperMock, javaTypeMock);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        assertNull(baseInstance.createContextual(serializerProviderMock, beanPropertyMock));
    }

    @Test
    public void testSerializeWithType() throws IOException {
        baseInstance.serializeWithType(valueMock, jsonGeneratorMock, serializerProviderMock, typeSerializerMock);
    }

    @Test
    public void testSerialize() throws IOException {
        baseInstance.serialize(valueMock, jsonGeneratorMock, serializerProviderMock);
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
        shortSerializer.serialize((short) 5, jsonGeneratorMock, serializerProviderMock);
        verify(jsonGeneratorMock, times(1)).writeNumber(anyShort());
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer();
        integerSerializer.serialize(5, jsonGeneratorMock, serializerProviderMock);
        verify(jsonGeneratorMock, times(1)).writeNumber(anyInt());
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer intLikeSerializer = new NumberSerializers.IntLikeSerializer();
        intLikeSerializer.serialize(5, jsonGeneratorMock, serializerProviderMock);
        verify(jsonGeneratorMock, times(1)).writeNumber(anyInt());
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer();
        longSerializer.serialize(5L, jsonGeneratorMock, serializerProviderMock);
        verify(jsonGeneratorMock, times(1)).writeNumber(anyLong());
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer floatSerializer = new NumberSerializers.FloatSerializer();
        floatSerializer.serialize(5.0f, jsonGeneratorMock, serializerProviderMock);
        verify(jsonGeneratorMock, times(1)).writeNumber(anyFloat());
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer();
        doubleSerializer.serialize(5.0, jsonGeneratorMock, serializerProviderMock);
        verify(jsonGeneratorMock, times(1)).writeNumber(anyDouble());
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(6, allDeserializers.size());
    }
}