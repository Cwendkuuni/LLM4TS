package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtils.constructParser;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader mockObjectIdReader() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ObjectIdReader reader = mock(ObjectIdReader.class);
        when(reader.propertyName).thenReturn(new PropertyName("id"));
        when(reader.getIdType()).thenReturn(Integer.class);
        when(reader.getDeserializer()).thenReturn(new FromStringDeserializer(Integer.class, null));
        when(reader.idProperty).thenReturn(new BeanPropertyDefinition(new AnnotatedMember(new PropertyName("id"), JsonId.class), Integer.class));
        when(reader.generator).thenReturn(new ObjectIdGenerator());
        when(reader.resolver).thenReturn(new ObjectIdResolver());
        return reader;
    }

    @Test
    public void testObjectIdValuePropertyConstruction() {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertEquals("id", property.getName());
        assertEquals(Integer.class, property.getType());
        assertEquals(objectIdReader, property._objectIdReader);
        assertEquals(null, property.getValueDeserializer());
    }

    @Test
    public void testObjectIdValuePropertyCopy() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty copy = new ObjectIdValueProperty(property, new FromStringDeserializer(Integer.class, null));
        assertEquals("id", copy.getName());
        assertEquals(Integer.class, copy.getType());
        assertEquals(objectIdReader, copy._objectIdReader);
        assertEquals(new FromStringDeserializer(Integer.class, null), copy.getValueDeserializer());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testObjectIdValuePropertyNameChange() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        property = property.withName(new PropertyName("newName"));
    }

    @Test
    public void testObjectIdValuePropertyValueDeserializerChange() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonDeserializer<Integer> deserializer = new FromStringDeserializer(Integer.class, null);
        ObjectIdValueProperty copy = new ObjectIdValueProperty(property, deserializer);
        assertEquals(deserializer, copy.getValueDeserializer());
    }

    @Test
    public void testObjectIdValuePropertyDeserialize() throws IOException, JsonProcessingException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonParser parser = constructParser("42");
        Object id = property.deserialize(parser, new DeserializationContext());
        assertEquals(42, id);
    }

    @Test
    public void testObjectIdValuePropertyDeserializeAndSet() throws IOException, JsonProcessingException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object id = new Integer(1);
        JsonParser parser = constructParser("42");
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        property.deserializeAndSet(parser, ctxt, instance);
        assertEquals(42, id);
    }

    @Test
    public void testObjectIdValuePropertySetAndReturn() throws IOException, JsonProcessingException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object id = new Integer(1);
        Object instance = new Object();
        Object idReturned = property.setAndReturn(instance, id);
        assertSame(id, idReturned);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testObjectIdValuePropertySetNull() throws IOException, JsonProcessingException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        property.set(instance, null);
    }

    @Test
    public void testObjectIdValuePropertySet() throws IOException, JsonProcessingException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object id = new Integer(1);
        Object instance = new Object();
        property.set(instance, id);
        assertEquals(id, instance.getClass().getField("id").get(instance));
    }

    @Test
    public void testObjectIdValuePropertyGetAnnotation() {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(property.getAnnotation(JsonId.class));
    }

    @Test
    public void testObjectIdValuePropertyGetMember() {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(property.getMember());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testObjectIdValuePropertyDeserializeWithValueDeserializer() throws IOException, JsonProcessingException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectParser parser = new ObjectMapper().getFactory().createParser("{\"id\": 42}");
        Object instance = new Object();
        property.deserializeAndSet(parser, new DeserializationContext(), instance);
        fail();
    }

    @Test
    public void testObjectIdValuePropertyDeserializeWithValueDeserializerAndSetReturn() throws IOException, JsonProcessingException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectParser parser = new ObjectMapper().getFactory().createParser("{\"id\": 42}");
        Object instance = new Object();
        Object id = property.deserializeWithValueDeserializerAndReturn(parser, new DeserializationContext(), instance);
        assertEquals(42, id);
    }

    @Test
    public void testObjectIdValuePropertySetAndReturnWithValueDeserializer() throws IOException, JsonProcessingException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectParser parser = new ObjectMapper().getFactory().createParser("{\"id\": 42}");
        Object instance = new Object();
        Object id = property.setAndReturnWithValueDeserializer(parser, new DeserializationContext(), instance);
        assertEquals(42, id);
    }

    @Test
    public void testObjectIdValuePropertySetAndReturnWithValueDeserializerAndSetProperty() throws IOException, JsonProcessingException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectParser parser = new ObjectMapper().getFactory().createParser("{\"id\": 42}");
        Object instance = new Object();
        Object id = property.setAndReturnWithValueDeserializerAndSetProperty(parser, new DeserializationContext(), instance);
        assertEquals(42, id);
        assertEquals(42, instance.getClass().getField("id").get(instance));
    }

    @Test
    public void testObjectIdValuePropertySetAndReturnWithValueDeserializerAndSetPropertyNull() throws IOException, JsonProcessingException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectParser parser = new ObjectMapper().getFactory().createParser("{\"id\": null}");
        Object instance = new Object();
        Object id = property.setAndReturnWithValueDeserializerAndSetProperty(parser, new DeserializationContext(), instance);
        assertNull(id);
        assertNull(instance.getClass().getField("id").get(instance));
    }

    @Test
    public void testObjectIdValuePropertySetAndReturnWithValueDeserializerAndSetPropertyWithTypeDeserializer() throws IOException, JsonProcessingException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectParser parser = new ObjectMapper().getFactory().createParser("{\"id\": \"foo\"}");
        Object instance = new Object();
        ObjectReader objectReader = new ObjectMapper().getFactory().createObjectReader();
        Converter<String, ObjectId> idConverter = mock(Converter.class);
        ObjectIdGenerator objectIdGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver objectIdResolver = mock(ObjectIdResolver.class);
        TypeDeserializer idTypeDeserializer = new TypeDeserializer() {
            @SuppressWarnings("unchecked")
            public Object deserializeTypedFromObject(JsonParser p, DeserializationContext ctxt) throws IOException {
                return idConverter.convert(p, ctxt, String.class, ObjectId.class);
            }
        };
        when(parser.getValueAs(ObjectId.class)).thenReturn(null);
        when(parser.getValueAs(String.class)).thenReturn("foo");
        when(objectReader.readValue(parser, ObjectId.class)).thenReturn(mock(ObjectId.class));
        when(objectIdGenerator.generateId(instance, objectIdResolver)).thenReturn(new ObjectId("foo"));
        when(idConverter.convert(parser, new DeserializationContext(), String.class, ObjectId.class)).thenReturn(new ObjectId("foo"));
        Object id = property.setAndReturnWithValueDeserializerAndSetProperty(parser, new DeserializationContext(), instance);
        assertEquals("foo", id);
        assertEquals(new ObjectId("foo"), instance.getClass().getField("id").get(instance));
    }

    @Test
    public void testObjectIdValuePropertySetAndReturnWithValueDeserializerAndSetPropertyWithNameTransformer() throws IOException, JsonProcessingException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectParser parser = new ObjectMapper().getFactory().createParser("{\"id\": \"foo\"}");
        Object instance = new Object();
        ObjectReader objectReader = new ObjectMapper().getFactory().createObjectReader();
        NameTransformer idNameTransformer = mock(NameTransformer.class);
        ObjectIdGenerator objectIdGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver objectIdResolver = mock(ObjectIdResolver.class);
        TypeDeserializer idTypeDeserializer = new TypeDeserializer() {
            @SuppressWarnings("unchecked")
            public Object deserializeTypedFromObject(JsonParser p, DeserializationContext ctxt) throws IOException {
                return idTypeDeserializer.deserializeTypedFromObject(p, ctxt);
            }
        };
        when(parser.getValueAs(ObjectId.class)).thenReturn(null);
        when(parser.getValueAs(String.class)).thenReturn("foo");
        when(objectReader.readValue(parser, ObjectId.class)).thenReturn(mock(ObjectId.class));
        when(objectIdGenerator.generateId(instance, objectIdResolver)).thenReturn(new ObjectId("bar"));
        when(idNameTransformer.transformFromPropertyName("id")).thenReturn("newId");
        when(idNameTransformer.transformToPropertyName(new PropertyName("newId"))).thenReturn(new PropertyName("id"));
        when(idTypeDeserializer.deserializeTypedFromObject(parser, new DeserializationContext())).thenReturn(idNameTransformer);
        Object id = property.setAndReturnWithValueDeserializerAndSetProperty(parser, new DeserializationContext(), instance);
        assertEquals("foo", id);
        assertEquals(new ObjectId("bar"), instance.getClass().getField("id").get(instance));
    }
}