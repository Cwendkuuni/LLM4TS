package com.example.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSerialize;
import com.fasterxml.jackson.core.SerializationConfig;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.BeanPropertyWriter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonProperty;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter.MarkerForEmpty;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TypeFactory;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private AnnotationIntrospector introspector;

    @Before
    public void setup() {
        config = new SerializationConfig(new ObjectMapper.DefaultConfig());
        config.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        config.setAnnotationIntrospector(introspector = mock(AnnotationIntrospector.class));
        beanDesc = mock(BeanDescription.class);
    }

    @Test
    public void testBuildWriter_propertyWithDefaultValue() throws JsonMappingException {
        // Given
        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        when(beanDesc.findPropertyInclusion(JsonInclude.Value.empty())).thenReturn(JsonInclude.Value.STANDARD);
        when(beanDesc.getType()).thenReturn(TypeFactory.defaultInstance().constructType(Foo.class));
        when(beanDesc.getBeanClass()).thenReturn(Foo.class);
        when(beanDesc.findDefaultViews()).thenReturn(new Class[] { String[].class });
        when(beanDesc.instantiateBean(config.canOverrideAccessModifiers())).thenReturn(new Foo("defaultValue"));

        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);

        AnnotatedMember am = mock(AnnotatedMember.class);
        when(am.getRawType()).thenReturn(String[].class);

        JsonInclude.Value inclPerType = JsonInclude.Value.merge(
                beanDesc.findPropertyInclusion(JsonInclude.Value.empty()),
                config.getDefaultPropertyInclusion(beanDesc.getBeanClass(), JsonInclude.Value.empty()));

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // When
        BeanPropertyWriter writer = propertyBuilder.buildWriter(mock(SerializerProvider.class), null, declaredType, null, typeSerializer, null, am, true);

        // Then
        assertNotNull(writer);
    }

    @Test
    public void testBuildWriter_propertyWithoutDefaultValue() throws JsonMappingException {
        // Given
        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        when(beanDesc.findPropertyInclusion(JsonInclude.Value.empty())).thenReturn(JsonInclude.Value.STANDARD);
        when(beanDesc.getType()).thenReturn(TypeFactory.defaultInstance().constructType(Foo.class));
        when(beanDesc.getBeanClass()).thenReturn(Foo.class);
        when(beanDesc.findDefaultViews()).thenReturn(new Class[] { String[].class });
        when(beanDesc.instantiateBean(config.canOverrideAccessModifiers())).thenReturn(null);

        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);

        AnnotatedMember am = mock(AnnotatedMember.class);
        when(am.getRawType()).thenReturn(String[].class);

        JsonInclude.Value inclPerType = JsonInclude.Value.merge(
                beanDesc.findPropertyInclusion(JsonInclude.Value.empty()),
                config.getDefaultPropertyInclusion(beanDesc.getBeanClass(), JsonInclude.Value.empty()));

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // When
        BeanPropertyWriter writer = propertyBuilder.buildWriter(mock(SerializerProvider.class), null, declaredType, null, typeSerializer, null, am, true);

        // Then
        assertNotNull(writer);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriter_propertyWithInvalidSerializationType() throws JsonMappingException {
        // Given
        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        when(beanDesc.findPropertyInclusion(JsonInclude.Value.empty())).thenReturn(JsonInclude.Value.STANDARD);
        when(beanDesc.getType()).thenReturn(TypeFactory.defaultInstance().constructType(Foo.class));
        when(beanDesc.getBeanClass()).thenReturn(Foo.class);
        when(beanDesc.findDefaultViews()).thenReturn(new Class[] { String[].class });
        when(beanDesc.instantiateBean(config.canOverrideAccessModifiers())).thenReturn(new Foo("defaultValue"));

        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);

        AnnotatedMember am = mock(AnnotatedMember.class);
        when(am.getRawType()).thenReturn(Object.class);

        JsonInclude.Value inclPerType = JsonInclude.Value.merge(
                beanDesc.findPropertyInclusion(JsonInclude.Value.empty()),
                config.getDefaultPropertyInclusion(beanDesc.getBeanClass(), JsonInclude.Value.empty()));

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // When & Then
        propertyBuilder.buildWriter(mock(SerializerProvider.class), null, declaredType, null, typeSerializer, null, am, true);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriter_propertyWithNullSerializationType() throws JsonMappingException {
        // Given
        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        when(beanDesc.findPropertyInclusion(JsonInclude.Value.empty())).thenReturn(JsonInclude.Value.STANDARD);
        when(beanDesc.getType()).thenReturn(TypeFactory.defaultInstance().constructType(Foo.class));
        when(beanDesc.getBeanClass()).thenReturn(Foo.class);
        when(beanDesc.findDefaultViews()).thenReturn(new Class[] { String[].class });
        when(beanDesc.instantiateBean(config.canOverrideAccessModifiers())).thenReturn(new Foo("defaultValue"));

        JavaType declaredType = null;
        TypeSerializer typeSerializer = mock(TypeSerializer.class);

        AnnotatedMember am = mock(AnnotatedMember.class);
        when(am.getRawType()).thenReturn(String[].class);

        JsonInclude.Value inclPerType = JsonInclude.Value.merge(
                beanDesc.findPropertyInclusion(JsonInclude.Value.empty()),
                config.getDefaultPropertyInclusion(beanDesc.getBeanClass(), JsonInclude.Value.empty()));

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // When & Then
        propertyBuilder.buildWriter(mock(SerializerProvider.class), null, declaredType, null, typeSerializer, null, am, true);
    }

    @Test
    public void testFindSerializationType_propertyWithNullAnnotations() throws JsonMappingException {
        // Given
        when(beanDesc.getClassAnnotations()).thenReturn(null);
        when(beanDesc.getType()).thenReturn(TypeFactory.defaultInstance().constructType(Foo.class));
        when(beanDesc.getBeanClass()).thenReturn(Foo.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // When
        JavaType actualType = propertyBuilder.findSerializationType(null, false, null);

        // Then
        assertNull(actualType);
    }

    @Test
    public void testFindSerializationType_propertyWithDefaultSerializationType() throws JsonMappingException {
        // Given
        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        when(beanDesc.getType()).thenReturn(TypeFactory.defaultInstance().constructType(Foo.class));
        when(beanDesc.getBeanClass()).thenReturn(Foo.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // When
        JavaType actualType = propertyBuilder.findSerializationType(null, false, null);

        // Then
        assertEquals(TypeFactory.defaultInstance().constructType(Foo.class), actualType);
    }

    @Test
    public void testFindSerializationType_propertyWithCustomSerializationType() throws JsonMappingException {
        // Given
        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        when(beanDesc.getType()).thenReturn(TypeFactory.defaultInstance().constructType(Foo.class));
        when(beanDesc.getBeanClass()).thenReturn(Foo.class);
        when(_annotationIntrospector.refineSerializationType(config, (Annotated) mock(Method.class), null)).thenReturn(TypeFactory.defaultInstance().constructType(Bar.class));

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // When
        JavaType actualType = propertyBuilder.findSerializationType(null, false, null);

        // Then
        assertEquals(TypeFactory.defaultInstance().constructType(Bar.class), actualType);
    }

    private static class Annotations {}

    public static class Foo {
        private String prop;

        @JsonProperty("foo")
        @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
        public String getProp() {
            return prop;
        }

        @JsonProperty("foo")
        @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
        public void setProp(String prop) {
            this.prop = prop;
        }

        public Foo() {}

        public Foo(String prop) {
            this.prop = prop;
        }
    }

    public static class Bar {}
}