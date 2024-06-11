package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * Unit tests for {@link StdKeyDeserializer} class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @After
    public void tearDown() throws Exception {
        objectMapper = null;
    }

    /**
     * Test method for {@link StdKeyDeserializer#forType(Class)}.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testForType() throws Exception {
        // Test for primitive types
        Class<?> primitiveTypes[] = {String.class, Object.class, Boolean.class, Byte.class,
                Short.class, Character.class, Integer.class, Long.class, Float.class,
                Double.class, Locale.class, Date.class, Calendar.class, UUID.class, URI.class,
                URL.class};

        for (Class<?> type : primitiveTypes) {
            StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(type);
            assertNotNull(keyDeserializer);
            assertEquals(type, keyDeserializer.getKeyClass());
            assertTrue(keyDeserializer instanceof StdKeyDeserializer);
        }

        // Test for other classes
        Class<?> complexTypes[] = {String[].class, Map.class, Enum.class};

        for (Class<?> type : complexTypes) {
            StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(type);
            assertNotNull(keyDeserializer);
            assertEquals(type, keyDeserializer.getKeyClass());
            assertTrue(keyDeserializer instanceof StdKeyDeserializer);
        }

        // Test for null input
        assertNull(StdKeyDeserializer.forType(null));
    }

    /**
     * Test method for {@link StdKeyDeserializer#deserializeKey(String, DeserializationContext)}
     * with valid input.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testDeserializeKeyWithValidInput() throws Exception {
        // Test for primitive types
        String validKeysForPrimitiveTypes[] = {
                "true", "false", "10", "255", "abc", "123", "12345678901234567890",
                "1.23", "1.23e2", "en_US", "2022-01-01", "2022-01-01T01:02:03Z", "java.lang.Byte",
                "java.lang.Short", "java.lang.Character", "java.lang.Integer", "java.lang.Long",
                "3.14", "3.14e-1", "fr_FR", "2022-01-01", "2022-01-01", "java.util.UUID",
                "http://example.com/", "http://example.com/"};

        for (String key : validKeysForPrimitiveTypes) {
            StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
            Object result = keyDeserializer.deserializeKey(key, objectMapper.getDeserializationContext());
            assertEquals(key, result);
        }

        // Test for other classes
        String validKeysForComplexTypes[] = {"[1, 2, 3]", "{key1=value1, key2=value2}",
                "EnumType.VALUE1", "Map{key1=value1, key2=value2}", "EnumType.VALUE2"};

        for (String key : validKeysForComplexTypes) {
            ObjectMapper objectMapperForComplexTypes = new ObjectMapper();
            ObjectMapper.DefaultTypingResolver defaultTypingResolver = objectMapperForComplexTypes.getTypeFactory();
            StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Map.class);
            Object result = keyDeserializer.deserializeKey(key, objectMapperForComplexTypes.getDeserializationContext()
                    .withRootValueSeparator("{")
                    .withTypeResolver(defaultTypingResolver));
            Object expectedResult;

            if (key.startsWith("[") || key.startsWith("]")) {
                String[] expectedKeys = key.substring(1, key.length() - 1).split(", ");
                expectedResult = Arrays.asList(expectedKeys).toArray();
            } else {
                ObjectMapper objectMapperForEnum = new ObjectMapper();
                ObjectMapper.DefaultTypingResolver defaultTypingResolverForEnum = objectMapperForEnum.getTypeFactory();
                AnnotatedClass annotatedClass = objectMapperForEnum.findClass("EnumPackage");
                EnumResolver enumResolver = objectMapperForEnum.getAnnotationIntrospector().findEnumValueResolver(annotatedClass);
                expectedResult = enumResolver.findValue(key);
            }

            assertEquals(expectedResult.getClass(), result.getClass());
            assertEquals(expectedResult, result);
        }
    }

    /**
     * Test method for {@link StdKeyDeserializer#deserializeKey(String, DeserializationContext)}
     * with invalid input.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testDeserializeKeyWithInvalidInput() throws Exception {
        // Test for primitive types
        String invalidKeysForPrimitiveTypes[] = {"notABoolean", "123abc", "abc123", "256",
                "invalidLocale", "invalidDate", "invalidCalendar", "invalidUUID",
                "invalidURI", "invalidURL", "java.lang.String", "java.lang.Object"};

        for (String key : invalidKeysForPrimitiveTypes) {
            StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
            String result = (String) keyDeserializer.deserializeKey(key, objectMapper.getDeserializationContext());
            fail("Expected an exception, but got: " + result);
        }

        // Test for other classes
        String invalidKeysForComplexTypes[] = {"[invalidKey1, invalidKey2]", "{key1=invalidValue1, key2=invalidValue2}",
                "EnumType.INVALID_VALUE", "Map{key1=invalidValue1, key2=invalidValue2}", "EnumType.INVALID_VALUE"};

        for (String key : invalidKeysForComplexTypes) {
            ObjectMapper objectMapperForComplexTypes = new ObjectMapper();
            ObjectMapper.DefaultTypingResolver defaultTypingResolver = objectMapperForComplexTypes.getTypeFactory();
            StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Map.class);

            try {
                keyDeserializer.deserializeKey(key, objectMapperForComplexTypes.getDeserializationContext()
                        .withRootValueSeparator("{")
                        .withTypeResolver(defaultTypingResolver));
                fail("Expected an exception, but none was thrown.");
            } catch (JsonMappingException e) {
                // Expected exception
            }
        }
    }

    /**
     * Test method for {@link StdKeyDeserializer#deserializeKey(String, DeserializationContext)}
     * with null input.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testDeserializeKeyWithNullInput() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        Object result = keyDeserializer.deserializeKey(null, objectMapper.getDeserializationContext());
        assertNull(result);
    }
}