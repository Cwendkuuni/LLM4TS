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
    public void testCSVRecordConstruction() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
        assertArrayEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertNull(record.comment);
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertFalse(record.isSet("InvalidColumnName"));
    }

    @Test
    public void testCSVRecordWithoutMapping() {
        String[] values = new String[]{"Name", "Age", "City"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals(1, record.getRecordNumber());
        assertArrayEquals(values, record.values());
        assertNull(record.mapping);
        assertNull(record.comment);
        assertTrue(record.isConsistent());
        assertFalse(record.isMapped("Name"));
        assertFalse(record.isMapped("Age"));
        assertFalse(record.isMapped("City"));
        assertFalse(record.isSet("Name"));
        assertFalse(record.isSet("Age"));
        assertFalse(record.isSet("City"));
    }

    @Test
    public void testGetValue() {
        String[] values = new String[]{"John Doe", "32", "New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("John Doe", record.get(0));
        assertEquals("32", record.get(1));
        assertEquals("New York", record.get(2));
        assertNull(record.get(3));

        assertEquals("John Doe", record.get("Name"));
        assertEquals("32", record.get("Age"));
        assertNull(record.get("InvalidColumnName"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValueWithoutMapping() {
        String[] values = new String[]{"Name", "Age", "City"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        record.get("InvalidColumnName");
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isConsistent());

        String[] valuesWithMissingColumn = new String[]{"Name", "Age"};
        CSVRecord recordWithMissingColumn = new CSVRecord(valuesWithMissingColumn, mapping, null, 1);

        assertFalse(recordWithMissingColumn.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertFalse(record.isMapped("InvalidColumnName"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertFalse(record.isSet("InvalidColumnName"));
    }

    @Test
    public void testGetComment() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, "Comment for record", 1);

        assertEquals("Comment for record", record.getComment());
        assertNull(record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
        assertNull(record.getRecordNumber());
    }

    @Test
    public void testSize() {
        String[] values = new String[]{"Name", "Age", "City"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals(3, record.size());
        assertNull(record.size());
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"Name", "Age", "City"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals("[Name, Age, City]", record.toString());
    }
}