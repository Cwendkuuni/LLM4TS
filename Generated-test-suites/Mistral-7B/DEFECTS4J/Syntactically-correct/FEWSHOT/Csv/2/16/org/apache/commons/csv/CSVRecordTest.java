package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithValues() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        String comment = "This is a comment";
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(3, csvRecord.size());
        assertEquals("Name", csvRecord.get(0));
        assertEquals("Age", csvRecord.get(1));
        assertEquals("City", csvRecord.get(2));
        assertEquals("This is a comment", csvRecord.getComment());
        assertEquals(1, csvRecord.getRecordNumber());

        Iterator<String> iterator = csvRecord.iterator();
        assertEquals("Name", iterator.next());
        assertEquals("Age", iterator.next());
        assertEquals("City", iterator.next());
    }

    @Test
    public void testCSVRecordWithEmptyValues() {
        String[] values = new String[0];
        Map<String, Integer> mapping = null;
        String comment = null;
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(0, csvRecord.size());
        assertNull(csvRecord.getComment());
        assertNull(csvRecord.get(0));
        assertNull(csvRecord.getRecordNumber());
        assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testCSVRecordWithInvalidMapping() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("City", 2);
        String comment = null;
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertFalse(csvRecord.isConsistent());
        assertNull(csvRecord.get("Age"));
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithoutMapping() {
        String[] values = new String[]{"Name", "Age", "City"};
        String comment = null;
        long recordNumber = 1;

        new CSVRecord(values, null, comment, recordNumber);
    }

    @Test
    public void testGetByIndex() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        String comment = null;
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("Name", csvRecord.get(0));
        assertEquals("Age", csvRecord.get(1));
        assertEquals("City", csvRecord.get(2));
    }

    @Test
    public void testGetByName() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        String comment = null;
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("Name", csvRecord.get("Name"));
        assertEquals("Age", csvRecord.get("Age"));
        assertEquals("City", csvRecord.get("City"));
        assertNull(csvRecord.get("InvalidName"));
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        String comment = null;
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
        assertTrue(csvRecord.isConsistent());

        String[] values2 = new String[]{"Name", "Age", "City", "InvalidColumn"};
        Map<String, Integer> mapping2 = new HashMap<>();
        mapping2.put("Name", 0);
        mapping2.put("Age", 1);
        mapping2.put("City", 2);
        CSVRecord csvRecord2 = new CSVRecord(values2, mapping2, null, 1);
        assertFalse(csvRecord2.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        String comment = null;
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertTrue(csvRecord.isMapped("Name"));
        assertTrue(csvRecord.isMapped("Age"));
        assertTrue(csvRecord.isMapped("City"));
        assertFalse(csvRecord.isMapped("InvalidName"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        String comment = null;
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertTrue(csvRecord.isSet("Name"));
        assertTrue(csvRecord.isSet("Age"));
        assertTrue(csvRecord.isSet("City"));
        assertFalse(csvRecord.isSet("InvalidName"));

        String[] values2 = new String[]{"Name", "", "City"};
        Map<String, Integer> mapping2 = new HashMap<>();
        mapping2.put("Name", 0);
        mapping2.put("Age", 1);
        mapping2.put("City", 2);
        CSVRecord csvRecord2 = new CSVRecord(values2, mapping2, null, 1);
        assertFalse(csvRecord2.isSet("Age"));
    }
}