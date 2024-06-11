
package com.google.javascript.rhino.jstype;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RecordTypeTest {

    private RecordType emptyRecord;

    private RecordType recordType;

    private JSTypeRegistry registry;

    private Map<String, RecordProperty> properties;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        properties = new HashMap<>();
        emptyRecord = new RecordType(registry, new HashMap<>());
        properties.put("name", new RecordProperty(new JSType(), null));
        properties.put("age", new RecordProperty(new JSType(), null));
        recordType = new RecordType(registry, properties);
    }

    @Test
    public void testIsSynthetic() {
        assertFalse(emptyRecord.isSynthetic());
        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("name", new RecordProperty(new JSType(), null));
        properties1.put("age", new RecordProperty(new JSType(), null));
        RecordType recordType1 = new RecordType(registry, properties1);

        assertTrue(recordType.checkRecordEquivalenceHelper(recordType1, EquivalenceMethod.EQUALITY));
        assertFalse(recordType.checkRecordEquivalenceHelper(emptyRecord, EquivalenceMethod.EQUALITY));
    }

    @Test
    public void testIsSubtype() {
        JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertTrue(objectType.isSubtype(recordType));

        assertFalse(recordType.isSubtype(objectType));
        assertFalse(emptyRecord.isSubtype(recordType));
    }
}

