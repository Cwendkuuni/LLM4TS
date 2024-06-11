package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

    private ObjectNode objectNode;
    private JsonParser mockJsonParser;
    private DeserializationContext mockContext;
    private TypeDeserializer deserializer;
    private TypeIdResolver typeIdResolver;

    @Before
    public void setup() throws Exception {
        mockJsonParser = mock(JsonParser.class);
        mockContext = mock(DeserializationContext.class);
        objectNode = new ObjectMapper().createObjectNode();
        typeIdResolver = mock(TypeIdResolver.class);

        deserializer = new AsWrapperTypeDeserializer(new Jdk8ClassKey("java.lang.String"), typeIdResolver, "", false, String.class);
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        when(mockJsonParser.nextToken()).thenReturn(START_OBJECT, FIELD_NAME, "typeId", START_OBJECT, FIELD_NAME, "_class", VALUE_STRING, END_OBJECT, END_OBJECT);
        when(typeIdResolver.resolveTypeId(mockContext, "typeId")).thenReturn(new TypeId("com.test.Type"));
        Object value = deserializer.deserializeTypedFromObject(mockJsonParser, mockContext);
        assertEquals(new TypeIdentifier("com.test.Type"), deserializer.getTypeId());
        assertEquals(ValueInsideTypeId.class, deserializer.getValueInsideTypeId().getType());
        assertEquals(String.class, deserializer.getContentClass());
        assertEquals(new TypeReference<Object>() {}.getType(), value.getClass());
        assertEquals("com.test.Type", value.getClass().getCanonicalName());
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        when(mockJsonParser.nextToken()).thenReturn(START_ARRAY, VALUE_NULL, END_ARRAY);
        Object value = deserializer.deserializeTypedFromArray(mockJsonParser, mockContext);
        assertNull(value);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        when(mockJsonParser.nextToken()).thenReturn(VALUE_STRING, "test");
        Object value = deserializer.deserializeTypedFromScalar(mockJsonParser, mockContext);
        assertEquals("test", value);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        when(mockJsonParser.nextToken()).thenReturn(START_OBJECT, FIELD_NAME, "typeId", START_OBJECT, FIELD_NAME, "_class", VALUE_STRING, END_OBJECT, END_OBJECT);
        when(typeIdResolver.resolveTypeId(mockContext, "typeId")).thenReturn(new TypeId("com.test.Type"));

        ObjectNode jsonNode = new ObjectMapper().readTree("{\"typeId\": {\"_class\": \"com.test.Type\"}}");
        Object value = deserializer.deserializeTypedFromAny(jsonNode.traverse(), mockContext);
        assertEquals(new TypeIdentifier("com.test.Type"), deserializer.getTypeId());
        assertEquals(ValueInsideTypeId.class, deserializer.getValueInsideTypeId().getType());
        assertEquals(String.class, deserializer.getContentClass());
        assertEquals(new TypeReference<Object>() {}.getType(), value.getClass());
        assertEquals("com.test.Type", value.getClass().getCanonicalName());
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithInvalidJsonObject() throws IOException {
        when(mockJsonParser.nextToken()).thenReturn(START_ARRAY, VALUE_NULL, END_ARRAY);
        deserializer.deserializeTypedFromObject(mockJsonParser, mockContext);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithInvalidJsonString() throws IOException {
        when(mockJsonParser.nextToken()).thenReturn(START_OBJECT, FIELD_NAME, "typeId", START_OBJECT, FIELD_NAME, "_class", VALUE_NUMBER, END_OBJECT, END_OBJECT);
        deserializer.deserializeTypedFromObject(mockJsonParser, mockContext);
    }
}