package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ClassNameIdResolverTest {

    private ClassNameIdResolver classNameIdResolver;

    @Mock
    private JavaType baseType;

    @Mock
    private TypeFactory typeFactory;

    @Mock
    private Object value;

    @Mock
    private Class<?> clazz;

    @Mock
    private DatabindContext context;

    @Before
    public void setUp() {
        classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);
    }

    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, classNameIdResolver.getMechanism());
    }

    @Test
    public void testIdFromValue() {
        when(value.getClass()).thenReturn(clazz);
        when(clazz.getName()).thenReturn("java.lang.Integer");

        String id = classNameIdResolver.idFromValue(value);
        assertEquals("java.lang.Integer", id);
    }

    @Test
    public void testIdFromValueAndType() {
        when(clazz.getName()).thenReturn("java.lang.Integer");

        String id = classNameIdResolver.idFromValueAndType(value, clazz);
        assertEquals("java.lang.Integer", id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        when(typeFactory.findClass(anyString())).thenReturn(clazz);

        JavaType javaType = classNameIdResolver.typeFromId(context, "java.lang.Integer");

        verify(typeFactory, times(1)).findClass("java.lang.Integer");
        assertEquals(clazz, javaType.getRawClass());
    }

    @Test
    public void testTypeFromIdWithUnknownTypeId() throws IOException {
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(context instanceof DeserializationContext).thenReturn(true);
        when((DeserializationContext) context).thenReturn(deserializationContext);

        JavaType baseType = mock(JavaType.class);
        String id = "unknown.id";
        String reason = "no such class found";

        JavaType unknownType = classNameIdResolver.handleUnknownTypeId(baseType, id, classNameIdResolver, reason);

        assertEquals(null, unknownType);
        verify(deserializationContext, times(1)).handleUnknownTypeId(baseType, id, classNameIdResolver, reason);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithInvalidTypeId() throws IOException {
        when(typeFactory.findClass(anyString())).thenThrow(new ClassNotFoundException());

        classNameIdResolver.typeFromId(context, "InvalidTypeId");
    }
}