package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithValidValuesAndMapping() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
        assertArrayEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertNull(record.comment);
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertFalse(record.isSet("ZipCode"));
        assertFalse(record.isSet("InvalidColumnName"));
        assertEquals("Name", record.get(0));
        assertEquals("Age", record.get(1));
        assertEquals("City", record.get(2));
        assertEquals("City", record.get("City"));
        assertEquals("Age", record.get("Age"));
        assertEquals(null, record.get("ZipCode"));
        assertEquals(null, record.get("InvalidColumnName"));
        assertArrayEquals(Arrays.asList("Name", "Age", "City").toArray(), record.iterator().toArray());
    }

    @Test
    public void testCSVRecordWithNullValuesAndMapping() {
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(null, mapping, null, 1);

        assertNull(record.values());
        assertSame(mapping, record.mapping);
        assertNull(record.comment);
        assertTrue(record.isConsistent());
        assertFalse(record.isMapped("Name"));
        assertFalse(record.isSet("Name"));
        assertSame(0, record.size());
    }

    @Test
    public void testCSVRecordWithNullMapping() {
        String[] values = {"Name", "Age", "City"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertArrayEquals(values, record.values());
        assertNull(record.mapping);
        assertNull(record.comment);
        assertTrue(record.isConsistent());
        assertFalse(record.isMapped("Name"));
        assertFalse(record.isSet("Name"));
        assertEquals(3, record.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithNoMapping() {
        String[] values = {"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        record.get("InvalidColumnName");
    }

    @Test
    public void testCSVRecordWithEmptyValues() {
        String[] values = {};
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertArrayEquals(values, record.values());
        assertSame(mapping, record.mapping);
        assertNull(record.comment);
        assertTrue(record.isConsistent());
        assertFalse(record.isMapped("Name"));
        assertFalse(record.isSet("Name"));
        assertEquals(0, record.size());
    }

    @Test
    public void testCSVRecordWithEmptyMapping() {
        String[] values = {"Name", "Age", "City"};

        CSVRecord record = new CSVRecord(values, new HashMap<>(), null, 1);

        assertArrayEquals(values, record.values());
        assertNull(record.mapping);
        assertNull(record.comment);
        assertTrue(record.isConsistent());
        assertFalse(record.isMapped("Name"));
        assertFalse(record.isSet("Name"));
        assertEquals(3, record.size());
    }

    @Test
    public void testCSVRecordWithEmptyValuesAndEmptyMapping() {
        CSVRecord record = new CSVRecord(new String[0], new HashMap<>(), null, 1);

        assertArrayEquals(new String[0], record.values());
        assertNull(record.mapping);
        assertNull(record.comment);
        assertTrue(record.isConsistent());
        assertFalse(record.isMapped("Name"));
        assertFalse(record.isSet("Name"));
        assertEquals(0, record.size());
    }

    @Test
    public void testCSVRecordWithInvalidMappingSize() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 4);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertArrayEquals(values, record.values());
        assertSame(mapping, record.mapping);
        assertNull(record.comment);
        assertFalse(record.isConsistent());
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertFalse(record.isSet("City"));
        assertEquals(3, record.size());
    }

    @Test
    public void testCSVRecordWithEmptyComment() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, "", 1);

        assertEquals("", record.getComment());
    }

    @Test
    public void testCSVRecordWithComment() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, "Comment", 1);

        assertEquals("Comment", record.getComment());
    }

    @Test
    public void testCSVRecordGetWithInvalidIndex() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertThrows(IndexOutOfBoundsException.class, () -> record.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> record.get(values.length));
    }

    @Test
    public void testCSVRecordGetWithInvalidName() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertThrows(IllegalArgumentException.class, () -> record.get("InvalidColumnName"));
    }

    @Test
    public void testCSVRecordGetWithEmptyMapping() {
        String[] values = {"Name", "Age", "City"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertThrows(IllegalStateException.class, () -> record.get("Name"));
    }

    @Test
    public void testCSVRecordGetWithNullMapping() {
        String[] values = {"Name", "Age", "City"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertThrows(IllegalStateException.class, () -> record.get("Name"));
    }
}