package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @Before
    public void setUp() {
        values = new String[]{"Name", "Age", "City"};
        mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        comment = "Record comment";
        recordNumber = 1;
    }

    @Test
    public void testCSVRecordConstruction() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("Name,Age,City", record.toString());
        assertEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertEquals(comment, record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testGet() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("Name", record.get(0));
        assertEquals("Age", record.get(1));
        assertEquals("City", record.get(2));
        assertNull(record.get(3));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("Name", record.get("Name"));
        assertEquals("Age", record.get("Age"));
        assertEquals("City", record.get("City"));
        assertNull(record.get("Unknown"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        Map<String, Integer> emptyMapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, emptyMapping, comment, recordNumber);
        record.get("Name");
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertTrue(record.isConsistent());

        String[] wrongValues = new String[]{"Name", "Age", "City", "Unknown"};
        CSVRecord wrongRecord = new CSVRecord(wrongValues, mapping, comment, recordNumber);
        assertFalse(wrongRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertFalse(record.isMapped("Unknown"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertFalse(record.isSet("Name"));
        assertFalse(record.isSet("Age"));
        assertFalse(record.isSet("City"));

        String[] setValues = new String[]{"Name:John", "Age:25", "City:New York"};
        CSVRecord setRecord = new CSVRecord(setValues, mapping, comment, recordNumber);
        assertTrue(setRecord.isSet("Name"));
        assertTrue(setRecord.isSet("Age"));
        assertTrue(setRecord.isSet("City"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        List<String> list = Arrays.asList(record.values());
        Iterator<String> iter = record.iterator();
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], iter.next());
        }
        assertFalse(iter.hasNext());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(values.length, record.size());
    }
}