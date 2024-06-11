package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class JavaTypeTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testJavaTypeBase() throws Exception {
        // Test creation of JavaType instances with no handlers
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType javaType = TypeFactory.defaultInstance().constructType(JavaType.class);

        assertEquals("java.lang.String", stringType.getErasedSignature());
        assertEquals("java.lang.Integer", intType.getErasedSignature());
        assertEquals("com.fasterxml.jackson.databind.JavaType", javaType.getErasedSignature());

        assertFalse(stringType.isContainerType());
        assertFalse(intType.isContainerType());
        assertFalse(javaType.isContainerType());

        // Test creation of JavaType instances with handlers
        JavaType stringTypeWithHandler = new SimpleType("java.lang.String", null, mapper.getFactory().getValueSerializer(String.class));
        JavaType intTypeWithHandler = new SimpleType("java.lang.Integer", null, mapper.getFactory().getValueSerializer(Integer.class));
        JavaType javaTypeWithHandler = new SimpleType("com.fasterxml.jackson.databind.JavaType", null, mapper.getFactory().getValueSerializer(JavaType.class));

        assertEquals("java.lang.String", stringTypeWithHandler.getErasedSignature());
        assertEquals("java.lang.Integer", intTypeWithHandler.getErasedSignature());
        assertEquals("com.fasterxml.jackson.databind.JavaType", javaTypeWithHandler.getErasedSignature());

        assertFalse(stringTypeWithHandler.isContainerType());
        assertFalse(intTypeWithHandler.isContainerType());
        assertFalse(javaTypeWithHandler.isContainerType());

        // Test constructing and copying JavaType instances with handlers
        JavaType stringTypeWithValueHandler = TypeFactory.defaultInstance().constructType(String.class).withValueHandler(mapper.getFactory().getValueSerializer(String.class));
        JavaType stringTypeCopyWithValueHandler = stringTypeWithValueHandler.copy();

        assertEquals("java.lang.String", stringTypeWithValueHandler.getErasedSignature());
        assertEquals("java.lang.String", stringTypeCopyWithValueHandler.getErasedSignature());

        assertSame(mapper.getFactory().getValueSerializer(String.class), stringTypeWithValueHandler.getValueHandler());
        assertSame(mapper.getFactory().getValueSerializer(String.class), stringTypeCopyWithValueHandler.getValueHandler());

        JavaType stringTypeWithTypeHandler = TypeFactory.defaultInstance().constructType(String.class).withTypeHandler(mapper.getFactory().createTypeSerializer(String.class));
        JavaType stringTypeCopyWithTypeHandler = stringTypeWithTypeHandler.copy();

        assertEquals("java.lang.String", stringTypeWithTypeHandler.getErasedSignature());
        assertEquals("java.lang.String", stringTypeCopyWithTypeHandler.getErasedSignature());

        assertSame(mapper.getFactory().createTypeSerializer(String.class), stringTypeWithTypeHandler.getTypeHandler());
        assertSame(mapper.getFactory().createTypeSerializer(String.class), stringTypeCopyWithTypeHandler.getTypeHandler());

        // Test narrowing and widening of JavaType instances
        JavaType stringArrayType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType stringListType = TypeFactory.defaultInstance().constructType(java.util.List.class).narrowBy(String.class);

        assertEquals("java.lang.String[]", stringArrayType.getErasedSignature());
        assertEquals("[Ljava.lang.String;", stringArrayType.getGenericSignature());
        assertEquals("java.util.List", stringListType.getErasedSignature());
        assertEquals("[Ljava.lang.Object;", stringListType.getGenericSignature());

        assertTrue(stringArrayType.isArrayType());
        assertFalse(stringListType.isArrayType());

        JavaType stringArrayTypeNarrowed = stringArrayType.narrowBy(String[].class);
        JavaType stringArrayTypeForcedNarrowed = stringArrayType.forcedNarrowBy(String[].class);

        assertSame(stringArrayTypeNarrowed, stringArrayTypeForcedNarrowed);
        assertSame(stringArrayType, stringArrayTypeNarrowed);

        JavaType stringListTypeWidened = stringListType.widenBy(Object[].class);
        JavaType stringListTypeWidenedForced = stringListType.forcedWidenBy(Object[].class);

        assertSame(stringListTypeWidened, stringListTypeWidenedForced);
        assertNotSame(stringListType, stringListTypeWidened);

        // Test further narrowing and widening of JavaType instances
        JavaType stringListTypeNarrowed = stringListType.narrowBy(ArrayList.class);
        JavaType stringListTypeForcedNarrowed = stringListType.forcedNarrowBy(ArrayList.class);
        JavaType objectListTypeWidened = stringListType.widenBy(List.class);
        JavaType objectListTypeWidenedForced = stringListType.forcedWidenBy(List.class);

        assertSame(stringListTypeNarrowed, stringListTypeForcedNarrowed);
        assertSame(stringListType, stringListTypeNarrowed);
        assertSame(objectListTypeWidened, objectListTypeWidenedForced);
        assertNotSame(stringListType, objectListTypeWidened);

        // Test narrowing and widening of JavaType instances with contained types
        JavaType javaClassType = TypeFactory.defaultInstance().constructType(JavaType.class).narrowBy(JavaClassType.class);
        JavaType javaClassTypeForced = javaClassType.forcedNarrowBy(JavaClassType.class);

        JavaType javaInterfaceTypeWidened = javaClassType.widenBy(JavaInterfaceType.class);
        JavaType javaInterfaceTypeWidenedForced = javaClassType.forcedWidenBy(JavaInterfaceType.class);

        assertSame(javaClassType, javaClassTypeForced);
        assertSame(javaClassType, javaClassType.copy());
        assertNotSame(javaInterfaceTypeWidened, javaInterfaceTypeWidenedForced);
        assertNotSame(javaClassType, javaInterfaceTypeWidened);
    }

    @Test
    public void testJavaTypeWithTypeArguments() throws Exception {
        // Test creation of JavaType instances with type arguments
        JavaType listType = TypeFactory.defaultInstance().constructType(new TypeReference<List<String>>() {}.getType());
        JavaType mapType = TypeFactory.defaultInstance().constructType(new TypeReference<Map<String, Integer>>() {}.getType());

        assertEquals("java.util.List", listType.getErasedSignature());
        assertEquals("[Ljava.lang.Object;", listType.getGenericSignature());
        assertEquals("java.util.Map", mapType.getErasedSignature());
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapType.getGenericSignature());

        // Test constructing and copying JavaType instances with type arguments
        JavaType listTypeWithValueHandler = listType.copy();
        listTypeWithValueHandler = listTypeWithValueHandler.withValueHandler(mapper.getFactory().getValueSerializer(List.class));

        assertEquals("java.util.List", listType.getErasedSignature());
        assertEquals("[Ljava.lang.Object;", listType.getGenericSignature());
        assertEquals("java.util.List", listTypeWithValueHandler.getErasedSignature());
        assertEquals("[Ljava.lang.Object;", listTypeWithValueHandler.getGenericSignature());

        assertSame(mapper.getFactory().getValueSerializer(List.class), listTypeWithValueHandler.getValueHandler());

        JavaType mapTypeWithValueHandler = mapType.copy();
        mapTypeWithValueHandler = mapTypeWithValueHandler.withValueHandler(mapper.getFactory().getValueSerializer(Map.class));

        assertEquals("java.util.Map", mapType.getErasedSignature());
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapType.getGenericSignature());
        assertEquals("java.util.Map", mapTypeWithValueHandler.getErasedSignature());
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapTypeWithValueHandler.getGenericSignature());

        assertSame(mapper.getFactory().getValueSerializer(Map.class), mapTypeWithValueHandler.getValueHandler());

        JavaType listTypeWithTypeHandler = listType.copy();
        listTypeWithTypeHandler = listTypeWithTypeHandler.withTypeHandler(mapper.getFactory().createTypeSerializer(List.class));

        assertEquals("java.util.List", listType.getErasedSignature());
        assertEquals("[Ljava.lang.Object;", listType.getGenericSignature());
        assertEquals("java.util.List", listTypeWithTypeHandler.getErasedSignature());
        assertEquals("[Ljava.lang.Object;", listTypeWithTypeHandler.getGenericSignature());

        assertSame(mapper.getFactory().createTypeSerializer(List.class), listTypeWithTypeHandler.getTypeHandler());

        JavaType mapTypeWithTypeHandler = mapType.copy();
        mapTypeWithTypeHandler = mapTypeWithTypeHandler.withTypeHandler(mapper.getFactory().createTypeSerializer(Map.class));

        assertEquals("java.util.Map", mapType.getErasedSignature());
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapType.getGenericSignature());
        assertEquals("java.util.Map", mapTypeWithTypeHandler.getErasedSignature());
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapTypeWithTypeHandler.getGenericSignature());

        assertSame(mapper.getFactory().createTypeSerializer(Map.class), mapTypeWithTypeHandler.getTypeHandler());

        // Test narrowing and widening of JavaType instances with type arguments
        JavaType listTypeOfStringInt = listType.narrowBy(List.class).narrowBy(String.class).narrowBy(Integer.class);
        JavaType listTypeOfStringIntForced = listTypeOfStringInt.forcedNarrowBy(List.class).forcedNarrowBy(String.class).forcedNarrowBy(Integer.class);

        JavaType mapTypeOfStringInt = mapType.narrowBy(Map.class).narrowBy(String.class).narrowBy(Integer.class);
        JavaType mapTypeOfStringIntForced = mapTypeOfStringInt.forcedNarrowBy(Map.class).forcedNarrowBy(String.class).forcedNarrowBy(Integer.class);

        JavaType listTypeOfString = listType.narrowBy(List.class).narrowBy(String.class);
        JavaType listTypeOfStringForced = listTypeOfString.forcedNarrowBy(List.class).forcedNarrowBy(String.class);

        JavaType mapTypeOfString = mapType.narrowBy(Map.class).narrowBy(String.class);
        JavaType mapTypeOfStringForced = mapTypeOfString.forcedNarrowBy(Map.class).forcedNarrowBy(String.class);

        JavaType listTypeOfObject = listType.widenBy(List.class).widenBy(Object.class);
        JavaType listTypeOfObjectForced = listTypeOfObject.forcedWidenBy(List.class).forcedWidenBy(Object.class);

        JavaType mapTypeOfObject = mapType.widenBy(Map.class).widenBy(Object.class);
        JavaType mapTypeOfObjectForced = mapTypeOfObject.forcedWidenBy(Map.class).forcedWidenBy(Object.class);

        assertSame(listTypeOfStringIntForced, listTypeOfStringInt);
        assertSame(listTypeOfString, listTypeOfStringForced);
        assertSame(mapTypeOfStringIntForced, mapTypeOfStringInt);
        assertSame(mapTypeOfString, mapTypeOfStringForced);
        assertNotSame(listTypeOfString, listTypeOfObject);
        assertNotSame(mapTypeOfString, mapTypeOfObject);

        // Test further narrowing and widening of JavaType instances with type arguments
        JavaType mapTypeOfStringStringInt = mapTypeOfString.narrowBy(Map.class).narrowBy(String.class).narrowBy(Map.class).narrowBy(String.class).narrowBy(Integer.class);
        JavaType mapTypeOfStringStringIntForced = mapTypeOfStringStringInt.forcedNarrowBy(Map.class).forcedNarrowBy(String.class).forcedNarrowBy(Map.class).forcedNarrowBy(String.class).forcedNarrowBy(Integer.class);

        JavaType listTypeOfStringListStringInt = listTypeOfString.narrowBy(List.class).narrowBy(String.class).narrowBy(List.class).narrowBy(String.class).narrowBy(Integer.class);
        JavaType listTypeOfStringListStringIntForced = listTypeOfStringListStringInt.forcedNarrowBy(List.class).forcedNarrowBy(String.class).forcedNarrowBy(List.class).forcedNarrowBy(String.class).forcedNarrowBy(Integer.class);

        JavaType mapTypeOfStringStringMapStringInt = mapTypeOfString.narrowBy(Map.class).narrowBy(String.class).narrowBy(Map.class).narrowBy(String.class).narrowBy(Map.class).narrowBy(String.class).narrowBy(Integer.class);
        JavaType mapTypeOfStringStringMapStringIntForced = mapTypeOfStringStringMapStringInt.forcedNarrowBy(Map.class).forcedNarrowBy(String.class).forcedNarrowBy(Map.class).forcedNarrowBy(String.class).forcedNarrowBy(Map.class).forcedNarrowBy(String.class).forcedNarrowBy(Integer.class);

        JavaType listTypeOfStringListString = listTypeOfString.narrowBy(List.class).narrowBy(String.class).narrowBy(List.class).narrowBy(String.class);
        JavaType listTypeOfStringListStringForced = listTypeOfStringListString.forcedNarrowBy(List.class).forcedNarrowBy(String.class).forcedNarrowBy(List.class).forcedNarrowBy(String.class);

        JavaType mapTypeOfStringStringList = mapTypeOfString.narrowBy(Map.class).narrowBy(String.class).narrowBy(Map.class).narrowBy(String.class).narrowBy(List.class);
        JavaType mapTypeOfStringStringListForced = mapTypeOfStringStringList.forcedNarrowBy(Map.class).forcedNarrowBy(String.class).forcedNarrowBy(Map.class).forcedNarrowBy(String.class).forcedNarrowBy(List.class);

        assertSame(mapTypeOfStringStringIntForced, mapTypeOfStringStringInt);
        assertSame(mapTypeOfStringString, mapTypeOfStringStringForced);
        assertSame(listTypeOfStringListStringIntForced, listTypeOfStringListStringInt);
        assertSame(listTypeOfStringListString, listTypeOfStringListStringForced);
        assertNotSame(mapTypeOfStringString, mapTypeOfStringStringList);
        assertNotSame(listTypeOfStringListString, listTypeOfStringListStringForced);
    }
}