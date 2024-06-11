package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {
    @Test
    public void testCreateRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("x", new RecordProperty("x", registry.getBooleanType()));
        properties.put("y", new RecordProperty("y", registry.getNumberType()));
        RecordType recordType = new RecordType(registry, properties);
        assertEquals(properties.size(), recordType.getPropertyCount());
        assertEquals("{x: boolean, y: number}", recordType.toString());
    }

    @Test
    public void testRecordTypeEquivalence() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("x", new RecordProperty("x", registry.getBooleanType()));
        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("x", new RecordProperty("x", registry.getNumberType()));
        propertiesB.put("y", new RecordProperty("y", registry.getNumberType()));

        RecordType recordTypeA = new RecordType(registry, propertiesA);
        RecordType recordTypeB = new RecordType(registry, propertiesB);
        assertFalse(recordTypeA.isEquivalentTo(recordTypeB));
    }

    @Test
    public void testRecordTypeEquivalenceWithSameProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("x", new RecordProperty("x", registry.getBooleanType()));
        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("x", new RecordProperty("x", registry.getBooleanType()));

        RecordType recordTypeA = new RecordType(registry, propertiesA);
        RecordType recordTypeB = new RecordType(registry, propertiesB);
        assertTrue(recordTypeA.isEquivalentTo(recordTypeB));
    }

    @Test
    public void testRecordTypeEquivalenceWithSubtypes() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("x", new RecordProperty("x", registry.getBooleanType()));
        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("x", new RecordProperty("x", registry.getNumberType()));
        propertiesB.put("y", new RecordProperty("y", registry.getNumberType()));

        RecordType recordTypeA = new RecordType(registry, propertiesA);
        RecordType recordTypeB = new RecordType(registry, propertiesB);
        assertFalse(recordTypeA.isEquivalentTo(recordTypeB));

        RecordType recordTypeC = new RecordType(registry, propertiesB);
        assertTrue(recordTypeA.isEquivalentTo(recordTypeC));
    }

    @Test
    public void testRecordTypeGetGreatestSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("x", new RecordProperty("x", registry.getBooleanType()));
        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("x", new RecordProperty("x", registry.getBooleanType()));
        propertiesB.put("y", new RecordProperty("y", registry.getNumberType()));

        RecordType recordTypeA = new RecordType(registry, propertiesA);
        RecordType recordTypeB = new RecordType(registry, propertiesB);
        JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
        assertTrue(recordTypeA.isEquivalentTo(greatestSubtype));

        RecordType recordTypeC = new RecordType(registry,
            Maps.newHashMap().put("x", new RecordProperty("x", registry.getNumberType())));
        UnionType greatestSubtype2 = (UnionType) recordTypeA.getGreatestSubtypeHelper(recordTypeC);
        assertEquals("{x: number}", greatestSubtype2.toString());
    }

    @Test
    public void testRecordTypeIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("x", new RecordProperty("x", registry.getBooleanType()));
        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("x", new RecordProperty("x", registry.getBooleanType()));
        propertiesB.put("y", new RecordProperty("y", registry.getNumberType()));

        RecordType recordTypeA = new RecordType(registry, propertiesA);
        RecordType recordTypeB = new RecordType(registry, propertiesB);
        assertFalse(recordTypeB.isSubtype(recordTypeA));

        RecordType recordTypeC = new RecordType(registry, propertiesB);
        assertTrue(recordTypeA.isSubtype(recordTypeC));
    }

    @Test
    public void testRecordTypeIsSubtypeWithPropertySubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("x", new RecordProperty("x", registry.getBooleanType()));
        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("x", new RecordProperty("x", registry.getNumberType()));
        propertiesB.put("y", new RecordProperty("y", registry.getNumberType()));

        RecordType recordTypeA = new RecordType(registry, propertiesA);
        RecordType recordTypeB = new RecordType(registry, propertiesB);
        assertFalse(recordTypeB.isSubtype(recordTypeA));

        UnionType numberTypeUnion = new UnionType(registry,
            new ObjectType[] { registry.getNumberType(), registry.getStringType() });
        RecordType recordTypeC = new RecordType(registry,
            Maps.newHashMap().put("x", new RecordProperty("x", numberTypeUnion)));

        assertTrue(recordTypeA.isSubtype(recordTypeC));
    }

    @Test
    public void testRecordTypeIsSubtypeWithObjectType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("x", new RecordProperty("x", registry.getBooleanType()));

        RecordType recordType = new RecordType(registry, properties);
        ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertTrue(objectType.isSubtype(recordType));
    }

    @Test
    public void testRecordTypeSubtypeWithRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("x", new RecordProperty("x", registry.getBooleanType()));
        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("x", new RecordProperty("x", registry.getBooleanType()));
        propertiesB.put("y", new RecordProperty("y", registry.getNumberType()));

        RecordType recordTypeA = new RecordType(registry, propertiesA);
        RecordType recordTypeB = new RecordType(registry, propertiesB);
        assertFalse(recordTypeB.isSubtype(recordTypeA));

        RecordType recordTypeC = new RecordType(registry, propertiesB);
        assertTrue(recordTypeA.isSubtype(recordTypeC));
    }

    @Test
    public void testRecordTypeToString() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("x", new RecordProperty("x", registry.getBooleanType()));
        properties.put("y", new RecordProperty("y", registry.getNumberType()));

        RecordType recordType = new RecordType(registry, properties);
        String recordTypeString = recordType.toString();
        assertEquals("{x: boolean, y: number}", recordTypeString);
    }
}