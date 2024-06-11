package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;

public class RecordTypeBuilderTest {
    private JSTypeRegistry registry;
    private RecordTypeBuilder recordTypeBuilder;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        recordTypeBuilder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testAddProperty() {
        recordTypeBuilder.addProperty("name", registry.getStringType(), null);
        RecordProperty nameProperty = recordTypeBuilder.properties.get("name");
        assertNotNull(nameProperty);
        assertTrue(nameProperty instanceof RecordProperty);
        assertEquals(registry.getStringType(), nameProperty.getType());
    }

    @Test
    public void testAddDuplicateProperty() {
        recordTypeBuilder.addProperty("name", registry.getStringType(), null);
        RecordProperty nameProperty1 = recordTypeBuilder.properties.get("name");

        assertNull(recordTypeBuilder.addProperty("name", registry.getStringType(), null));

        RecordProperty nameProperty2 = recordTypeBuilder.properties.get("name");
        assertNull(nameProperty2);
    }

    @Test
    public void testBuildEmptyRecord() {
        JSType recordType = recordTypeBuilder.build();
        assertTrue(recordType instanceof RecordType);
        assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getMetadata().getType());
    }

    @Test
    public void testBuildRecordWithProperties() {
        recordTypeBuilder.addProperty("name", registry.getStringType(), null);
        recordTypeBuilder.addProperty("age", registry.getNumberType(), null);
        JSType recordType = recordTypeBuilder.build();
        assertTrue(recordType instanceof RecordType);
        Map<String, JSType> properties = recordType.getMetadata().getProperties();
        assertTrue(properties.size() == 2);
        assertEquals(registry.getStringType(), properties.get("name"));
        assertEquals(registry.getNumberType(), properties.get("age"));
    }
}