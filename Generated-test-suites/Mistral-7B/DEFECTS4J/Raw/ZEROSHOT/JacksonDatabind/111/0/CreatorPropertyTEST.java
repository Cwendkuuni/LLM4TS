package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

import com.fasterxml.jackson.databind.deser.CreatorProperty;

public class CreatorPropertyTest {

    @SuppressWarnings("unchecked")
    private static JavaType createType(Class<?> cls, PropertyName name, JavaType type) {
        return (JavaType) new BeanDescription.StandardBeanDescription(cls)
                .findProperty(name)
                .getType();
    }

    // Test constructor
    @org.junit.Test
    public void testCreatorPropertyConstructor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = createType(TestBean.class, name, null);
        Annotations contextAnnotations = Annotations.empty();

        AnnotatedParameter param = Annotations.createParameterAnnotation(TestBean.class, "testProperty");
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, 0, null, null);

        assertEquals(name, property.getName());
        assertEquals(type, property.getType());
        assertEquals(null, property.getWrapperName());
        assertEquals(null, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertNull(property._annotated);
        assertEquals(0, property._creatorIndex);
        assertNull(property._injectableValueId);
        assertNull(property._fallbackSetter);
    }

    // Test copy constructor
    @org.junit.Test
    public void testCreatorPropertyCopyConstructor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = createType(TestBean.class, name, null);
        Annotations contextAnnotations = Annotations.empty();

        AnnotatedParameter param = Annotations.createParameterAnnotation(TestBean.class, "testProperty");
        CreatorProperty srcProperty = new CreatorProperty(name, type, null, null, contextAnnotations, param, 0, null, null);

        CreatorProperty copyProperty = new CreatorProperty(srcProperty, name);

        assertEquals(name, copyProperty.getName());
        assertEquals(type, copyProperty.getType());
        assertEquals(null, copyProperty.getWrapperName());
        assertEquals(null, copyProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, copyProperty.getContextAnnotations());
        assertSame(param, copyProperty._annotated);
        assertEquals(0, copyProperty._creatorIndex);
        assertNull(copyProperty._injectableValueId);
        assertNull(copyProperty._fallbackSetter);
    }

    // Test constructor with TypeDeserializer
    @org.junit.Test
    public void testCreatorPropertyConstructorWithTypeDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = createType(TestBean.class, name, null);
        Annotations contextAnnotations = Annotations.empty();

        AnnotatedParameter param = Annotations.createParameterAnnotation(TestBean.class, "testProperty");
        CreatorProperty property = new CreatorProperty(name, type, null, new StdTypeDeserializer(), contextAnnotations, param, 0, null, null);

        assertEquals(name, property.getName());
        assertEquals(type, property.getType());
        assertEquals(null, property.getWrapperName());
        assertSame(new StdTypeDeserializer(), property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertNull(property._annotated);
        assertEquals(0, property._creatorIndex);
        assertNull(property._injectableValueId);
        assertNull(property._fallbackSetter);
    }

    // Test setFallbackSetter method
    @org.junit.Test
    public void testSetFallbackSetter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = createType(TestBean.class, name, null);
        Annotations contextAnnotations = Annotations.empty();

        AnnotatedParameter param = Annotations.createParameterAnnotation(TestBean.class, "testProperty");
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, 0, null, null);

        SettableBeanProperty fallbackSetter = Annotations.createMethodProperty(TestBean.class, "setTestProperty");
        property.setFallbackSetter(fallbackSetter);

        assertEquals(name, property.getName());
        assertEquals(type, property.getType());
        assertEquals(null, property.getWrapperName());
        assertEquals(null, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertNull(property._annotated);
        assertEquals(0, property._creatorIndex);
        assertNull(property._injectableValueId);
        assertEquals(fallbackSetter, property._fallbackSetter);
    }

    // Test isIgnorable method
    @org.junit.Test
    public void testIsIgnorable() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = createType(TestBean.class, name, null);
        Annotations contextAnnotations = Annotations.empty();

        AnnotatedParameter param = Annotations.createParameterAnnotation(TestBean.class, "testProperty");
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, 0, null, null);

        property.markAsIgnorable();
        assertTrue(property.isIgnorable());

        property.setIgnorable(false);
        assertFalse(property.isIgnorable());
    }

    // Test findInjectableValue method
    @org.junit.Test
    public void testFindInjectableValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = createType(TestBean.class, name, null);
        Annotations contextAnnotations = Annotations.empty();

        AnnotatedParameter param = Annotations.createParameterAnnotation(TestBean.class, "testProperty");
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, 0, null, null);

        DeserializationContext context = mapper.getConfig().getContextInstance();
        TestBean beanInstance = new TestBean();

        Object injectableValue = property.findInjectableValue(context, beanInstance);
        assertNull(injectableValue);

        TestInjectableBean injectableBean = new TestInjectableBean();
        context.injectValues(injectableBean);
        injectableValue = property.findInjectableValue(context, beanInstance);
        assertSame(injectableBean, injectableValue);
    }

    // Test inject method
    @org.junit.Test
    public void testInject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = createType(TestBean.class, name, null);
        Annotations contextAnnotations = Annotations.empty();

        AnnotatedParameter param = Annotations.createParameterAnnotation(TestBean.class, "testProperty");
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, 0, null, null);

        DeserializationContext context = mapper.getConfig().getContextInstance();
        TestBean beanInstance = new TestBean();

        Object injectableValue = property.findInjectableValue(context, beanInstance);
        property.inject(context, beanInstance);
        assertSame(injectableValue, beanInstance.testProperty);
    }

    // Test deserializeAndSet method
    @org.junit.Test
    public void testDeserializeAndSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = createType(TestBean.class, name, null);
        Annotations contextAnnotations = Annotations.empty();

        AnnotatedParameter param = Annotations.createParameterAnnotation(TestBean.class, "testProperty");
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, 0, null, null);

        JsonParser p = mapper.getFactory().createParser("{\"testProperty\":1}");
        DeserializationContext context = mapper.getConfig().getContextInstance();
        TestBean beanInstance = new TestBean();

        property.deserializeAndSet(p, context, beanInstance);
        assertEquals(1, beanInstance.testProperty);
    }

    // Test setter verification
    @org.junit.Test(expected = IOException.class)
    public void testMissingSetter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = createType(TestBean.class, name, null);
        Annotations contextAnnotations = Annotations.empty();

        AnnotatedParameter param = Annotations.createParameterAnnotation(TestBean.class, "testProperty");
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, 0, null, null);

        JsonParser p = mapper.getFactory().createParser("{\"testProperty\":1}");
        DeserializationConfig config = mapper.getConfig();
        config.setNullValueProvider(new None());
        DeserializationContext context = config.getContextInstance();
        TestBean beanInstance = new TestBean();

        property.deserializeAndSet(p, context, beanInstance);
    }

    // Test getter
    @org.junit.Test
    public void testToString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = createType(TestBean.class, name, null);
        Annotations contextAnnotations = Annotations.empty();

        AnnotatedParameter param = Annotations.createParameterAnnotation(TestBean.class, "testProperty");
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, 0, null, null);

        String expected = "[creator property, name 'testProperty'; inject id null]";
        assertEquals(expected, property.toString());

        property = new CreatorProperty(name, type, null, null, contextAnnotations, param, 0, null, null);
        property.markAsIgnorable();
        expected = "[creator property, name 'testProperty'; inject id null, ignorable true]";
        assertEquals(expected, property.toString());

        JavaType typeWithAnnotation = createType(TestBeanWithAnnotation.class, name, null);
        property = new CreatorProperty(name, typeWithAnnotation, null, null, contextAnnotations, param, 0, null, null);
        expected = "[creator property, name 'testProperty'; inject id null, creatorIndex 0]";
        assertEquals(expected, property.toString());
    }

    // Test AnnotatedMember getter
    @org.junit.Test
    public void testGetMember() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = createType(TestBean.class, name, null);
        Annotations contextAnnotations = Annotations.empty();

        AnnotatedParameter param = Annotations.createParameterAnnotation(TestBean.class, "testProperty");
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, 0, null, null);

        AnnotatedMember member = property.getMember();
        assertEquals(param, member);
    }

    // Test getCreatorIndex method
    @org.junit.Test
    public void testGetCreatorIndex() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = createType(TestBean.class, name, null);
        Annotations contextAnnotations = Annotations.empty();

        AnnotatedParameter param = Annotations.createParameterAnnotation(TestBean.class, "testProperty");
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, 0, null, null);

        assertEquals(0, property.getCreatorIndex());

        JavaType typeWithAnnotation = createType(TestBeanWithCreatorIndex.class, name, null);
        property = new CreatorProperty(name, typeWithAnnotation, null, null, contextAnnotations, param, 1, null, null);

        assertEquals(1, property.getCreatorIndex());
    }

    private static class TestBean {
        @JsonProperty
        int testProperty;
    }

    private static class TestBeanWithAnnotation {
        @JsonProperty
        @JsonPropertyOrder(1)
        int testProperty;
    }

    private static class TestBeanWithCreatorIndex {
        @JsonProperty
        @JsonPropertyOrder(1)
        int testProperty;

        @JsonCreator
        public TestBeanWithCreatorIndex(@JsonProperty("testProperty") int testProperty) {
            this.testProperty = testProperty;
        }
    }

    private static class TestInjectableBean {
        @JsonProperty
        public int testProperty;
    }

    private static class StdTypeDeserializer extends TypeDeserializer {

    }
}