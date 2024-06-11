package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {
    private CSVRecord csvRecord;
    private static String[] values;
    private static Map<String, Integer> mapping;
    private static String comment;
    private static long recordNumber;

    @Before
    public void setup() {
        values = new String[] {"value1", "value2", "value3"};
        mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        comment = "test comment";
        recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetWithEnum() {
        assertEquals("value1", csvRecord.get(TestEnum.VALUE1));
        assertEquals("value2", csvRecord.get(TestEnum.VALUE2));
        assertEquals("value3", csvRecord.get(TestEnum.VALUE3));
    }

    @Test
    public void testGetWithIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithoutMapping() {
        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
        record.get("col1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidName() {
        csvRecord.get("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidIndex() {
        csvRecord.get(5);
    }

    @Test
    public void testGetComment() {
        assertEquals("test comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
        mapping.remove("col3");
        assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("col1"));
        assertTrue(csvRecord.isMapped("col2"));
        assertTrue(csvRecord.isMapped("col3"));
        assertFalse(csvRecord.isMapped("invalid"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("col1"));
        assertTrue(csvRecord.isSet("col2"));
        assertTrue(csvRecord.isSet("col3"));
        assertFalse(csvRecord.isSet("invalid"));
    }

    @Test
    public void testIterator() {
        // TODO: Add test for iterator
    }

    @Test
    public void testPutIn() {
        // TODO: Add test for putIn
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToList() {
        // TODO: Add test for toList
    }

    @Test
    public void testToMap() {
        // TODO: Add test for toMap
    }

    private enum TestEnum {
        VALUE1,
        VALUE2,
        VALUE3;
    }
}