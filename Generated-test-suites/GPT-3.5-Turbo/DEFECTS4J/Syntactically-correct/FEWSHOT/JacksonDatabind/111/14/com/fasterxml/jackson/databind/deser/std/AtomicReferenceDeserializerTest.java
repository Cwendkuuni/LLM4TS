package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

public class AtomicReferenceDeserializerTest {
    
    private AtomicReferenceDeserializer atomicReferenceDeserializer;
    
    @Before
    public void setup() {
        
        JavaType fullType = new JavaType();
        ValueInstantiator instantiator = new ValueInstantiator();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        JsonDeserializer<?> deserializer = new JsonDeserializer<>();
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, instantiator, typeDeserializer, deserializer);
    }
    
    @Test
    public void testWithResolved() {
        
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        JsonDeserializer<?> valueDeserializer = new JsonDeserializer<>();
        AtomicReferenceDeserializer result = atomicReferenceDeserializer.withResolved(typeDeserializer, valueDeserializer);
        
        assertNotNull(result);
        //additional assertions
    }
    
    @Test
    public void testGetNullValue() throws JsonMappingException {
        
        DeserializationContext context = new DeserializationContext();
        AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(context);
        
        assertNotNull(result);
        //additional assertions
    }
    
    @Test
    public void testGetEmptyValue() {
        
        DeserializationContext context = new DeserializationContext();
        Object result = atomicReferenceDeserializer.getEmptyValue(context);
        
        assertNotNull(result);
        //additional assertions
    }
    
    @Test
    public void testReferenceValue() {
        
        Object contents = new Object();
        AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);
        
        assertNotNull(result);
        //additional assertions
    }
    
    @Test
    public void testGetReferenced() {
        
        AtomicReference<Object> reference = new AtomicReference<>(new Object());
        Object result = atomicReferenceDeserializer.getReferenced(reference);
        
        assertNotNull(result);
        //additional assertions
    }
    
    @Test
    public void testUpdateReference() {
        
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();
        AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(reference, contents);
        
        assertNotNull(result);
        //additional assertions
    }
    
    @Test
    public void testSupportsUpdate() {
        
        DeserializationConfig config = new DeserializationConfig();
        Boolean result = atomicReferenceDeserializer.supportsUpdate(config);
        
        assertNotNull(result);
        //additional assertions
    }
}