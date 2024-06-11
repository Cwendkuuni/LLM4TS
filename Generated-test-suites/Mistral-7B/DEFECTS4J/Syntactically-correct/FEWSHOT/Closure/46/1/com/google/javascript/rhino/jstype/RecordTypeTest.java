package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {

    @Test
    public void testCreateRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty("a", JSType.INT));
        RecordType recordType = new RecordType(registry, properties);
        assertNotNull(recordType);
    }

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", JSType.INT));
        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty("a", JSType.INT));
        propertiesB.put("b", new RecordProperty("b", JSType.INT));

        RecordType recordTypeA = new RecordType(registry, propertiesA);
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        assertTrue(recordTypeA.isEquivalentTo(recordTypeB));
    }

    @Test
    public void testIsEquivalentToDifferentProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", JSType.INT));
        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("b", new RecordProperty("b", JSType.INT));

        RecordType recordTypeA = new RecordType(registry, propertiesA);
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        assertFalse(recordTypeA.isEquivalentTo(recordTypeB));
    }

    @Test
    public void testIsEquivalentToNull() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType = new RecordType(registry, Maps.newHashMap());

        assertFalse(recordType.isEquivalentTo(null));
    }

    @Test
    public void testGetImplicitPrototype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();

        RecordType recordType = new RecordType(registry, properties);

        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
            recordType.getImplicitPrototype());
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();

        RecordType recordType = new RecordType(registry, properties);

        recordType.defineProperty("a", JSType.INT, false, null);

        assertTrue(recordType.hasProperty("a"));
        assertEquals(JSType.INT, recordType.getPropertyType("a"));
    }

    @Test
    public void testDefinePropertyFrozen() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();

        RecordType recordType = new RecordType(registry, properties);

        recordType.defineProperty("a", JSType.INT, false, null);
        recordType.isFrozen = true;

        assertFalse(recordType.defineProperty("a", JSType.INT, false, null));
    }

    @Test
    public void testGetLeastSupertypeSameType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", JSType.INT));
        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty("a", JSType.INT));

        RecordType recordTypeA = new RecordType(registry, propertiesA);
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        JSType leastSupertype = recordTypeA.getLeastSupertype(recordTypeB);

        assertEquals(recordTypeA, leastSupertype);
    }

    @Test
    public void testGetLeastSupertypeDifferentProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", JSType.INT));
        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("b", new RecordProperty("b", JSType.INT));

        RecordType recordTypeA = new RecordType(registry, propertiesA);
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        JSType leastSupertype = recordTypeA.getLeastSupertype(recordTypeB);

        assertTrue(leastSupertype instanceof PrototypeObjectType);
        assertNull(leastSupertype.getPropertyType("b"));
    }

    @Test
    public void testGetLeastSupertypeNoProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordTypeA = new RecordType(registry, Maps.newHashMap());
        RecordType recordTypeB = new RecordType(registry, Maps.newHashMap());

        JSType leastSupertype = recordTypeA.getLeastSupertype(recordTypeB);

        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), leastSupertype);
    }

    @Test
    public void testGetLeastSupertypeWithOtherType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty("a", JSType.INT));

        RecordType recordType = new RecordType(registry, properties);
        JSType otherType = registry.getNativeType(JSTypeNative.STRING);

        JSType leastSupertype = recordType.getLeastSupertype(otherType);

        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), leastSupertype);
    }

    @Test
    public void testGetGreatestSubtypeHelperSameType() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType recordTypeA = new RecordType(registry, Maps.newHashMap());
        RecordType recordTypeB = new RecordType(registry, Maps.newHashMap());

        JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);

        assertEquals(recordTypeA, greatestSubtype);
    }

    @Test
    public void testGetGreatestSubtypeHelperDifferentProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", JSType.INT));
        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("b", new RecordProperty("b", JSType.INT));

        RecordType recordTypeA = new RecordType(registry, propertiesA);
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);

        assertTrue(greatestSubtype instanceof RecordType);
        assertEquals(2, ((RecordType) greatestSubtype).properties.size());
    }

    @Test
    public void testGetGreatestSubtypeHelperNoProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType recordTypeA = new RecordType(registry, Maps.newHashMap());
        RecordType recordTypeB = new RecordType(registry, Maps.newHashMap());

        JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);

        assertEquals(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype);
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", JSType.INT));
        RecordType recordTypeA = new RecordType(registry, propertiesA);

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty("a", JSType.INT));
        propertiesB.put("b", new RecordProperty("b", JSType.INT));
        RecordType recordTypeB = new RecordType(registry, propertiesB);

        Map<String, RecordProperty> propertiesC = Maps.newHashMap();
        propertiesC.put("b", new RecordProperty("b", JSType.INT));
        RecordType recordTypeC = new RecordType(registry, propertiesC);

        Map<String, RecordProperty> propertiesD = Maps.newHashMap();
        propertiesD.put("c", new RecordProperty("c", JSType.INT));
        RecordType recordTypeD = new RecordType(registry, propertiesD);

        assertTrue(recordTypeA.isSubtype(recordTypeA));
        assertTrue(recordTypeA.isSubtype(recordTypeB));
        assertTrue(recordTypeB.isSubtype(recordTypeA));
        assertFalse(recordTypeC.isSubtype(recordTypeA));
        assertFalse(recordTypeD.isSubtype(recordTypeA));
    }

    @Test
    public void testIsSubtypeWithEmptyRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType recordTypeA = new RecordType(registry, Maps.newHashMap());
        RecordType recordTypeB = new RecordType(registry, Maps.newHashMap());
        RecordType recordTypeEmpty = new RecordType(registry, Maps.newHashMap());

        assertTrue(recordTypeA.isSubtype(recordTypeEmpty));
        assertTrue(recordTypeEmpty.isSubtype(recordTypeA));
        assertTrue(recordTypeB.isSubtype(recordTypeEmpty));
        assertTrue(recordTypeEmpty.isSubtype(recordTypeB));
    }

    @Test
    public void testIsSubtypeWithNull() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType recordType = new RecordType(registry, Maps.newHashMap());

        assertFalse(recordType.isSubtype(null));
    }

    @Test
    public void testIsSubtypeWithDifferentTypes() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty("a", JSType.INT));
        RecordType recordType = new RecordType(registry, properties);

        JSType otherType = registry.getNativeType(JSTypeNative.STRING);

        assertFalse(recordType.isSubtype(otherType));
    }

    @Test
    public void testResolveProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();

        RecordType recordType = new RecordType(registry, properties);

        JSType propertyTypeA = new IntType();
        properties.put("a", new RecordProperty("a", propertyTypeA));

        ErrorReporter errorReporter = new ErrorReporter() {
            @Override
            public void error(int line, int pos, int severity, String message) {
                fail("Unexpected error: " + message);
            }

            @Override
            public void warning(int line, int pos, String message) {
                // Do nothing.
            }
        };
        StaticScope<JSType> scope = new StaticScope<JSType>(registry);

        recordType.resolveInternal(errorReporter, scope);

        JSType resolvedTypeA = properties.get("a").getType();
        assertEquals(propertyTypeA, resolvedTypeA);
    }
}