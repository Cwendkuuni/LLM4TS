package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {
    private static final JSTypeRegistry registry = new JSTypeRegistry();

    @Test
    public void testRecordTypeCreation() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordProperty("a", registry.getStringType()));
        properties.put("b", new RecordProperty("b", registry.getNumberType()));

        RecordType recordType = new RecordType(registry, properties, true);
        assertNotNull(recordType);
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("a", new RecordProperty("a", registry.getStringType()));
        properties1.put("b", new RecordProperty("b", registry.getNumberType()));

        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("b", new RecordProperty("b", registry.getNumberType()));
        properties2.put("a", new RecordProperty("a", registry.getStringType()));

        RecordType recordType1 = new RecordType(registry, properties1, true);
        RecordType recordType2 = new RecordType(registry, properties2, true);

        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
        assertTrue(recordType2.checkRecordEquivalenceHelper(recordType1, false));
    }

    @Test
    public void testCheckRecordEquivalenceHelperWithUnknowns() {
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("a", new RecordProperty("a", registry.getStringType()));
        properties1.put("b", new RecordProperty("b", registry.getNumberType()));

        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("b", new RecordProperty("b", registry.getNumberType()));
        properties2.put("a", new RecordProperty("a", registry.getUnknownType()));

        RecordType recordType1 = new RecordType(registry, properties1, true);
        RecordType recordType2 = new RecordType(registry, properties2, true);

        assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));
        assertFalse(recordType2.checkRecordEquivalenceHelper(recordType1, false));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        Map<String, RecordProperty> propertiesA = new HashMap<>();
        propertiesA.put("a", new RecordProperty("a", registry.getStringType()));
        propertiesA.put("b", new RecordProperty("b", registry.getNumberType()));

        Map<String, RecordProperty> propertiesB = new HashMap<>();
        propertiesB.put("b", new RecordProperty("b", registry.getNumberType()));
        propertiesB.put("c", new RecordProperty("c", registry.getNumberType()));

        RecordType recordTypeA = new RecordType(registry, propertiesA, true);
        RecordType recordTypeB = new RecordType(registry, propertiesB, true);

        JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
        assertTrue(recordTypeB.isSubtype(grearestSubtype));

        greatestSubtype = recordTypeB.getGreatestSubtypeHelper(recordTypeA);
        assertTrue(recordTypeA.isSubtype(grearestSubtype));
    }

    @Test
    public void testGetGreatestSubtypeHelperWithConflictingProperties() {
        Map<String, RecordProperty> propertiesA = new HashMap<>();
        propertiesA.put("a", new RecordProperty("a", registry.getStringType()));
        propertiesA.put("b", new RecordProperty("b", registry.getNumberType()));

        Map<String, RecordProperty> propertiesB = new HashMap<>();
        propertiesB.put("b", new RecordProperty("b", registry.getStringType()));
        propertiesB.put("a", new RecordProperty("a", registry.getNumberType()));

        RecordType recordTypeA = new RecordType(registry, propertiesA, true);
        RecordType recordTypeB = new RecordType(registry, propertiesB, true);

        JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
        assertTrue(registry.getNativeObjectType(JSTypeNative.NO_TYPE).equals(grearestSubtype));

        greatestSubtype = recordTypeB.getGreatestSubtypeHelper(recordTypeA);
        assertTrue(registry.getNativeObjectType(JSTypeNative.NO_TYPE).equals(grearestSubtype));
    }

    @Test
    public void testIsSubtype() {
        Map<String, RecordProperty> propertiesA = new HashMap<>();
        propertiesA.put("a", new RecordProperty("a", registry.getStringType()));
        propertiesA.put("b", new RecordProperty("b", registry.getNumberType()));

        Map<String, RecordProperty> propertiesB = new HashMap<>();
        propertiesB.put("b", new RecordProperty("b", registry.getNumberType()));
        propertiesB.put("a", new RecordProperty("a", registry.getStringType()));

        RecordType recordTypeA = new RecordType(registry, propertiesA, true);
        RecordType recordTypeB = new RecordType(registry, propertiesB, true);

        assertTrue(recordTypeA.isSubtype(recordTypeB));
        assertTrue(recordTypeB.isSubtype(recordTypeA));
    }

    @Test
    public void testIsSubtypeWithDifferentNumberOfProperties() {
        Map<String, RecordProperty> propertiesA = new HashMap<>();
        propertiesA.put("a", new RecordProperty("a", registry.getStringType()));
        propertiesA.put("b", new RecordProperty("b", registry.getNumberType()));

        RecordType recordTypeA = new RecordType(registry, propertiesA, true);

        Map<String, RecordProperty> propertiesB = new HashMap<>();
        propertiesB.put("a", new RecordProperty("a", registry.getStringType()));

        RecordType recordTypeB = new RecordType(registry, propertiesB, true);

        assertFalse(recordTypeA.isSubtype(recordTypeB));
        assertFalse(recordTypeB.isSubtype(recordTypeA));
    }

    @Test
    public void testIsSubtypeWithIncompatiblePropertyTypes() {
        Map<String, RecordProperty> propertiesA = new HashMap<>();
        propertiesA.put("a", new RecordProperty("a", registry.getStringType()));
        propertiesA.put("b", new RecordProperty("b", registry.getNumberType()));

        Map<String, RecordProperty> propertiesB = new HashMap<>();
        propertiesB.put("a", new RecordProperty("a", registry.getNumberType()));
        propertiesB.put("b", new RecordProperty("b", registry.getStringType()));

        RecordType recordTypeA = new RecordType(registry, propertiesA, true);
        RecordType recordTypeB = new RecordType(registry, propertiesB, true);

        assertFalse(recordTypeA.isSubtype(recordTypeB));
        assertFalse(recordTypeB.isSubtype(recordTypeA));
    }

    @Test
    public void testIsSubtypeWithIncompatiblePropertyTypesButBIsASubtypeOfAAfterTypeCoercion() {
        Map<String, RecordProperty> propertiesA = new HashMap<>();
        propertiesA.put("a", new RecordProperty("a", registry.getStringType()));
        propertiesA.put("b", new RecordProperty("b", registry.getNumberType()));

        Map<String, RecordProperty> propertiesB = new HashMap<>();
        propertiesB.put("a", new RecordProperty("a", registry.getStringType()));
        propertiesB.put("b", new RecordProperty("b", registry.getBooleanType()));

        RecordType recordTypeA = new RecordType(registry, propertiesA, true);
        RecordType recordTypeB = new RecordType(registry, propertiesB, true);

        assertTrue(recordTypeA.isSubtype(recordTypeB));
        assertTrue(recordTypeB.isSubtype(recordTypeA));
    }

    @Test
    public void testIsSubtypeWithIncompatiblePropertyTypesButABIsASubtypeOfBAfterTypeCoercion() {
        Map<String, RecordProperty> propertiesA = new HashMap<>();
        propertiesA.put("a", new RecordProperty("a", registry.getStringType()));
        propertiesA.put("b", new RecordProperty("b", registry.getNumberType()));

        Map<String, RecordProperty> propertiesB = new HashMap<>();
        propertiesB.put("a", new RecordProperty("a", registry.getNumberType()));
        propertiesB.put("b", new RecordProperty("b", registry.getStringType()));

        RecordType recordTypeA = new RecordType(registry, propertiesA, true);
        RecordType recordTypeB = new RecordType(registry, propertiesB, true);

        assertTrue(recordTypeB.isSubtype(recordTypeA));
        assertTrue(recordTypeA.isSubtype(recordTypeB));
    }

    @Test
    public void testIsSubtypeWithUnionTypes() {
        Map<String, RecordProperty> propertiesA = new HashMap<>();
        propertiesA.put("a", new RecordProperty("a", registry.getStringType()));
        propertiesA.put("b", new RecordProperty("b", registry.getNumberType()));

        RecordType recordTypeA = new RecordType(registry, propertiesA, true);

        UnionTypeBuilder builder = new UnionTypeBuilder(registry);
        ObjectType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        builder.addAlternate(objType);
        builder.addAlternate(registry.getNativeType(JSTypeNative.NULL_TYPE));

        JSType unionType = builder.build();

        RecordType recordTypeB = new RecordType(registry, propertiesA, false);
        recordTypeB.setUnionType(unionType);

        assertTrue(recordTypeA.isSubtype(recordTypeB));
        assertTrue(recordTypeB.isSubtype(recordTypeA));
    }

    @Test
    public void testResolveInternal() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordProperty("a", registry.getStringType()));
        properties.put("b", new RecordProperty("b", registry.getNumberType()));

        RecordType recordType = new RecordType(registry, properties, true);

        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>(registry);
        scope.put("a", registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
        scope.put("b", registry.getNativeType(JSTypeNative.NUMBER_TYPE));

        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
        assertTrue(recordType.isEquivalentTo(resolvedType));
    }
}