package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        Object a = new Object() {
            @JsonSerialize(using = Serializer.class)
            public int getSerializedValue() {
                return 42;
            }
        };
        JavaType declaredType = BeanPropertyDefinition.standardPropertyDefinition(new String("name"), int.class).getType();
        try {
            PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
            JavaType serializationType = propertyBuilder.findSerializationType(new Annotated(new java.lang.reflect.Method(a.getClass().getMethod("getSerializedValue")), a.getClass()), false, declaredType);
            assertEquals(int.class, serializationType.getRawClass());
        } catch (NoSuchMethodException e) {
            // expected
        }

        @JsonSerialize(using = Serializer.class)
        static class StaticSerializer {}
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        static class TestBean {}

        TestBean testBean = new TestBean();
        AnnotatedMember am = new AnnotatedMember(testBean.getClass().getDeclaredField("field"), testBean.getClass());
        declaredType = BeanPropertyDefinition.standardPropertyDefinition("field", String[].class).getType();
        try {
            PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
            JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
            JsonSerialize annotation = am.getAnnotation(JsonSerialize.class);
            if (annotation != null) {
                assertEquals(Serializer.class, serializationType.getRawClass());
            } else {
                assertNull(serializationType);
            }
        } catch (NoSuchFieldException e) {
            // expected
        }

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        @JsonSerialize(using = StaticSerializer.class)
        static class TestBeanWithAnnotation {}

        TestBeanWithAnnotation testBeanWithAnnotation = new TestBeanWithAnnotation();
        am = new AnnotatedMember(testBeanWithAnnotation.getClass().getDeclaredField("field"), testBeanWithAnnotation.getClass());
        declaredType = BeanPropertyDefinition.standardPropertyDefinition("field", String[].class).getType();
        try {
            PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
            JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
            JsonSerialize annotation = am.getAnnotation(JsonSerialize.class);
            if (annotation != null) {
                assertEquals(StaticSerializer.class, serializationType.getRawClass());
            } else {
                assertNull(serializationType);
            }
        } catch (NoSuchFieldException e) {
            // expected
        }
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        static class TestBean {
            private int field;

            public int getField() {
                return field;
            }
        }

        Serializer serializer = new Serializer();
        TypeSerializer typeSerializer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember(TestBean.class.getDeclaredField("field"), TestBean.class);
        JavaType declaredType = BeanPropertyDefinition.standardPropertyDefinition("field", int.class).getType();
        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(objectWriter.getSerializerProvider(), null, declaredType, serializer, typeSerializer, null, am, false);
        assertEquals(int.class, beanPropertyWriter.getDeclaredType());
        assertEquals(serializer, beanPropertyWriter.getSerializer());
        assertEquals(typeSerializer, beanPropertyWriter.getTypeSerializer());
        assertEquals(int[].class, beanPropertyWriter.getContentType());
    }

    @Test
    public void testSuppressNulls() throws JsonMappingException {
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        static class TestBean {
            private Integer field;

            public Integer getField() {
                return field;
            }
        }

        Serializer serializer = new Serializer();
        TypeSerializer typeSerializer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember(TestBean.class.getDeclaredField("field"), TestBean.class);
        JavaType
            declaredType = BeanPropertyDefinition.standardPropertyDefinition("field", Integer.class).getType();
        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(objectWriter.getSerializerProvider(), null, declaredType, serializer, typeSerializer, null, am, false);
        assertTrue(beanPropertyWriter.isSuppressNulls());

        propertyBuilder = new PropertyBuilder(null, null);
        BeanPropertyWriter beanPropertyWriter2 = propertyBuilder.buildWriter(objectWriter.getSerializerProvider(), null, declaredType, serializer, typeSerializer, null, am, true);
        assertFalse(beanPropertyWriter2.isSuppressNulls());
    }

    @Test
    public void testValueToSuppress() throws JsonMappingException {
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        static class TestBean {
            private Integer field;

            public Integer getField() {
                return field;
            }
        }

        Serializer serializer = new Serializer();
        TypeSerializer typeSerializer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember(TestBean.class.getDeclaredField("field"), TestBean.class);
        JavaType
            declaredType = BeanPropertyDefinition.standardPropertyDefinition("field", Integer.class).getType();
        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(objectWriter.getSerializerProvider(), null, declaredType, serializer, typeSerializer, null, am, false);
        Object valueToSuppress = beanPropertyWriter.getValueToSuppress();
        assertNull(valueToSuppress);

        propertyBuilder = new PropertyBuilder(null, null);
        propertyBuilder._defaultInclusion = new JsonInclude.Value(JsonInclude.Include.NON_EMPTY);
        beanPropertyWriter = propertyBuilder.buildWriter(objectWriter.getSerializerProvider(), null, declaredType, serializer, typeSerializer, null, am, false);
        Object valueToSuppress2 = beanPropertyWriter.getValueToSuppress();
        assertNull(valueToSuppress2);

        propertyBuilder = new PropertyBuilder(null, null);
        propertyBuilder._defaultBean = new TestBean();
        beanPropertyWriter = propertyBuilder.buildWriter(objectWriter.getSerializerProvider(), null, declaredType, serializer, typeSerializer, null, am, false);
        Object valueToSuppress3 = beanPropertyWriter.getValueToSuppress();
        assertNull(valueToSuppress3);

        propertyBuilder = new PropertyBuilder(null, null);
        propertyBuilder._defaultInclusion = new JsonInclude.Value(JsonInclude.Include.NON_DEFAULT);
        beanPropertyWriter = propertyBuilder.buildWriter(objectWriter.getSerializerProvider(), null, declaredType, serializer, typeSerializer, null, am, false);
        Integer valueToSuppress4 = (Integer) beanPropertyWriter.getValueToSuppress();
        assertEquals(0, valueToSuppress4.intValue());

        propertyBuilder = new PropertyBuilder(null, null);
        propertyBuilder._defaultInclusion = new JsonInclude.Value(JsonInclude.Include.NON_ABSENT);
        beanPropertyWriter = propertyBuilder.buildWriter(objectWriter.getSerializerProvider(), null, declaredType, serializer, typeSerializer, null, am, false);
        assertNull(beanPropertyWriter.getValueToSuppress());

        propertyBuilder = new PropertyBuilder(null, null);
        propertyBuilder._defaultInclusion = new JsonInclude.Value(JsonInclude.Include.NON_NULL);
        beanPropertyWriter = propertyBuilder.buildWriter(objectWriter.getSerializerProvider(), null, declaredType, serializer, typeSerializer, null, am, false);
        assertNull(beanPropertyWriter.getValueToSuppress());
    }
}