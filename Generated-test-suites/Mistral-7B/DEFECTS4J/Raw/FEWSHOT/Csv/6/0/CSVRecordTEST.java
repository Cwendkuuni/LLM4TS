package org.apache.commons.csv;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGet() {
        String[] values = new String[]{"apple", "orange", "banana"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals("apple", record.get(0));
        assertEquals("orange", record.get(1));
        assertEquals("banana", record.get(2));
        assertEquals("apple", record.get(Enum.valueOf(CSVRecord.class, "ZERO")));
        assertEquals("orange", record.get(Enum.valueOf(CSVRecord.class, "ONE")));
        assertEquals("banana", record.get(Enum.valueOf(CSVRecord.class, "TWO")));

        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, null, null, 1);
        assertNull(emptyRecord.get(0));
        assertNull(emptyRecord.get(Enum.valueOf(CSVRecord.class, "ZERO")));
    }

    @Test
    public void testGetNullString() {
        String[] values = new String[]{"apple", null, "banana"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals("apple", record.get(0));
        assertNull(record.get(1));
        assertEquals("banana", record.get(2));
        assertEquals("apple", record.get(Enum.valueOf(CSVRecord.class, "ZERO")));
        assertNull(record.get(Enum.valueOf(CSVRecord.class, "ONE")));
        assertEquals("banana", record.get(Enum.valueOf(CSVRecord.class, "TWO")));
    }

    @Test
    public void testGetWithMapping() {
        String[] values = new String[]{"name1", "value1", "name2", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("name1", record.get("name1"));
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
        assertNull(record.get("name3"));

        assertEquals("name1", record.get(Enum.valueOf(CSVRecord.class, "ZERO")));
        assertEquals("value1", record.get(Enum.valueOf(CSVRecord.class, "ZERO")));
        assertEquals("name2", record.get(Enum.valueOf(CSVRecord.class, "TWO")));
        assertEquals("value2", record.get(Enum.valueOf(CSVRecord.class, "TWO")));
    }

    @Test
    public void testGetComment() {
        String comment = "This is a comment.";
        CSVRecord record = new CSVRecord(new String[0], null, comment, 1);

        assertEquals(comment, record.getComment());
        assertNull(record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 1);

        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"name1", "value1", "name2", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isConsistent());

        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, null, null, 1);
        assertTrue(emptyRecord.isConsistent());

        String[] valuesWithMismatch = new String[]{"name1", "value1", "name3", "value2"};
        CSVRecord recordWithMismatch = new CSVRecord(valuesWithMismatch, mapping, null, 1);
        assertFalse(recordWithMismatch.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"name1", "value1", "name2", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertFalse(record.isMapped("name3"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"name1", "value1", "name2", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertFalse(record.isSet("name3"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"name1", "value1", "name2", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        List<String> expectedList = Arrays.asList(values);

        Iterator<String> recordIterator = record.iterator();
        for (String expectedValue : expectedList) {
            assertEquals(expectedValue, recordIterator.next());
        }
        assertFalse(recordIterator.hasNext());
    }

    @Test
    public void testPutIn() {
        String[] values = new String[]{"name1", "value1", "name2", "value2"};
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("name1", "value1");
        expectedMap.put("name2", "value2");

        CSVRecord record = new CSVRecord(values, null, null, 1);
        Map<String, String> map = record.putIn(new HashMap<String, String>());

        assertEquals(expectedMap, map);
    }

    @Test
    public void testSize() {
        String[] values = new String[]{"name1", "value1", "name2", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals(values.length, record.size());
    }

    @Test
    public void testToList() {
        String[] values = new String[]{"name1", "value1", "name2", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        List<String> expectedList = Arrays.asList(values);

        List<String> recordList = record.toList();

        assertEquals(expectedList, recordList);
    }

    @Test
    public void testToMap() {
        String[] values = new String[]{"name1", "value1", "name2", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("name1", "value1");
        expectedMap.put("name2", "value2");

        Map<String, String> recordMap = record.toMap();

        assertEquals(expectedMap, recordMap);
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"name1", "value1", "name2", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        String expectedString = "[name1, value1, name2, value2]";

        assertEquals(expectedString, record.toString());
    }

    @Test
    public void testValues() {
        String[] values = new String[]{"name1", "value1", "name2", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        String[] expectedValues = {"name1", "value1", "name2", "value2"};

        String[] recordValues = record.values();

        assertArrayEquals(expectedValues, recordValues);
    }
}