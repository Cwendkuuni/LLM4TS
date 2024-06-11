package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String[].class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(declaredType, serializationType);

        JsonInclude inclusion = JsonInclude.Include.NON_DEFAULT;
        JsonInclude annotation = Mockito.mock(JsonInclude.class);
        when(annotation.getValueInclusion()).thenReturn(inclusion);
        when(am.getAnnotation(JsonInclude.class)).thenReturn(annotation);

        serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertNull(serializationType);

        Class<?> serClass = Mockito.mock(Class.class);
        when(serClass.isAssignableFrom(declaredType.getRawClass())).thenReturn(true);
        when(am.getMemberName()).thenReturn("test");
        when(am.getAnnotated()).thenReturn(serClass);

        serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(serClass, serializationType.getRawClass());

        when(am.getAnnotated()).thenReturn(null);
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> propertyBuilder.findSerializationType(am, false, declaredType));
        assertEquals("Illegal concrete-type annotation for method 'test': class class com.fasterxml.jackson.databind.ser.PropertyBuilderTest$1 at com.fasterxml.jackson.databind.ser.PropertyBuilder.findSerializationType(PropertyBuilder.java:226)", exception.getMessage());
    }

    @Test
    public void testGetDefaultBean() {
        BeanDescription beanDescription = Mockito.mock(BeanDescription.class);
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        when(beanDescription.getClassInfo()).thenReturn(new ClassInfo(Object.class));
        when(beanDescription.instantiateBean(Boolean.TRUE)).thenReturn(new Object());
        when(beanDescription.getClassAnnotations()).thenReturn(Mockito.mock(Annotations.class));
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);

        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean);

        when(beanDescription.instantiateBean(Boolean.FALSE)).thenThrow(new InstantiationException());
        defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        when(beanDescription.getClassInfo()).thenReturn(new ClassInfo(String.class));
        defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyDescription propertyDescription = Mockito.mock(PropertyDescription.class);
        BeanDescription beanDescription = Mockito.mock(BeanDescription.class);
        when(beanDescription.getPropertyDescriptions()).thenReturn(Arrays.asList(propertyDescription));
        when(propertyDescription.getName()).thenReturn("test");
        when(propertyDescription.getAnnotatedMember()).thenReturn(Mockito.mock(AnnotatedMember.class));

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        Object defaultBean = new Object();
        when(propertyDescription.getAnnotatedMember().getValue(defaultBean)).thenReturn("defaultValue");

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("test", Mockito.mock(AnnotatedMember.class), TypeFactory.defaultInstance().constructType(String.class));
        assertEquals("defaultValue", propertyDefaultValue);

        when(propertyDescription.getAnnotatedMember().getValue(defaultBean)).thenThrow(new IllegalAccessException());
        Throwable exception = assertThrows(RuntimeException.class, () -> propertyBuilder.getPropertyDefaultValue("test", Mockito.mock(AnnotatedMember.class), TypeFactory.defaultInstance().constructType(String.class)));
        assertEquals("Failed to get property 'test' of default Object@15db9742 (class java.lang.Object) instance", exception.getMessage());
    }

    @Test
    public void testGetDefaultValue() {
        JavaType jsonType = TypeFactory.defaultInstance().constructType(String.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        Object defaultValue = propertyBuilder.getDefaultValue(jsonType);
        assertNull(defaultValue);

        JsonInclude inclusion = JsonInclude.Include.NON_EMPTY;
        when(jsonType.isContainerType()).thenReturn(true);
        defaultValue = propertyBuilder.getDefaultValue(jsonType);
        assertEquals(inclusion, defaultValue);

        when(jsonType.isContainerType()).thenReturn(false);
        when(jsonType.getRawClass()).thenReturn(String[].class);
        defaultValue = propertyBuilder.getDefaultValue(jsonType);
        assertEquals(Arrays.asList(), defaultValue);

        when(jsonType.getRawClass()).thenReturn(Integer.TYPE);
        defaultValue = propertyBuilder.getDefaultValue(jsonType);
        assertEquals(0, defaultValue);

        when(jsonType.getRawClass()).thenReturn(Integer.class);
        defaultValue = propertyBuilder.getDefaultValue(jsonType);
        assertEquals(0, defaultValue);

        when(jsonType.getRawClass()).thenReturn(Float.TYPE);
        defaultValue = propertyBuilder.getDefaultValue(jsonType);
        assertEquals(0.0F, defaultValue);

        when(jsonType.getRawClass()).thenReturn(Float.class);
        defaultValue = propertyBuilder.getDefaultValue(jsonType);
        assertEquals(0.0F, defaultValue);

        when(jsonType.getRawClass()).thenReturn(Double.TYPE);
        defaultValue = propertyBuilder.getDefaultValue(jsonType);
        assertEquals(0.0, defaultValue);

        when(jsonType.getRawClass()).thenReturn(Double.class);
        defaultValue = propertyBuilder.getDefaultValue(jsonType);
        assertEquals(0.0, defaultValue);
    }
}