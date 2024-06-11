package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AsPropertyTypeDeserializerTest {

    private AsPropertyTypeDeserializer asPropertyTypeDeserializer;

    @Before
    public void setUp() {
        JavaType bt = new SimpleType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new SimpleType();
        As inclusion = As.PROPERTY;

        asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        JsonParser jsonParser = new JsonFactory().createParser("{\"field1\":\"value1\",\"type\":\"type1\"}");
        DeserializationContext deserializationContext = new ObjectMapper().getDeserializationContext();

        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);

        Assert.assertNotNull(result);
        Assert.assertEquals("value1", result.getField1());
        Assert.assertEquals("type", asPropertyTypeDeserializer.getTypeInclusion().name());
    }

    @Test
    public void testDeserializeTypedForId() throws IOException {
        JsonParser jsonParser = new JsonFactory().createParser("{\"field1\":\"value1\",\"type\":\"type1\"}");
        DeserializationContext deserializationContext = new ObjectMapper().getDeserializationContext();
        TokenBuffer tb = new TokenBuffer(jsonParser, deserializationContext);

        Object result = asPropertyTypeDeserializer.deserializeTypedForId(jsonParser, deserializationContext, tb);

        Assert.assertNotNull(result);
        Assert.assertEquals("value1", result.getField1());
        Assert.assertEquals("type", asPropertyTypeDeserializer.getTypeInclusion().name());
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws IOException {
        JsonParser jsonParser = new JsonFactory().createParser("{\"field1\":\"value1\",\"type\":\"type1\"}");
        DeserializationContext deserializationContext = new ObjectMapper().getDeserializationContext();
        TokenBuffer tb = new TokenBuffer(jsonParser, deserializationContext);

        Object result = asPropertyTypeDeserializer.deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tb);

        Assert.assertNotNull(result);
        Assert.assertEquals("value1", result.getField1());
        Assert.assertEquals("type", asPropertyTypeDeserializer.getTypeInclusion().name());
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        JsonParser jsonParser = new JsonFactory().createParser("{\"field1\":\"value1\",\"type\":\"type1\"}");
        DeserializationContext deserializationContext = new ObjectMapper().getDeserializationContext();

        Object result = asPropertyTypeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext);

        Assert.assertNotNull(result);
        Assert.assertEquals("value1", result.getField1());
        Assert.assertEquals("type", asPropertyTypeDeserializer.getTypeInclusion().name());
    }

    private static class SimpleType {
        private String field1;

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }
    }
}