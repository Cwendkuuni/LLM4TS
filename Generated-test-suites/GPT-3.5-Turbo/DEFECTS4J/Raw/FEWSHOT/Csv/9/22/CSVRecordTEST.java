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
    public void testGetWithEnum() {
        CSVRecord record = createCSVRecord();
        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
        assertEquals("value3", record.get(TestEnum.VALUE3));
    }

    @Test
    public void testGetWithIndex() {
        CSVRecord record = createCSVRecord();
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetWithName() {
        CSVRecord record = createCSVRecord();
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
        assertEquals("value3", record.get("name3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithNoHeaderMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        record.get("name1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithUnmappedName() {
        CSVRecord record = createCSVRecord();
        record.get("unknown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidIndex() {
        CSVRecord record = createCSVRecord();
        record.get(3);
    }

    @Test
    public void testGetComment() {
        CSVRecord record = createCSVRecord();
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = createCSVRecord();
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = createCSVRecord();
        assertTrue(record.isConsistent());

        record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertFalse(record.isConsistent());

        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = createCSVRecord();
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertTrue(record.isMapped("name3"));

        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(record.isMapped("name1"));
        assertFalse(record.isMapped("name2"));
        assertFalse(record.isMapped("name3"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = createCSVRecord();
        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertTrue(record.isSet("name3"));

        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(record.isSet("name1"));
        assertFalse(record.isSet("name2"));
        assertFalse(record.isSet("name3"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = createCSVRecord();
        int i = 0;
        for (String value : record) {
            assertEquals("value" + (i + 1), value);
            i++;
        }
        assertEquals(3, i);
    }

    @Test
    public void testPutIn() {
        CSVRecord record = createCSVRecord();
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        assertEquals(record.get("name1"), map.get("name1"));
        assertEquals(record.get("name2"), map.get("name2"));
        assertEquals(record.get("name3"), map.get("name3"));
    }

    @Test
    public void testSize() {
        CSVRecord record = createCSVRecord();
        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = createCSVRecord();
        assertEquals(3, record.toList().size());
        assertEquals("value1", record.toList().get(0));
        assertEquals("value2", record.toList().get(1));
        assertEquals("value3", record.toList().get(2));
    }

    @Test
    public void testToMap() {
        CSVRecord record = createCSVRecord();
        Map<String, String> map = record.toMap();
        assertEquals(record.get("name1"), map.get("name1"));
        assertEquals(record.get("name2"), map.get("name2"));
        assertEquals(record.get("name3"), map.get("name3"));
    }

    @Test
    public void testValues() {
        CSVRecord record = createCSVRecord();
        assertEquals(3, record.values().length);
        assertEquals("value1", record.values()[0]);
        assertEquals("value2", record.values()[1]);
        assertEquals("value3", record.values()[2]);
    }

    private CSVRecord createCSVRecord() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        return new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, "comment", 1);
    }

    private enum TestEnum {
        VALUE1, VALUE2, VALUE3
    }
}