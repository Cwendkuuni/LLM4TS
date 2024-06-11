package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotationMap;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;

public class ObjectIdValuePropertyTest
{
    private ObjectIdReader objectIdReader;
    
    private PropertyMetadata propertyMetadata;
    
    private JsonDeserializer<?> jsonDeserializer;
    
    private ObjectIdValueProperty objectIdValueProperty;
    
    private JsonParser jsonParser;
    
    private DeserializationContext context;
    
    private Object instance;
    
    @Before
    public void setUp() throws Exception
    {
        objectIdReader = mock(ObjectIdReader.class);
        propertyMetadata = mock(PropertyMetadata.class);
        jsonDeserializer = mock(JsonDeserializer.class);
        jsonParser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);
        instance = mock(Object.class);
        
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.getDeserializer()).thenReturn(jsonDeserializer);
        
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
    }
    
    @Test
    public void testWithNewName()
    {
        PropertyName newName = new PropertyName("newId");
        ObjectIdValueProperty newProperty = objectIdValueProperty.withName(newName);
        
        assertNotNull(newProperty);
        assertEquals(newName, newProperty.getFullName());
        assertEquals(String.class, newProperty.getType());
        assertEquals(propertyMetadata, newProperty.getMetadata());
        assertEquals(jsonDeserializer, newProperty.getValueDeserializer());
        assertEquals(objectIdReader, newProperty._objectIdReader);
    }
    
    @Test
    public void testWithValueDeserializer()
    {
        JsonDeserializer<?> newDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty newProperty = objectIdValueProperty.withValueDeserializer(newDeserializer);
        
        assertNotNull(newProperty);
        assertEquals(objectIdValueProperty.getFullName(), newProperty.getFullName());
        assertEquals(objectIdValueProperty.getType(), newProperty.getType());
        assertEquals(objectIdValueProperty.getMetadata(), newProperty.getMetadata());
        assertEquals(newDeserializer, newProperty.getValueDeserializer());
        assertEquals(objectIdReader, newProperty._objectIdReader);
    }
    
    @Test
    public void testGetAnnotation()
    {
        assertNull(objectIdValueProperty.getAnnotation(TestAnnotation.class));
    }
    
    @Test
    public void testGetMember()
    {
        assertNull(objectIdValueProperty.getMember());
    }
    
    @Test
    public void testDeserializeAndSet_NullId()
	throws IOException
    {
        when(jsonDeserializer.deserialize(jsonParser, context)).thenReturn(null);
        
        assertNull(objectIdValueProperty.deserializeSetAndReturn(jsonParser, context, instance));
    }
    
    @Test
    public void testDeserializeAndSet_IdNull()
	throws IOException
    {
        when(jsonDeserializer.deserialize(jsonParser, context)).thenReturn("null");
        
        assertNull(objectIdValueProperty.deserializeSetAndReturn(jsonParser, context, instance));
    }
    
    @Test
    public void testDeserializeAndSet_IdNotNull()
	throws IOException
    {
        String id = "123";
        
        when(jsonDeserializer.deserialize(jsonParser, context)).thenReturn(id);
        when(context.findObjectId(id, objectIdReader.generator, objectIdReader.resolver)).thenReturn(new ReadableObjectId());
        
        assertNotNull(objectIdValueProperty.deserializeSetAndReturn(jsonParser, context, instance));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn_NoIdProperty()
	throws IOException
    {
        objectIdValueProperty.setAndReturn(instance, "value");
    }
    
    @Test
    public void testSetAndReturn_WithIdProperty()
	throws IOException
    {
        SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
        when(objectIdReader.idProperty).thenReturn(idProperty);
        
        when(idProperty.setAndReturn(instance, "value")).thenReturn(instance);
        
        assertEquals(instance, objectIdValueProperty.setAndReturn(instance, "value"));
    }
}

interface TestAnnotation
{
    String value();
}