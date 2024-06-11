
package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    private static final String[] VALUES = { "value1", "value2", "value3" };
    private static final Map<String, Integer> MAPPING = new HashMap<>();

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);

        assertEquals("value1", record.get(EnumTest.Enum1));
        assertEquals("value2", record.get(EnumTest.Enum2));
        assertEquals("value3", record.get(EnumTest.Enum3));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);

        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        MAPPING.put("col1", 0);
        MAPPING.put("col2", 1);
        MAPPING.put("col3", 2);

        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);

        assertEquals("value1", record.get("col1"));
        assertEquals("value2", record.get("col2"));
        assertEquals("value3", record.get("col3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord record = new CSVRecord(VALUES, null, null, 1);
        record.get("col1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidMapping() {
        MAPPING.put("col1", 0);
        MAPPING.put("col2", 1);
        MAPPING.put("col3", 2);

        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);
        record.get("col4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidIndex() {
        MAPPING.put("col1", 0);
        MAPPING.put("col2", 1);
        MAPPING.put("col3", 3);

        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);
        record.get("col3");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, "This is a comment", 1);

        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record1 = new CSVRecord(VALUES, MAPPING, null, 1);
        assertEquals(1, record1.getRecordNumber());

        CSVRecord record2 = new CSVRecord(VALUES, MAPPING, null, 100);
        assertEquals(100, record2.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        MAPPING.put("col1", 0);
        MAPPING.put("col2", 1);
        MAPPING.put("col3", 2);

        CSVRecord record1 = new CSVRecord(VALUES, MAPPING, null, 1);
        assertTrue(record1.isConsistent());

        CSVRecord record2 = new CSVRecord(VALUES, null, null, 1);
        assertTrue(record2.isConsistent());

        CSVRecord record3 = new CSVRecord(new String[] { "value1" }, MAPPING, null, 1);
        assertFalse(record3.isConsistent());
    }

    @Test
    public void testIsMapped() {
        MAPPING.put("col1", 0);
        MAPPING.put("col2", 1);
        MAPPING.put("col3", 2);

        CSVRecord record1 = new CSVRecord(VALUES, MAPPING, null, 1);
        assertTrue(record1.isMapped("col1"));
        assertTrue(record1.isMapped("col2"));
        assertTrue(record1.isMapped("col3"));
        assertFalse(record1.isMapped("col4"));

        CSVRecord record2 = new CSVRecord(VALUES, null, null, 1);
        assertFalse(record2.isMapped("col1"));
        assertFalse(record2.isMapped("col2"));
        assertFalse(record2.isMapped("col3"));
    }

    @Test
    public void testIsSet() {
        MAPPING.put("col1", 0);
        MAPPING.put("col2", 1);
        MAPPING.put("col3", 2);

        CSVRecord record1 = new CSVRecord(VALUES, MAPPING, null, 1);
        assertTrue(record1.isSet("col1"));
        assertTrue(record1.isSet("col2"));
        assertTrue(record1.isSet("col3"));
        assertFalse(record1.isSet("col4"));

        CSVRecord record2 = new CSVRecord(new String[] { "value1" }, MAPPING, null, 1);
        assertTrue(record2.isSet("col1"));
        assertFalse(record2.isSet("col2"));
        assertFalse(record2.isSet("col3"));
        assertFalse(record2.isSet("col4"));

        CSVRecord record3 = new CSVRecord(VALUES, null, null, 1);
        assertFalse(record3.isSet("col1"));
        assertFalse(record3.isSet("col2"));
        assertFalse(record3.isSet("col3"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);

        int count = 0;
        for (String value : record) {
            assertEquals(VALUES[count], value);
            count++;
        }
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);

        assertEquals(Arrays.asList(VALUES), record.toList());
    }

    @Test
    public void testPutIn() {
        MAPPING.put("col1", 0);
        MAPPING.put("col2", 1);
        MAPPING.put("col3", 2);

        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);
        Map<String, String> map = new HashMap<>();

        map = record.putIn(map);

        assertEquals("value1", map.get("col1"));
        assertEquals("value2", map.get("col2"));
        assertEquals("value3", map.get("col3"));
    }

    @Test
    public void testPutInEmptyRecord() {
        CSVRecord record = new CSVRecord(new String[0], MAPPING, null, 1);
        Map<String, String> map = new HashMap<>();

        map = record.putIn(map);

        assertTrue(map.isEmpty());
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);

        assertEquals("[value1, value2, value3]", record.toString());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);

        assertEquals(VALUES, record.values());
    }

    private static class EnumTest {
        public enum Enum1 {
        }

        public enum Enum2 {
        }

        public enum Enum3 {
        }
    }
}
