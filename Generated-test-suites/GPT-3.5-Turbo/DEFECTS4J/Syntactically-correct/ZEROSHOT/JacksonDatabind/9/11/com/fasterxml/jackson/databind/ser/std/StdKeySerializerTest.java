package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeySerializerTest {
    private StdKeySerializer serializer;
    private SerializerProvider provider;
    private JsonGenerator jgen;

    @Before
    public void setUp() throws Exception {
        serializer = new StdKeySerializer();
        provider = new SerializerProvider();
        jgen = new JsonGenerator();
    }

    @Test
    public void testSerialize_withDate_shouldCallDefaultSerializeDateKey() throws IOException {
        // Arrange
        Date value = new Date();

        // Act
        serializer.serialize(value, jgen, provider);

        // Assert
        assertTrue(jgen.calledDefaultSerializeDateKey);
        assertNull(jgen.fieldName);
    }

    @Test
    public void testSerialize_withNonDate_shouldWriteFieldNameWithStringValue() throws IOException {
        // Arrange
        Object value = new Object();

        // Act
        serializer.serialize(value, jgen, provider);

        // Assert
        assertFalse(jgen.calledDefaultSerializeDateKey);
        assertEquals(value.toString(), jgen.fieldName);
    }
    
    @Test
    public void testGetSchema_shouldReturnSchemaNodeOfStringType() throws JsonMappingException {
        // Arrange
        Type typeHint = Object.class;

        // Act
        JsonNode schema = serializer.getSchema(provider, typeHint);

        // Assert
        assertEquals("string", schema.getNodeType());
    }

    @Test
    public void testAcceptJsonFormatVisitor_shouldExpectStringFormat() throws JsonMappingException {
        // Arrange
        JavaType typeHint = new JavaType();

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        assertEquals("string", visitor.getVisitedFormat());
    }
}

class JsonGenerator {
    public boolean calledDefaultSerializeDateKey;
    public String fieldName;

    public void defaultSerializeDateKey(Date value, JsonGenerator jgen) {
        calledDefaultSerializeDateKey = true;
    }

    public void writeFieldName(String name) {
        fieldName = name;
    }
}

class SerializerProvider {
    public JsonNode createSchemaNode(String type) {
        return new JsonNode(type);
    }
}

class JavaType {
    
}

class JsonNode {
    private String type;

    public JsonNode(String type) {
        this.type = type;
    }

    public String getNodeType() {
        return type;
    }
}

class JsonFormatVisitorWrapper {
    private String format;

    public void expectStringFormat(JavaType typeHint) {
        format = "string";
    }

    public String getVisitedFormat() {
        return format;
    }
}