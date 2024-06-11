package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link CSVRecord}.
 *
 * @author [Your Name]
 */
public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setup() {
        String[] values = new String[]{"Name1", "Value1", "Value2", "Value3", null};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name1", 0);
        mapping.put("Name2", 1);
        mapping.put("Name3", 2);
        record = new CSVRecord(values, mapping, null, 1);
    }

    @Test
    public void testGetEnum() {
        Enum<String> enumConstant = Enum.valueOf(String.class, "Name1");
        String result = record.get(enumConstant);
        assertEquals("Name1", result);
    }

    @Test
    public void testGetInt() {
        String result = record.get(0);
        assertEquals("Name1", result);
    }

    @Test
    public void testGetString() {
        String result = record.get("Name1");
        assertEquals("Name1", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testNoHeaderMapping() {
        record = new CSVRecord(new String[0], null, null, 1);
        record.get("Name1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name1", 10);
        record = new CSVRecord(new String[]{"Value1"}, mapping, null, 1);
        record.get("Name1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInconsistentRecord() {
        String[] values = new String[]{"Name1", "Value1", "Value2", null};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name1", 0);
        mapping.put("Name2", 1);
        mapping.put("Name3", 2);
        record = new CSVRecord(values, mapping, null, 1);
        values = new String[]{"Name1", "Value1", "Value2"};
        try {
            record.get("Name4");
        } catch (IllegalArgumentException e) {
            fail("Expected IllegalArgumentException with message containing 'Name4'", e);
        }
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
        assertTrue(record.isMapped("Name1"));
        assertFalse(record.isMapped("Name4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("Name1"));
        assertFalse(record.isSet("Name4"));
    }

    @Test
    public void testIterator() {
        List<String> expectedList = Arrays.asList("Name1", "Value1", "Value2", "Value3", null);
        Iterator<String> iterator = record.iterator();
        List<String> actualList = Arrays.asList(iterator.next(), iterator.next(), iterator.next(),
                iterator.next(), iterator.next());
        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        List<Map.Entry<String, String>> entries = Arrays.asList(map.entrySet().iterator().next(),
                map.entrySet().iterator().next(), map.entrySet().iterator().next(),
                map.entrySet().iterator().next(), map.entrySet().iterator().next());
        List<Entry<String, Integer>> recordEntries = Arrays.asList(mapping.entrySet().iterator().next(),
                mapping.entrySet().iterator().next(), mapping.entrySet().iterator().next(),
                mapping.entrySet().iterator().next(), mapping.entrySet().iterator().next());
        for (int i = 0; i < entries.size(); i++) {
            assertEquals(recordEntries.get(i).getKey(), entries.get(i).getKey());
            assertEquals(record.get(i), entries.get(i).getValue());
        }
    }

    @Test
    public void testSize() {
        assertEquals(5, record.size());
    }

    @Test
    public void testToList() {
        List<String> result = record.toList();
        List<String> expectedList = Arrays.asList("Name1", "Value1", "Value2", "Value3", null);
        assertArrayEquals(expectedList.toArray(), result.toArray());
    }

    @Test
    public void testToMap() {
        Map<String, String> result = record.toMap();
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Name1", "Value1");
        expectedMap.put("Name2", "Value2");
        expectedMap.put("Name3", "Value3");
        assertEquals(expectedMap, result);
    }

    @Test
    public void testToString() {
        assertEquals("[Name1, Value1, Value2, Value3, null]", record.toString());
    }

    @Test
    public void testValues() {
        String[] result = record.values();
        assertArrayEquals(record.values(), result);
    }

}