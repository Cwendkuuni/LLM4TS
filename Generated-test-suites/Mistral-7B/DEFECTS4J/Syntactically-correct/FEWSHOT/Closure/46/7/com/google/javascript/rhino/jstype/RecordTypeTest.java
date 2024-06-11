package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

public class RecordTypeTest {
    @Test
    public void testCreateRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propertiesMap = Maps.newHashMap();
        propertiesMap.put("a", new RecordProperty("a", IntType.getInstance(registry), null));
        propertiesMap.put("b", new RecordProperty("b", StringType.getInstance(registry), null));

        RecordType recordType = new RecordType(registry, propertiesMap);
        assertNotNull(recordType);
    }

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propertiesMap1 = Maps.newHashMap();
        propertiesMap1.put("a", new RecordProperty("a", IntType.getInstance(registry), null));
        propertiesMap1.put("b", new RecordProperty("b", StringType.getInstance(registry), null));

        RecordType recordType1 = new RecordType(registry, propertiesMap1);

        Map<String, RecordProperty> propertiesMap2 = Maps.newHashMap();
        propertiesMap2.put("a", new RecordProperty("a", IntType.getInstance(registry), null));
        propertiesMap2.put("b", new RecordProperty("b", StringType.getInstance(registry), null));
        propertiesMap2.put("c", new RecordProperty("c", BooleanType.getInstance(registry), null));

        RecordType recordType2 = new RecordType(registry, propertiesMap2);

        Map<String, RecordProperty> propertiesMap3 = Maps.newHashMap();
        propertiesMap3.put("a", new RecordProperty("a", IntType.getInstance(registry), null));
        propertiesMap3.put("b", new RecordProperty("b", IntType.getInstance(registry), null));

        RecordType recordType3 = new RecordType(registry, propertiesMap3);

        Map<String, RecordProperty> propertiesMap4 = Maps.newHashMap();
        propertiesMap4.put("c", new RecordProperty("c", BooleanType.getInstance(registry), null));

        RecordType recordType4 = new RecordType(registry, propertiesMap4);

        Map<String, RecordProperty> propertiesMap5 = Maps.newHashMap();
        propertiesMap5.put("a", new RecordProperty("a", StringType.getInstance(registry), null));
        propertiesMap5.put("b", new RecordProperty("b", IntType.getInstance(registry), null));

        RecordType recordType5 = new RecordType(registry, propertiesMap5);

        assertTrue(recordType1.isEquivalentTo(recordType1));
        assertTrue(recordType1.isEquivalentTo(recordType2));
        assertFalse(recordType1.isEquivalentTo(recordType3));
        assertFalse(recordType1.isEquivalentTo(recordType4));
        assertFalse(recordType1.isEquivalentTo(recordType5));
    }

    @Test
    public void testGetImplicitPrototype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType = new RecordType(registry, null);
        assertSame(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType = new RecordType(registry, null);

        recordType.defineProperty("a", IntType.getInstance(registry), true, null);
        assertNotNull(recordType.getPropertyType("a"));
        assertNull(recordType.getPropertyNode("a"));

        recordType.defineProperty("a", IntType.getInstance(registry), false, null);
        assertNotNull(recordType.getPropertyType("a"));
        assertNotNull(recordType.getPropertyNode("a"));
    }

    @Test
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propertiesMap1 = Maps.newHashMap();
        propertiesMap1.put("a", new RecordProperty("a", IntType.getInstance(registry), null));
        propertiesMap1.put("b", new RecordProperty("b", StringType.getInstance(registry), null));

        RecordType recordType1 = new RecordType(registry, propertiesMap1);

        Map<String, RecordProperty> propertiesMap2 = Maps.newHashMap();
        propertiesMap2.put("a", new RecordProperty("a", IntType.getInstance(registry), null));
        propertiesMap2.put("b", new RecordProperty("b", StringType.getInstance(registry), null));
        propertiesMap2.put("c", new RecordProperty("c", BooleanType.getInstance(registry), null));

        RecordType recordType2 = new RecordType(registry, propertiesMap2);

        Map<String, RecordProperty> propertiesMap3 = Maps.newHashMap();
        propertiesMap3.put("a", new RecordProperty("a", IntType.getInstance(registry), null));
        propertiesMap3.put("b", new RecordProperty("b", IntType.getInstance(registry), null));

        RecordType recordType3 = new RecordType(registry, propertiesMap3);

        RecordType leastSupertype = recordType1.getLeastSupertype(recordType2);
        assertSame(recordType1, leastSupertype);

        leastSupertype = recordType1.getLeastSupertype(recordType3);
        assertSame(recordType1, leastSupertype);

        leastSupertype = recordType1.getLeastSupertype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
        assertSame(recordType1, leastSupertype);

        leastSupertype = recordType1.getLeastSupertype(registry.getNativeType(JSTypeNative.NO_TYPE));
        assertSame(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), leastSupertype);

        leastSupertype = recordType1.getLeastSupertype(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE));
        assertSame(registry.getNativeType(JSTypeNative.NO_TYPE), leastSupertype);

        leastSupertype = recordType1.getLeastSupertype(registry.getNativeType(JSTypeNative.FUNCTION_TYPE));
        assertSame(registry.getNativeType(JSTypeNative.NO_TYPE), leastSupertype);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propertiesMap1 = Maps.newHashMap();
        propertiesMap1.put("a", new RecordProperty("a", IntType.getInstance(registry), null));
        propertiesMap1.put("b", new RecordProperty("b", StringType.getInstance(registry), null));

        RecordType recordType1 = new RecordType(registry, propertiesMap1);

        Map<String, RecordProperty> propertiesMap2 = Maps.newHashMap();
        propertiesMap2.put("a", new RecordProperty("a", IntType.getInstance(registry), null));
        propertiesMap2.put("b", new RecordProperty("b", StringType.getInstance(registry), null));
        propertiesMap2.put("c", new RecordProperty("c", BooleanType.getInstance(registry), null));

        RecordType recordType2 = new RecordType(registry, propertiesMap2);

        Map<String, RecordProperty> propertiesMap3 = Maps.newHashMap();
        propertiesMap3.put("a", new RecordProperty("a", IntType.getInstance(registry), null));
        propertiesMap3.put("b", new RecordProperty("b", IntType.getInstance(registry), null));

        RecordType recordType3 = new RecordType(registry, propertiesMap3);

        Map<String, RecordProperty> propertiesMap4 = Maps.newHashMap();
        propertiesMap4.put("a", new RecordProperty("a", IntType.getInstance(registry), null));
        propertiesMap4.put("b", new RecordProperty("b", IntType.getInstance(registry), null));
        propertiesMap4.put("c", new RecordProperty("c", IntType.getInstance(registry), null));

        RecordType recordType4 = new RecordType(registry, propertiesMap4);

        RecordType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
        assertSame(recordType2, greatestSubtype);

        greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
        assertSame(recordType3, greatestSubtype);

        greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType4);
        assertSame(recordType4, greatestSubtype);

        greatestSubtype = recordType1.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
        assertSame(recordType1, greatestSubtype);

        greatestSubtype = recordType1.getGreatestSubtypeHelper(registry.getNativeType(JSTypeNative.NO_TYPE));
        assertSame(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);

        greatestSubtype = recordType1.getGreatestSubtypeHelper(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE));
        assertSame(registry.getNativeType(JSTypeNative.NO_TYPE), greatestSubtype);

        greatestSubtype = recordType1.getGreatestSubtypeHelper(registry.getNativeType(JSTypeNative.FUNCTION_TYPE));
        assertSame(registry.getNativeType(JSTypeNative.NO_TYPE), greatestSubtype);
    }
}