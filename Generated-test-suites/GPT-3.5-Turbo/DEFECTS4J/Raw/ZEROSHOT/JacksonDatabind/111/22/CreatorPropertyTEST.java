package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class CreatorPropertyTest {

    @Test
    public void testFindInjectableValue() throws Exception {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object beanInstance = new Object();
        
        CreatorProperty property = new CreatorProperty(PropertyName.construct("name"), JavaType.defaultType(), null,
                TypeDeserializer.None, Mockito.mock(Annotations.class), null, 0, "valueId", PropertyMetadata.STD_REQUIRED);
        
        assertEquals("valueId", property.findInjectableValue(context, beanInstance));
    }
    
    @Test
    public void testInject() throws Exception {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object beanInstance = new Object();
        
        CreatorProperty property = new CreatorProperty(PropertyName.construct("name"), JavaType.defaultType(), null,
                TypeDeserializer.None, Mockito.mock(Annotations.class), null, 0, "valueId", PropertyMetadata.STD_REQUIRED);
        
        property.set(beanInstance, "value");
        property.inject(context, beanInstance);
        
        assertEquals("value", beanInstance);
    }
    
    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object beanInstance = new Object();
        
        CreatorProperty property = new CreatorProperty(PropertyName.construct("name"), JavaType.defaultType(), null,
                TypeDeserializer.None, Mockito.mock(Annotations.class), null, 0, "valueId", PropertyMetadata.STD_REQUIRED);
        
        JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
        property.setValueDeserializer(deserializer);
        
        assertNull(property.deserializeAndSet(parser, context, beanInstance));
        
        Mockito.verify(parser).deserialize(deserializer, context);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object beanInstance = new Object();
        
        CreatorProperty property = new CreatorProperty(PropertyName.construct("name"), JavaType.defaultType(), null,
                TypeDeserializer.None, Mockito.mock(Annotations.class), null, 0, "valueId", PropertyMetadata.STD_REQUIRED);
        
        JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
        property.setValueDeserializer(deserializer);
        
        assertNull(property.deserializeSetAndReturn(parser, context, beanInstance));
        
        Mockito.verify(parser).deserialize(deserializer, context);
    }
    
    @Test
    public void testSet() throws Exception {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object beanInstance = new Object();
        
        CreatorProperty property = new CreatorProperty(PropertyName.construct("name"), JavaType.defaultType(), null,
                TypeDeserializer.None, Mockito.mock(Annotations.class), null, 0, "valueId", PropertyMetadata.STD_REQUIRED);
        
        property.set(beanInstance, "value");
        
        assertEquals("value", beanInstance);
    }
    
    @Test
    public void testSetAndReturn() throws Exception {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object beanInstance = new Object();
        
        CreatorProperty property = new CreatorProperty(PropertyName.construct("name"), JavaType.defaultType(), null,
                TypeDeserializer.None, Mockito.mock(Annotations.class), null, 0, "valueId", PropertyMetadata.STD_REQUIRED);
        
        assertEquals("value", property.setAndReturn(beanInstance, "value"));
    }
}