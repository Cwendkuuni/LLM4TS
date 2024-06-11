package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", int.class));
        RecordType recordTypeA = new RecordType(registry, propertiesA);

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty("a", int.class));
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        assertTrue(recordTypeA.isEquivalentTo(recordTypeB));

        Map<String, RecordProperty> propertiesC = Maps.newHashMap();
        propertiesC.put("a", new RecordProperty("a", long.class));
        RecordType recordTypeC = new RecordType(registry, propertiesC);

        assertFalse(recordTypeA.isEquivalentTo(recordTypeC));
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", int.class));
        RecordType recordTypeA = new RecordType(registry, propertiesA);

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty("a", int.class));
        propertiesB.put("b", new RecordProperty("b", int.class));
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        assertTrue(recordTypeA.isSubtype(recordTypeB));

        Map<String, RecordProperty> propertiesC = Maps.newHashMap();
        propertiesC.put("a", new RecordProperty("a", long.class));
        RecordType recordTypeC = new RecordType(registry, propertiesC);

        assertFalse(recordTypeA.isSubtype(recordTypeC));

        Map<String, RecordProperty> propertiesD = Maps.newHashMap();
        propertiesD.put("a", new RecordProperty("a", int.class));
        propertiesD.put("b", new RecordProperty("b", int.class));
        RecordType recordTypeD = new RecordType(registry, propertiesD);

        assertTrue(recordTypeB.isSubtype(recordTypeD));

        Map<String, RecordProperty> propertiesE = Maps.newHashMap();
        propertiesE.put("a", new RecordProperty("a", Object.class));
        RecordType recordTypeE = new RecordType(registry, propertiesE);

        assertTrue(recordTypeA.isSubtype(recordTypeE));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", int.class));
        RecordType recordTypeA = new RecordType(registry, propertiesA);

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty("a", int.class));
        propertiesB.put("b", new RecordProperty("b", String.class));
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        Map<String, RecordProperty> propertiesC = Maps.newHashMap();
        propertiesC.put("a", new RecordProperty("a", int.class));
        propertiesC.put("b", new RecordProperty("b", String.class));
        propertiesC.put("c", new RecordProperty("c", int.class));
        RecordType recordTypeC = new RecordType(registry, propertiesC);

        Map<String, RecordProperty> propertiesD = Maps.newHashMap();
        propertiesD.put("a", new RecordProperty("a", int.class));
        RecordType recordTypeD = new RecordType(registry, propertiesD);

        Map<String, RecordProperty> propertiesE = Maps.newHashMap();
        propertiesE.put("a", new RecordProperty("a", Object.class));
        RecordType recordTypeE = new RecordType(registry, propertiesE);

        JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
        assertEquals(recordTypeB, greatestSubtype);

        greatestSubtype = recordTypeB.getGreatestSubtypeHelper(recordTypeC);
        assertEquals(recordTypeC, greatestSubtype);

        greatestSubtype = recordTypeC.getGreatestSubtypeHelper(recordTypeD);
        assertEquals(recordTypeD, greatestSubtype);

        greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeE);
        assertEquals(recordTypeE, greatestSubtype);
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        RecordType recordType = new RecordType(registry, properties);

        JSType propertyType = int.class;
        String propertyName = "property";

        recordType.defineProperty(propertyName, propertyType, true, null);

        assertTrue(recordType.hasProperty(propertyName));
        assertEquals(propertyType, recordType.getPropertyType(propertyName));
    }

    @Test
    public void testDefinePropertyFrozen() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        RecordType recordType = new RecordType(registry, properties);

        recordType.isFrozen = true;

        JSType propertyType = int.class;
        String propertyName = "property";

        assertFalse(recordType.defineProperty(propertyName, propertyType, true, null));
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("property", new RecordProperty("property", int.class));
        RecordType recordType = new RecordType(registry, properties);
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>(registry);

        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
        assertEquals(recordType, resolvedType);
    }
}