package com.google.javascript.rhino.jstype;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {
    private ErrorReporter errorReporter;
    private JSTypeRegistry registry;

    @Before
    public void setup() {
        errorReporter = new ErrorReporter();
        registry = new JSTypeRegistry(errorReporter);
    }

    @Test
    public void testCreateRecordType() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties.put("b", new RecordProperty("b", registry.getNumberType()));
        RecordType recordType = new RecordType(registry, recordProperties);

        assertEquals(recordType.getClass(), RecordType.class);
        assertEquals(recordType.properties.size(), 2);
        assertEquals(recordType.getPropertyType("a"), registry.getStringType());
        assertEquals(recordType.getPropertyType("b"), registry.getNumberType());
    }

    @Test
    public void testIsEquivalentTo() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties.put("b", new RecordProperty("b", registry.getNumberType()));

        RecordType recordType1 = new RecordType(registry, recordProperties);
        RecordType recordType2 = new RecordType(registry, recordProperties);

        assertTrue(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsNotEquivalentToDifferentProperties() {
        Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
        recordProperties1.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties1.put("b", new RecordProperty("b", registry.getNumberType()));

        Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
        recordProperties2.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties2.put("c", new RecordProperty("c", registry.getNumberType()));

        RecordType recordType1 = new RecordType(registry, recordProperties1);
        RecordType recordType2 = new RecordType(registry, recordProperties2);

        assertFalse(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsNotEquivalentToDifferentTypes() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties.put("b", new RecordProperty("b", registry.getNumberType()));

        RecordType recordType1 = new RecordType(registry, recordProperties);
        JSType otherType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

        assertFalse(recordType1.isEquivalentTo(otherType));
    }

    @Test
    public void testGetImplicitPrototype() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties.put("b", new RecordProperty("b", registry.getNumberType()));

        RecordType recordType = new RecordType(registry, recordProperties);

        assertEquals(recordType.getImplicitPrototype().getClass(), Object.class);
    }

    @Test
    public void testDefineProperty() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties.put("b", new RecordProperty("b", registry.getNumberType()));

        RecordType recordType = new RecordType(registry, recordProperties);

        // Define a property "c" of type number.
        recordType.defineProperty("c", registry.getNumberType(), false, null);

        assertTrue(recordType.hasProperty("c"));
        assertEquals(recordType.getPropertyType("c"), registry.getNumberType());
    }

    @Test(expected = IllegalStateException.class)
    public void testDefinePropertyWithNullRecordProperty() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();

        RecordType recordType = new RecordType(registry, recordProperties);

        recordType.defineProperty("a", null, false, null);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
        recordProperties1.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties1.put("b", new RecordProperty("b", registry.getNumberType()));

        RecordType recordType1 = new RecordType(registry, recordProperties1);

        Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
        recordProperties2.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties2.put("b", new RecordProperty("b", registry.getNumberType()));
        recordProperties2.put("c", new RecordProperty("c", registry.getNumberType()));

        RecordType recordType2 = new RecordType(registry, recordProperties2);

        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

        assertTrue(greatestSubtype.isEquivalentTo(recordType1));
    }

    @Test
    public void testGetGreatestSubtypeHelperWithDifferentProperties() {
        Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
        recordProperties1.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties1.put("b", new RecordProperty("b", registry.getNumberType()));

        RecordType recordType1 = new RecordType(registry, recordProperties1);

        Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
        recordProperties2.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties2.put("c", new RecordProperty("c", registry.getNumberType()));

        RecordType recordType2 = new RecordType(registry, recordProperties2);

        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

        assertTrue(greatestSubtype.isEquivalentTo(registry.getNativeObjectType(JSTypeNative.NO_TYPE)));
    }

    @Test
    public void testGetGreatestSubtypeHelperWithDifferentTypes() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties.put("b", new RecordProperty("b", registry.getNumberType()));

        RecordType recordType = new RecordType(registry, recordProperties);

        JSType otherType = registry.getNativeType(JSTypeNative.OBJECT_TYPE);

        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(otherType);

        assertTrue(greatestSubtype.isEquivalentTo(otherType));
    }

    @Test
    public void testIsSubtype() {
        Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
        recordProperties1.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties1.put("b", new RecordProperty("b", registry.getNumberType()));

        RecordType recordType1 = new RecordType(registry, recordProperties1);

        Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
        recordProperties2.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties2.put("b", new RecordProperty("b", registry.getNumberType()));
        recordProperties2.put("c", new RecordProperty("c", registry.getNumberType()));

        RecordType recordType2 = new RecordType(registry, recordProperties2);

        assertTrue(recordType1.isSubtype(recordType2));

        Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
        recordProperties3.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties3.put("d", new RecordProperty("d", registry.getNumberType()));

        RecordType recordType3 = new RecordType(registry, recordProperties3);

        assertFalse(recordType1.isSubtype(recordType3));
    }

    @Test
    public void testToString() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties.put("b", new RecordProperty("b", registry.getNumberType()));

        RecordType recordType = new RecordType(registry, recordProperties);

        String recordTypeString = recordType.toString();

        String expected = "RecordType { a = String, b = Number }";

        assertThat(recordTypeString).isEqualTo(expected);
    }

    @Test
    public void testResolveInternal() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties.put("b", new RecordProperty("b", registry.getNumberType()));

        RecordType recordType = new RecordType(registry, recordProperties);

        JSType resolvedType = recordType.resolveInternal(errorReporter, null);

        assertTrue(resolvedType instanceof RecordType);
    }
}