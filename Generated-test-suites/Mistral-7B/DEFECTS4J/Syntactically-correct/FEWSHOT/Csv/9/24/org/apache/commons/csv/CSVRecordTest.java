package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertIterableEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testConstructor_StringArray_Map_String_Long() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        String comment = "This is a comment";
        long recordNumber = 123;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertEquals(comment, record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
        assertTrue(record.isConsistent());
    }

    @Test
    public void testConstructor_StringArray_NullMap_Null_Null() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = null;
        String comment = null;
        long recordNumber = 0;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(values, record.values());
        assertNull(record.mapping);
        assertNull(record.getComment());
        assertEquals(0, record.getRecordNumber());
        assertTrue(record.isConsistent());
    }

    @Test
    public void testGet_Enum() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        String result = record.get(Enum.valueOf(CSVRecordTest.class, "HEADER_ONE"));
        assertEquals("value1", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_NullEnum() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get(null);
    }

    @Test
    public void testGet_Int() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        String result = record.get(0);
        assertEquals("header1", result);
    }

    @Test
    public void testGet_String() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        String result = record.get("header2");
        assertEquals("value2", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGet_String_NoMapping() {
        String[] values = {"header1", "value1", "value2"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        record.get("header2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_String_MappedButNullValue() {
        String[] values = {"header1", "value1", null};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get("header2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_String_InconsistentRecord() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get("header2");
    }

    @Test
    public void testGetComment() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);

        CSVRecord record = new CSVRecord(values, mapping, "This is a comment", 1);

        String result = record.getComment();
        assertEquals("This is a comment", result);
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        long result = record.getRecordNumber();
        assertEquals(1, result);
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistent_MismatchingSize() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.values = new String[2];

        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));
        assertFalse(record.isMapped("header3"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("header1"));
        assertTrue(record.isSet("header2"));
        assertFalse(record.isSet("header3"));
    }

    @Test
    public void testIterator() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        List<String> expected = Arrays.asList("header1", "value1", "value2");

        Iterator<String> iterator = record.iterator();

        assertIterableEquals(expected, iterator);
    }

    @Test
    public void testPutIn() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, String> target = new HashMap<>();

        CSVRecord record = new CSVRecord(values, null, null, 1);

        Map<String, String> result = record.putIn(target);

        assertEquals(values.length, result.size());
        assertArrayEquals(values, result.values().toArray(new String[0]));
    }

    @Test
    public void testSize() {
        String[] values = {"header1", "value1", "value2"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        int result = record.size();

        assertEquals(values.length, result);
    }

    @Test
    public void testToList() {
        String[] values = {"header1", "value1", "value2"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        List<String> result = record.toList();

        assertArrayEquals(values, result.toArray(new String[0]));
    }

    @Test
    public void testToMap() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Map<String, String> result = record.toMap();

        assertEquals(mapping.size(), result.size());
        for (Map.Entry<String, Integer> entry : mapping.entrySet()) {
            Function<Map.Entry<String, Integer>, Map.Entry<String, String>> mapper = e -> Map.entry(e.getKey(), e.getValue()
                    .toString());
            Map.Entry<String, String> expected = mapper.apply(entry);
            Map.Entry<String, String> actual = result.entrySet().iterator().next();
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testToString() {
        String[] values = {"header1", "value1", "value2"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        String result = record.toString();

        assertEquals("[header1, value1, value2]", result);
    }
}