package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.object.POJOClass;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    private ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
    }

    @Test
    public void testIdFromValue() throws Exception {
        JavaType baseType = mapper.getTypeFactory().constructType(ClassNameIdResolver.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, mapper.getTypeFactory());

        Object value = new POJOClass("TestClass");
        String id = resolver.idFromValue(value);
        assertEquals("com.fasterxml.jackson.databind.test.TestClass", id);

        Class<?> clazz = Class.forName("java.lang.String");
        id = resolver.idFromValue(clazz);
        assertEquals("java.lang.String", id);

        // Test for enum
        Enum e = Enum.valueOf(Enum.class, "RED");
        id = resolver.idFromValue(e);
        assertEquals("java.lang.Enum$EnumConstantByName", id);
    }

    @Test
    public void testIdFromValueAndType() throws Exception {
        JavaType baseType = mapper.getTypeFactory().constructType(ClassNameIdResolver.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, mapper.getTypeFactory());

        Object value = new POJOClass("TestClass");
        Class<?> type = POJOClass.class;
        String id = resolver.idFromValueAndType(value, type);
        assertEquals("com.fasterxml.jackson.databind.test.TestClass", id);

        // Test for inner class
        Class<?> outerClass = TestInnerClass.class;
        String idFromOuterClass = resolver.idFromValueAndType(new TestInnerClass(), outerClass);
        assertEquals("com.fasterxml.jackson.databind.test.TestInnerClass", idFromOuterClass);

        // Test for generic type
        TypeReference<List<String>> typeReference = new TypeReference<List<String>>() {};
        String idFromGenericType = resolver.idFromValueAndType(Arrays.asList("test"), typeReference);
        assertEquals("java.util.ArrayList", idFromGenericType);
    }

    @Test
    public void testTypeFromId() throws Exception {
        JavaType baseType = mapper.getTypeFactory().constructType(ClassNameIdResolver.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, mapper.getTypeFactory());

        String id = "java.lang.String";
        JavaType type = resolver.typeFromId(mapper.getSerializerProvider().getEmptyContext(), id);
        assertEquals(String.class, type.getRawClass());

        id = "java.util.Collections$UnmodifiableListMappers$ArrayListWrapper";
        type = resolver.typeFromId(mapper.getSerializerProvider().getEmptyContext(), id);
        assertEquals(ArrayList.class, type.getRawClass());

        id = "java.lang.Enum$EnumConstantByName";
        try {
            type = resolver.typeFromId(mapper.getSerializerProvider().getEmptyContext(), id);
            fail("Should throw exception for unknown type id");
        } catch (IOException e) {
            // Expected
        }
    }
}