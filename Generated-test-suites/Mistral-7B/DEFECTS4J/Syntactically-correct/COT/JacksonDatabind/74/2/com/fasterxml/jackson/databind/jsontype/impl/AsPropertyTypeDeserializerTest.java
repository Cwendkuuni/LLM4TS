package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.fasterxml.jackson.core.JsonToken.*;

public class AsPropertyTypeDeserializerTest {

    private ObjectMapper objectMapper;
    private AsPropertyTypeDeserializer asPropertyTypeDeserializer;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructType(AsPropertyTypeDeserializer.class);
        TypeIdResolver typeIdResolver = new SimpleTypeIdResolver();
        String typePropertyName = "typePropertyName";
        As inclusion = As.PROPERTY;
        JavaType defaultImpl = objectMapper.getTypeFactory().constructType(String.class);
        asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, typePropertyName, false, defaultImpl, inclusion);
    }

    @Test
    public void testDeserializeTypedFromObjectWithValidTypeId() throws Exception {
        String json = "{\"typePropertyName\":\"StringTypeId\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, objectMapper.getDeserializationContext());
        Assert.assertEquals("StringTypeId", result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithEmptyObject() throws Exception {
        String json = "{}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, objectMapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithInvalidTypePropertyName() throws Exception {
        String json = "{\"invalidPropertyName\":\"StringTypeId\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        try {
            asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, objectMapper.getDeserializationContext());
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("Wrong type id property 'invalidPropertyName' for class [simple type, class java.lang.String]", e.getMessage());
        }
    }

    @Test
    public void testDeserializeTypedFromObjectWithArrayWrapper() throws Exception {
        String json = "[\"ArrayWrapper\"]";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, objectMapper.getDeserializationContext());
        Assert.assertEquals("[ArrayWrapper]", result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithNaturalValue() throws Exception {
        String json = "42";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, objectMapper.getDeserializationContext());
        Assert.assertEquals(42, result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithSTART_OBJECT() throws Exception {
        String json = "{\"typePropertyName\":{}}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, objectMapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithMissingProperty() throws Exception {
        String json = "{}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        try {
            asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, objectMapper.getDeserializationContext());
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("missing property 'typePropertyName' that is to contain type id"));
        }
    }

    @Test
    public void testDeserializeTypedFromObjectWithInvalidJson() throws Exception {
        String json = "\"invalid\"";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        try {
            asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, objectMapper.getDeserializationContext());
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("Can not construct instance of java.lang.String value (no String constructor, nor Serializer found for class java.lang.String, and no properties decorated with @JsonCreator, or @JSON-B INSTANCED, or @JSON-B DEFAULT, or @JSON-B DECLARED-CONSTRUCTORS found)", e.getMessage());
        }
    }
}