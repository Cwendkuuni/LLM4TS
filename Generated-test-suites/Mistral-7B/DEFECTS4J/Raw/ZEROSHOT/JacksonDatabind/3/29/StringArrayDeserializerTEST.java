package com.example.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import org.junit.Test;

/**
 * Test class for StringArrayDeserializer.
 */
public class StringArrayDeserializerTest {

    @Test
    public void testDeserializeWithNullElementDeserializer() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        ObjectBuffer objectBuffer = mock(ObjectBuffer.class);

        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(deserializationContext.leaseObjectBuffer()).thenReturn(objectBuffer);

        StringArrayDeserializer stringArrayDeserializer = StringArrayDeserializer.instance;
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeWithEmptyArray() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        ObjectBuffer objectBuffer = mock(ObjectBuffer.class);

        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(deserializationContext.leaseObjectBuffer()).thenReturn(objectBuffer);

        StringArrayDeserializer stringArrayDeserializer = StringArrayDeserializer.instance;
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeWithSingleValue() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        ObjectBuffer objectBuffer = mock(ObjectBuffer.class);

        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test");
        when(deserializationContext.leaseObjectBuffer()).thenReturn(objectBuffer);

        StringArrayDeserializer stringArrayDeserializer = StringArrayDeserializer.instance;
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        assertArrayEquals(new String[]{"test"}, result);
    }

    @Test
    public void testDeserializeWithCustomElementDeserializer() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        ObjectBuffer objectBuffer = mock(ObjectBuffer.class);
        JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);

        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(deserializationContext.leaseObjectBuffer()).thenReturn(objectBuffer);
        when(deserializationContext.findValueDeserializer(String.class)).thenReturn(stringDeserializer);

        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer(stringDeserializer);
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        assertArrayEquals(new String[]{"test", null}, result);
    }

    @Test
    public void testDeserializeWithEmptyStringAsNull() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        ObjectBuffer objectBuffer = mock(ObjectBuffer.class);

        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("");
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(deserializationContext.leaseObjectBuffer()).thenReturn(objectBuffer);

        StringArrayDeserializer stringArrayDeserializer = StringArrayDeserializer.instance;
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithInvalidToken() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        ObjectBuffer objectBuffer = mock(ObjectBuffer.class);

        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        when(deserializationContext.leaseObjectBuffer()).thenReturn(objectBuffer);

        StringArrayDeserializer stringArrayDeserializer = StringArrayDeserializer.instance;
        stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        // Should throw an IOException
    }

    @Test
    public void testCreateContextual() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);

        JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);

        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test");
        when(deserializationContext.findValueDeserializer(String.class)).thenReturn(stringDeserializer);

        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContext, property);

        assertNotNull(result);
        assertTrue(result instanceof StringArrayDeserializer);

        if (result instanceof StringArrayDeserializer) {
            StringArrayDeserializer stringArrayDeserializerResult = (StringArrayDeserializer) result;
            assertSame(stringDeserializer, stringArrayDeserializerResult._elementDeserializer);
        }
    }

    @Test
    public void testCreateContextualWithCustomDeserializer() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);

        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test");
        when(deserializationContext.findValueDeserializer(String.class)).thenReturn(stringDeserializer);

        JsonDeserializer<String[]> expectedResult = new StringArrayDeserializer(stringDeserializer);

        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContext, property);

        assertNotNull(result);
        assertTrue(result instanceof StringArrayDeserializer);

        if (result instanceof StringArrayDeserializer) {
            StringArrayDeserializer stringArrayDeserializerResult = (StringArrayDeserializer) result;
            assertSame(expectedResult, stringArrayDeserializerResult);
        }
    }

    @Test
    public void testCreateContextualWithContentConverter() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
        ObjectMapper objectMapper = mock(ObjectMapper.class);

        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test");
        when(deserializationContext.findValueDeserializer(String.class)).thenReturn(stringDeserializer);
        when(deserializationContext.getConfig()).thenReturn(objectMapper.getConfig());

        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContext, property);

        assertNotNull(result);
        assertTrue(result instanceof StringArrayDeserializer);

        if (result instanceof StringArrayDeserializer) {
            StringArrayDeserializer stringArrayDeserializerResult = (StringArrayDeserializer) result;
            JsonDeserializer<String[]> expectedResult = new StringArrayDeserializer(stringDeserializer);
            expectedResult = objectMapper.configure(DeserializationFeature.USE_JACKSON_ANNOTATIONS, true)
                    .addAbstractTypeResolver(new AbstractTypeResolver() {
                        @Override
                        public TypeResolution findContentTypeResolver(DeserializationContext ctxt, TypeDeserializer propertyType) {
                            return new SimpleTypeResolver(new TypeReference<String[]>() {}.getType());
                        }
                    }).deserializerForType(new TypeReference<String[]>() {}.getType());
            assertSame(expectedResult, stringArrayDeserializerResult);
        }
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "[\"test1\", \"test2\", \"test3\"]";
        StringArrayDeserializer stringArrayDeserializer = StringArrayDeserializer.instance;

        Object result = objectMapper.readValue(json, new TypeReference<Object>() {}.getType());

        assertArrayEquals(new String[]{"test1", "test2", "test3"}, ((Object[]) result));
    }
}