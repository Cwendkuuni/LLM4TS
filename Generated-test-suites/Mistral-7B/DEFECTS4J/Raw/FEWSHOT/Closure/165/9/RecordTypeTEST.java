package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.nativetypes.JSTypeNative;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

    private ErrorReporter errorReporter;
    private JSTypeRegistry registry;

    @Rule
    public void initMocks() {
        errorReporter = mock(ErrorReporter.class);
        registry = new JSTypeRegistry(errorReporter);
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void setUp() {
        // Initialize the registry with some basic types
        registry.putType(JSTypeNative.OBJECT_TYPE, new ObjectType(registry));
        registry.putType(JSTypeNative.STRING_TYPE, new StringType(registry));
        registry.putType(JSTypeNative.NUMBER_TYPE, new NumberType(registry));
        registry.putType(JSTypeNative.BOOLEAN_TYPE, new BooleanType(registry));
        registry.putType(JSTypeNative.NULL_TYPE, new NullType(registry));
    }

    @Test
    public void testCreateRecordType() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties.put("b", new RecordProperty("b", registry.getNumberType()));

        RecordType recordType = new RecordType(registry, recordProperties);

        // Check that the record type was constructed correctly
        assertEquals(recordProperties.size(), recordType.getPropertyCount());
        assertEquals("Record", recordType.getName());
        assertEquals(recordProperties, recordType.getProperties());

        // Test that the record type can be frozen
        recordType.freeze();
        assertFalse(recordType.isFrozen());
        recordType.freeze();
        assertTrue(recordType.isFrozen());
    }

    @Test
    public void testIsEquivalentTo() {
        Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
        recordProperties1.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties1.put("b", new RecordProperty("b", registry.getNumberType()));

        Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
        recordProperties2.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties2.put("b", new RecordProperty("b", registry.getNumberType()));
        recordProperties2.put("c", new RecordProperty("c", registry.getBooleanType()));

        RecordType recordType1 = new RecordType(registry, recordProperties1);
        RecordType recordType2 = new RecordType(registry, recordProperties2);

        assertFalse(recordType1.isEquivalentTo(recordType2));

        // Record types with the same properties and types are equivalent
        RecordType recordType3 = new RecordType(registry, recordProperties1);
        assertTrue(recordType1.isEquivalentTo(recordType3));

        // A record type is a subtype of itself
        assertTrue(recordType1.isEquivalentTo(recordType1));
    }

    @Test
    public void testGetImplicitPrototype() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("a", new RecordProperty("a", registry.getStringType()));

        RecordType recordType = new RecordType(registry, recordProperties);

        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
                recordType.getImplicitPrototype());
    }

    @Test
    public void testDefineProperty() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("a", new RecordProperty("a", registry.getStringType()));

        RecordType recordType = new RecordType(registry, recordProperties);

        // Define a new property
        recordType.defineProperty("c", registry.getBooleanType(), false, null);

        assertEquals(3, recordType.getPropertyCount());
        assertTrue(recordType.hasProperty("a"));
        assertTrue(recordType.hasProperty("b"));
        assertTrue(recordType.hasProperty("c"));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        // Test with a record type and another record type with the same properties
        Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
        recordProperties1.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties1.put("b", new RecordProperty("b", registry.getNumberType()));

        Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
        recordProperties2.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties2.put("b", new RecordProperty("b", registry.getNumberType()));

        RecordType recordType1 = new RecordType(registry, recordProperties1);
        RecordType recordType2 = new RecordType(registry, recordProperties2);

        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
        assertEquals(recordType1, greatestSubtype);

        // Test with a record type and a non-record type
        Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
        recordProperties3.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties3.put("b", new RecordProperty("b", registry.getNumberType()));

        RecordType recordType3 = new RecordType(registry, recordProperties3);
        JSType otherType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);

        JSType greatestSubtype2 = recordType1.getGreatestSubtypeHelper(otherType);
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype2);

        // Test with two record types with different properties
        Map<String, RecordProperty> recordProperties4 = Maps.newHashMap();
        recordProperties4.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties4.put("b", new RecordProperty("b", registry.getNumberType()));

        Map<String, RecordProperty> recordProperties5 = Maps.newHashMap();
        recordProperties5.put("c", new RecordProperty("c", registry.getBooleanType()));
        recordProperties5.put("d", new RecordProperty("d", registry.getStringType()));

        RecordType recordType4 = new RecordType(registry, recordProperties4);
        RecordType recordType5 = new RecordType(registry, recordProperties5);

        JSType greatestSubtype3 = recordType1.getGreatestSubtypeHelper(recordType5);
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype3);
    }

    @Test
    public void testIsSubtype() {
        // Test with a record type and a record type with the same properties
        Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
        recordProperties1.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties1.put("b", new RecordProperty("b", registry.getNumberType()));

        Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
        recordProperties2.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties2.put("b", new RecordProperty("b", registry.getNumberType()));

        RecordType recordType1 = new RecordType(registry, recordProperties1);
        RecordType recordType2 = new RecordType(registry, recordProperties2);

        assertTrue(recordType1.isSubtype(recordType2));
        assertTrue(recordType2.isSubtype(recordType1));

        // Test with a record type and a record type with different properties
        Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
        recordProperties3.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties3.put("b", new RecordProperty("b", registry.getNumberType()));

        Map<String, RecordProperty> recordProperties4 = Maps.newHashMap();
        recordProperties4.put("c", new RecordProperty("c", registry.getBooleanType()));
        recordProperties4.put("d", new RecordProperty("d", registry.getStringType()));

        RecordType recordType3 = new RecordType(registry, recordProperties3);
        RecordType recordType4 = new RecordType(registry, recordProperties4);

        assertFalse(recordType3.isSubtype(recordType4));
        assertFalse(recordType4.isSubtype(recordType3));

        // Test with a record type and a non-record type
        Map<String, RecordProperty> recordProperties5 = Maps.newHashMap();
        recordProperties5.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties5.put("b", new RecordProperty("b", registry.getNumberType()));

        RecordType recordType5 = new RecordType(registry, recordProperties5);
        JSType otherType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);

        assertFalse(recordType5.isSubtype(otherType));
        assertFalse(otherType.isSubtype(recordType5));
    }

    @Test
    public void testResolveInternal() {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("a", new RecordProperty("a", registry.getStringType()));
        recordProperties.put("b", new RecordProperty("b", registry.getNumberType()));

        RecordType recordType = new RecordType(registry, recordProperties);
        JSType typeA = registry.getNativeType(JSTypeNative.STRING_TYPE);
        JSType typeB = registry.getNativeType(JSTypeNative.NUMBER_TYPE);

        when(registry.getTypeByName("String")).thenReturn(typeA);
        when(registry.getTypeByName("Number")).thenReturn(typeB);

        recordType.resolveInternal(errorReporter, null);

        verify(registry, times(2)).getTypeByName(anyString());
        verifyNoMoreInteractions(registry);
    }
}