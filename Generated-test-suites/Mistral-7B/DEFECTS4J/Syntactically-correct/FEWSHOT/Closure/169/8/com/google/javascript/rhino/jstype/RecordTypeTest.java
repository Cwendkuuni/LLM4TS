package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {
    @Test
    public void testIsSynthetic() {
        RecordType recordType1 = new RecordType(MockJSTypeRegistry.getInstance(), Maps.newHashMap(), true);
        RecordType recordType2 = new RecordType(MockJSTypeRegistry.getInstance(), Maps.newHashMap(), false);

        assertTrue(recordType1.isSynthetic() == false);
        assertTrue(recordType2.isSynthetic() == true);
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType recordType1 = new RecordType(MockJSTypeRegistry.getInstance(), Maps.newHashMap(), true);
        RecordType recordType2 = new RecordType(MockJSTypeRegistry.getInstance(), Maps.newHashMap(), true);
        RecordType recordType3 = new RecordType(MockJSTypeRegistry.getInstance(), Maps.newHashMap(), true);
        RecordType recordType4 = new RecordType(MockJSTypeRegistry.getInstance(), Maps.newLinkedHashSet("prop1", "prop2").asMap(), true);

        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType1, true));
        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
        assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, true));
        assertFalse(recordType1.checkRecordEquivalenceHelper(recordType4, true));
        assertFalse(recordType1.checkRecordEquivalenceHelper(new ObjectType(MockJSTypeRegistry.getInstance(), null), true));
    }
}