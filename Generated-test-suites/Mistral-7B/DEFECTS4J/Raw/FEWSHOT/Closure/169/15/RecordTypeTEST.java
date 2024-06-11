package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

    @Test
    public void testConstructors() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty(registry.getNullType(), null));
        RecordType recordType = new RecordType(registry, properties, true);
        RecordType synthesizedRecordType = new RecordType(registry, properties, false);
        assertFalse(recordType.isSynthetic());
        assertTrue(synthesizedRecordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty(registry.getBooleanType(), null));
        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty(registry.getNumberType(), null));
        RecordType recordTypeA = new RecordType(registry, propertiesA, true);
        RecordType recordTypeB = new RecordType(registry, propertiesB, true);
        assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));

        RecordType recordTypeC = new RecordType(registry, propertiesA, true);
        assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeC, false));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordTypeA = new RecordType(registry, Maps.newHashMap(), true) {
            {
                defineDeclaredProperty("a", registry.getStringType(), null);
                defineDeclaredProperty("b", registry.getNumberType(), null);
            }
        };
        RecordType recordTypeB = new RecordType(registry, Maps.newHashMap(), true) {
            {
                defineDeclaredProperty("a", registry.getNumberType(), null);
                defineDeclaredProperty("b", registry.getStringType(), null);
            }
        };
        RecordType recordTypeC = new RecordType(registry, Maps.newHashMap(), true) {
            {
                defineDeclaredProperty("c", registry.getBooleanType(), null);
            }
        };
        RecordType recordTypeD = new RecordType(registry, Maps.newHashMap(), true) {
            {
                defineDeclaredProperty("a", registry.getStringType(), null);
                defineDeclaredProperty("b", registry.getStringType(), null);
            }
        };

        JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
        assertNull(grearestSubtype);

        greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeC);
        assertNull(grearestSubtype);

        greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeD);
        assertTrue(recordTypeA.isEquivalentTo(grearestSubtype));
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordTypeA = new RecordType(registry, Maps.newHashMap(), true) {
            {
                defineDeclaredProperty("a", registry.getBooleanType(), null);
                defineDeclaredProperty("b", registry.getNumberType(), null);
            }
        };
        RecordType recordTypeB = new RecordType(registry, Maps.newHashMap(), true) {
            {
                defineDeclaredProperty("a", registry.getNumberType(), null);
                defineDeclaredProperty("b", registry.getStringType(), null);
            }
        };
        RecordType recordTypeC = new RecordType(registry, Maps.newHashMap(), true) {
            {
                defineDeclaredProperty("c", registry.getBooleanType(), null);
            }
        };
        RecordType recordTypeD = new RecordType(registry, Maps.newHashMap(), true) {
            {
                defineDeclaredProperty("a", registry.getStringType(), null);
                defineDeclaredProperty("b", registry.getStringType(), null);
            }
        };
        RecordType recordTypeE = new RecordType(registry, Maps.newHashMap(), true) {
            {
                defineDeclaredProperty("a", registry.getBooleanType(), null);
                defineDeclaredProperty("b", registry.getNumberType(), null);
            }
        };

        assertTrue(recordTypeA.isSubtype(recordTypeE));
        assertTrue(recordTypeE.isSubtype(recordTypeA));

        assertFalse(recordTypeA.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
        assertFalse(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordTypeA));

        assertFalse(recordTypeA.isSubtype(recordTypeB));
        assertFalse(recordTypeB.isSubtype(recordTypeA));

        assertTrue(recordTypeA.isSubtype(recordTypeC));
        assertTrue(recordTypeC.isSubtype(recordTypeA));

        assertTrue(recordTypeA.isSubtype(recordTypeD));
        assertTrue(recordTypeD.isSubtype(recordTypeA));
    }
}