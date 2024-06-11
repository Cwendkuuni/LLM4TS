package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.*;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    private ClassNameIdResolver classNameIdResolver;

    @Before
    public void setUp() {
        JavaType baseType = TypeFactory.unknownType();
        TypeFactory typeFactory = new TypeFactory();
        classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);
    }

    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, classNameIdResolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        // not used with class name - based resolvers
    }

    @Test
    public void testIdFromValue() {
        Object value = new Object();
        String id = classNameIdResolver.idFromValue(value);
        assertNotNull(id);
    }

    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        Class<?> type = value.getClass();
        String id = classNameIdResolver.idFromValueAndType(value, type);
        assertNotNull(id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext context = null; // TODO: Initialize context
        String id = ""; // TODO: Initialize id
        JavaType javaType = classNameIdResolver.typeFromId(context, id);
        assertNotNull(javaType);
    }

    @After
    public void tearDown() {
        classNameIdResolver = null;
    }
}