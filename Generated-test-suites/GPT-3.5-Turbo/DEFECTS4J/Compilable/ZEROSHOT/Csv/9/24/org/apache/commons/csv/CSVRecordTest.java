package org.apache.commons.csv;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private CSVRecord record;

    @Test
    public void testGetEnum() {
        record = createCSVRecord();
        String value = record.get(TestEnum.TEST_VALUE_1);
        assertEquals("Value 1", value);
    }

    @Test
    public void testGetIndex() {
        record = createCSVRecord();
        String value = record.get(1);
        assertEquals("Value 2", value);
    }

    @Test
    public void testGetByName() {
        record = createCSVRecord();
        String value = record.get("Column 1");
        assertEquals("Value 1", value);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        record = createCSVRecordNoMapping();
        String value = record.get("Column 1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameNotFound() {
        record = createCSVRecord();
        String value = record.get("Invalid Column");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameOutOfBounds() {
        record = createCSVRecord();
        String value = record.get("Column 3");
    }

    @Test
    public void testGetComment() {
        record = createCSVRecord();
        String comment = record.getComment();
        assertEquals("Record comment", comment);
    }

    @Test
    public void testGetRecordNumber() {
        record = createCSVRecord();
        long recordNumber = record.getRecordNumber();
        assertEquals(1, recordNumber);
    }

    @Test
    public void testIsConsistent() {
        record = createCSVRecord();
        boolean isConsistent = record.isConsistent();
        assertTrue(isConsistent);
    }

    @Test
    public void testIsConsistentNoMapping() {
        record = createCSVRecordNoMapping();
        boolean isConsistent = record.isConsistent();
        assertTrue(isConsistent);
    }

    @Test
    public void testIsConsistentInvalidMapping() {
        record = createCSVRecordInvalidMapping();
        boolean isConsistent = record.isConsistent();
        assertFalse(isConsistent);
    }

    @Test
    public void testIsMapped() {
        record = createCSVRecord();
        boolean isMapped = record.isMapped("Column 1");
        assertTrue(isMapped);
    }

    @Test
    public void testIsMappedNotMapped() {
        record = createCSVRecord();
        boolean isMapped = record.isMapped("Invalid Column");
        assertFalse(isMapped);
    }

    @Test
    public void testIsSet() {
        record = createCSVRecord();
        boolean isSet = record.isSet("Column 1");
        assertTrue(isSet);
    }

    @Test
    public void testIsSetNotMapped() {
        record = createCSVRecord();
        boolean isSet = record.isSet("Invalid Column");
        assertFalse(isSet);
    }

    @Test
    public void testIterator() {
        record = createCSVRecord();
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testPutIn() {
        record = createCSVRecord();
        Map<String, String> map = new HashMap<>();
        map = record.putIn(map);
        assertEquals("Value 1", map.get("Column 1"));
        assertEquals("Value 2", map.get("Column 2"));
    }

    @Test
    public void testSize() {
        record = createCSVRecord();
        int size = record.size();
        assertEquals(2, size);
    }

    @Test
    public void testToString() {
        record = createCSVRecord();
        String recordString = record.toString();
        assertEquals("[Value 1, Value 2]", recordString);
    }

    private CSVRecord createCSVRecord() {
        String[] values = {"Value 1", "Value 2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column 1", 0);
        mapping.put("Column 2", 1);
        String comment = "Record comment";
        long recordNumber = 1;
        return new CSVRecord(values, mapping, comment, recordNumber);
    }

    private CSVRecord createCSVRecordNoMapping() {
        String[] values = {"Value 1", "Value 2"};
        String comment = "Record comment";
        long recordNumber = 1;
        return new CSVRecord(values, null, comment, recordNumber);
    }

    private CSVRecord createCSVRecordInvalidMapping() {
        String[] values = {"Value 1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column 1", 0);
        mapping.put("Column 2", 1);
        String comment = "Record comment";
        long recordNumber = 1;
        return new CSVRecord(values, mapping, comment, recordNumber);
    }

    private enum TestEnum {
        TEST_VALUE_1,
        TEST_VALUE_2
    }

}