package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.javascript.rhino.Node;

public class RecordTypeTest {

    private JSTypeRegistry registry = new JSTypeRegistry();
    private Map<String, RecordProperty> properties = new HashMap<String, RecordProperty>();

    @Test
    public void testConstructor() {
        RecordType recordType = new RecordType(registry, properties);
        assertTrue(recordType.isSynthetic());

        RecordType recordTypeDeclared = new RecordType(registry, properties, true);
        assertFalse(recordTypeDeclared.isSynthetic());
    }

    @Test
    public void testDefineProperty() {
        RecordType recordType = new RecordType(registry, properties);
        recordType.defineProperty("prop1", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
        assertTrue(recordType.hasProperty("prop1"));

        recordType.defineProperty("prop2", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);
        assertTrue(recordType.hasProperty("prop2"));
    }

    @Test
    public void testGetGreatestSubtypeHelper_RecordType() {
        properties.put("prop1", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        properties.put("prop2", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        RecordType recordType = new RecordType(registry, properties);

        RecordType otherRecordType = new RecordType(registry, properties);
        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(otherRecordType);
        assertEquals(recordType, greatestSubtype);

        otherRecordType.defineProperty("prop3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), false, null);
        greatestSubtype = recordType.getGreatestSubtypeHelper(otherRecordType);
        assertTrue(greatestSubtype.isRecordType());
        assertEquals(3, greatestSubtype.getOwnPropertyNames().size());
        assertTrue(greatestSubtype.hasProperty("prop1"));
        assertTrue(greatestSubtype.hasProperty("prop2"));
        assertTrue(greatestSubtype.hasProperty("prop3"));
    }

    @Test
    public void testGetGreatestSubtypeHelper_JSType() {
        properties.put("prop1", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        properties.put("prop2", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        RecordType recordType = new RecordType(registry, properties);

        JSType objectType = new ObjectType(registry, null) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isSubtype(JSType that) { return false; }
        };

        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(objectType);
        assertTrue(greatestSubtype.isObjectType());
        assertEquals(objectType, greatestSubtype);

        greatestSubtype = objectType.getGreatestSubtype(recordType);
        assertEquals(objectType, greatestSubtype);
    }

    @Test
    public void testIsSubtype() {
        properties.put("prop1", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        properties.put("prop2", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        RecordType recordType = new RecordType(registry, properties);

        RecordType otherRecordType = new RecordType(registry, properties);
        assertTrue(recordType.isSubtype(otherRecordType));

        otherRecordType.defineProperty("prop3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), false, null);
        assertFalse(recordType.isSubtype(otherRecordType));

        ObjectType objectType = new ObjectType(registry, null) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isSubtype(JSType that) { return false; }
        };
        assertTrue(recordType.isSubtype(objectType));
    }
}