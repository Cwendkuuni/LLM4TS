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
    public void testNewInstance() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertArrayEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertNull(record.comment);
        assertEquals(0, record.recordNumber);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testGetValueByEnum() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        Enum<String> enum1 = new Enum<String>("header1") {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString() {
                return "header1";
            }
        };

        assertEquals("foo", record.get(enum1));

        Enum<String> enum2 = new Enum<String>("header4") {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString() {
                return "header4";
            }
        };

        assertThrows(IllegalArgumentException.class, () -> record.get(enum2));
    }

    @Test
    public void testGetValueByIndex() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], record.get(i));
        }
    }

    @Test
    public void testGetValueByName() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        for (Map.Entry<String, Integer> entry : mapping.entrySet()) {
            assertEquals(entry.getValue().intValue(), record.get(entry.getKey()));
        }
    }

    @Test
    public void testGetValueByNameNullMapping() {
        String[] values = new String[]{"foo", "bar", "baz"};

        CSVRecord record = new CSVRecord(values, null, null, 0);

        assertThrows(IllegalStateException.class, record::get);
    }

    @Test
    public void testGetValueByNameUnknownColumn() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertThrows(IllegalArgumentException.class, () -> record.get("header4"));
    }

    @Test
    public void testGetValueByNameInconsistentRecord() {
        String[] values = new String[1];
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertThrows(IllegalArgumentException.class, () -> record.get("header1"));
    }

    @Test
    public void testGetComment() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        String comment = "comment";
        CSVRecord record = new CSVRecord(values, mapping, comment, 0);

        assertEquals(comment, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[1];
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertFalse(record.isConsistent());

        String[] values2 = new String[]{"foo", "bar", "baz"};

        CSVRecord record2 = new CSVRecord(values2, mapping, null, 0);

        assertTrue(record2.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));
        assertTrue(record.isMapped("header3"));
        assertFalse(record.isMapped("header4"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertTrue(record.isSet("header1"));
        assertTrue(record.isSet("header2"));
        assertTrue(record.isSet("header3"));
        assertFalse(record.isSet("header4"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        Iterator<String> iterator = record.iterator();

        List<String> expected = Arrays.asList("foo", "bar", "baz");

        List<String> actual = new ArrayList<>();

        while (iterator.hasNext()) {
            actual.add(iterator.next());
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testPutIn() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, String> expected = new HashMap<>();
        expected.put("header1", "foo");
        expected.put("header2", "bar");
        expected.put("header3", "baz");

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        Map<String, String> result = record.putIn(new HashMap<>());

        assertEquals(expected, result);
    }

    @Test
    public void testSize() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals(values.length, record.size());
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        String expected = "[foo, bar, baz]";
        String actual = record.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testValues() {
        String[] values = new String[]{"foo", "bar", "baz"};

        CSVRecord record = new CSVRecord(values, null, null, 0);

        assertSame(values, record.values());
    }
}