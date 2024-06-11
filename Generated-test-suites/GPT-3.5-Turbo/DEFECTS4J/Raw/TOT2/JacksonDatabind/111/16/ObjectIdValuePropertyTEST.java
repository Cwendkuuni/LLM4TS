package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithName() {
        ObjectIdValueProperty objIdValueProperty = new ObjectIdValueProperty(null, null); // Set ObjectIdReader and PropertyMetaData accordingly
        SettableBeanProperty prop = objIdValueProperty.withName(PropertyName.construct("newName"));
        
        assertEquals("newName", prop.getName());
    }

    // Other test cases for the remaining methods can be added here
}