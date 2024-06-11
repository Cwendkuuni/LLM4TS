package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import java.io.IOException;
import java.lang.reflect.Method;

public class MethodPropertyTest {

    @Test
    public void testMethodPropertyConstruction() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", String.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new StandardAnnotationIntrospector().findAnnotation(AnnotatedMethod.class, new MethodStub());
        AnnotatedMethod method = new AnnotatedMethod(String.class.getMethod("setTestProperty", String.class));

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(methodProperty);
        assertEquals(propDef, methodProperty.getPropertyDefinition());
        assertEquals(type, methodProperty.getType());
        assertEquals(typeDeser, methodProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(method, methodProperty.getAnnotated());
        assertEquals(method.getAnnotated(), methodProperty._annotated);
        assertEquals(method.getName(), methodProperty._setter.getName());
    }

    @Test
    public void testMethodPropertyCopyConstructor() throws NoSuchMethodException {
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(String.class.getMethod("setTestProperty", String.class));
        MethodProperty source = new MethodProperty(new BeanPropertyDefinition("testProperty", String.class),
                ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class), null,
                new StandardAnnotationIntrospector().findAnnotation(AnnotatedMethod.class, annotatedMethod),
                annotatedMethod);

        Method method = new MethodStub().getMethod();
        MethodProperty target = new MethodProperty(source, method);

