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
    public void testCreateRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty("a", registry.getNumberType()));
        RecordType recordType = new RecordType(registry, properties);
        assertNotNull(recordType);
    }

    @Test(expected = IllegalStateException.class)
    public void testNullRecordProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", null);
        new RecordType(registry, properties);
    }

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("a", new RecordProperty("a", registry.getNumberType()));
        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("a", new RecordProperty("a", registry.getNumberType()));
        properties2.put("b", new RecordProperty("b", registry.getNumberType()));
        RecordType recordType2 = new RecordType(registry, properties2);

        Map<String, RecordProperty> properties3 = Maps.newHashMap();
        properties3.put("b", new RecordProperty("b", registry.getNumberType()));
        RecordType recordType3 = new RecordType(registry, properties3);

        assertTrue(recordType1.isEquivalentTo(recordType1));
        assertTrue(recordType1.isEquivalentTo(recordType2));
        assertTrue(recordType2.isEquivalentTo(recordType2));
        assertTrue(recordType2.isEquivalentTo(recordType3));

        assertFalse(recordType1.isEquivalentTo(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
        assertFalse(recordType1.isEquivalentTo(registry.getNativeType(JSTypeNative.NO_TYPE)));
    }

    @Test
    public void testGetImplicitPrototype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        RecordType recordType = new RecordType(registry, properties);
        assertNotNull(recordType.getImplicitPrototype());
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        RecordType recordType = new RecordType(registry, properties);

        recordType.defineProperty("a", registry.getNumberType(), false, null);
        assertTrue(recordType.hasProperty("a"));
        assertNotNull(recordType.getPropertyType("a"));
        assertNull(recordType.getPropertyNode("a"));

        recordType.defineProperty("a", registry.getNumberType(), true, null);
        assertTrue(recordType.isPropertyTypeDeclared("a"));
        assertNotNull(recordType.getPropertyType("a"));
        assertNull(recordType.getPropertyNode("a"));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();

        JSType recordType1 = new RecordType(registry, Maps.newHashMap());
        JSType recordType2 = new RecordType(registry, Maps.newHashMap());
        JSType recordType3 = new RecordType(registry, Maps.newHashMap());
        JSType recordType4 = new RecordType(registry, Maps.newHashMap());

        JSType type1 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getGreatestSubtype(recordType1);
        assertEquals(recordType1, type1);

        recordType2.addProperty("a", registry.getNumberType(), null, null);
        JSType type2 = recordType1.getGreatestSubtypeHelper(recordType2);
        assertEquals(recordType2, type2);

        recordType3.addProperty("a", registry.getNumberType(), null, null);
        recordType3.addProperty("b", registry.getNumberType(), null, null);
        JSType type3 = recordType1.getGreatestSubtypeHelper(recordType3);
        assertEquals(recordType3, type3);

        recordType4.addProperty("b", registry.getNumberType(), null, null);
        JSType type4 = recordType3.getGreatestSubtypeHelper(recordType4);
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), type4);

        JSType recordType5 = new RecordType(registry, Maps.newHashMap());
        recordType5.addProperty("a", registry.getNumberType(), null, null);
        JSType type5 = recordType1.getGreatestSubtypeHelper(recordType5);
        assertEquals(recordType5, type5);

        JSType recordType6 = new RecordType(registry, Maps.newHashMap());
        recordType6.addProperty("b", registry.getNumberType(), null, null);
        JSType type6 = recordType5.getGreatestSubtypeHelper(recordType6);
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), type6);
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        JSType recordType1 = new RecordType(registry, Maps.newHashMap());
        JSType recordType2 = new RecordType(registry, Maps.newHashMap());
        JSType recordType3 = new RecordType(registry, Maps.newHashMap());
        JSType recordType4 = new RecordType(registry, Maps.newHashMap());
        JSType nativeObjectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

        // RecordType1 is a subtype of itself
        assertTrue(recordType1.isSubtype(recordType1));

        // RecordType1 is a subtype of RecordType1 with an additional property
        recordType2.addProperty("a", registry.getNumberType(), null, null);
        assertTrue(recordType1.isSubtype(recordType2));

        // RecordType1 is a subtype of RecordType1 with a different property
        recordType2.addProperty("b", registry.getNumberType(), null, null);
        assertFalse(recordType1.isSubtype(recordType2));

        // RecordType1 is a subtype of Object type
        assertTrue(nativeObjectType.isSubtype(recordType1));

        // RecordType1 is not a subtype of RecordType1 with a different property type
        recordType3.addProperty("a", registry.getBooleanType(), null, null);
        assertFalse(recordType1.isSubtype(recordType3));

        // RecordType1 is a subtype of RecordType1 with multiple additional properties
        recordType4.addProperty("a", registry.getNumberType(), null, null);
        recordType4.addProperty("b", registry.getNumberType(), null, null);
        assertTrue(recordType1.isSubtype(recordType4));

        // RecordType1 is not a subtype of RecordType1 with a missing property
        recordType4.removeProperty("a");
        assertFalse(recordType1.isSubtype(recordType4));

        // RecordType1 is not a subtype of NO_TYPE
        assertFalse(registry.getNativeType(JSTypeNative.NO_TYPE).isSubtype(recordType1));

        // Object type is a subtype of RecordType1
        assertTrue(recordType1.isSubtype(nativeObjectType));

        // Object type is not a subtype of RecordType1 with a different property
        JSType objectTypeWithProperty = new RecordType(registry, Maps.newHashMap())
                .addProperty("a", registry.getNumberType(), null, null);
        assertFalse(nativeObjectType.isSubtype(objectTypeWithProperty));

        // RecordType1 is a subtype of another RecordType1
        JSType recordType5 = new RecordType(registry, Maps.newHashMap());
        recordType5.addProperty("a", registry.getNumberType(), null, null);
        assertTrue(recordType1.isSubtype(recordType5));

        // RecordType1 is a subtype of RecordType1 with a different property name
        JSType recordType6 = new RecordType(registry, Maps.newHashMap());
        recordType6.addProperty("b", registry.getNumberType(), null, null);
        assertTrue(recordType1.isSubtype(recordType6));
    }
}