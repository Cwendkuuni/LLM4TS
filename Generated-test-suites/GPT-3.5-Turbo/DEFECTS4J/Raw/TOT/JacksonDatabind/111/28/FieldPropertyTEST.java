package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FieldPropertyTest {

    private static FieldProperty fieldProperty;

    @BeforeClass
    public static void setUp() throws NoSuchFieldException {
        Field field = FieldProperty.class.getDeclaredField("_field");
        field.setAccessible(true);
        fieldProperty = new FieldProperty(
                null,
                null,
                null,
                null,
                new AnnotatedField(null, field, null)
        );
    }

    @Test
    public void testGetAnnotation() {
        assertNull(fieldProperty.getAnnotation(TestAnnotation.class));
    }

    @Test
    public void testGetMember() {
        assertNotNull(fieldProperty.getMember());
    }

    @Test
    public void testFixAccess() throws NoSuchFieldException {
        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();
        Field field = fieldProperty.getClass().getDeclaredField("_field");
        boolean isAccessible = field.isAccessible();
        fieldProperty.fixAccess(config);
        assertEquals(isAccessible, field.isAccessible());
    }

    @Test
    public void testDeserializeAndSet_NullTokenSkipNulls() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        fieldProperty._skipNulls = true;

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(jsonParser, Mockito.times(0)).isNull();
        verify(jsonParser, Mockito.times(0)).getNullValue();

        assertNull(instance);
    }

    @Test
    public void testDeserializeAndSet_NullToken() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());
        jsonParser = jsonFactory.createParser("{\"foo\": null}");

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(jsonParser).hasToken(JsonToken.VALUE_NULL);
        verify(jsonParser, Mockito.times(0)).isStructStart();
        verify(jsonParser, Mockito.times(0)).getNullValue();

        assertNotNull(instance);
    }

    @Test
    public void testDeserializeAndSet_NormalValue() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = "test";

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(fieldProperty._valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(value);

        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(jsonParser).hasToken(JsonToken.VALUE_NULL);
        verify(jsonParser, Mockito.times(0)).isStructStart();
        verify(jsonParser, Mockito.times(0)).getNullValue();
        verify(fieldProperty._valueDeserializer).deserialize(jsonParser, deserializationContext);

        assertEquals(value, instance);
    }

    @Test
    public void testDeserializeAndSet_NullValue() throws IOException {
        JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());
        JsonParser jsonParser = jsonFactory.createParser("{\"foo\": null}");
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        Object nullValue = null;

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(fieldProperty._valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(nullValue);
        when(fieldProperty._nullProvider.getNullValue(deserializationContext)).thenReturn(nullValue);

        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(jsonParser).hasToken(JsonToken.VALUE_NULL);
        verify(jsonParser, Mockito.times(0)).isStructStart();
        verify(jsonParser, Mockito.times(0)).getNullValue();
        verify(fieldProperty._valueDeserializer).deserialize(jsonParser, deserializationContext);
        verify(fieldProperty._nullProvider).getNullValue(deserializationContext);

        assertEquals(nullValue, instance);
    }

    @Test
    public void testDeserializeSetAndReturn_NullTokenSkipNulls() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        fieldProperty._skipNulls = true;

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        Object result = fieldProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        verify(jsonParser, Mockito.times(0)).isNull();
        verify(jsonParser, Mockito.times(0)).getNullValue();

        assertNull(result);
    }

    @Test
    public void testDeserializeSetAndReturn_NullToken() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());
        jsonParser = jsonFactory.createParser("{\"foo\": null}");

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        Object result = fieldProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        verify(jsonParser).hasToken(JsonToken.VALUE_NULL);
        verify(jsonParser, Mockito.times(0)).isStructStart();
        verify(jsonParser, Mockito.times(0)).getNullValue();

        assertNotNull(result);
    }

    @Test
    public void testSet_NormalValue() throws IOException {
        Object instance = new Object();
        Object value = "test";

        fieldProperty.set(instance, value);

        assertEquals(value, instance);
    }

    @Test
    public void testSet_NullValue() throws IOException {
        Object instance = new Object();
        Object nullValue = null;

        fieldProperty.set(instance, nullValue);

        assertEquals(nullValue, instance);
    }

    @Test
    public void testSetAndReturn_NormalValue() throws IOException {
        Object instance = new Object();
        Object value = "test";

        Object result = fieldProperty.setAndReturn(instance, value);

        assertEquals(instance, result);
        assertEquals(value, instance);
    }

    @Test
    public void testSetAndReturn_NullValue() throws IOException {
        Object instance = new Object();
        Object nullValue = null;

        Object result = fieldProperty.setAndReturn(instance, nullValue);

        assertEquals(instance, result);
        assertEquals(nullValue, instance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFixAccess_NullField() throws NoSuchFieldException {
        Field field = FieldProperty.class.getDeclaredField("_field");
        FieldProperty fieldPropertyWithNullField = new FieldProperty(
                null,
                null,
                null,
                null,
                new AnnotatedField(null, null, null)
        );

        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();
        boolean isAccessible = field.isAccessible();
        fieldPropertyWithNullField.fixAccess(config);
        assertEquals(isAccessible, field.isAccessible());      
    }
}