
package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;
    
    @Before
    public void setUp() {
        propertyBuilder = new PropertyBuilder(null, null);
    }
    
    @Test
    public void testGetPropertyDefaultValue() {
        assertNull(propertyBuilder.getPropertyDefaultValue(null, null, null));
    }
    
    @Test
    public void testGetDefaultValue() {
        assertNull(propertyBuilder.getDefaultValue(null));
    }
}
