package com.example.objectidvalueproperty; // replace with your package name if needed

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    private ObjectMapper objectMapper;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        objectIdReader = new ObjectIdReader(null, null, null, null, null, null, null, null);
    }

    @Test
    public void testObjectIdValuePropertyConstructor() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNotNull(property);
    }

    @Test
    public void testObjectIdValuePropertyCopyConstructor() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty copiedProperty = new ObjectIdValueProperty(property, null);
        assertNotNull(copiedProperty);
        assertEquals(property, copiedProperty);
    }

    @Test
    public void testObjectIdValuePropertyWithName() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty renamedProperty = property.withName(new PropertyName("newName"));
        assertNotSame(property, renamedProperty);
        assertEquals(property.getName(), property.getName());
        assertEquals("newName", renamedProperty.getName().asString());
    }

    @Test
    public void testObjectIdValuePropertyWithValueDeserializer() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonDeserializer<Object> deserializer = new StubJsonDeserializer();
        ObjectIdValueProperty deserializerProperty = new ObjectIdValueProperty(property, deserializer);
        assertSame(deserializer, deserializerProperty.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(property.getAnnotation(String.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object id = "123";
        JsonParser jp = objectMapper.getFactory().createParser(new StringReader(id));
        Object instance = new Object();
        property.deserializeAndSet(jp, objectMapper.getDeserializationContext(), instance);
        assertEquals(id, property.getDeserializer().deserialize(jp, objectMapper.getDeserializationContext()));

        ReadableObjectId roid = property.getObjectIdReader().getContext().findObjectId(id, null, null);
        assertEquals(id, roid.getId());
        assertSame(instance, roid.getObjectIdProperty().getObject());
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object id = "123";
        JsonParser jp = objectMapper.getFactory().createParser(new StringReader(id));
        Object instance = new Object();

        Object result = property.deserializeSetAndReturn(jp, objectMapper.getDeserializationContext(), instance);
        assertEquals(id, property.getDeserializer().deserialize(jp, objectMapper.getDeserializationContext()));

        ReadableObjectId roid = property.getObjectIdReader().getContext().findObjectId(id, null, null);
        assertEquals(id, roid.getId());
        assertSame(instance, roid.getObjectIdProperty().getObject());

        assertEquals(instance, result);
    }

    @Test
    public void testSerializeAndSet() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object id = "123";
        Object instance = new Object();

        property.set(instance, id);
        ObjectWriter ow = objectMapper.writerWithRootValue(instance);
        String json = ow.writeValueAsString(instance);
        JsonParser jp = objectMapper.getFactory().createParser(new StringReader(json));
        Object deserializedInstance = objectMapper.readValue(jp, instance.getClass());

        Object deserializedId = property.getDeserializer().deserialize(jp, objectMapper.getDeserializationContext());
        assertEquals(id, deserializedId);
        assertEquals(id, property.get(deserializedInstance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNull() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        property.set(instance, null);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object oldId = "123";
        Object newId = "456";
        Object instance = new Object();
        Object result = property.setAndReturn(instance, newId);

        assertEquals(newId, property.get(instance));
        assertNotSame(instance, result);
        assertSame(instance, result);
    }

    private static class StubJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsString();
        }
    }

    private static class StringReader implements CharSequence {

        private final String _text;

        public StringReader(String text) {
            _text = text;
        }

        @Override
        public int length() {
            return _text.length();
        }

        @Override
        public char charAt(int index) {
            return _text.charAt(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return new StringReader(_text.subSequence(start, end));
        }

        @Override
        public String toString() {
            return _text;
        }
    }
}