        assertEquals(source.getPropertyDefinition(), target.getPropertyDefinition());
        assertEquals(source.getType(), target.getType());
        assertEquals(source.getTypeDeserializer(), target.getTypeDeserializer());
        assertEquals(source.getContextAnnotations(), target.getContextAnnotations());
        assertEquals(source.getAnnotated(), target.getAnnotated());
        assertEquals(source._annotated, target._annotated);
        assertEquals(source._setter, target._setter);
        assertEquals(source._skipNulls, target._skipNulls);
    }

    @Test
    public void testMethodPropertyNameChange() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", String.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new StandardAnnotationIntrospector().findAnnotation(AnnotatedMethod.class, new MethodStub());
        AnnotatedMethod method = new AnnotatedMethod(String.class.getMethod("setTestProperty", String.class));

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(methodProperty);

        PropertyName newName = new PropertyName("newTestProperty");
        MethodProperty newMethodProperty = methodProperty.withName(newName);

        assertEquals(newName, newMethodProperty.getName());
        assertEquals(methodProperty.getPropertyDefinition(), newMethodProperty.getPropertyDefinition());
        assertEquals(methodProperty.getType(), newMethodProperty.getType());
        assertEquals(methodProperty.getTypeDeserializer(), newMethodProperty.getTypeDeserializer());
        assertEquals(methodProperty.getContextAnnotations(), newMethodProperty.getContextAnnotations());
        assertEquals(methodProperty.getAnnotated(), newMethodProperty.getAnnotated());
        assertEquals(methodProperty._annotated, newMethodProperty._annotated);
        assertEquals(methodProperty._setter, newMethodProperty._setter);
        assertEquals(methodProperty._skipNulls, newMethodProperty._skipNulls);
    }

    @Test
    public void testMethodPropertyValueDeserializerChange() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", String.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new StandardAnnotationIntrospector().findAnnotation(AnnotatedMethod.class, new MethodStub());
        AnnotatedMethod method = new AnnotatedMethod(String.class.getMethod("setTestProperty", String.class));

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(methodProperty);

        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        MethodProperty newMethodProperty = methodProperty.withValueDeserializer(valueDeserializer);

        assertEquals(methodProperty.getPropertyDefinition(), newMethodProperty.getPropertyDefinition());
        assertEquals(methodProperty.getType(), newMethodProperty.getType());
        assertEquals(methodProperty.getTypeDeserializer(), newMethodProperty.getTypeDeserializer());
        assertEquals(methodProperty.getContextAnnotations(), newMethodProperty.getContextAnnotations());
        assertEquals(methodProperty.getAnnotated(), newMethodProperty.getAnnotated());
        assertEquals(methodProperty._annotated, newMethodProperty._annotated);
        assertEquals(methodProperty._setter, newMethodProperty._setter);
        assertEquals(methodProperty._skipNulls, newMethodProperty._skipNulls);
        assertEquals(valueDeserializer, newMethodProperty._valueDeserializer);
    }

    @Test
    public void testMethodPropertyNullProviderChange() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", String.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new StandardAnnotationIntrospector().findAnnotation(AnnotatedMethod.class, new MethodStub());
        AnnotatedMethod method = new AnnotatedMethod(String.class.getMethod("setTestProperty", String.class));

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(methodProperty);

        NullValueProvider nullValueProvider = new AlwaysNullValueProvider();
        MethodProperty newMethodProperty = methodProperty.withNullProvider(nullValueProvider);

        assertEquals(methodProperty.getPropertyDefinition(), newMethodProperty.getPropertyDefinition());
        assertEquals(methodProperty.getType(), newMethodProperty.getType());
        assertEquals(methodProperty.getTypeDeserializer(), newMethodProperty.getTypeDeserializer());
        assertEquals(methodProperty.getContextAnnotations(), newMethodProperty.getContextAnnotations());
        assertEquals(methodProperty.getAnnotated(), newMethodProperty.getAnnotated());
        assertEquals(methodProperty._annotated, newMethodProperty._annotated);
        assertEquals(methodProperty._setter, newMethodProperty._setter);
        assertEquals(methodProperty._skipNulls, newMethodProperty._skipNulls);
        assertNotSame(methodProperty._nullProvider, newMethodProperty._nullProvider);
        assertEquals(nullValueProvider, newMethodProperty._nullProvider);
    }

    @Test
    public void testMethodPropertyFixAccess() throws IOException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", String.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new StandardAnnotationIntrospector().findAnnotation(AnnotatedMethod.class, new MethodStub());
        AnnotatedMethod method = new AnnotatedMethod(String.class.getMethod("setTestProperty", String.class));

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(methodProperty);

        DeserializationConfig config = new DeserializationConfig(ObjectMapper.getDefaultInstance());
        config.setBeanPropertyDefinitionVisibility(MapperFeature.VISIBILITY_ANY);
        methodProperty.fixAccess(config);

        assertTrue(methodProperty._annotated.isAccessible());
    }

    @Test
    public void testMethodPropertyGetAnnotation() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", String.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new StandardAnnotationIntrospector().findAnnotation(AnnotatedMethod.class, new MethodStub());
        AnnotatedMethod method = new AnnotatedMethod(String.class.getMethod("setTestProperty", String.class));

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(methodProperty);

        Class<MyAnnotation> annotationType = MyAnnotation.class;
        MyAnnotation annotation = method.getAnnotated().getAnnotation(annotationType);
        assertEquals(annotation, methodProperty.getAnnotation(annotationType));
    }

    @Test
    public void testMethodPropertyInvokeSetter() throws IOException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", String.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new StandardAnnotationIntrospector().findAnnotation(AnnotatedMethod.class, new MethodStub());
        AnnotatedMethod method = new AnnotatedMethod(String.class.getMethod("setTestProperty", String.class));

        Object instance = new TestInstance();
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(methodProperty);

        String value = "testValue";
        methodProperty.set(instance, value);
        assertEquals(value, instance.getTestProperty());
    }

    @Test
    public void testMethodPropertyInvokeSetterAndReturn() throws IOException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", String.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new StandardAnnotationIntrospector().findAnnotation(AnnotatedMethod.class, new MethodStub());
        AnnotatedMethod method = new AnnotatedMethod(String.class.getMethod("setTestProperty", String.class));

        Object instance = new TestInstance();
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(methodProperty);

        String value = "testValue";
        Object result = methodProperty.setAndReturn(instance, value);
        assertEquals(value, result);
        assertEquals(value, instance.getTestProperty());
    }

    @Test
    public void testMethodPropertyDeserializeAndSet() throws IOException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", String.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new StandardAnnotationIntrospector().findAnnotation(AnnotatedMethod.class, new MethodStub());
        AnnotatedMethod method = new AnnotatedMethod(String.class.getMethod("setTestProperty", String.class));

        ObjectMapper mapper = new ObjectMapper();
        Object instance = new TestInstance();

        String testJson = "\"testValue\"";
        JsonParser parser = mapper.getFactory().createParser(testJson);
        parser.nextToken();

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        methodProperty.deserializeAndSet(parser, null, instance);

        assertEquals("testValue", instance.getTestProperty());
    }

    @Test
    public void testMethodPropertyDeserializeSetAndReturn() throws IOException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", String.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new StandardAnnotationIntrospector().findAnnotation(AnnotatedMethod.class, new MethodStub());
        AnnotatedMethod method = new AnnotatedMethod(String.class.getMethod("setTestProperty", String.class));

        ObjectMapper mapper = new ObjectMapper();
        Object instance = new TestInstance();

        String testJson = "\"testValue\"";
        JsonParser parser = mapper.getFactory().createParser(testJson);
        parser.nextToken();

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        Object result = methodProperty.deserializeSetAndReturn(parser, null, instance);

        assertEquals("testValue", result);
        assertEquals("testValue", instance.getTestProperty());
    }

    @Test
    public void testMethodPropertyReadResolve() throws IOException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", String.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new StandardAnnotationIntrospector().findAnnotation(AnnotatedMethod.class, new MethodStub());
        AnnotatedMethod method = new AnnotatedMethod(String.class.getMethod("setTestProperty", String.class));

        Object instance = new TestInstance();
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method) {
            @Override
            public Object readResolve() {
                return new MethodProperty(this, method.getAnnotated());
            }
        };

        String value = "testValue";
        methodProperty.set(instance, value);
        assertEquals(value, instance.getTestProperty());

        Object readResolved = methodProperty.readResolve();
        assertEquals(methodProperty, readResolved);
        assertEquals(value, instance.getTestProperty());
    }

    static class TestInstance {
        private String testProperty;

        public String getTestProperty() {
            return testProperty;
        }

        public void setTestProperty(String testProperty) {
            this.testProperty = testProperty;
        }
    }

    static class StringDeserializer extends StdDeserializer<String> {
        public StringDeserializer() {
            this(null);
        }

        public StringDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    static class AlwaysNullValueProvider implements NullValueProvider {
        @Override
        public Object getNullValue(DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public boolean isEmpty(DeserializationContext ctxt, JsonParser p) throws IOException {
            return p.getValueAsString().isEmpty();
        }
    }

    static class MethodStub {
        static Class<?> getClass() {
            return String.class;
        }

        static AnnotatedMember getMember() {
            return _annotated;
        }

        static AnnotatedMethod getMethod() {
            return new AnnotatedMethod(String.class.getMethod("setTestProperty", String.class));
        }
    }
}