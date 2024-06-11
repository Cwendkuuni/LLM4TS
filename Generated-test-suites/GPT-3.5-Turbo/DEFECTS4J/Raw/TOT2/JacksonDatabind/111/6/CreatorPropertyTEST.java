package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.deser.CreatorProperty;

public class CreatorPropertyTest {

    @Test
    public void testConstructor() {
        PropertyName name = new PropertyName("testName");
        JavaType type = new JavaType();
        PropertyName wrapperName = new PropertyName("wrapperName");
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 1;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = new PropertyMetadata();

        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);

        assertEquals(name, creatorProperty.getName());
        assertEquals(type, creatorProperty.getType());
        assertEquals(wrapperName, creatorProperty.getWrapperName());
        assertEquals(typeDeser, creatorProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, creatorProperty.getContextAnnotations());
        assertEquals(param, creatorProperty.getMember());
        assertEquals(index, creatorProperty.getCreatorIndex());
        assertEquals(injectableValueId, creatorProperty.getInjectableValueId());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        CreatorProperty creatorProperty = new CreatorProperty();
        JsonParser jsonParser = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        try {
            creatorProperty.deserializeAndSet(jsonParser, ctxt, instance);
            fail("Expected InvalidDefinitionException to be thrown");
        } catch (InvalidDefinitionException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        CreatorProperty creatorProperty = new CreatorProperty();
        JsonParser jsonParser = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        try {
            creatorProperty.deserializeSetAndReturn(jsonParser, ctxt, instance);
            fail("Expected InvalidDefinitionException to be thrown");
        } catch (InvalidDefinitionException e) {
            // Expected
        }
    }

    @Test
    public void testSet() throws IOException {
        CreatorProperty creatorProperty = new CreatorProperty();
        Object instance = new Object();
        Object value = new Object();

        try {
            creatorProperty.set(instance, value);
            fail("Expected InvalidDefinitionException to be thrown");
        } catch (InvalidDefinitionException e) {
            // Expected
        }
    }

    @Test
    public void testSetAndReturn() throws IOException {
        CreatorProperty creatorProperty = new CreatorProperty();
        Object instance = new Object();
        Object value = new Object();

        try {
            creatorProperty.setAndReturn(instance, value);
            fail("Expected InvalidDefinitionException to be thrown");
        } catch (InvalidDefinitionException e) {
            // Expected
        }
    }

}