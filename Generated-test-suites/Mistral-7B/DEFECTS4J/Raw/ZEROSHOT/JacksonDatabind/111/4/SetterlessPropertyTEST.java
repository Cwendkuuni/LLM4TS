package com.yourpackage; // replace with your testing package

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.SetterlessProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.lang.reflect.Method;

public class SetterlessPropertyTest extends TestBase {

    private static final Class<?> CLASS_UNDER_TEST = SetterlessProperty.class;

    @SuppressWarnings("unchecked")
    private static SetterlessProperty createProperty(String propertyName, Method getter) {
        return new SetterlessProperty(null, null, null, null, new AnnotatedMethod(getter)) {
            @Override
            public Method getGetMethod() {
                return getter;
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static SetterlessProperty createProperty(String propertyName, Method getter, TypeDeserializer typeDeserializer) {
        return new SetterlessProperty(null, null, typeDeserializer, null, new AnnotatedMethod(getter)) {
            @Override
            public Method getGetMethod() {
                return getter;
            }
        };
    }

    @Test
    public void testCreateProperty() {
        String propertyName = "testProperty";
        Method getter = getClass().getMethod(propertyName);
        SetterlessProperty property = createProperty(propertyName, getter);

        assertEquals(propertyName, property.getName());
        assertEquals(getter, property.getGetMethod());
    }

    @Test
    public void testCreatePropertyWithTypeDeserializer() {
        String propertyName = "testProperty";
        Method getter = getClass().getMethod(propertyName);
        TypeDeserializer typeDeserializer = null; // replace with appropriate TypeDeserializer
        SetterlessProperty property = createProperty(propertyName, getter, typeDeserializer);

        assertEquals(propertyName, property.getName());
        assertEquals(getter, property.getGetMethod());
        assertEquals(typeDeserializer, property.getTypeDeserializer());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        String propertyName = "testProperty";
        Method getter = getClass().getMethod(propertyName);
        Object testInstance = new Object();

        SetterlessProperty property = createProperty(propertyName, getter);
        JsonParser p = createParser("{'testProperty': 'testValue'}");

        property.deserializeAndSet(p, context(), testInstance);

        Object propertyValue = getter.invoke(testInstance);
        assertEquals("testValue", propertyValue);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        String propertyName = "testProperty";
        Method getter = getClass().getMethod(propertyName);
        Object testInstance = new Object();

        SetterlessProperty property = createProperty(propertyName, getter);
        JsonParser p = createParser("{'testProperty': 'testValue'}");

        property.deserializeSetAndReturn(p, context(), testInstance);
        Object propertyValue = getter.invoke(testInstance);
        assertEquals("testValue", propertyValue);
        assertEquals(testInstance, property.getInstance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        String propertyName = "testProperty";
        Method getter = getClass().getMethod(propertyName);
        Object testInstance = new Object();

        SetterlessProperty property = createProperty(propertyName, getter);
        property.set(testInstance, "testValue");
        fail("Should not reach this point as SetterlessProperty's 'set' method is unsupported.");
    }

    @Test
    public void testSetAndReturn() throws IOException {
        String propertyName = "testProperty";
        Method getter = getClass().getMethod(propertyName);
        Object testInstance = new Object();

        SetterlessProperty property = createProperty(propertyName, getter);
        Object returnValue = property.setAndReturn(testInstance, "testValue");

        assertEquals(testInstance, returnValue);
        Object propertyValue = getter.invoke(testInstance);
        assertEquals("testValue", propertyValue);
    }

    @Test
    public void testGetAnnotation() {
        String propertyName = "testProperty";
        Method getter = getClass().getMethod(propertyName);
        Annotation annotation = getClass().getAnnotation(propertyName);

        SetterlessProperty property = createProperty(propertyName, getter);
        assertEquals(annotation, property.getAnnotation(annotation.getClass()));
    }

    @Test
    public void testGetMember() {
        String propertyName = "testProperty";
        Method getter = getClass().getMethod(propertyName);

        SetterlessProperty property = createProperty(propertyName, getter);
        assertEquals(new AnnotatedMethod(getter), property.getMember());
    }

    @Test
    public void testFixAccess() {
        String propertyName = "testProperty";
        Method getter = getClass().getMethod(propertyName);

        SetterlessProperty property = createProperty(propertyName, getter);
        property.fixAccess(context().getDefaultConfig());
        assertTrue(getter.isAccessible());
    }
}