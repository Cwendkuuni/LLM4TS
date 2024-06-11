package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        assertEquals(objectIdReader.propertyName, objectIdValueProperty._propName);
        assertEquals(objectIdReader.getIdType(), objectIdValueProperty._type);
        assertEquals(metadata, objectIdValueProperty._metadata);
        assertEquals(objectIdReader.getDeserializer(), objectIdValueProperty._valueDeserializer);
    }

    @Test
    public void testCopyConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(src, deser);
        assertEquals(src._propName, objectIdValueProperty._propName);
        assertEquals(src._type, objectIdValueProperty._type);
        assertEquals(src._metadata, objectIdValueProperty._metadata);
        assertEquals(deser, objectIdValueProperty._valueDeserializer);
    }

    @Deprecated
    @Test
    public void testDeprecatedCopyConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(src, newName);
        assertEquals(src._propName.getSimpleName(), objectIdValueProperty._propName.getSimpleName());
        assertEquals(src._type, objectIdValueProperty._type);
        assertEquals(src._metadata, objectIdValueProperty._metadata);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        Class<A> acls = A.class;
        assertNull(objectIdValueProperty.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        JsonParser jp = new ObjectMapper().getFactory().createParser("{}");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper().getFactory());
        Object instance = new Object();
        objectIdValueProperty.deserializeAndSet(jp, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        JsonParser jp = new ObjectMapper().getFactory().createParser("{}");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper().getFactory());
        Object instance = new Object();
        objectIdValueProperty.deserializeSetAndReturn(jp, ctxt, instance);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testSetAndReturnWithoutIdProperty() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        Object instance = new Object();
        objectIdValueProperty.setAndReturn(instance, new Object());
    }

    @Test
    public void testSetAndReturnWithIdProperty() throws IOException {
        ObjectIdValueProperty oidProp = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        Object instance = new Object();
        SettableBeanProperty idProp = new SettableBeanProperty();
        oidProp._objectIdReader.idProperty = idProp;
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{}");
        DeserializationContext ctxt = new DeserializationContext(mapper.getFactory());
        Object id = new Object();
        oidProp._valueDeserializer.deserialize(jp, ctxt);
        ReadableObjectId roid = ctxt.findObjectId(id, oidProp._objectIdReader.generator, oidProp._objectIdReader.resolver);
        roid.bindItem(instance);
        oidProp.setAndReturn(instance, id);
    }
    
    private static class ObjectIdReader {
    	String propertyName;
    	Class<?> idType;
    	PropertyMetadata metadata;
    	JsonDeserializer<?> deserializer;
    	SettableBeanProperty idProperty;
    	
		public Class<?> getIdType() {
			return idType;
		}
		public PropertyMetadata getMetadata() {
			return metadata;
		}
		public JsonDeserializer<?> getDeserializer() {
			return deserializer;
		}
	}
    
    private static class PropertyMetadata {
    }
    
    private static class A {
    }
    
    private static class SettableBeanProperty {
    	public Object setAndReturn(Object instance, Object value) throws IOException {
    		return instance;
    	}
    }
    
    private static class ObjectMapper {
    	private Factory factory = new Factory();
    	
    	public Factory getFactory() {
    		return factory;
    	}
    	
    	private class Factory {
	    	public JsonParser createParser(String json) throws IOException {
	    		return new JsonParser();
	    	}
	    }
    }
    
    private static class JsonParser {
    }
    
    private static class DeserializationContext {
    	private ObjectMapper.Factory factory;
    	
    	public DeserializationContext(ObjectMapper.Factory factory) {
    		this.factory = factory;
    	}
    	
    	public ReadableObjectId findObjectId(Object id, Object generator, Object resolver) {
    		ReadableObjectId roid = new ReadableObjectId();
    		return roid;
    	}
    }
    
    private static class ReadableObjectId {
    	public void bindItem(Object instance) {
    		
    	}
    }
    
    private static class JsonDeserializer<T> {
    	public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    		return null;
    	}
    }
}