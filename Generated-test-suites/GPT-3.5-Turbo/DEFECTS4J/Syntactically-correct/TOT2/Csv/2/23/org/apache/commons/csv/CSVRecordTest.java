
package org.apache.commons.csv;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.HashMap;

public class CSVRecordTest {

    CSVRecord record;

    @BeforeClass
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        HashMap<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        record = new CSVRecord(values, mapping, "Sample comment", 1);
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", record.get(0));
    }

    @Test
    public void testGetByName() {
        assertEquals("value2", record.get("col2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, null, null, 1);
        record.get("col1");
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("col1"));
        assertFalse(record.isMapped("col4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("col2"));
        assertFalse(record.isSet("col3"));
        assertFalse(record.isSet("col4"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testGetComment() {
        assertEquals("Sample comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", record.toString());
    }
}

