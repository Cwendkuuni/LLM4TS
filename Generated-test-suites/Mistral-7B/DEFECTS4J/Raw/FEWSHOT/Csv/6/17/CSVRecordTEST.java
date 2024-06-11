package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecord() {
        String[] values = new String[]{"Col1", "Col2", "Col3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals(1, record.getRecordNumber());
        assertEquals(3, record.size());
        assertSame(values, record.values());
        assertEquals("Col1", record.get(0));
        assertEquals("Col2", record.get(1));
        assertEquals("Col3", record.get(2));
        assertEquals("Col1", record.get("header1"));
        assertEquals("Col2", record.get("header2"));
        assertEquals("Col3", record.get("header3"));
        assertNull(record.get("nonExistentHeader"));
        assertNull(record.getComment());
        assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecordWithNullMapping() {
        String[] values = new String[]{"Col1", "Col2", "Col3"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        assertEquals(1, record.getRecordNumber());
        assertEquals(3, record.size());
        assertNull(record.getComment());
        assertFalse(record.isConsistent());
        try {
            record.get("header1");
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testCSVRecordWithNullValues() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(null, mapping, null, 1L);
        assertEquals(1, record.getRecordNumber());
        assertTrue(Arrays.equals(new String[0], record.values()));
        assertFalse(record.isConsistent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordWithInvalidIndex() {
        String[] values = new String[]{"Col1", "Col2", "Col3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        record.get(3);
    }

    @Test
    public void testCSVRecordWithEmptyMap() {
        String[] values = new String[]{"Col1", "Col2", "Col3"};
        CSVRecord record = new CSVRecord(values, new HashMap<>(), null, 1L);
        assertEquals(1, record.getRecordNumber());
        assertTrue(Arrays.equals(new String[0], record.values()));
        assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecordWithMissingValue() {
        String[] values = new String[]{"Col1", "Col2", "Col3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertFalse(record.isSet("header4"));
    }

    @Test
    public void testCSVRecordWithComment() {
        String[] values = new String[]{"Col1", "Col2", "Col3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        String comment = "This is a comment.";
        CSVRecord record = new CSVRecord(values, mapping, comment, 1L);
        assertEquals(1, record.getRecordNumber());
        assertEquals("This is a comment.", record.getComment());
    }

    @Test
    public void testCSVRecordIterator() {
        String[] values = new String[]{"Col1", "Col2", "Col3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        Iterator<String> iterator = record.iterator();
        List<String> list = Arrays.asList(record.values());
        int i = 0;
        while (iterator.hasNext()) {
            assertEquals(list.get(i), iterator.next());
            i++;
        }
    }

    @Test
    public void testCSVRecordToString() {
        String[] values = new String[]{"Col1", "Col2", "Col3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals("[Col1, Col2, Col3]", record.toString());
    }

    @Test
    public void testCSVRecordPutIn() {
        String[] values = new String[]{"Col1", "Col2", "Col3"};
        Map<String, String> result = new HashMap<>();
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        record.putIn(result);
        assertEquals("Col1", result.get("header1"));
        assertEquals("Col2", result.get("header2"));
        assertEquals("Col3", result.get("header3"));
    }
}