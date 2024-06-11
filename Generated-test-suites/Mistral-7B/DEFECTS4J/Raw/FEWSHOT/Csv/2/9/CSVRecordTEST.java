package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordEmpty() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 0);
        assertNull(record.getComment());
        assertTrue(record.isConsistent());
        assertEquals(0, record.size());
        assertEquals(0, record.getRecordNumber());
        assertArrayEquals(new String[0], record.values());
        assertFalse(record.isMapped("name"));
        assertFalse(record.isSet("name"));
        assertFalse(record.iterator().hasNext());
    }

    @Test
    public void testCSVRecordWithValues() {
        String[] values = {"Alice", "25", "Smith"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("LastName", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("Alice", record.get(0));
        assertEquals("25", record.get(1));
        assertEquals("Smith", record.get(2));
        assertEquals("Smith", record.get("LastName"));
        assertEquals("Alice", record.get("Name"));
        assertEquals("25", record.get("Age"));
        assertFalse(record.get("Unknown"), "Returned a value for unknown column");
        assertSame(mapping, record.mapping);
        assertSame(values, record.values());
        assertSame(Collections.singletonList("Name"), Arrays.asList(record.values()).subList(0, 1).iterator().asList());
        assertSame(Collections.singletonList("Age"), Arrays.asList(record.values()).subList(1, 2).iterator().asList());
        assertSame(Collections.singletonList("Smith"), Arrays.asList(record.values()).subList(2, 3).iterator().asList());
        assertTrue(record.isConsistent());
        assertEquals(3, record.size());
        assertEquals(1, record.getRecordNumber());
        assertArrayEquals(values, record.values());
        assertFalse(record.iterator().hasNext());
    }

    @Test
    public void testCSVRecordWithComment() {
        String[] values = {"Alice", "25", "Smith"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("LastName", 2);

        CSVRecord record = new CSVRecord(values, mapping, "Comment for record", 1);

        assertEquals("Comment for record", record.getComment());
    }

    @Test
    public void testCSVRecordWithNullValues() {
        String[] values = {null, "25", null};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("LastName", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertNull(record.get(0));
        assertEquals("25", record.get(1));
        assertNull(record.get(2));
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithoutMapping() {
        String[] values = {"Alice", "25", "Smith"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("Name");
    }

    @Test
    public void testCSVRecordInconsistent() {
        String[] values = {"Alice", "25", "Smith"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("LastName", 3); // wrong index

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecordToString() {
        String[] values = {"Alice", "25", "Smith"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("LastName", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("[Alice, 25, Smith]", record.toString());
    }

    @Test
    public void testCSVRecordIterator() {
        String[] values = {"Alice", "25", "Smith"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("LastName", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Iterator<String> it = record.iterator();
        List<String> expectedList = Arrays.asList(record.values());

        for (int i = 0; i < record.size(); i++) {
            assertEquals(expectedList.get(i), it.next());
        }

        assertFalse(it.hasNext());
    }

}