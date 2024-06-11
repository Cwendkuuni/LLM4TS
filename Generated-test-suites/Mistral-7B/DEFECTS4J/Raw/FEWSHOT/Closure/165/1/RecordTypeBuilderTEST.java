package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeBuilderTest {

    @Test
    public void testRecordTypeBuilder() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        // Test adding properties
        builder.addProperty("foo", registry.getStringType(), null);
        builder.addProperty("bar", registry.getNumberType(), null);

        // Test building record type
        JSType recordType = builder.build();
        assertNotNull(recordType);
        assertTrue(recordType instanceof RecordType);

        RecordType record = (RecordType) recordType;
        Map<String, RecordProperty> properties = record.getProperties();
        assertEquals(2, properties.size());
        RecordProperty fooProperty = properties.get("foo");
        RecordProperty barProperty = properties.get("bar");

        assertEquals(registry.getStringType(), fooProperty.getType());
        assertEquals(registry.getNumberType(), barProperty.getType());
    }

    @Test
    public void testEmptyRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        // Test building empty record type
        JSType recordType = builder.build();
        assertNotNull(recordType);
        assertTrue(recordType instanceof RecordType);

        RecordType record = (RecordType) recordType;
        Map<String, RecordProperty> properties = record.getProperties();
        assertTrue(properties.isEmpty());
        assertEquals(JSTypeNative.OBJECT_TYPE, record.getType());
    }
}