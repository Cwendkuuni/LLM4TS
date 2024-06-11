package org.apache.commons.csv;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

public class CSVRecordTest {


    private CSVRecord createRecord(String[] values, HashMap<String, Integer> mapping, String comment, long recordNumber) {
        return new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGet() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = createRecord(values, null, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetWithName() {
        String[] values = {"value1", "value2"};
        HashMap<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = createRecord(values, mapping, null, 1);
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertNull(record.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithNameNoMapping() {
        String[] values = {"value1", "value2"};
        CSVRecord record = createRecord(values, null, null, 1);
        record.get("column1");
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"value1", "value2"};
        CSVRecord record = createRecord(values, null, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithMapping() {
        String[] values = {"value1", "value2"};
        HashMap<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = createRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithMappingMismatch() {
        String[] values = {"value1", "value2"};
        HashMap<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("column1", 0);
        CSVRecord record = createRecord(values, mapping, null, 1);
        assertTrue(!record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = {"value1", "value2"};
        HashMap<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = createRecord(values, mapping, null, 1);
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertTrue(!record.isMapped("column3"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2"};
        HashMap<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = createRecord(values, mapping, null, 1);
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertTrue(!record.isSet("column3"));
    }

    @Test
    public void testIterator() {
        String[] values = {"value1", "value2"};
        CSVRecord record = createRecord(values, null, null, 1);
        int counter = 0;
        for (String value : record) {
            assertEquals(values[counter], value);
            counter++;
        }
    }

    @Test
    public void testValues() {
        String[] values = {"value1", "value2"};
        CSVRecord record = createRecord(values, null, null, 1);
        assertEquals(values[0], record.values()[0]);
        assertEquals(values[1], record.values()[1]);
    }

    @Test
    public void testGetComment() {
        String[] values = {"value1", "value2"};
        CSVRecord record = createRecord(values, null, "comment", 1);
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2"};
        CSVRecord record = createRecord(values, null, null, 5);
        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        String[] values = {"value1", "value2"};
        CSVRecord record = createRecord(values, null, null, 1);
        assertEquals(2, record.size());
    }

    @Test
    public void testToString() {
        String[] values = {"value1", "value2"};
        CSVRecord record = createRecord(values, null, null, 1);
        assertEquals("[value1, value2]", record.toString());
    }
}