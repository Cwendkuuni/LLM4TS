
package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty newProperty = property.withName(new PropertyName("newName"));
        assertEquals("newName", newProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        StdDeserializer<?> deserializer = new StdDeserializer<>(String.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);
        assertEquals(deserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.getMember());
    }
    
    // Add more test cases here for other methods

}

