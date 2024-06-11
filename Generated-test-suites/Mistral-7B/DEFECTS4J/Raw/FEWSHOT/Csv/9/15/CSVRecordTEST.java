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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testIsConsistentWithEmptyMappingAndValues() {
        CSVRecord csvRecord = new CSVRecord(new String[0], null, null, 0);
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistentWithEmptyMappingAndValuesWithRecordNumber() {
        CSVRecord csvRecord = new CSVRecord(new String[0], null, null, 1);
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistentWithMappingAndEmptyValues() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        CSVRecord csvRecord = new CSVRecord(new String[0], mapping, null, 0);
        assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistentWithMappingAndValues() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        String[] values = new String[]{"ValueA", "ValueB"};
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 0);
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistentWithMappingAndValuesWithNullValues() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        String[] values = new String[]{null, "ValueB"};
        assertFalse(new CSVRecord(values, mapping, null, 0).isConsistent());
    }

    @Test
    public void testIsConsistentWithMappingAndValuesWithValuesLengthGreaterThanMappingSize() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);
        String[] values = new String[]{"ValueA", "ValueB", "ValueC"};
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 0);
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistentWithMappingAndValuesWithValuesLengthLessThanMappingSize() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);
        String[] values = new String[]{"ValueA"};
        assertFalse(new CSVRecord(values, mapping, null, 0).isConsistent());
    }

    @Test
    public void testIsConsistentWithEmptyMappingAndValuesWithRecordNumber() {
        CSVRecord csvRecord = new CSVRecord(new String[0], null, null, 1);
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistentWithMappingAndValuesWithRecordNumber() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        String[] values = new String[]{"ValueA", "ValueB"};
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testGetWithNullMapping() {
        assertNull(new CSVRecord(new String[0], null, null, 0).get("A"));
    }

    @Test
    public void testGetWithEmptyMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord csvRecord = new CSVRecord(new String[]{"ValueA"}, mapping, null, 0);
        assertNull(csvRecord.get("B"));
        assertEquals("ValueA", csvRecord.get("A"));
    }

    @Test
    public void testGetWithMappingAndValuesMatching() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        String[] values = new String[]{"ValueA", "ValueB"};
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 0);
        assertEquals("ValueB", csvRecord.get("B"));
    }

    @Test
    public void testGetWithMappingAndValuesMismatching() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        String[] values = new String[]{"ValueA", "ValueC"};
        assertThrows(IllegalArgumentException.class, () -> new CSVRecord(values, mapping, null, 0).get("B"));
    }

    @Test
    public void testGetWithEmptyMappingAndValues() {
        assertNull(new CSVRecord(new String[0], null, null, 0).get("A"));
    }

    @Test
    public void testGetEnumWithNullMapping() {
        assertNull(new CSVRecord(new String[0], null, null, 0).get(Enum.class, "A"));
    }

    @Test
    public void testGetEnumWithEmptyMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord csvRecord = new CSVRecord(new String[]{"ValueA"}, mapping, null, 0);
        assertNull(csvRecord.get(Enum.class, "B"));
        assertEquals("ValueA", csvRecord.get(Enum.class, "A"));
    }

    @Test
    public void testGetEnumWithMappingAndValuesMatching() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        String[] values = new String[]{"ValueA", "ValueB"};
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 0);
        assertEquals("ValueB", csvRecord.get(Enum.class, "B"));
    }

    @Test
    public void testGetEnumWithMappingAndValuesMismatching() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        String[] values = new String[]{"ValueA", "ValueC"};
        assertThrows(IllegalArgumentException.class, () -> new CSVRecord(values, mapping, null, 0).get(Enum.class, "B"));
    }

    @Test
    public void testGetEnumWithEmptyMappingAndValues() {
        assertNull(new CSVRecord(new String[0], null, null, 0).get(Enum.class, "A"));
    }

    @Test
    public void testGetByIndexWithNullValues() {
        CSVRecord csvRecord = new CSVRecord(null, null, null, 0);
        assertNull(csvRecord.get(0));
    }

    @Test
    public void testGetByIndexWithEmptyValues() {
        CSVRecord csvRecord = new CSVRecord(new String[0], null, null, 0);
        assertNull(csvRecord.get(0));
    }

    @Test
    public void testGetByIndexWithValues() {
        String[] values = new String[]{"ValueA", "ValueB"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 0);
        assertEquals("ValueA", csvRecord.get(0));
    }

    @Test
    public void testGetByIndexWithValuesAndRecordNumber() {
        String[] values = new String[]{"ValueA", "ValueB"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
        assertEquals("ValueB", csvRecord.get(1));
    }

    @Test
    public void testGetCommentWithNullComment() {
        CSVRecord csvRecord = new CSVRecord(new String[0], null, null, 0);
        assertNull(csvRecord.getComment());
    }

    @Test
    public void testGetCommentWithEmptyComment() {
        CSVRecord csvRecord = new CSVRecord(new String[0], null, "", 0);
        assertEquals("", csvRecord.getComment());
    }

    @Test
    public void testGetCommentWithComment() {
        CSVRecord csvRecord = new CSVRecord(new String[0], null, "Comment", 0);
        assertEquals("Comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumberWithNullRecordNumber() {
        CSVRecord csvRecord = new CSVRecord(new String[0], null, null, 0);
        assertEquals(0, csvRecord.getRecordNumber());
    }

    @Test
    public void testGetRecordNumberWithRecordNumber() {
        CSVRecord csvRecord = new CSVRecord(new String[0], null, null, 1);
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIterator() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        String[] values = new String[]{"ValueA", "ValueB"};
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 0);
        Iterator<String> iterator = csvRecord.iterator();
        assertEquals("ValueA", iterator.next());
        assertEquals("ValueB", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutInWithEmptyMap() {
        Map<String, String> map = new HashMap<>();
        CSVRecord csvRecord = new CSVRecord(new String[]{"ValueA", "ValueB"}, null, null, 0);
        assertEquals(map, csvRecord.putIn(map));
        assertEquals("ValueA", map.get("ValueA"));
        assertEquals("ValueB", map.get("ValueB"));
    }

    @Test
    public void testPutInWithNonEmptyMap() {
        Map<String, String> map = new HashMap<>();
        map.put("A", "ValueA");
        map.put("B", "ValueB");
        CSVRecord csvRecord = new CSVRecord(new String[]{"ValueA", "ValueB", "ValueC"}, null, null, 0);
        assertEquals(map, csvRecord.putIn(map));
        assertEquals("ValueA", map.get("ValueA"));
        assertEquals("ValueB", map.get("ValueB"));
        assertEquals("ValueC", map.get("ValueC"));
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"ValueA", "ValueB"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 0);
        assertEquals("[ValueA, ValueB]", csvRecord.toString());
    }

    @Test
    public void testValues() {
        String[] values = new String[]{"ValueA", "ValueB"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 0);
        assertArrayEquals(values, csvRecord.values());
    }
}