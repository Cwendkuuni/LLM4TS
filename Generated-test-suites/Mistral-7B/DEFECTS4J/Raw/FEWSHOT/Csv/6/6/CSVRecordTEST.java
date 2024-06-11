package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setUp() {
        String[] values = new String[]{"name", "age", "city"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("city", 2);

        record = new CSVRecord(values, mapping, null, 0);
    }

    @Test
    public void testGet() {
        assertEquals("name", record.get(0));
        assertEquals("age", record.get(1));
        assertEquals("city", record.get(2));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNoMapping() {
        record = new CSVRecord(new String[0], null, null, 0);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidIndex() {
        record = new CSVRecord(new String[]{"name", "age", "city"}, null, null, 0);
        record.get(3);
    }

    @Test
    public void testGetEmpty() {
        record = new CSVRecord(new String[0], null, null, 0);
        assertNull(record.get("name"));
    }

    @Test
    public void testGetComment() {
        record = new CSVRecord(new String[]{"name", "age", "city"}, null, "comment", 0);
        assertEquals("comment", record.getComment());
        assertNull(record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(0, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("name"));
        assertFalse(record.isMapped("unknown"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("name"));
        assertFalse(record.isSet("unknown"));
    }

    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        assertEquals("[name, age, city]", record.toString());
    }

    @Test
    public void testValues() {
        assertSame(record.values(), record.values());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNullMapping() {
        record = new CSVRecord(new String[]{"name", "age", "city"}, null, null, 0);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidName() {
        record = new CSVRecord(new String[]{"name", "age", "city"}, null, null, 0);
        record.get((String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidIndexNegative() {
        record = new CSVRecord(new String[]{"name", "age", "city"}, null, null, 0);
        record.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidIndexOutOfBounds() {
        record = new CSVRecord(new String[]{"name", "age", "city"}, null, null, 0);
        record.get(3);
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        List<Entry<String, Integer>> entryList = new ArrayList<>(mapping.entrySet());
        for (Entry<String, Integer> entry : entryList) {
            assertEquals(entry.getKey(), map.get(entry.getKey()));
        }
    }
}