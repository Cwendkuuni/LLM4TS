package org.apache.commons.csv.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for CSVRecord class.
 */
public class CSVRecordTest {

    private CSVFormat format;
    private CSVParser parser;

    @Before
    public void setUp() {
        format = new CSVFormat();
        parser = new CSVParser(format);
    }

    @Test
    public void testNewInstance() {
        final String[] values = new String[] { "name1", "age1", "gender1" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertArrayEquals(values, record.values());
        assertSame(mapping, record.mapping);
        assertNull(record.comment);
        assertSame(1, record.getRecordNumber());
        assertTrue(record.isConsistent());
    }

    @Test
    public void testGet() {
        final String[] values = new String[] { "name1", "age1", "gender1" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertSame("name1", record.get("name"));
        assertSame("age1", record.get("age"));
        assertSame("gender1", record.get("gender"));
        assertSame("name1", record.get(0));
        assertSame("age1", record.get(1));
        assertSame("gender1", record.get(2));
        assertSame("name1", record.get(Enum.NAME1.toString()));
        assertSame("age1", record.get(Enum.AGE1.toString()));
        assertSame("gender1", record.get(Enum.GENDER1.toString()));
    }

    @Test
    public void testGetComment() {
        final String[] values = new String[] { "name1", "age1", "gender1" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        final CSVRecord record = new CSVRecord(values, mapping, "comment1", 1);

        assertSame("comment1", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        final String[] values = new String[] { "name1", "age1", "gender1" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertSame(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        final String[] values = new String[] { "name1", "age1", "gender1" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());

        mapping.put("unknown", 3);
        assertFalse(record.isConsistent());

        values = new String[] { "name1", "age1", "gender1" };
        mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        final CSVRecord inconsistentRecord = new CSVRecord(values, mapping, null, 1);
        assertFalse(inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        final String[] values = new String[] { "name1", "age1", "gender1" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isMapped("name"));
        assertTrue(record.isMapped("age"));
        assertTrue(record.isMapped("gender"));
        assertFalse(record.isMapped("unknown"));
    }

    @Test
    public void testIsSet() {
        final String[] values = new String[] { "name1", "age1", "gender1" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("name"));
        assertTrue(record.isSet("age"));
        assertTrue(record.isSet("gender"));
        assertFalse(record.isSet("unknown"));

        values = new String[] { "name1", null, "gender1" };
        mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        final CSVRecord recordWithNullValue = new CSVRecord(values, mapping, null, 1);

        assertTrue(recordWithNullValue.isSet("name"));
        assertFalse(recordWithNullValue.isSet("age"));
        assertTrue(recordWithNullValue.isSet("gender"));
    }

    @Test
    public void testGetIterator() {
        final String[] values = new String[] { "name1", "age1", "gender1" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        final Iterator<String> iterator = record.iterator();
        List<String> expectedList = Arrays.asList("name1", "age1", "gender1");

        for (int i = 0; i < 3; i++) {
            assertSame(expectedList.get(i), iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn() {
        final String[] values = new String[] { "name1", "age1", "gender1" };
        final Map<String, String> map = new HashMap<>();

        final CSVRecord record = new CSVRecord(values, null, null, 1);
        map.putAll(record.putIn(map));

        List<String> expectedList = Arrays.asList("name1", "age1", "gender1");
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            assertSame(expectedList.get(Integer.parseInt(entry.getKey().substring(1))), entry.getValue());
        }
    }

    @Test
    public void testSize() {
        final String[] values = new String[] { "name1", "age1", "gender1" };

        final CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        final String[] values = new String[] { "name1", "age1", "gender1" };

        final CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals("[name1, age1, gender1]", record.toString());
    }

    @Test
    public void testValues() {
        final String[] values = new String[] { "name1", "age1", "gender1" };

        final CSVRecord record = new CSVRecord(values, null, null, 1);
        assertArrayEquals(values, record.values());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidIndex() {
        final String[] values = new String[] { "name1", "age1", "gender1" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get(3);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithNoHeaderMapping() {
        final String[] values = new String[] { "name1", "age1", "gender1" };

        final CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("unknown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithUnmappedColumn() {
        final String[] values = new String[] { "name1", "age1", "gender1" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);

        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get("gender");

        mapping.remove("age");
        record.get("age");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetWithInvalidName() {
        final String[] values = new String[] { "name1", "age1", "gender1" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get("unkown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidEnum() {
        final String[] values = new String[] { "name1", "age1", "gender1" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get(Enum.valueOf(String.class, "UnknownEnum"));
    }

    @Test
    public void testPutInWithEmptyMap() {
        final String[] values = new String[] { "name1", "age1", "gender1" };
        Map<String, String> map = new HashMap<>();

        final CSVRecord record = new CSVRecord(values, null, null, 1);
        map.putAll(record.putIn(map));

        assertEquals(3, map.size());
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            assertSame("name1", entry.getKey());
            assertSame("name1", entry.getValue());
        }
    }
}

enum Enum {
    NAME1, AGE1, GENDER1, UNKNOWN_ENUM;
}