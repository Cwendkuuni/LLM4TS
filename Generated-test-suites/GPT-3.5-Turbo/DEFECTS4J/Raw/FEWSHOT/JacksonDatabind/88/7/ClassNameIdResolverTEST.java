package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.EnumMap;
import java.util.EnumSet;

public class ClassNameIdResolverTest {
    private TypeIdResolverBase typeIdResolver;
    private JavaType baseType;
    private TypeFactory typeFactory;
    private DatabindContext context;
    private ObjectMapper objectMapper;
    private MapperConfig<DeserializationFeature> config;

    @Before
    public void setUp() {
        baseType = mock(JavaType.class);
        typeFactory = mock(TypeFactory.class);
        context = mock(DatabindContext.class);
        objectMapper = mock(ObjectMapper.class);
        config = mock(MapperConfig.class);
        typeIdResolver = new ClassNameIdResolver(baseType, typeFactory);
    }

    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, typeIdResolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        // No assertions, method not used with class name-based resolvers
        typeIdResolver.registerSubtype(Object.class, "");
    }

    @Test
    public void testIdFromValue() {
        Object value = new Object();
        Class<?> valueClass = value.getClass();
        String result = typeIdResolver.idFromValue(value);
        
        assertEquals(valueClass.getName(), result);
    }

    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        Class<?> valueClass = value.getClass();
        String result = typeIdResolver.idFromValueAndType(value, valueClass);
        
        assertEquals(valueClass.getName(), result);
    }

    @Test
    public void testTypeFromId() throws Exception {
        String id = "java.lang.Object";
        Class<?> cls = Object.class;
        JavaType expected = mock(JavaType.class);

        when(typeFactory.findClass(id)).thenReturn(cls);
        when(typeFactory.constructSpecializedType(baseType, cls)).thenReturn(expected);

        JavaType result = typeIdResolver.typeFromId(context, id);

        assertEquals(expected, result);
    }

    @Test
    public void testTypeFromIdWithUnknownTypeId() throws Exception {
        String id = "com.example.UnknownType";
        Class<?> cls = Object.class;
        JavaType expected = mock(JavaType.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        when(typeFactory.findClass(id)).thenThrow(ClassNotFoundException.class);
        when(objectMapper.getDeserializationContext()).thenReturn(deserializationContext);
        when(deserializationContext.handleUnknownTypeId(baseType, id, typeIdResolver, "no such class found")).thenReturn(expected);

        JavaType result = typeIdResolver.typeFromId(context, id);

        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithInvalidTypeId() throws Exception {
        String id = "com.example.InvalidType";

        when(typeFactory.findClass(id)).thenThrow(Exception.class);

        typeIdResolver.typeFromId(context, id);
    }

    @Test
    public void testTypeFromIdWithGenericTypes() throws Exception {
        String id = "com.example.GenericType<java.lang.String>";
        JavaType expected = mock(JavaType.class);

        when(typeFactory.constructFromCanonical(id)).thenReturn(expected);

        JavaType result = typeIdResolver.typeFromId(context, id);

        assertEquals(expected, result);
    }

    @Test
    public void testTypeFromIdWithEnumSetType() throws Exception {
        String id = "java.util.EnumSet<java.lang.String,EnumSet.noneOf>";
        JavaType expected = mock(JavaType.class);

        when(typeFactory.constructCollectionType(EnumSet.class, String.class)).thenReturn(expected);

        JavaType result = typeIdResolver.typeFromId(context, id);

        assertEquals(expected, result);
    }

    @Test
    public void testTypeFromIdWithEnumMapType() throws Exception {
        String id = "java.util.EnumMap<java.lang.String,java.lang.Integer>";
        JavaType expected = mock(JavaType.class);

        when(typeFactory.constructMapType(EnumMap.class, String.class, Integer.class)).thenReturn(expected);

        JavaType result = typeIdResolver.typeFromId(context, id);

        assertEquals(expected, result);
    }

    @Test
    public void test_idFrom() {
        Object value = new Object();
        Class<?> cls = value.getClass();
        String expected = cls.getName();

        String result = typeIdResolver._idFrom(value, cls, typeFactory);

        assertEquals(expected, result);
    }

    @Test
    public void test_idFrom_WithEnumClass() {
        Object value = mock(Enum.class);
        Class<?> cls = value.getClass();
        Class<?> enumClass = mock(Class.class);
        String expected = enumClass.getName();

        when(cls.isEnum()).thenReturn(false);
        when(cls.getSuperclass()).thenReturn(enumClass);

        String result = typeIdResolver._idFrom(value, cls, typeFactory);

        assertEquals(expected, result);
    }

    @Test
    public void test_idFrom_WithJavaUtilClass() {
        Object value = new Object();
        Class<?> cls = value.getClass();
        String javaUtilClassName = "java.util.ArrayList";
        String expected = cls.getName();

        when(cls.getName()).thenReturn(javaUtilClassName);

        String result = typeIdResolver._idFrom(value, cls, typeFactory);

        assertEquals(expected, result);
    }

    @Test
    public void test_idFrom_WithInnerClass() {
        Object value = new Object();
        Class<?> cls = value.getClass();
        String outerClassName = "com.example.OuterClass";
        String expected = cls.getName();

        when(ClassUtil.getOuterClass(cls)).thenReturn(outerClassName);
        when(ClassUtil.getOuterClass(baseType.getRawClass())).thenReturn(null);

        String result = typeIdResolver._idFrom(value, cls, typeFactory);

        assertEquals(expected, result);
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        String expected = "class name used as type id";

        String result = typeIdResolver.getDescForKnownTypeIds();

        assertEquals(expected, result);
    }
}