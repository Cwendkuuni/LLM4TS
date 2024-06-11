package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Test class for {@link ObjectIdValueProperty}
 */
public class ObjectIdValuePropertyTest {

    private JsonDeserializer<ObjectIdNode> objectIdNodeDeserializer;
    private ObjectIdReader objectIdReader;
    private AnnotationIntrospector introspector;
    private DeserializationContext ctxt;
    private JsonParser parser;

    @Before
    public void setup() {
        objectIdNodeDeserializer = mock(JsonDeserializer.class);
        objectIdReader = new ObjectIdReader() {
            private final BeanPropertyDefinition propertyDefinition;
            private final ObjectIdGenerator generator = mock(ObjectIdGenerator.class);
            private final ObjectIdResolver resolver = mock(ObjectIdResolver.class);

            public ObjectIdReader(BeanPropertyDefinition beanPropertyDefinition) {
                this.propertyDefinition = beanPropertyDefinition;
            }

            @Override
            public PropertyName propertyName() {
                return propertyDefinition.getName();
            }

            @Override
            public Class<?> idType() {
                return ObjectIdNode.class;
            }

            @Override
            public JsonDeserializer<ObjectIdNode> idDeserializer() {
                return objectIdNodeDeserializer;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return generator;
            }

            @Override
            public ObjectIdResolver objectIdResolver() {
                return resolver;
            }

            @Override
            public String getIdIdentifier() {
                return null;
            }

            @Override
            public ObjectIdReader withIdType(Class<?> idType) {
                throw new UnsupportedOperationException();
            }
        };
        introspector = mock(AnnotationIntrospector.class);
        ctxt = mock(DeserializationContext.class);
        parser = mock(JsonParser.class);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, createPropertyMetadata());
        assertNull(property.getAnnotation(ObjectIdGenerator.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, createPropertyMetadata());
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, createPropertyMetadata());

        ObjectIdNode idNode = createObjectIdNode();
        when(parser.getValueAsToken()).thenReturn(idNode);
        when(introspector.findBeanProperty(any(Class.class), eq(propertyDefinition.getName()))).thenReturn(propertyDefinition);

        property.deserializeAndSet(parser, ctxt, null);

        ArgumentCaptor<ObjectIdNode> idNodeArgumentCaptor = ArgumentCaptor.forClass(ObjectIdNode.class);
        verify(ctxt).findObjectId(idNodeArgumentCaptor.capture(), eq(generator), eq(resolver));

        ObjectIdNode capturedIdNode = idNodeArgumentCaptor.getValue();
        verify(generator).generateIdInternal(any(), anyInt(), any());
        verify(resolver).resolveId(capturedIdNode);
        verify(propertyDefinition.getBeanProperty().getMethodName("set")).invoke(any(), eq(capturedIdNode.getValue()));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, createPropertyMetadata());

        ObjectIdNode idNode = createObjectIdNode();
        when(parser.getValueAsToken()).thenReturn(idNode);
        when(introspector.findBeanProperty(any(Class.class), eq(propertyDefinition.getName()))).thenReturn(propertyDefinition);

        Object result = property.deserializeSetAndReturn(parser, ctxt, null);

        verifyNoMoreInteractions(generator, resolver, propertyDefinition.getBeanProperty().getMethodName("set"));
        assertEquals(result, null);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, createPropertyMetadata());

        Object id = "123";
        when(introspector.findBeanProperty(any(Class.class), eq(propertyDefinition.getName()))).thenReturn(propertyDefinition);

        property.set(null, id);

        verify(propertyDefinition.getBeanProperty().getMethodName("set")).invoke(any(), eq(id));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, createPropertyMetadata());

        Object id = "123";
        when(introspector.findBeanProperty(any(Class.class), eq(propertyDefinition.getName()))).thenReturn(propertyDefinition);

        Object result = property.setAndReturn(null, id);

        verifyNoMoreInteractions(propertyDefinition.getBeanProperty().getMethodName("set"));
        assertEquals(result, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, createPropertyMetadata());
        property.setAndReturn(null, "123");
    }

    private PropertyMetadata createPropertyMetadata() {
        PropertyMetadata propertyMetadata = mock(PropertyMetadata.class);
        when(propertyMetadata.getSerializedName()).thenReturn("id");
        when(propertyMetadata.getSerializable()).thenReturn(Boolean.TRUE);
        when(propertyMetadata.getDefaultValue()).thenReturn(null);
        return propertyMetadata;
    }

    private ObjectIdNode createObjectIdNode() {
        StdSerializer<ObjectIdNode> serializer = new StdSerializer<ObjectIdNode>(ObjectIdNode.class) {};
        ObjectIdNode objectIdNode = new ObjectIdNode(serializer);
        objectIdNode.setValue("123");
        return objectIdNode;
    }
}