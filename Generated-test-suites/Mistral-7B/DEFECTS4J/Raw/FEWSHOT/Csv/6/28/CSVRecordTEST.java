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
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setUp() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        record = new CSVRecord(values, mapping, null, 1L);
    }

    @Test
    public void testGet() {
        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get(1));
        assertEquals("baz", record.get(2));
    }

    @Test
    public void testGetEnum() {
        enum TestEnum { HEADER1, HEADER2, HEADER3 }

        assertEquals("foo", record.get(TestEnum.HEADER1));
        assertEquals("bar", record.get(TestEnum.HEADER2));
        assertEquals("baz", record.get(TestEnum.HEADER3));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get(1));
        assertEquals("baz", record.get(2));
    }

    @Test
    public void testGetByInvalidName() {
        assertNull(record.get("invalidName"));
    }

    @Test
    public void testGetByInvalidIndex() {
        try {
            record.get(-1);
            fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testGetComment() {
        assertNull(record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1L, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));
        assertTrue(record.isMapped("header3"));
        assertFalse(record.isMapped("invalidName"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("header1"));
        assertTrue(record.isSet("header2"));
        assertTrue(record.isSet("header3"));
        assertFalse(record.isSet("invalidName"));
    }

    @Test
    public void testIterator() {
        Iterator<String> iterator = record.iterator();

        List<String> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }

        assertArrayEquals(new String[]{"foo", "bar", "baz"}, result.toArray());
    }

    @Test
    public void testPutIn() {
        Map<String, String> result = new HashMap<>();
        record.putIn(result);

        Map<String, String> expected = new HashMap<>();
        expected.put("header1", "foo");
        expected.put("header2", "bar");
        expected.put("header3", "baz");

        assertArrayEquals(expected.toArray(new HashMap[0].getClass()), result.toArray());
    }

    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        assertEquals("[foo, bar, baz]", record.toString());
    }

    @Test
    public void testValues() {
        assertSame(record.values(), record.values());
    }

    @Test
    public void testEmptyRecord() {
        CSVRecord emptyRecord = new CSVRecord(CSVRecord.EMPTY_STRING_ARRAY, null, null, 1L);

        assertFalse(emptyRecord.isConsistent());
        assertFalse(emptyRecord.isMapped("header1"));
        assertFalse(emptyRecord.isSet("header1"));
        assertFalse(emptyRecord.iterator().hasNext());
        assertArrayEquals(CSVRecord.EMPTY_STRING_ARRAY, emptyRecord.values());
    }

    @Test
    public void testNullMapping() {
        CSVRecord nullMappingRecord = new CSVRecord(new String[]{"foo", "bar", "baz"}, null, null, 1L);

        assertFalse(nullMappingRecord.isConsistent());
        assertNull(nullMappingRecord.get("header1"));
        assertNull(nullMappingRecord.get(0));
        assertNull(nullMappingRecord.get(1));
        assertNull(nullMappingRecord.get(2));
    }

    @Test
    public void testRecordWithInvalidMappingSize() {
        String[] values = new String[]{"foo", "bar", "baz", "qux"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        try {
            new CSVRecord(values, mapping, null, 1L);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testRecordWithInvalidMappingKey() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("invalidKey", 3);

        try {
            new CSVRecord(values, mapping, null, 1L);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testRecordWithInvalidMappingValue() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 3);

        try {
            new CSVRecord(values, mapping, null, 1L);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}