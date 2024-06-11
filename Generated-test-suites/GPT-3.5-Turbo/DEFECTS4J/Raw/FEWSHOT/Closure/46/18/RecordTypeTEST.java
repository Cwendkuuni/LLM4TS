package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RecordTypeTest {
    
    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType recordType1 = new RecordType(registry, new HashMap<>());
        RecordType recordType2 = new RecordType(registry, new HashMap<>());

        assertTrue(recordType1.isEquivalentTo(recordType1));
        assertTrue(recordType1.isEquivalentTo(recordType2));
        assertTrue(recordType2.isEquivalentTo(recordType1));
        assertTrue(recordType2.isEquivalentTo(recordType2));
    }

    @Test
    public void testGetImplicitPrototype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType recordType = new RecordType(registry, new HashMap<>());

        ObjectType expectedPrototype = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        ObjectType actualPrototype = recordType.getImplicitPrototype();

        assertEquals(expectedPrototype, actualPrototype);
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType recordType1 = new RecordType(registry, new HashMap<>());
        RecordType recordType2 = new RecordType(registry, new HashMap<>());

        assertTrue(recordType1.isSubtype(recordType1));
        assertTrue(recordType1.isSubtype(recordType2));
        assertTrue(recordType2.isSubtype(recordType1));
        assertTrue(recordType2.isSubtype(recordType2));
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType recordType = new RecordType(registry, new HashMap<>());
        boolean result = recordType.defineProperty("property", registry.getNativeType(JSTypeNative.NULL_TYPE), false, null);

        assertTrue(result);
        assertTrue(recordType.hasProperty("property"));
        assertEquals(registry.getNativeType(JSTypeNative.NULL_TYPE), recordType.getPropertyType("property"));
    }
    
    @Test
    public void testDefineProperty_frozen() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType recordType = new RecordType(registry, new HashMap<>());
        recordType.setFrozen(true);
        
        boolean result = recordType.defineProperty("property", registry.getNativeType(JSTypeNative.NULL_TYPE), false, null);

        assertFalse(result);
        assertFalse(recordType.hasProperty("property"));
    }

    @Test
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType recordType1 = new RecordType(registry, new HashMap<>());
        RecordType recordType2 = new RecordType(registry, new HashMap<>());

        JSType expectedSupertype = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        JSType actualSupertype1 = recordType1.getLeastSupertype(recordType2);
        JSType actualSupertype2 = recordType2.getLeastSupertype(recordType1);

        assertEquals(expectedSupertype, actualSupertype1);
        assertEquals(expectedSupertype, actualSupertype2);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType recordType1 = new RecordType(registry, new HashMap<>());
        RecordType recordType2 = new RecordType(registry, new HashMap<>());

        JSType expectedSubtype = registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE);
        JSType actualSubtype1 = recordType1.getGreatestSubtypeHelper(recordType2);
        JSType actualSubtype2 = recordType2.getGreatestSubtypeHelper(recordType1);

        assertEquals(expectedSubtype, actualSubtype1);
        assertEquals(expectedSubtype, actualSubtype2);
    }

    @Test
    public void testToMaybeRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType recordType = new RecordType(registry, new HashMap<>());
        RecordType maybeRecordType = recordType.toMaybeRecordType();

        assertEquals(recordType, maybeRecordType);
    }
  
}