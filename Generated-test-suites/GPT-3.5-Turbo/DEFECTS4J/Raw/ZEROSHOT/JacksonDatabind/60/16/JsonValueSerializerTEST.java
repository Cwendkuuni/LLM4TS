package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

    @Mock
    private JsonGenerator mockJsonGenerator;

    @Mock
    private SerializerProvider mockSerializerProvider;

    private JsonValueSerializer jsonValueSerializer;

    @Before
    public void setUp() {
        jsonValueSerializer = new JsonValueSerializer(null, null);
    }

    @Test
    public void testSerializeNull() throws Exception {
        mockSerializerProvider.defaultSerializeNull(mockJsonGenerator);
        replay(mockSerializerProvider, mockJsonGenerator);

        jsonValueSerializer.serialize(null, mockJsonGenerator, mockSerializerProvider);

        verify(mockSerializerProvider, mockJsonGenerator);
    }

    @Test
    public void testSerializeWithNonNullValueAndNoSerializer() throws Exception {
        Object value = new Object();
        mockSerializerProvider.findTypedValueSerializer(Object.class, true, null);
        expectLastCall().andReturn(null);
        mockSerializerProvider.defaultSerializeNull(mockJsonGenerator);
        replay(mockSerializerProvider, mockJsonGenerator);

        jsonValueSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        verify(mockSerializerProvider, mockJsonGenerator);
    }

    @Test
    public void testSerializeWithNonNullValueAndSerializer() throws Exception {
        Object value = new Object();
        JsonSerializer<Object> mockValueSerializer = mock(JsonSerializer.class);
        expect(mockValueSerializer.getClass()).andReturn(null);
        mockValueSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        expectLastCall();
        replay(mockValueSerializer, mockJsonGenerator, mockSerializerProvider);

        jsonValueSerializer._valueSerializer = mockValueSerializer;
        jsonValueSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        verify(mockValueSerializer, mockJsonGenerator, mockSerializerProvider);
    }

    @Test
    public void testSerializeWithTypeNull() throws Exception {
        mockSerializerProvider.defaultSerializeNull(mockJsonGenerator);
        replay(mockSerializerProvider, mockJsonGenerator);

        jsonValueSerializer.serializeWithType(null, mockJsonGenerator, mockSerializerProvider, null);

        verify(mockSerializerProvider, mockJsonGenerator);
    }

    @Test
    public void testSerializeWithTypeNonNaturalType() throws Exception {
        Object value = new Object();
        Class<?> rawType = String.class;
        JsonSerializer<Object> mockValueSerializer = mock(JsonSerializer.class);
        expect(mockValueSerializer.getClass()).andReturn(null);
        mockSerializerProvider.findTypedValueSerializer(rawType, true, null);
        expectLastCall().andReturn(mockValueSerializer);
        mockValueSerializer.serializeWithType(value, mockJsonGenerator, mockSerializerProvider, null);
        expectLastCall();
        replay(mockValueSerializer, mockJsonGenerator, mockSerializerProvider);

        jsonValueSerializer._valueSerializer = mockValueSerializer;
        jsonValueSerializer.isNaturalTypeWithStdHandling(rawType, mockValueSerializer);
        jsonValueSerializer.serializeWithType(value, mockJsonGenerator, mockSerializerProvider, null);

        verify(mockValueSerializer, mockJsonGenerator, mockSerializerProvider);
    }
}