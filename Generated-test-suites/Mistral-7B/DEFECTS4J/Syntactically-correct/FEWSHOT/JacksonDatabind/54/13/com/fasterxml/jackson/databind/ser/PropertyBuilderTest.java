package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Object serializationType = propertyBuilder.findSerializationType(null, false, Object.class);
        assertNull(serializationType);

        AnnotatedMember am = new AnnotatedClassMember("test", null, null, null, null, null, null);
        SerializationConfig config = null;
        BeanDescription beanDesc = null;
        JavaType declaredType = new TypeReference<Object>() {}.getType();
        Class<?> serClass = Object.class;
        JsonSerialize.Typing serializationTyping = JsonSerialize.Typing.DEFAULT_TYPING;
        JavaType secondary = new TypeReference<Object>() {}.getType();

        assertEquals(Object.class, propertyBuilder.findSerializationType(am, false, declaredType).getRawClass());
        assertEquals(Object.class, propertyBuilder.findSerializationType(am, true, declaredType).getRawClass());

        assertNotNull(propertyBuilder.findSerializationType(am, false, declaredType));
        assertNotNull(propertyBuilder.findSerializationType(am, true, declaredType));

        assertEquals(serClass, propertyBuilder.findSerializationType(am, false, declaredType).getRawClass());
        assertEquals(secondary, propertyBuilder.findSerializationType(am, true, declaredType).getRawClass());

        Class<?> rawDeclared = secondary.getRawClass();
        assertTrue(serClass.isAssignableFrom(rawDeclared));

        Annotated a = null;
        assertEquals(Object.class, propertyBuilder.findSerializationType(a, false, declaredType).getRawClass());
        assertEquals(Object.class, propertybuilder.findSerializationType(a, true, declaredType).getRawClass());

        a = am;
        JsonSerialize.Typing typing = JsonSerialize.Typing.STATIC;
        assertEquals(declaredType, propertyBuilder.findSerializationType(a, true, declaredType));
        assertEquals(declaredType.withStaticTyping(), propertyBuilder.findSerializationType(a, true, declaredType));

        assertEquals(declaredType, propertyBuilder.findSerializationType(a, false, declaredType));
        assertEquals(declaredType, propertyBuilder.findSerializationType(a, false, declaredType).withoutStaticTyping());

        a = am;
        assertEquals(declaredType, propertyBuilder.findSerializationType(a, false, declaredType));
        assertEquals(secondary, propertyBuilder.findSerializationType(a, true, declaredType));

        // Check that the exception is thrown when the types are not related
        try {
            propertyBuilder.findSerializationType(a, true, new TypeReference<String>() {}.getType());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetDefaultBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        BeanDescription beanDesc = null;
        SerializationConfig config = null;
        propertyBuilder = new PropertyBuilder(config, beanDesc);

        defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        beanDesc = new BeanDescription(null, null, null, null);
        propertyBuilder = new PropertyBuilder(config, beanDesc);

        defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        try {
            propertyBuilder.instantiateBean(true);
            fail();
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        assertEquals(null, propertyBuilder.getPropertyDefaultValue("test", null, Object.class));

        BeanDescription beanDesc = null;
        SerializationConfig config = null;
        propertyBuilder = new PropertyBuilder(config, beanDesc);

        assertEquals(null, propertyBuilder.getPropertyDefaultValue("test", null, Object.class));

        beanDesc = new BeanDescription(null, null, null, null);
        propertyBuilder = new PropertyBuilder(config, beanDesc);

        assertEquals(null, propertyBuilder.getPropertyDefaultValue("test", null, Object.class));

        try {
            propertyBuilder.memberValue("test", null);
            fail();
        } catch (Exception e) {
            // expected
        }

        beanDesc = new BeanDescription(new Class<?>[0], null);
        propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object defaultBean = new Object[0];
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("test", null, Object.class);
        assertNull(propertyDefaultValue);

        propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("test", null, new TypeReference<Object[]>() {}.getType());
        assertEquals(defaultBean, propertyDefaultValue);

        beanDesc = new BeanDescription(new Class<?>[]{String.class}, null);
        propertyBuilder = new PropertyBuilder(config, beanDesc);

        Class<?> clazz = String.class;
        Object propertyDefaultValueString = propertyBuilder.getPropertyDefaultValue("test", null, clazz);
        assertEquals("", propertyDefaultValueString);

        beanDesc = new BeanDescription(new Class<?>[]{String.class}, null);
        propertyBuilder = new PropertyBuilder(config, beanDesc);

        clazz = String[].class;
        Object propertyDefaultValueArrayString = propertyBuilder.getPropertyDefaultValue("test", null, clazz);
        assertArrayEquals(new String[0], propertyDefaultValueArrayString);

        beanDesc = new BeanDescription(new Class<?>[]{MyClass.class}, null);
        propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object propertyDefaultValueMyClass = propertyBuilder.getPropertyDefaultValue("test", null, MyClass.class);
        assertNull(propertyDefaultValueMyClass);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        assertEquals(null, propertyBuilder.getDefaultValue(null));

        BeanDescription beanDesc = null;
        SerializationConfig config = null;
        propertyBuilder = new PropertyBuilder(config, beanDesc);

        assertEquals(null, propertyBuilder.getDefaultValue(null));

        beanDesc = new BeanDescription(new Class<?>[0], null);
        propertyBuilder = new PropertyBuilder(config, beanDesc);

        assertEquals(null, propertyBuilder.getDefaultValue(null));

        assertEquals(0, propertyBuilder.getDefaultValue(int.class));
        assertEquals(0L, propertyBuilder.getDefaultValue(long.class));
        assertEquals(false, propertyBuilder.getDefaultValue(boolean.class));
        assertEquals("", propertyBuilder.getDefaultValue(String.class));
        assertEquals(new Object[0], propertyBuilder.getDefaultValue(Object[].class));
        assertEquals(new String[0], propertyBuilder.getDefaultValue(String[][].class));
        assertEquals(new MyClass[0], propertyBuilder.getDefaultValue(MyClass[].class));

        beanDesc = new BeanDescription(new Class<?>[]{String.class}, null);
        propertyBuilder = new PropertyBuilder(config, beanDesc);

        assertEquals(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(Object.class));
        assertEquals(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(int[].class));
        assertEquals(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(String[].class));
        assertEquals(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(MyClass[].class));
    }
}

class MyClass {}