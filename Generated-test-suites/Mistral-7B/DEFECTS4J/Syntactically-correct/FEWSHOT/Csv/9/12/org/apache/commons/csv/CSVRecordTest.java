package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testNewInstance() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("col1", 0);
            put("col2", 1);
            put("col3", 2);
        }};
        String comment = "This is a comment.";
        long recordNumber = 12345L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(recordNumber, record.getRecordNumber());
        assertArrayEquals(values, record.values());
        assertSame(mapping, record.mapping);
        assertNull(record.comment);
    }

    @Test
    public void testGetEnumValue() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("col1", 0);
            put("col2", 1);
            put("col3", 2);
        }};
        String comment = "This is a comment.";
        long recordNumber = 12345L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Enum<String> enumValue = Enum.valueOf(String.class, "col1");
        String value = record.get(enumValue);
        assertEquals("foo", value);
    }

    @Test
    public void testGetValueByIndex() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("col1", 0);
            put("col2", 1);
            put("col3", 2);
        }};
        String comment = "This is a comment.";
        long recordNumber = 12345L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get(1));
        assertEquals("baz", record.get(2));
    }

    @Test
    public void testGetValueByName() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("col1", 0);
            put("col2", 1);
            put("col3", 2);
        }};
        String comment = "This is a comment.";
        long recordNumber = 12345L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        String value = record.get("col1");
        assertEquals("foo", value);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValueByNameNullMapping() {
        String[] values = new String[]{"foo", "bar", "baz"};
        long recordNumber = 12345L;

        CSVRecord record = new CSVRecord(values, null, null, recordNumber);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueByNameUnknownColumn() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("col1", 0);
            put("col2", 1);
        }};
        String comment = "This is a comment.";
        long recordNumber = 12345L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        String value = record.get("col3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueByInvalidIndex() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("col1", 0);
            put("col2", 1);
            put("col3", 2);
        }};
        String comment = "This is a comment.";
        long recordNumber = 12345L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        String value = record.get(-1);
    }

    @Test
    public void testGetComment() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("col1", 0);
            put("col2", 1);
            put("col3", 2);
        }};
        String comment = "This is a comment.";
        long recordNumber = 12345L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("This is a comment.", record.getComment());
        assertNull(record.getComment());
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("col1", 0);
            put("col2", 1);
            put("col3", 2);
        }};

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isConsistent());

        mapping = new HashMap<String, Integer>() {{
            put("col1", 0);
            put("col2", 1);
            put("col3", 3);
        }};

        record = new CSVRecord(values, mapping, null, 0);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("col1", 0);
            put("col2", 1);
            put("col3", 2);
        }};

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertTrue(record.isMapped("col1"));
        assertTrue(record.isMapped("col2"));
        assertTrue(record.isMapped("col3"));
        assertFalse(record.isMapped("unknown"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"foo", null, "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("col1", 0);
            put("col2", 1);
            put("col3", 2);
        }};

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertTrue(record.isSet("col1"));
        assertFalse(record.isSet("col2"));
        assertFalse(record.isSet("col3"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("col1", 0);
            put("col2", 1);
            put("col3", 2);
        }};

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        List<String> list = new ArrayList<String>(Arrays.asList(values));

        Iterator<String> it = record.iterator();
        for (int i = 0; i < record.size(); i++) {
            assertEquals(list.get(i), it.next());
        }
        assertFalse(it.hasNext());
    }

    @Test
    public void testPutIn() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, String> map = new HashMap<String, String>();

        CSVRecord record = new CSVRecord(values, null, null, 0);

        Map<String, String> expectedMap = new HashMap<String, String>() {{
            put("col1", "foo");
            put("col2", "bar");
            put("col3", "baz");
        }};

        map = record.putIn(map);

        assertEquals(expectedMap, map);
    }

    @Test
    public void testSize() {
        String[] values = new String[]{"foo", "bar", "baz"};

        CSVRecord record = new CSVRecord(values, null, null, 0);

        assertEquals(values.length, record.size());
    }

    @Test
    public void testToList() {
        String[] values = new String[]{"foo", "bar", "baz"};

        CSVRecord record = new CSVRecord(values, null, null, 0);

        List<String> list = record.toList();

        assertEquals(Arrays.asList(values), list);
    }

    @Test
    public void testToMap() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("col1", 0);
            put("col2", 1);
            put("col3", 2);
        }};

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        Map<String, String> expectedMap = new HashMap<String, String>() {{
            put("col1", "foo");
            put("col2", "bar");
            put("col3", "baz");
        }};

        Map<String, String> map = record.toMap();

        assertEquals(expectedMap, map);
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"foo", "bar", "baz"};

        CSVRecord record = new CSVRecord(values, null, null, 0);

        String expected = "[foo, bar, baz]";

        assertEquals(expected, record.toString());
    }

    @Test
    public void testValues() {
        String[] values = new String[]{"foo", "bar", "baz"};

        CSVRecord record = new CSVRecord(values, null, null, 0);

        String[] expectedValues = new String[]{"foo", "bar", "baz"};

        assertArrayEquals(expectedValues, record.values());
    }
}