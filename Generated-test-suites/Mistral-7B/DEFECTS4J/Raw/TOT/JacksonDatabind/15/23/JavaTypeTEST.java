package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import org.junit.Assert.*;

public class JavaTypeTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testJavaTypeConstruction() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaType = TypeFactory.defaultInstance().constructType(JavaType.class);

        assertEquals(int.class, intType.getRawClass());
        assertEquals(String.class, stringType.getRawClass());
        assertEquals(JavaType.class, javaType.getRawClass());

        // Test withTypeHandler
        JavaType intTypeWithHandler = intType.withValueHandler("intHandler");
        assertNotNull(intTypeWithHandler.getValueHandler());
        assertEquals("intHandler", intTypeWithHandler.getValueHandler().toString());

        // Test withContentTypeHandler
        JavaType listIntType = TypeFactory.defaultInstance().constructType(List.class, int.class);
        JavaType listIntTypeWithHandler = listIntType.withContentTypeHandler("listIntHandler");
        assertNotNull(listIntTypeWithHandler.getContentTypeHandler());
        assertEquals("listIntHandler", listIntTypeWithHandler.getContentTypeHandler().toString());

        // Test withTypeHandler for non-instantiatble classes (interfaces)
        JavaType listInterfaceType = TypeFactory.defaultInstance().constructType(List.class, MyInterface.class);
        JavaType listInterfaceTypeWithHandler = listInterfaceType.withTypeHandler("listInterfaceHandler");
        assertNotNull(listInterfaceTypeWithHandler.getTypeHandler());
        assertEquals("listInterfaceHandler", listInterfaceTypeWithHandler.getTypeHandler().toString());

        // Test withStaticTyping
        JavaType intTypeStatic = intType.withStaticTyping();
        assertTrue(intTypeStatic.useStaticType());

        // Test narrowBy
        JavaType intArrayType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType intArrayTypeNarrowed = intType.narrowBy(intArrayType.getRawClass());
        assertEquals(intArrayType, intArrayTypeNarrowed);

        // Test widenBy
        JavaType intWrapperType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType intWrapperTypeWidened = intType.widenBy(intWrapperType.getRawClass());
        assertEquals(intType, intWrapperTypeWidened);

        // Test narrowContentsBy and widenContentsBy
        JavaType listStringType = TypeFactory.defaultInstance().constructType(List.class, String.class);
        JavaType listIntTypeNarrowed = intType.narrowContentsBy(String.class);
        JavaType listIntTypeWidened = listStringType.widenContentsBy(Integer.class);
        assertEquals(intType, listIntTypeNarrowed);
        assertEquals(listStringType, listIntTypeWidened);
    }

    private static class MyInterface {
    }
}