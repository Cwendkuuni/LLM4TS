package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import org.junit.Test;

public class RecordTypeTest {
    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties1.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties2.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties1.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties2.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.isSubtype(recordType2));
    }
}