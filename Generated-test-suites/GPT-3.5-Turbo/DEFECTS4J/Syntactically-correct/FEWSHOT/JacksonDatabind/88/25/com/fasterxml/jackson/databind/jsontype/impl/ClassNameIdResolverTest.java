package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.util.*;

import org.junit.Test;
import org.mockito.Mockito;

public class ClassNameIdResolverTest {

    ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(JavaType.builder().build(), TypeFactory.defaultInstance());

    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, classNameIdResolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        Class<?> type = mock(Class.class);
        String name = "";
        classNameIdResolver.registerSubtype(type, name);
        // Add assertion here
    }

    @Test
    public void testIdFromValue() {
        Object value = new Object();
        String result = classNameIdResolver.idFromValue(value);
        // Add assertion here
    }

    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        Class<?> type = mock(Class.class);
        String result = classNameIdResolver.idFromValueAndType(value, type);
        // Add assertion here
    }

    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext context = mock(DatabindContext.class);
        String id = "";
        JavaType result = classNameIdResolver.typeFromId(context, id);
        // Add assertion here
    }

    @Test
    public void test_typeFromId() throws IOException {
        DatabindContext ctxt = mock(DatabindContext.class);
        String id = "";
        JavaType result = classNameIdResolver._typeFromId(id, ctxt);
        // Add assertion here
    }

    @Test
    public void test_idFrom() {
        Object value = new Object();
        Class<?> cls = mock(Class.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        String result = classNameIdResolver._idFrom(value, cls, typeFactory);
        // Add assertion here
    }

    @Test
    public void test_idFrom__with_Enum() {
        Enum<?> value = mock(Enum.class);
        Class<?> cls = mock(Class.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        String result = classNameIdResolver._idFrom(value, cls, typeFactory);
        // Add assertion here
    }

    @SuppressWarnings("deprecation")
    @Test
    public void test_idFrom__with_java_util() {
        Object value = new Object();
        Class<?> cls = mock(Class.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        String result = classNameIdResolver._idFrom(value, cls, typeFactory);
        // Add assertion here
    }

    @Test
    public void test_idFrom__with_inner_class() {
        Object value = new Object();
        Class<?> cls = mock(Class.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        String result = classNameIdResolver._idFrom(value, cls, typeFactory);
        // Add assertion here
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        String result = classNameIdResolver.getDescForKnownTypeIds();
        // Add assertion here
    }
}