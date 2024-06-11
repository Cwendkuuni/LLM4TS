
package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.*;
import org.mockito.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyBuilderTest {
    private SerializationConfig config;
    private BeanDescription beanDesc;
    private AnnotationIntrospector annotationIntrospector;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        config = mock(SerializationConfig.class);
        beanDesc = mock(BeanDescription.class);
        annotationIntrospector = mock(AnnotationIntrospector.class);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        when(config.getAnnotationIntrospector()).thenReturn(annotationIntrospector);
    }

    @After
    public void tearDown() {
        config = null;
        beanDesc = null;
        annotationIntrospector = null;
        propertyBuilder = null;
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
        BeanPropertyWriter result = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        // Assert
        assertNotNull(result);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        // Arrange
        Annotated a = mock(Annotated.class);
        boolean useStaticTyping = true;
        JavaType declaredType = mock(JavaType.class);
        // Act
        JavaType result = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        // Assert
        assertNull(result);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        // Arrange
        String name = "test";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        Object defaultBean = null;
        // Act
        Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        // Assert
        assertNull(result);
    }

    @Test
    public void testGetDefaultValueWithPrimitiveType() {
        // Arrange
        JavaType type = mock(JavaType.class);
        Class<?> cls = int.class;
        when(type.getRawClass()).thenReturn(cls);
        // Act
        Object result = propertyBuilder.getDefaultValue(type);
        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetDefaultValueWithStringType() {
        // Arrange
        JavaType type = mock(JavaType.class);
        Class<?> cls = String.class;
        when(type.getRawClass()).thenReturn(cls);
        // Act
        Object result = propertyBuilder.getDefaultValue(type);
        // Assert
        assertEquals("", result);
    }

    @Test
    public void testGetDefaultValueWithContainerType() {
        // Arrange
        JavaType type = mock(JavaType.class);
        when(type.isContainerType()).thenReturn(true);
        // Act
        Object result = propertyBuilder.getDefaultValue(type);
        // Assert
        assertEquals(JsonInclude.Include.NON_EMPTY, result);
    }

    @Test
    public void testGetDefaultValueWithReferenceType() {
        // Arrange
        JavaType type = mock(JavaType.class);
        when(type.isReferenceType()).thenReturn(true);
        // Act
        Object result = propertyBuilder.getDefaultValue(type);
        // Assert
        assertEquals(JsonInclude.Include.NON_EMPTY, result);
    }

    @Test
    public void testGetDefaultValueWithObjectType() {
        // Arrange
        JavaType type = mock(JavaType.class);
        when(type.getRawClass()).thenReturn(Object.class);
        // Act
        Object result = propertyBuilder.getDefaultValue(type);
        // Assert
        assertNull(result);
    }

    @Test
    public void testThrowWrapped() {
        // Arrange
        String propName = "test";
        Object defaultBean = new Object();
        Exception e = new Exception();
        // Act
        try {
            propertyBuilder._throwWrapped(e, propName, defaultBean);
        } catch (IllegalArgumentException ex) {
            // Assert
            assertNotNull(ex);
        }
    }
}
