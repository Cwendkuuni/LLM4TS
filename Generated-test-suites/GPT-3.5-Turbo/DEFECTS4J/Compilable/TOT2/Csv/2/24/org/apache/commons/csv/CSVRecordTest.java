package org.apache.commons.csv;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("firstName", 0);
        mapping.put("lastName", 1);
        mapping.put("age", 2);
        String comment = "Test Comment";
        long recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByIndex() {
        assertEquals("John", csvRecord.get(0));
        assertEquals("Doe", csvRecord.get(1));
        assertEquals("30", csvRecord.get(2));
    }

    @Test
    public void testGetByName() {
        assertEquals("John", csvRecord.get("firstName"));
        assertEquals("Doe", csvRecord.get("lastName"));
        assertEquals("30", csvRecord.get("age"));
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("firstName"));
        assertTrue(csvRecord.isMapped("lastName"));
        assertTrue(csvRecord.isMapped("age"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("firstName"));
        assertTrue(csvRecord.isSet("lastName"));
        assertTrue(csvRecord.isSet("age"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testGetComment() {
        assertEquals("Test Comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToString() {
        assertEquals("[John, Doe, 30]", csvRecord.toString());
    }
}