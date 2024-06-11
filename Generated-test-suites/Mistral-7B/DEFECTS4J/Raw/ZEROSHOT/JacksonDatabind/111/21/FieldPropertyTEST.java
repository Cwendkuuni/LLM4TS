package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;

public class FieldPropertyTest {

    private ObjectTestObject testObject;
    private JacksonAnnotation testAnnotation;

    @Before
    public void setup() {
        testObject = new ObjectTestObject();
        testAnnotation = new JacksonAnnotation();
        try {
            Field field = ObjectTestObject.class.getDeclaredField("testField");
            field.setAccessible(true);
            FieldProperty property = new FieldProperty(
                    new BeanPropertyDefinition(new PropertyName("testField"), new TypeReference<Integer>() {}, null, null),
                    new JavaType(Integer.class), null, Annotations.empty(), new AnnotatedField(new Annotation[] { testAnnotation }, "testField", null, null, null, null));
            property._field = field;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstructor() {
        FieldProperty property = new FieldProperty(null, null, null, null, null);
        assertEquals(null, property._annotated);
        assertEquals(null, property._field);
        assertEquals(false, property._skipNulls);

        FieldProperty property2 = new FieldProperty(null, null, null, null, new AnnotatedField(new Annotation[] { testAnnotation }, "testField", null, null, null, null));
        assertEquals(testAnnotation, property2._annotated.getAnnotation(JacksonAnnotation.class));
        assertEquals(null, property2._field);
        assertEquals(false, property2._skipNulls);

        FieldProperty property3 = new FieldProperty(
                new BeanPropertyDefinition(new PropertyName("testField"), new TypeReference<Integer>() {}, null, null),
                new JavaType(Integer.class), null, Annotations.empty(), new AnnotatedField(new Annotation[] { testAnnotation }, "testField", null, null, null, null));
        assertEquals(testAnnotation, property3._annotated.getAnnotation(JacksonAnnotation.class));
        assertEquals("testField", property3.getName().asString());
        assertEquals(Integer.class, property3.getType());
        assertTrue(property3._skipNulls == false);

        FieldProperty property4 = new FieldProperty(property3, new PropertyName("testField2"));
        assertEquals("testField2", property4.getName().asString());
        assertEquals(Integer.class, property4.getType());
        assertEquals(testAnnotation, property4._annotated.getAnnotation(JacksonAnnotation.class));
        assertEquals(property3, property4._previousVersion);
    }

    @Test
    public void testSetValue() throws IOException {
        String json = "{\"testField\": 1}";
        JsonParser parser = new JsonFactory().createParser(new StringReader(json));

        ObjectReader reader = new ObjectMapper().readerFor(ObjectTestObject.class);
        Object readValue = reader.readValue(parser);
        assertEquals(1, testObject.getTestField());

        property.deserializeAndSet(parser, new DeserializationContext(), testObject);
        assertEquals(1, testObject.getTestField());

        json = "{\"testField\": null}";
        parser = new JsonFactory().createParser(new StringReader(json));
        if (property._skipNulls) {
            assertNull(property.deserializeAndSet(parser, new DeserializationContext(), testObject));
        } else {
            assertEquals(0, testObject.getTestField());
        }

        property.set(testObject, "testString");
        assertEquals("testString", testObject.getTestField());
    }

    @Test
    public void testSetValueAndReturn() throws IOException {
        String json = "{\"testField\": 1}";
        JsonParser parser = new JsonFactory().createParser(new StringReader(json));

        Object readValue = new ObjectMapper().readValue(new StringReader(json), ObjectTestObject.class);
        assertEquals(1, readValue.getTestField());

        Object result = property.deserializeSetAndReturn(parser, new DeserializationContext(), testObject);
        assertEquals(testObject, result);
        assertEquals(1, testObject.getTestField());

        json = "{\"testField\": null}";
        parser = new JsonFactory().createParser(new StringReader(json));
        if (property._skipNulls) {
            assertNull(property.deserializeSetAndReturn(parser, new DeserializationContext(), testObject));
            assertEquals(0, testObject.getTestField());
        } else {
            assertEquals(0, testObject.getTestField());
            Object result2 = property.deserializeSetAndReturn(parser, new DeserializationContext(), testObject);
            assertEquals(testObject, result2);
            assertEquals(0, testObject.getTestField());
        }

        property.set(testObject, "testString");
        assertEquals("testString", testObject.getTestField());
        Object result3 = property.setAndReturn(testObject, "testString2");
        assertEquals(testObject, result3);
        assertEquals("testString2", testObject.getTestField());
    }
}

class ObjectTestObject {
    private int testField;

    public int getTestField() {
        return testField;
    }

    public void setTestField(int testField) {
        this.testField = testField;
    }
}

class JacksonAnnotation {
}