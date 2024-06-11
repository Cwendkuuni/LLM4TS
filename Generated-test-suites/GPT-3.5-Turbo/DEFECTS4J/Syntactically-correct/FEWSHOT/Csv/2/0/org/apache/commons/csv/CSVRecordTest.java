package org.apache.commons.csv;

import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertNull(record.get("name1"));
        assertNull(record.get("name2"));
        assertNull(record.get("name3"));

        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, Map.of("name1", 0, "name2", 1, "name3", 2), null, 1);
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
        assertEquals("value3", record.get("name3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        record.get("name1");
    }

    @Test
    public void testIsConsistent() {
        // No mapping provided
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertTrue(record.isConsistent());

        // Mapping provided and matches record size
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, Map.of("name1", 0, "name2", 1, "name3", 2), null, 1);
        assertTrue(record.isConsistent());

        // Mapping provided but does not match record size
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, Map.of("name1", 0, "name2", 1), null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(record.isMapped("name1"));
        assertFalse(record.isMapped("name2"));
        assertFalse(record.isMapped("name3"));

        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, Map.of("name1", 0, "name2", 1, "name3", 2), null, 1);
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertTrue(record.isMapped("name3"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(record.isSet("name1"));
        assertFalse(record.isSet("name2"));
        assertFalse(record.isSet("name3"));

        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, Map.of("name1", 0, "name2", 1, "name3", 2), null, 1);
        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertTrue(record.isSet("name3"));

        record = new CSVRecord(new String[]{"value1", "value2"}, Map.of("name1", 0, "name2", 1, "name3", 2), null, 1);
        assertFalse(record.isSet("name3"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.iterator().next());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, record.values());
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("[value1, value2, value3]", record.toString());
    }

}