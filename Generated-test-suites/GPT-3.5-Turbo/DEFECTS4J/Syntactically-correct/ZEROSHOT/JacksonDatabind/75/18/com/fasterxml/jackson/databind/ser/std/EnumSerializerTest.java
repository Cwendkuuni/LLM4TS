package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EnumSerializerTest {

    private EnumSerializer enumSerializer;

    @Mock
    private EnumValues enumValues;

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private JsonGenerator jsonGenerator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        enumSerializer = new EnumSerializer(enumValues, true);
    }

    @Test
    public void testSerializeAsIndex() throws IOException {
        enumSerializer.serialize(Enum.TEST, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeNumber(Enum.TEST.ordinal());
    }

    @Test
    public void testSerializeUsingToString() throws IOException {
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        enumSerializer.serialize(Enum.TEST, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeString(Enum.TEST.toString());
    }

    @Test
    public void testSerializeUsingEnumValues() throws IOException {
        when(enumValues.serializedValueFor(Enum.TEST)).thenReturn("TEST");
        enumSerializer.serialize(Enum.TEST, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeString("TEST");
    }

    @Test
    public void testCreateContextual() throws Exception {
        assertNull(enumSerializer.createContextual(serializerProvider, null));
    }

    @Test
    public void testGetEnumValues() {
        assertEquals(enumValues, enumSerializer.getEnumValues());
    }
    
    enum Enum {
        TEST
    }
}