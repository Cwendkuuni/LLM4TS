package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PropertyBuilderTest {
    @Test
    public void testGetDefaultBean() {
        // Test when defaultBean is null
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.getDefaultBean());

        // Test when defaultBean is NO_DEFAULT_MARKER
        propertyBuilder = new PropertyBuilder(null, null);
        propertyBuilder._defaultBean = PropertyBuilder.NO_DEFAULT_MARKER;
        assertNull(propertyBuilder.getDefaultBean());

        // Test when defaultBean is an instance of Object
        propertyBuilder = new PropertyBuilder(null, null);
        Object defaultBean = new Object();
        propertyBuilder._defaultBean = defaultBean;
        assertEquals(defaultBean, propertyBuilder.getDefaultBean());
    }

    @Test
    public void testGetPropertyDefaultValue() {
        // Test when defaultBean is null
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.getPropertyDefaultValue(null, null, null));

        // Test when defaultBean is NO_DEFAULT_MARKER
        propertyBuilder = new PropertyBuilder(null, null);
        propertyBuilder._defaultBean = PropertyBuilder.NO_DEFAULT_MARKER;
        assertNull(propertyBuilder.getPropertyDefaultValue(null, null, null));

        // Test when defaultBean is an instance of Object
        propertyBuilder = new PropertyBuilder(null, null);
        Object defaultBean = new Object();
        propertyBuilder._defaultBean = defaultBean;
        assertNull(propertyBuilder.getPropertyDefaultValue(null, null, null));
    }

    @Test
    public void testGetDefaultValue() {
        // Test when type is primitive
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertEquals(0, propertyBuilder.getDefaultValue(JavaType.construct(int.class)));

        // Test when type is primitive wrapper
        propertyBuilder = new PropertyBuilder(null, null);
        assertEquals(0, propertyBuilder.getDefaultValue(JavaType.construct(Integer.class)));

        // Test when type is container type or reference type
        propertyBuilder = new PropertyBuilder(null, null);
        assertEquals(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(JavaType.construct(String.class)));

        // Test when type is String
        propertyBuilder = new PropertyBuilder(null, null);
        assertEquals("", propertyBuilder.getDefaultValue(JavaType.construct(String.class)));

        // Test when type is other than above cases
        propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.getDefaultValue(JavaType.construct(Object.class)));
    }
}