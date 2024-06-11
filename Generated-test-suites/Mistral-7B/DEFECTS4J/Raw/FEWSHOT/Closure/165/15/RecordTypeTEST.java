package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {
    private static final ErrorReporter ERROR_REPORTER = ErrorReporter.NULL;
    private RecordType recordType;

    @Before
    public void setUp() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("prop1", new RecordProperty("prop1", registry.getStringType()));
        properties.put("prop2", new RecordProperty("prop2", registry.getIntType()));

        recordType = new RecordType(registry, properties);
    }

    @Test
    public void testIsEquivalentTo() {
        RecordType recordType2 = new RecordType(recordType.getTypeRegistry(), properties);
        assertTrue(recordType.isEquivalentTo(recordType2));

        RecordType recordType3 = new RecordType(recordType.getTypeRegistry(), null);
        assertFalse(recordType.isEquivalentTo(recordType3));

        RecordType recordType4 = new RecordType(recordType.getTypeRegistry(), null);
        recordType4.defineProperty("prop1", recordType.getPropertyType("prop1"), true, null);
        assertFalse(recordType.isEquivalentTo(recordType4));

        RecordType recordType5 = new RecordType(recordType.getTypeRegistry(), null);
        recordType5.defineProperty("prop1", recordType.getPropertyType("prop1"), true, null);
        recordType5.defineProperty("prop3", recordType.getTypeRegistry().getIntType(), true, null);
        assertFalse(recordType.isEquivalentTo(recordType5));
    }

    @Test
    public void testGetImplicitPrototype() {
        ObjectType implicitPrototype = recordType.getImplicitPrototype();
        assertTrue(implicitPrototype.isInstanceOf(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    }

    @Test
    public void testDefineProperty() {
        boolean isDefined = recordType.defineProperty("prop3", recordType.getTypeRegistry().getIntType(), false, null);
        assertTrue(isDefined);

        JSType propertyType = recordType.getPropertyType("prop3");
        assertTrue(propertyType.isInstanceOf(registry.getIntType()));

        isDefined = recordType.defineProperty("prop3", recordType.getTypeRegistry().getStringType(), false, null);
        assertFalse(isDefined);

        JSType undefinedProperty = recordType.getPropertyType("prop4");
        assertNull(undefinedProperty);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType recordType2 = new RecordType(recordType.getTypeRegistry(), properties);
        RecordType recordType3 = new RecordType(recordType.getTypeRegistry(), null);
        recordType3.defineProperty("prop1", recordType.getPropertyType("prop1"), true, null);
        recordType3.defineProperty("prop3", recordType.getTypeRegistry().getIntType(), true, null);

        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(recordType2);
        assertTrue(recordType.isEquivalentTo(greatestSubtype));

        greatestSubtype = recordType.getGreatestSubtypeHelper(recordType3);
        RecordType greatestSubtypeRecord = greatestSubtype.toMaybeRecordType();
        assertTrue(greatestSubtypeRecord.hasProperty("prop1"));
        assertTrue(greatestSubtypeRecord.getPropertyType("prop1").isEquivalentTo(recordType.getPropertyType("prop1")));
        assertTrue(greatestSubtypeRecord.hasProperty("prop3"));
        assertTrue(greatestSubtypeRecord.getPropertyType("prop3").isInstanceOf(registry.getIntType()));
    }

    @Test
    public void testIsSubtype() {
        RecordType recordType2 = new RecordType(recordType.getTypeRegistry(), properties);
        RecordType recordType3 = new RecordType(recordType.getTypeRegistry(), null);
        recordType3.defineProperty("prop1", recordType.getPropertyType("prop1"), true, null);
        recordType3.defineProperty("prop3", recordType.getTypeRegistry().getIntType(), true, null);

        assertTrue(recordType.isSubtype(recordType2));
        assertTrue(recordType2.isSubtype(recordType));

        assertFalse(recordType.isSubtype(recordType3));
        assertFalse(recordType3.isSubtype(recordType));

        ObjectType objectType = recordType.getTypeRegistry().getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertTrue(objectType.isSubtype(recordType));
        assertTrue(recordType.isSubtype(objectType));
    }

    @Test
    public void testResolveInternal() {
        JSType propertyType = recordType.getPropertyType("prop1");
        JSType resolvedPropertyType = propertyType.resolve(ERROR_REPORTER, null);
        assertTrue(resolvedPropertyType.isEquivalentTo(recordType.getPropertyType("prop1")));
    }
}