
package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class SettableAnyPropertyTest {
    
    private SettableAnyProperty settableAnyProperty;
    private BeanProperty beanProperty;
    private AnnotatedMember setter;
    private JavaType javaType;
    private KeyDeserializer keyDeserializer;
    private JsonDeserializer<Object> valueDeserializer;
    private TypeDeserializer typeDeserializer;
    
    @Before
    public void setup() {
        beanProperty = null;
        setter = null;
        javaType = null;
        keyDeserializer = null;
        valueDeserializer = null;
        typeDeserializer = null;
        settableAnyProperty = new SettableAnyProperty(beanProperty, setter, javaType, keyDeserializer, valueDeserializer, typeDeserializer);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(beanProperty, settableAnyProperty.getProperty());
        assertEquals(javaType, settableAnyProperty.getType());
        assertNull(settableAnyProperty._keyDeserializer);
        assertEquals(valueDeserializer, settableAnyProperty._valueDeserializer);
        assertEquals(typeDeserializer, settableAnyProperty._valueTypeDeserializer);
    }
    
    @Test
    public void testWithDeserializer() {
        JsonDeserializer<Object> newDeserializer = new JsonDeserializer<Object>() {
            
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        
        SettableAnyProperty newSettableAnyProperty = settableAnyProperty.withValueDeserializer(newDeserializer);
        
        assertEquals(beanProperty, newSettableAnyProperty.getProperty());
        assertEquals(javaType, newSettableAnyProperty.getType());
        assertNull(newSettableAnyProperty._keyDeserializer);
        assertEquals(newDeserializer, newSettableAnyProperty._valueDeserializer);
        assertEquals(typeDeserializer, newSettableAnyProperty._valueTypeDeserializer);
    }
    
    @Test
    public void testFixAccess() {
        DeserializationConfig config = new DeserializationConfig();
        
        settableAnyProperty.fixAccess(config);
        
        assertTrue(setter.isAccessible());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadResolveWithoutSetter() {
        settableAnyProperty.readResolve();
    }
    
    @Test
    public void testGetValueDeserializer() {
        assertNull(settableAnyProperty.hasValueDeserializer());
    }
    
    @Test
    public void testDeserialize() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        
        Object nullValue = settableAnyProperty.deserialize(parser, context);
        
        assertNull(nullValue);
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        Object instance = null;
        String propName = "";
        
        settableAnyProperty.deserializeAndSet(parser, context, instance, propName);
    }
    
    @Test
    public void testDeserializeAndSetWithUnresolvedForwardReference() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        Object instance = null;
        String propName = "";
        UnresolvedForwardReference reference = null;
        Class<?> type = null;
        
        SettableAnyProperty.AnySetterReferring referring = new SettableAnyProperty.AnySetterReferring(
            settableAnyProperty, reference, type, instance, propName);
        
        reference = new UnresolvedForwardReference(null, "");
        reference.setRoid(null);
        reference.getRoid().appendReferring(referring);
        
        settableAnyProperty.deserializeAndSet(parser, context, instance, propName);
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        
        Object typedValue = settableAnyProperty.deserializeWithType(parser, context);
        
        assertNull(typedValue);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testSetFieldAnnotated() throws IOException {
        setter = new AnnotatedField(null, null, null);
        AnnotatedField field = (AnnotatedField) setter;
        Object instance = new Object();
        Object propName = "key";
        Object value = "value";
        
        field.setValue(instance, null);
        settableAnyProperty.set(instance, propName, value);
        
        Map<Object, Object> mapValue = (Map<Object, Object>) field.getValue(instance);
        
        assertTrue(mapValue.containsKey(propName));
        assertEquals(value, mapValue.get(propName));
    }
    
    @Test
    public void testSetMethodAnnotated() throws IOException {
        setter = new AnnotatedMethod(null, null, null);
        AnnotatedMethod method = (AnnotatedMethod) setter;
        Object instance = new Object();
        Object propName = "key";
        Object value = "value";
        
        settableAnyProperty.set(instance, propName, value);
        
        assertEquals(propName + value, method.getValue(instance));
    }
}
