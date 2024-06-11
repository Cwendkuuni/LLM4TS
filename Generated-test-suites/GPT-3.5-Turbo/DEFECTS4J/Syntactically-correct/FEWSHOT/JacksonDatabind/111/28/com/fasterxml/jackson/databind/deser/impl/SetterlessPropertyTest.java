package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class SetterlessPropertyTest {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private final NullValueProvider nullValueProvider = mock(NullValueProvider.class);
    private final JsonDeserializer<?> jsonDeserializer = mock(JsonDeserializer.class);
    private final TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
    private final PropertyName propertyName = mock(PropertyName.class);
    private final Annotations annotations = mock(Annotations.class);
    private final JavaType javaType = mock(JavaType.class);
    private final DeserializationContext deserializationContext = mock(DeserializationContext.class);
    private final DeserializationConfig deserializationConfig = mock(DeserializationConfig.class);
    private final JsonParser jsonParser = mock(JsonParser.class);
    private final RuntimeException runtimeException = new RuntimeException();
    private final SetterlessProperty setterlessProperty = new SetterlessProperty(propertyName, javaType, typeDeserializer, annotations, objectMapper).withValueDeserializer(jsonDeserializer).withNullProvider(nullValueProvider);
    
    @Test
    public void testConstructor_withPropertyNameAndTypeAndDeserializerAndContextAnnotationsAndAnnotatedMethod() {
        assertEquals(propertyName, setterlessProperty.getFullName());
        assertEquals(javaType, setterlessProperty.getType());
        assertEquals(nullValueProvider, setterlessProperty.getNullValueProvider());
    }
    
    @Test
    public void testConstructor_withSetterlessPropertyAndDeserializerAndNullValueProvider() {
        SetterlessProperty newSetterlessProperty = new SetterlessProperty(setterlessProperty, jsonDeserializer, nullValueProvider);
        assertEquals(propertyName, newSetterlessProperty.getFullName());
        assertEquals(javaType, newSetterlessProperty.getType());
        assertEquals(jsonDeserializer, newSetterlessProperty.getValueDeserializer());
        assertEquals(nullValueProvider, newSetterlessProperty.getNullValueProvider());
    }
    
    @Test
    public void testConstructor_withSetterlessPropertyAndNewName() {
        SetterlessProperty newSetterlessProperty = new SetterlessProperty(setterlessProperty, propertyName);
        assertEquals(propertyName, newSetterlessProperty.getFullName());
        assertEquals(javaType, newSetterlessProperty.getType());
        assertEquals(jsonDeserializer, newSetterlessProperty.getValueDeserializer());
        assertEquals(nullValueProvider, newSetterlessProperty.getNullValueProvider());
    }
    
    @Test
    public void testWithNullProvider() {
        assertEquals(propertyName, setterlessProperty.withNullProvider(nullValueProvider).getFullName());
        assertEquals(javaType, setterlessProperty.withNullProvider(nullValueProvider).getType());
        assertEquals(jsonDeserializer, setterlessProperty.withNullProvider(nullValueProvider).getValueDeserializer());
        assertEquals(nullValueProvider, setterlessProperty.withNullProvider(nullValueProvider).getNullValueProvider());
    }
    
    @Test
    public void testFixAccess() {
        setterlessProperty.fixAccess(deserializationConfig);
    }
    
    @Test
    public void testGetAnnotation() {
        assertNull(setterlessProperty.getAnnotation(null));
    }
    
    @Test
    public void testGetMember() {
        assertNull(setterlessProperty.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        // TODO: Implement test cases for deserializeAndSet
        // Current implementation throws UnsupportedOperationException
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = new Object();
        assertEquals(instance, setterlessProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        setterlessProperty.set(new Object(), new Object());
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        assertEquals(instance, setterlessProperty.setAndReturn(instance, new Object()));
    }
    
    private void _throwAsIOE(JsonParser p, Exception e) throws IOException {
        if (e instanceof IOException) {
            throw (IOException) e;
        }
        if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        }
        while (e.getCause() != null) {
            e = e.getCause();
        }
        throw new IOException(e.getMessage(), p.getTokenLocation(), e);
    }
}