package org.apache.commons.csv;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, new HashMap<>(), null, 1);
        assertEquals("value2", record.get(TestEnum.VALUE2));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, new HashMap<>(), null, 1);
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, mapping, null, 1);

        assertEquals("value2", record.get("Header2"));

        try {
            record.get("NonExistingHeader");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[0], new HashMap<>(), "This is a comment", 1);
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[0], new HashMap<>(), null, 5);
        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, new HashMap<>(), null, 1);
        assertTrue(record.isConsistent());

        Map<String, Integer> inconsistentMapping = new HashMap<>();
        inconsistentMapping.put("Header1", 0);
        assertFalse(new CSVRecord(new String[] { "value1" }, inconsistentMapping, null, 1).isConsistent());
    }

    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        CSVRecord record = new CSVRecord(new String[0], mapping, null, 1);

        assertTrue(record.isMapped("Header1"));
        assertFalse(record.isMapped("NonExistingHeader"));
    }

    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        CSVRecord record = new CSVRecord(new String[] { "value1" }, mapping, null, 1);

        assertTrue(record.isSet("Header1"));
        assertFalse(record.isSet("Header1")); // Value not set
        assertFalse(record.isSet("NonExistingHeader"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, new HashMap<>(), null, 1);
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, new HashMap<>(), null, 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testToMap() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, mapping, null, 1);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Header1", "value1");
        expectedMap.put("Header2", "value2");
        expectedMap.put("Header3", "value3");

        assertEquals(expectedMap, record.toMap());
    }

}

enum TestEnum {
    VALUE1, VALUE2, VALUE3
}