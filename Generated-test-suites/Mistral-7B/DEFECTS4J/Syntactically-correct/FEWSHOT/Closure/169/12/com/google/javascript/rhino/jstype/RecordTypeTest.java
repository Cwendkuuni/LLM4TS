package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties.put("b", new RecordProperty("b", registry.getIntType()));

        RecordType recordType1 = new RecordType(registry, recordProperties, true);
        RecordType recordType2 = new RecordType(registry, recordProperties, true);

        assertTrue(recordType1.isSubtype(recordType2));
        assertTrue(recordType2.isSubtype(recordType1));

        RecordType recordType3 = new RecordType(registry, recordProperties, true);
        recordType3.properties.put("c", registry.getIntType());

        assertFalse(recordType1.isSubtype(recordType3));
        assertFalse(recordType3.isSubtype(recordType1));

        RecordType recordType4 = new RecordType(registry, recordProperties, false);
        recordType4.properties.put("d", registry.getIntType());

        assertFalse(recordType1.isSubtype(recordType4));
        assertFalse(recordType4.isSubtype(recordType1));

        RecordType recordType5 = new RecordType(registry, recordProperties, true);
        JSType recordType5PropertyType = recordType5.getPropertyType("b");
        recordType5.setPropertyType("b", recordType5PropertyType.getLeastSupertype(registry.getNativeType(JSTypeNative.NUMBER_TYPE)));

        assertTrue(recordType1.isSubtype(recordType5));
        assertTrue(recordType5.isSubtype(recordType1));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
        recordProperties1.put("a", new RecordProperty("a", registry.getStringType()));

        RecordType recordType1 = new RecordType(registry, recordProperties1, true);

        Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
        recordProperties2.put("a", new RecordProperty("a", registry.getIntType()));
        recordProperties2.put("b", new RecordProperty("b", registry.getIntType()));

        RecordType recordType2 = new RecordType(registry, recordProperties2, true);

        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
        assertTrue(registry.getNativeType(JSTypeNative.OBJECT_TYPE).isEquivalentTo(greatestSubtype));

        Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
        recordProperties3.put("a", new RecordProperty("a", registry.getIntType()));
        recordProperties3.put("b", new RecordProperty("b", registry.getStringType()));

        RecordType recordType3 = new RecordType(registry, recordProperties3, true);

        greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
        assertTrue(recordType3.isSubtype(greatestSubtype));

        Map<String, RecordProperty> recordProperties4 = Maps.newHashMap();
        recordProperties4.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties4.put("c", new RecordProperty("c", registry.getIntType()));

        RecordType recordType4 = new RecordType(registry, recordProperties4, true);

        greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType4);
        assertTrue(registry.getNativeType(JSTypeNative.OBJECT_TYPE).isEquivalentTo(greatestSubtype));

        Map<String, RecordProperty> recordProperties5 = Maps.newHashMap();
        recordProperties5.put("a", new RecordProperty("a", registry.getRecordType(recordProperties1)));

        RecordType recordType5 = new RecordType(registry, recordProperties5, true);

        greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType5);
        assertTrue(recordType1.isSubtype(greatestSubtype));
    }
}