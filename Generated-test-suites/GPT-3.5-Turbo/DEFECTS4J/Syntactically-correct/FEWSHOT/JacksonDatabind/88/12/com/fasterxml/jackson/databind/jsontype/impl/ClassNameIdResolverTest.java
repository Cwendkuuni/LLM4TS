package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class ClassNameIdResolverTest {
    @Mock
    private JavaType mockBaseType;

    @Mock
    private TypeFactory mockTypeFactory;

    private ClassNameIdResolver classNameIdResolver;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        classNameIdResolver = new ClassNameIdResolver(mockBaseType, mockTypeFactory);
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
        Class<?> cls = value.getClass();

        String expected = classNameIdResolver._idFrom(value, cls, mockTypeFactory);
        assertEquals(expected, classNameIdResolver.idFromValue(value));
    }

    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        Class<?> cls = value.getClass();

        String expected = classNameIdResolver._idFrom(value, cls, mockTypeFactory);
        assertEquals(expected, classNameIdResolver.idFromValueAndType(value, cls));
    }

    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext mockContext = mock(DatabindContext.class);
        String id = "com.example.MyClass";
        JavaType expected = classNameIdResolver._typeFromId(id, mockContext);
        assertEquals(expected, classNameIdResolver.typeFromId(mockContext, id));
    }

    @Test
    public void testTypeFromIdUnknown() throws IOException {
        DeserializationContext mockContext = mock(DeserializationContext.class);
        String id = "com.unknown.UnknownClass";

        TypeFactory mockTypeFactory = mock(TypeFactory.class);

        JavaType t = classNameIdResolver._baseType;
        String typeName = _idFrom(t, classNameIdResolver._baseType.getRawClass(), mockTypeFactory);

        String expectedMessage = "no such class found";
        try {
            classNameIdResolver.typeFromId(mockContext, id);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid type id '"+id+"' (for id type 'Id.class'): "+expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testTypeFromIdException() throws IOException {
        DatabindContext mockContext = mock(DatabindContext.class);
        String id = "com.example.MyClass";

        TypeFactory mockTypeFactory = mock(TypeFactory.class);

        JavaType t = classNameIdResolver._baseType;
        String typeName = _idFrom(t, classNameIdResolver._baseType.getRawClass(), mockTypeFactory);

        String expectedMessage = "Invalid type id '"+id+"' (for id type 'Id.class')";

        try {
            classNameIdResolver.typeFromId(mockContext, id);
        } catch (IllegalArgumentException e) {
            assertEquals(expectedMessage, e.getMessage().substring(0, expectedMessage.length()));
        }
    }

    @Test
    public void testTypeFromIdGeneric() throws IOException {
        DatabindContext mockContext = mock(DatabindContext.class);
        String id = "java.util.ArrayList<java.lang.String>";

        TypeFactory mockTypeFactory = mock(TypeFactory.class);
        Class<?> cls;

        JavaType t = classNameIdResolver._baseType;
        String typeName = _idFrom(t, classNameIdResolver._baseType.getRawClass(), mockTypeFactory);

        JavaType expected = mockTypeFactory.constructFromCanonical(typeName);
        assertEquals(expected, classNameIdResolver.typeFromId(mockContext, id));
    }

    @Test
    public void testTypeFromIdArray() throws IOException {
        DatabindContext mockContext = mock(DatabindContext.class);
        String id = "java.util.List[]";

        TypeFactory mockTypeFactory = mock(TypeFactory.class);
        Class<?> cls;

        JavaType t = classNameIdResolver._baseType;
        String typeName = _idFrom(t, classNameIdResolver._baseType.getRawClass(), mockTypeFactory);

        JavaType expected = mockTypeFactory.constructArrayType(mockTypeFactory.constructFromCanonical(typeName));
        assertEquals(expected, classNameIdResolver.typeFromId(mockContext, id));
    }

    protected String _idFrom(Object value, Class<?> cls, TypeFactory typeFactory)
    {
        if (Enum.class.isAssignableFrom(cls)) {
            if (!cls.isEnum()) {
                cls = cls.getSuperclass();
            }
        }
        String str = cls.getName();
        if (str.startsWith("java.util")) {
            if (value instanceof EnumSet<?>) {
                Class<?> enumClass = ClassUtil.findEnumType((EnumSet<?>) value);
                str = typeFactory.constructCollectionType(EnumSet.class, enumClass).toCanonical();
            } else if (value instanceof EnumMap<?,?>) {
                Class<?> enumClass = ClassUtil.findEnumType((EnumMap<?,?>) value);
                Class<?> valueClass = Object.class;
                str = typeFactory.constructMapType(EnumMap.class, enumClass, valueClass).toCanonical();
            } else {
                String end = str.substring(9);
                if ((end.startsWith(".Arrays$") || end.startsWith(".Collections$"))
                       && str.indexOf("List") >= 0) {
                    str = "java.util.ArrayList";
                }
            }
        } else if (str.indexOf('$') >= 0) {
            Class<?> outer = ClassUtil.getOuterClass(cls);
            if (outer != null) {
                if (ClassUtil.getOuterClass(_baseType.getRawClass()) == null) {
                    cls = _baseType.getRawClass();
                    str = cls.getName();
                }
            }
        }
        return str;
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        assertEquals("class name used as type id", classNameIdResolver.getDescForKnownTypeIds());
    }
}