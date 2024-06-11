package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FieldPropertyTest {

    private static final Object TEST_INSTANCE = new Object();

    @Test
    public void testConstructors() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new AnnotatedField(new Field(TestClass.class, "testField"), new Annotation[]{}), "testField", null, null, null, null, null, null);
        JavaType type = ObjectType.getInstance(TestClass.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField field = new AnnotatedField(new Field(TestClass.class, "testField"), new Annotation[]{});

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        FieldProperty fieldProperty2 = new FieldProperty(fieldProperty, new JsonDeserializer<>(){}, new NullValueProvider.Std() {});
        FieldProperty fieldProperty3 = new FieldProperty(fieldProperty, new PropertyName("testName"));
        FieldProperty fieldProperty4 = new FieldProperty(fieldProperty, new JsonDeserializer<>(){}, null);

        FieldProperty fieldProperty5 = new FieldProperty(fieldProperty4);

        assertEquals(fieldProperty, fieldProperty2);
        assertEquals(fieldProperty2, fieldProperty3);
        assertEquals(fieldProperty3, fieldProperty5);
    }

    @Test
    public void testSetterMethods() throws IOException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new AnnotatedField(new Field(TestClass.class, "testField"), new Annotation[]{}), "testField", null, null, null, null, null, null);
        JavaType type = ObjectType.getInstance(TestClass.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField field = new AnnotatedField(new Field(TestClass.class, "testField"), new Annotation[]{});

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        Object value = new Object();

        Object instance = TEST_INSTANCE;

        fieldProperty.set(instance, value);
        assertEquals(value, fieldProperty.get(instance));

        assertEquals(instance, fieldProperty.getBean());

        Object nullValue = null;
        fieldProperty.set(instance, nullValue);
        assertNull(fieldProperty.get(instance));

        fieldProperty.fixAccess(new DeserializationConfig());

        JsonParser mockParser = new MockJsonParser();
        mockParser.setCurrentName("testName");
        mockParser.nextToken(JsonToken.VALUE_NULL);
        fieldProperty.deserializeAndSet(mockParser, new DeserializationContext(new ObjectMapper()), instance);
        assertNull(fieldProperty.get(instance));

        mockParser.nextToken(JsonToken.START_OBJECT);
        mockParser.nextToken(JsonToken.FIELD_NAME);
        String testName = "testName";
        mockParser.setValue(testName);
        mockParser.nextToken(JsonToken.VALUE_STRING);
        mockParser.setValue("testValue");
        fieldProperty.deserializeAndSet(mockParser, new DeserializationContext(new ObjectMapper()), instance);
        assertEquals("testValue", fieldProperty.get(instance));
    }

    @Test
    public void testDeserializationWithNullValueProvider() throws IOException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new AnnotatedField(new Field(TestClass.class, "testField"), new Annotation[]{}), "testField", null, null, null, null, null, null);
        JavaType type = ObjectType.getInstance(TestClass.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField field = new AnnotatedField(new Field(TestClass.class, "testField"), new Annotation[]{});

        NullValueProvider nullValueProvider = new NullValueProvider.Std();
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        Object instance = TEST_INSTANCE;

        JsonParser mockParser = new MockJsonParser();
        mockParser.nextToken(JsonToken.VALUE_NULL);
        fieldProperty.deserializeAndSet(mockParser, new DeserializationContext(new ObjectMapper()), instance);
        assertNull(fieldProperty.get(instance));

        mockParser.nextToken(JsonToken.START_ARRAY);
        fieldProperty.deserializeAndSet(mockParser, new DeserializationContext(new ObjectMapper()), instance);
        assertNull(fieldProperty.get(instance));

        mockParser.nextToken(JsonToken.VALUE_NULL);
        fieldProperty.deserializeAndSet(mockParser, new DeserializationContext(new ObjectMapper()), instance);
        assertNull(fieldProperty.get(instance));

        fieldProperty.setNullProvider(nullValueProvider);

        mockParser.nextToken(JsonToken.VALUE_NULL);
        fieldProperty.deserializeAndSet(mockParser, new DeserializationContext(new ObjectMapper()), instance);
        assertNull(fieldProperty.get(instance));
    }

    @Test
    public void testDeserializationWithTypeDeserializer() throws IOException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new AnnotatedField(new Field(TestClass.class, "testField"), new Annotation[]{}), "testField", null, null, null, null, null, null);
        JavaType type = ObjectType.getInstance(TestClass.class);
        TypeDeserializer typeDeser = new SimpleTypeDeserializer();
        Annotations contextAnnotations = null;
        AnnotatedField field = new AnnotatedField(new Field(TestClass.class, "testField"), new Annotation[]{});

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        Object instance = TEST_INSTANCE;

        JsonParser mockParser = new MockJsonParser();
        mockParser.nextToken(JsonToken.VALUE_STRING);
        mockParser.setValue("testValue");
        Object deserializedValue = fieldProperty.deserializeSetAndReturn(mockParser, new DeserializationContext(new ObjectMapper()), instance);
        assertEquals("testValue", fieldProperty.get(instance));
        assertEquals(deserializedValue, fieldProperty.get(instance));
    }

    @Test
    public void testValueDeserializerWithNullValueProvider() throws IOException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new AnnotatedField(new Field(TestClass.class, "testField"), new Annotation[]{}), "testField", null, null, null, null, null, null);
        JavaType type = ObjectType.getInstance(TestClass.class);
        TypeDeserializer typeDeser = new JsonDeserializer<Object>() {};
        Annotations contextAnnotations = null;
        AnnotatedField field = new AnnotatedField(new Field(TestClass.class, "testField"), new Annotation[]{});

        NullValueProvider nullValueProvider = new NullValueProvider.Std();
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        Object instance = TEST_INSTANCE;

        JsonParser mockParser = new MockJsonParser();
        mockParser.nextToken(JsonToken.VALUE_NULL);
        fieldProperty.deserializeAndSet(mockParser, new DeserializationContext(new ObjectMapper()), instance);
        assertNull(fieldProperty.get(instance));

        mockParser.nextToken(JsonToken.START_ARRAY);
        fieldProperty.deserializeAndSet(mockParser, new DeserializationContext(new ObjectMapper()), instance);
        assertNull(fieldProperty.get(instance));

        mockParser.nextToken(JsonToken.VALUE_NULL);
        fieldProperty.deserializeAndSet(mockParser, new DeserializationContext(new ObjectMapper()), instance);
        assertNull(fieldProperty.get(instance));

        fieldProperty.setNullProvider(nullValueProvider);

        mockParser.nextToken(JsonToken.VALUE_NULL);
        fieldProperty.deserializeAndSet(mockParser, new DeserializationContext(new ObjectMapper()), instance);
        assertNull(fieldProperty.get(instance));

        mockParser.nextToken(JsonToken.VALUE_STRING);
        mockParser.setValue("testValue");
        fieldProperty.deserializeAndSet(mockParser, new DeserializationContext(new ObjectMapper()), instance);
        assertEquals("testValue", fieldProperty.get(instance));
    }

    @Test
    public void testFixAccess() throws IOException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new AnnotatedField(new Field(TestClass.class, "testField"), new Annotation[]{}), "testField", null, null, null, null, null, null);
        JavaType type = ObjectType.getInstance(TestClass.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField field = new AnnotatedField(new Field(TestClass.class, "testField"), new Annotation[]{});

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        Object instance = TEST_INSTANCE;

        fieldProperty.fixAccess(new DeserializationConfig());
        fieldProperty.set(instance, new Object());
        assertEquals(new Object(), fieldProperty.get(instance));
    }
}

class TestClass {
    private String testField;
}

class SimpleTypeDeserializer extends TypeDeserializer {
    @Override
    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vd) throws IOException {
        return p.getValueAsString();
    }
}

class MockJsonParser extends JsonParser {
    private JsonToken currentToken = JsonToken.START_OBJECT;

    public JsonToken nextToken() throws IOException {
        return currentToken;
    }

    public void nextValue() throws IOException {
        // empty
    }

    public void setCurrentName(String name) {
        // empty
    }
}