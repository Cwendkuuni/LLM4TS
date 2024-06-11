package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.Std;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private AnnotationIntrospector annotationIntrospector;
    private JsonDeserializer<ObjectIdValueProperty> objectIdValuePropertyDeserializer;
    private BeanPropertyDefinition idPropertyDefinition;

    @Before
    public void setUp() throws Exception {
        objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(PropertyName.construct("id"));
        when(objectIdReader.getIdType()).thenReturn(Integer.class);
        when(objectIdReader.getDeserializer()).thenReturn(mock(JsonDeserializer.class));
        when(objectIdReader.idProperty).thenReturn(null);
        when(objectIdReader.generator).thenReturn(ObjectIdGenerators.IntSequenceGenerator.instance);
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));

        annotationIntrospector = mock(AnnotationIntrospector.class);
        when(annotationIntrospector.findPropertyAnnotation(PropertyName.construct("id"), ObjectIdGenerator.class))
                .thenReturn(mock(ObjectIdGenerator.class));

        objectIdValuePropertyDeserializer = new Std(Std.NullValue.NULL, ObjectIdValueProperty.class, null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(annotationIntrospector);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.getParser();
        JsonDeserializer<ObjectIdValueProperty> deserializer = mapper.deserializerForClass(ObjectIdValueProperty.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        BeanPropertyDefinition idPropertyDefinition = new BeanPropertyDefinition(objectIdValueProperty.getName(),
                ObjectIdValueProperty.class, null, null, null, null, null, null, null, null, null);

        TypeDeserializer intTypeDeserializer = mapper.findValueDeserializer(Integer.class, null, null);
        JsonDeserializer<ReadableObjectId> readableObjectIdDeserializer = new Std(Std.NullValue.NULL, ReadableObjectId.class, intTypeDeserializer);
        objectIdValueProperty._objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return objectIdReader.propertyName;
            }

            @Override
            public Class<?> getIdType() {
                return objectIdReader.getIdType();
            }

            @Override
            public JsonDeserializer<?> getDeserializer() {
                return objectIdReader.getDeserializer();
            }

            @Override
            public ObjectIdReader.IdGenerator generator() {
                return objectIdReader.generator;
            }

            @Override
            public ObjectIdReader.ObjectIdResolver resolver() {
                return objectIdReader.resolver;
            }

            @Override
            public JsonDeserializer<? extends ReadableObjectId> idDeserializer() {
                return readableObjectIdDeserializer;
            }

            @Override
            public BeanPropertyDefinition idProperty() {
                return idPropertyDefinition;
            }
        };
        when(objectIdValuePropertyDeserializer.createContextual(jsonParser, jsonFactory, ObjectIdValueProperty.class, null))
                .thenReturn(mock(DeserializationContext.class));
        when(objectIdValuePropertyDeserializer.deserialize(jsonParser, mock(DeserializationContext.class))).thenReturn(objectIdValueProperty);
    }

    @Test
    public void testSerialization() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        ObjectWriter objectWriter = new ObjectMapper().writerWithView(PropertyAccessor.ALL, null);
        String json = objectWriter.writeValueAsString(objectIdValueProperty);
        assertNull(json);

        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.getParser();
        ObjectIdValueProperty deserializedObject = mapper.readValue(jsonParser, ObjectIdValueProperty.class);
        assertSame(objectIdValueProperty, deserializedObject);
    }

    @Test
    public void testDeserialization() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);

        ObjectNode node = mapper.createObjectNode();
        node.put("name", "id");
        node.put("idType", "java.lang.Integer");
        node.put("metadata", new ObjectMapper().writerWithView(PropertyAccessor.ALL, null).writeValueAsString(new ObjectMapper().findPropertiesConfig(ObjectIdValueProperty.class)));
        JsonParser jsonParser = jsonFactory.getParser();
        jsonParser.readValueAs(ObjectIdValueProperty.class, node, new ObjectMapper().findDeserializer(ObjectIdValueProperty.class));
        ObjectIdValueProperty deserializedObject = jsonParser.readValueAs(ObjectIdValueProperty.class);
        assertSame(objectIdValueProperty, deserializedObject);
    }

    @Test
    public void testDeserializationAndSet() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        ObjectNode node = mapper.createObjectNode();
        node.put("name", "id");
        node.put("idType", "java.lang.Integer");
        node.put("value", 1);
        JsonParser jsonParser = jsonFactory.getParser();
        jsonParser.readValueAs(ObjectIdValueProperty.class, node, objectIdValuePropertyDeserializer);
        Object instance = new Object();
        ObjectIdValueProperty.deserializeAndSet(jsonParser, mock(DeserializationContext.class), instance);
        assertSame(1, instance.getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();
        Object value = 1;
        Object result = objectIdValueProperty.setAndReturn(instance, value);
        assertSame(value, result);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        Class<ObjectIdGenerator> annotationType = ObjectIdGenerator.class;
        assertNull(objectIdValueProperty.getAnnotation(annotationType));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        assertNull(objectIdValueProperty.getMember());
    }
}