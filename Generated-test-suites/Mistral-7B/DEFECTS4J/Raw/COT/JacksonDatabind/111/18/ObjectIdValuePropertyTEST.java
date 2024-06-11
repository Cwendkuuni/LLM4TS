package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ObjectIdValueProperty
 */
public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty property;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() {
        AnnotatedClass ac = mock(AnnotatedClass.class);
        when(ac.findAnnotation(JsonId.class)).thenReturn(new JsonId());
        BeanPropertyDefinition bpd = mock(BeanPropertyDefinition.class);
        when(bpd.getName()).thenReturn("testProperty");
        AnnotatedMember am = mock(AnnotatedMember.class);
        when(am.getMemberName()).thenReturn("testProperty");
        when(am.getAnnotations()).thenReturn(ac.annotations());
        property = new ObjectIdValueProperty(objectIdReader = mock(ObjectIdReader.class), bpd, new JacksonAnnotationIntrospector(), null);
        property.setName(new PropertyName("testProperty"));
    }

    @Test
    public void testEmptyConstructor() {
        assertNull(new ObjectIdValueProperty(null, null));
    }

    @Test
    public void testConstructor() {
        assertNotNull(property);
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty propertyWithName = property.withName(new PropertyName("testNewName"));
        assertNotNull(propertyWithName);
        assertNotSame(property, propertyWithName);
        assertEquals("testNewName", propertyWithName.getName().asString());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty propertyWithDeserializer = property.withValueDeserializer(deserializer);
        assertNotNull(propertyWithDeserializer);
        assertSame(deserializer, propertyWithDeserializer.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        ObjectIdValueProperty propertyWithNullProvider = property.withNullProvider(nullValueProvider);
        assertNotNull(propertyWithNullProvider);
        assertSame(nullValueProvider, propertyWithNullProvider.getNullProvider());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNullPropertyWithoutIdProperty() throws IOException {
        when(objectIdReader.getIdProperty()).thenReturn(null);
        property.set(new Object(), null);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        String jsonString = "{\"id\": \"123\"}";
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getCurrentName()).thenReturn("id");
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getValueAsString()).thenReturn("123");
        Object id = "123";
        when(objectIdReader.getIdType()).thenReturn(IdType.STRING);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));

        when(objectIdReader.getIdProperty()).thenReturn(new StubSettableBeanProperty());

        property.deserializeAndSet(jsonParser, mock(DeserializationContext.class), new Object());

        verify(objectIdReader.generator).nextId();
        verify(objectIdReader.resolver).findProperty("123");
        verify(objectIdReader.getIdProperty()).bindItem(new Object());
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNullValueParser() throws IOException {
        property.deserializeAndSet(null, mock(DeserializationContext.class), new Object());
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithValueParserWithoutId() throws IOException {
        when(objectIdReader.getIdType()).thenReturn(IdType.INT);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.getIdProperty()).thenReturn(new StubSettableBeanProperty());

        String jsonString = "{\"value\": 123}";
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getCurrentName()).thenReturn("value");
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER);
        when(jsonParser.getValueAsInt()).thenReturn(123);

        property.deserializeAndSet(jsonParser, mock(DeserializationContext.class), new Object());
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithValueParserWithInvalidIdType() throws IOException {
        when(objectIdReader.getIdType()).thenReturn(IdType.OBJECT);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.getIdProperty()).thenReturn(new StubSettableBeanProperty());

        String jsonString = "{\"id\": \"object\"}";
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getCurrentName()).thenReturn("id");
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getValueAsString()).thenReturn("object");

        property.deserializeAndSet(jsonParser, mock(DeserializationContext.class), new Object());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(objectIdReader.getIdType()).thenReturn(IdType.INT);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.getIdProperty()).thenReturn(new StubSettableBeanProperty());

        Object instance = new Object();
        Object id = 123;

        Object result = property.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), instance);

        verify(objectIdReader.generator).nextId();
        verify(objectIdReader.resolver).findProperty(id.toString());
        verify(objectIdReader.getIdProperty()).bindItem(instance);
        verify(objectIdReader.getIdProperty()).setAndReturn(instance, id);
        assertSame(instance, result);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        when(objectIdReader.getIdProperty()).thenReturn(new StubSettableBeanProperty());

        Object instance = new Object();
        Object id = 123;

        Object result = property.setAndReturn(instance, id);

        verify(objectIdReader.getIdProperty()).setAndReturn(instance, id);
        assertSame(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithNoIdProperty() throws IOException {
        when(objectIdReader.getIdProperty()).thenReturn(null);
        property.setAndReturn(new Object(), 123);
    }

    private static class StubSettableBeanProperty implements SettableBeanProperty {
        @Override
        public void bind(Object pojo) {
        }

        @Override
        public Object getValue(Object pojo) {
            return null;
        }

        @Override
        public Object getValueInstantiator() {
            return null;
        }

        @Override
        public void set(Object pojo, Object value) throws IOException {
        }
    }

}