package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private static final String[] VALUES = {"value1", "value2", "value3"};
    private static final Map<String, Integer> MAPPING = new HashMap<>();
    private static final String COMMENT = "This is a comment.";
    private static final long RECORD_NUMBER = 1;

    private CSVRecord record;

    @Before
    public void setup() {
        MAPPING.put("header1", 0);
        MAPPING.put("header2", 1);
        MAPPING.put("header3", 2);
        record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        assertEquals("value1", record.get("header1"));
        assertEquals("value2", record.get("header2"));
        assertEquals("value3", record.get("header3"));
        assertNull(record.get("header4"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord recordWithoutMapping = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        recordWithoutMapping.get("header1");
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
        CSVRecord inconsistentRecord = new CSVRecord(new String[]{"value1"}, MAPPING, COMMENT, RECORD_NUMBER);
        assertFalse(inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));
        assertTrue(record.isMapped("header3"));
        assertFalse(record.isMapped("header4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("header1"));
        assertTrue(record.isSet("header2"));
        assertTrue(record.isSet("header3"));
        assertFalse(record.isSet("header4"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : record) {
            assertEquals(VALUES[count], value);
            count++;
        }
    }

    @Test
    public void testValues() {
        assertEquals(VALUES, record.values());
    }

    @Test
    public void testGetComment() {
        assertEquals(COMMENT, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(RECORD_NUMBER, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        assertEquals(VALUES.length, record.size());
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", record.toString());
    }
}