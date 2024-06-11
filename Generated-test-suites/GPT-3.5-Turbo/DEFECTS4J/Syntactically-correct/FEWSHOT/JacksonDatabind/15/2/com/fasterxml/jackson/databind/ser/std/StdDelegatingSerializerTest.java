package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ContextualSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.Type;

@SuppressWarnings("unchecked")
public class StdDelegatingSerializerTest {
    
    @Mock
    private Converter<Object, Object> converter;
    
    @Mock
    private JavaType delegateType;
    
    @Mock
    private JsonSerializer<Object> delegateSerializer;
    
    @Mock
    private JsonSerializer<Object> resolvedDelegateSerializer;
    
    private StdDelegatingSerializer delegatingSerializer;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        delegatingSerializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        delegatingSerializer._delegateSerializer = resolvedDelegateSerializer;
    }
    
    @Test
    public void testConstructor1() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        assertEquals(converter, serializer.getConverter());
        assertNull(serializer._delegateType);
        assertNull(serializer._delegateSerializer);
    }
    
    @Test
    public void testConstructor2() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(String.class, converter);
        assertEquals(converter, serializer.getConverter());
        assertNull(serializer._delegateType);
        assertNull(serializer._delegateSerializer);
    }
    
    @Test
    public void testConstructor3() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        assertEquals(converter, serializer.getConverter());
        assertEquals(delegateType, serializer._delegateType);
        assertEquals(delegateSerializer, serializer._delegateSerializer);
    }
    
    @Test
    public void testWithDelegate() {
        StdDelegatingSerializer serializer = delegatingSerializer.withDelegate(converter, delegateType, delegateSerializer);
        assertEquals(converter, serializer.getConverter());
        assertEquals(delegateType, serializer._delegateType);
        assertEquals(delegateSerializer, serializer._delegateSerializer);
    }
    
    @Test
    public void testResolve() throws IOException {
        SerializerProvider provider = mock(SerializerProvider.class);
        ResolvableSerializer resolvableSerializer = mock(ResolvableSerializer.class);
        when(resolvedDelegateSerializer instanceof ResolvableSerializer).thenReturn(true);
        when(resolvedDelegateSerializer).thenReturn(resolvableSerializer);
        
        delegatingSerializer.resolve(provider);
        
        verify(resolvableSerializer, times(1)).resolve(provider);
    }
    
    @Test
    public void testCreateContextual() throws IOException {
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        ContextualSerializer contextualSerializer = mock(ContextualSerializer.class);
        when(resolvedDelegateSerializer instanceof ContextualSerializer).thenReturn(true);
        when(resolvedDelegateSerializer).thenReturn(contextualSerializer);
        
        JsonSerializer<?> result = delegatingSerializer.createContextual(provider, property);
        
        verify(contextualSerializer, times(1)).handleSecondaryContextualization(resolvedDelegateSerializer, property);
        assertEquals(result, delegatingSerializer);
    }
    
    @Test
    public void testSerialize() throws IOException {
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        delegatingSerializer.serialize(value, gen, provider);
        
        verify(resolvedDelegateSerializer, times(1)).serialize(value, gen, provider);
    }
    
    @Test
    public void testSerializeWithType() throws IOException {
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        
        delegatingSerializer.serializeWithType(value, gen, provider, typeSer);
        
        verify(resolvedDelegateSerializer, times(1)).serializeWithType(value, gen, provider, typeSer);
    }
    
    @Test
    public void testIsEmpty1() throws IOException {
        Object value = new Object();
        
        delegatingSerializer.isEmpty(value);
        
        verify(resolvedDelegateSerializer, times(1)).isEmpty(value);
    }
    
    @Test
    public void testIsEmpty2() throws IOException {
        SerializerProvider prov = mock(SerializerProvider.class);
        Object value = new Object();
        
        delegatingSerializer.isEmpty(prov, value);
        
        verify(resolvedDelegateSerializer, times(1)).isEmpty(prov, value);
    }
    
    @Test
    public void testGetSchema1() throws IOException {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        
        when(resolvedDelegateSerializer instanceof SchemaAware).thenReturn(true);
        when(((SchemaAware) resolvedDelegateSerializer).getSchema(provider, typeHint)).thenReturn(null);
        
        delegatingSerializer.getSchema(provider, typeHint);
        
        verify((SchemaAware) resolvedDelegateSerializer, times(1)).getSchema(provider, typeHint);
    }
    
    @Test
    public void testGetSchema2() throws IOException {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        boolean isOptional = true;
        
        when(resolvedDelegateSerializer instanceof SchemaAware).thenReturn(true);
        when(((SchemaAware) resolvedDelegateSerializer).getSchema(provider, typeHint, isOptional)).thenReturn(null);
        
        delegatingSerializer.getSchema(provider, typeHint, isOptional);
        
        verify((SchemaAware) resolvedDelegateSerializer, times(1)).getSchema(provider, typeHint, isOptional);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws IOException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        delegatingSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(resolvedDelegateSerializer, times(1)).acceptJsonFormatVisitor(visitor, typeHint);
    }
    
    @Test
    public void testConvertValue() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        Object value = "test";
        
        Object result = delegatingSerializer.convertValue(value);
        
        assertEquals(value, result);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testWithDelegateInvalid() {
        StdDelegatingSerializer invalidSerializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer) {
            @Override
            protected StdDelegatingSerializer withDelegate(Converter<Object, ?> converter, JavaType delegateType,
                    JsonSerializer<?> delegateSerializer) {
                // Do nothing
                return this;
            }
        };
        
        invalidSerializer.withDelegate(converter, delegateType, delegateSerializer);
    }
}