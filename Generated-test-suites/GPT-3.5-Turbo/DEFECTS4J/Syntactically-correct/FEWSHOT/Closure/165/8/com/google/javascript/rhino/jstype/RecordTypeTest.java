package com.google.javascript.rhino.jstype;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
    private JSTypeRegistry registry;
    private RecordType recordType;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        
        RecordProperty property1 = new RecordProperty(null, null);
        RecordProperty property2 = new RecordProperty(null, null);
        
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("property1", property1);
        properties.put("property2", property2);
        
        recordType = new RecordType(registry, properties);
    }

    @Test
    public void testIsEquivalentTo() {
        RecordProperty property1 = new RecordProperty(null, null);
        RecordProperty property2 = new RecordProperty(null, null);
        
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("property1", property1);
        properties.put("property2", property2);
        
        RecordType otherRecordType = new RecordType(registry, properties);
        
        assert(recordType.isEquivalentTo(otherRecordType));
    }

    @Test
    public void testIsSubtype() {
        RecordProperty property1 = new RecordProperty(null, null);
        RecordProperty property2 = new RecordProperty(null, null);
        
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("property1", property1);
        properties.put("property2", property2);
        
        RecordType otherRecordType = new RecordType(registry, properties);
        
        assert(recordType.isSubtype(otherRecordType));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordProperty property1 = new RecordProperty(null, null);
        RecordProperty property2 = new RecordProperty(null, null);
        RecordProperty property3 = new RecordProperty(null, null);
        
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("property1", property1);
        properties1.put("property2", property2);
        
        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("property1", property1);
        properties2.put("property2", property2);
        properties2.put("property3", property3);
        
        Map<String, RecordProperty> properties3 = Maps.newHashMap();
        properties3.put("property1", property1);
        
        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);
        RecordType recordType3 = new RecordType(registry, properties3);
        
        assert(recordType.getGreatestSubtypeHelper(recordType1).isEquivalentTo(recordType));
        assert(recordType.getGreatestSubtypeHelper(recordType2).isEquivalentTo(recordType2));
        assert(recordType.getGreatestSubtypeHelper(recordType3).isEmptyType());
    }
    
    /*
    * Test various scenarios of defineProperty()
    */
    @Test
    public void testDefineProperty() {
        // Test when the record type is frozen
        assert(recordType.defineProperty("property3", null, false, null) == false);
        
        // Test when the record type is not frozen
        recordType = new RecordType(registry, null);
        assert(recordType.defineProperty("property1", null, false, null) == false);
        
        // Test when the property is inferred
        assert(recordType.defineProperty("property1", null, true, null) == true);
    }
}