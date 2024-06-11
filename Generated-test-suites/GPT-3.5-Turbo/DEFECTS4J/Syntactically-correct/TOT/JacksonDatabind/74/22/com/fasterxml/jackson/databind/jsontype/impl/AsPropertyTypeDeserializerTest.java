package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        // Arrange
        JavaType bt = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "type";
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;
        AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(bt, idRes,
                typePropertyName, typeIdVisible, defaultImpl);
        JsonParser p = null;
        DeserializationContext ctxt = null;

        // Act
        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(p, ctxt);

        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedForId() throws IOException {
        // Arrange
        JavaType bt = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "type";
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;
        AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(bt, idRes,
                typePropertyName, typeIdVisible, defaultImpl);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        TokenBuffer tb = null;

        // Act
        Object result = asPropertyTypeDeserializer.deserializeTypedForId(p, ctxt, tb);

        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws IOException {
        // Arrange
        JavaType bt = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "type";
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;
        AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(bt, idRes,
                typePropertyName, typeIdVisible, defaultImpl);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        TokenBuffer tb = null;

        // Act
        Object result = asPropertyTypeDeserializer.deserializeTypedUsingDefaultImpl(p, ctxt, tb);

        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        // Arrange
        JavaType bt = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "type";
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;
        AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(bt, idRes,
                typePropertyName, typeIdVisible, defaultImpl);
        JsonParser p = null;
        DeserializationContext ctxt = null;

        // Act
        Object result = asPropertyTypeDeserializer.deserializeTypedFromAny(p, ctxt);

        // Assert
        assertNull(result);
    }

}