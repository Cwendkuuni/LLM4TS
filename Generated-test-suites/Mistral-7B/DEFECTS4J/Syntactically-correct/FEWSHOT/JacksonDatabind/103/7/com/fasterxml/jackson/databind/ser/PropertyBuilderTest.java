package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        class TestBean {
            @JsonSerialize(using = JsonCustomSerializer.class, include = JsonSerialize.Inclusion.NON_DEFAULT)
            private int property;
        }

        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(new AnnotationIntrospector()), beanDesc);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = TypeFactory.uncheckedType(TestBean.class);

        try {
            JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
            Assert.assertEquals(declaredType, serializationType);
        } catch (JsonMappingException e) {
            // expected
        }

        JsonSerializer<Object> jsonCustomSerializer = Mockito.mock(JsonSerializer.class);
        JsonInclude.Include include = JsonInclude.Include.NON_DEFAULT;
        JsonSerialize annotation = new JsonSerialize() {{
            setInclude(include);
            setUsing(jsonCustomSerializer);
        }};
        AnnotationInstance annotationInstance = new AnnotationInstance(annotation.getClass(), annotation);
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        ReflectionTestUtils.setFieldValue(annotatedMember, "value", annotationInstance);
        Mockito.when(am.getAnnotation(JsonSerialize.class)).thenReturn(annotatedMember);

        try {
            JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
            Assert.assertEquals(TypeFactory.uncheckedType(JsonCustomSerializer.class), serializationType);
        } catch (JsonMappingException e) {
            // expected
        }

        JsonInclude.Include include2 = JsonInclude.Include.ALWAYS;
        JsonSerialize annotation2 = new JsonSerialize() {{
            setInclude(include2);
        }};
        AnnotationInstance annotationInstance2 = new AnnotationInstance(annotation2.getClass(), annotation2);
        AnnotatedMember annotatedMember2 = Mockito.mock(AnnotatedMember.class);
        ReflectionTestUtils.setFieldValue(annotatedMember2, "value", annotationInstance2);
        Mockito.when(am.getAnnotation(JsonSerialize.class)).thenReturn(annotatedMember2);

        try {
            JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
            Assert.assertEquals(declaredType, serializationType);
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        class TestBean {
            private int property;
        }

        SerializationConfig config = new SerializationConfig(new AnnotationIntrospector());
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new AnnotatedMethod("getDefaultValue", TestBean.class), null);
        JavaType declaredType = TypeFactory.uncheckedType(TestBean.class);
        JsonSerializer<Object> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        Assert.assertNotNull(writer);
    }

    @Test
    public void testBuildWriterWithContentTypeSerializer() throws JsonMappingException {
        class TestBean {
            private List<String> property;
        }

        SerializationConfig config = new SerializationConfig(new AnnotationIntrospector());
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new AnnotatedMethod("getDefaultValue", TestBean.class), null);
        JavaType declaredType = TypeFactory.uncheckedType(TestBean.class);
        JsonSerializer<Object> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;
        TypeSerializer listSerializer = Mockito.mock(TypeSerializer.class);

        JavaType contentType = TypeFactory.uncheckedType(ArrayList.class);
        contentType = contentType.withContentTypeHandler(listSerializer);

        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        Assert.assertNotNull(writer);
    }

    @Test
    public void testGetClassAnnotations() {
        class TestBean {
            @JsonInclude(JsonInclude.Include.NON_DEFAULT)
            private int property;
        }

        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(new AnnotationIntrospector()), beanDesc);

        Annotation[] annotations = propertyBuilder.getClassAnnotations();
        Assert.assertArrayEquals(new JsonInclude[]{new JsonInclude(){{setInclude(JsonInclude.Include.NON_DEFAULT);}}}, annotations);
    }
}