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
    public void testCSVRecord_Empty() {
        CSVRecord record = new CSVRecord(null, null, null, -1);
        assertTrue(record.size() == 0);
        assertFalse(record.isConsistent());
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
        assertArrayEquals(new String[0], record.values());
    }

    @Test
    public void testCSVRecord_Valid() {
        String[] values = new String[]{"Name", "Age", "Country"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Country", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.size() == values.length);
        assertFalse(record.isConsistent() == mapping.size() == values.length);
        assertNull(record.getComment());
        assertEquals(1, record.getRecordNumber());
        assertArrayEquals(values, record.values());

        assertEquals("Name", record.get(0));
        assertEquals("Age", record.get(1));
        assertEquals("Country", record.get(2));
        assertEquals("Name", record.get("Name"));
        assertEquals("Age", record.get("Age"));
        assertEquals("Country", record.get("Country"));
        assertNull(record.get("Unknown"));
        assertFalse(record.isConsistent() == mapping.size() == values.length);
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecord_NoHeaderMapping() {
        String[] values = new String[]{"Name", "Age", "Country"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("Name");
    }

    @Test
    public void testCSVRecord_IsMapped() {
        String[] values = new String[]{"Name", "Age", "Country"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Country", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("Country"));
        assertFalse(record.isMapped("Unknown"));
    }

    @Test
    public void testCSVRecord_IsSet() {
        String[] values = new String[]{"Name", "Age", "Country"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Country", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertFalse(record.isSet("Name"));
        assertFalse(record.isSet("Age"));
        assertFalse(record.isSet("Country"));

        String[] values2 = new String[]{"John Doe", "30", "USA"};
        CSVRecord record2 = new CSVRecord(values2, mapping, null, 2);
        assertTrue(record2.isSet("Name"));
        assertTrue(record2.isSet("Age"));
        assertTrue(record2.isSet("Country"));
    }

    @Test
    public void testCSVRecord_Iterator() {
        String[] values = new String[]{"Name", "Age", "Country"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Country", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Iterator<String> it = record.iterator();
        assertEquals(record.get(0), it.next());
        assertEquals(record.get(1), it.next());
        assertEquals(record.get(2), it.next());
        assertFalse(it.hasNext());
    }

}