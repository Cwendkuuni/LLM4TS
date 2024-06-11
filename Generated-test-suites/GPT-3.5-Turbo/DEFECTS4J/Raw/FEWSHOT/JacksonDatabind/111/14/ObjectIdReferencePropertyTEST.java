package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdReferencePropertyTest {
    
    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        assertNull(property.withNullProvider(null));
    }
    
    @Test
    public void testWithNullProvider_WithValueDeserializer() {
        JsonObjectDeserializer deser = new JsonObjectDeserializer();
        NullValueProvider nva = new NullValueProvider();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        property.withNullProvider(nva).withValueDeserializer(deser);
        
        assertEquals(deser, property.getValueDeserializer());
        assertEquals(nva, property.getNullValueProvider());
    }
    
    @Test
    public void testWithNullProvider_WithValueDeserializer_SameInstance() {
        JsonObjectDeserializer deser = new JsonObjectDeserializer();
        NullValueProvider nva1 = new NullValueProvider();
        NullValueProvider nva2 = new NullValueProvider();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        property.withNullProvider(nva1).withValueDeserializer(deser);
        property.withNullProvider(nva2).withValueDeserializer(deser);
        
        assertEquals(deser, property.getValueDeserializer());
        assertEquals(nva2, property.getNullValueProvider());
    }
    
    @Test
    public void testFixAccess_WithForwardNotNull() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, null);
        DeserializationConfig config = new DeserializationConfig();
        property.fixAccess(config);
        
        // No assertions, just verify that there are no exceptions thrown
    }
    
    @Test
    public void testGetAnnotation() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, null);
        assertNull(property.getAnnotation(null));
    }
    
    @Test
    public void testGetMember() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, null);
        assertNull(property.getMember());
    }
    
    @Test
    public void testGetCreatorIndex() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, null);
        assertEquals(0, property.getCreatorIndex());
    }
    
    @Test
    public void testDeserializeAndSet() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        Object instance = new Object();
        try {
            property.deserializeAndSet(p, ctxt, instance);
        } catch (IOException e) {
            fail();
        }
        
        // No assertions, just verify that there are no exceptions thrown
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        Object instance = new Object();
        try {
            assertNull(property.deserializeSetAndReturn(p, ctxt, instance));
        } catch (IOException e) {
            fail();
        }
        
        // No assertions, just verify that there are no exceptions thrown
    }
    
    @Test
    public void testDeserializeSetAndReturn_WithUnresolvedForwardReference() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        UnresolvedForwardReference reference = new UnresolvedForwardReference();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        Object instance = new Object();
        try {
            assertNull(property.deserializeSetAndReturn(p, ctxt, instance));
        } catch (IOException e) {
            fail();
        }
        
        // No assertions, just verify that there are no exceptions thrown
    }
    
    @Test
    public void testSet() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, null);
        Object instance = new Object();
        try {
            property.set(instance, null);
        } catch (IOException e) {
            fail();
        }
        
        // No assertions, just verify that there are no exceptions thrown
    }
    
    @Test
    public void testSetAndReturn() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, null);
        Object instance = new Object();
        try {
            assertNull(property.setAndReturn(instance, null));
        } catch (IOException e) {
            fail();
        }
        
        // No assertions, just verify that there are no exceptions thrown
    }
    
    public class JsonObjectDeserializer extends JsonDeserializer<Object>{
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
    
    public class NullValueProvider implements NullValueProvider {

        @Override
        public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
            return null;
        }
        
    }

}