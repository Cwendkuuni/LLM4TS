package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import java.util.HashMap;
import java.util.Map;
import com.google.javascript.rhino.Node;

public class RecordTypeTest {
    @Mock
    JSTypeRegistry registry;

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType record1 = createRecordType();
        RecordType record2 = createRecordType();
        assertTrue(record1.checkRecordEquivalenceHelper(record2, EquivalenceMethod.Strict));
    }

    @Test
    public void testCheckRecordEquivalenceHelper_WhenDifferentPropertySet() {
        RecordType record1 = createRecordType();
        RecordType record2 = new RecordType(registry, new HashMap<>(), true);
        assertFalse(record1.checkRecordEquivalenceHelper(record2, EquivalenceMethod.Strict));
    }

    @Test
    public void testCheckRecordEquivalenceHelper_WhenDifferentPropertyType() {
        RecordType record1 = createRecordType();
        RecordType record2 = createRecordType();
        record2.defineProperty("prop1", registry.getNativeType("int"), true, new Node());
        assertFalse(record1.checkRecordEquivalenceHelper(record2, EquivalenceMethod.Strict));
    }
    
    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType record1 = createRecordType();
        JSType that = registry.getNativeType("int");
        JSType result = record1.getGreatestSubtypeHelper(that);
        assertTrue(result.isEquivalentTo(registry.getNativeObjectType(JSTypeNative.NO_TYPE)));
    }

    @Test
    public void testToMaybeRecordType_WhenObjectIsRecord() {
        RecordType record1 = createRecordType();
        RecordType record2 = record1.toMaybeRecordType();
        assertTrue(record1 == record2);
    }

    @Test
    public void testIsSubtype_WhenRecordSubtypeOfObject() {
        RecordType record1 = createRecordType();
        JSType type = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertTrue(record1.isSubtype(type));
    }

    @Test
    public void testIsSubtype_WhenRecordSubtypeOfRecord() {
        RecordType record1 = createRecordType();
        RecordType record2 = createRecordType();
        assertTrue(RecordType.isSubtype(record1, record2));
    }

    @Test
    public void testIsSubtype_WhenRecordNotSubtypeOfRecord() {
        RecordType record1 = createRecordType();
        RecordType record2 = new RecordType(registry, new HashMap<String, RecordProperty>(), true);
        assertFalse(RecordType.isSubtype(record1, record2));
    }

    private RecordType createRecordType() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("prop1", new RecordProperty(registry.getNativeType("string"), new Node()));
        properties.put("prop2", new RecordProperty(registry.getNativeType("boolean"), new Node()));
        return new RecordType(registry, properties, true);
    }
}