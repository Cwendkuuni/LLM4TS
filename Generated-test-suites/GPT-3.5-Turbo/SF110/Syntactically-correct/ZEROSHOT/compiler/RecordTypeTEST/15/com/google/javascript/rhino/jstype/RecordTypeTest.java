package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import java.util.Map;
import java.util.Set;

public class RecordTypeTest {

    @Test
    public void testRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        properties.put("property2", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
        RecordType recordType = new RecordType(registry, properties);
        
        assertNotNull(recordType);
        assertTrue(recordType.isSynthetic());
        assertTrue(recordType.checkRecordEquivalenceHelper(recordType, null));
        assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
        assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.ANY_TYPE)));
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("property1", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        properties1.put("property2", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
        RecordType recordType1 = new RecordType(registry, properties1);
        
        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("property1", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        properties2.put("property2", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
        properties2.put("property3", new RecordProperty(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), new Node()));
        RecordType recordType2 = new RecordType(registry, properties2);
        
        assertTrue(RecordType.isSubtype(recordType1, recordType1));
        assertFalse(RecordType.isSubtype(recordType1, recordType2));
    }
}