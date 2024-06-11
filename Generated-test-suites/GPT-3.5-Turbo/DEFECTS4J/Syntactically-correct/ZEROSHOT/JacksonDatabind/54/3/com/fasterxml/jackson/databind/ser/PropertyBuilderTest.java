package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class PropertyBuilderTest {

    private SerializationConfig _config;
    private BeanDescription _beanDesc;
    private PropertyBuilder _propertyBuilder;

    @Before
    public void setup() {
        _config = mock(SerializationConfig.class);
        _beanDesc = mock(BeanDescription.class);
        _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        // Arrange
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        // Act
        BeanPropertyWriter result = _propertyBuilder.buildWriter(prov, propDef, declaredType, ser,
                typeSer, contentTypeSer, am, defaultUseStaticTyping);

        // Assert
        assertTrue(result instanceof BeanPropertyWriter);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        // Arrange
        Annotated a = mock(Annotated.class);
        boolean useStaticTyping = true;
        JavaType declaredType = mock(JavaType.class);
        JavaType expectedType = mock(JavaType.class);
        AnnotationIntrospector annotationIntrospector = mock(AnnotationIntrospector.class);
        when(_config.getAnnotationIntrospector()).thenReturn(annotationIntrospector);
        when(annotationIntrospector.refineSerializationType(_config, a, declaredType)).thenReturn(expectedType);
        
        // Act
        JavaType result = _propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);

        // Assert
        assertEquals(expectedType, result);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        // Arrange
        String name = "propertyName";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        Object defaultBean = mock(Object.class);

        // Act
        Object result = _propertyBuilder.getPropertyDefaultValue(name, member, type);

        // Assert
        assertEquals(null, result);
    }

    @Test
    public void testGetDefaultValue() {
        // Arrange
        JavaType type = mock(JavaType.class);
        Class<?> cls = String.class;
        when(type.getRawClass()).thenReturn(cls);


        // act
        Object result = _propertyBuilder.getDefaultValue(type);

        // Assert
        assertEquals("", result);
    }
}