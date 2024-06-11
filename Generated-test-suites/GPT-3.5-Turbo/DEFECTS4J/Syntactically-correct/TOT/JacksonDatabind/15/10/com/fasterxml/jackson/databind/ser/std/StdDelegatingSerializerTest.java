
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Type;

@RunWith(MockitoJUnitRunner.class)
public class StdDelegatingSerializerTest {

    @Mock
    private JsonGenerator jsonGeneratorMock;

    @Mock
    private SerializerProvider serializerProviderMock;

    @Mock
    private TypeSerializer typeSerializerMock;

    @Test
    public void testSerialize_NullValue_ShouldInvokeDefaultSerializeNull() throws IOException {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SampleConverter());
        serializer.serialize(null, jsonGeneratorMock, serializerProviderMock);

        // Assert
        Mockito.verify(serializerProviderMock, Mockito.times(1)).defaultSerializeNull(jsonGeneratorMock);
    }

    @Test
    public void testSerialize_NonNullValue_ShouldInvokeDelegateSerializer() throws IOException {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SampleConverter());
        serializer.setupDelegateSerializer(jsonGeneratorMock, serializerProviderMock);

        // Act
        serializer.serialize("test", jsonGeneratorMock, serializerProviderMock);

        // Assert
        Mockito.verify(serializer.delegateSerializer, Mockito.times(1)).serialize("test", jsonGeneratorMock, serializerProviderMock);
    }

    @Test
    public void testSerialize_NullValueAndDelegateSerializerIsNull_ShouldInvokeDefaultSerializeNull() throws IOException {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SampleConverter(), String.class, null);
        serializer.serialize(null, jsonGeneratorMock, serializerProviderMock);

        // Assert
        Mockito.verify(serializerProviderMock, Mockito.times(1)).defaultSerializeNull(jsonGeneratorMock);
    }

    @Test
    public void testSerialize_WithType_NullValue_ShouldInvokeDefaultSerializeNull() throws IOException {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SampleConverter());
        serializer.serializeWithType(null, jsonGeneratorMock, serializerProviderMock, typeSerializerMock);

        // Assert
        Mockito.verify(serializerProviderMock, Mockito.times(1)).defaultSerializeNull(jsonGeneratorMock);
    }

    @Test
    public void testSerialize_WithType_NonNullValue_ShouldInvokeDelegateSerializer() throws IOException {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SampleConverter());
        serializer.setupDelegateSerializer(jsonGeneratorMock, serializerProviderMock);

        // Act
        serializer.serializeWithType("test", jsonGeneratorMock, serializerProviderMock, typeSerializerMock);

        // Assert
        Mockito.verify(serializer.delegateSerializer, Mockito.times(1)).serializeWithType("test", jsonGeneratorMock, serializerProviderMock, typeSerializerMock);
    }

    @Test
    public void testIsEmpty_NullValue_ShouldDelegateToDelegateSerializer() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SampleConverter());
        serializer.setupDelegateSerializer(jsonGeneratorMock, serializerProviderMock);

        // Act
        serializer.isEmpty(serializerProviderMock, null);

        // Assert
        Mockito.verify(serializer.delegateSerializer, Mockito.times(1)).isEmpty(serializerProviderMock, null);
    }

    @Test
    public void testIsEmpty_NonNullValue_ShouldDelegateToDelegateSerializer() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SampleConverter());
        serializer.setupDelegateSerializer(jsonGeneratorMock, serializerProviderMock);

        // Act
        serializer.isEmpty(serializerProviderMock, "test");

        // Assert
        Mockito.verify(serializer.delegateSerializer, Mockito.times(1)).isEmpty(serializerProviderMock, "test");
    }

    @Test
    public void testGetSchema_WhenDelegateSerializerIsSchemaAware_ShouldInvokeGetSchemaMethodOfDelegateSerializer() throws JsonMappingException {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SampleConverter());
        SchemaAware schemaAwareDelegateSerializerMock = Mockito.mock(SchemaAware.class);
        Mockito.when(serializer.delegateSerializer).thenReturn(schemaAwareDelegateSerializerMock);

        // Act
        serializer.getSchema(serializerProviderMock, String.class);

        // Assert
        Mockito.verify(schemaAwareDelegateSerializerMock, Mockito.times(1)).getSchema(serializerProviderMock, String.class);
    }

    @Test
    public void testGetSchema_WhenDelegateSerializerIsNotSchemaAware_ShouldInvokeGetSchemaMethodOfSuperClass() throws JsonMappingException {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SampleConverter());

        // Act
        serializer.getSchema(serializerProviderMock, String.class);

        // Assert
        Mockito.verify(serializerProviderMock, Mockito.times(1)).getClass();
    }

    @Test
    public void testGetSchema_WithOptionalFlag_WhenDelegateSerializerIsSchemaAware_ShouldInvokeGetSchemaMethodOfDelegateSerializer() throws JsonMappingException {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SampleConverter());
        SchemaAware schemaAwareDelegateSerializerMock = Mockito.mock(SchemaAware.class);
        Mockito.when(serializer.delegateSerializer).thenReturn(schemaAwareDelegateSerializerMock);

        // Act
        serializer.getSchema(serializerProviderMock, String.class, true);

        // Assert
        Mockito.verify(schemaAwareDelegateSerializerMock, Mockito.times(1)).getSchema(serializerProviderMock, String.class, true);
    }

    @Test
    public void testGetSchema_WithOptionalFlag_WhenDelegateSerializerIsNotSchemaAware_ShouldInvokeGetSchemaMethodOfSuperClass() throws JsonMappingException {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SampleConverter());

        // Act
        serializer.getSchema(serializerProviderMock, String.class, true);

        // Assert
        Mockito.verify(serializerProviderMock, Mockito.times(1)).getClass();
    }

    @Test
    public void testAcceptJsonFormatVisitor_WhenDelegateSerializerIsJsonFormatVisitable_ShouldInvokeAcceptJsonFormatVisitorOfDelegateSerializer() throws JsonMappingException {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SampleConverter());
        JsonFormatVisitorWrapper visitorWrapperMock = Mockito.mock(JsonFormatVisitorWrapper.class);
        Mockito.when(serializer.delegateSerializer).thenReturn((JsonFormatVisitable) serializer);
        JavaType typeHintMock = Mockito.mock(JavaType.class);

        // Act
        serializer.acceptJsonFormatVisitor(visitorWrapperMock, typeHintMock);

        // Assert
        Mockito.verify(serializer, Mockito.times(1)).acceptJsonFormatVisitor(visitorWrapperMock, typeHintMock);
    }

    @Test
    public void testAcceptJsonFormatVisitor_WhenDelegateSerializerIsNotJsonFormatVisitable_ShouldDoNothing() throws JsonMappingException {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new SampleConverter());
        JsonFormatVisitorWrapper visitorWrapperMock = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHintMock = Mockito.mock(JavaType.class);

        // Act
        serializer.acceptJsonFormatVisitor(visitorWrapperMock, typeHintMock);

        // Assert
        Mockito.verify(serializer.delegateSerializer, Mockito.never()).acceptJsonFormatVisitor(visitorWrapperMock, typeHintMock);
    }

    private static class SampleConverter implements Converter<Object, Object> {
        @Override
        public Object convert(Object value) {
            return value;
        }
    }
}
