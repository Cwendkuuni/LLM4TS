
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor1() {
        ObjectIdReader mockObjectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, metadata);

        assertEquals(mockObjectIdReader.propertyName, property.getName());
        assertEquals(mockObjectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(mockObjectIdReader.getDeserializer(), property.getValueDeserializer());
        assertEquals(mockObjectIdReader, property._objectIdReader);
    }
    
    @Test
    public void testConstructor2() {
        ObjectIdReader mockObjectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, metadata);

        JsonDeserializer<?> mockDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty newProperty = property.withValueDeserializer(mockDeserializer);

        assertEquals(mockObjectIdReader.propertyName, newProperty.getName());
        assertEquals(mockObjectIdReader.getIdType(), newProperty.getType());
        assertEquals(metadata, newProperty.getMetadata());
        assertEquals(mockDeserializer, newProperty.getValueDeserializer());
        assertEquals(mockObjectIdReader, newProperty._objectIdReader);
    }
    
    @Test
    public void testConstructor3() {
        ObjectIdReader mockObjectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, metadata);

        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty newProperty = property.withName(newName);

        assertEquals(newName, newProperty.getName());
        assertEquals(mockObjectIdReader.getIdType(), newProperty.getType());
        assertEquals(metadata, newProperty.getMetadata());
        assertEquals(mockObjectIdReader.getDeserializer(), newProperty.getValueDeserializer());
        assertEquals(mockObjectIdReader, newProperty._objectIdReader);
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DefaultDeserializationContext.class);
        Object instance = new Object();
        ObjectMapper mapper = new ObjectMapper();
        
        ObjectIdReader mockObjectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, metadata);

        property.deserializeAndSet(mockParser, mockContext, instance);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DefaultDeserializationContext.class);
        Object instance = new Object();
        ObjectMapper mapper = new ObjectMapper();
        
        ObjectIdReader mockObjectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, metadata);

        property.deserializeSetAndReturn(mockParser, mockContext, instance);
    }
    
    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        ObjectIdReader mockObjectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, metadata);

        property.set(instance, value);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        ObjectIdReader mockObjectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, metadata);

        property.setAndReturn(instance, value);
    }
    
    @Test
    public void testGetAnnotation() {
        ObjectIdReader mockObjectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, metadata);

        assertNull(property.getAnnotation(Annotation.class));
    }
    
    @Test
    public void testGetMember() {
        ObjectIdReader mockObjectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, metadata);

        assertNull(property.getMember());
    }
}
