package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

    private ErrorReporter errorReporter;
    private JSTypeRegistry registry;
    private RecordType recordType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        errorReporter = mock(ErrorReporter.class);
        registry = new JSTypeRegistry(new ErrorReporter() {
            @Override
            public void error(String message, Object... args) {
                errorReporter.error(message, args);
            }
        });
        recordType = new RecordType(registry, createRecordPropertiesMap());
    }

    @Test
    public void testIsEquivalentToSameRecordType() {
        RecordType recordType1 = recordType;
        assertTrue(recordType.isEquivalentTo(recordType1));
    }

    @Test
    public void testIsEquivalentToDifferentRecordTypeWithSameProperties() {
        RecordType recordType1 = createRecordTypeWithSameProperties();
        assertTrue(recordType.isEquivalentTo(recordType1));
    }

    @Test
    public void testIsEquivalentToDifferentRecordTypeWithDifferentProperties() {
        RecordType recordType1 = createRecordTypeWithDifferentProperties();
        assertFalse(recordType.isEquivalentTo(recordType1));
    }

    @Test
    public void testIsSubtypeOfSelf() {
        assertTrue(recordType.isSubtype(recordType));
    }

    @Test
    public void testIsSubtypeOfEmptyObjectType() {
        assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    }

    @Test
    public void testIsSubtypeOfDifferentRecordTypeWithSameProperties() {
        RecordType recordType1 = createRecordTypeWithSameProperties();
        assertTrue(recordType.isSubtype(recordType1));
    }

    @Test
    public void testIsSubtypeOfDifferentRecordTypeWithDifferentProperties() {
        RecordType recordType1 = createRecordTypeWithDifferentProperties();
        assertFalse(recordType.isSubtype(recordType1));
    }

    @Test
    public void testIsSubtypeOfNonRecordType() {
        JSType intType = registry.getIntType();
        assertFalse(recordType.isSubtype(intType));
    }

    @Test
    public void testGetGreatestSubtypeHelperEmptyRecordType() {
        RecordType emptyRecordType = new RecordType(registry, Maps.newHashMap());
        JSType result = emptyRecordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
    }

    @Test
    public void testGetGreatestSubtypeHelperRecordTypeWithEmptyProperties() {
        RecordType recordType = createRecordTypeWithEmptyProperties();
        JSType result = recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
    }

    @Test
    public void testGetGreatestSubtypeHelperRecordTypeWithSingleProperty() {
        RecordType recordType = createRecordTypeWithSingleProperty();
        JSType result = recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
    }

    @Test
    public void testGetGreatestSubtypeHelperRecordTypeWithMultipleProperties() {
        RecordType recordType = createRecordTypeWithMultipleProperties();
        JSType result = recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
        JSType expected = new RecordType(registry, createRecordPropertiesMap());
        assertEquals(expected, result);
    }

    @Test
    public void testGetGreatestSubtypeHelperRecordTypeWithConflictingProperties() {
        RecordType recordType = createRecordTypeWithConflictingProperties();
        JSType result = recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), result);
    }

    @Test
    public void testGetLeastSupertypeEmptyRecordType() {
        RecordType emptyRecordType = new RecordType(registry, Maps.newHashMap());
        JSType result = emptyRecordType.getLeastSupertype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
    }

    @Test
    public void testGetLeastSupertypeRecordTypeWithEmptyProperties() {
        RecordType recordType = createRecordTypeWithEmptyProperties();
        JSType result = recordType.getLeastSupertype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
    }

    @Test
    public void testGetLeastSupertypeRecordTypeWithSingleProperty() {
        RecordType recordType = createRecordTypeWithSingleProperty();
        JSType result = recordType.getLeastSupertype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
        JSType expected = recordType;
        assertEquals(expected, result);
    }

    @Test
    public void testGetLeastSupertypeRecordTypeWithMultipleProperties() {
        RecordType recordType = createRecordTypeWithMultipleProperties();
        JSType result = recordType.getLeastSupertype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
        JSType expected = recordType;
        assertEquals(expected, result);
    }

    @Test
    public void testGetLeastSupertypeRecordTypeWithConflictingProperties() {
        RecordType recordType = createRecordTypeWithConflictingProperties();
        JSType result = recordType.getLeastSupertype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
        JSType expected = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertEquals(expected, result);
    }

    @Test
    public void testDefinePropertyFrozenRecordType() {
        recordType.freeze();
        assertFalse(recordType.defineProperty("test", registry.getStringType(), false, null));
    }

    @Test
    public void testDefinePropertyUnfrozenRecordType() {
        assertFalse(recordType.defineProperty("test", registry.getStringType(), false, null));

        recordType.unfreeze();

        assertFalse(recordType.defineProperty("test", registry.getStringType(), false, null));

        recordType.freeze();

        JSType propertyType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        Node propertyNode = mock(Node.class);

        assertTrue(recordType.defineProperty("test", propertyType, false, propertyNode));
    }

    private Map<String, RecordProperty> createRecordPropertiesMap() {
        RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
        RecordProperty prop2 = new RecordProperty("prop2", registry.getIntType());
        return Maps.newHashMap(prop1, prop2);
    }

    private RecordType createRecordTypeWithEmptyProperties() {
        return new RecordType(registry, Maps.newHashMap());
    }

    private RecordType createRecordTypeWithSingleProperty() {
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("prop", new RecordProperty("prop", registry.getStringType()));
        return new RecordType(registry, properties);
    }

    private RecordType createRecordTypeWithMultipleProperties() {
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("prop1", new RecordProperty("prop1", registry.getStringType()));
        properties.put("prop2", new RecordProperty("prop2", registry.getIntType()));
        return new RecordType(registry, properties);
    }

    private RecordType createRecordTypeWithConflictingProperties() {
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("prop1", new RecordProperty("prop1", registry.getStringType()));
        properties.put("prop1", new RecordProperty("prop1", registry.getIntType()));
        return new RecordType(registry, properties);
    }

    private RecordType createRecordTypeWithSameProperties() {
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("prop1", new RecordProperty("prop1", registry.getStringType()));
        properties.put("prop2", new RecordProperty("prop2", registry.getIntType()));
        return new RecordType(registry, properties);
    }
}