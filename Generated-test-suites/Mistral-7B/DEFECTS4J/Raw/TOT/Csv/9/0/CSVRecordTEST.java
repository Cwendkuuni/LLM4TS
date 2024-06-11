package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithValidData() {
        String[] values = new String[] { "John Doe", "john.doe@example.com", "45" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);
        mapping.put("Age", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("John Doe", record.get("Name"));
        assertEquals("john.doe@example.com", record.get("Email"));
        assertEquals("45", record.get("Age"));
        assertEquals("45", record.get(2));
        assertEquals("45", record.get(new Integer(2)));
        assertSame(mapping, record.mapping);
        assertNull(record.comment);
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Email"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Email"));
        assertTrue(record.isSet("Age"));
        assertEquals(3, record.size());
        List<String> valuesList = record.toList();
        assertEquals(values[0], valuesList.get(0));
        assertEquals(values[1], valuesList.get(1));
        assertEquals(values[2], valuesList.get(2));
    }

    @Test
    public void testCSVRecordWithInvalidData() {
        String[] values = new String[] { "John Doe", "john.doe@example.com", "45" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);
        mapping.put("Age", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        try {
            record.get("InvalidName");
            assert false : "Should not reach this point.";
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            record.get(10);
            assert false : "Should not reach this point.";
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }

        try {
            record = new CSVRecord(values, null, null, 1);
            assert false : "Should not reach this point.";
        } catch (IllegalStateException e) {
            // Expected exception
        }

        try {
            record.getComment();
            assert false : "Should not reach this point.";
        } catch (NullPointerException e) {
            // Expected exception
        }

        try {
            CSVRecord recordWithInvalidData = new CSVRecord(new String[0], mapping, null, 1);
            assert false : "Should not reach this point.";
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            CSVRecord recordWithInvalidMapping = new CSVRecord(values, null, null, 1);
            assert false : "Should not reach this point.";
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }
}