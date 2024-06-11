package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecord() {
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        String[] values = new String[]{"John Doe", "30", "New York"};
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals(1L, record.getRecordNumber());
        assertArrayEquals(new String[]{"John Doe", "30", "New York"}, record.values());
        assertEquals("John Doe", record.get(0));
        assertEquals("30", record.get(1));
        assertEquals("New York", record.get(2));
        assertEquals("New York", record.get("City"));
        assertEquals("30", record.get("Age"));
        assertEquals(null, record.get("Unknown"));
        assertEquals("John Doe,30,New York", record.toString());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));

        Iterator<String> it = record.iterator();
        assertSame("John Doe", it.next());
        assertSame("30", it.next());
        assertSame("New York", it.next());
        assertNull(it.next());
    }

    @Test
    public void testCSVRecordEmpty() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 1L);

        assertEquals(1L, record.getRecordNumber());
        assertArrayEquals(new String[0], record.values());
        assertEquals(null, record.get(0));
        assertEquals(null, record.get(1));
        assertEquals(null, record.get(2));
        assertEquals(null, record.get("Name"));
        assertEquals(null, record.get("Age"));
        assertEquals(null, record.get("City"));
        assertEquals("", record.toString());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("Name"));
        assertFalse(record.isMapped("Unknown"));
        assertFalse(record.isSet("Name"));
        assertFalse(record.isSet("Age"));
        assertFalse(record.isSet("City"));
        assertFalse(record.hasComment());
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordNoMapping() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        record.get("Name");
    }

    @Test
    public void testCSVRecordInconsistent() {
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1);
        String[] values = new String[]{"John Doe", "30", "New York", "City"};
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecordComment() {
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        String[] values = new String[]{"John Doe", "30", "New York"};
        String comment = "Record with comment";
        CSVRecord record = new CSVRecord(values, mapping, comment, 1L);

        assertEquals(comment, record.getComment());
    }

}