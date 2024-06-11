package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {

    @Test
    public void testRecordTypeCreation() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty("a", registry.getNativeBooleanType()));
        properties.put("b", new RecordProperty("b", registry.getNativeStringType()));

        RecordType recordType = new RecordType(registry, properties);

        assertEquals(recordType.getClass(), RecordType.class);
        assertEquals(recordType.getName(), "AnonymousRecordType{a: boolean, b: string}");
        assertEquals(recordType.properties.size(), 2);

        RecordProperty a = recordType.getProperty("a");
        RecordProperty b = recordType.getProperty("b");

        assertEquals(a.getName(), "a");
        assertEquals(a.getType().getClass(), BooleanType.class);
        assertEquals(b.getName(), "b");
        assertEquals(b.getType().getClass(), StringType.class);

        assertTrue(recordType.isEquivalentTo(recordType));
        assertFalse(recordType.isEquivalentTo(registry.getNativeIntType()));
    }

    @Test
    public void testRecordTypeEquivalence() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", registry.getNativeBooleanType()));
        propertiesA.put("b", new RecordProperty("b", registry.getNativeStringType()));

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty("a", registry.getNativeBooleanType()));
        propertiesB.put("b", new RecordProperty("b", registry.getNativeStringType()));
        propertiesB.put("c", new RecordProperty("c", registry.getNativeNumberType()));

        RecordType recordTypeA = new RecordType(registry, propertiesA);
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        assertFalse(recordTypeA.isEquivalentTo(recordTypeB));

        propertiesB.remove("c");

        assertTrue(recordTypeA.isEquivalentTo(recordTypeB));
    }

    @Test
    public void testRecordTypePropertyAccess() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty("a", registry.getNativeBooleanType()));
        properties.put("b", new RecordProperty("b", registry.getNativeStringType()));

        RecordType recordType = new RecordType(registry, properties);

        assertEquals(recordType.getProperty("a").getType(),
                recordType.getProperty("a").getType());
        assertEquals(recordType.getProperty("a").getType(), registry.getNativeBooleanType());

        RecordProperty propertyA = recordType.getProperty("a");
        RecordProperty propertyB = recordType.getProperty("b");

        assertFalse(propertyA.isEquivalentTo(propertyB));
        assertFalse(propertyA.isEquivalentTo(recordType.getProperty("b")));

        assertEquals(propertyA.getPropertyNode(), propertyA.getPropertyNode());
        assertNull(propertyA.getPropertyNode());
    }

    @Test
    public void testRecordTypeIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", registry.getNativeBooleanType()));
        propertiesA.put("b", new RecordProperty("b", registry.getNativeStringType()));

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("b", new RecordProperty("b", registry.getNativeStringType()));
        propertiesB.put("c", new RecordProperty("c", registry.getNativeNumberType()));

        RecordType recordTypeA = new RecordType(registry, propertiesA);
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        assertTrue(recordTypeA.isSubtype(recordTypeA));
        assertFalse(recordTypeA.isSubtype(registry.getNativeIntType()));
        assertFalse(recordTypeA.isSubtype(recordTypeB));

        Map<String, RecordProperty> propertiesC = new TreeMap<>();
        propertiesC.putAll(propertiesA);
        propertiesC.put("c", new RecordProperty("c", registry.getNativeNumberType()));

        RecordType recordTypeC = new RecordType(registry, propertiesC);

        assertTrue(recordTypeA.isSubtype(recordTypeC));
        assertTrue(recordTypeC.isSubtype(recordTypeA));
    }

    @Test
    public void testRecordTypeGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", registry.getNativeBooleanType()));
        propertiesA.put("b", new RecordProperty("b", registry.getNativeStringType()));

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("b", new RecordProperty("b", registry.getNativeStringType()));
        propertiesB.put("c", new RecordProperty("c", registry.getNativeNumberType()));

        RecordType recordTypeA = new RecordType(registry, propertiesA);
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        UnionType result = recordTypeA.getLeastSupertype(recordTypeB);

        assertTrue(result.isInstanceOf(UnionType.class));
        assertTrue(result.getAlternates().size() > 0);

        JSType typeC = new RecordType(registry, Maps.newHashMap());
        JSType typeD = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

        RecordType recordTypeC = new RecordType(registry, propertiesA);
        RecordType recordTypeD = new RecordType(registry, Maps.newHashMap());

        JSType leastSupertypeC = recordTypeA.getLeastSupertype(recordTypeC);
        JSType leastSupertypeD = recordTypeA.getLeastSupertype(recordTypeD);

        assertEquals(leastSupertypeC, recordTypeC);
        assertEquals(leastSupertypeD, typeD);
    }

    @Test
    public void testRecordTypeGetGreatestSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", registry.getNativeBooleanType()));
        propertiesA.put("b", new RecordProperty("b", registry.getNativeStringType()));

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("b", new RecordProperty("b", registry.getNativeStringType()));

        RecordType recordTypeA = new RecordType(registry, propertiesA);
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        JSType greatestSubtype = recordTypeB.getGreatestSubtype(recordTypeA);

        assertTrue(greatestSubtype.isInstanceOf(RecordType.class));
        assertEquals(greatestSubtype.getName(), "AnonymousRecordType{b: string}");
    }

    @Test
    public void testRecordTypeFrozen() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty("a", registry.getNativeBooleanType()));
        properties.put("b", new RecordProperty("b", registry.getNativeStringType()));

        RecordType recordType = new RecordType(registry, properties);

        recordType.freeze();

        assertFalse(recordType.defineProperty("a", registry.getNativeIntType(), false, null));
    }

    @Test
    public void testRecordTypeToString() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty("a", registry.getNativeBooleanType()));
        properties.put("b", new RecordProperty("b", registry.getNativeStringType()));

        RecordType recordType = new RecordType(registry, properties);

        assertEquals(recordType.toString(), "AnonymousRecordType{a: boolean, b: string}");
    }
}