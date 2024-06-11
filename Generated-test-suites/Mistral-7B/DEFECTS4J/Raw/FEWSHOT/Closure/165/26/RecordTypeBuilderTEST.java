package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeBuilderTest {

    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
        String name = "name";
        JSType type = registry.getBooleanType();
        Node propertyNode = new Node(); // Assuming a valid property node for testing

        RecordTypeBuilder builder = recordTypeBuilder.addProperty(name, type, propertyNode);
        assertNotNull(builder);
        RecordProperty recordProperty = builder.properties.get(name);
        assertNotNull(recordProperty);
        assertEquals(type, recordProperty.getType());
    }

    @Test
    public void testAddDuplicateProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
        String name = "name";
        JSType type = registry.getBooleanType();
        Node propertyNode = new Node(); // Assuming a valid property node for testing

        RecordTypeBuilder builder = recordTypeBuilder.addProperty(name, type, propertyNode);
        assertNotNull(builder);
        RecordProperty recordProperty = builder.properties.get(name);
        assertNotNull(recordProperty);

        builder = recordTypeBuilder.addProperty(name, type, propertyNode);
        assertNull(builder);
    }

    @Test
    public void testBuildEmptyRecord() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
        JSType record = recordTypeBuilder.build();
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), record);
    }

    @Test
    public void testBuildRecordWithProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
        String name1 = "name1";
        String name2 = "name2";
        JSType boolType = registry.getBooleanType();
        JSType intType = registry.getIntType();
        Node propertyNode1 = new Node(); // Assuming valid property nodes for testing
        Node propertyNode2 = new Node(); // Assuming valid property nodes for testing

        RecordTypeBuilder builder = recordTypeBuilder
            .addProperty(name1, boolType, propertyNode1)
            .addProperty(name2, intType, propertyNode2);
        JSType record = builder.build();
        RecordType recordType = (RecordType) record;
        Map<String, JSType> properties = recordType.getProperties();
        assertEquals(2, properties.size());
        assertEquals(boolType, properties.get(name1));
        assertEquals(intType, properties.get(name2));
    }
}