package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Test;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class RecordTypeTest {

    @Test
    public void testCreateRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty("a", registry.getStringType()));
        properties.put("b", new RecordProperty("b", registry.getIntType()));

        RecordType recordType = new RecordType(registry, properties);
        assertEquals(recordType.getClass(), RecordType.class);
        assertEquals(recordType.toString(), "RecordType { a: String, b: Number }");
    }

    @Test
    public void testIsSubtypeRecordTypeToRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", registry.getStringType()));
        RecordType recordTypeA = new RecordType(registry, propertiesA);

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty("a", registry.getStringType()));
        propertiesB.put("b", new RecordProperty("b", registry.getIntType()));
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        assertTrue(recordTypeA.isSubtype(recordTypeB));
    }

    @Test
    public void testIsSubtypeRecordTypeToRecordWithExtraProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", registry.getStringType()));
        RecordType recordTypeA = new RecordType(registry, propertiesA);

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty("a", registry.getStringType()));
        propertiesB.put("c", new RecordProperty("c", registry.getIntType()));
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        assertFalse(recordTypeA.isSubtype(recordTypeB));
    }

    @Test
    public void testIsSubtypeRecordTypeToRecordWithDifferentPropertyType() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", registry.getStringType()));
        RecordType recordTypeA = new RecordType(registry, propertiesA);

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty("a", registry.getIntType()));
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        assertFalse(recordTypeA.isSubtype(recordTypeB));
    }

    @Test
    public void testIsSubtypeRecordTypeToObjectType() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty("a", registry.getStringType()));
        RecordType recordType = new RecordType(registry, properties);

        assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType));
    }

    @Test
    public void testIsSubtypeObjectTypeToRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty("a", registry.getStringType()));
        RecordType recordType = new RecordType(registry, properties);

        assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    }

    @Test
    public void testIsSubtypeRecordTypeToRecordTypeWithEmptyProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", registry.getStringType()));
        RecordType recordTypeA = new RecordType(registry, propertiesA);

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        assertTrue(recordTypeA.isSubtype(recordTypeB));
    }

    @Test
    public void testIsSubtypeRecordTypeToRecordTypeWithSamePropertiesButDifferentRegistry() {
        JSTypeRegistry registryA = new JSTypeRegistry();
        JSTypeRegistry registryB = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty("a", registryA.getStringType()));
        RecordType recordTypeA = new RecordType(registryA, properties);
        JSType recordTypeBString = registryB.getStringType();
        properties.put("a", new RecordProperty("a", recordTypeBString));
        RecordType recordTypeB = new RecordType(registryB, properties);

        assertTrue(recordTypeA.isSubtype(recordTypeB));
    }

    @Test
    public void testIsSubtypeRecordTypeToRecordTypeWithFrozenProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty("a", registry.getStringType()));
        RecordType recordType = new RecordType(registry, properties);
        recordType.freeze();

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty("a", registry.getIntType()));
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        assertFalse(recordType.isSubtype(recordTypeB));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", registry.getStringType()));
        RecordType recordTypeA = new RecordType(registry, propertiesA);

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty("a", registry.getIntType()));
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
        assertEquals(greatestSubtype.getClass(), UnionType.class);
        assertTrue(greatestSubtype.isSubtype(recordTypeA));
        assertTrue(greatestSubtype.isSubtype(recordTypeB));

        // Test with empty record type
        Map<String, RecordProperty> emptyProperties = Maps.newHashMap();
        RecordType emptyRecordType = new RecordType(registry, emptyProperties);
        greatestSubtype = recordTypeA.getGreatestSubtypeHelper(emptyRecordType);
        assertEquals(greatestSubtype.getClass(), recordTypeA.getClass());

        // Test with same record type
        greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeA);
        assertEquals(greatestSubtype.getClass(), recordTypeA.getClass());

        // Test with native object type
        JSType nativeObjectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        greatestSubtype = recordTypeA.getGreatestSubtypeHelper(nativeObjectType);
        assertEquals(greatestSubtype.getClass(), recordTypeA.getClass());

        // Test with record type with incompatible property
        Map<String, RecordProperty> propertiesC = Maps.newHashMap();
        propertiesC.put("a", new RecordProperty("a", registry.getIntType()));
        RecordType recordTypeC = new RecordType(registry, propertiesC);
        greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeC);
        assertEquals(greatestSubtype.getClass(), registry.getNativeObjectType(JSTypeNative.NO_TYPE).getClass());

        // Test with record type with different property order
        Map<String, RecordProperty> propertiesD = Maps.newLinkedHashSet(propertiesA).asMap();
        RecordType recordTypeD = new RecordType(registry, propertiesD);
        greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeD);
        assertEquals(greatestSubtype.getClass(), recordTypeA.getClass());
    }

    @Test
    public void testGetGreatestSubtypeRecordTypes() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", registry.getStringType()));
        RecordType recordTypeA = new RecordType(registry, propertiesA);

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("b", new RecordProperty("b", registry.getIntType()));
        propertiesB.put("a", new RecordProperty("a", registry.getStringType()));
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        JSType greatestSubtype = recordTypeA.getGreatestSubtype(recordTypeB);
        assertEquals(greatestSubtype.getClass(), recordTypeB.getClass());

        greatestSubtype = recordTypeB.getGreatestSubtype(recordTypeA);
        assertEquals(greatestSubtype.getClass(), recordTypeB.getClass());
    }

    @Test
    public void testGetImplicitPrototype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty("a", registry.getStringType()));
        RecordType recordType = new RecordType(registry, properties);

        assertEquals(recordType.getImplicitPrototype().getClass(), registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getClass());
    }

    @Test
    public void testFreeze() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty("a", registry.getStringType()));
        RecordType recordType = new RecordType(registry, properties);

        recordType.freeze();

        try {
            recordType.addProperty("a", registry.getIntType(), null, null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", registry.getStringType()));
        RecordType recordTypeA = new RecordType(registry, propertiesA);

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty("a", registry.getStringType()));
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        assertTrue(recordTypeA.isEquivalentTo(recordTypeB));

        Map<String, RecordProperty> propertiesC = Maps.newHashMap();
        propertiesC.put("a", new RecordProperty("a", registry.getIntType()));
        RecordType recordTypeC = new RecordType(registry, propertiesC);

        assertFalse(recordTypeA.isEquivalentTo(recordTypeC));
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ErrorReporter errorReporter = new ErrorReporter() {};

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty("a", registry.getStringType()));
        RecordType recordType = new RecordType(registry, properties);

        JSType resolvedType = recordType.resolveInternal(errorReporter, new StaticScope<>(registry));
        assertEquals(resolvedType.getClass(), RecordType.class);

        properties.put("a", new RecordProperty("a", registry.getIntType()));

        resolvedType = recordType.resolveInternal(errorReporter, new StaticScope<>(registry));
        assertEquals(resolvedType.getClass(), UnionType.class);
        assertTrue(resolvedType.getTypes().contains(registry.getStringType()));
        assertTrue(resolvedType.getTypes().contains(registry.getIntType()));
    }
}