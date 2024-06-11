
package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private String[] values;
    private HashMap<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @Before
    public void setUp() {
        values = new String[] {"value1", "value2", "value3"};
        mapping = new HashMap<String, Integer>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        comment = "This is a comment";
        recordNumber = 1;
    }

    @Test
    public void testGet() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetWithInvalidIndex() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertNull(record.get(3));
    }

    @Test
    public void testGetWithNullMapping() {
        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
        assertNull(record.get("column1"));
        assertNull(record.get("column2"));
        assertNull(record.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithNoMapping() {
        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
        record.get("column4");
    }

    @Test
    public void testGetWithMapping() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertEquals("value3", record.get("column3"));
    }

    @Test
    public void testGetWithInvalidName() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertNull(record.get("column4"));
    }

    @Test
    public void testIsConsistentWithNullMapping() {
        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithValidMapping() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithInvalidMapping() {
        mapping.put("column4", 3);
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMappedWithNullMapping() {
        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
        assertFalse(record.isMapped("column1"));
        assertFalse(record.isMapped("column2"));
        assertFalse(record.isMapped("column3"));
    }

    @Test
    public void testIsMappedWithValidMapping() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertTrue(record.isMapped("column3"));
    }

    @Test
    public void testIsMappedWithInvalidMapping() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertFalse(record.isMapped("column4"));
    }

    @Test
    public void testIsSetWithNullMapping() {
        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
        assertFalse(record.isSet("column1"));
        assertFalse(record.isSet("column2"));
        assertFalse(record.isSet("column3"));
    }

    @Test
    public void testIsSetWithValidMapping() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertTrue(record.isSet("column3"));
    }

    @Test
    public void testIsSetWithInvalidMapping() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertFalse(record.isSet("column4"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        int count = 0;
        for (String value : record) {
            assertEquals(values[count], value);
            count++;
        }
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertArrayEquals(values, record.values());
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(comment, record.getComment());
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

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(Arrays.toString(values), record.toString());
    }

}
