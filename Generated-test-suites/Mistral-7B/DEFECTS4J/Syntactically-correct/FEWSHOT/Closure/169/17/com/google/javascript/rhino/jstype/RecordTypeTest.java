package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

public class RecordTypeTest {

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        // RecordType A
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
        RecordType typeA = new RecordType(registry, propertiesA, true);

        // RecordType B
        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
        propertiesB.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
        RecordType typeB = new RecordType(registry, propertiesB, true);

        // RecordType C
        Map<String, RecordProperty> propertiesC = Maps.newHashMap();
        propertiesC.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
        propertiesC.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE)));
        RecordType typeC = new RecordType(registry, propertiesC, true);

        // Test cases
        assertTrue(typeA.isSubtype(typeA));
        assertTrue(typeA.isSubtype(typeB));
        assertFalse(typeB.isSubtype(typeA));
        assertFalse(typeB.isSubtype(typeC));
        assertFalse(typeC.isSubtype(typeB));
        assertFalse(typeC.isSubtype(typeA));
    }
}