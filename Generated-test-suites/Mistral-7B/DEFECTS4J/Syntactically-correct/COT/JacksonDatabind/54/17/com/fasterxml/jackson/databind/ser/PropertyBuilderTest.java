package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter.MarkerForEmpty;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private AnnotationIntrospector annotationIntrospector;

    @Before
    public void setup() {
        config = Mockito.mock(SerializationConfig.class);
        beanDesc = Mockito.mock(BeanDescription.class);
        annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
    }

    @Test
    public void testPropertyBuilderCreation() {
        new PropertyBuilder(config, beanDesc);
        verify(beanDesc).getClassAnnotations();
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        JavaType declaredType = Mockito.mock(JavaType.class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        when(beanDesc.findPropertyInclusion(any())).thenReturn(JsonInclude.Value.STANDARD_INCLUSION);
        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());

        BeanPropertyWriter writer = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), null, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNotNull(writer);

        verify(annotationIntrospector).findNullSerializer(am);
        verify(annotationIntrospector).findUnwrappingNameTransformer(am);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationTypeWithInvalidClassType() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType declaredType = Mockito.mock(JavaType.class);
        Annotated a = Mockito.mock(Annotated.class);

        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        when(annotationIntrospector.refineSerializationType(config, a, declaredType)).thenReturn(JavaType.construct(int[].class));

        propertyBuilder.findSerializationType(a, false, declaredType);
    }

    @Test
    public void testGetClassAnnotations() {
        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertEquals(new Annotations(), propertyBuilder.getClassAnnotations());
    }

    @Test
    public void testGetPropertyDefaultValueForPrimitiveType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        ClassUtil.DefaultValue defaultValue = ClassUtil.defaultValue(int.class);
        assertEquals(defaultValue, propertyBuilder.getDefaultValue(JavaType.construct(int.class)));
    }

    @Test
    public void testGetPropertyDefaultValueForStringType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertEquals("", propertyBuilder.getDefaultValue(JavaType.construct(String.class)));
    }

    @Test
    public void testGetPropertyDefaultValueForArrayType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultValue = ArrayBuilders.getArrayComparator(new int[]{});
        assertEquals(defaultValue, propertyBuilder.getDefaultValue(JavaType.construct(int[].class)));
    }

    @Test
    public void testGetPropertyDefaultValueForContainerType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType containerType = Mockito.mock(JavaType.class);
        when(containerType.isReferenceType()).thenReturn(false);
        when(containerType.isContainerType()).thenReturn(true);
        assertEquals(MarkerForEmpty.MARKER_FOR_EMPTY, propertyBuilder.getDefaultValue(containerType));
    }

    @Test
    public void testGetPropertyDefaultValueForReferenceType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType referenceType = Mockito.mock(JavaType.class);
        when(referenceType.isReferenceType()).thenReturn(true);
        assertEquals(MarkerForEmpty.MARKER_FOR_EMPTY, propertyBuilder.getDefaultValue(referenceType));
    }

    @Test
    public void testGetDefaultBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyBuilder.getDefaultBean();
        verify(beanDesc).instantiateBean(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValueWithNullDefaultBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = null;
        try {
            propertyBuilder.getPropertyDefaultValue("name", Mockito.mock(AnnotatedMember.class), JavaType.construct(String.class));
        } catch (Exception e) {
            throw propertyBuilder._throwWrapped(e, "name", defaultBean);
        }
    }

    @Test
    public void testThrowWrappedException() throws Exception {
        Exception e = Mockito.mock(Exception.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyBuilder._throwWrapped(e, "name", new Object());
        verifyThrowable(e);
    }
}