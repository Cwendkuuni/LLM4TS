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
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecord_AllArgs() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        String comment = "This is a test record";
        long recordNumber = 123L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertEquals(comment, record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
        assertEquals(3, record.size());
        assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecord_NoArgs() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 0L);
        assertEquals(0, record.size());
        assertNull(record.mapping);
        assertNull(record.getComment());
        assertEquals(0L, record.getRecordNumber());
    }

    @Test
    public void testGet_Index() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        String comment = "This is a test record";
        long recordNumber = 123L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("Name", record.get(0));
        assertEquals("Age", record.get(1));
        assertEquals("City", record.get(2));
        assertNull(record.get(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_Index_OutOfBounds() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        String comment = "This is a test record";
        long recordNumber = 123L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        record.get(3);
    }

    @Test
    public void testGet_Name() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        String comment = "This is a test record";
        long recordNumber = 123L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("Name", record.get("Name"));
        assertEquals("Age", record.get("Age"));
        assertEquals("City", record.get("City"));
        assertNull(record.get("Unknown"));
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        assertTrue(record.isConsistent());

        mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 3);
        record = new CSVRecord(values, mapping, null, 0L);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertFalse(record.isMapped("Unknown"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertFalse(record.isSet("Unknown"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"Name", "Age", "City"};

        CSVRecord record = new CSVRecord(values, null, null, 0L);

        List<String> expected = Arrays.asList(values);

        Iterator<String> iterator = record.iterator();

        List<String> actual = new ArrayList<>();
        while (iterator.hasNext()) {
            actual.add(iterator.next());
        }

        assertArrayEquals(expected.toArray(new String[0]), actual.toArray(new String[0]));
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"Name", "Age", "City"};

        CSVRecord record = new CSVRecord(values, null, null, 0L);

        assertEquals("[Name, Age, City]", record.toString());
    }

}