package org.apache.commons.csv;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals("A", record.get(0));
        assertEquals("B", record.get(1));
        assertEquals("C", record.get(2));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertNull(record.get("unknown"));
        assertEquals("A", record.get("0"));
        assertEquals("B", record.get("1"));
        assertEquals("C", record.get("2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        record.get("name");
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertTrue(record.isConsistent());

        record = new CSVRecord(new String[]{"A", "B"}, null, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertFalse(record.isMapped("name"));

        record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertTrue(record.isMapped("0"));
        assertTrue(record.isMapped("1"));
        assertTrue(record.isMapped("2"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertFalse(record.isSet("name"));

        record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertTrue(record.isSet("0"));
        assertTrue(record.isSet("1"));
        assertTrue(record.isSet("2"));
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "This is a comment", 1);
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(3, count);
    }

}