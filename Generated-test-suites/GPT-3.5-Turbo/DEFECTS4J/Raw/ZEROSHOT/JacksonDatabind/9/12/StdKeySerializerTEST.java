package com.fasterxml.jackson.databind.ser.std;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;


public class StdKeySerializerTest {

    private StdKeySerializer serializer = new StdKeySerializer();

    @Test
    public void testSerialize_withDateValue_shouldSerializeDateKey() throws IOException {
        // Arrange
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        Date date = new Date();

        // Act
        serializer.serialize(date, generator, provider);

        // Assert
        Assert.assertTrue(generator.fieldName.equals(date.toString()));
    }

    @Test
    public void testSerialize_withNonDateValue_shouldSerializeToString() throws IOException {
        // Arrange
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        String value = "test";

        // Act
        serializer.serialize(value, generator, provider);

        // Assert
        Assert.assertTrue(generator.fieldName.equals(value));
    }

    @Test
    public void testGetSchema_shouldReturnStringSchemaNode() throws JsonMappingException {
        // Arrange
        SerializerProviderStub provider = new SerializerProviderStub();
        Type typeHint = String.class;

        // Act
        JsonNode schema = serializer.getSchema(provider, typeHint);

        // Assert
        Assert.assertEquals("string", schema.getNodeType());
    }

    @Test
    public void testAcceptJsonFormatVisitor_shouldExpectStringFormat() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapperStub visitor = new JsonFormatVisitorWrapperStub();
        JavaType typeHint = JavaType.builder().build();

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        Assert.assertTrue(visitor.expectStringFormatCalled);
    }

    class JsonGeneratorStub extends JsonGenerator {
        public String fieldName;

        @Override
        public void writeFieldName(String name) throws IOException {
            fieldName = name;
        }
        // Implement other abstract methods of JsonGenerator
    }

    class SerializerProviderStub extends SerializerProvider {
        // Implement abstract methods of SerializerProvider
    }

    class JsonFormatVisitorWrapperStub extends JsonFormatVisitorWrapper {
        public boolean expectStringFormatCalled;

        @Override
        public void expectStringFormat(JavaType convertedType) throws JsonMappingException {
            expectStringFormatCalled = true;
        }
        // Implement other abstract methods of JsonFormatVisitorWrapper
    }
}