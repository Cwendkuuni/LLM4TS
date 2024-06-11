package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

    private JSTypeRegistry registry;
    private RecordTypeBuilder builder;

    @Before
    public void setup() {
        registry = new JSTypeRegistry();
        builder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testIsSynthetic() {
        RecordType recordType = new RecordType(registry, builder.build(), false);
        assertTrue(recordType.isSynthetic());

        recordType = new RecordType(registry, builder.build(), true);
        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        builder.addProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null);
        builder.addProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
        builder.addProperty("c", registry.getNativeType(JSTypeNative.UNDEFINED_TYPE), null);

        RecordType recordType1 = new RecordType(registry, builder.build(), true);
        RecordType recordType2 = new RecordType(registry, builder.build(), true);
        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));

        builder.addProperty("d", registry.getNativeType(JSTypeNative.NULL_TYPE), null);
        recordType2 = new RecordType(registry, builder.build(), true);
        assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));
    }

    @Test
    public void testGetImplicitPrototype() {
        RecordType recordType = new RecordType(registry, builder.build(), true);
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
    }

    @Test
    public void testDefineProperty() {
        RecordType recordType = new RecordType(registry, builder.build(), true);
        assertFalse(recordType.defineProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null));

        assertTrue(recordType.defineProperty("d", registry.getNativeType(JSTypeNative.UNDEFINED_TYPE), false, null));
        assertEquals(registry.getNativeType(JSTypeNative.UNDEFINED_TYPE), recordType.getPropertyType("d"));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        builder.addProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null);
        RecordType recordType1 = new RecordType(registry, builder.build(), true);

        builder.addProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
        RecordType recordType2 = new RecordType(registry, builder.build(), true);
        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
        assertTrue(greatestSubtype.isRecordType());
        assertEquals(recordType2, greatestSubtype.toMaybeRecordType());
    }

    @Test
    public void testIsSubtype() {
        builder.addProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null);
        RecordType recordType1 = new RecordType(registry, builder.build(), true);

        builder.addProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
        RecordType recordType2 = new RecordType(registry, builder.build(), true);
        assertTrue(recordType1.isSubtype(recordType2));

        builder.addProperty("c", registry.getNativeType(JSTypeNative.OBJECT_TYPE), null);
        RecordType recordType3 = new RecordType(registry, builder.build(), true);
        assertFalse(recordType1.isSubtype(recordType3));
    }

    @Test
    public void testIsSubtypeWithObjectType() {
        builder.addProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null);

        RecordType recordType = new RecordType(registry, builder.build(), true);
        assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    }

    @Test
    public void testIsSubtypeWithNonRecordType() {
        builder.addProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null);

        RecordType recordType = new RecordType(registry, builder.build(), true);
        assertFalse(recordType.isSubtype(registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
    }
    
    @Test
    public void testResolveInternal() {
        builder.addProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null);
        RecordType recordType = new RecordType(registry, builder.build(), true);

        ErrorReporter errorReporter = new ErrorReporter(registry);
        StaticScope<JSType> scope = new StaticScope<>();
        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);

        assertTrue(resolvedType.isRecordType());
        assertEquals(recordType, resolvedType.toMaybeRecordType());
    }
    
}