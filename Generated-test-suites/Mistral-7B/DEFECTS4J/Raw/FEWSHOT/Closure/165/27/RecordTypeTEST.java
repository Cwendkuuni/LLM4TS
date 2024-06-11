package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

    @Test
    public void testRecordTypeCreation() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty(Integer.TYPE, null));
        RecordType recordType = new RecordType(registry, properties);
        assertTrue(recordType.isRecordType());
    }

    @Test
    public void testIsEquivalentToSameRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty(Integer.TYPE, null));
        RecordType recordType1 = new RecordType(registry, properties);
        RecordType recordType2 = new RecordType(registry, properties);
        assertTrue(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsEquivalentToDifferentRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty(Integer.TYPE, null));
        RecordType recordTypeA = new RecordType(registry, propertiesA);

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("b", new RecordProperty(Integer.TYPE, null));
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        assertFalse(recordTypeA.isEquivalentTo(recordTypeB));
    }

    @Test
    public void testIsSubtypeRecordTypeWithSameProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty(Integer.TYPE, null));
        RecordType recordTypeA = new RecordType(registry, propertiesA);

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty(Integer.TYPE, null));
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        assertTrue(recordTypeA.isSubtype(recordTypeB));
        assertTrue(recordTypeB.isSubtype(recordTypeA));
    }

    @Test
    public void testIsSubtypeRecordTypeWithDifferentProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty(Integer.TYPE, null));
        RecordType recordTypeA = new RecordType(registry, propertiesA);

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("b", new RecordProperty(Integer.TYPE, null));
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        assertFalse(recordTypeA.isSubtype(recordTypeB));
        assertFalse(recordTypeB.isSubtype(recordTypeA));
    }

    @Test
    public void testIsSubtypeRecordTypeWithSuperProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty(Integer.TYPE, null));
        RecordType recordTypeA = new RecordType(registry, propertiesA);

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty(String.class, null));
        propertiesB.put("b", new RecordProperty(Integer.TYPE, null));
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        assertTrue(recordTypeA.isSubtype(recordTypeB));
    }

    @Test
    public void testIsSubtypeRecordTypeWithSubProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty(Integer.TYPE, null));
        propertiesA.put("b", new RecordProperty(Integer.TYPE, null));
        RecordType recordTypeA = new RecordType(registry, propertiesA);

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty(Integer.TYPE, null));
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        Map<String, RecordProperty> propertiesC = Maps.newHashMap();
        propertiesC.put("a", new RecordProperty(Integer.TYPE, null));
        propertiesC.put("b", new RecordProperty(Long.TYPE, null));
        RecordType recordTypeC = new RecordType(registry, propertiesC);

        assertTrue(recordTypeA.isSubtype(recordTypeB));
        assertFalse(recordTypeB.isSubtype(recordTypeC));
    }

    @Test
    public void testGetImplicitPrototype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty(Integer.TYPE, null));
        RecordType recordType = new RecordType(registry, properties);

        ObjectType implicitPrototype = recordType.getImplicitPrototype();
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty(Integer.TYPE, null));
        RecordType recordType = new RecordType(registry, properties);

        recordType.defineProperty("b", String.class, false, null);

        assertTrue(recordType.hasProperty("b"));
        assertTrue(recordType.getPropertyType("b").isStringType());
    }

    @Test
    public void testDefinePropertyFrozenRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty(Integer.TYPE, null));
        RecordType recordType = new RecordType(registry, properties);
        recordType.freeze();

        assertFalse(recordType.defineProperty("b", String.class, false, null));
    }

    @Test
    public void testGetGreatestSubtypeHelperNullType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordTypeA = createRecordTypeWithProperties(registry, "a");
        JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(null);
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);
    }

    @Test
    public void testGetGreatestSubtypeHelperSameType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordTypeA = createRecordTypeWithProperties(registry, "a");
        RecordType recordTypeB = createRecordTypeWithProperties(registry, "a");
        JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
        assertEquals(recordTypeA, greatestSubtype);
    }

    @Test
    public void testGetGreatestSubtypeHelperDifferentTypes() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordTypeA = createRecordTypeWithProperties(registry, "a");
        RecordType recordTypeB = createRecordTypeWithProperties(registry, "b");
        JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);
    }

    @Test
    public void testGetGreatestSubtypeHelperSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordTypeA = createRecordTypeWithProperties(registry, "a");
        RecordType recordTypeB = createRecordTypeWithProperties(registry, "b");
        RecordType recordTypeC = createRecordTypeWithProperties(registry, "c");
        recordTypeC.addProperty("a", recordTypeB);
        JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeC);
        assertEquals(recordTypeB, greatestSubtype);
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType = createRecordTypeWithProperties(registry, "a");
        ErrorReporter errorReporter = new ErrorReporter() {
            @Override
            public void error(String message, int line, int column) {
                fail("Should not reach here.");
            }
        };
        StaticScope<JSType> scope = new StaticScope<>(registry, null);

        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
        assertNotNull(resolvedType);
    }

    private static RecordType createRecordTypeWithProperties(JSTypeRegistry registry, String propertyName) {
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put(propertyName, new RecordProperty(Integer.TYPE, null));
        RecordType recordType = new RecordType(registry, properties);
        return recordType;
    }
}