package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    /**
     * Test constructor.
     */
    @Test
    public void testCSVRecord() {
        String[] values = new String[] { "Name", "Age", "City" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(1, record.getRecordNumber());
        assertArrayEquals(values, record.values());
        assertSame(mapping, record.mapping);
        assertNull(record.getComment());

        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, null, null, 0);
        assertEquals(0, emptyRecord.getRecordNumber());
        assertArrayEquals(emptyValues, emptyRecord.values());
        assertNull(emptyRecord.mapping);
        assertNull(emptyRecord.getComment());

        // Test null mapping
        String[] nullValues = new String[] { "Name", "Age" };
        CSVRecord nullMappingRecord = new CSVRecord(nullValues, null, null, 1);
        assertArrayEquals(nullValues, nullMappingRecord.values());
        assertNull(nullMappingRecord.mapping);
        assertNull(nullMappingRecord.getComment());
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

        assertNull(record.get(-1));
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

        Map<String, Integer> inconsistentMapping = new HashMap<>();
        inconsistentMapping.put("Name", 0);
        inconsistentMapping.put("City", 1);
        inconsistentMapping.put("Age", 2);

        CSVRecord inconsistentRecord = new CSVRecord(values, inconsistentMapping, null, 1);
        assertFalse(inconsistentRecord.isConsistent());

        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, null, null, 1);
        assertTrue(emptyRecord.isConsistent());

        String[] valuesWithExtra = new String[] { "Name", "Age", "City", "Unknown" };
        Map<String, Integer> mappingWithExtra = new HashMap<>();
        mappingWithExtra.put("Name", 0);
        mappingWithExtra.put("Age", 1);
        mappingWithExtra.put("City", 2);

        CSVRecord recordWithExtra = new CSVRecord(valuesWithExtra, mappingWithExtra, null, 1);
        assertFalse(recordWithExtra.isConsistent());
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

        Map<String, Integer> emptyMapping = new HashMap<>();
        CSVRecord emptyRecord = new CSVRecord(values, emptyMapping, null, 1);
        assertFalse(emptyRecord.isMapped("Name"));
    }

    /**
     * Test isSet method.
     */
    @Test
    public void testIsSet() {
        String[] values = new String[] { "Name:John", "Age:30", "City:New York" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));

        assertFalse(record.isSet("Unknown"));

        Map<String, Integer> emptyMapping = new HashMap<>();
        CSVRecord emptyRecord = new CSVRecord(values, emptyMapping, null, 1);
        assertFalse(emptyRecord.isSet("Name"));

        // Test empty value
        String[] emptyValues = new String[0];
        CSVRecord emptyValuesRecord = new CSVRecord(emptyValues, null, null, 1);
        assertFalse(emptyValuesRecord.isSet("Name"));
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

        Iterator<String> iterator = record.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            assertEquals(values[i++], iterator.next());
        }
    }

    /**
     * Test values method.
     */
    @Test
    public void testValues() {
        String[] values = new String[] { "Name", "Age", "City" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertArrayEquals(values, record.values());

        String[] emptyValues = new String[0];
        CSVRecord emptyValuesRecord = new CSVRecord(emptyValues, null, null, 1);
        assertArrayEquals(emptyValues, emptyValuesRecord.values());
    }

    /**
     * Test getComment method.
     */
    @Test
    public void testGetComment() {
        String[] values = new String[] { "Name", "Age", "City" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, "Comment", 1);
        assertEquals("Comment", record.getComment());

        CSVRecord emptyCommentRecord = new CSVRecord(values, mapping, null, 1);
        assertNull(emptyCommentRecord.getComment());
    }

    /**
     * Test getRecordNumber method.
     */
    @Test
    public void testGetRecordNumber() {
        String[] values = new String[] { "Name", "Age", "City" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(1, record.getRecordNumber());

        CSVRecord emptyRecord = new CSVRecord(values, mapping, null, 0);
        assertEquals(0, emptyRecord.getRecordNumber());
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        String[] values = new String[] { "Name", "Age", "City" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(3, record.size());

        String[] emptyValues = new String[0];
        CSVRecord emptyValuesRecord = new CSVRecord(emptyValues, null, null, 1);
        assertEquals(0, emptyValuesRecord.size());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        String[] values = new String[] { "Name", "Age", "City" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("[Name, Age, City]", record.toString());

        CSVRecord emptyValuesRecord = new CSVRecord(new String[0], null, null, 1);
        assertEquals("[]", emptyValuesRecord.toString());
    }
}