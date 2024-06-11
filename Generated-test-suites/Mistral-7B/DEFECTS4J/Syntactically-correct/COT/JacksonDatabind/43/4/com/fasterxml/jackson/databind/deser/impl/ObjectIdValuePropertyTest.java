package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.util.ObjectIdReader;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdValuePropertyTest extends BaseMapTest {

    private ObjectIdReader objectIdReader = new ObjectIdReader() {
        @Override
        public PropertyName propertyName() {
            return PropertyName.construct("id");
        }

        @Override
        public Class<?> idType() {
            return String.class;
        }

        @Override
        public JsonDeserializer<ObjectId> idDeserializer() {
            return new StdDeserializer<ObjectId>(ObjectId.class) {};
        }

        @Override
        public JsonSerializer<ObjectId> idSerializer() {
            return new StdSerializer<ObjectId>(ObjectId.class) {};
        }

        @Override
        public ObjectIdGenerator idGenerator() {
            return new ObjectIdGenerator() {
                long counter = 0;

                @Override
                public ObjectId generateId(SerializerProvider serializers, BeanProperty property) {
                    return new ObjectId(counter++);
                }
            };
        }

        @Override
        public ObjectIdResolver idResolver() {
            return new ObjectIdResolver() {};
        }
    };

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testObjectIdValuePropertyConstruction() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNotNull(property);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testObjectIdValuePropertyCopyConstructor() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty copied = new ObjectIdValueProperty(property, new JsonDeserializer<ObjectId>() {});
        assertEquals(property, copied);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testObjectIdValuePropertyNameChange() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty renamed = new ObjectIdValueProperty(property, new PropertyName("newName"));
        assertEquals(renamed.getName(), "newName");
        assertEquals(renamed.getClass(), ObjectIdValueProperty.class);
        assertEquals(renamed.getMetadata(), property.getMetadata());
        assertEquals(renamed._objectIdReader, property._objectIdReader);
    }

    @org.junit.Test
    public void testObjectIdValuePropertyValueDeserializerChange() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty changed = new ObjectIdValueProperty(property, new JsonDeserializer<ObjectId>() {});
        assertEquals(changed.getValueDeserializer(), new JsonDeserializer<ObjectId>() {});
        assertEquals(changed.getClass(), ObjectIdValueProperty.class);
        assertEquals(changed.getName(), property.getName());
        assertEquals(changed.getMetadata(), property.getMetadata());
        assertEquals(changed._objectIdReader, property._objectIdReader);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testObjectIdValuePropertyNullNameChange() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        new ObjectIdValueProperty(property, null);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testObjectIdValuePropertyNullValueDeserializerChange() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        new ObjectIdValueProperty(property, null);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testObjectIdValuePropertyWithNullObjectIdReader() throws Exception {
        new ObjectIdValueProperty(null, new PropertyMetadata());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testObjectIdValuePropertyWithNullPropertyMetadata() throws Exception {
        new ObjectIdValueProperty(objectIdReader, null);
    }

    @org.junit.Test
    public void testObjectIdValuePropertyGetAnnotation() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(property.getAnnotation(Annotation.class));
    }

    @org.junit.Test
    public void testObjectIdValuePropertyGetMember() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(property.getMember());
    }

    @org.junit.Test
    public void testObjectIdValuePropertyDeserializeAndSet() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object id = "123";
        Object testInstance = new Object();

        JsonParser mockParser = createParser();
        mockParser.setValue(id);

        property.deserializeAndSet(mockParser, getContext(), testInstance);

        ObjectId idObj = (ObjectId) objectIdReader.idDeserializer().deserialize(mockParser, getContext());
        assertEquals(idObj.getValue(), id);

        ObjectId boundId = context.findObjectId(idObj, objectIdReader.generator, objectIdReader.resolver);
        assertEquals(testInstance.getClass().getDeclaredField("id").get(testInstance), idObj);
    }

    @org.junit.Test
    public void testObjectIdValuePropertyDeserializeSetAndReturnNullId() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object testInstance = new Object();

        JsonParser mockParser = createParser();

        Object result = property.deserializeSetAndReturn(mockParser, getContext(), testInstance);

        assertNull(result);
    }

    @org.junit.Test
    public void testObjectIdValuePropertySet() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object id = "123";
        Object testInstance = new Object();

        testInstance.getClass().getDeclaredField("id").set(testInstance, id);

        property.set(testInstance, null);

        assertNull(testInstance.getClass().getDeclaredField("id").get(testInstance));
    }

    @org.junit.Test
    public void testObjectIdValuePropertySetAndReturnNullId() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object testInstance = new Object();

        Object result = property.setAndReturn(testInstance, null);

        assertNull(result);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testObjectIdValuePropertySetAndReturnNullInstance() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        property.setAndReturn(null, "123");
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testObjectIdValuePropertySetAndReturnNullIdValue() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object testInstance = new Object();

        property.setAndReturn(testInstance, null);
    }

    @org.junit.Test
    public void testObjectIdValuePropertySetAndReturn() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object testInstance = new Object();
        Object id = "123";

        ObjectId idObj = new ObjectId(id);
        context.findObjectId(idObj, objectIdReader.generator, objectIdReader.resolver);

        Object result = property.setAndReturn(testInstance, idObj);
        assertEquals(result, testInstance);

        ObjectId boundId = testInstance.getClass().getDeclaredField("id").get(testInstance);
        assertEquals(boundId.getValue(), id);
    }
}