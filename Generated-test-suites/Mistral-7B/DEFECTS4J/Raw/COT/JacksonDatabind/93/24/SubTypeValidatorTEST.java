package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * SubTypeValidatorTest class.
 *
 * @since 2.8.11
 */
public class SubTypeValidatorTest
{
    private static ObjectMapper objectMapper;

    @BeforeClass
    public static void setup()
    {
        objectMapper = new ObjectMapper();
    }

    // Test case for public constructor
    @Test
    public void testSubTypeValidator()
    {
        SubTypeValidator validator = SubTypeValidator.instance();
        assertTrue(validator instanceof SubTypeValidator);
    }

    // Test case for getInstance method
    @Test
    public void testGetInstance()
    {
        SubTypeValidator validator1 = SubTypeValidator.instance();
        SubTypeValidator validator2 = SubTypeValidator.instance();
        assertTrue(validator1 == validator2);
    }

    // Test case for validateSubType method with a valid class
    @Test
    public void testValidateSubTypeValidClass() throws JsonMappingException
    {
        JavaType validType = objectMapper.getTypeFactory().constructType(String.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, validType);
    }

    // Test case for validateSubType method with an illegal class
    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeIllegalClass() throws JsonMappingException
    {
        JavaType illegalType = objectMapper.getTypeFactory().constructType(SubTypeValidator.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, illegalType);
    }

    // Test case for validateSubType method with a class starting with a specific prefix
    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithPrefix() throws JsonMappingException
    {
        JavaType prefixType = objectMapper.getTypeFactory().constructType(SubTypeValidatorPrefixChecker.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, prefixType);
    }

    // Test case for validateSubType method with a nested class
    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithNestedClass() throws JsonMappingException
    {
        JavaType nestedType = objectMapper.getTypeFactory().constructType(SubTypeValidatorNestedChecker.class.getDeclaredClass("InnerClass"));
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, nestedType);
    }

    // Test case for validateSubType method with an interface
    @Test
    public void testValidateSubTypeWithInterface() throws JsonMappingException
    {
        JavaType interfaceType = objectMapper.getTypeFactory().constructType(JavaType.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, interfaceType);
    }

    // Test case for validateSubType method with a generic class
    @Test
    public void testValidateSubTypeWithGenericClass() throws JsonMappingException
    {
        JavaType genericType = objectMapper.getTypeFactory().constructType(new TypeReference<List<String>>() {});
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, genericType);
    }

    // Test case for validateSubType method with an array
    @Test
    public void testValidateSubTypeWithArray() throws JsonMappingException
    {
        JavaType arrayType = objectMapper.getTypeFactory().constructType(String[].class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, arrayType);
    }

    // Test case for validateSubType method with a primitive type
    @Test
    public void testValidateSubTypeWithPrimitiveType() throws JsonMappingException
    {
        JavaType primitiveType = objectMapper.getTypeFactory().constructType(int.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, primitiveType);
    }

    // Test case for validateSubType method with a long primitive type
    @Test
    public void testValidateSubTypeWithLongPrimitiveType() throws JsonMappingException
    {
        JavaType longPrimitiveType = objectMapper.getTypeFactory().constructType(long.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, longPrimitiveType);
    }

    // Test case for validateSubType method with a double primitive type
    @Test
    public void testValidateSubTypeWithDoublePrimitiveType() throws JsonMappingException
    {
        JavaType doublePrimitiveType = objectMapper.getTypeFactory().constructType(double.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, doublePrimitiveType);
    }

    // Test case for validateSubType method with a boolean primitive type
    @Test
    public void testValidateSubTypeWithBooleanPrimitiveType() throws JsonMappingException
    {
        JavaType booleanPrimitiveType = objectMapper.getTypeFactory().constructType(boolean.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, booleanPrimitiveType);
    }

    // Test case for validateSubType method with a char primitive type
    @Test
    public void testValidateSubTypeWithCharPrimitiveType() throws JsonMappingException
    {
        JavaType charPrimitiveType = objectMapper.getTypeFactory().constructType(char.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, charPrimitiveType);
    }

    // Test case for validateSubType method with a byte primitive type
    @Test
    public void testValidateSubTypeWithBytePrimitiveType() throws JsonMappingException
    {
        JavaType bytePrimitiveType = objectMapper.getTypeFactory().constructType(byte.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, bytePrimitiveType);
    }

    // Test case for validateSubType method with a short primitive type
    @Test
    public void testValidateSubTypeWithShortPrimitiveType() throws JsonMappingException
    {
        JavaType shortPrimitiveType = objectMapper.getTypeFactory().constructType(short.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, shortPrimitiveType);
    }

    // Test case for validateSubType method with a byte array
    @Test
    public void testValidateSubTypeWithByteArray() throws JsonMappingException
    {
        JavaType byteArrayType = objectMapper.getTypeFactory().constructType(byte[].class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, byteArrayType);
    }

    // Test case for validateSubType method with a short array
    @Test
    public void testValidateSubTypeWithShortArray() throws JsonMappingException
    {
        JavaType shortArrayType = objectMapper.getTypeFactory().constructType(short[].class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, shortArrayType);
    }

    // Test case for validateSubType method with a long array
    @Test
    public void testValidateSubTypeWithLongArray() throws JsonMappingException
    {
        JavaType longArrayType = objectMapper.getTypeFactory().constructType(long[].class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, longArrayType);
    }

    // Test case for validateSubType method with a double array
    @Test
    public void testValidateSubTypeWithDoubleArray() throws JsonMappingException
    {
        JavaType doubleArrayType = objectMapper.getTypeFactory().constructType(double[].class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, doubleArrayType);
    }

    // Test case for validateSubType method with a char array
    @Test
    public void testValidateSubTypeWithCharArray() throws JsonMappingException
    {
        JavaType charArrayType = objectMapper.getTypeFactory().constructType(char[].class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, charArrayType);
    }

    // Test case for validateSubType method with a boolean array
    @Test
    public void testValidateSubTypeWithBooleanArray() throws JsonMappingException
    {
        JavaType booleanArrayType = objectMapper.getTypeFactory().constructType(boolean[].class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, booleanArrayType);
    }

    // Test case for validateSubType method with null type
    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithNullType() throws JsonMappingException
    {
        JavaType nullType = null;
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, nullType);
    }

    // Test case for validateSubType method with an empty set
    @Test
    public void testValidateSubTypeWithEmptySet() throws JsonMappingException
    {
        Set<String> emptySet = Collections.emptySet();
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.setIllegalClassNames(emptySet);
        JavaType emptyClassType = objectMapper.getTypeFactory().constructType(String.class);
        validator.validateSubType(null, emptyClassType);
    }
}

class SubTypeValidatorPrefixChecker
{
    public static class InnerClass
    {
    }
}

class SubTypeValidatorNestedChecker
{
    public static class InnerClass
    {
        public void test()
        {
        }
    }
}