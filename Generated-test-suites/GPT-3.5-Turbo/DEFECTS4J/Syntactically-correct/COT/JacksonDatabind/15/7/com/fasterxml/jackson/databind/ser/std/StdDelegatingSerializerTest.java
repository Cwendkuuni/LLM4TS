
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {
    
    @Test
    public void testSerialize_NullValue_SerializeNull() throws IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        serializer.serialize(null, generator, provider);
        
        Mockito.verify(provider, Mockito.times(1)).defaultSerializeNull(generator);
    }
    
    @Test
    public void testSerialize_ValidValue_DelegateSerialization() throws IOException {
        Converter<Object, Object> converter = Mockito.mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Object value = new Object();
        Object convertedValue = new Object();
        
        Mockito.when(converter.convert(value)).thenReturn(convertedValue);
        
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        
        serializer._delegateSerializer = delegateSerializer;
        
        serializer.serialize(value, generator, provider);
        
        Mockito.verify(delegateSerializer, Mockito.times(1)).serialize(convertedValue, generator, provider);
    }
    
    @Test
    public void testSerializeWithType_ValidValue_DelegateSerializationWithType() throws IOException {
        Converter<Object, Object> converter = Mockito.mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Object value = new Object();
        Object convertedValue = new Object();
        
        Mockito.when(converter.convert(value)).thenReturn(convertedValue);
        
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);
        
        serializer._delegateSerializer = delegateSerializer;
        
        serializer.serializeWithType(value, generator, provider, typeSerializer);
        
        Mockito.verify(delegateSerializer, Mockito.times(1)).serializeWithType(convertedValue, generator, provider, typeSerializer);
    }
    
    @Test
    public void testIsEmpty_ValidValue_DelegateIsEmpty() throws IOException {
        Converter<Object, Object> converter = Mockito.mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Object value = new Object();
        Object convertedValue = new Object();
        
        Mockito.when(converter.convert(value)).thenReturn(convertedValue);
        
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        
        serializer._delegateSerializer = delegateSerializer;
        
        Mockito.when(delegateSerializer.isEmpty(provider, convertedValue)).thenReturn(true);
        
        boolean isEmpty = serializer.isEmpty(provider, value);
        
        Assert.assertTrue(isEmpty);
    }
    
    @Test
    public void testGetSchema_SchemaAwareDelegate_ReturnsDelegateSchema() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);
        
        JsonSerializer<?> delegateSerializer = Mockito.mock(JsonSerializer.class);
        JsonNode delegateSchema = Mockito.mock(JsonNode.class);
        
        serializer._delegateSerializer = delegateSerializer;
        
        Mockito.when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        Mockito.when(((SchemaAware) delegateSerializer).getSchema(provider, typeHint)).thenReturn(delegateSchema);
        
        JsonNode schema = serializer.getSchema(provider, typeHint);
        
        Assert.assertEquals(delegateSchema, schema);
    }
    
    @Test
    public void testGetSchema_NoSchemaAwareDelegate_CallsSuperGetSchema() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);
        
        JsonNode superSchema = Mockito.mock(JsonNode.class);
        
        serializer._delegateSerializer = null;
        
        JsonNode schema = serializer.getSchema(provider, typeHint);
        
        Assert.assertEquals(superSchema, schema);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_DelegateAcceptsVisitor_CallsDelegateAcceptJsonFormatVisitor() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        
        JsonSerializer<?> delegateSerializer = Mockito.mock(JsonSerializer.class);
        
        serializer._delegateSerializer = delegateSerializer;
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        Mockito.verify(delegateSerializer, Mockito.times(1)).acceptJsonFormatVisitor(visitor, typeHint);
    }
    
    @Test
    public void testConvertValue_ValidValue_ConvertsValue() {
        Converter<Object, Object> converter = Mockito.mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Object value = new Object();
        Object convertedValue = new Object();
        
        Mockito.when(converter.convert(value)).thenReturn(convertedValue);
        
        Object result = serializer.convertValue(value);
        
        Assert.assertEquals(convertedValue, result);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testWithDelegate_NotOverridden_IllegalStateException() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        
        serializer.withDelegate(null, null, null);
    }
    
    @Test
    public void testResolve_DelegateIsResolvable_ResolvesDelegate() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        
        serializer._delegateSerializer = delegateSerializer;
        
        serializer.resolve(provider);
        
        Mockito.verify(delegateSerializer, Mockito.times(1)).resolve(provider);
    }
    
    @Test
    public void testCreateContextual_NoDelegate_CallsFindValueSerializer() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(Mockito.mock(Converter.class));
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        
        JavaType delegateType = Mockito.mock(JavaType.class);
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        
        Mockito.when(serializer._delegateSerializer).thenReturn(null);
        Mockito.when(serializer._delegateType).thenReturn(delegateType);
        Mockito.when(serializer._converter.getOutputType(provider.getTypeFactory())).thenReturn(delegateType);
        Mockito.when(provider.findValueSerializer(delegateType)).thenReturn(delegateSerializer);
        
        JsonSerializer<?> result = serializer.createContextual(provider, property);
        
        Mockito.verify(provider, Mockito.times(1)).findValueSerializer(delegateType);
        Assert.assertEquals(delegateSerializer, result);
    }
    
    @Test
    public void testCreateContextual_DelegateImplementsContextualSerializer_CallsDelegateContextualSerializer() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(Mockito.mock(Converter.class));
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        
        JavaType delegateType = Mockito.mock(JavaType.class);
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        
        Mockito.when(serializer._delegateSerializer).thenReturn(delegateSerializer);
        Mockito.when(serializer._delegateType).thenReturn(delegateType);
        Mockito.when(serializer.withDelegate(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(serializer);
        Mockito.when(delegateSerializer instanceof ContextualSerializer).thenReturn(true);
        Mockito.when(provider.handleSecondaryContextualization(delegateSerializer, property)).thenReturn(delegateSerializer);
        
        JsonSerializer<?> result = serializer.createContextual(provider, property);
        
        Mockito.verify(provider, Mockito.times(1)).handleSecondaryContextualization(delegateSerializer, property);
        Assert.assertEquals(serializer, result);
    }
    
    @Test
    public void testCreateContextual_DelegateNotImplementsContextualSerializer_CreatesNewWithDelegate() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(Mockito.mock(Converter.class));
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        
        JavaType delegateType = Mockito.mock(JavaType.class);
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        
        Mockito.when(serializer._delegateSerializer).thenReturn(delegateSerializer);
        Mockito.when(serializer._delegateType).thenReturn(delegateType);
        Mockito.when(serializer.withDelegate(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(serializer);
        Mockito.when(delegateSerializer instanceof ContextualSerializer).thenReturn(false);
        
        JsonSerializer<?> result = serializer.createContextual(provider, property);
        
        Mockito.verify(serializer, Mockito.times(1)).withDelegate(Mockito.any(), Mockito.any(), Mockito.any());
        Assert.assertEquals(serializer, result);
    }
}
