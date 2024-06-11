package org.apache.commons.csv;

import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {

    // Test constructor with valid values
    @Test
    public void testConstructorWithValidValues() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertNotNull(record);
        assertEquals(values.length, record.size());
        assertEquals(values[0], record.get(0));
    }

    // Test constructor with null values
    @Test
    public void testConstructorWithNullValues() {
        CSVRecord record = new CSVRecord(null, null, null, 1);
        assertNotNull(record);
        assertEquals(0, record.size());
    }

    // Test get method with valid index
    @Test
    public void testGetWithValidIndex() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertNotNull(record);
        assertEquals(values[0], record.get(0));
    }

    // Test get method with invalid index
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertNotNull(record);
        record.get(3);
    }

    // Test get method with valid name
    @Test
    public void testGetWithName() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertNotNull(record);
        assertEquals(values[0], record.get("col1"));
    }

    // Test get method with invalid name
    @Test
    public void testGetWithInvalidName() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertNotNull(record);
        assertNull(record.get("col2"));
    }

    // Test isConsistent method with consistent record
    @Test
    public void testIsConsistentWithConsistentRecord() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertNotNull(record);
        assertTrue(record.isConsistent());
    }

    // Test isConsistent method with inconsistent record
    @Test
    public void testIsConsistentWithInconsistentRecord() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertNotNull(record);
        assertFalse(record.isConsistent());
    }

    // Test isMapped method with mapped column
    @Test
    public void testIsMappedWithMappedColumn() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertNotNull(record);
        assertTrue(record.isMapped("col1"));
    }

    // Test isMapped method with unmapped column
    @Test
    public void testIsMappedWithUnmappedColumn() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertNotNull(record);
        assertFalse(record.isMapped("col2"));
    }

    // Test isSet method with set column
    @Test
    public void testIsSetWithSetColumn() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertNotNull(record);
        assertTrue(record.isSet("col1"));
    }

    // Test isSet method with unset column
    @Test
    public void testIsSetWithUnsetColumn() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertNotNull(record);
        assertFalse(record.isSet("col1"));
    }

    // Test iterator method
    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertNotNull(record);
        Iterator<String> iterator = record.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            assertEquals(values[count], iterator.next());
            count++;
        }
        assertEquals(values.length, count);
    }

    // Test values method
    @Test
    public void testValues() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertNotNull(record);
        assertArrayEquals(values, record.values());
    }

    // Test getComment method
    @Test
    public void testGetComment() {
        String[] values = {"value1", "value2", "value3"};
        String comment = "This is a comment";
        CSVRecord record = new CSVRecord(values, null, comment, 1);
        assertNotNull(record);
        assertEquals(comment, record.getComment());
    }

    // Test getRecordNumber method
    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};
        long recordNumber = 42;
        CSVRecord record = new CSVRecord(values, null, null, recordNumber);
        assertNotNull(record);
        assertEquals(recordNumber, record.getRecordNumber());
    }

    // Test size method
    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertNotNull(record);
        assertEquals(values.length, record.size());
    }
    
    // Test toString method
    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertNotNull(record);
        assertEquals(Arrays.toString(values), record.toString());
    }
}