package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import java.io.IOException;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithNullName() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.withName(null));
    }

    @Test
    public void testWithNullDeserializer() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.withValueDeserializer(null));
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        try {
            property.deserializeAndSet(null, null, null);
        } catch (IOException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        try {
            assertNull(property.deserializeSetAndReturn(null, null, null));
        } catch (IOException e) {
            fail("Should not throw exception");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithNoIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        property.set(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithNoIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        property.setAndReturn(null, null);
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
}