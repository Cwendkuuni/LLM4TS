package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        CSVRecord record = createTestRecord();
        Enum<?> testEnum = TestEnum.TEST;
        String expectedValue = "value";
        assertEquals(expectedValue, record.get(testEnum));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = createTestRecord();
        int testIndex = 1;
        String expectedValue = "value";
        assertEquals(expectedValue, record.get(testIndex));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = createTestRecord();
        String testName = "name";
        String expectedValue = "value";
        assertEquals(expectedValue, record.get(testName));
    }

    @Test
    public void testGetComment() {
        CSVRecord record = createTestRecord();
        String expectedComment = "comment";
        assertEquals(expectedComment, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = createTestRecord();
        long expectedRecordNumber = 123;
        assertEquals(expectedRecordNumber, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = createTestRecord();
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = createTestRecord();
        String testName = "name";
        assertTrue(record.isMapped(testName));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = createTestRecord();
        String testName = "name";
        assertTrue(record.isSet(testName));
    }

    @Test
    public void testIterator() {
        CSVRecord record = createTestRecord();
        assertFalse(record.iterator().hasNext());
    }

    @Test
    public void testPutIn() {
        CSVRecord record = createTestRecord();
        Map<String, String> map = new HashMap<>();
        assertFalse(record.putIn(map).isEmpty());
    }

    @Test
    public void testSize() {
        CSVRecord record = createTestRecord();
        int expectedSize = 2;
        assertEquals(expectedSize, record.size());
    }

    @Test
    public void testToMap() {
        CSVRecord record = createTestRecord();
        assertFalse(record.toMap().isEmpty());
    }

    private CSVRecord createTestRecord() {
        String[] values = {"value", "value"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("name2", 1);
        String comment = "comment";
        long recordNumber = 123;
        return new CSVRecord(values, mapping, comment, recordNumber);
    }

    private enum TestEnum {
        TEST;
    }

}