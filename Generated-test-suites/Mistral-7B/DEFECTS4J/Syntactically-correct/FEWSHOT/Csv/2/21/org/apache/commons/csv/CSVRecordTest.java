package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithValues() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertArrayEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertNull(record.comment);
        assertEquals(1, record.recordNumber);
        assertEquals(3, record.size());
        assertTrue(record.isConsistent());
        assertSame(values[0], record.get("Name"));
        assertSame(values[1], record.get("Age"));
        assertSame(values[2], record.get("City"));
    }

    @Test
    public void testCSVRecordWithoutValues() {
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(null, mapping, null, 1);

        assertNull(record.values());
        assertSame(mapping, record.mapping);
        assertNull(record.comment);
        assertEquals(1, record.recordNumber);
        assertEquals(0, record.size());
        assertTrue(record.isConsistent());
    }

    @Test
    public void testGetConsistentRecordWithInvalidMapping() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2); // incorrect index
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertFalse(record.isConsistent());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValueWithNullMapping() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("Name");
    }

    @Test
    public void testGetValueWithInvalidName() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertNull(record.get("InvalidName"));
    }

    @Test
    public void testCSVRecordWithComment() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        String comment = "This is a comment";
        CSVRecord record = new CSVRecord(values, mapping, comment, 1);

        assertEquals(comment, record.getComment());
    }

    @Test
    public void testCSVRecordWithNullComment() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertNull(record.getComment());
    }

    @Test
    public void testCSVRecordWithInvalidRecordNumber() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        long recordNumber = -1;
        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);

        assertFalse(record.isConsistent());
    }
}