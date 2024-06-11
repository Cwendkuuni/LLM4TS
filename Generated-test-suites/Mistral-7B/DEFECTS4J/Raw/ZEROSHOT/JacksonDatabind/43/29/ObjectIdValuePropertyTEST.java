package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.PojoIntrospector;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.util.BeanUtil;

/**
 * Test class for {@link ObjectIdValueProperty}.
 */
public class ObjectIdValuePropertyTest {

    private ObjectMapper objectMapper;
    private JsonDeserializer<ObjectIdValueProperty> deserializer;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        PojoIntrospector introspector = objectMapper.getSerializationConfig()
                .getIntrospector();

        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        JsonDeserializer<Object> idDeserializer = Mockito.mock(JsonDeserializer.class);
        deserializer = new ObjectIdValueProperty.Std(objectIdReader, null, null, idDeserializer);

        JavaBeanProperty property = new JavaBeanProperty("id", Object.class, null, null);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, property.getMetadata());
    }

    @Test
    public void testNewInstance() {
        ObjectIdValueProperty newInstance = new ObjectIdValueProperty(objectIdValueProperty, null);
        assertEquals(objectIdValueProperty, newInstance);
    }

    @Test
    public void testNewInstanceWithName() {
        PropertyName newName = Mockito.mock(PropertyName.class);
        ObjectIdValueProperty newInstance = new ObjectIdValueProperty(objectIdValueProperty, newName);
        assertEquals(objectIdValueProperty, newInstance);
        assertEquals(newName, newInstance.getName());
    }

    @Test
    public void testNewInstanceWithDeserializer() {
        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
        ObjectIdValueProperty newInstance = new ObjectIdValueProperty(objectIdValueProperty, deserializer);
        assertEquals(objectIdValueProperty, newInstance);
        assertEquals(deserializer, newInstance.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty property = objectIdValueProperty;
        Object id = "123";
        Object instance = new Object();

        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        when(parser.nextValue()).thenReturn(id);
        when(context.findBeanProperty("id", Object.class)).thenReturn(property);
        when(context.findObjectId(eq(id), Mockito.any(ObjectIdReader.class), Mockito.any(ObjectIdGenerator.class))).thenReturn(Mockito.mock(ReadableObjectId.class));

        property.deserializeAndSet(parser, context, instance);

        ReadableObjectId readableObjectId = Mockito.requireNonNull(Mockito.argThat(arg -> arg instanceof ReadableObjectId), "Unexpected argument type in arg");
        verify(readableObjectId).bindItem(instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdValueProperty property = objectIdValueProperty;
        Object id = "123";
        Object instance = new Object();

        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        when(parser.nextValue()).thenReturn(id);
        when(context.findBeanProperty("id", Object.class)).thenReturn(property);
        when(context.findObjectId(eq(id), Mockito.any(ObjectIdReader.class), Mockito.any(ObjectIdResolver.class))).thenReturn(Mockito.mock(ReadableObjectId.class));
        when(property.getValueDeserializer().deserialize(parser, context)).thenReturn(id);

        SettableBeanProperty idProperty = Mockito.mock(SettableBeanProperty.class);
        when(property.getBeanProperty()).thenReturn(idProperty);
        when(idProperty.getName()).thenReturn("id");

        Object returnedInstance = property.deserializeSetAndReturn(parser, context, instance);

        assertEquals(instance, returnedInstance);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdValueProperty property = objectIdValueProperty;
        Object id = "123";
        Object instance = new Object();

        SettableBeanProperty idProperty = Mockito.mock(SettableBeanProperty.class);
        when(property.getValueDeserializer().deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class))).thenReturn(id);
        when(idProperty.getName()).thenReturn("id");
        when(idProperty.getMember()).thenReturn(Mockito.mock(AnnotatedMember.class));
        when(idProperty.setAndReturn(instance, id)).thenReturn(instance);

        property.set(instance, id);

        verify(idProperty).setAndReturn(instance, id);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty property = objectIdValueProperty;
        Object id = "123";
        Object instance = new Object();

        SettableBeanProperty idProperty = Mockito.mock(SettableBeanProperty.class);
        when(property.getValueDeserializer().deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class))).thenReturn(id);
        when(idProperty.getName()).thenReturn("id");
        when(idProperty.getMember()).thenReturn(Mockito.mock(AnnotatedMember.class));
        when(idProperty.setAndReturn(instance, id)).thenReturn(instance);

        Object returnedInstance = property.setAndReturn(instance, id);

        assertEquals(instance, returnedInstance);
    }
}