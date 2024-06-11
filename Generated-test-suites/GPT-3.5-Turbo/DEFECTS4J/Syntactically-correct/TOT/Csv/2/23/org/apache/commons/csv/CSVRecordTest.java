package org.apache.commons.csv;

import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    private static final String[] VALUES = {"John", "Doe", "john.doe@example.com"};
    private static final long RECORD_NUMBER = 1L;
    private static final String COMMENT = "This is a comment";

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("john.doe@example.com", record.get(2));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        assertNull(record.get("Name"));
        assertNull(record.get("Email"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        record.get("Name");
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsNotConsistent() {
        String[] inconsistentValues = {"John", "Doe"};
        CSVRecord record = new CSVRecord(inconsistentValues, null, COMMENT, RECORD_NUMBER);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        assertFalse(record.isMapped("Name"));
        assertFalse(record.isMapped("Email"));
    }

    @Test
    public void testIsMappedWithMapping() {
        String[] mappedValues = {"John", "Doe"};
        CSVRecord record = new CSVRecord(mappedValues, null, COMMENT, RECORD_NUMBER);
        assertFalse(record.isMapped("Name"));
        assertFalse(record.isMapped("Email"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        assertFalse(record.isSet("Name"));
        assertFalse(record.isSet("Email"));
    }

    @Test
    public void testIsSetWithMapping() {
        String[] mappedValues = {"John", "Doe"};
        CSVRecord record = new CSVRecord(mappedValues, null, COMMENT, RECORD_NUMBER);
        assertFalse(record.isSet("Name"));
        assertFalse(record.isSet("Email"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        assertArrayEquals(VALUES, record.values());
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        assertEquals(COMMENT, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        assertEquals(RECORD_NUMBER, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        assertEquals(3, record.size());
    }


    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        assertEquals(Arrays.toString(VALUES), record.toString());
    }

}