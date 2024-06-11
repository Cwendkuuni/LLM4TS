package com.example.test;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;

    @Before
    public void setUp() {
        config = new SerializationConfig();
        beanDesc = new BeanDescription(Mockito.mock(Object.class));
        config.setDefaultPropertyInclusion(JsonInclude.Value.standardInclude(JsonInclude.Include.NON_NULL));
    }

    @Test
    public void testGetClassAnnotations() {
        // Test the getClassAnnotations method
        assertNotNull(PropertyBuilder.class.getMethod("getClassAnnotations"));
        assertEquals(beanDesc.getClassAnnotations(), PropertyBuilder.class.getMethod("getClassAnnotations").invoke(new PropertyBuilder(config, beanDesc)));
    }

    @Test
    public void testBuildWriter_NullProperties() {
        // Test the buildWriter method with null properties
        assertNull(PropertyBuilder.buildWriter(null, null, null, null, null, null, null, false));
    }

    @Test
    public void testBuildWriter_ValidArgs() {
        // Test the buildWriter method with valid arguments
        Object value = new Object();
        JsonSerializer<?> serializer = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSerializer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        assertEquals(new BeanPropertyWriter(propDef, am, beanDesc.getClassAnnotations(), declaredType, serializer, typeSerializer, null, false, null),
                PropertyBuilder.buildWriter(config, beanDesc, declaredType, serializer, typeSerializer, contentTypeSerializer, am, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildWriter_NullContentTypeSerializer() {
        // Test the buildWriter method with a null contentTypeSerializer
        PropertyBuilder.buildWriter(config, beanDesc, null, null, null, null, null, false, null);
    }

    @Test
    public void testFindSerializationType_AnnotationPresent() {
        // Test the findSerializationType method with an annotation present
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        Mockito.when(a.getType()).thenReturn(declaredType);
        JavaType secondary = Mockito.mock(JavaType.class);
        Mockito.when(_annotationIntrospector.refineSerializationType(config, a, declaredType)).thenReturn(secondary);
        Mockito.when(secondary.isAssignableFrom(declaredType.getRawClass())).thenReturn(true);
        Mockito.when(_annotationIntrospector.findSerializationTyping(a)).thenReturn(JsonSerialize.Typing.STATIC);
        assertTrue(PropertyBuilder.useStaticTyping(PropertyBuilder.findSerializationType(a, true, declaredType)));
        assertEquals(declaredType.withStaticTyping(), PropertyBuilder.findSerializationType(a, true, declaredType).withStaticTyping());
    }

    @Test
    public void testFindSerializationType_AnnotationAbsent() {
        // Test the findSerializationType method with no annotation
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        assertFalse(PropertyBuilder.useStaticTyping(PropertyBuilder.findSerializationType(a, true, declaredType)));
        assertNull(PropertyBuilder.findSerializationType(a, true, declaredType));
    }

    @Test
    public void testGetDefaultBean() {
        // Test the getDefaultBean method
        assertNotNull(PropertyBuilder.class.getMethod("getDefaultBean"));
        assertNull(PropertyBuilder.getDefaultBean());
    }

    @Test
    public void testGetPropertyDefaultValue_StringProperty() {
        // Test the getPropertyDefaultValue method with a String property
        String propName = "testProperty";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        Mockito.when(member.getValue(Mockito.any())).thenReturn("defaultValue");
        assertEquals("defaultValue", PropertyBuilder.getPropertyDefaultValue(propName, member, Mockito.mock(JavaType.class)));
    }

    @Test
    public void testGetPropertyDefaultValue_NullBean() {
        // Test the getPropertyDefaultValue method with a null bean
        String propName = "testProperty";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        assertNull(PropertyBuilder.getPropertyDefaultValue(propName, member, Mockito.mock(JavaType.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_NullPropName() {
        // Test the getPropertyDefaultValue method with a null propName
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        Annotated defaultBean = Mockito.mock(Annotated.class);
        PropertyBuilder.getPropertyDefaultValue(null, member, Mockito.mock(JavaType.class));
    }

    @Test
    public void testGetDefaultValue() {
        // Test the getDefaultValue method
        assertNotNull(PropertyBuilder.class.getMethod("getDefaultValue", JavaType.class));
        assertEquals(JsonInclude.Include.NON_DEFAULT, PropertyBuilder.getDefaultValue(Mockito.mock(JavaType.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultValue_NullType() {
        // Test the getDefaultValue method with a null type
        PropertyBuilder.getDefaultValue(null);
    }
}