
package com.google.javascript.rhino.jstype;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeTest {

    private JSTypeRegistry registry;

    @Before
    public void setup() {
        registry = new JSTypeRegistry();
    }

    @Test
    public void testDefineProperty() {
        RecordType recordType = createRecordType();
        assertFalse(recordType.defineProperty("property", registry.getNativeType(JSTypeNative.NO_TYPE), false, null));
        assertTrue(recordType.defineProperty("newProperty", registry.getNativeType(JSTypeNative.NUMBER_TYPE), true, null));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType recordType1 = createRecordType();
        RecordType recordType2 = createRecordType();

        assertTrue(recordType1.getGreatestSubtypeHelper(recordType2).isEquivalentTo(recordType1));

        recordType2.defineProperty("newProperty", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);

        assertFalse(recordType1.getGreatestSubtypeHelper(recordType2).isEquivalentTo(recordType1));
    }

    @Test
    public void testIsSubtype() {
        RecordType recordType1 = createRecordType();
        RecordType recordType2 = createRecordType();

        assertTrue(recordType1.isSubtype(registry.getNativeType(JSTypeNative.OBJECT_TYPE)));

        assertTrue(recordType1.isSubtype(recordType2));

        recordType2.defineProperty("newProperty", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);

        assertFalse(recordType1.isSubtype(recordType2));
    }

    private RecordType createRecordType() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties.put("property2", new RecordProperty(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null));

        return new RecordType(registry, properties);
    }
}
