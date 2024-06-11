package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {
    private JSTypeRegistry registry;
    private RecordType recordType;
    private Map<String, RecordTypeBuilder.RecordProperty> properties;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        properties = new HashMap<>();

        // Add properties to the map
        properties.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node(1)));
        properties.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), new Node(2)));

        // Create a new RecordType
        recordType = new RecordType(registry, properties);
    }

    @Test
    public void testIsSynthetic() {
        assertEquals(false, recordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType otherRecord = new RecordType(registry, properties);

        boolean result = recordType.checkRecordEquivalenceHelper(otherRecord, EquivalenceMethod.ignoreTypeParameterization());

        assertEquals(true, result);
    }

    @Test
    public void testDefineProperty() {
        boolean result = recordType.defineProperty("property3", registry.getNativeType(JSTypeNative.STRING_TYPE), true, new Node(3));

        assertEquals(true, result);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        ObjectType that = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);

        assertEquals(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype);
    }

    @Test
    public void testIsSubtype() {
        ObjectType that = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        boolean result = recordType.isSubtype(that);

        assertEquals(true, result);
    }

    @Test
    public void testIsSubtypeWithRecordType() {
        Map<String, RecordTypeBuilder.RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node(1)));
        otherProperties.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), new Node(2)));

        RecordType otherRecordType = new RecordType(registry, otherProperties);

        boolean result = RecordType.isSubtype(recordType, otherRecordType);

        assertEquals(true, result);
    }
}