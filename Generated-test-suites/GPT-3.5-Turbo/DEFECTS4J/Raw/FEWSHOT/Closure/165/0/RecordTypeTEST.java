
package com.google.javascript.rhino.jstype;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import static org.junit.Assert.*;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class RecordTypeTest {
    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newTreeMap();
        properties1.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    
        Map<String, RecordProperty> properties2 = Maps.newTreeMap();
        properties2.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    
        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);
    
        assertTrue(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newTreeMap();
        properties1.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    
        Map<String, RecordProperty> properties2 = Maps.newTreeMap();
        properties2.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    
        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);
    
        assertTrue(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newTreeMap();
        properties.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    
        RecordType recordType1 = new RecordType(registry, properties);
        RecordType recordType2 = new RecordType(registry, properties);
        RecordType recordType3 = new RecordType(registry, properties);
    
        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    
        assertTrue(greatestSubtype.isEquivalentTo(recordType3));
    }
}
