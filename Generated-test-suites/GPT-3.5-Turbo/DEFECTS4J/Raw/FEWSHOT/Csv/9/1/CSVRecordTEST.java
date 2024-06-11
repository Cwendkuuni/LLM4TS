package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = createRecord();
        assertEquals("value", record.get(TestEnum.VALUE));
    }

    @Test
    public void testGetIndex() {
        CSVRecord record = createRecord();
        assertEquals("value", record.get(0));
    }

    @Test
    public void testGetName() {
        CSVRecord record = createRecord();
        assertEquals("value", record.get("name"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameNoMapping() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameInvalid() {
        CSVRecord record = createRecord();
        record.get("invalid");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[0], null, "comment", 0);
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 5);
        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testIsConsistentTrue() {
        CSVRecord record = createRecord();
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentFalse() {
        CSVRecord record = new CSVRecord(new String[1], null, null, 0);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMappedTrue() {
        CSVRecord record = createRecord();
        assertTrue(record.isMapped("name"));
    }

    @Test
    public void testIsMappedFalse() {
        CSVRecord record = createRecord();
        assertFalse(record.isMapped("invalid"));
    }

    @Test
    public void testIsSetTrue() {
        CSVRecord record = createRecord();
        assertTrue(record.isSet("name"));
    }

    @Test
    public void testIsSetFalse() {
        CSVRecord record = createRecord();
        assertFalse(record.isSet("invalid"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = createRecord();
        assertEquals("value", record.iterator().next());
    }

    @Test
    public void testPutIn() {
        CSVRecord record = createRecord();
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        assertEquals("value", map.get("name"));
    }

    @Test
    public void testSize() {
        CSVRecord record = createRecord();
        assertEquals(1, record.size());
    }

    @Test
    public void testToMap() {
        CSVRecord record = createRecord();
        Map<String, String> map = record.toMap();
        assertEquals("value", map.get("name"));
    }

    @Test
    public void testToString() {
        CSVRecord record = createRecord();
        assertEquals("[value]", record.toString());
    }

    @Test
    public void testValues() {
        CSVRecord record = createRecord();
        assertEquals("value", record.values()[0]);
    }
    
    private CSVRecord createRecord() {
        String[] values = {"value"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        return new CSVRecord(values, mapping, null, 0);
    }
}