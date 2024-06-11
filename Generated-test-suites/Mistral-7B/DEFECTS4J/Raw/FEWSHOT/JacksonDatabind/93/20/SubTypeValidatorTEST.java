package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

    private static final SubTypeValidator INSTANCE = SubTypeValidator.instance();

    @Test
    public void testValidateSubType_allowedClass() throws JsonMappingException {
        JavaType allowedType = TypeFactory.defaultInstance().constructType(String.class);
        INSTANCE.validateSubType(null, allowedType);
    }

    @Test
    public void testValidateSubType_disallowedClassWithSimpleName() throws JsonMappingException {
        JavaType disallowedType = TypeFactory.defaultInstance().constructType("org.apache.commons.collections.functors.InvokerTransformer");
        assertThrows(JsonMappingException.class, () -> INSTANCE.validateSubType(null, disallowedType));
    }

    @Test
    public void testValidateSubType_disallowedClassWithFullName() throws JsonMappingException {
        JavaType disallowedType = TypeFactory.defaultInstance().constructType("org.apache.commons.collections.functors.InvokerTransformer");
        assertThrows(JsonMappingException.class, () -> INSTANCE.validateSubType(null, disallowedType));
    }

    @Test
    public void testValidateSubType_disallowedInterface() throws JsonMappingException {
        JavaType disallowedInterfaceType = TypeFactory.defaultInstance().constructType(Iterable.class);
        assertThrows(JsonMappingException.class, () -> INSTANCE.validateSubType(null, disallowedInterfaceType));
    }

    @Test
    public void testValidateSubType_disallowedClassWithSuperClass() throws JsonMappingException {
        JavaType disallowedSuperClassType = TypeFactory.defaultInstance().constructType(AbstractPointcutAdvisor.class);
        JavaType subType = TypeFactory.defaultInstance().constructType(MySubType.class);
        assertThrows(JsonMappingException.class, () -> INSTANCE.validateSubType(null, subType.getGenericType()));
        assertThrows(JsonMappingException.class, () -> INSTANCE.validateSubType(null, subType.getRawType()));
        INSTANCE.validateSubType(null, disallowedSuperClassType);
        INSTANCE.validateSubType(null, subType.getSuperclass());
    }

    @Test
    public void testValidateSubType_disallowedClassWithPackageName() throws JsonMappingException {
        JavaType disallowedPackageType = TypeFactory.defaultInstance().constructType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
        assertThrows(JsonMappingException.class, () -> INSTANCE.validateSubType(null, disallowedPackageType));
    }

    @Test
    public void testValidateSubType_unmodifiableSet() throws JsonMappingException {
        Set<String> unmodifiableSet = Collections.unmodifiableSet(SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES);
        SubTypeValidator validator = new SubTypeValidator();
        validator._cfgIllegalClassNames = unmodifiableSet;
        JavaType disallowedType = TypeFactory.defaultInstance().constructType("java.util.logging.FileHandler");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(null, disallowedType));
    }

    private static class MySubType extends AbstractPointcutAdvisor {}

}