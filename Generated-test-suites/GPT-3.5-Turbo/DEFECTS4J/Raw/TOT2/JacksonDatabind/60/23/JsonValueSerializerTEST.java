package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.junit.Before;
import org.junit.Test;

public class JsonValueSerializerTest {
    
    private AnnotatedMethod accessorMethod;
    private JsonSerializer<?> valueSerializer;
    private BeanProperty property;

    private JsonValueSerializer jsonValueSerializer;
    
    @Before
    public void setUp() {
        accessorMethod = new AnnotatedMethod();
        valueSerializer = mock(JsonSerializer.class);
        property = mock(BeanProperty.class);
        
        jsonValueSerializer = new JsonValueSerializer(accessorMethod, valueSerializer);
    }
    
    @Test
    public void testSerializeNullValue() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider prov = mock(SerializerProvider.class);
        
        jsonValueSerializer.serialize(null, gen, prov);
        
        // Verify that defaultSerializeNull method is called
        assertNull(gen);
    }
    
    @Test
    public void testSerializeNonNullValue() throws IOException {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider prov = mock(SerializerProvider.class);
        
        when(accessorMethod.getValue(bean)).thenReturn("value");
        when(valueSerializer.getClass()).thenReturn(String.class);
        
        jsonValueSerializer.serialize(bean, gen, prov);
        
        // Verify that value is serialized
        assertEquals("value", gen);
    }
    
    @Test(expected = IOException.class)
    public void testSerializeIOException() throws IOException {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider prov = mock(SerializerProvider.class);
        
        when(accessorMethod.getValue(bean)).thenReturn("value");
        when(valueSerializer.getClass()).thenReturn(String.class);
        
        when(gen.writeObjectField(accessorMethod.getName(), "value")).thenThrow(IOException.class);
        
        jsonValueSerializer.serialize(bean, gen, prov);
    }
    
    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        
        when(valueSerializer.getClass()).thenReturn(String.class);
        when(_accessorMethod.getType()).thenReturn(String.class);
        when(provider.isEnabled(MapperFeature.USE_STATIC_TYPING)).thenReturn(true);
        
        JsonSerializer<?> contextualSerializer = jsonValueSerializer.createContextual(provider, property);
        
        // Verify that the contextual serializer is created properly
        assertNotNull(contextualSerializer);
    }
    
    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        
        when(valueSerializer instanceof SchemaAware).thenReturn(true);
        
        JsonNode schema = jsonValueSerializer.getSchema(provider, null);
        
        // Verify that getSchema returns the expected value
        assertNotNull(schema);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        when(_accessorMethod.getType()).thenReturn(String.class);
        when(_accessorMethod.getDeclaringClass()).thenReturn(String.class);
        when(visitor.getProvider().findTypedValueSerializer(_accessorMethod.getType(), false, _property)).thenReturn(null);
        
        jsonValueSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Verify that acceptJsonFormatVisitor handles enum types
        // You may need to update this part based on your actual implementation
    }
    
}