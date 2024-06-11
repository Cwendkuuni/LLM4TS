package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

    private ObjectMapper objectMapper;
    private TypeIdResolver typeIdResolver;
    private DeserializationContext context;
    private JsonDeserializer<String> stringDeserializer;
    private TypeDeserializer asWrapperTypeDeserializer;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        typeIdResolver = mock(TypeIdResolver.class);
        context = mock(DeserializationContext.class);
        stringDeserializer = mock(JsonDeserializer.class);
        asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
                objectMapper.getTypeFactory().constructType(String.class), typeIdResolver, "_type", false, String.class);

        when(typeIdResolver.idFromValueAndType(eq(String.class), any())).thenReturn(null);
        when(typeIdResolver.findValueId(eq(String.class), any())).thenReturn("valueTypeId");
        when(typeIdResolver.findValueId(eq(String.class), eq("value"))).thenReturn("subTypeId");
        when(context.findBeanDescription(eq(String.class))).thenReturn(constructBeanDescription());
        when(context.findDeserializer(eq(String.class), any())).thenReturn(stringDeserializer);
    }

    @Test
    public void testDeserializeTypedFromObject() throws JsonProcessingException, IOException {
        JsonParser jsonParser = createJsonParser("{\"type\":\"valueTypeId\", \"value\":\"value\"}");

        Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, context);

        assertEquals("value", result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws JsonProcessingException, IOException {
        JsonParser jsonParser = createJsonParser("{\"type\":\"valueTypeId\", \"value\":[{\"type\":\"subTypeId\", \"value\":\"subValue\"}]}");

        Object result = asWrapperTypeDeserializer.deserializeTypedFromArray(jsonParser, context);

        assertEquals("subValue", result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws JsonProcessingException, IOException {
        JsonParser jsonParser = createJsonParser("{\"type\":\"valueTypeId\", \"value\":\"valueString\"}");

        Object result = asWrapperTypeDeserializer.deserializeTypedFromScalar(jsonParser, context);

        assertEquals("valueString", result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws JsonProcessingException, IOException {
        JsonParser jsonParser = createJsonParser("{\"type\":\"valueTypeId\", \"value\":123}");

        Object result = asWrapperTypeDeserializer.deserializeTypedFromAny(jsonParser, context);

        assertEquals(123, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithInvalidJson() throws IOException {
        JsonParser jsonParser = createJsonParser("{\"type\":\"valueTypeId\"}");

        asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithMissingType() throws IOException {
        JsonParser jsonParser = createJsonParser("{\"value\":\"value\"}");

        asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithMissingValue() throws IOException {
        JsonParser jsonParser = createJsonParser("{\"type\":\"valueTypeId\"}");

        asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, context);
    }

    @Test
    public void testDeserializeWithTypeIdVisible() throws JsonProcessingException, IOException {
        AsWrapperTypeDeserializer visibleAsWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
                objectMapper.getTypeFactory().constructType(String.class), typeIdResolver, "_type", true, String.class);

        JsonParser jsonParser = createJsonParser("{\"_type\":\"valueTypeId\", \"value\":\"value\"}");

        Object result = visibleAsWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, context);

        assertEquals("value", result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithTypeIdVisibleAndMissingType() throws IOException {
        AsWrapperTypeDeserializer visibleAsWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
                objectMapper.getTypeFactory().constructType(String.class), typeIdResolver, "_type", true, String.class);

        JsonParser jsonParser = createJsonParser("{\"_type\":\"valueTypeId\"}");

        visibleAsWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithTypeIdVisibleAndMissingValue() throws IOException {
        AsWrapperTypeDeserializer visibleAsWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
                objectMapper.getTypeFactory().constructType(String.class), typeIdResolver, "_type", true, String.class);

        JsonParser jsonParser = createJsonParser("{\"_type\":\"valueTypeId\"}");

        visibleAsWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, context);
    }

    private JsonParser createJsonParser(String jsonString) throws IOException {
        TokenBuffer tokenBuffer = new TokenBuffer(null, false);
        tokenBuffer.writeRawValue(jsonString);
        return tokenBuffer.asParser();
    }

    private BeanDescription constructBeanDescription() {
        return new BeanDescription(String.class);
    }
}