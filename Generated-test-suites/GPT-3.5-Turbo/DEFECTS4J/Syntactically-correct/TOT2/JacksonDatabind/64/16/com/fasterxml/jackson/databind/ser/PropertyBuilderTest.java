package com.fasterxml.jackson.databind.ser;

import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testPropertyBuilderConstructor() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        assertNotNull(propertyBuilder);
        assertEquals(config, propertyBuilder._config);
        assertEquals(beanDesc, propertyBuilder._beanDesc);
    }

    @Test
    public void testGetClassAnnotations() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Annotations annotations = propertyBuilder.getClassAnnotations();

        assertNotNull(annotations);
    }

    // Add more test cases for other methods in the PropertyBuilder class

}