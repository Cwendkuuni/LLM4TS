package com.fasterxml.jackson.databind.ser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

public class PropertyBuilderTest {
    
    private PropertyBuilder propertyBuilder;
    private SerializationConfig config;
    private BeanDescription beanDesc;
    
    @Before
    public void setUp() {
        config = new SerializationConfig();
        beanDesc = new BeanDescription();
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }
    
    @Test
    public void testGetDefaultBean() {
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
    }
    
    @Test
    public void testGetPropertyDefaultValue() {
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("name", new AnnotatedMember(), new JavaType());
        assertNull(propertyDefaultValue);
    }
    
    @Test
    public void testGetDefaultValue() {
        Object defaultValue = propertyBuilder.getDefaultValue(new JavaType());
        assertNull(defaultValue);
    }
}