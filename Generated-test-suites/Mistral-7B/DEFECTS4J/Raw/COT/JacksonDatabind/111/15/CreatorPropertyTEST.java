package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.None;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

import org.junit.Test;

public class CreatorPropertyTest {

    @Test
    public void testCreatorPropertyConstructor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructType(CreatorClass.class);
        PropertyName propertyName = PropertyName.construct("name");
        TypeDeserializer typeDeser = new None();
        Annotations contextAnnotations = Annotations.empty();
        AnnotatedParameter param = Annotations.findValue(CreatorClass.class.getDeclaredMethod("creatorMethod"), 0);
        int index = 0;
        Object injectableValueId = "valueId";
        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, propertyName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        assertEquals(propertyName, creatorProperty.getName());
        assertEquals(javaType, creatorProperty.getType());
        assertNull(creatorProperty.getWrapperName());
        assertNull(creatorProperty.getTypeDeserializer());
        assertNull(creatorProperty.getContextAnnotations());
        assertNull(creatorProperty.getMetadata());
        assertEquals(param, creatorProperty.getAnnotated());
        assertEquals(index, creatorProperty.getCreatorIndex());
        assertEquals(injectableValueId, creatorProperty.getInjectableValueId());
        assertNull(creatorProperty.getFallbackSetter());
        assertFalse(creatorProperty.isIgnorable());
    }

    @Test
    public void testCreatorPropertyCopyConstructor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructType(CreatorClass.class);
        PropertyName propertyName = PropertyName.construct("name");
        TypeDeserializer typeDeser = new None();
        Annotations contextAnnotations = Annotations.empty();
        AnnotatedParameter param = Annotations.findValue(CreatorClass.class.getDeclaredMethod("creatorMethod"), 0);
        int index = 0;
        Object injectableValueId = "valueId";
        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, propertyName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        CreatorProperty copy = new CreatorProperty(creatorProperty, null);

        assertEquals(creatorProperty.getName(), copy.getName());
        assertEquals(creatorProperty.getType(), copy.getType());
        assertEquals(creatorProperty.getWrapperName(), copy.getWrapperName());
        assertEquals(creatorProperty.getTypeDeserializer(), copy.getTypeDeserializer());
        assertEquals(creatorProperty.getContextAnnotations(), copy.getContextAnnotations());
        assertEquals(creatorProperty.getMetadata(), copy.getMetadata());
        assertEquals(creatorProperty.getAnnotated(), copy.getAnnotated());
        assertEquals(creatorProperty.getCreatorIndex(), copy.getCreatorIndex());
        assertEquals(creatorProperty.getInjectableValueId(), copy.getInjectableValueId());
        assertNull(copy.getFallbackSetter());
        assertFalse(copy.isIgnorable());
    }

    @Test
    public void testCreatorPropertySetterConstructor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructType(CreatorClass.class);
        PropertyName propertyName = PropertyName.construct("name");
        TypeDeserializer typeDeser = new None();
        Annotations contextAnnotations = Annotations.empty();
        AnnotatedParameter param = Annotations.findValue(CreatorClass.class.getDeclaredMethod("creatorMethod"), 0);
        int index = 0;
        Object injectableValueId = "valueId";
        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, propertyName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        SettableBeanProperty fallbackSetter = new TestSettableBeanProperty();
        CreatorProperty copy = new CreatorProperty(creatorProperty, fallbackSetter);

        assertEquals(creatorProperty.getName(), copy.getName());
        assertEquals(creatorProperty.getType(), copy.getType());
        assertEquals(creatorProperty.getWrapperName(), copy.getWrapperName());
        assertEquals(creatorProperty.getTypeDeserializer(), copy.getTypeDeserializer());
        assertEquals(creatorProperty.getContextAnnotations(), copy.getContextAnnotations());
        assertEquals(creatorProperty.getMetadata(), copy.getMetadata());
        assertEquals(creatorProperty.getAnnotated(), copy.getAnnotated());
        assertEquals(creatorProperty.getCreatorIndex(), copy.getCreatorIndex());
        assertEquals(creatorProperty.getInjectableValueId(), copy.getInjectableValueId());
        assertEquals(fallbackSetter, copy.getFallbackSetter());
        assertFalse(copy.isIgnorable());
    }

    @Test
    public void testCreatorPropertySetIgnorable() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructType(CreatorClass.class);
        PropertyName propertyName = PropertyName.construct("name");
        TypeDeserializer typeDeser = new None();
        Annotations contextAnnotations = Annotations.empty();
        AnnotatedParameter param = Annotations.findValue(CreatorClass.class.getDeclaredMethod("creatorMethod"), 0);
        int index = 0;
        Object injectableValueId = "valueId";
        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, propertyName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        creatorProperty.markAsIgnorable();

        assertTrue(creatorProperty.isIgnorable());
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingSetterThrowsException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructType(CreatorClass.class);
        PropertyName propertyName = PropertyName.construct("name");
        TypeDeserializer typeDeser = new None();
        Annotations contextAnnotations = Annotations.empty();
        AnnotatedParameter param = Annotations.findValue(CreatorClass.class.getDeclaredMethod("creatorMethod"), 0);
        int index = 0;
        Object injectableValueId = "valueId";
        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, propertyName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        creatorProperty.setFallbackSetter(null);

        creatorProperty.deserializeAndSet(null, null, null);
    }

    @Test
    public void testFindInjectableValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructType(CreatorClass.class);
        PropertyName propertyName = PropertyName.construct("name");
        TypeDeserializer typeDeser = new None();
        Annotations contextAnnotations = Annotations.empty();
        AnnotatedParameter param = Annotations.findValue(CreatorClass.class.getDeclaredMethod("creatorMethod"), 0);
        int index = 0;
        Object injectableValueId = "valueId";
        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, propertyName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        ObjectMapper testMapper = new ObjectMapper();
        CreatorClass creatorClass = testMapper.readValue(getClass().getResource("/creator-class.json"), CreatorClass.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object beanInstance = mapper.readValue(getClass().getResource("/creator-instance.json"), javaType);

        Object injectedValue = creatorProperty.findInjectableValue(context, beanInstance);

        assertEquals("injectedValue", injectedValue);
    }

    @Test
    public void testInject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructType(CreatorClass.class);
        PropertyName propertyName = PropertyName.construct("name");
        TypeDeserializer typeDeser = new None();
        Annotations contextAnnotations = Annotations.empty();
        AnnotatedParameter param = Annotations.findValue(CreatorClass.class.getDeclaredMethod("creatorMethod"), 0);
        int index = 0;
        Object injectableValueId = "valueId";
        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, propertyName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        ObjectMapper testMapper = new ObjectMapper();
        CreatorClass creatorClass = testMapper.readValue(getClass().getResource("/creator-class.json"), CreatorClass.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object beanInstance = mapper.readValue(getClass().getResource("/creator-instance.json"), javaType);

        creatorProperty.inject(context, beanInstance);

        Object injectedValue = creatorClass.getName();

        assertEquals(injectedValue, beanInstance.getClass().getDeclaredField("name").get(beanInstance));
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingInjectableValueIdThrowsException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructType(CreatorClass.class);
        PropertyName propertyName = PropertyName.construct("name");
        TypeDeserializer typeDeser = new None();
        Annotations contextAnnotations = Annotations.empty();
        AnnotatedParameter param = Annotations.findValue(CreatorClass.class.getDeclaredMethod("creatorMethod"), 0);
        int index = 0;
        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, propertyName, typeDeser, contextAnnotations, param, index, null, null);

        ObjectMapper testMapper = new ObjectMapper();
        CreatorClass creatorClass = testMapper.readValue(getClass().getResource("/creator-class.json"), CreatorClass.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object beanInstance = mapper.readValue(getClass().getResource("/creator-instance.json"), javaType);

        creatorProperty.inject(context, beanInstance);
    }

    @Test
    public void testSetFallbackSetter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructType(CreatorClass.class);
        PropertyName propertyName = PropertyName.construct("name");
        TypeDeserializer typeDeser = new None();
        Annotations contextAnnotations = Annotations.empty();
        AnnotatedParameter param = Annotations.findValue(CreatorClass.class.getDeclaredMethod("creatorMethod"), 0);
        int index = 0;
        Object injectableValueId = "valueId";
        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, propertyName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        SettableBeanProperty fallbackSetter = new TestSettableBeanProperty();

        creatorProperty.setFallbackSetter(fallbackSetter);

        assertEquals(fallbackSetter, creatorProperty.getFallbackSetter());
    }

    @Test
    public void testGetValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructType(CreatorClass.class);
        PropertyName propertyName = PropertyName.construct("name");
        TypeDeserializer typeDeser = new None();
        Annotations contextAnnotations = Annotations.empty();
        AnnotatedParameter param = Annotations.findValue(CreatorClass.class.getDeclaredMethod("creatorMethod"), 0);
        int index = 0;
        Object injectableValueId = "valueId";
        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, propertyName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        ObjectMapper testMapper = new ObjectMapper();
        CreatorClass creatorClass = testMapper.readValue(getClass().getResource("/creator-class.json"), CreatorClass.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object beanInstance = mapper.readValue(getClass().getResource("/creator-instance.json"), javaType);

        creatorProperty.inject(context, beanInstance);

        Object value = creatorProperty.getValue(beanInstance);

        assertEquals(creatorClass.getName(), value);
    }

    @Test(expected = IllegalAccessException.class)
    public void testGetValueIllegalAccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructType(CreatorClass.class);
        PropertyName propertyName = PropertyName.construct("name");
        TypeDeserializer typeDeser = new None();
        Annotations contextAnnotations = Annotations.empty();
        AnnotatedParameter param = Annotations.findValue(CreatorClass.class.getDeclaredMethod("creatorMethod"), 0);
        int index = 0;
        Object injectableValueId = "valueId";
        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, propertyName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        ObjectMapper testMapper = new ObjectMapper();
        CreatorClass creatorClass = testMapper.readValue(getClass().getResource("/creator-class.json"), CreatorClass.class);

        Object beanInstance = new CreatorClass();

        creatorProperty.inject(null, beanInstance);

        creatorProperty.getValue(beanInstance);
    }

    // Add more test cases here as needed

    static class CreatorClass {
        @JsonProperty("name")
        private String name;

        @JsonCreator
        public CreatorClass(@JsonProperty("name") String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class TestSettableBeanProperty extends BeanProperty {
        public void set(Object instance, Object value) {}
        public Object setAndReturn(Object instance, Object value) { return value; }
    }
}