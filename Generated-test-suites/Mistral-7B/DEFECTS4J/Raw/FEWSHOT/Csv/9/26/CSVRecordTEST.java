package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testNewCSVRecord() {
        String[] values = new String[] {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertNull(record.comment);
        assertEquals(0, record.recordNumber);
        assertTrue(record.isConsistent());
        assertFalse(record.isMapped("nonExistentHeader"));

        String value1 = record.get("header1");
        assertEquals("value1", value1);
        String value2 = record.get("header2");
        assertEquals("value2", value2);
        assertNull(record.get("nonExistentHeader"));
        assertSame(0, record.getRecordNumber());
    }

    @Test
    public void testNewCSVRecordWithNullMapping() {
        String[] values = new String[] {"value1", "value2"};

        CSVRecord record = new CSVRecord(values, null, null, 0);

        assertEquals(values, record.values());
        assertNull(record.mapping);
        assertNull(record.comment);
        assertEquals(0, record.recordNumber);
        assertTrue(record.isConsistent());
        assertFalse(record.isMapped("header1"));

        try {
            record.get("header1");
            fail("Should have thrown an IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testNewCSVRecordWithInconsistentSize() {
        String[] values = new String[] {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertNull(record.comment);
        assertEquals(0, record.recordNumber);
        assertFalse(record.isConsistent());

        try {
            record.get("header3");
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNewCSVRecordWithEmptyValues() {
        String[] values = new String[0];
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertNull(record.comment);
        assertEquals(0, record.recordNumber);
        assertTrue(record.isConsistent());
        assertFalse(record.isMapped("header1"));
    }

    @Test
    public void testNewCSVRecordWithNullValues() {
        String[] values = null;
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);

        try {
            CSVRecord record = new CSVRecord(values, mapping, null, 0);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNewCSVRecordWithNullMapping() {
        String[] values = new String[] {"value1", "value2"};

        try {
            CSVRecord record = new CSVRecord(values, null, null, 0);
            fail("Should have thrown an IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testGetValueByEnum() {
        String[] values = new String[] {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals("value1", record.get(Header1.HEADER1));
        assertEquals("value2", record.get(Header1.HEADER2));
    }

    @Test
    public void testGetValueByIndex() {
        String[] values = new String[] {"value1", "value2"};

        CSVRecord record = new CSVRecord(values, null, null, 0);

        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetValueByName() {
        String[] values = new String[] {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals("value1", record.get("header1"));
        assertEquals("value2", record.get("header2"));
        assertNull(record.get("nonExistentHeader"));
    }

    @Test
    public void testGetComment() {
        String[] values = new String[] {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        String comment = "This is a comment.";

        CSVRecord record = new CSVRecord(values, mapping, comment, 0);

        assertEquals(comment, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = new String[] {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record1 = new CSVRecord(values, mapping, null, 0);
        CSVRecord record2 = new CSVRecord(values, mapping, null, 1);

        assertEquals(0, record1.getRecordNumber());
        assertEquals(1, record2.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[] {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertTrue(record.isConsistent());

        mapping.put("nonExistentHeader", 2);

        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[] {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));
        assertFalse(record.isMapped("nonExistentHeader"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[] {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertTrue(record.isSet("header1"));
        assertTrue(record.isSet("header2"));
        assertFalse(record.isSet("nonExistentHeader"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[] {"value1", "value2"};

        CSVRecord record = new CSVRecord(values, null, null, 0);

        List<String> valuesList = record.toList();

        Iterator<String> iterator = record.iterator();

        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], iterator.next());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn() {
        String[] values = new String[] {"value1", "value2"};
        Map<String, String> map = new HashMap<>();

        CSVRecord record = new CSVRecord(values, null, null, 0);

        map.putAll(record.putIn(map));

        for (Map.Entry<String, String> entry : map.entrySet()) {
            assertEquals(entry.getValue(), values[entry.getKey().intValue()]);
        }
    }

    @Test
    public void testSize() {
        String[] values = new String[] {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals(values.length, record.size());
    }

    @Test
    public void testToString() {
        String[] values = new String[] {"value1", "value2"};

        CSVRecord record = new CSVRecord(values, null, null, 0);

        assertEquals("[value1, value2]", record.toString());
    }
}