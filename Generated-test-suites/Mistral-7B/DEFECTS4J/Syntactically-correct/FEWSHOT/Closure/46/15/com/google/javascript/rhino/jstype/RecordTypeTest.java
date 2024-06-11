package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

    @Test
    public void testIsEquivalentTo() {
        // Test equivalent record types
        Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
        recordProperties1.put("a", new RecordProperty("a", String.class));
        RecordType recordType1 = new RecordType(JSTypeRegistry.EMPTY, recordProperties1);

        Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
        recordProperties2.put("a", new RecordProperty("a", String.class));
        RecordType recordType2 = new RecordType(JSTypeRegistry.EMPTY, recordProperties2);

        assertTrue(recordType1.isEquivalentTo(recordType2));

        // Test non-equivalent record types
        recordProperties1.put("b", new RecordProperty("b", Integer.class));
        recordType1 = new RecordType(JSTypeRegistry.EMPTY, recordProperties1);

        assertFalse(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsSubtype() {
        // Test subtype relationship between record types

        // Record type A is a subtype of record type B
        Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
        recordPropertiesA.put("a", new RecordProperty("a", String.class));
        RecordType recordTypeA = new RecordType(JSTypeRegistry.EMPTY, recordPropertiesA);

        Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
        recordPropertiesB.put("a", new RecordProperty("a", String.class));
        recordPropertiesB.put("b", new RecordProperty("b", Integer.class));
        RecordType recordTypeB = new RecordType(JSTypeRegistry.EMPTY, recordPropertiesB);

        assertTrue(recordTypeA.isSubtype(recordTypeB));

        // Record type A is not a subtype of record type C
        Map<String, RecordProperty> recordPropertiesC = Maps.newHashMap();
        recordPropertiesC.put("c", new RecordProperty("c", Boolean.class));
        RecordType recordTypeC = new RecordType(JSTypeRegistry.EMPTY, recordPropertiesC);

        assertFalse(recordTypeA.isSubtype(recordTypeC));

        // Test record type as a supertype of itself
        assertTrue(recordTypeA.isSubtype(recordTypeA));
    }

    @Test
    public void testDefineProperty() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        RecordType recordType = new RecordType(JSTypeRegistry.EMPTY, recordProperties);

        assertFalse(recordType.defineProperty("property", String.class, false, null));
        recordProperties.put("property", new RecordProperty("property", String.class));
        assertTrue(recordType.defineProperty("property", String.class, false, null));
    }

    @Test(expected = IllegalStateException.class)
    public void testDefinePropertyWithNullProperty() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        RecordType recordType = new RecordType(JSTypeRegistry.EMPTY, recordProperties);

        recordType.defineProperty("property", String.class, false, null);
        recordProperties.put(null, new RecordProperty(null, String.class));
        recordType.defineProperty(null, String.class, false, null);
    }

    @Test
    public void testGetLeastSupertype() {
        Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
        recordPropertiesA.put("a", new RecordProperty("a", String.class));
        RecordType recordTypeA = new RecordType(JSTypeRegistry.EMPTY, recordPropertiesA);

        Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
        recordPropertiesB.put("a", new RecordProperty("a", String.class));
        recordPropertiesB.put("b", new RecordProperty("b", Integer.class));
        RecordType recordTypeB = new RecordType(JSTypeRegistry.EMPTY, recordPropertiesB);

        RecordType leastSupertype = recordTypeA.getLeastSupertype(recordTypeB);
        assertTrue(leastSupertype.isEquivalentTo(recordTypeB));

        Map<String, RecordProperty> recordPropertiesC = Maps.newHashMap();
        recordPropertiesC.put("c", new RecordProperty("c", Boolean.class));
        RecordType recordTypeC = new RecordType(JSTypeRegistry.EMPTY, recordPropertiesC);

        RecordType leastSupertype2 = recordTypeA.getLeastSupertype(recordTypeC);
        assertTrue(leastSupertype2.isEquivalentTo(JSTypeRegistry.EMPTY));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
        recordPropertiesA.put("a", new RecordProperty("a", String.class));
        RecordType recordTypeA = new RecordType(JSTypeRegistry.EMPTY, recordPropertiesA);

        Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
        recordPropertiesB.put("a", new RecordProperty("a", String.class));
        recordPropertiesB.put("b", new RecordProperty("b", Integer.class));
        RecordType recordTypeB = new RecordType(JSTypeRegistry.EMPTY, recordPropertiesB);

        RecordType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
        assertTrue(greatestSubtype.isEquivalentTo(recordTypeB));

        Map<String, RecordProperty> recordPropertiesC = Maps.newHashMap();
        recordPropertiesC.put("c", new RecordProperty("c", Boolean.class));
        RecordType recordTypeC = new RecordType(JSTypeRegistry.EMPTY, recordPropertiesC);

        RecordType greatestSubtype2 = recordTypeA.getGreatestSubtypeHelper(recordTypeC);
        assertTrue(greatestSubtype2.isEquivalentTo(JSTypeRegistry.EMPTY));

        Map<String, RecordProperty> recordPropertiesD = Maps.newHashMap();
        recordPropertiesD.put("a", new RecordProperty("a", String.class));
        RecordType recordTypeD = new RecordType(JSTypeRegistry.EMPTY, recordPropertiesD);

        Map<String, RecordProperty> recordPropertiesE = Maps.newHashMap();
        recordPropertiesE.put("a", new RecordProperty("a", String.class));
        recordPropertiesE.put("e", new RecordProperty("e", Integer.class));
        RecordType recordTypeE = new RecordType(JSTypeRegistry.EMPTY, recordPropertiesE);

        RecordType greatestSubtype3 = recordTypeA.getGreatestSubtypeHelper(recordTypeE);
        assertTrue(greatestSubtype3.isEquivalentTo(recordTypeE));

        Map<String, RecordProperty> recordPropertiesF = Maps.newHashMap();
        recordPropertiesF.put("a", new RecordProperty("a", String.class));
        recordPropertiesF.put("f", new RecordProperty("f", Double.class));
        RecordType recordTypeF = new RecordType(JSTypeRegistry.EMPTY, recordPropertiesF);

        UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(JSTypeRegistry.EMPTY);
        ObjectType altType1 = new ObjectType(JSTypeRegistry.EMPTY, null);
        altType1.defineProperty("a", String.class, false, null);
        unionTypeBuilder.addAlternate(altType1);

        ObjectType altType2 = new ObjectType(JSTypeRegistry.EMPTY, null);
        altType2.defineProperty("a", String.class, false, null);
        altType2.defineProperty("f", Double.class, false, null);
        unionTypeBuilder.addAlternate(altType2);

        RecordType greatestSubtype4 = recordTypeA.getGreatestSubtypeHelper(unionTypeBuilder.build());
        RecordType expectedRecordType = new RecordType(JSTypeRegistry.EMPTY, Maps.newHashMap());
        expectedRecordType.defineProperty("a", String.class, false, null);
        expectedRecordType.defineProperty("f", Double.class, false, null);
        assertTrue(greatestSubtype4.isEquivalentTo(expectedRecordType));
    }
}