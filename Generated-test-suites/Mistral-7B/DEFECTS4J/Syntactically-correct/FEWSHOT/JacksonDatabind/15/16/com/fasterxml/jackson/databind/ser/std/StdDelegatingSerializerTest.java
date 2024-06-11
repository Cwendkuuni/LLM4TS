package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    private Converter<Object, ?> converter;
    private StdDelegatingSerializer<Object> delegatingSerializer;
    private JsonSerializer<Object> delegateSerializer;

    @Before
    public void setup() {
        converter = mock(Converter.class);
        delegateSerializer = mock(JsonSerializer.class);
        delegatingSerializer = new StdDelegatingSerializer(converter);
    }

    @Test
    public void testSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        when(converter.convert(any())).thenReturn("testValue");
        when(delegateSerializer.serialize(any(), eq(new JsonGenerator()))).thenAnswer(i -> i.getArguments()[0]);

        Object value = new Object();
        String result = delegatingSerializer.serialize(value, new JsonGenerator(), new SerializerProvider());

        Assert.assertEquals("testValue", result);
    }

    @Test
    public void testSerializationWithType() throws JsonGenerationException, JsonMappingException, IOException {
        when(converter.convert(any())).thenReturn("testValue");
        when(delegateSerializer.serializeWithType(any(), eq(new JsonGenerator()), any(), eq(TypeSerializer.instance))).thenAnswer(i -> i.getArguments()[0]);

        Object value = new Object();
        delegatingSerializer.serializeWithType(value, new JsonGenerator(), new SerializerProvider(), TypeSerializer.instance);

        verify(delegateSerializer).serializeWithType(eq("testValue"), any(), any(), eq(TypeSerializer.instance));
    }

    @Test
    public void testResolve() {
        delegatingSerializer.resolve(new SerializerProvider());
        verify(delegateSerializer).resolve(new SerializerProvider());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        when(converter.getOutputType(any())).thenReturn(String.class);
        JsonSerializer<String> stringSerializer = mock(JsonSerializer.class);

        delegatingSerializer = delegatingSerializer.withDelegate(converter, String.class, stringSerializer);

        assertEquals(stringSerializer, delegatingSerializer.getDelegatee());
    }

    @Test
    public void testGetDelegatee() {
        JsonSerializer<Object> objectSerializer = mock(JsonSerializer.class);
        delegatingSerializer = new StdDelegatingSerializer(converter, null, objectSerializer);

        assertEquals(objectSerializer, delegatingSerializer.getDelegatee());
    }

    @Test
    public void testGetConverter() {
        assertEquals(converter, delegatingSerializer.getConverter());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        SchemaAware schemaAwareDelegateSerializer = mock(SchemaAware.class);

        delegatingSerializer = new StdDelegatingSerializer(converter, null, schemaAwareDelegateSerializer);

        delegatingSerializer.acceptJsonFormatVisitor(any(), any());

        verify(schemaAwareDelegateSerializer).acceptJsonFormatVisitor(any(), any());
    }

    @Test
    public void testIsEmpty() {
        Object nullValue = null;
        Object emptyValue = new Object();

        when(delegateSerializer.isEmpty(any())).thenReturn(false);

        Assert.assertFalse(delegatingSerializer.isEmpty(new SerializerProvider(), nullValue));
        Assert.assertFalse(delegatingSerializer.isEmpty(new SerializerProvider(), emptyValue));
    }

    @Test
    public void testIsEmptyWithProvider() {
        Object nullValue = null;
        Object emptyValue = new Object();

        when(delegateSerializer.isEmpty(eq(new SerializerProvider()), any())).thenReturn(false);

        Assert.assertFalse(delegatingSerializer.isEmpty(new SerializerProvider(), nullValue));
        Assert.assertFalse(delegatingSerializer.isEmpty(new SerializerProvider(), emptyValue));
    }
}