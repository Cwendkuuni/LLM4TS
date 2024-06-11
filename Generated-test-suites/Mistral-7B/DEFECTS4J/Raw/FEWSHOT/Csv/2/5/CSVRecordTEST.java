package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithValuesAndMapping() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertArrayEquals(new String[]{"Name", "Age", "City"}, record.values());
        assertEquals("Name", record.get(0));
        assertEquals("Age", record.get(1));
        assertEquals("City", record.get(2));
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertNull(record.get("Unknown"));
    }

    @Test
    public void testCSVRecordWithValuesButNoMapping() {
        String[] values = {"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertArrayEquals(new String[]{"Name", "Age", "City"}, record.values());
        assertNull(record.get(0));
        assertNull(record.get(1));
        assertNull(record.get(2));
        assertFalse(record.isConsistent());
    }

    @Test
    public void testGetComment() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, "Comment", 1);

        assertEquals("Comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        String[] values = {"Name", "Age", "City"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        String[] values = {"Name", "Age", "City"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals("[Name, Age, City]", record.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithNoMapping() {
        String[] values = {"Name", "Age", "City"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        record.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInconsistentRecord() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get("City");
    }
}