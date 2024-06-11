package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Before;
import org.junit.Test;

public class CreatorPropertyTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreatorPropertyConstruction() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(TestClass.class);
        PropertyName propertyName = PropertyName.construct("testProperty");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        Object injectableValueId = null;
        AnnotatedParameter annotatedParameter = AnnotatedParameter.construct(TestClass.class, "testMethod", null, 0);
        int creatorIndex = 0;

        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, null, typeDeser, contextAnnotations, annotatedParameter, creatorIndex, injectableValueId, null);

        assertNotNull(creatorProperty);
        assertEquals(propertyName, creatorProperty.getName());
        assertEquals(javaType, creatorProperty.getType());
        assertNull(creatorProperty.getWrapperName());
        assertNull(creatorProperty.getTypeDeserializer());
        assertNull(creatorProperty.getContextAnnotations());
        assertNull(creatorProperty.getMetadata());
        assertNull(creatorProperty.getFallbackSetter());
        assertNull(creatorProperty.getInjectableValueId());
        assertEquals(creatorIndex, creatorProperty.getCreatorIndex());
        assertEquals(annotatedParameter, creatorProperty.getAnnotated());
        assertFalse(creatorProperty.isIgnorable());
    }

    @Test
    public void testCreatorPropertyCopyConstructor() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(TestClass.class);
        PropertyName propertyName = PropertyName.construct("testProperty");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        Object injectableValueId = null;
        AnnotatedParameter annotatedParameter = AnnotatedParameter.construct(TestClass.class, "testMethod", null, 0);
        int creatorIndex = 0;

        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, null, typeDeser, contextAnnotations, annotatedParameter, creatorIndex, injectableValueId, null);

        CreatorProperty copiedCreatorProperty = new CreatorProperty(creatorProperty, propertyName);

        assertEquals(creatorProperty, copiedCreatorProperty);
        assertEquals(propertyName, copiedCreatorProperty.getName());
        assertEquals(javaType, copiedCreatorProperty.getType());
        assertNull(copiedCreatorProperty.getWrapperName());
        assertNull(copiedCreatorProperty.getTypeDeserializer());
        assertNull(copiedCreatorProperty.getContextAnnotations());
        assertNull(copiedCreatorProperty.getMetadata());
        assertNull(copiedCreatorProperty.getFallbackSetter());
        assertNull(copiedCreatorProperty.getInjectableValueId());
        assertEquals(creatorIndex, copiedCreatorProperty.getCreatorIndex());
        assertEquals(annotatedParameter, copiedCreatorProperty.getAnnotated());
        assertFalse(copiedCreatorProperty.isIgnorable());
    }

    @Test
    public void testCreatorPropertyWithValueDeserializer() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(TestClass.class);
        PropertyName propertyName = PropertyName.construct("testProperty");
        TypeDeserializer typeDeser = new CustomTypeDeserializer();
        Annotations contextAnnotations = null;
        Object injectableValueId = null;
        AnnotatedParameter annotatedParameter = AnnotatedParameter.construct(TestClass.class, "testMethod", null, 0);
        int creatorIndex = 0;

        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, null, typeDeser, contextAnnotations, annotatedParameter, creatorIndex, injectableValueId, null);

        JsonDeserializer<String> deserializer = new JsonStringDeserializer();
        CreatorProperty copiedCreatorProperty = new CreatorProperty(creatorProperty, deserializer, null);

        assertEquals(creatorProperty, copiedCreatorProperty);
        assertEquals(propertyName, copiedCreatorProperty.getName());
        assertEquals(javaType, copiedCreatorProperty.getType());
        assertNull(copiedCreatorProperty.getWrapperName());
        assertEquals(typeDeser, copiedCreatorProperty.getTypeDeserializer());
        assertNull(copiedCreatorProperty.getContextAnnotations());
        assertNull(copiedCreatorProperty.getMetadata());
        assertNull(copiedCreatorProperty.getFallbackSetter());
        assertNull(copiedCreatorProperty.getInjectableValueId());
        assertEquals(creatorIndex, copiedCreatorProperty.getCreatorIndex());
        assertEquals(annotatedParameter, copiedCreatorProperty.getAnnotated());
        assertFalse(copiedCreatorProperty.isIgnorable());
    }

    @Test
    public void testCreatorPropertyWithNullProvider() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(TestClass.class);
        PropertyName propertyName = PropertyName.construct("testProperty");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        Object injectableValueId = null;
        AnnotatedParameter annotatedParameter = AnnotatedParameter.construct(TestClass.class, "testMethod", null, 0);
        int creatorIndex = 0;

        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, null, typeDeser, contextAnnotations, annotatedParameter, creatorIndex, injectableValueId, null);

        NullValueProvider nullValueProvider = new CustomNullValueProvider();
        CreatorProperty copiedCreatorProperty = new CreatorProperty(creatorProperty, null, nullValueProvider);

        assertEquals(creatorProperty, copiedCreatorProperty);
        assertEquals(propertyName, copiedCreatorProperty.getName());
        assertEquals(javaType, copiedCreatorProperty.getType());
        assertNull(copiedCreatorProperty.getWrapperName());
        assertNull(copiedCreatorProperty.getTypeDeserializer());
        assertNull(copiedCreatorProperty.getContextAnnotations());
        assertNull(copiedCreatorProperty.getMetadata());
        assertNull(copiedCreatorProperty.getFallbackSetter());
        assertNull(copiedCreatorProperty.getInjectableValueId());
        assertEquals(creatorIndex, copiedCreatorProperty.getCreatorIndex());
        assertEquals(annotatedParameter, copiedCreatorProperty.getAnnotated());
        assertFalse(copiedCreatorProperty.isIgnorable());
        assertEquals(nullValueProvider, copiedCreatorProperty.getNullProvider());
    }

    @Test
    public void testIgnorableProperty() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(TestClass.class);
        PropertyName propertyName = PropertyName.construct("testProperty");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        Object injectableValueId = null;
        AnnotatedParameter annotatedParameter = AnnotatedParameter.construct(TestClass.class, "testMethod", null, 0);
        int creatorIndex = 0;

        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, null, typeDeser, contextAnnotations, annotatedParameter, creatorIndex, injectableValueId, null);
        creatorProperty.markAsIgnorable();

        CreatorProperty copiedCreatorProperty = new CreatorProperty(creatorProperty, propertyName);

        assertFalse(creatorProperty.isIgnorable());
        assertTrue(copiedCreatorProperty.isIgnorable());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFallbackSetterNull() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(TestClass.class);
        PropertyName propertyName = PropertyName.construct("testProperty");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        Object injectableValueId = null;
        AnnotatedParameter annotatedParameter = AnnotatedParameter.construct(TestClass.class, "testMethod", null, 0);
        int creatorIndex = 0;

        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, null, typeDeser, contextAnnotations, annotatedParameter, creatorIndex, injectableValueId, null);
        creatorProperty.setFallbackSetter(null);
    }

    @Test
    public void testFindInjectableValue() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Object instance = objectMapper.readValue(getClass().getResource("/test-data.json"), TestClass.class);
        JavaType javaType = objectMapper.getTypeFactory().constructType(TestClass.class);
        PropertyName propertyName = PropertyName.construct("testProperty");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        Object injectableValueId = "testValueId";
        AnnotatedParameter annotatedParameter = AnnotatedParameter.construct(TestClass.class, "testMethod", null, 0);
        int creatorIndex = 0;

        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, null, typeDeser, contextAnnotations, annotatedParameter, creatorIndex, injectableValueId, null);

        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        Object injectedValue = creatorProperty.findInjectableValue(ctxt, instance);
        assertEquals("testValue", injectedValue);
    }

    @Test
    public void testInjectValue() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Object instance = objectMapper.readValue(getClass().getResource("/test-data.json"), TestClass.class);
        JavaType javaType = objectMapper.getTypeFactory().constructType(TestClass.class);
        PropertyName propertyName = PropertyName.construct("testProperty");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        Object injectableValueId = "testValueId";
        AnnotatedParameter annotatedParameter = AnnotatedParameter.construct(TestClass.class, "testMethod", null, 0);
        int creatorIndex = 0;

        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, null, typeDeser, contextAnnotations, annotatedParameter, creatorIndex, injectableValueId, null);

        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        creatorProperty.inject(ctxt, instance);

        JsonParser p = objectMapper.getFactory().createParser(getClass().getResource("/test-data.json").toString());
        Object readValue = p.readValueAs(TestClass.class);
        assertEquals(instance, readValue);
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingSetter() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Object instance = objectMapper.readValue(getClass().getResource("/test-data.json"), TestClass.class);
        JavaType javaType = objectMapper.getTypeFactory().constructType(TestClass.class);
        PropertyName propertyName = PropertyName.construct("missingSetterProperty");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        Object injectableValueId = null;
        AnnotatedParameter annotatedParameter = AnnotatedParameter.construct(TestClass.class, "testMethod", null, 0);
        int creatorIndex = 0;

        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, null, typeDeser, contextAnnotations, annotatedParameter, creatorIndex, injectableValueId, null);

        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        creatorProperty.deserializeAndSet(ctxt.getParser(), ctxt, instance);
    }

    static class TestClass {

        @JsonCreator
        public TestClass(String testProperty, @JsonProperty("testProperty2") Integer testProperty2) {
            this.testProperty = testProperty;
            this.testProperty2 = testProperty2;
        }

        private String testProperty;
        private Integer testProperty2;

        public static class CustomTypeDeserializer extends StdTypeDeserializer<TestClass> {
            public CustomTypeDeserializer() { this(null); }

            protected CustomTypeDeserializer(Class<?> vc) { super(vc); }

            @Override
            public TestClass deserializeWithType(DeserializationContext ctxt, JsonParser p,
                    @Nullable Object beanProperty) throws IOException {
                TestClass testClass = new TestClass(p.getValueAsString(), null);
                ctxt.findBeanProperty("testProperty2", TestClass.class, null, testClass, null);
                return testClass;
            }
        }

        @JsonProperty
        public void setTestProperty2(Integer testProperty2) {
            this.testProperty2 = testProperty2;
        }

        public String getTestProperty() {
            return testProperty;
        }

        public Integer getTestProperty2() {
            return testProperty2;
        }

        @JsonProperty(value = "testProperty")
        public String testProperty;

        @JsonProperty(value = "testProperty2")
        public Integer testProperty2;
    }

    static class CustomNullValueProvider implements NullValueProvider {
        @Override
        public Object getNullValue(DeserializationContext ctxt, JavaType type) {
            return null;
        }
    }

    static class JsonStringDeserializer extends StdDeserializer<String> {
        public JsonStringDeserializer() { this(null); }

        protected JsonStringDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }
}