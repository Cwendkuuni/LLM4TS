package com.example.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void TestAsPropertyTypeDeserializer() {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
        assertNotNull(deserializer);
    }

    @Test
    public void TestAsPropertyTypeDeserializerConstructor1() {
        JavaType baseType = null;
        TypeIdResolver idResolver = null;
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
    }

    @Test
    public void TestAsPropertyTypeDeserializerConstructor2() {
        JavaType baseType = null;
        TypeIdResolver idResolver = null;
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;
        As inclusion = As.PROPERTY;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        assertEquals(inclusion, deserializer.getTypeInclusion());
    }

    @Test
    public void TestForProperty() {
        JavaType baseType = null;
        TypeIdResolver idResolver = null;
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        BeanProperty property = new BeanProperty("propertyName", null, null);
        AsPropertyTypeDeserializer deserializer2 = deserializer.forProperty(property);
        assertEquals(deserializer, deserializer2);
    }

    @Test
    public void TestgetTypeInclusion() {
        JavaType baseType = null;
        TypeIdResolver idResolver = null;
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNullArguments() {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
    }

    @Test
    public void TestEmptyJSONInput() {
        String json = "";
        JsonParser parser = new JsonFactory().createParser(json);
        DeserializationContext ctxt = new StandardDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
        try {
            deserializer.deserializeTypedFromObject(parser, ctxt);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test(expected = JsonMappingException.class)
    public void TestInvalidJSONInput() {
        String json = "{\"invalid\":";
        JsonParser parser = new JsonFactory().createParser(json);
        DeserializationContext ctxt = new StandardDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
        try {
            deserializer.deserializeTypedFromObject(parser, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test(expected = JsonMappingException.class)
    public void TestUnrecognizedPropertyName() {
        String json = "{\"unrecognizedPropertyName\": 42}";
        JsonParser parser = new JsonFactory().createParser(json);
        DeserializationContext ctxt = new StandardDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
        try {
            deserializer.deserializeTypedFromObject(parser, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test(expected = JsonMappingException.class)
    public void TestMalformedJSONInput() {
        String json = "{\"propertyName\": 42";
        JsonParser parser = new JsonFactory().createParser(json);
        DeserializationContext ctxt = new StandardDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
        try {
            deserializer.deserializeTypedFromObject(parser, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test(expected = JsonMappingException.class)
    public void TestNonMatchingTypeNames() {
        String json = "{\"typePropertyName\": \"MismatchedType\"}";
        JsonParser parser = new JsonFactory().createParser(json);
        DeserializationContext ctxt = new StandardDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, "typePropertyName", false, null);
        try {
            deserializer.deserializeTypedFromObject(parser, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void TestCustomDeserializers() {
        String json = "{\"typePropertyName\": \"CustomType\"}";
        JsonDeserializer<CustomType> customDeserializer = new CustomTypeDeserializer();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext ctxt = new StandardDeserializationContext();
        ctxt.findConverters().addConverter(new TypeIdResolverBase() {
            @Override
            public TypeDeserializer getTypeDeserializer(DeserializationContext ctxt, BeanProperty property, TypeId id) throws IOException {
                return customDeserializer;
            }
        });
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, "typePropertyName", false, CustomType.class);
        Object deserializedObject = deserializer.deserializeTypedFromObject(parser, ctxt);
        assertTrue(deserializedObject instanceof CustomType);
    }
}