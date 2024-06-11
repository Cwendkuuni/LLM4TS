package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {
    private JSTypeRegistry registry;
    private ErrorReporter t;

    @Before
    public void setup() {
        registry = new JSTypeRegistry();
        t = new ErrorReporter() {
            @Override
            public void error(int line, int pos, String message) {
            }

            @Override
            public void warning(int line, int pos, String message) {
            }
        };
    }

    @Test
    public void testCreateRecordType() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("x", new RecordProperty("x", registry.getNumberType()));
        recordProperties.put("y", new RecordProperty("y", registry.getNumberType()));
        RecordType recordType = new RecordType(registry, recordProperties, true);

        assertTrue(recordType.isRecordType());
        assertEquals(2, recordType.getPropertyCount());

        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
                recordType.getImplicitPrototype());
    }

    @Test
    public void testIsSubtypeRecordType() {
        Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
        recordProperties1.put("x", new RecordProperty("x", registry.getNumberType()));
        RecordType recordType1 = new RecordType(registry, recordProperties1, true);

        Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
        recordProperties2.put("x", new RecordProperty("x", registry.getNumberType()));
        recordProperties2.put("y", new RecordProperty("y", registry.getNumberType()));
        RecordType recordType2 = new RecordType(registry, recordProperties2, true);

        assertTrue(recordType1.isSubtype(recordType2));
        assertTrue(recordType2.isSubtype(recordType1));

        Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
        recordProperties3.put("y", new RecordProperty("y", registry.getNumberType()));
        RecordType recordType3 = new RecordType(registry, recordProperties3, true);

        assertFalse(recordType1.isSubtype(recordType3));
        assertFalse(recordType3.isSubtype(recordType1));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
        recordProperties1.put("x", new RecordProperty("x", registry.getNumberType()));
        RecordType recordType1 = new RecordType(registry, recordProperties1, true);

        Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
        recordProperties2.put("x", new RecordProperty("x", registry.getBooleanType()));
        recordProperties2.put("y", new RecordProperty("y", registry.getNumberType()));
        RecordType recordType2 = new RecordType(registry, recordProperties2, true);

        UnionTypeBuilder builder = new UnionTypeBuilder(registry);
        builder.addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
        builder.addAlternate(recordType1);
        JSType greatestSubtype1 = builder.build();

        JSType greatestSubtype2 = recordType1.getGreatestSubtypeHelper(recordType2, false);
        assertTrue(greatestSubtype1.isEquivalentTo(greatestSubtype2));

        Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
        recordProperties3.put("z", new RecordProperty("z", registry.getNumberType()));
        RecordType recordType3 = new RecordType(registry, recordProperties3, true);

        UnionTypeBuilder builder2 = new UnionTypeBuilder(registry);
        builder2.addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
        builder2.addAlternate(recordType2);
        builder2.addAlternate(recordType3);
        JSType greatestSubtype3 = builder2.build();

        JSType greatestSubtype4 = recordType2.getGreatestSubtypeHelper(recordType3, false);
        assertTrue(greatestSubtype3.isSubtype(greatestSubtype4));

        JSType greatestSubtype5 = recordType1.getGreatestSubtypeHelper(recordType3, false);
        assertTrue(greatestSubtype3.isSubtype(greatestSubtype5));
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
        recordProperties1.put("x", new RecordProperty("x", registry.getNumberType()));
        RecordType recordType1 = new RecordType(registry, recordProperties1, true);

        Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
        recordProperties2.put("x", new RecordProperty("x", registry.getNumberType()));
        recordProperties2.put("y", new RecordProperty("y", registry.getNumberType()));
        RecordType recordType2 = new RecordType(registry, recordProperties2, true);

        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
        assertTrue(recordType2.checkRecordEquivalenceHelper(recordType1, false));

        Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
        recordProperties3.put("x", new RecordProperty("x", registry.getNumberType()));
        recordProperties3.put("y", new RecordProperty("y", registry.getBooleanType()));
        RecordType recordType3 = new RecordType(registry, recordProperties3, true);

        assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, false));
        assertFalse(recordType3.checkRecordEquivalenceHelper(recordType1, false));

        Map<String, RecordProperty> recordProperties4 = Maps.newHashMap();
        recordProperties4.put("z", new RecordProperty("z", registry.getNumberType()));
        RecordType recordType4 = new RecordType(registry, recordProperties4, true);

        assertFalse(recordType1.checkRecordEquivalenceHelper(recordType4, false));
        assertFalse(recordType4.checkRecordEquivalenceHelper(recordType1, false));
    }

    @Test
    public void testIsSynthetic() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("x", new RecordProperty("x", registry.getNumberType()));
        RecordType recordType = new RecordType(registry, recordProperties, false);
        assertTrue(recordType.isSynthetic());

        Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
        recordProperties2.put("x", new RecordProperty("x", registry.getNumberType()));
        recordProperties2.put("y", new RecordProperty("y", registry.getNumberType()));
        RecordType recordType2 = new RecordType(registry, recordProperties2, true);
        assertFalse(recordType2.isSynthetic());
    }

    @Test
    public void testGetPropertyType() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("x", new RecordProperty("x", registry.getNumberType()));
        RecordType recordType = new RecordType(registry, recordProperties, true);

        assertEquals(registry.getNumberType(), recordType.getPropertyType("x"));
        assertNull(recordType.getPropertyType("y"));
    }

    @Test
    public void testHasProperty() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("x", new RecordProperty("x", registry.getNumberType()));
        RecordType recordType = new RecordType(registry, recordProperties, true);

        assertTrue(recordType.hasProperty("x"));
        assertFalse(recordType.hasProperty("y"));
    }

    @Test
    public void testDefineProperty() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("x", new RecordProperty("x", registry.getNumberType()));
        RecordType recordType = new RecordType(registry, recordProperties, true);

        assertNull(recordType.getPropertyNode("x"));

        Node propertyNode = new Node_Property();
        recordType.defineProperty("x", registry.getNumberType(), false, propertyNode);

        assertNotNull(recordType.getPropertyNode("x"));
    }

    @Test(expected = IllegalStateException.class)
    public void testDefinePropertyWithNullRecordProperty() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        RecordType recordType = new RecordType(registry, recordProperties, true);

        recordType.defineProperty("x", registry.getNumberType(), false, null);
    }

    @Test
    public void testResolveInternal() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("x", new RecordProperty("x", registry.getNumberType()));
        RecordType recordType = new RecordType(registry, recordProperties, true);

        JSType resolvedType = recordType.resolveInternal(t, null);
        assertEquals(recordType, resolvedType);
    }
}