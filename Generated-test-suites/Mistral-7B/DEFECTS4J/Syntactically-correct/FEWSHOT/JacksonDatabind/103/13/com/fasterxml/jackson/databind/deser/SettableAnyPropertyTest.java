package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyTypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.WithValueTypeDeserializer;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;

public class SettableAnyPropertyTest extends TestBase {

    @Test
    public void testSettableAnyProperty() throws IOException {
        AnnotationIntrospector introspector = mock(AnnotationIntrospector.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedField.class);
        when(setter.getType()).thenReturn(String.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer valueTypeDeserializer = new EmptyTypeDeserializer();
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(anyProperty);
        assertEquals(property, anyProperty.getProperty());
        assertEquals(setter, anyProperty._setter);
        assertEquals(type, anyProperty.getType());
        assertNull(anyProperty.getValueDeserializer());
        assertNull(anyProperty.getValueTypeDeserializer());
        assertNull(anyProperty.getKeyDeserializer());
        assertFalse(anyProperty.isSetterIsField());

        JsonDeserializer<Object> newValueDeserializer = mock(JsonDeserializer.class);
        SettableAnyProperty newAnyProperty = anyProperty.withValueDeserializer(newValueDeserializer);
        assertEquals(anyProperty.getProperty(), newAnyProperty.getProperty());
        assertEquals(anyProperty._setter, newAnyProperty._setter);
        assertEquals(anyProperty.getType(), newAnyProperty.getType());
        assertEquals(newValueDeserializer, newAnyProperty.getValueDeserializer());
        assertEquals(valueTypeDeserializer, newAnyProperty.getValueTypeDeserializer());
        assertEquals(keyDeserializer, newAnyProperty.getKeyDeserializer());
        assertFalse(newAnyProperty.isSetterIsField());
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() throws IOException {
        AnnotationIntrospector introspector = mock(AnnotationIntrospector.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedField.class);
        when(setter.getType()).thenReturn(String.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = new WithValueTypeDeserializer(JsonDeserializer.None.instance());

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(anyProperty);
        assertEquals(property, anyProperty.getProperty());
        assertEquals(setter, anyProperty._setter);
        assertEquals(type, anyProperty.getType());
        assertNotNull(anyProperty.getValueDeserializer());
        assertEquals(valueTypeDeserializer, anyProperty.getValueTypeDeserializer());
        assertNull(anyProperty.getKeyDeserializer());
        assertFalse(anyProperty.isSetterIsField());

        Object instance = new Object();
        String propName = "testProperty";
        String value = "testValue";

        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentName()).thenReturn(propName);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn(value);

        DeserializationContext context = mock(DeserializationContext.class);
        when(context.getAnnotationIntrospector()).thenReturn(introspector);
        when(context.getBeanProperty(_property)).thenReturn(property);
        when(context.getDeserializationConfig()).thenReturn(context);
        when(context.findBeanProperty(_property.getName())).thenReturn(property);
        when(context.findBeanProperties("testProperty", SettableAnyProperty.class)).thenReturn(new Object[]{property});
        when(context.handleValueDeserialization(valueDeserializer, property, parser, context)).thenReturn(value);

        anyProperty.deserializeAndSet(parser, context, instance, propName);

        Map<Object, Object> map = (Map<Object, Object>) ((AnnotatedField) setter).getValue(instance);
        assertEquals(value, map.get(propName));
    }

    @Test(expected = JsonMappingException.class)
    public void testSettableAnyPropertyWithNullValueDeserializer() throws IOException {
        AnnotationIntrospector introspector = mock(AnnotationIntrospector.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedField.class);
        when(setter.getType()).thenReturn(String.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = null;
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "testProperty";

        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        anyProperty.deserializeAndSet(parser, context, instance, propName);
    }

    @Test
    public void testSettableAnyPropertyWithKeyDeserializer() throws IOException {
        AnnotationIntrospector introspector = mock(AnnotationIntrospector.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedField.class);
        when(setter.getType()).thenReturn(String.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(anyProperty);
        assertEquals(property, anyProperty.getProperty());
        assertEquals(setter, anyProperty._setter);
        assertEquals(type, anyProperty.getType());
        assertNull(anyProperty.getValueDeserializer());
        assertEquals(valueTypeDeserializer, anyProperty.getValueTypeDeserializer());
        assertNotNull(anyProperty.getKeyDeserializer());
        assertFalse(anyProperty.isSetterIsField());

        Object instance = new Object();
        String keyName = "testKey";
        String propName = "testProperty";
        String value = "testValue";

        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentName()).thenReturn(keyName);
        when(parser.getValueAsString()).thenReturn(propName);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn(value);
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.getAnnotationIntrospector()).thenReturn(introspector);
        when(context.getBeanProperty(_property)).thenReturn(property);
        when(context.getDeserializationConfig()).thenReturn(context);
        when(context.findBeanProperty(_property.getName())).thenReturn(property);
        when(context.findBeanProperties("testProperty", SettableAnyProperty.class)).thenReturn(new Object[]{property});
        when(context.handleKeyDeserialization(_keyDeserializer, keyName, propName, parser, context)).thenReturn(propName);
        when(context.handleValueDeserialization(valueDeserializer, property, parser, context)).thenReturn(value);

        anyProperty.deserializeAndSet(parser, context, instance, propName);

        Map<Object, Object> map = (Map<Object, Object>) ((AnnotatedField) setter).getValue(instance);
        assertEquals(value, map.get(propName));
        assertEquals(propName, map.keySet().iterator().next());
    }

    @Test(expected = JsonMappingException.class)
    public void testSettableAnyPropertyWithUnresolvedForwardReference() throws IOException {
        AnnotationIntrospector introspector = mock(AnnotationIntrospector.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedField.class);
        when(setter.getType()).thenReturn(String.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        Object instance = new Object();
        String propName = "testProperty";

        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);

        when(parser.getCurrentName()).thenReturn(propName);
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);

        anyProperty.deserializeAndSet(parser, context, instance, propName);
    }

    @Test
    public void testSettableAnyPropertyWithKeyDeserializerNullKey() throws IOException {
        AnnotationIntrospector introspector = mock(AnnotationIntrospector.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedField.class);
        when(setter.getType()).thenReturn(String.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(anyProperty);
        assertEquals(property, anyProperty.getProperty());
        assertEquals(setter, anyProperty._setter);
        assertEquals(type, anyProperty.getType());
        assertNull(anyProperty.getValueDeserializer());
        assertNull(anyProperty.getValueTypeDeserializer());
        assertNull(anyProperty.getKeyDeserializer());
        assertFalse(anyProperty.isSetterIsField());

        Object instance = new Object();
        String propName = "testProperty";
        String value = "testValue";

        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentName()).thenReturn(propName);
        when(parser.getValueAsString()).thenReturn(value);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.getAnnotationIntrospector()).thenReturn(introspector);
        when(context.getBeanProperty(_property)).thenReturn(property);
        when(context.getDeserializationConfig()).thenReturn(context);
        when(context.findBeanProperty(_property.getName())).thenReturn(property);
        when(context.findBeanProperties("testProperty", SettableAnyProperty.class)).thenReturn(new Object[]{property});
        when(context.handleValueDeserialization(valueDeserializer, property, parser, context)).thenReturn(value);

        anyProperty.deserializeAndSet(parser, context, instance, propName);

        Map<Object, Object> map = (Map<Object, Object>) ((AnnotatedField) setter).getValue(instance);
        assertEquals(value, map.get(propName));
    }
}