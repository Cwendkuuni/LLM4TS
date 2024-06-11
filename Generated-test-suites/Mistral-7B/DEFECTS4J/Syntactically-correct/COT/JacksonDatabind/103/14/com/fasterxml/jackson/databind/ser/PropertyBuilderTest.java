package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;;

public class PropertyBuilderTest {

    @Mock
    private SerializationConfig config;

    @Mock
    private BeanDescription beanDesc;

    @Mock
    private AnnotationIntrospector annotationIntrospector;

    @InjectMocks
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() {
        when(config.getDefaultPropertyInclusion(any(Class.class), any()))
                .thenReturn(JsonInclude.Value.builder().build());
        when(beanDesc.getClassAnnotations())
                .thenReturn(new Annotations());
        when(beanDesc.findPropertyInclusion(JsonInclude.Value.empty()))
                .thenReturn(JsonInclude.Value.builder().build());
        when(beanDesc.getType())
                .thenReturn(new TypeFactory().uncheckedType(Object.class));
    }

    @Test
    public void testPropertyBuilderConstruction() {
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        assertNotNull(builder);
        assertEquals(config, builder._config);
        assertEquals(beanDesc, builder._beanDesc);
        assertEquals(annotationIntrospector, builder._annotationIntrospector);
    }

    @Test
    public void testBuildWriter_NoContentTypeSer() throws JsonMappingException {
        JavaType declaredType = new TypeFactory().uncheckedType(Integer.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        Object valueToSuppress = new Object();
        try {
            PropertyWriter writer = propertyBuilder.buildWriter(mock(SerializerProvider.class), propDef, declaredType, ser, null, null, null, false);
            fail("Expected JsonMappingException to be thrown.");
        } catch (JsonMappingException e) {
            verify(propDef).getAccessor();
            verifyNoMoreInteractions(config, beanDesc, annotationIntrospector);
            assertEquals("Could not determine property type", e.getMessage());
        }
    }

    @Test
    public void testBuildWriter_WithContentTypeSer() throws JsonMappingException {
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        JavaType declaredType = new TypeFactory().uncheckedType(Integer[].class);
        JavaType serializationType = new TypeFactory().uncheckedType(Integer[].class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);

        when(propDef.getAccessor()).thenReturn(new AnnotatedMember() {
            @Override
            public Annotation[] getAnnotations() {
                return new Annotation[0];
            }

            @Override
            public Class<?> getRawType() {
                return Integer[].class;
            }
        });

        PropertyWriter writer = propertyBuilder.buildWriter(mock(SerializerProvider.class), propDef, declaredType, ser, null, null, null, false);

        verify(config).getDefaultInclusion(eq(Integer[].class), any());
        verify(beanDesc).getClassAnnotations();
        verify(beanDesc).findPropertyInclusion(eq(JsonInclude.Value.empty()));
        verify(annotationIntrospector).findSerializationType(any(), eq(false), declaredType);
        verify(ser).getDefaultSerializerProvider();
        assertNotNull(writer);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindSerializationType_AnnotationForcesTypeToUseDeclaredType()
            throws JsonMappingException {
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType declaredType = new TypeFactory().uncheckedType(String.class);

        when(member.getGenericType()).thenReturn(new GenericType(String[].class));
        when(member.getRawType()).thenReturn(String[].class);

        JavaType secondary = new TypeFactory().uncheckedType(String[].class);
        when(annotationIntrospector.refineSerializationType(eq(config), eq(member), declaredType))
                .thenReturn(secondary);

        propertyBuilder.findSerializationType(member, false, declaredType);
    }

    @Test
    public void testGetDefaultBean() {
        Object defaultBean = new Object();
        when(beanDesc.instantiateBean(anyBoolean()))
                .thenReturn(defaultBean);

        Object result = propertyBuilder.getDefaultBean();
        assertSame(defaultBean, result);
    }

    @Test
    public void testGetDefaultBean_Null() {
        when(beanDesc.instantiateBean(anyBoolean()))
                .thenReturn(null);

        Object result = propertyBuilder.getDefaultBean();
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_Null() {
        String propName = "propName";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = new TypeFactory().uncheckedType(Integer.class);

        propertyBuilder.getPropertyDefaultValue(propName, member, type);
    }

    @Test
    public void testGetPropertyDefaultValue_Value() throws JsonMappingException {
        String propName = "propName";
        Object defaultBean = new Object();
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = new TypeFactory().uncheckedType(Integer.class);
        int defaultValue = 1;

        when(member.getValue(eq(defaultBean)))
                .thenReturn(defaultValue);

        Object result = propertyBuilder.getPropertyDefaultValue(propName, member, type);
        assertEquals(defaultValue, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_ThrowsException() throws Exception {
        String propName = "propName";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = new TypeFactory().uncheckedType(Integer.class);
        Exception e = new Exception();

        when(member.getValue(anyObject()))
                .thenThrow(e);

        propertyBuilder.getPropertyDefaultValue(propName, member, type);
    }

    @Test
    public void test_throwWrapped() throws JsonMappingException {
        String propName = "propName";
        Object defaultBean = new Object();
        Exception e = new JsonMappingException("Failed to get property");

        propertyBuilder._throwWrapped(e, propName, defaultBean);
    }
}