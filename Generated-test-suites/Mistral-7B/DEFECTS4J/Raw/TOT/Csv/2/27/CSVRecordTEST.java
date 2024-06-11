package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * CSVRecordTest class.
 */
public class CSVRecordTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        String[] values = new String[] { "Name", "Age", "City" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(1, record.getRecordNumber());
        assertEquals(3, record.size());
        assertNull(record.getComment());

        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, null, null, 0);
        assertEquals(0, emptyRecord.getRecordNumber());
        assertEquals(0, emptyRecord.size());
        assertNull(emptyRecord.getComment());
    }

    /**
     * Test get method.
     */
    @Test
    public void testGet() {
        String[] values = new String[] { "Name", "Age", "City" };
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

    /**
     * Test get method with name.
     */
    @Test
    public void testGetByName() {
        String[] values = new String[] { "Name", "Age", "City" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("Name", record.get("Name"));
        assertEquals("Age", record.get("Age"));
        assertEquals("City", record.get("City"));
        assertNull(record.get("Unknown"));
    }

    /**
     * Test isConsistent method.
     */
    @Test
    public void testIsConsistent() {
        String[] values = new String[] { "Name", "Age", "City" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());

        String[] valuesInconsistent = new String[] { "Name", "Age" };
        Map<String, Integer> mappingInconsistent = new HashMap<>();
        mappingInconsistent.put("Name", 0);
        mappingInconsistent.put("City", 1);
        CSVRecord recordInconsistent = new CSVRecord(valuesInconsistent, mappingInconsistent, null, 1);
        assertFalse(recordInconsistent.isConsistent());
    }

    /**
     * Test isMapped method.
     */
    @Test
    public void testIsMapped() {
        String[] values = new String[] { "Name", "Age", "City" };
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

    /**
     * Test isSet method.
     */
    @Test
    public void testIsSet() {
        String[] values = new String[] { "Name", "Age", "City" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertFalse(record.isSet("Unknown"));

        String[] valuesEmpty = new String[0];
        Map<String, Integer> mappingEmpty = new HashMap<>();
        mappingEmpty.put("Name", 0);
        mappingEmpty.put("Age", 1);
        mappingEmpty.put("City", 2);
        CSVRecord recordEmpty = new CSVRecord(valuesEmpty, mappingEmpty, null, 1);
        assertFalse(recordEmpty.isSet("Name"));
        assertFalse(recordEmpty.isSet("Age"));
        assertFalse(recordEmpty.isSet("City"));
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIterator() {
        String[] values = new String[] { "Name", "Age", "City" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        List<String> valuesList = Arrays.asList(record.values());
        Iterator<String> iterator = record.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            assertEquals(valuesList.get(i++), iterator.next());
        }
    }
}