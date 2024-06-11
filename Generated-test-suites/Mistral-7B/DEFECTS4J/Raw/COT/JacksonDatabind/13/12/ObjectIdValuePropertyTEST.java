package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Test class for ObjectIdValueProperty
 */
public class ObjectIdValuePropertyTest extends TestBase {

    private static final ObjectIdReader objectIdReader = new ObjectIdReader() {
        @Override
        public PropertyName propertyName() {
            return new PropertyName("id");
        }

        @Override
        public Class<Object> idType() {
            return Object.class;
        }

        @Override
        public ObjectIdGenerator idGenerator() {
            return null;
        }

        @Override
        public ObjectIdResolver idResolver() {
            return null;
        }

        @Override
        public JsonDeserializer<Object> idDeserializer() {
            return null;
        }

        @Override
        public SettableBeanProperty idProperty() {
            return null;
        }
    };

    private static final ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

    @org.junit.Test
    public void testConstructor() {
        assertNotNull(objectIdValueProperty);
    }

    @org.junit.Test
    public void testGetAnnotation() throws Exception {
        assertNull(objectIdValueProperty.getAnnotation(null));
    }

    @org.junit.Test
    public void testGetMember() throws Exception {
        assertNull(objectIdValueProperty.getMember());
    }

    @org.junit.Test
    public void testDeserializeAndSet() throws Exception {
        Object instance = new Object();
        JsonParser jp = createParser("{\"id\":1}");

        objectIdValueProperty.deserializeAndSet(jp, context(), instance);

        ObjectIdValueProperty property = (ObjectIdValueProperty) findProperty(instance, "id");
        assertNotNull(property);
        assertEquals(1, property.getId());
    }

    @org.junit.Test
    public void testDeserializeSetAndReturn() throws Exception {
        Object instance = new Object();
        JsonParser jp = createParser("{\"id\":1}");

        Object result = objectIdValueProperty.deserializeSetAndReturn(jp, context(), instance);

        assertEquals(instance, result);
        ObjectIdValueProperty property = (ObjectIdValueProperty) findProperty(instance, "id");
        assertNotNull(property);
        assertEquals(1, property.getId());
    }

    @org.junit.Test
    public void testSet() throws Exception {
        Object instance = new Object();
        Object id = new Object();

        objectIdValueProperty.set(instance, id);

        ObjectIdValueProperty property = (ObjectIdValueProperty) findProperty(instance, "id");
        assertNotNull(property);
        assertEquals(id, property.getId());
    }

    @org.junit.Test
    public void testSetAndReturn() throws Exception {
        Object instance = new Object();
        Object id = new Object();

        Object result = objectIdValueProperty.setAndReturn(instance, id);

        assertEquals(instance, result);
        ObjectIdValueProperty property = (ObjectIdValueProperty) findProperty(instance, "id");
        assertNotNull(property);
        assertEquals(id, property.getId());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetInvalid() throws Exception {
        Object instance = new Object();

        objectIdValueProperty.set(instance, null);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnInvalid() throws Exception {
        Object instance = new Object();

        objectIdValueProperty.setAndReturn(instance, null);
    }

    private ObjectIdValueProperty findProperty(Object instance, String name) {
        ObjectIdValueProperty property = null;
        try {
            Method method = ClassUtil.findMethod(instance.getClass(), "getId", null);
            property = (ObjectIdValueProperty) method.getDeclaringClass().getDeclaredMethod("getPropertyByName", String.class).invoke(null, name);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            fail("Failed to find ObjectIdValueProperty: " + e.getMessage());
        } catch (IllegalAccessException e) {
            fail("Failed to access ObjectIdValueProperty: " + e.getMessage());
        }
        return property;
    }

    private JsonParser createParser(String jsonData) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.getFactory().createParser(jsonData);
    }
}