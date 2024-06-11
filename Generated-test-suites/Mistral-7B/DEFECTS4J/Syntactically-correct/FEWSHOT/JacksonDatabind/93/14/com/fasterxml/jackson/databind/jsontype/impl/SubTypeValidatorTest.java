package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SubTypeValidatorTest {

    private static SubTypeValidator instance() {
        return SubTypeValidator.instance();
    }

    @Test
    public void testValidSubType() throws JsonMappingException {
        JavaType validType = JavaType.construct(String.class, ObjectId.class, ClassIdType.construct(String.class, "valid"));
        instance().validateSubType(null, validType);
    }

    @Test
    public void testInvalidSubTypeWithSimpleName() throws JsonMappingException {
        JavaType invalidType = JavaType.construct(String.class, ObjectId.class, ClassIdType.construct(String.class, "org.apache.commons.collections.functors.InvokerTransformer"));
        assertThrows(JsonMappingException.class, () -> instance().validateSubType(null, invalidType));
    }

    @Test
    public void testInvalidSubTypeWithFullyQualifiedName() throws JsonMappingException {
        JavaType invalidType = JavaType.construct(String.class, ObjectId.class, ClassIdType.construct(String.class, "org.apache.commons.collections.functors.InvokerTransformer"));
        assertThrows(JsonMappingException.class, () -> instance().validateSubType(null, invalidType));
    }

    @Test
    public void testInvalidSubTypeWithSpringPrefix() throws JsonMappingException {
        JavaType invalidType = JavaType.construct(String.class, ObjectId.class, ClassIdType.construct(String.class, "org.springframework.beans.factory.config.PropertyPathFactoryBean"));
        assertThrows(JsonMappingException.class, () -> instance().validateSubType(null, invalidType));
    }

    @Test
    public void testInvalidSubTypeWithSpringPrefixAndSuperclassCheck() throws JsonMappingException {
        JavaType invalidType = JavaType.construct(String.class, ObjectId.class, ClassIdType.construct(String.class, "org.springframework.beans.factory.config.PropertyPathFactoryBean"));
        JavaType superclass = JavaType.construct(String.class, ObjectId.class, ClassIdType.construct(String.class, "org.springframework.beans.factory.ObjectFactory"));
        JavaType parentType = JavaType.construct(String.class, ObjectId.class, ClassIdType.construct(String.class, "org.springframework.beans.factory.ObjectFactory"), superclass);
        JavaType grandParentType = JavaType.construct(String.class, ObjectId.class, ClassIdType.construct(String.class, "org.springframework.beans.factory.ObjectFactory"), parentType);
        instance().validateSubType(null, grandParentType); // this should not throw an exception

        assertThrows(JsonMappingException.class, () -> instance().validateSubType(null, invalidType));
    }

    @Test
    public void testValidateSubTypeWithNullArgument() {
        assertThrows(NullPointerException.class, () -> instance().validateSubType(null, null));
    }

    @Test
    public void testValidateSubTypeWithNullJavaType() {
        assertThrows(NullPointerException.class, () -> instance().validateSubType(null, null));
    }

    @Test
    public void testValidateSubTypeWithNullDeserializationContext() {
        assertThrows(NullPointerException.class, () -> instance().validateSubType(null, JavaType.construct(String.class)));
    }

    @Test
    public void testValidateSubTypeWithEmptyJavaTypeId() {
        JavaType invalidType = JavaType.construct(String.class, ObjectId.class, ClassIdType.construct(String.class, ""));
        assertThrows(JsonMappingException.class, () -> instance().validateSubType(null, invalidType));
    }

    @Test
    public void testValidateSubTypeWithInvalidJavaTypeId() {
        JavaType invalidType = JavaType.construct(String.class, ObjectId.class, ClassIdType.construct(String.class, "invalidTypeId"));
        assertThrows(JsonMappingException.class, () -> instance().validateSubType(null, invalidType));
    }

    @Test
    public void testValidateSubTypeWithInvalidJavaTypeClass() {
        JavaType invalidType = JavaType.construct(ObjectNode.class, ObjectId.class, ClassIdType.construct(String.class, "java.lang.String"));
        assertThrows(JsonMappingException.class, () -> instance().validateSubType(null, invalidType));
    }

    @Test
    public void testValidateSubTypeWithCircularDependency() throws JsonMappingException {
        JavaType circularType1 = JavaType.construct(String.class, ObjectId.class, ClassIdType.construct(String.class, "CircularType1"));
        JavaType circularType2 = JavaType.construct(String.class, ObjectId.class, ClassIdType.construct(String.class, "CircularType2"), circularType1);
        assertThrows(JsonMappingException.class, () -> instance().validateSubType(null, circularType2));
    }

    @Test
    public void testValidateSubTypeWithBaseType() throws JsonMappingException {
        JavaType baseType = JavaType.construct(String.class, ObjectId.class, ClassIdType.construct(String.class, "String"));
        instance().validateSubType(null, baseType);
    }

    @Test
    public void testValidateSubTypeWithObjectType() throws JsonMappingException {
        JavaType objectType = JavaType.construct(Object.class, ObjectId.class);
        instance().validateSubType(null, objectType);
    }

    @Test
    public void testValidateSubTypeWithNullClassId() {
        JavaType javaType = JavaType.construct(String.class, null, null);
        assertThrows(NullPointerException.class, () -> instance().validateSubType(null, javaType));
    }
}