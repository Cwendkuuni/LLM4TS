package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeBuilderTest {

    private JSTypeRegistry registry;
    private RecordTypeBuilder builder;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        builder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testAddProperty() {
        builder.addProperty("name", registry.getStringType(), null);
        assertNotNull(builder.build());
    }

    @Test(expected = NullPointerException.class)
    public void testAddDuplicateProperty() {
        builder.addProperty("name", registry.getStringType(), null);
        builder.addProperty("name", registry.getStringType(), null);
    }

    @Test
    public void testAddProperties() {
        builder.addProperty("name1", registry.getStringType(), null);
        builder.addProperty("name2", registry.getStringType(), null);
        JSType recordType = builder.build();
        Map<String, RecordProperty> properties = recordType.asRecordType().getProperties();
        assertEquals(2, properties.size());
        assertEquals("name1", properties.keySet().iterator().next());
        assertEquals("name2", properties.keySet().iterator().next());
    }

    @Test
    public void testEmptyRecord() {
        JSType recordType = builder.build();
        assertNotNull(recordType);
    }
}