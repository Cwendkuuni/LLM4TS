import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {
    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        ObjectIdValueProperty prop = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertEquals(objectIdReader.propertyName, prop.getName());
        assertEquals(objectIdReader.getIdType(), prop.getType());
        assertEquals(metadata, prop.getMetadata());
    }
    
    @Test
    public void testCopyConstructorWithDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        JsonDeserializer<?> deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty prop = new ObjectIdValueProperty(src, deserializer);
        
        assertEquals(src.getName(), prop.getName());
        assertSame(deserializer, prop.getValueDeserializer());
    }
    
    @Test
    public void testCopyConstructorWithNewName() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        PropertyName newName = new PropertyName("newName");
        
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty prop = new ObjectIdValueProperty(src, newName);
        
        assertEquals(newName, prop.getName());
    }
    
    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        PropertyName newName = new PropertyName("newName");
        
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty prop = src.withName(newName);
        
        assertEquals(newName, prop.getName());
    }
    
    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        JsonDeserializer<?> deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty prop = src.withValueDeserializer(deserializer);
        
        assertSame(deserializer, prop.getValueDeserializer());
    }
    
    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        
        ObjectIdValueProperty prop = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertNull(prop.getAnnotation(Test.class));
    }
    
    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        
        ObjectIdValueProperty prop = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertNull(prop.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        JsonDeserializer<?> deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        
        ObjectIdValueProperty prop = new ObjectIdValueProperty(objectIdReader, metadata);
        
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        
        prop.deserializeAndSet(p, ctxt, instance);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        JsonDeserializer<?> deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        
        ObjectIdValueProperty prop = new ObjectIdValueProperty(objectIdReader, metadata);
        
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        
        Object result = prop.deserializeSetAndReturn(p, ctxt, instance);
        
        assertNull(result);
    }
    
    @Test
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        JsonDeserializer<?> deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        
        ObjectIdValueProperty prop = new ObjectIdValueProperty(objectIdReader, metadata);
        
        Object instance = new Object();
        Object value = new Object();
        
        prop.set(instance, value);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        JsonDeserializer<?> deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        
        ObjectIdValueProperty prop = new ObjectIdValueProperty(objectIdReader, metadata);
        
        Object instance = new Object();
        Object value = new Object();
        
        Object result = prop.setAndReturn(instance, value);
        
        assertNull(result);
    }
}