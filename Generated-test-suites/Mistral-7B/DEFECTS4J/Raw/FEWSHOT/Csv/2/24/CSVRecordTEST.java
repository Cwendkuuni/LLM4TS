package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGet() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals("Name", record.get(0));
        assertEquals("Age", record.get(1));
        assertEquals("City", record.get(2));
        assertNull(record.get(3));
    }

    @Test
    public void testGetMappingNull() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        try {
            record.get("InvalidColumnName");
            fail();
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testGetMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("Name", record.get("Name"));
        assertEquals("Age", record.get("Age"));
        assertEquals("City", record.get("City"));
        assertNull(record.get("InvalidColumnName"));
    }

    @Test
    public void testIsConsistent() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());

        String[] wrongValues = new String[]{"Name", "Age", null, "City"};
        CSVRecord inconsistentRecord = new CSVRecord(wrongValues, mapping, null, 1);
        assertFalse(inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertFalse(record.isMapped("InvalidColumnName"));
    }

    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertFalse(record.isSet("InvalidColumnName"));

        String[] valuesWithNull = new String[]{"Name", "Age", "City", null};
        CSVRecord recordWithNull = new CSVRecord(valuesWithNull, mapping, null, 1);

        assertFalse(recordWithNull.isSet("Name"));
        assertTrue(recordWithNull.isSet("Age"));
        assertTrue(recordWithNull.isSet("City"));
        assertFalse(recordWithNull.isSet("InvalidColumnName"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        Iterator<String> iterator = record.iterator();

        assertEquals("Name", iterator.next());
        assertEquals("Age", iterator.next());
        assertEquals("City", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testValues() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        String[] recordValues = record.values();
        assertArrayEquals(values, recordValues);
    }

    @Test
    public void testGetCommentNull() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertNull(record.getComment());
    }

    @Test
    public void testGetComment() {
        String comment = "This is a comment";
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, comment, 1);

        assertEquals(comment, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals("[Name, Age, City]", record.toString());
    }
}