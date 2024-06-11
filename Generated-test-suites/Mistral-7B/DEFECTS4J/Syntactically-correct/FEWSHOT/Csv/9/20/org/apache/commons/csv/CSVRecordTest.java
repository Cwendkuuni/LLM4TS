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
import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(Parameterized.class)
public class CSVRecordTest {

    private final String[] values;
    private final Map<String, Integer> mapping;
    private final String comment;
    private final long recordNumber;

    public CSVRecordTest(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) {
        this.values = values;
        this.mapping = mapping;
        this.comment = comment;
        this.recordNumber = recordNumber;
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {new String[]{"name", "age"}, null, null, 1},
                {new String[]{"John Doe", "30"}, new HashMap<String, Integer>() {{
                        put("name", 0);
                        put("age", 1);
                    }}, null, 1},
                {new String[]{"name", "John Doe", "age", "30"}, new HashMap<String, Integer>() {{
                        put("name", 0);
                        put("age", 2);
                    }}, null, 1},
                {new String[]{"name", "John Doe", "age", "30", ""}, new HashMap<String, Integer>() {{
                        put("name", 0);
                        put("age", 3);
                    }}, null, 1},
                {new String[]{}, null, null, 1},
                {new String[]{"name", ""}, null, null, 1},
                {new String[]{"name", "John Doe", "age", "30"}, new HashMap<String, Integer>() {{
                        put("name", 0);
                        put("age", 2);
                    }}, "comment", 1},
                {new String[]{"name", "John Doe", "age", "30"}, null, "comment", 1},
                {new String[]{"name", "John Doe", "age", "30"}, new HashMap<String, Integer>() {{
                        put("name", 0);
                        put("age", 2);
                    }}, null, -1},
                {new String[]{"name", "John Doe", "age", "30"}, null, null, -1}
        });
    }

    @Test
    public void testCSVRecord() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertEquals(comment, record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testGet() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], record.get(i));
        }
        for (String name : mapping.keySet()) {
            assertEquals(values[mapping.get(name).intValue()], record.get(name));
        }
        try {
            record.get(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertTrue(record.isConsistent());
        Map<String, Integer> mapping1 = new HashMap<String, Integer>(mapping);
        mapping1.remove("name");
        CSVRecord record1 = new CSVRecord(values, mapping1, comment, recordNumber);
        assertFalse(record1.isConsistent());
        String[] values1 = Arrays.copyOf(values, values.length + 1);
        CSVRecord record2 = new CSVRecord(values1, mapping, comment, recordNumber);
        assertFalse(record2.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        for (String name : mapping.keySet()) {
            assertTrue(record.isMapped(name));
        }
        assertFalse(record.isMapped(""));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        for (String name : mapping.keySet()) {
            assertTrue(record.isSet(name));
        }
        assertFalse(record.isSet(""));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        List<String> list = record.toList();
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], record.iterator().next());
            assertEquals(values[i], list.get(i));
        }
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new ConcurrentHashMap<>(values.length);
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        Map<String, String> map1 = record.putIn(map);
        for (String name : mapping.keySet()) {
            assertEquals(values[mapping.get(name).intValue()], map1.get(name));
        }
        assertEquals(map1, map);
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(values.length, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        List<String> list = record.toList();
        assertEquals(values.length, list.size());
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], list.get(i));
        }
    }

    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        Map<String, String> map = record.toMap();
        for (Entry<String, Integer> entry : mapping.entrySet()) {
            assertEquals(values[entry.getValue().intValue()], map.get(entry.getKey()));
        }
        assertEquals(mapping.size(), map.size());
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("[name, John Doe, age, 30]", record.toString());
    }
}