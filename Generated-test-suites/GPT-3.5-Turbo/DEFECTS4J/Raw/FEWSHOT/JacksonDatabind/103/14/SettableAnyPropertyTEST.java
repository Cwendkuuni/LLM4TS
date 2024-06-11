package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.apache.commons.cli.Option;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.keyval.KeyDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class SettableAnyPropertyTest {

    @Test
    public void testGetAndSetProperty() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeser = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeser = mock(JsonDeserializer.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        
        SettableAnyProperty settable = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        
        assertEquals(property, settable.getProperty());
    }
    
    @Test
    public void testHasValueDeserializer() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeser = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeser = mock(JsonDeserializer.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        
        SettableAnyProperty settable = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        
        assertEquals(true, settable.hasValueDeserializer());
    }
    
    @Test
    public void testGetType() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeser = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeser = mock(JsonDeserializer.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        
        SettableAnyProperty settable = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        
        assertEquals(type, settable.getType());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMethod setter = mock(AnnotatedMethod.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeser = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeser = mock(JsonDeserializer.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        String propName = "prop1";
        Object value = mock(Object.class);
        
        SettableAnyProperty settable = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        settable.set(instance, propName, value);
        
        verify(setter, times(1)).callOnWith(instance, propName, value);
        verify(p, times(1)).getCurrentToken();
        verify(valueDeser, times(1)).deserialize(p, ctxt);
    }
    
    @Test
    public void testDeserialize() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedField.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeser = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeser = mock(JsonDeserializer.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        SettableAnyProperty settable = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        Object deserializedValue = settable.deserialize(p, ctxt);
        
        verify(valueDeser, times(1)).deserialize(p, ctxt);
        assertEquals(p, settable.deserializeAndSet(p, ctxt, deserializedValue, "prop1"));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testSetWhenSetterIsField() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedField setter = mock(AnnotatedField.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeser = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeser = mock(JsonDeserializer.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Option option = mock(Option.class);
        Map<Object,Object> map = mock(Map.class);
        
        SettableAnyProperty settable = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        option.addOption(map);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testSetWhenSetterIsMethod() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMethod setter = mock(AnnotatedMethod.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeser = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeser = mock(JsonDeserializer.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Option option = mock(Option.class);
        
        SettableAnyProperty settable = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        option.addOption(settable);
    }

    @Test(expected = IOException.class)
    public void testThrowAsIOE() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeser = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeser = mock(JsonDeserializer.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);

        SettableAnyProperty settable = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        settable._throwAsIOE(new Exception(), "propName", "value");
    }

    @Test
    public void testGetClassName() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeser = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeser = mock(JsonDeserializer.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);

        SettableAnyProperty settable = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        assertEquals("com.fasterxml.jackson.databind.deser.SettableAnyProperty", settable.getClassName());
    }

    @Test
    public void testToString() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeser = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeser = mock(JsonDeserializer.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);

        SettableAnyProperty settable = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        assertEquals("[any property on class com.fasterxml.jackson.databind.deser.SettableAnyProperty]", settable.toString());
    }
}