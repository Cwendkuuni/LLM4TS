package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithEmptyValues() {
        String[] emptyValues = {};
        Map<String, Integer> emptyMapping = Collections.emptyMap();
        String emptyComment = null;
        long emptyRecordNumber = 0;

        CSVRecord record = new CSVRecord(emptyValues, emptyMapping, emptyComment, emptyRecordNumber);
        assertNull(record.getComment());
        assertEquals(0, record.getRecordNumber());
        assertTrue(record.isConsistent());
        assertFalse(record.isMapped("columnName"));
        assertFalse(record.isSet("columnName"));
        assertFalse(record.iterator().hasNext());
        assertTrue(record.toList().isEmpty());
        assertTrue(record.toMap().isEmpty());
        assertEquals("[]", record.toString());
        assertArrayEquals(new String[0], record.values());
    }

    @Test
    public void testCSVRecordWithValues() {
        String[] values = {"column1", "column2", "column3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("column1", record.get(0));
        assertEquals("column2", record.get(1));
        assertEquals("column3", record.get(2));
        assertEquals("This is a comment", record.getComment());
        assertEquals(1, record.getRecordNumber());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isSet("column1"));
        assertTrue(record.isMapped("column2"));
        assertTrue(record.isSet("column2"));
        assertTrue(record.isMapped("column3"));
        assertTrue(record.isSet("column3"));
        assertEquals(3, record.size());

        List<String> list = record.toList();
        Iterator<String> iterator = record.iterator();
        Map<String, String> map = record.toMap();

        assertEquals(Arrays.toString(values), Arrays.toString(list.toArray()));
        assertEquals(Arrays.toString(values), Arrays.toString(map.values().toArray()));
        assertEquals(map.size(), 3);
        assertEquals("map=[column1=column1, column2=column2, column3=column3]", map.toString());
        assertEquals(3, iterator.size());
        assertEquals("column1", iterator.next());
        assertEquals("column2", iterator.next());
        assertEquals("column3", iterator.next());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithUnmappedColumnName() {
        String[] values = {"column1", "column2", "column3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        String comment = null;
        long recordNumber = 0;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        record.get("column4");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetWithMappedButNotSetColumnName() {
        String[] values = {"column1", "column2", "column3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column4", 3);
        String comment = null;
        long recordNumber = 0;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        record.get("column4");
    }
}