package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class SubTypeValidatorTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testValidateSubTypeWithValidClasses() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = null;
        JavaType validType = objectMapper.getTypeFactory().constructType(String.class);

        validator.validateSubType(context, validType);
    }

    @Test
    public void testValidateSubTypeWithInvalidClasses() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = null;

        JavaType invalidType1 = objectMapper.getTypeFactory().constructType(SubTypeValidator.class);
        validator.validateSubType(context, invalidType1);

        JavaType invalidType2 = objectMapper.getTypeFactory().constructType(SubTypeValidator.class.getPackage().getName() + ".Util");
        validator.validateSubType(context, invalidType2);

        JavaType invalidType3 = objectMapper.getTypeFactory().constructType("java.lang.Runtime");
        validator.validateSubType(context, invalidType3);

        JavaType invalidType4 = objectMapper.getTypeFactory().constructType("com.fasterxml.jackson.core.JsonFactory");
        validator.validateSubType(context, invalidType4);

        JavaType invalidType5 = objectMapper.getTypeFactory().constructType("org.apache.commons.cli.Options");
        validator.validateSubType(context, invalidType5);

        JavaType invalidType6 = objectMapper.getTypeFactory().constructType("java.sql.Connection");
        validator.validateSubType(context, invalidType6);

        JavaType invalidType7 = objectMapper.getTypeFactory().constructType("java.io.File");
        validator.validateSubType(context, invalidType7);

        JavaType invalidType8 = objectMapper.getTypeFactory().constructType("java.net.URL");
        validator.validateSubType(context, invalidType8);

        JavaType invalidType9 = objectMapper.getTypeFactory().constructType("java.lang.ClassLoader");
        validator.validateSubType(context, invalidType9);

        JavaType invalidType10 = objectMapper.getTypeFactory().constructType("java.io.FileInputStream");
        validator.validateSubType(context, invalidType10);

        JavaType invalidType11 = objectMapper.getTypeFactory().constructType("java.io.FileOutputStream");
        validator.validateSubType(context, invalidType11);

        JavaType invalidType12 = objectMapper.getTypeFactory().constructType("java.io.BufferedReader");
        validator.validateSubType(context, invalidType12);

        JavaType invalidType13 = objectMapper.getTypeFactory().constructType("java.io.BufferedWriter");
        validator.validateSubType(context, invalidType13);

        JavaType invalidType14 = objectMapper.getTypeFactory().constructType("java.io.PrintWriter");
        validator.validateSubType(context, invalidType14);

        JavaType invalidType15 = objectMapper.getTypeFactory().constructType("java.net.Socket");
        validator.validateSubType(context, invalidType15);

        JavaType invalidType16 = objectMapper.getTypeFactory().constructType("java.net.ServerSocket");
        validator.validateSubType(context, invalidType16);

        JavaType invalidType17 = objectMapper.getTypeFactory().constructType("java.net.URLClassLoader");
        validator.validateSubType(context, invalidType17);

        JavaType invalidType18 = objectMapper.getTypeFactory().constructType("java.lang.reflect.Method");
        validator.validateSubType(context, invalidType18);

        JavaType invalidType19 = objectMapper.getTypeFactory().constructType("java.lang.reflect.Field");
        validator.validateSubType(context, invalidType19);

        JavaType invalidType20 = objectMapper.getTypeFactory().constructType("java.lang.reflect.InvocationTargetException");
        validator.validateSubType(context, invalidType20);

        JavaType invalidType21 = objectMapper.getTypeFactory().constructType("java.lang.reflect.Array");
        validator.validateSubType(context, invalidType21);

        JavaType invalidType22 = objectMapper.getTypeFactory().constructType("java.util.Map");
        validator.validateSubType(context, invalidType22);

        JavaType invalidType23 = objectMapper.getTypeFactory().constructType("java.util.HashMap");
        validator.validateSubType(context, invalidType23);

        JavaType invalidType24 = objectMapper.getTypeFactory().constructType("java.util.HashSet");
        validator.validateSubType(context, invalidType24);

        JavaType invalidType25 = objectMapper.getTypeFactory().constructType("java.util.LinkedList");
        validator.validateSubType(context, invalidType25);

        JavaType invalidType26 = objectMapper.getTypeFactory().constructType("java.util.ArrayList");
        validator.validateSubType(context, invalidType26);

        JavaType invalidType27 = objectMapper.getTypeFactory().constructType("java.util.Collections");
        validator.validateSubType(context, invalidType27);

        JavaType invalidType28 = objectMapper.getTypeFactory().constructType("java.util.Properties");
        validator.validateSubType(context, invalidType28);

        JavaType invalidType29 = objectMapper.getTypeFactory().constructType("java.util.EnumSet");
        validator.validateSubType(context, invalidType29);

        JavaType invalidType30 = objectMapper.getTypeFactory().constructType("java.util.EnumMap");
        validator.validateSubType(context, invalidType30);

        JavaType invalidType31 = objectMapper.getTypeFactory().constructType("java.io.Serializable");
        validator.validateSubType(context, invalidType31);

        JavaType invalidType32 = objectMapper.getTypeFactory().constructType("java.lang.Runnable");
        validator.validateSubType(context, invalidType32);

        JavaType invalidType33 = objectMapper.getTypeFactory().constructType("java.lang.Thread");
        validator.validateSubType(context, invalidType33);

        JavaType invalidType34 = objectMapper.getTypeFactory().constructType("java.lang.Throwable");
        validator.validateSubType(context, invalidType34);

        JavaType invalidType35 = objectMapper.getTypeFactory().constructType("java.lang.NullPointerException");
        validator.validateSubType(context, invalidType35);
    }

    @Test
    public void testValidateSubTypeWithCustomIllegalClasses() throws JsonMappingException {
        Set<String> customIllegalClasses = new HashSet<>();
        customIllegalClasses.add("com.mypackage.MyNastyClass");

        SubTypeValidator validator = new SubTypeValidator() {
            @Override
            protected Set<String> _cfgIllegalClassNames = customIllegalClasses;
        };

        JavaType customInvalidType = objectMapper.getTypeFactory().constructType("com.mypackage.MyNastyClass");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, customInvalidType));
    }
}