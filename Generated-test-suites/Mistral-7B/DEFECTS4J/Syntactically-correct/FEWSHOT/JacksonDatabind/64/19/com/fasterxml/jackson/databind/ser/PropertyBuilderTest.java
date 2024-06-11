package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private PropertyBuilder _propertyBuilder;
    private SerializationConfig _serializationConfig;
    private BeanDescription _beanDescription;
    private AnnotationIntrospector _annotationIntrospector;

    @Before
    public void setup() {
        _serializationConfig = mock(SerializationConfig.class);
        _beanDescription = mock(BeanDescription.class);
        _annotationIntrospector = mock(AnnotationIntrospector.class);

        _propertyBuilder = new PropertyBuilder(_serializationConfig, _beanDescription) {
            @Override
            protected Annotations getClassAnnotations() {
                return mock(Annotations.class);
            }
        };
    }

    @Test
    public void testBuildWriter_whenGivenDefaultValues_thenConstructsBeanPropertyWriter() throws Exception {
        // Given
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        // When
        BeanPropertyWriter bpw = _propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        // Then
        assertNotNull(bpw);
    }

    @Test
    public void testFindSerializationType_whenGivenAnnotatedMember_thenReturnsCorrectType() throws Exception {
        // Given
        Annotated a = mock(Annotated.class);
        JavaType declaredType = mock(JavaType.class);

        // When
        JavaType actualType = _propertyBuilder.findSerializationType(a, false, declaredType);

        // Then
        assertSame(declaredType, actualType);
    }

    @Test
    public void testFindSerializationType_whenGivenAnnotatedMemberWithSerializationTypeAnnotation_thenReturnsTypeAnnotatedWithSerializationType() throws Exception {
        // Given
        Annotated a = mock(Annotated.class);
        JavaType declaredType = mock(JavaType.class);
        JavaType secondaryType = mock(JavaType.class);
        when(_annotationIntrospector.refineSerializationType(_serializationConfig, a, declaredType)).thenReturn(secondaryType);

        // When
        JavaType actualType = _propertyBuilder.findSerializationType(a, false, declaredType);

        // Then
        assertSame(secondaryType, actualType);
    }

    @Test
    public void testFindSerializationType_whenGivenAnnotatedMemberWithJsonSerializeAnnotation_thenReturnsTypeAnnotatedWithJsonSerializeAnnotation() throws Exception {
        // Given
        Annotated a = mock(Annotated.class);
        JavaType declaredType = mock(JavaType.class);
        JsonSerialize jsonSerializeAnnotation = mock(JsonSerialize.class);
        when(_annotationIntrospector.findSerializationTyping(a)).thenReturn(new JsonSerialize.Typing() {
            @Override
            public JsonSerialize.Typing getSerializerType() {
                return JsonSerialize.Typing.STATIC;
            }
        });
        when(_annotationIntrospector.findSerializationTypeAnnotation(a, JsonSerialize.class)).thenReturn(jsonSerializeAnnotation);

        // When
        JavaType actualType = _propertyBuilder.findSerializationType(a, false, declaredType);

        // Then
        assertEquals(JsonSerialize.Typing.STATIC, actualType.getStaticTyping());
    }

    @Test
    public void testGetDefaultBean_whenCalledWithClassInfo_thenReturnsDefaultBeanInstance() throws Exception {
        // Given
        Object defaultBean = new Object();
        when(_beanDescription.instantiateBean(_serializationConfig.canOverrideAccessModifiers())).thenReturn(defaultBean);

        // When
        Object result = _propertyBuilder.getDefaultBean();

        // Then
        assertSame(defaultBean, result);
    }

    @Test
    public void testGetPropertyDefaultValue_whenGivenNameStringAnnotatedMemberAndType_thenReturnsDefaultValue() throws Exception {
        // Given
        String name = "testName";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        Object defaultBean = new Object();
        Object propertyDefaultValue = "testValue";
        when(member.getValue(defaultBean)).thenReturn(propertyDefaultValue);

        // When
        Object result = _propertyBuilder.getPropertyDefaultValue(name, member, type);

        // Then
        assertEquals(propertyDefaultValue, result);
    }

    @Test
    public void testGetPropertyDefaultValue_whenGivenNameStringAnnotatedMemberAndTypeWithNoDefaultInstance_thenReturnsDefaultValueOfType() throws Exception {
        // Given
        String name = "testName";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = new TypeReference<String>() {}.getType();
        Object defaultBean = null;
        Object propertyDefaultValue = "testValue";
        when(member.getValue(defaultBean)).thenThrow(new IllegalStateException());
        when(_propertyBuilder.getDefaultValue(type)).thenReturn(propertyDefaultValue);

        // When
        Object result = _propertyBuilder.getPropertyDefaultValue(name, member, type);

        // Then
        assertEquals(propertyDefaultValue, result);
    }

    @Test
    public void testGetPropertyDefaultValue_whenGivenNameStringAnnotatedMemberAndTypeWithJsonIncludeAnnotation_thenReturnsNull() throws Exception {
        // Given
        String name = "testName";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = new TypeReference<String>() {}.getType();
        JsonInclude jsonIncludeAnnotation = mock(JsonInclude.class);
        when(_annotationIntrospector.findJsonIncludeAnnotation(member)).thenReturn(jsonIncludeAnnotation);
        when(jsonIncludeAnnotation.value()).thenReturn(JsonInclude.Include.NON_DEFAULT);

        // When
        Object result = _propertyBuilder.getPropertyDefaultValue(name, member, type);

        // Then
        assertNull(result);
    }
}