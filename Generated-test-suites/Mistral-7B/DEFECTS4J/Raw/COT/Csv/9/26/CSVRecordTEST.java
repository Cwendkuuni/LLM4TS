package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for CSVRecord.
 */
public class CSVRecordTest {

    @Test
    public void testCSVRecord_basic() {
        String[] values = new String[]{"Alice", "25", "NY"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, record.size());
        assertEquals("Alice", record.get(0));
        assertEquals("25", record.get(1));
        assertEquals("NY", record.get(2));
        assertEquals(null, record.getComment());
        assertEquals(1, record.getRecordNumber());
        assertTrue(record.isConsistent());
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecord_noMapping() {
        String[] values = new String[]{"Alice", "25", "NY"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecord_invalidMapping() {
        String[] values = new String[]{"Alice", "25", "NY"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecord_insufficientValues() {
        String[] values = new String[]{"Alice", "25"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get("City");
    }

    @Test
    public void testCSVRecord_getEnum() {
        String[] values = new String[]{"Alice", "25", "NY"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("Alice", record.get(CSVRecord.Name.class));
        assertEquals("25", record.get(CSVRecord.Age.class));
        assertEquals("NY", record.get(CSVRecord.City.class));
    }

    @Test
    public void testCSVRecord_getComment() {
        String comment = "This is a comment.";
        String[] values = new String[]{"Alice", "25", "NY"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, comment, 1);

        assertEquals(comment, record.getComment());
    }

    @Test
    public void testCSVRecord_getRecordNumber() {
        String[] values = new String[]{"Alice", "25", "NY"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testCSVRecord_isConsistent() {
        String[] values = new String[]{"Alice", "25", "NY"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isConsistent());

        values = new String[]{"Alice", "25"};
        mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        record = new CSVRecord(values, mapping, null, 1);

        assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecord_isMapped() {
        String[] values = new String[]{"Alice", "25", "NY"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertFalse(record.isMapped("Unknown"));
    }

    @Test
    public void testCSVRecord_isSet() {
        String[] values = new String[]{"Alice", "25", "NY"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertFalse(record.isSet("Unknown"));
    }

    @Test
    public void testCSVRecord_iterator() {
        String[] values = new String[]{"Alice", "25", "NY"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        List<String> expected = Arrays.asList("Alice", "25", "NY");
        Iterator<String> iterator = record.iterator();

        for (String value : expected) {
            assertEquals(value, iterator.next());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCSVRecord_putIn() {
        String[] values = new String[]{"Alice", "25", "NY"};
        Map<String, String> result = new HashMap<>();

        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.putIn(result);

        Map<String, String> expected = new HashMap<>();
        expected.put("Name", "Alice");
        expected.put("Age", "25");
        expected.put("City", "NY");

        assertEquals(expected, result);
    }

    @Test
    public void testCSVRecord_toMap() {
        String[] values = new String[]{"Alice", "25", "NY"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Map<String, String> expected = new HashMap<>();
        expected.put("Name", "Alice");
        expected.put("Age", "25");
        expected.put("City", "NY");

        Map<String, String> result = record.toMap();

        assertEquals(expected, result);
    }

    @Test
    public void testCSVRecord_toString() {
        String[] values = new String[]{"Alice", "25", "NY"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("[Alice, 25, NY]", record.toString());
    }
}