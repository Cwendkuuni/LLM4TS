package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

    @Test
    public void testRecordEquivalenceHelper() {
        RecordType recordType1 = new RecordType(new JSTypeRegistry(), Maps.newHashMap());
        RecordType recordType2 = new RecordType(new JSTypeRegistry(), Maps.newHashMap());
        RecordType recordType3 = new RecordType(new JSTypeRegistry(), Maps.newHashMap());

        recordType1.defineProperty("property1", JSType.STRING, false, null);
        recordType2.defineProperty("property1", JSType.NUMBER, false, null);
        recordType3.defineProperty("property1", JSType.STRING, false, null);

        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType3, false));
        assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));
        assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, true));
        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType3, true));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSType type1 = JSType.OBJECT;
        JSType type2 = new UnionType(new JSTypeRegistry(), Node.NULL_TYPE, JSType.STRING);
        RecordType recordType1 = new RecordType(new JSTypeRegistry(), Maps.newHashMap());
        RecordType recordType2 = new RecordType(new JSTypeRegistry(), Maps.newHashMap());

        recordType1.defineProperty("property1", JSType.STRING, false, null);
        recordType2.defineProperty("property1", JSType.NUMBER, false, null);
        recordType2.defineProperty("property2", JSType.STRING, false, null);

        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(type1);
        assertEquals(type1, greatestSubtype);

        greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
        assertNotNull(greatestSubtype);
        assertTrue(greatestSubtype.isRecordType());

        RecordType greatestRecordSubtype = greatestSubtype.toMaybeRecordType();
        assertEquals(recordType1.properties.size(), greatestRecordSubtype.properties.size());
        for (String property : recordType1.properties.keySet()) {
            JSType typeA = recordType1.getPropertyType(property);
            JSType typeB = greatestRecordSubtype.getPropertyType(property);
            assertEquals(typeA, typeB);
        }

        greatestSubtype = recordType1.getGreatestSubtypeHelper(type2);
        assertNotNull(greatestSubtype);
        assertTrue(type2.isSubtype(greatestSubtype));
    }

    @Test
    public void testIsSubtype() {
        RecordType recordType1 = new RecordType(new JSTypeRegistry(), Maps.newHashMap());
        RecordType recordType2 = new RecordType(new JSTypeRegistry(), Maps.newHashMap());
        RecordType recordType3 = new RecordType(new JSTypeRegistry(), Maps.newHashMap());
        RecordType recordType4 = new RecordType(new JSTypeRegistry(), Maps.newHashMap());

        recordType1.defineProperty("property1", JSType.STRING, false, null);
        recordType2.defineProperty("property1", JSType.NUMBER, false, null);
        recordType3.defineProperty("property1", JSType.STRING, false, null);
        recordType4.defineProperty("property1", JSType.NUMBER, false, null);
        recordType4.defineProperty("property2", JSType.STRING, false, null);

        assertTrue(recordType1.isSubtype(recordType1));
        assertTrue(recordType1.isSubtype(recordType3));
        assertFalse(recordType1.isSubtype(recordType2));
        assertFalse(recordType1.isSubtype(recordType4));
    }

    @Test
    public void testIsSubtypeWithRecordTypeBuilder() {
        RecordType recordType1 = new RecordType(new JSTypeRegistry(), Maps.newHashMap());
        RecordType recordType2 = new RecordType(new JSTypeRegistry(), Maps.newHashMap());

        recordType1.defineProperty("property1", JSType.STRING, false, null);
        recordType2.defineProperty("property1", JSType.NUMBER, false, null);

        RecordProperty recordProperty1 = new RecordProperty("property1", JSType.STRING, null, null);
        RecordProperty recordProperty2 = new RecordProperty("property2", JSType.NUMBER, null, null);

        RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
        recordTypeBuilder.setSynthesized(false);
        recordTypeBuilder.addProperty("property1", JSType.STRING, recordProperty1);
        recordTypeBuilder.addProperty("property2", JSType.NUMBER, recordProperty2);

        RecordType recordType3 = recordTypeBuilder.build();

        assertTrue(recordType1.isSubtype(recordType3));
        assertFalse(recordType3.isSubtype(recordType1));
    }

    @Test
    public void testDefineProperty() {
        RecordType recordType = new RecordType(new JSTypeRegistry(), Maps.newHashMap());
        assertFalse(recordType.defineProperty("property1", JSType.STRING, false, null));
        assertTrue(recordType.hasProperty("property1"));
        assertEquals(JSType.STRING, recordType.getPropertyType("property1"));
    }

    @Test(expected = IllegalStateException.class)
    public void testDefinePropertyWithNullRecordProperty() {
        RecordType recordType = new RecordType(new JSTypeRegistry(), Maps.newHashMap());
        RecordProperty recordProperty = null;
        recordType.defineProperty("property1", JSType.STRING, false, recordProperty);
    }
}