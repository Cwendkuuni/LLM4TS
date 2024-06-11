package com.example.junit; // replace with your test package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test class for ClassNameIdResolver
 */
public class ClassNameIdResolverTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TypeFactory typeFactory = objectMapper.getTypeFactory();

    /**
     * Test method for {@link ClassNameIdResolver#ClassNameIdResolver(JavaType, TypeFactory)}
     */
    @Test
    public void testClassNameIdResolver() {
        JavaType baseType = typeFactory.constructType(String.class);
        ClassNameIdResolver classUnderTest = new ClassNameIdResolver(baseType, typeFactory);
        assertNotNull(classUnderTest);
    }

    /**
     * Test method for {@link ClassNameIdResolver#getMechanism()}
     */
    @Test
    public void testGetMechanism() {
        ClassNameIdResolver classUnderTest = new ClassNameIdResolver(null, null);
        assertEquals(JsonTypeInfo.Id.CLASS, classUnderTest.getMechanism());
    }

    /**
     * Test method for {@link ClassNameIdResolver#registerSubtype(Class, String)}
     */
    @Test
    public void testRegisterSubtype() {
        // Not applicable for ClassNameIdResolver as it doesn't use this method
    }

    /**
     * Test method for {@link ClassNameIdResolver#idFromValue(Object)}
     */
    @Test
    public void testIdFromValue() {
        ClassNameIdResolver classUnderTest = new ClassNameIdResolver(null, null);
        Object meaninglessObject = new Object();

        String result = classUnderTest.idFromValue(meaninglessObject);
        String expected = meaninglessObject.getClass().getName();
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link ClassNameIdResolver#idFromValueAndType(Object, Class)}
     */
    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver classUnderTest = new ClassNameIdResolver(null, null);
        Object meaninglessObject = new Object();
        Class<?> meaninglessClass = meaninglessObject.getClass();

        String result = classUnderTest.idFromValueAndType(meaninglessObject, meaninglessClass);
        String expected = meaninglessClass.getName();
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link ClassNameIdResolver#typeFromId(DatabindContext, String)}
     */
    @Test
    public void testTypeFromId() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DatabindContext context = objectMapper.getPolymorphicContext();

        String id = "java.lang.String";
        JavaType expectedType = typeFactory.constructType(String.class);

        JavaType resultType = new ClassNameIdResolver(null, typeFactory).typeFromId(context, id);
        assertEquals(expectedType, resultType);
    }

    /**
     * Test method for {@link ClassNameIdResolver#_typeFromId(String, DatabindContext)}
     */
    @Test
    public void test_typeFromId() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DatabindContext context = objectMapper.getPolymorphicContext();

        String id = "java.lang.String";
        JavaType expectedType = typeFactory.constructType(String.class);

        JavaType resultType = new ClassNameIdResolver(null, typeFactory)._typeFromId(id, context);
        assertEquals(expectedType, resultType);
    }

    /**
     * Test method for {@link ClassNameIdResolver#_idFrom(Object, Class, TypeFactory)}
     */
    @Test
    public void test_idFrom() {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();

        Object meaninglessObject = new Object();
        Class<?> meaninglessClass = meaninglessObject.getClass();

        String result = new ClassNameIdResolver(null, typeFactory)
                ._idFrom(meaninglessObject, meaninglessClass, typeFactory);
        String expected = meaninglessClass.getName();
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link ClassNameIdResolver#getDescForKnownTypeIds()}
     */
    @Test
    public void testGetDescForKnownTypeIds() {
        ClassNameIdResolver classUnderTest = new ClassNameIdResolver(null, null);
        String result = classUnderTest.getDescForKnownTypeIds();
        assertEquals("class name used as type id", result);
    }

    /**
     * Test method for handling class not found exceptions
     * when trying to deserialize a String type id
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromId_ClassNotFoundException() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DatabindContext context = objectMapper.getPolymorphicContext();

        String id = "com.unknown.package.UnimplementedClass";
        new ClassNameIdResolver(null, typeFactory).typeFromId(context, id);
    }

    /**
     * Test method for handling generic types
     */
    @Test
    public void testTypeFromId_Generic() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DatabindContext context = objectMapper.getPolymorphicContext();

        String id = "java.util.List<java.lang.String>";
        JavaType expectedType = typeFactory.constructType(List.class, String.class);

        JavaType resultType = new ClassNameIdResolver(null, typeFactory).typeFromId(context, id);
        assertEquals(expectedType, resultType);
    }

    /**
     * Test method for handling EnumSet and EnumMap classes
     */
    @Test
    public void testTypeFromId_EnumSetAndEnumMap() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DatabindContext context = objectMapper.getPolymorphicContext();

        EnumSet<String> enumSet = EnumSet.of("ONE", "TWO");
        String id = typeFactory.constructCollectionType(EnumSet.class, String.class).toCanonical();

        JavaType expectedType = typeFactory.constructType(EnumSet.class, String.class);

        JavaType resultType = new ClassNameIdResolver(null, typeFactory).typeFromId(context, id);
        assertEquals(expectedType, resultType);

        EnumMap<String, Integer> enumMap = new EnumMap<>(String.class);
        enumMap.put("ONE", 1);
        enumMap.put("TWO", 2);
        id = typeFactory.constructMapType(EnumMap.class, String.class, Integer.class).toCanonical();

        expectedType = typeFactory.constructType(EnumMap.class, String.class, Integer.class);

        resultType = new ClassNameIdResolver(null, typeFactory).typeFromId(context, id);
        assertEquals(expectedType, resultType);
    }

    /**
     * Test method for handling inner classes
     */
    @Test
    public void testTypeFromId_InnerClass() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DatabindContext context = objectMapper.getPolymorphicContext();

        String outerClassName = "com.inner.OuterClass";
        String innerClassName = "com.inner.OuterClass$InnerClass";

        JavaType outerType = typeFactory.constructType(outerClassName);
        JavaType innerType = outerType.getComponentType();

        String id = innerType.getName();

        JavaType expectedType = innerType;

        JavaType resultType = new ClassNameIdResolver(outerType, typeFactory).typeFromId(context, id);
        assertEquals(expectedType, resultType);
    }

    /**
     * Test method for handling special classes like ArrayList, Arrays$List, etc.
     */
    @Test
    public void testTypeFromId_SpecialClasses() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DatabindContext context = objectMapper.getPolymorphicContext();

        String id = "java.util.ArrayList";
        JavaType expectedType = typeFactory.constructType(ArrayList.class);

        JavaType resultType = new ClassNameIdResolver(null, typeFactory).typeFromId(context, id);
        assertEquals(expectedType, resultType);

        id = "java.util.Arrays$List";
        expectedType = typeFactory.constructType(ArrayList.class);

        resultType = new ClassNameIdResolver(null, typeFactory).typeFromId(context, id);
        assertEquals(expectedType, resultType);
    }
}