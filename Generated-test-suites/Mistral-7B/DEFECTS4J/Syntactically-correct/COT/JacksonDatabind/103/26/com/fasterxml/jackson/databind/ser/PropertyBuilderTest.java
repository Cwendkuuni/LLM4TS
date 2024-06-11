package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSerialize;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.test.BaseTest;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.*;

public class PropertyBuilderTest extends BaseTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private AnnotationIntrospector annotationIntrospector;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() throws Exception {
        config = mock(SerializationConfig.class);
        beanDesc = mock(BeanDescription.class);
        annotationIntrospector = mock(AnnotationIntrospector.class);
        when(config.getAnnotationIntrospector()).thenReturn(annotationIntrospector);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testConstructor() {
        assertNotNull(propertyBuilder);
    }

    @Test
    public void testGetClassAnnotations() throws Exception {
        when(beanDesc.getClassAnnotations()).thenReturn(mock(Annotations.class));
        Annotations result = propertyBuilder.getClassAnnotations();
        verify(beanDesc).getClassAnnotations();
        assertSame(result, propertyBuilder.getClassAnnotations());
    }

    @Test
    public void testBuildWriter_NullAllArgs() {
        JavaType declaredType = TypeFactory.unknownType();
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        assertNull(propertyBuilder.buildWriter(null, propDef, declaredType, null, null, null, null, false));
    }

    @Test
    public void testBuildWriter_TypeSerializerNull() throws Exception {
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.unknownType();
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        assertNull(propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, null, am, false));
    }

    @Test
    public void testBuildWriter_ContentTypeSerializerNull() throws Exception {
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.unknownType();
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = false;
        BeanPropertyWriter result = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        verify(prov, times(1)).reportBadPropertyDefinition(beanDesc, propDef, "missing contentTypeSerializer");
        assertNull(result);
    }

    @Test
    public void testBuildWriter_Success() throws Exception {
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.unknownType();
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        BeanPropertyWriter result = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        verify(prov, times(1)).getSerializerProvider();
        verifyNoMoreInteractions(prov);
        verify(beanDesc, times(1)).getClassAnnotations();
        verify(am, times(1)).getGenericType();
        verify(am, times(1)).getName();
        verify(am, times(1)).getRawType();
        verify(propDef, times(1)).getBeanDescription();
        verify(propDef, times(1)).getAccessor();
        verify(propDef, times(1)).getBeanProperty();
        verify(propDef, times(1)).isContainer();
        verify(propDef, times(1)).findViews();
        verifyNoMoreInteractions(beanDesc, propDef, am);
        assertNotNull(result);
        assertNull(result.getValueToSuppress());
        assertFalse(result.suppressNulls());
    }

    @Test
    public void testFindSerializationType_AnnotationPresent() throws Exception {
        Annotated a = mock(Annotated.class);
        when(a.getAnnotations()).thenReturn(new JsonSerialize<>().withType(TypeFactory.unknownType()));
        JavaType result = propertyBuilder.findSerializationType(a, true, TypeFactory.unknownType());
        verify(annotationIntrospector).refineSerializationType(eq(config), eq(a), eq(TypeFactory.unknownType()));
        assertSame(result, TypeFactory.unknownType().withStaticTyping());
    }

    @Test
    public void testFindSerializationType_AnnotationAbsent() throws Exception {
        Annotated a = mock(Annotated.class);
        JavaType result = propertyBuilder.findSerializationType(a, true, TypeFactory.unknownType());
        assertNull(result);
    }

    @Test
    public void testGetDefaultBean() throws Exception {
        when(beanDesc.instantiateBean(Boolean.FALSE)).thenReturn(new Object());
        Object result = propertyBuilder.getDefaultBean();
        verify(beanDesc, times(1)).instantiateBean(Boolean.FALSE);
        assertSame(result, propertyBuilder.getDefaultBean());
    }

    @Test
    public void testGetDefaultBean_Null() throws Exception {
        when(beanDesc.instantiateBean(Boolean.FALSE)).thenThrow(new IllegalStateException());
        Object result = propertyBuilder.getDefaultBean();
        verify(beanDesc, times(1)).instantiateBean(Boolean.FALSE);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_NullPropName() throws Exception {
        String propName = null;
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = TypeFactory.unknownType();
        Object result = propertyBuilder.getPropertyDefaultValue(propName, member, type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_NullMember() throws Exception {
        String propName = "propName";
        AnnotatedMember member = null;
        JavaType type = TypeFactory.unknownType();
        Object result = propertyBuilder.getPropertyDefaultValue(propName, member, type);
    }

    @Test
    public void testGetPropertyDefaultValue_Success() throws Exception {
        String propName = "propName";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = TypeFactory.unknownType();
        Object result = propertyBuilder.getPropertyDefaultValue(propName, member, type);
        verify(beanDesc, times(1)).instantiateBean(Boolean.FALSE);
        verify(member, times(1)).getValue(anyObject());
        Object defaultValue = "defaultValue";
        when(member.getValue(anyObject())).thenReturn(defaultValue);
        assertSame(result, defaultValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testgetDefaultValue_NullType() throws Exception {
        JavaType type = null;
        Object result = propertyBuilder.getDefaultValue(type);
        verifyNoInteractions(beanUtil);
        assertNull(result);
    }

    @Test
    public void testgetDefaultValue_SimpleType() throws Exception {
        JavaType type = TypeFactory.constructSimpleType(int.class, "int");
        Object result = propertyBuilder.getDefaultValue(type);
        verify(beanUtil, times(1)).getDefaultValue(type);
        assertEquals(Integer.TYPE.getDefaultValue(), result);
    }

    @Test
    public void testgetDefaultValue_ArrayType() throws Exception {
        JavaType type = TypeFactory.constructArrayType(int[].class);
        Object result = propertyBuilder.getDefaultValue(type);
        verify(beanUtil, times(1)).getDefaultValue(type);
        assertArrayEquals(new int[0], result);
    }

    @Test
    public void testgetDefaultValue_ContainerType() throws Exception {
        JavaType type = TypeFactory.constructCollectionType(ArrayList.class, String.class);
        Object result = propertyBuilder.getDefaultValue(type);
        verify(beanUtil, times(1)).getDefaultValue(type);
        assertNotNull(result);
        assertTrue(result instanceof ArrayList);
        assertEquals(0, ((ArrayList) result).size());
    }

    @Test
    public void testThrowWrapped() throws Exception {
        Exception e = new Exception("Exception message");
        String propName = "propName";
        Object defaultBean = new Object();
        Object result = propertyBuilder._throwWrapped(e, propName, defaultBean);
        verify(defaultBean, times(1)).getClass();
        verify(beanDesc, times(1)).instantiateBean(Boolean.FALSE);
        verify(defaultBean, times(1)).getClass();
        assertEquals(propName, e.getMessage());
        assertEquals("Failed to get property 'propName' of default "+defaultBean.getClass().getName()+" instance", result.getMessage());
    }

    @After
    public void tearDown() throws Exception {
        reset(beanDesc, annotationIntrospector, config, ser, typeSer, contentTypeSer, prov, am);
    }
}