package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValuePropertyConstructorWithObjectIdReaderAndPropertyMetadata() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("testId");
            }

            @Override
            public Class<?> getIdType() {
                return String.class;
            }

            @Override
            public ObjectIdGenerator _valueGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver _valueResolver() {
                return null;
            }
        };
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        assertEquals("testId", obj.getName());
        assertNotNull(obj._objectIdReader);
    }

    // ... other test cases here ...

    @Test(expected = NullPointerException.class)
    public void testDeserializeAndSetWithNullJsonParser() {
        JsonParser parser = null;
        ObjectIdValueProperty obj = new ObjectIdValueProperty(new ObjectIdReader() { /* stubs */ }, null);
        try {
            obj.deserializeAndSet(parser, new TestContext(), new Object());
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    // ... other test cases here ...
}