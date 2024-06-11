package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setup() {
        String[] values = new String[] {"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        record = new CSVRecord(values, mapping, null, 1L);
    }

    @Test
    public void testGetEnumValue() {
        assertEquals("name1", record.get(Enum.valueOf(CSVRecord.class, "NAME1")));
    }

    @Test
    public void testGetValueByIndex() {
        assertEquals("value1", record.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueByNegativeIndex() {
        record.get(-1);
    }

    @Test
    public void testGetValueByName() {
        assertEquals("value1", record.get("name1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueByNonExistentName() {
        record.get("name3");
    }

    @Test
    public void testGetComment() {
        assertNull(record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("name1"));
        assertFalse(record.isMapped("name3"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("name1"));
        assertFalse(record.isSet("name3"));
    }

    @Test
    public void testGetIterator() {
        Iterator<String> it = record.iterator();
        assertSame(record.values(), it.toArray());
    }

    @Test
    public void testPutInMap() {
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        assertEquals(3, map.size());
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
        assertNull(map.get("name3"));
    }

    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        List<String> list = record.toList();
        assertArrayEquals(record.values(), list.toArray());
    }

    @Test
    public void testToMap() {
        Map<String, String> map = record.toMap();
        assertEquals(3, map.size());
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
        assertNull(map.get("name3"));
    }

    @Test
    public void testToString() {
        assertEquals("[name1, value1, value2]", record.toString());
    }

    @Test
    public void testValues() {
        assertArrayEquals(record.values(), record.values());
    }

}