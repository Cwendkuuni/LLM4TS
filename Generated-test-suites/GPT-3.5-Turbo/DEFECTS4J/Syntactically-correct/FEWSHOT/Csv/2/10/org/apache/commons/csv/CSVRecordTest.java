package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CSVRecordTest {
    @Test
    public void testGet() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get(1));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        record.get(2);
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        assertNull(record.get("foo"));
        assertNull(record.get("bar"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        record.get("foo");
    }

    @Test
    public void testGetByNameWithMapping() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, Map.of("bar", 1), null, 1);
        assertNull(record.get("foo"));
        assertEquals("bar", record.get("bar"));
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record1 = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        assertTrue(record1.isConsistent());
        CSVRecord record2 = new CSVRecord(new String[]{"foo"}, null, null, 1);
        assertTrue(record2.isConsistent());
        CSVRecord record3 = new CSVRecord(new String[]{"foo", "bar"}, Map.of("bar", 1), null, 1);
        assertTrue(record3.isConsistent());
        CSVRecord record4 = new CSVRecord(new String[]{"foo"}, Map.of("bar", 1), null, 1);
        assertTrue(record4.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        assertTrue(record.isMapped("foo"));
        assertTrue(record.isMapped("bar"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record1 = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        assertTrue(record1.isSet("foo"));
        assertTrue(record1.isSet("bar"));
        CSVRecord record2 = new CSVRecord(new String[]{"foo"}, null, null, 1);
        assertTrue(record2.isSet("foo"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        assertTrue(record.iterator().hasNext());
        assertEquals("foo", record.iterator().next());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        assertEquals(2, record.values().length);
        assertEquals("foo", record.values()[0]);
        assertEquals("bar", record.values()[1]);
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, "comment", 1);
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        assertEquals(2, record.size());
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        assertEquals("[foo, bar]", record.toString());
    }
}