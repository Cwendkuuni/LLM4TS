package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testEmptyCSVRecord() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertEquals(0, record.size());
        assertNull(record.getComment());
        assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecordWithValues() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertEquals(3, record.size());
        assertSame(values, record.values());
        assertNull(record.getComment());
        assertFalse(record.isConsistent());

        String value1 = record.get(0);
        assertEquals("foo", value1);

        String value2 = record.get(1);
        assertEquals("bar", value2);

        String value3 = record.get(2);
        assertEquals("baz", value3);

        assertSame("foo", record.get(0));
        assertSame("bar", record.get(1));
        assertSame("baz", record.get(2));

        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord recordWithMapping = new CSVRecord(values, mapping, null, 0);
        assertEquals(3, recordWithMapping.size());
        assertSame(values, recordWithMapping.values());
        assertNull(recordWithMapping.getComment());
        assertTrue(recordWithMapping.isConsistent());

        String mappedValue1 = recordWithMapping.get("col1");
        assertEquals("foo", mappedValue1);

        String mappedValue2 = recordWithMapping.get("col2");
        assertEquals("bar", mappedValue2);

        String mappedValue3 = recordWithMapping.get("col3");
        assertEquals("baz", mappedValue3);

        assertSame("foo", recordWithMapping.get("col1"));
        assertSame("bar", recordWithMapping.get("col2"));
        assertSame("baz", recordWithMapping.get("col3"));

        assertSame(values, recordWithMapping.values());
        assertSame(mapping, recordWithMapping.mapping);
        assertNull(recordWithMapping.getComment());
        assertTrue(recordWithMapping.isConsistent());

        String comment = "comment";
        CSVRecord recordWithComment = new CSVRecord(values, mapping, comment, 0);
        assertEquals(3, recordWithComment.size());
        assertSame(values, recordWithComment.values());
        assertEquals("comment", recordWithComment.getComment());
        assertTrue(recordWithComment.isConsistent());

        long recordNumber = 123;
        CSVRecord recordWithNumber = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(3, recordWithNumber.size());
        assertSame(values, recordWithNumber.values());
        assertEquals("comment", recordWithNumber.getComment());
        assertTrue(recordWithNumber.isConsistent());
        assertEquals(recordNumber, recordWithNumber.getRecordNumber());
    }

    @Test
    public void testGetNullValue() {
        String[] values = new String[]{"foo", null, "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertSame("", record.get(1));
        assertNull(record.get("col1"));
        assertNull(record.get("col2"));
    }

    @Test
    public void testGetValueByNameWithNoMapping() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertThrows(IllegalStateException.class, () -> record.get("col1"));
    }

    @Test
    public void testGetValueByNameWithInvalidIndex() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertThrows(IllegalArgumentException.class, () -> record.get(-1));
        assertThrows(IllegalArgumentException.class, () -> record.get(values.length));
    }

    @Test
    public void testGetValueByNameWithInvalidMapping() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertThrows(IllegalArgumentException.class, () -> record.get("col4"));
    }

    @Test
    public void testGetValueByNameWithMissingValue() {
        String[] values = new String[]{"foo", null, "bar"};
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertNull(record.get("col4"));
    }

    @Test
    public void testPutAllInMap() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, String> map = new HashMap<String, String>();
        map = values[0].toString().equals("") ? map : CSVRecord.util().putIn(map);
        for (int i = 0; i < values.length; i++) {
            map.put(CSVRecord.DEFAULT_HEADER_NAME + i, values[i]);
        }
        CSVRecord record = new CSVRecord(values, null, null, 0);
        Map<String, String> mapFromRecord = record.toMap();
        assertArrayEquals(map.toArray(new Map[0]), mapFromRecord.toArray(new Map[0]));
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        String expected = "[foo, bar, baz]";
        assertEquals(expected, record.toString());
    }
}