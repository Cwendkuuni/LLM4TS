package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValuePropertyConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNotNull(objectIdValueProperty);
    }

    @Test
    public void testObjectIdValuePropertyCopyConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty copiedObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, new JsonDeserializer<>());
        assertNotNull(copiedObjectIdValueProperty);
        assertEquals(objectIdValueProperty, copiedObjectIdValueProperty);
    }

    @Test
    public void testObjectIdValuePropertyNameChange() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty renamedObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, newName);
        assertEquals(newName, renamedObjectIdValueProperty.getName());
    }

    @Test
    public void testObjectIdValuePropertyValueDeserializerChange() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonDeserializer<?> newDeserializer = new JsonDeserializer<>() {};
        ObjectIdValueProperty deserializerChangedObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, newDeserializer);
        assertNotSame(objectIdValueProperty._valueDeserializer, deserializerChangedObjectIdValueProperty._valueDeserializer);
        assertEquals(objectIdValueProperty, deserializerChangedObjectIdValueProperty);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(objectIdValueProperty.getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonParser jsonParser = createParser("{\"id\":1}");
        Object instance = new Object();
        objectIdValueProperty.deserializeAndSet(jsonParser, new DeserializationContext(), instance);

        Object id = objectIdValueProperty.getValue();
        assertEquals(1, id);
    }

    @Test
    public void testDeserializeSetAndReturnNullId() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonParser jsonParser = createParser("{}");
        Object instance = new Object();
        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, new DeserializationContext(), instance);
        assertNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        objectIdValueProperty.set(instance, null);
    }

    @Test
    public void testSetAndReturnNullId() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        Object result = objectIdValueProperty.setAndReturn(instance, null);
        assertNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithNullValueDeserializer() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        Object result = objectIdValueProperty.setAndReturn(instance, null);
        assertNull(result);
    }

    @Test
    public void testSetAndReturnWithValidId() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        Object id = new Object();
        Object result = objectIdValueProperty.setAndReturn(instance, id);
        assertSame(instance, result);
    }

    private JsonParser createParser(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(json);
    }

    private static class ObjectIdReader implements ObjectIdResolver, ObjectIdGenerator {
        public String propertyName = "propertyName";
        public Class<?> idType = Integer.class;
        public TypeDeserializer idTypeDeserializer = null;

        public ObjectReader getObjectReader() {
            return null;
        }

        public ObjectIdReader() {
        }
    }
}