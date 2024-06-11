package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyContextTypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class CreatorPropertyTest {

    @SuppressWarnings("unchecked")
    @JsonCreator
    public static class TestClass {
        private final int creatorIndex;
        private final String name;
        private final CreatorProperty creatorProperty;

        @JsonProperty("creatorIndex")
        public int getCreatorIndex() { return creatorIndex; }

        @JsonProperty("name")
        public String getName() { return name; }

        public CreatorProperty getCreatorProperty() { return creatorProperty; }

        public TestClass(int creatorIndex, String name, CreatorProperty creatorProperty) {
            this.creatorIndex = creatorIndex;
            this.name = name;
            this.creatorProperty = creatorProperty;
        }
    }

    @Test
    public void testConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AnnotationContext context = mapper.getSerializationConfig().getDefaultAnnotationIntrospector();
        BeanDescription beanDescription = mapper.findBeanDescription(TestClass.class);
        JsonFactory jsonFactory = mapper.getFactory();

        JsonParser p = jsonFactory.getParser();
        p.readValueAsTree("{\"creatorIndex\":1, \"name\":\"testName\", \"creatorProperty\":{\"name\":\"testName2\", \"type\":\"java.lang.String\",\"creatorIndex\":2}}");

        TestClass testClass = mapper.readValue(p, TestClass.class);
        CreatorProperty creatorProperty = testClass.getCreatorProperty();

        assertEquals(1, creatorProperty.getCreatorIndex());
        assertEquals("testName", creatorProperty.getName());
        assertNotNull(creatorProperty._annotated);
        assertNull(creatorProperty._fallbackSetter);
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingInjectableValueId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AnnotationContext context = mapper.getSerializationConfig().getDefaultAnnotationIntrospector();
        BeanDescription beanDescription = mapper.findBeanDescription(TestClass.class);
        JsonFactory jsonFactory = mapper.getFactory();

        JsonParser p = jsonFactory.getParser();
        p.readValueAsTree("{\"creatorIndex\":1, \"name\":\"testName\", \"creatorProperty\":{\"name\":\"testName2\"}}");

        TestClass testClass = mapper.readValue(p, TestClass.class);
        CreatorProperty creatorProperty = testClass.getCreatorProperty();

        DeserializationContext ctxt = mapper.getDeserializationContext();
        creatorProperty.inject(ctxt, testClass);
    }

    @Test
    public void testInjectableValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AnnotationContext context = mapper.getSerializationConfig().getDefaultAnnotationIntrospector();
        BeanDescription beanDescription = mapper.findBeanDescription(TestClass.class);
        JsonFactory jsonFactory = mapper.getFactory();

        JsonParser p = jsonFactory.getParser();
        ObjectNode node = p.readValueAsTree("{\"creatorIndex\":1, \"name\":\"testName\", \"creatorProperty\":{\"name\":\"testName2\", \"type\":\"java.lang.String\",\"injectableValueId\":\"injectId\", \"value\":\"testValue\"}}");

        TestClass testClass = mapper.readValue(p, TestClass.class);
        CreatorProperty creatorProperty = testClass.getCreatorProperty();

        DeserializationContext ctxt = mapper.getDeserializationContext();
        creatorProperty.inject(ctxt, testClass);

        assertEquals("testValue", creatorProperty.getName());
    }

    @Test(expected = IOException.class)
    public void testMissingSetter() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AnnotationContext context = mapper.getSerializationConfig().getDefaultAnnotationIntrospector();
        BeanDescription beanDescription = mapper.findBeanDescription(TestClass.class);
        JsonFactory jsonFactory = mapper.getFactory();

        JsonParser p = jsonFactory.getParser();
        ObjectNode node = p.readValueAsTree("{\"creatorIndex\":1, \"name\":\"testName\", \"creatorProperty\":{\"name\":\"testName2\", \"type\":\"java.lang.String\"}}");

        TestClass testClass = mapper.readValue(p, TestClass.class);
        CreatorProperty creatorProperty = testClass.getCreatorProperty();

        Object instance = new Object();
        creatorProperty.setFallbackSetter(null);

        creatorProperty.inject(null, instance);
    }

    @Test
    public void testIsIgnorable() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AnnotationContext context = mapper.getSerializationConfig().getDefaultAnnotationIntrospector();
        BeanDescription beanDescription = mapper.findBeanDescription(TestClass.class);
        JsonFactory jsonFactory = mapper.getFactory();

        JsonParser p = jsonFactory.getParser();
        ObjectNode node = p.readValueAsTree("{\"creatorIndex\":1, \"name\":\"testName\", \"creatorProperty\":{\"name\":\"testName2\", \"type\":\"java.lang.String\", \"ignorable\":true}}");

        TestClass testClass = mapper.readValue(p, TestClass.class);
        CreatorProperty creatorProperty = testClass.getCreatorProperty();

        assertTrue(creatorProperty.isIgnorable());

        Object instance = new Object();
        creatorProperty.set(instance, "testValue");
        assertEquals("testValue", creatorProperty.getName());
    }

    @Test
    public void testSetter() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AnnotationContext context = mapper.getSerializationConfig().getDefaultAnnotationIntrospector();
        BeanDescription beanDescription = mapper.findBeanDescription(TestClass.class);
        JsonFactory jsonFactory = mapper.getFactory();

        JsonParser p = jsonFactory.getParser();
        ObjectNode node = p.readValueAsTree("{\"creatorIndex\":1, \"name\":\"testName\", \"creatorProperty\":{\"name\":\"testName2\", \"type\":\"java.lang.String\"}}");

        TestClass testClass = mapper.readValue(p, TestClass.class);
        CreatorProperty creatorProperty = testClass.getCreatorProperty();

        Object instance = new Object();
        creatorProperty.setFallbackSetter(new StdScalarProperty(new JavaClass(String.class), null, null, null, null, null, null, null, null, null, null, null, EmptyContextTypeDeserializer.instance, null, null, null, null, null, null, null, null, null));

        assertEquals(null, creatorProperty.getName());

        String value = "testValue";
        creatorProperty.set(instance, value);
        assertEquals(value, creatorProperty.getName());
    }
}