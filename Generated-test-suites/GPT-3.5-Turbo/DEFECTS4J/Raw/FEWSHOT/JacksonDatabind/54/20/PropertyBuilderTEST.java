package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PropertyBuilderTest {
    private PropertyBuilder propertyBuilder;
    private SerializationConfig config;
    private BeanDescription beanDesc;

    @Before
    public void setUp() {
        config = new SerializationConfig(null, null, null);
        beanDesc = new BeanDescription(null, BeanClass.class, null, null, null, null);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testGetDefaultBean() {
        assertNull(propertyBuilder.getDefaultBean());
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        assertNull(propertyBuilder.getPropertyDefaultValue("name", null, null));
    }

    @Test
    public void testGetDefaultValue() {
        assertEquals(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(TypeFactory.type(String.class)));
    }

    private static class BeanClass {
    }
}