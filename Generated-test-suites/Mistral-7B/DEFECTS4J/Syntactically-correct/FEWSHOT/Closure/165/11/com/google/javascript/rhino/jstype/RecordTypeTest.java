package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.Maps;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.types.ObjectType;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class RecordTypeTest {

    @Test
    public void testRecordTypeCreation() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.INT_TYPE)));
        properties.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE)));

        RecordType recordType = new RecordType(registry, properties);

        assertEquals("Record <a: int, b: string>", recordType.toString());
    }

    @Test
    public void testRecordTypeProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.INT_TYPE)));
        properties.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE)));

        RecordType recordType = new RecordType(registry, properties);

        Set<String> propertyNames = recordType.getPropertyNames();
        assertEquals(2, propertyNames.size());
        assertTrue(propertyNames.contains("a"));
        assertTrue(propertyNames.contains("b"));

        JSType propertyTypeA = recordType.getPropertyType("a");
        assertEquals(registry.getNativeType(JSTypeNative.INT_TYPE), propertyTypeA);

        JSType propertyTypeB = recordType.getPropertyType("b");
        assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE), propertyTypeB);

        assertFalse(recordType.hasProperty("c"));

        recordType.defineProperty("c", registry.getNativeType(JSTypeNative.BOOL_TYPE), false, null);
        assertTrue(recordType.hasProperty("c"));
    }

    @Test
    public void testRecordTypeEquivalence() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.INT_TYPE)));
        propertiesA.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE)));

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.INT_TYPE)));
        propertiesB.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE)));
        propertiesB.put("c", new RecordProperty("c", registry.getNativeType(JSTypeNative.BOOL_TYPE)));

        RecordType recordTypeA = new RecordType(registry, propertiesA);
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        assertFalse(recordTypeA.isEquivalentTo(recordTypeB));

        propertiesB.remove("c");

        assertTrue(recordTypeA.isEquivalentTo(recordTypeB));

        RecordType recordTypeC = new RecordType(registry, propertiesA);
        assertTrue(recordTypeA.isEquivalentTo(recordTypeC));
    }

    @Test
    public void testRecordTypeSubtyping() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.INT_TYPE)));
        propertiesA.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE)));

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.INT_TYPE)));
        propertiesB.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE)));
        propertiesB.put("c", new RecordProperty("c", registry.getNativeType(JSTypeNative.BOOL_TYPE)));

        RecordType recordTypeA = new RecordType(registry, propertiesA);
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        RecordType recordTypeC = new RecordType(registry, propertiesA);
        RecordType recordTypeD = new RecordType(registry, propertiesB);

        assertFalse(recordTypeA.isSubtype(recordTypeB));
        assertFalse(recordTypeB.isSubtype(recordTypeA));

        assertTrue(recordTypeA.isSubtype(recordTypeC));
        assertTrue(recordTypeC.isSubtype(recordTypeA));

        assertTrue(recordTypeB.isSubtype(recordTypeD));
        assertFalse(recordTypeD.isSubtype(recordTypeB));

        // Record type A is a subtype of record type D because it has all the
        // properties of D and their types are compatible.
        assertTrue(recordTypeA.isSubtype(recordTypeD));
    }

    @Test
    public void testRecordTypeGetGreatestSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.INT_TYPE)));
        propertiesA.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE)));

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.INT_TYPE)));
        propertiesB.put("c", new RecordProperty("c", registry.getNativeType(JSTypeNative.BOOL_TYPE)));

        RecordType recordTypeA = new RecordType(registry, propertiesA);
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        JSType greatestSubtype = recordTypeA.getGreatestSubtype(recordTypeB);

        assertTrue(recordTypeA.isSubtype(greatestSubtype));
        assertTrue(recordTypeB.isSubtype(greatestSubtype));

        if (greatestSubtype instanceof RecordType) {
            RecordType greatestRecordType = (RecordType) greatestSubtype;
            assertEquals(2, greatestRecordType.getPropertyNames().size());
            assertTrue(greatestRecordType.hasProperty("a"));
            assertTrue(greatestRecordType.hasProperty("b"));
        } else if (greatestSubtype instanceof UnionType) {
            UnionType greatestUnionType = (UnionType) greatestSubtype;
            assertEquals(2, greatestUnionType.getAlternates().size());
            assertTrue(greatestUnionType.getAlternates().contains(recordTypeA));
            assertTrue(greatestUnionType.getAlternates().contains(recordTypeB));
        }
    }
}