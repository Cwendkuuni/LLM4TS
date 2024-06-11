package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        SerializationConfig config = new SerializationConfig();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        BeanDescription beanDesc = new BeanDescription(TestProperty.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        JavaType declaredType = TypeFactory.defaultInstance().constructType(TestProperty.class);
        Object serDef = AnnotationIntrospector.findSerializerAnnotation(TestProperty.class.getDeclaredField("property"));

        JsonSerialize.Typing typing = AnnotationIntrospector.findSerializationTyping(TestProperty.class.getDeclaredField("property"));
        boolean useStaticTyping = false;

        JavaType serializationType = propertyBuilder.findSerializationType(TestProperty.class.getDeclaredField("property"), useStaticTyping, declaredType);

        assertEquals(String.class, serializationType.getRawClass());

        useStaticTyping = true;
        serializationType = propertyBuilder.findSerializationType(TestProperty.class.getDeclaredField("property"), useStaticTyping, declaredType);

        assertEquals(String.class, serializationType.getRawClass());
        assertEquals(JsonSerialize.Typing.STATIC, typing);

        // Test with custom serializer
        TypeSerializer typeSerializer = null;
        JsonSerialize serializer = new JsonSerializer() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString("custom");
            }
        };

        JavaType serializationTypeWithSerializer = propertyBuilder.findSerializationType(TestProperty.class.getDeclaredField("property"), useStaticTyping, declaredType, serializer, typeSerializer);

        assertEquals(String.class, serializationTypeWithSerializer.getRawClass());
        assertEquals(JsonSerialize.Typing.STATIC, typing);

        // Test with non-matching type
        Class<?> nonMatchingType = Integer.class;
        JavaType serializationTypeWithNonMatchingType = propertyBuilder.findSerializationType(TestProperty.class.getDeclaredField("property"), useStaticTyping, declaredType, null, null, nonMatchingType);

        assertNull(serializationTypeWithNonMatchingType);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        BeanDescription beanDesc = new BeanDescription(TestProperty.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        ObjectPropertyWriter writer = propertyBuilder.buildWriter(null, null, TypeFactory.defaultInstance().constructType(TestProperty.class), null, null, null, null, false);

        assertNotNull(writer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildWriterWithEmptyContentType() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        BeanDescription beanDesc = new BeanDescription(TestProperty.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        JavaType serializationType = TypeFactory.defaultInstance().constructType(TestContainer.class);
        TypeSerializer contentTypeSer = null;
        JavaType ct = null;

        propertyBuilder.buildWriter(null, null, serializationType, null, null, contentTypeSer, null, false);
    }

    @Test
    public void testGetClassAnnotations() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(TestProperty.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        assertEquals(TestProperty.class.getAnnotations(), propertyBuilder.getClassAnnotations());
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(TestProperty.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object defaultBean = propertyBuilder.getDefaultBean();
        TestProperty testProperty = new TestProperty();

        Object defaultValue = propertyBuilder.getPropertyDefaultValue("property", TestProperty.class.getDeclaredField("property"), TypeFactory.defaultInstance().constructType(TestProperty.class));

        assertNull(defaultValue);

        defaultBean = propertyBuilder.getDefaultBean();
        assertEquals("default", defaultBean.getPropertyValue("defaultProperty"));

        defaultValue = propertyBuilder.getPropertyDefaultValue("nonDefaultProperty", TestProperty.class.getDeclaredField("nonDefaultProperty"), TypeFactory.defaultInstance().constructType(TestProperty.class));

        assertNull(defaultValue);

        defaultValue = propertyBuilder.getPropertyDefaultValue("defaultProperty", TestProperty.class.getDeclaredField("defaultProperty"), TypeFactory.defaultInstance().constructType(TestProperty.class), testProperty);

        assertEquals("default", defaultValue);
    }
}

class TestProperty {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String property;

    private String defaultProperty = "default";

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getDefaultProperty() {
        return defaultProperty;
    }
}

class TestContainer {

    @JsonSerialize(using = ToStringSerializer.class)
    private List<String> list;
}