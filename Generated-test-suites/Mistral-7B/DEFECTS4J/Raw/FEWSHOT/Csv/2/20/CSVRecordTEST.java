package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordConsistent() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());

        String[] emptyValues = new String[0];
        record = new CSVRecord(emptyValues, null, null, 1);
        assertTrue(record.isConsistent());

        String[] wrongSizeValues = new String[]{"Name", "Age", "City", "Gender"};
        mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        mapping.put("Gender", 3);
        record = new CSVRecord(values, mapping, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecordGet() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("Name", record.get(0));
        assertEquals("Age", record.get(1));
        assertEquals("City", record.get(2));

        assertNull(record.get(3));
    }

    @Test
    public void testCSVRecordGetByName() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("Name", record.get("Name"));
        assertEquals("Age", record.get("Age"));
        assertEquals("City", record.get("City"));

        assertNull(record.get("Gender"));
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordNoMapping() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordInconsistent() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get("City");
    }

    @Test
    public void testCSVRecordIsMapped() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertFalse(record.isMapped("Gender"));
    }

    @Test
    public void testCSVRecordIsSet() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertFalse(record.isSet("Gender"));
    }

    @Test
    public void testCSVRecordIterator() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        Iterator<String> iterator = record.iterator();
        assertArrayEquals(values, iterator.toArray(new String[0]));
    }

    @Test
    public void testCSVRecordValues() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertArrayEquals(values, record.values());
    }

    @Test
    public void testCSVRecordComment() {
        String comment = "Record comment";
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, comment, 1);
        assertEquals(comment, record.getComment());
    }

    @Test
    public void testCSVRecordRecordNumber() {
        long recordNumber = 1;
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testCSVRecordSize() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testCSVRecordToString() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals("[Name, Age, City]", record.toString());
    }
}