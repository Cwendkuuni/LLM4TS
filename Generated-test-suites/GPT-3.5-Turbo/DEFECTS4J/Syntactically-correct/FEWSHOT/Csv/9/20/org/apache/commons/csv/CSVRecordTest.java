package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private String[] values = {"value1", "value2", "value3"};
    private Map<String, Integer> mapping = new HashMap<String, Integer>();
    private String comment = "This is a comment";
    private long recordNumber = 1;

    @Before
    public void setup() {
        mapping.put("field1", Integer.valueOf(0));
        mapping.put("field2", Integer.valueOf(1));
        mapping.put("field3", Integer.valueOf(2));
    }

    @Test
    public void testGetByEnum() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
        assertEquals("value3", record.get(TestEnum.VALUE3));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("value1", record.get("field1"));
        assertEquals("value2", record.get("field2"));
        assertEquals("value3", record.get("field3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
        record.get("field1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidName() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        record.get("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidIndex() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        record.get("field3");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertTrue(record.isConsistent());

        mapping.put("field4", Integer.valueOf(3));
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertTrue(record.isMapped("field1"));
        assertTrue(record.isMapped("field2"));
        assertTrue(record.isMapped("field3"));
        assertFalse(record.isMapped("field4"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertTrue(record.isSet("field1"));
        assertTrue(record.isSet("field2"));
        assertTrue(record.isSet("field3"));
        assertFalse(record.isSet("field4"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        int count = 0;
        for (String value : record) {
            assertEquals("value" + (count + 1), value);
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        Map<String, String> map = new HashMap<String, String>();
        record.putIn(map);
        assertEquals(3, map.size());
        assertEquals("value1", map.get("field1"));
        assertEquals("value2", map.get("field2"));
        assertEquals("value3", map.get("field3"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(3, record.size());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        String[] actualValues = record.values();
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], actualValues[i]);
        }
    }

    private enum TestEnum {
        VALUE1, VALUE2, VALUE3;
    }
}