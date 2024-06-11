package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    @Test
    public void testConstructor() {
        JavaType baseType = new SimpleType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);

        assertEquals(baseType, classNameIdResolver.getBaseType());
        assertEquals(typeFactory, classNameIdResolver.getTypeFactory());
    }

    @Test
    public void testGetMechanism() {
        JavaType baseType = new SimpleType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);

        assertEquals(JsonTypeInfo.Id.CLASS, classNameIdResolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        JavaType baseType = new SimpleType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);

        // not used with class name - based resolvers
    }

    @Test
    public void testIdFromValue() {
        JavaType baseType = new SimpleType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);

        // Test with an Object instance
        Object object = new Object();
        String expectedId = object.getClass().getName();
        
        assertEquals(expectedId, classNameIdResolver.idFromValue(object));
    }

    @Test
    public void testIdFromValueAndType() {
        JavaType baseType = new SimpleType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);

        // Test with an Object instance and its class type
        Object object = new Object();
        String expectedId = object.getClass().getName();
        
        assertEquals(expectedId, classNameIdResolver.idFromValue(object, object.getClass()));
    }

    @Test
    public void testTypeFromId() {
        JavaType baseType = new SimpleType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);

        DatabindContext context = mock(DatabindContext.class);
        String id = "java.lang.Object";
        JavaType expectedType = new SimpleType(Object.class);

        try {
            assertEquals(expectedType, classNameIdResolver.typeFromId(context, id));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testTypeFromId_UnknownId() {
        JavaType baseType = new SimpleType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);

        DatabindContext context = mock(DatabindContext.class);
        String id = "com.example.UnknownType";

        try {
            assertNull(classNameIdResolver.typeFromId(context, id));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void test_typeFromId_HandleUnknownTypeId() {
        JavaType baseType = new SimpleType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);

        DatabindContext context = mock(DeserializationContext.class);
        String id = "com.example.UnknownType";

        try {
            String errorMessage = "no such class found";
            assertNull(classNameIdResolver.typeFromId(context, id));

            verify(((DeserializationContext) context), times(1)).handleUnknownTypeId(baseType, id, classNameIdResolver, errorMessage);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void test_typeFromId_WithInvalidId() {
        JavaType baseType = new SimpleType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);

        DatabindContext context = mock(DatabindContext.class);
        String id = "invalid.id";

        try {
            classNameIdResolver.typeFromId(context, id);
            fail("IllegalArgumentException should have been thrown");
        } catch (IOException e) {
            String expectedMessage = "Invalid type id 'invalid.id' (for id type 'Id.class')";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void test_typeFromId_WithGeneric() {
        JavaType baseType = new SimpleType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);

        DatabindContext context = mock(DatabindContext.class);
        String id = "java.util.List<java.lang.String>";
        JavaType expectedType = new SimpleType(List.class).withContentType(new SimpleType(String.class));

        try {
            assertEquals(expectedType, classNameIdResolver.typeFromId(context, id));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void test_typeFromId_WithInnerClass_NotStatic() {
        JavaType baseType = new SimpleType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);

        DatabindContext context = mock(DatabindContext.class);
        String id = "com.example.OuterClass$InnerClass";
        JavaType expectedType = new SimpleType(InnerClass.class);

        try {
            assertEquals(expectedType, classNameIdResolver.typeFromId(context, id));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void test_typeFromId_WithInnerClass_Static() {
        JavaType baseType = new SimpleType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);

        DatabindContext context = mock(DatabindContext.class);
        String id = "com.example.OuterClass$InnerStaticClass";
        JavaType expectedType = new SimpleType(InnerStaticClass.class);

        try {
            assertEquals(expectedType, classNameIdResolver.typeFromId(context, id));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void test_idFromValue_WithEnum() {
        JavaType baseType = new SimpleType(Enum.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);

        // Test with an Enum instance
        SampleEnum sampleEnum = SampleEnum.VALUE1;
        String expectedId = sampleEnum.getClass().getSuperclass().getName();

        assertEquals(expectedId, classNameIdResolver.idFromValue(sampleEnum));
    }

    @Test
    public void test_idFromValue_WithEnumSet() {
        JavaType baseType = new SimpleType(EnumSet.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);

        // Test with an EnumSet instance
        EnumSet<SampleEnum> enumSet = EnumSet.allOf(SampleEnum.class);
        String expectedId = EnumSet.class.getName() + "<" + SampleEnum.class.getName() + ">";

        assertEquals(expectedId, classNameIdResolver.idFromValue(enumSet));
    }

    @Test
    public void test_idFromValue_WithEnumMap() {
        JavaType baseType = new SimpleType(EnumMap.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);

        // Test with an EnumMap instance
        EnumMap<SampleEnum, String> enumMap = new EnumMap<>(SampleEnum.class);
        String expectedId = EnumMap.class.getName() + "<" + SampleEnum.class.getName() + "," + String.class.getName() + ">";

        assertEquals(expectedId, classNameIdResolver.idFromValue(enumMap));
    }

    @Test
    public void test_idFromValue_WithArrayList_FromArraysAsList() {
        JavaType baseType = new SimpleType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);

        // Test with an ArrayList instance created from Arrays.asList()
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("a", "b", "c"));
        String expectedId = ArrayList.class.getName();

        assertEquals(expectedId, classNameIdResolver.idFromValue(arrayList));
    }

    @Test
    public void test_idFromValue_WithArrayList_FromCollectionsSingleton() {
        JavaType baseType = new SimpleType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);

        // Test with an ArrayList instance created from Collections.singleton()
        ArrayList<String> arrayList = new ArrayList<>(Collections.singleton("a"));
        String expectedId = ArrayList.class.getName();

        assertEquals(expectedId, classNameIdResolver.idFromValue(arrayList));
    }

    @Test
    public void test_idFromValue_WithArrayList_FromCollectionsSynchronizedList() {
        JavaType baseType = new SimpleType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);

        // Test with an ArrayList instance created from Collections.synchronizedList()
        ArrayList<String> arrayList = new ArrayList<>(Collections.synchronizedList(new ArrayList<>()));
        String expectedId = ArrayList.class.getName();

        assertEquals(expectedId, classNameIdResolver.idFromValue(arrayList));
    }

    @Test
    public void test_getDescForKnownTypeIds() {
        JavaType baseType = new SimpleType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);

        String expectedDesc = "class name used as type id";

        assertEquals(expectedDesc, classNameIdResolver.getDescForKnownTypeIds());
    }

    private enum SampleEnum {
        VALUE1, VALUE2, VALUE3
    }

    private class InnerClass {}

    private static class InnerStaticClass {}

}