package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

public class PropertyBuilderTest {
    
    private SerializationConfig config;
    private BeanDescription beanDesc;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() {
        config = new SerializationConfig(null, null, null);
        beanDesc = new BeanDescription(null, null);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testGetDefaultBean() {
        assertNull(propertyBuilder.getDefaultBean());
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testGetPropertyDefaultValue() throws Exception {
        assertNull(propertyBuilder.getPropertyDefaultValue("", null, null));
    }

    @Test
    public void testGetDefaultValue() {
        assertEquals("", propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(String.class)));
        assertNull(propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(Object.class)));
        assertEquals(0, propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(int.class)));
        assertEquals(0L, propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(long.class)));
        assertEquals(0.0f, propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(float.class)));
        assertEquals(0.0, propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(double.class)));
        assertEquals(false, propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(boolean.class)));
        assertEquals((byte) 0, propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(byte.class)));
        assertEquals((short) 0, propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(short.class)));
        assertEquals('0', propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(char.class)));

        assertEquals(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(Arrays.class)));
        assertEquals(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(Object[].class)));
        assertEquals(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(String[].class)));
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        assertNull(propertyBuilder.buildWriter(null, null, null, null, null, null, null, false));
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        assertNull(propertyBuilder.findSerializationType(null, false, null));
    }

    @Test
    public void testGetClassAnnotations() {
        assertNull(propertyBuilder.getClassAnnotations());
    }
}