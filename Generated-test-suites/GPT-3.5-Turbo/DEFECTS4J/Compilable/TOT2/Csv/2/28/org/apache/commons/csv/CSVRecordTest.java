package org.apache.commons.csv;
import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        assertEquals("A", record.get(0));
        assertEquals("B", record.get(1));
        assertEquals("C", record.get(2));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        assertNull(record.get("D"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        record.get("A");
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsNotConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C", "D"}, null, "", 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        assertTrue(record.isMapped("A"));
        assertFalse(record.isMapped("D"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        assertTrue(record.isSet("A"));
        assertFalse(record.isSet("D"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "This is a comment", 1);
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 5);
        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C", "D"}, null, "", 1);
        assertEquals(4, record.size());
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        assertEquals("[A, B, C]", record.toString());
    }
}