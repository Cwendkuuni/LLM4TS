package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;

    @Before
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        this.config = objectMapper.getSerializationConfig();
        this.beanDesc = new BeanDescription(Object.class);
    }

    @Test
    public void testClassAnnotations() {
        Annotations classAnnotations = new PropertyBuilder(config, beanDesc).getClassAnnotations();
        assertNotNull(classAnnotations);
    }

    @Test
    public void testBuildWriter_NullParam() {
        try {
            new PropertyBuilder(config, beanDesc).buildWriter(null, null, null, null, null, null, null, false);
            fail("expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testBuildWriter_ValidParam() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", String.class, null, null, null, null, null, null);
        SerializerProvider prov = new SerializerProvider();
        JavaType declaredType = ObjectType.Std_String;
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = false;

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        assertNotNull(bpw);
    }

    @Test
    public void testFindSerializationType_NoAnnotations() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType declaredType = ObjectType.Std_String;

        assertNull(propertyBuilder.findSerializationType(null, false, declaredType));
    }

    @Test
    public void testFindSerializationType_AnnotationsPresent() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated a = mock(Annotated.class);
        JavaType declaredType = ObjectType.Std_String;

        when(a.getType()).thenReturn(ObjectType.Std_String);
        JavaType secondary = ObjectType.Std_Integer;
        when(_annotationIntrospector.refineSerializationType(config, a, declaredType)).thenReturn(secondary);

        JavaType result = propertyBuilder.findSerializationType(a, false, declaredType);
        assertEquals(secondary, result);
    }

    @Test
    public void testGetClassAnnotations_EmptyAnnotations() {
        Annotations result = new PropertyBuilder(config, beanDesc).getClassAnnotations();
        assertEquals(0, result.size());
    }

    @Test
    public void testGetPropertyDefaultValue_NullPropertyName() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        String name = null;
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = ObjectType.Std_String;

        try {
            propertyBuilder.getPropertyDefaultValue(name, member, type);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetPropertyDefaultValue_StringType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        String name = "testProperty";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = ObjectType.Std_String;

        Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertEquals("", result);
    }

    @Test
    public void testGetPropertyDefaultValue_ContainerType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        String name = "testProperty";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = ObjectType.ArrayType.construct(ObjectType.Std_Integer, 5);

        Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertEquals(JsonInclude.Include.NON_EMPTY, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_NullDefaultBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        String name = "testProperty";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = ObjectType.Std_String;

        Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        fail("expected IllegalArgumentException");
    }

    @Test
    public void testGetPropertyDefaultValue_EmptyDefaultBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        String name = "testProperty";
        AnnotatedMember member = mock(AnnotatedMember.class);
        Object defaultBean = new Object();
        JavaType type = ObjectType.Std_String;

        PropertyBuilder spy = spy(propertyBuilder);
        when(spy.getDefaultBean()).thenReturn(defaultBean);

        Object result = spy.getPropertyDefaultValue(name, member, type);
        assertEquals(null, result);
    }

    @Test
    public void testGetPropertyDefaultValue_DefaultValuePresent() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        String name = "testProperty";
        AnnotatedMember member = mock(AnnotatedMember.class);
        Object defaultBean = new Object() {
            int testProp = 1;
        };
        JavaType type = ObjectType.Std_Integer;

        PropertyBuilder spy = spy(propertyBuilder);
        when(spy.getDefaultBean()).thenReturn(defaultBean);

        Object result = spy.getPropertyDefaultValue(name, member, type);
        assertEquals(1, result);
    }

    @Test
    public void testGetPropertyDefaultValue_ArrayType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        String name = "testProperty";
        AnnotatedMember member = mock(AnnotatedMember.class);
        Object defaultBean = new Object() {
            int[] testProp = {1, 2, 3};
        };
        JavaType type = ObjectType.ArrayType.construct(ObjectType.Std_Integer, 3);

        PropertyBuilder spy = spy(propertyBuilder);
        when(spy.getDefaultBean()).thenReturn(defaultBean);

        Object result = spy.getPropertyDefaultValue(name, member, type);
        assertArrayEquals(new int[]{1, 2, 3}, result);
    }
}