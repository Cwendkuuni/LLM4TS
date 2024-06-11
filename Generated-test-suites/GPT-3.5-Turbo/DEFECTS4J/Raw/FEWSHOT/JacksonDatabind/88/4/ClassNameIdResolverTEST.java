package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassNameIdResolverTest {
    private static final String CLASS_NAME = "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver";
    private static final String REGEX = ".*";

    @Mock
    private JavaType baseType;

    @Mock
    private TypeFactory typeFactory;

    @Mock
    private Object value;

    @Mock
    private DatabindContext context;

    @InjectMocks
    private ClassNameIdResolver idResolver;

    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, idResolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        idResolver.registerSubtype(Object.class, "Object");
        // Verify that the method is not used
        verifyZeroInteractions(Object.class);
    }

    @Test
    public void testIdFromValue() {
        String id = idResolver.idFromValue(value);
        assertEquals(CLASS_NAME, id);
    }

    @Test
    public void testIdFromValueAndType() {
        String id = idResolver.idFromValueAndType(value, Object.class);
        assertEquals(CLASS_NAME, id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        JavaType expectedType = mock(JavaType.class);

        when(idResolver._typeFromId(CLASS_NAME, context)).thenReturn(expectedType);

        JavaType actualType = idResolver.typeFromId(context, CLASS_NAME);

        assertEquals(expectedType, actualType);
    }

    @Test
    public void testTypeFromIdHandlingUnknownTypeId() throws IOException {
        JavaType unknownType = mock(JavaType.class);

        when(idResolver._typeFromId(CLASS_NAME, context)).thenThrow(new ClassNotFoundException());

        when(context instanceof DeserializationContext).thenReturn(true);
        when(((DeserializationContext) context).handleUnknownTypeId(any(), any(), any(), any()))
                .thenReturn(unknownType);

        JavaType actualType = idResolver.typeFromId(context, CLASS_NAME);

        assertEquals(unknownType, actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithInvalidTypeId() throws IOException {
        when(idResolver._typeFromId(CLASS_NAME, context)).thenThrow(new Exception());

        idResolver.typeFromId(context, CLASS_NAME);
    }

    @Test
    public void testTypeFromIdWithNonParameterizedId() throws IOException {
        JavaType expectedType = mock(JavaType.class);

        when(typeFactory.constructFromCanonical(CLASS_NAME)).thenReturn(expectedType);

        JavaType actualType = idResolver.typeFromId(context, CLASS_NAME);

        assertEquals(expectedType, actualType);
    }

    @Test
    public void testTypeFromIdWithParameterizedId() throws IOException {
        JavaType expectedType = mock(JavaType.class);
        when(typeFactory.findClass(CLASS_NAME)).thenReturn(Object.class);
        when(typeFactory.constructSpecializedType(baseType, Object.class)).thenReturn(expectedType);

        JavaType actualType = idResolver.typeFromId(context, CLASS_NAME);

        assertEquals(expectedType, actualType);
    }

    @Test
    public void testTypeFromIdWithException() throws IOException {
        when(typeFactory.findClass(CLASS_NAME)).thenThrow(new ClassNotFoundException());

        assertNull(idResolver.typeFromId(context, CLASS_NAME));
    }

    @Test
    public void testTypeFromIdWithIllegalArgumentException() throws IOException {
        when(typeFactory.findClass(CLASS_NAME)).thenThrow(new IllegalArgumentException());

        assertNull(idResolver.typeFromId(context, CLASS_NAME));
    }

    @Test
    public void testIdFrom() {
        assertEquals(CLASS_NAME, idResolver._idFrom(value, Object.class, typeFactory));
    }

    @Test
    public void testIdFromWithEnumSubType() {
        assertEquals(CLASS_NAME, idResolver._idFrom(mock(Enum.class), Object.class, typeFactory));
    }

    @Test
    public void testIdFromWithJavaUtilClass() {
        assertEquals("java.util.ArrayList", idResolver._idFrom(mock(ClassUtil.class), Object.class, typeFactory));
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        assertEquals("class name used as type id", idResolver.getDescForKnownTypeIds());
    }
}